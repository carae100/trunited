package io.github.dracosomething.trawakened.ability.skill.unique;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.entity.magic.barrier.HolyFieldEntity;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.AwakenedFearCapability;
import io.github.dracosomething.trawakened.entity.barrier.IntruderBarrier;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.checkerframework.checker.units.qual.C;

import java.util.List;

public class Alternate extends Skill {
    public Alternate() {
        super(SkillType.UNIQUE);
    }

    @Override
    public int modes() {
        return 3;
    }

    @Override
    public int nextMode(LivingEntity entity, TensuraSkillInstance instance, boolean reverse) {
        int var10000;
        CompoundTag tag = instance.getOrCreateTag();
        if (reverse) {
            switch (instance.getMode()) {
                case 1 -> var10000 = AwakenedFearCapability.getScared(entity) >= tag.getInt("original_scared")+10 ? 3 : 2;
                case 2 -> var10000 = 1;
                case 3 -> var10000 = 2;
                default -> var10000 = 0;
            }

            return var10000;
        } else {
            switch (instance.getMode()) {
                case 1 -> var10000 = 2;
                case 2 -> var10000 = AwakenedFearCapability.getScared(entity) >= tag.getInt("original_scared")+10 ? 3 : 1;
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
        if (!SkillHelper.outOfMagicule(entity, 100)) {
            IntruderBarrier holyField = new IntruderBarrier(target.level, target);
            holyField.setRadius(50.0F);
            holyField.setLife(-1);
            holyField.setPos(target.position().add(0.0, -25.0, 0.0));
            target.level.addFreshEntity(holyField);
            tag.putInt("original_scared", AwakenedFearCapability.getScared(target));
            AwakenedFearCapability.setAlternateO(entity, target);
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
                }
                break;
            case 2:
                if (AwakenedFearCapability.getScared(living) >= 3) {
                    if (living.getUUID().equals(AwakenedFearCapability.getAlternateO(entity))) {
                        if (entity instanceof Player player) {
                            player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", new Object[]{AwakenedFearCapability.getFearType(entity).getEffect()}, new Object[]{living}), false);
                            player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", new Object[]{AwakenedFearCapability.getFearType(entity).getItem()}, new Object[]{living}), false);
                            player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", new Object[]{AwakenedFearCapability.getFearType(entity).getEntity()}, new Object[]{living}), false);
                            player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", new Object[]{AwakenedFearCapability.getFearType(entity).getBlock()}, new Object[]{living}), false);
                        }
                    }
                }
                break;
            case 3:
                if (AwakenedFearCapability.getScared(entity) >= 13) {

                }
        }
    }

    @Override
    public void onTick(ManasSkillInstance instance, LivingEntity living) {
        living.addEffect(new MobEffectInstance(TensuraMobEffects.PRESENCE_CONCEALMENT.get(), 5, 255, false, false, false));
    }

    @Override
    public void onLearnSkill(ManasSkillInstance instance, LivingEntity living, UnlockSkillEvent event) {
        if (living instanceof Player player) {
            player.displayClientMessage(Component.translatable("trawakened.fear.learn_self", new Object[]{AwakenedFearCapability.getFearType(player).toString()}).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)), false);
        }
    }
}
