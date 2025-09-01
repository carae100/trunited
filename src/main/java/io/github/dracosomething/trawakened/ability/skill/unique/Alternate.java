package io.github.dracosomething.trawakened.ability.skill.unique;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.entity.human.OtherworlderEntity;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.dracosomething.trawakened.helper.MathHelper;
import io.github.dracosomething.trawakened.library.AlternateType;
import io.github.dracosomething.trawakened.library.FearTypes;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.AwakenedFearCapability;
import io.github.dracosomething.trawakened.entity.barrier.IntruderBarrier;
import io.github.dracosomething.trawakened.helper.FearHelper;
import io.github.dracosomething.trawakened.helper.skillHelper;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.world.trawakenedGamerules;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.*;

public class Alternate extends Skill {
    public Alternate() {
        super(SkillType.UNIQUE);
    }

    @Override
    public int modes() {
        return 11;
    }

    @Override
    public double magiculeCost(LivingEntity entity, ManasSkillInstance instance) {
        double var10000;
        CompoundTag tag = instance.getOrCreateTag();
        switch (instance.getMode()) {
            case 3, 11 -> var10000 = 1000;
            case 7 -> var10000 = 100;
            case 4 -> var10000 = 250;
            case 8 -> var10000 = 105;
            case 9 -> var10000 = 200;
            case 10 -> var10000 = 500;
            default -> var10000 = 0;
        }

        return var10000;
    }

    @Override
    public Component getModeName(int mode) {
        MutableComponent var10000;
        switch (mode) {
            case 1, 5 -> var10000 = Component.translatable("trawakened.skill.mode.alternate.fear");
            case 2, 6-> var10000 = Component.translatable("trawakened.skill.mode.alternate.scary");
            case 3, 7 -> var10000 = Component.translatable("trawakened.skill.mode.alternate.kill");
            case 4 -> var10000 = Component.translatable("trawakened.skill.mode.alternate.new_fear");
            case 8 -> var10000 = Component.translatable("trawakened.skill.mode.alternate.scare");
            case 9 -> var10000 = Component.translatable("trawakened.skill.mode.alternate.hideous_laughter");
            case 10 -> var10000 = Component.translatable("trawakened.skill.mode.alternate.alternate_mode");
            case 11 -> var10000 = Component.translatable("trawakened.skill.mode.alternate.intruder_mode");
            default -> var10000 = Component.empty();
        }

        return var10000;
    }

    @Override
    public int nextMode(LivingEntity entity, TensuraSkillInstance instance, boolean reverse) {
        int var10000;
        CompoundTag tag = instance.getOrCreateTag();
        if (!AwakenedFearCapability.GetIsAlternate(entity)) {
            if (reverse) {
                switch (instance.getMode()) {
                    case 1 -> var10000 = instance.isMastered(entity) ? 11 : 3;
                    case 2 -> var10000 = 1;
                    case 3 -> var10000 = 2;
                    default -> var10000 = 0;
                }

                return var10000;
            } else {
                switch (instance.getMode()) {
                    case 1 -> var10000 = 2;
                    case 2 -> var10000 = 3;
                    case 3 -> var10000 = instance.isMastered(entity) ? 4 : 1;
                    default -> var10000 = 0;
                }

                return var10000;
            }
        } else {
            if (reverse) {
                switch (instance.getMode()) {
                    case 4 -> var10000 = instance.isMastered(entity) ? entity.hasEffect(effectRegistry.INTRUDER_MODE.get()) ? 3 : 11 : switch (AlternateType.Assimilation.fromNBT(instance.getOrCreateTag().getCompound("assimilation"))) {
                        case OVERDRIVEN -> 9;
                        case FLAWED -> 8;
                        case COMPLETE -> 10;
                        default -> 7;
                    };
                    case 5 -> var10000 = 4;
                    case 6 -> var10000 = 5;
                    case 7 -> var10000 = 6;
                    case 8, 9, 10 -> var10000 = 7;
                    case 11 -> var10000 = switch (AlternateType.Assimilation.fromNBT(instance.getOrCreateTag().getCompound("assimilation"))) {
                        case OVERDRIVEN -> 9;
                        case FLAWED -> 8;
                        case COMPLETE -> 10;
                        default -> 7;
                    };
                    default -> var10000 = 0;
                }

                return var10000;
            } else {
                switch (instance.getMode()) {
                    case 4 -> var10000 = 5;
                    case 5 -> var10000 = 6;
                    case 6 -> var10000 = 7;
                    case 7 -> var10000 = switch (AlternateType.Assimilation.fromNBT(instance.getOrCreateTag().getCompound("assimilation"))) {
                        case FLAWED -> 8;
                        case COMPLETE -> 10;
                        case OVERDRIVEN -> 9;
                        default -> 4;
                    };
                    case 8, 9, 10 -> var10000 = instance.isMastered(entity) ? 11 : 4;
                    case 11 -> var10000 = entity.hasEffect(effectRegistry.INTRUDER_MODE.get()) ? 1 : 4;
                    default -> var10000 = 0;
                }
                return var10000;
            }
        }
    }

