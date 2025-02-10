package io.github.dracosomething.trawakened.ability.skill.unique;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.entity.magic.barrier.HolyFieldEntity;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.util.damage.TensuraDamageSource;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.dracosomething.trawakened.api.FearTypes;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.AwakenedFearCapability;
import io.github.dracosomething.trawakened.entity.barrier.IntruderBarrier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.checkerframework.checker.units.qual.C;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Alternate extends Skill {
    public Alternate() {
        super(SkillType.UNIQUE);
    }

    @Override
    public int modes() {
        return 4;
    }

    @Override
    public double magiculeCost(LivingEntity entity, ManasSkillInstance instance) {
        double var10000;
        CompoundTag tag = instance.getOrCreateTag();
        switch (instance.getMode()) {
            case 1 -> var10000 = 0;
            case 2 -> var10000 = 0;
            case 3 -> var10000 = 1000;
            default -> var10000 = 0;
        }

        return var10000;
    }

    @Override
    public Component getModeName(int mode) {
        MutableComponent var10000;
        switch (mode) {
            case 1 -> var10000 = Component.translatable("trawakened.skill.mode.alternate.fear");
            case 2 -> var10000 = Component.translatable("trawakened.skill.mode.alternate.scary");
            case 3 -> var10000 = Component.translatable("trawakened.skill.mode.alternate.kill");
            default -> var10000 = Component.empty();
        }

        return var10000;
    }

    @Override
    public int nextMode(LivingEntity entity, TensuraSkillInstance instance, boolean reverse) {
        int var10000;
        CompoundTag tag = instance.getOrCreateTag();
        if (reverse) {
            switch (instance.getMode()) {
                case 1 -> var10000 = 3;
                case 2 -> var10000 = 1;
                case 3 -> var10000 = 2;
                default -> var10000 = 0;
            }

            return var10000;
        } else {
            switch (instance.getMode()) {
                case 1 -> var10000 = 2;
                case 2 -> var10000 = 3;
                case 3 -> var10000 = 1;
                default -> var10000 = 0;
            }

            return var10000;
        }
    }

    @Override
    public List<MobEffect> getImmuneEffects(ManasSkillInstance instance, LivingEntity entity) {
        return List.of(MobEffects.GLOWING);
    }

    @Override
    public void onDamageEntity(ManasSkillInstance instance, LivingEntity entity, LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        CompoundTag tag = instance.getOrCreateTag();
        if (!SkillHelper.outOfMagicule(entity, 100) && !target.getPersistentData().hasUUID("alternate_UUID") && tag.getBoolean("is_locked")) {
            target.getPersistentData().putUUID("alternate_UUID", entity.getUUID());
            IntruderBarrier holyField = new IntruderBarrier(target.level, target);
            System.out.println(holyField);
            holyField.setRadius(25.0F);
            holyField.setLife(-1);
            holyField.setPos(target.position().add(0.0, -12.5, 0.0));
            target.level.addFreshEntity(holyField);
            tag.putInt("original_scared", AwakenedFearCapability.getScared(target));
            tag.putBoolean("is_locked", true);
            if (AwakenedFearCapability.getScared(target) >= 3) {
                if (entity instanceof Player player) {
                    player.displayClientMessage(Component.translatable("trawakened.fear.learn", new Object[]{AwakenedFearCapability.getFearType(target).toString()}).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)), false);
                }
            }
        }
    }

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        LivingEntity living = SkillHelper.getTargetingEntity(entity, ForgeMod.REACH_DISTANCE.get().getDefaultValue(), true);
        CompoundTag tag = instance.getOrCreateTag();
        switch (instance.getMode()) {
            case 1:
                if (AwakenedFearCapability.getScared(living) >= 3) {
                    if (entity instanceof Player player) {
                        player.displayClientMessage(Component.translatable("trawakened.fear.scared", new Object[]{AwakenedFearCapability.getScared(living)}).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)), false);
                    }
                } else {
                    if (entity instanceof Player player) {
                        player.displayClientMessage(Component.translatable("trawakened.fear.brave").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                    }
                }
                break;
            case 2:
                if (AwakenedFearCapability.getScared(living) >= 3) {
                    if (living.getPersistentData().getUUID("alternate_UUID").equals(entity.getUUID())) {
                        if (entity instanceof Player player) {
                            player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", AwakenedFearCapability.getFearType(living).getItem().toString().replace("[", "").replace("]", ""), living.getName()), false);
                            player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", AwakenedFearCapability.getFearType(living).getBlock().toString().replace("[", "").replace("Block{", "").replace("}", "").replace("]", ""), living.getName()), false);
                            player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", AwakenedFearCapability.getFearType(living).getEntity().toString().replace("[", "").replace("]", ""), living.getName()), false);
                            AwakenedFearCapability.getFearType(living).getEffect().forEach((effect) -> {
                                player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", effect.getDisplayName(), living.getName()), false);
                            });
                        }
                    }
                } else {
                    if (entity instanceof Player player) {
                        player.displayClientMessage(Component.translatable("trawakened.fear.brave").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                    }
                }
                break;
            case 3:
                if (!SkillHelper.outOfMagicule(entity, instance)) {
                    if (AwakenedFearCapability.getScared(living) >= (tag.getInt("original_scared") + 10) || AwakenedFearCapability.getFearType(living).equals(FearTypes.TRUTH)) {
                        living.hurt(TensuraDamageSources.insanity(living).bypassArmor().bypassMagic().bypassEnchantments().bypassInvul(), living.getMaxHealth() * 10);
                        if (living.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES)) {
                            Iterator var9 = living.level.players().iterator();

                            while (var9.hasNext()) {
                                Player everyone = (Player) var9.next();
                                if (everyone != entity) {
                                    everyone.sendSystemMessage(Component.translatable("trawakened.fake_attack.suicide", new Object[]{living}));
                                }
                            }
                        }
                    } else {
                        if (entity instanceof Player player) {
                            player.displayClientMessage(Component.translatable("trawakened.fear.brave").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public boolean canTick(ManasSkillInstance instance, LivingEntity entity) {
        return true;
    }

    @Override
    public void onTick(ManasSkillInstance instance, LivingEntity living) {
        living.addEffect(new MobEffectInstance(TensuraMobEffects.PRESENCE_CONCEALMENT.get(), 120, 255, false, false, false));
        if (living instanceof Player player) {
            if (!player.getAbilities().mayfly && !player.getAbilities().invulnerable && player.getAbilities().mayBuild) {
                player.getAbilities().mayfly = true;
                player.getAbilities().invulnerable = true;
                player.getAbilities().mayBuild = false;
                player.onUpdateAbilities();
            }
        }
    }

    @Override
    public void onDeath(ManasSkillInstance instance, LivingDeathEvent event) {
        CompoundTag tag = instance.getOrCreateTag();
        tag.putInt("original_scared", 0);
        tag.putBoolean("is_locked", false);
    }

    @Override
    public void onLearnSkill(ManasSkillInstance instance, LivingEntity living, UnlockSkillEvent event) {
        if (living instanceof Player player) {
            player.displayClientMessage(Component.translatable("trawakened.fear.learn_self", new Object[]{AwakenedFearCapability.getFearType(player).toString()}).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)), false);
        }
    }
}