    @Override
    public List<MobEffect> getImmuneEffects(ManasSkillInstance instance, LivingEntity entity) {
        if (!AwakenedFearCapability.GetIsAlternate(entity)) {
            return List.of(MobEffects.GLOWING);
        } else {
            return List.of();
        }
    }

    public void attach(LivingEntity target, CompoundTag tag, LivingEntity entity) {
        if (!target.getPersistentData().hasUUID("alternate_UUID")) {
            if (!SkillHelper.outOfMagicule(entity, 100)) {
                target.getPersistentData().putUUID("alternate_UUID", entity.getUUID());
                IntruderBarrier holyField = new IntruderBarrier(target.level, target);
                holyField.setRadius(25.0F);
                holyField.setLife(-1);
                holyField.setPos(target.position().add(0.0, -12.5, 0.0));
                target.level.addFreshEntity(holyField);
                tag.putUUID("field_uuid", holyField.getUUID());
                tag.putInt("original_scared", AwakenedFearCapability.getScared(target));
                tag.putUUID("target_uuid", target.getUUID());
                if (entity instanceof Player player) {
                    player.displayClientMessage(Component.translatable("trawakened.fear.scared", new Object[]{AwakenedFearCapability.getScared(target)}).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)), false);
                    player.displayClientMessage(Component.translatable("trawakened.fear.learn", new Object[]{AwakenedFearCapability.getFearType(target).toString()}).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)), false);
                }
            }
        }
    }

    public void removeAttachment(CompoundTag tag, LivingEntity entity, LivingEntity target) {
        if (tag.contains("target_uuid") && tag.contains("field_uuid") && target.getPersistentData().contains("alternate_UUID")) {
            Level level = entity.getLevel();
            if (level instanceof ServerLevel serverLevel) {
                Entity entity1 = serverLevel.getEntity(tag.getUUID("target_uuid"));
                if (entity1 == null) return;
                entity1.getPersistentData().remove("alternate_UUID");
                Entity holyfield = serverLevel.getEntity(tag.getUUID("field_uuid"));
                if (holyfield == null) return;
                holyfield.remove(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    public void onDamageEntity(ManasSkillInstance instance, LivingEntity entity, LivingHurtEvent event) {
        if (!AwakenedFearCapability.GetIsAlternate(entity)) {
            LivingEntity target = event.getEntity();
            CompoundTag tag = instance.getOrCreateTag();
            removeAttachment(tag, entity, target);
            attach(target, tag, entity);
            event.setCanceled(true);
        }
    }

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        LivingEntity living = SkillHelper.getTargetingEntity(entity, ForgeMod.REACH_DISTANCE.get().getDefaultValue(), true);
        CompoundTag tag = instance.getOrCreateTag();
        switch (instance.getMode()) {
            case 1:
                if (living == null) return;
                if (AwakenedFearCapability.getScared(living) >= 3) {
                    AwakenedFearCapability.increaseScared(living);
                    FearHelper.fearPenalty(living, AwakenedFearCapability.getScared(living));
                    instance.setCoolDown(12000);
                    if (MathHelper.RandomChance(25)) {
                        instance.addMasteryPoint(entity);
                    }
                } else {
                    if (entity instanceof Player player) {
                        player.displayClientMessage(Component.translatable("trawakened.fear.brave").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                    }
                }
                break;
            case 2:
                if (living.getPersistentData().getUUID("alternate_UUID").equals(entity.getUUID())) {
                    if (entity instanceof Player player) {
                        player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", AwakenedFearCapability.getFearType(living).getItem().toString().replace("[", "").replace("]", ""), living.getName()), false);
                        player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", AwakenedFearCapability.getFearType(living).getBlock().toString().replace("[", "").replace("Block{", "").replace("}", "").replace("]", ""), living.getName()), false);
                        player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", AwakenedFearCapability.getFearType(living).getEntity().toString().replace("[", "").replace("]", ""), living.getName()), false);
                        AwakenedFearCapability.getFearType(living).getEffect().forEach((effect) -> {
                            player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", effect.getDisplayName(), living.getName()), false);
                        });
                    }
                    instance.setCoolDown(5);
                    if (MathHelper.RandomChance(25)) {
                        instance.addMasteryPoint(entity);
                    }
                }
                break;
            case 3:
                if (!SkillHelper.outOfMagicule(entity, instance)) {
                    if (AwakenedFearCapability.getScared(living) >= (tag.getInt("original_scared") + 10) || AwakenedFearCapability.getFearType(living).equals(FearTypes.TRUTH)) {
                        living.hurt(TensuraDamageSources.insanity(living).bypassArmor().bypassMagic().bypassEnchantments().bypassInvul(), living.getMaxHealth() * 10);
                        if (!(entity.hasEffect(effectRegistry.INTRUDER_MODE.get()))) {
                            AwakenedFearCapability.SetIsAlternate(entity, true);
                            entity.removeEffect(TensuraMobEffects.PRESENCE_CONCEALMENT.get());
                            if (entity instanceof Player) {
                                this.CopySkills(living, (Player) entity);
                            }
                            if (entity instanceof Player player) {
                                if (!player.isCreative()) {
                                    player.getAbilities().mayfly = false;
                                    player.getAbilities().invulnerable = false;
                                    player.getAbilities().mayBuild = true;
                                    player.onUpdateAbilities();
                                }
                            }
                            AlternateType.Assimilation assimilation = AlternateType.Assimilation.getRandomAssimilation();
                            tag.put("assimilation", assimilation.toNBT());
                            tag.put("alternate_type", assimilation.getType().toNBT());
                            instance.setMode(4);
                        } else if (living instanceof OtherworlderEntity) {
                            tag.putBoolean("awakening", true);
                        }
                        if (living.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES)) {
                            Iterator var9 = living.level.players().iterator();

                            while (var9.hasNext()) {
                                Player everyone = (Player) var9.next();
                                if (everyone != entity) {
                                    everyone.sendSystemMessage(Component.translatable("trawakened.fake_attack.suicide", new Object[]{living}));
                                }
                            }
                        }
                        instance.setCoolDown(1000);
                        instance.addMasteryPoint(entity);
                    } else {
                        if (entity instanceof Player player) {
                            player.displayClientMessage(Component.translatable("trawakened.fear.brave").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                        }
                    }
                }
                break;
            case 4:
                if (!SkillHelper.outOfMagicule(entity, instance)) {
                    if (AwakenedFearCapability.getScared(living) >= 3) {
                        AwakenedFearCapability.setScared(living, 0);
                        AwakenedFearCapability.setScaredCooldown(living, 0);
                        AwakenedFearCapability.setFearType(living, FearTypes.getRandom());
                        if (entity instanceof Player player) {
                            player.displayClientMessage(Component.translatable("trawakened.fear.learn", new Object[]{AwakenedFearCapability.getFearType(living).toString()}).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)), false);
                        }
                        instance.setCoolDown(20);
                        if (MathHelper.RandomChance(15)) {
                            instance.addMasteryPoint(entity);
                        }
                    } else {
                        if (entity instanceof Player player) {
                            player.displayClientMessage(Component.translatable("trawakened.fear.brave").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                        }
                    }
                }
                break;
            case 5:
                if (living != null) {
                    if (!SkillHelper.outOfMagicule(entity, instance)) {
                        if (AwakenedFearCapability.getScared(living) >= 15 || AwakenedFearCapability.getFearType(living).equals(FearTypes.TRUTH)) {
                            float dmg = living.getHealth() - AwakenedFearCapability.getScared(living) * 5;
                            DamageSourceHelper.directSpiritualHurt(entity, living, TensuraDamageSources.insanity(living).bypassArmor().bypassMagic().bypassEnchantments().bypassInvul(), AwakenedFearCapability.getScared(living) * 5);
                            if (living instanceof OtherworlderEntity) {
                                if (dmg <= 0 && entity.hasEffect(effectRegistry.INTRUDER_MODE.get())) {
                                    tag.putBoolean("awakening", true);
                                }
                            }
                            if (living.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES)) {
                                Iterator var9 = living.level.players().iterator();

                                while (var9.hasNext()) {
                                    Player everyone = (Player) var9.next();
                                    if (everyone != entity) {
                                        everyone.sendSystemMessage(Component.translatable("trawakened.fake_attack.suicide", new Object[]{living}));
                                    }
                                }
                            }
                            instance.setCoolDown(250);
                            instance.addMasteryPoint(entity);
                        } else {
                            if (entity instanceof Player player) {
                                player.displayClientMessage(Component.translatable("trawakened.fear.brave").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                            }
                        }
                    }
                } else {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        player.displayClientMessage(Component.translatable("tensura.targeting.not_targeted").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                    }
                }
                break;
            case 6:
                if (!SkillHelper.outOfMagicule(entity, instance)) {
                    if (AwakenedFearCapability.getScared(living) >= 3) {
                        if (entity instanceof Player player) {
                            player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", AwakenedFearCapability.getFearType(living).getItem().toString().replace("[", "").replace("]", ""), living.getName()), false);
                            player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", AwakenedFearCapability.getFearType(living).getBlock().toString().replace("[", "").replace("Block{", "").replace("}", "").replace("]", ""), living.getName()), false);
                            player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", AwakenedFearCapability.getFearType(living).getEntity().toString().replace("[", "").replace("]", ""), living.getName()), false);
                            AwakenedFearCapability.getFearType(living).getEffect().forEach((effect) -> {
                                player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", effect.getDisplayName(), living.getName()), false);
                            });
                        }
                        instance.setCoolDown(10);
                        if (MathHelper.RandomChance(50)) {
                            instance.addMasteryPoint(entity);
                        }
                    } else {
                        if (entity instanceof Player player) {
                            player.displayClientMessage(Component.translatable("trawakened.fear.brave").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                        }
                    }
                }
                break;
            case 7:
                if (AwakenedFearCapability.getScared(living) >= 3) {
                    if (entity instanceof Player player) {
                        player.displayClientMessage(Component.translatable("trawakened.fear.scared", new Object[]{AwakenedFearCapability.getScared(living)}).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)), false);
                        player.displayClientMessage(Component.translatable("trawakened.fear.learn", new Object[]{AwakenedFearCapability.getFearType(living).toString()}).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)), false);
                    }
                    instance.setCoolDown(5);
                    if (MathHelper.RandomChance(25)) {
                        instance.addMasteryPoint(entity);
                    }
                } else {
                    if (entity instanceof Player player) {
                        player.displayClientMessage(Component.translatable("trawakened.fear.brave").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                    }
                }
                break;
            case 8:
                List<Entity> entities = skillHelper.DrawSphereAndGetEntitiesInIt(entity, 25, false);
                if (!entities.isEmpty()) {
                    if (!SkillHelper.outOfMagicule(entity, instance)) {
                        entities.forEach((Entity) -> {
                            if (Entity instanceof LivingEntity livingEntity) {
                                if (entity.level.getGameRules().getBoolean(trawakenedGamerules.NORMAL_FEAR)) {
                                    AwakenedFearCapability.setScared(livingEntity, AwakenedFearCapability.getScared(livingEntity) + 15);
                                } else {
                                    FearHelper.fearPenalty(livingEntity, AwakenedFearCapability.getScared(livingEntity) + 15);
                                }
                                SkillHelper.addEffectWithSource(livingEntity, livingEntity, MobEffects.POISON, 1000, 5, false, false, false, false);
                            }
                        });
                        if (entity instanceof Player) {
                            Player player = (Player) entity;
                            player.displayClientMessage(Component.translatable("trawakened.fear.inspire_fear", new Object[]{entities.size()}).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                        }
                        instance.setCoolDown(100);
                        instance.addMasteryPoint(entity);
                    }
                } else {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        player.displayClientMessage(Component.translatable("tensura.targeting.not_targeted").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                    }
                }
                break;
            case 9:
                List<Entity> entities2 = skillHelper.DrawSphereAndGetEntitiesInIt(entity, 30, false);
                if (!entities2.isEmpty()) {
                    if (!SkillHelper.outOfMagicule(entity, instance)) {
                        entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.WITHER_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                        entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PHANTOM_DEATH, SoundSource.PLAYERS, 1.0F, 1.0F);
                        entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CREEPER_HURT, SoundSource.PLAYERS, 1.0F, 1.0F);
                        entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.AMBIENT_CAVE, SoundSource.PLAYERS, 1.0F, 1.0F);
                        entities2.forEach((Entity) -> {
                            if (Entity instanceof LivingEntity livingEntity) {
                                if (entity.level.getGameRules().getBoolean(trawakenedGamerules.NORMAL_FEAR)) {
                                    AwakenedFearCapability.setScared(livingEntity, AwakenedFearCapability.getScared(livingEntity) + 20);
                                } else {
                                    FearHelper.fearPenalty(livingEntity, AwakenedFearCapability.getScared(livingEntity) + 20);
                                }
                                SkillHelper.addEffectWithSource(livingEntity, livingEntity, MobEffects.POISON, 1000, 5, false, false, false, false);
                            }
                        });
                        if (entity instanceof Player) {
                            Player player = (Player) entity;
                            player.displayClientMessage(Component.translatable("trawakened.fear.inspire_fear", new Object[]{entities2.size()}).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                        }
                        instance.setCoolDown(150);
                        instance.addMasteryPoint(entity);
                    }
                } else {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        player.displayClientMessage(Component.translatable("tensura.targeting.not_targeted").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                    }
                }
                break;
            case 10:
                if (!SkillHelper.outOfMagicule(entity, instance)) {
                    SkillHelper.addEffectWithSource(entity, entity, effectRegistry.ALTERNATE_MODE.get(), 1000, 1, true, false, false, false);
                    entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.WITHER_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PHANTOM_DEATH, SoundSource.PLAYERS, 1.0F, 1.0F);
                    entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                    instance.setCoolDown(150);
                    instance.addMasteryPoint(entity);
                }
                break;
            case 11:
                if (!SkillHelper.outOfMagicule(entity, instance)) {
                    SkillHelper.addEffectWithSource(entity, entity, effectRegistry.INTRUDER_MODE.get(), 1000, 1, true, false, false, false);
                    entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.WITHER_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PHANTOM_DEATH, SoundSource.PLAYERS, 1.0F, 1.0F);
                    entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        player.displayClientMessage(Component.translatable("trawakened.fear.inspiration").setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)), false);
                    }
                    instance.setCoolDown(150);
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
        if (!AwakenedFearCapability.GetIsAlternate(living)) {
            living.addEffect(new MobEffectInstance(TensuraMobEffects.PRESENCE_CONCEALMENT.get(), 120, 255, false, false, false));
            if (living instanceof Player player) {
                if (!player.isCreative()) {
                    player.getAbilities().mayfly = true;
                    player.getAbilities().invulnerable = true;
                    player.getAbilities().mayBuild = false;
                    player.onUpdateAbilities();
                }
            }
            if (living instanceof Player player) {
                if (TensuraPlayerCapability.getMagicule(player) <= 1000) {
                    if (MathHelper.RandomChance(1)) {
                        TensuraPlayerCapability.getFrom(player).ifPresent((capability) -> {
                            capability.setBaseMagicule(capability.getBaseMagicule() + 1, player);
                            capability.setMagicule(capability.getBaseMagicule());
                        });
                        TensuraPlayerCapability.sync(player);
                    }
                }
            }
        } else {
            switch (AlternateType.Assimilation.fromNBT(instance.getOrCreateTag().getCompound("assimilation"))) {
                case FLAWED -> AlternateType.Assimilation.flawedBuff(living);
                case OVERDRIVEN -> AlternateType.Assimilation.overdrivenBuff(living);
                case COMPLETE -> AlternateType.Assimilation.completeBuff(living);
            }
        }
    }

    @Override
    public void onDeath(ManasSkillInstance instance, LivingDeathEvent event) {
        CompoundTag tag = instance.getOrCreateTag();
        if (!AwakenedFearCapability.GetIsAlternate(event.getEntity())) {
            tag.putInt("original_scared", 0);
            tag.putBoolean("is_locked", false);
            AwakenedFearCapability.SetIsAlternate(event.getEntity(), false);
            tag.put("alternate_type", io.github.dracosomething.trawakened.library.AlternateType.INTRUDER.toNBT());
        }
    }

    @Override
    public void onLearnSkill(ManasSkillInstance instance, LivingEntity living, UnlockSkillEvent event) {
        AwakenedFearCapability.SetIsAlternate(living, false);
        instance.getOrCreateTag().putInt("original_scared", 0);
        instance.getOrCreateTag().putBoolean("is_locked", false);
        instance.getOrCreateTag().put("alternate_type", io.github.dracosomething.trawakened.library.AlternateType.INTRUDER.toNBT());
        if (living instanceof Player player) {
            player.displayClientMessage(Component.translatable("trawakened.fear.learn_self", new Object[]{AwakenedFearCapability.getFearType(player).toString()}).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)), false);
        }
    }

    private void CopySkills(LivingEntity target, Player player) {
        Iterator<ManasSkillInstance> skills = List.copyOf(SkillAPI.getSkillsFrom(target).getLearnedSkills()).iterator();

        while (skills.hasNext()) {
            ManasSkillInstance skill = skills.next();
            if (skill.getMastery() >= 0 && SkillUtils.hasSkill(player, skill.getSkill())) {
                System.out.println(skill);
                SkillUtils.learnSkill(player, skill.getSkill());
            }
        }
    }
}
