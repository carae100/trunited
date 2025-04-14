package io.github.dracosomething.trawakened.ability.skill.extra.System;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.data.TensuraTags;
import com.github.manasmods.tensura.entity.magic.misc.ThrownItemProjectile;
import com.github.manasmods.tensura.entity.projectile.*;
import com.github.manasmods.tensura.item.custom.HolyWaterItem;
import com.github.manasmods.tensura.item.custom.KunaiItem;
import com.github.manasmods.tensura.item.custom.WebCartridgeItem;
import com.github.manasmods.tensura.item.templates.custom.SimpleSpearItem;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.items.TensuraToolItems;
import com.github.manasmods.tensura.registry.skill.ExtraSkills;
import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import com.mojang.math.Vector3f;
import io.github.dracosomething.trawakened.helper.skillHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.github.manasmods.tensura.ability.skill.unique.ThrowerSkill.getProjectile;

public class rulers_authority extends SystemExtra {
    private CompoundTag tag;

    public rulers_authority() {
        super("rulers_authority");
        tag = new CompoundTag();
    }

    @Override
    public int modes() {
        return 3;
    }

    @Override
    public int nextMode(LivingEntity entity, TensuraSkillInstance instance, boolean reverse) {
         if (reverse) {
            return switch (instance.getMode()) {
                case 1 -> tag.getBoolean("awakened") ? 3 : 2;
                case 2 -> 1;
                case 3 -> 2;
                default -> 0;
            };
        } else {
             return switch (instance.getMode()) {
                 case 1 -> 2;
                 case 2 -> tag.getBoolean("awakened") ? 3 : 1;
                 case 3 -> 1;
                 default -> 0;
             };
         }
    }

    @Override
    public Component getModeName(int mode) {
        return switch (mode) {
            case 1 -> Component.translatable("trawakened.skill.system.extra.rulers_authority.mode1");
            case 2 -> Component.translatable("trawakened.skill.system.extra.rulers_authority.mode2");
            default -> Component.translatable("trawakened.skill.system.extra.rulers_authority.mode3");
        };
    }

    @Override
    public void onLearnSkill(ManasSkillInstance instance, LivingEntity living, UnlockSkillEvent event) {
        CompoundTag tag = instance.getOrCreateTag();
        this.tag.putBoolean("awakened", false);
        tag.put("cap", this.tag);
    }

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        if (instance.getMode() == 1) {
            ItemStack stack = entity.getMainHandItem();
            if (!stack.isEmpty()) {
                Projectile projectile = getProjectile(entity.level, entity, stack, instance);
                float speed = 1.0F;
                Vector3f vector3f = new Vector3f(entity.getViewVector(speed));
                if (projectile instanceof AbstractArrow) {
                    label38: {
                        AbstractArrow arrow = (AbstractArrow)projectile;
                        if (entity instanceof Player) {
                            Player player = (Player)entity;
                            if (player.getAbilities().instabuild) {
                                arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                                break label38;
                            }
                        }

                        arrow.pickup = AbstractArrow.Pickup.ALLOWED;
                        arrow.setBaseDamage(stack.getDamageValue()*1.5);
                    }
                } else if (projectile instanceof ThrowableItemProjectile) {
                    ThrowableItemProjectile itemProjectile = (ThrowableItemProjectile)projectile;
                    itemProjectile.setItem(stack);
                }

                projectile.shoot((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), 2.0F, 0.0F);
                entity.level.addFreshEntity(projectile);
                entity.swing(entity.getUsedItemHand(), true);
                if (entity instanceof Player) {
                    Player player = (Player)entity;
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }
            } else {
                LivingEntity target = SkillHelper.getTargetingEntity(entity, 30.0, false, true);
                if (target == null) {
                    return;
                }

//                if (target.getType().is(TensuraTags.EntityTypes.FULL_GRAVITY_CONTROL)) {
//                    return;
//                }

                label60: {
                    if (entity instanceof Player player) {
                        if (player.getAbilities().invulnerable) {
                            break label60;
                        }
                    }

                    if (target instanceof Player player) {
                        if (player.getAbilities().invulnerable) {
                            return;
                        }
                    }

                    if (SkillUtils.isSkillToggled(target, (ManasSkill)ExtraSkills.GRAVITY_DOMINATION.get()) || SkillUtils.isSkillToggled(target, (ManasSkill)ExtraSkills.GRAVITY_MANIPULATION.get())) {
                        return;
                    }
                }

                if (TensuraEPCapability.getCurrentEP(target) > TensuraEPCapability.getCurrentEP(entity) || !tag.getBoolean("awakened")) {
                    return;
                }

                CompoundTag tag = instance.getOrCreateTag();
                tag.putUUID("target", target.getUUID());
                System.out.println(tag.getUUID("target"));
                double range = Math.min(30.0, target.position().subtract(entity.getEyePosition()).length());
                tag.putDouble("range", (double)((int)range));
            }
        } else if (instance.getMode() == 2) {
            float speed = 1.0F;
            if (entity.isShiftKeyDown()) {
                pushForward(entity, -speed);
                entity.resetFallDistance();
            } else {
                pushForward(entity, speed);
                entity.resetFallDistance();
            }
            instance.setCoolDown(8);
        }
    }

    public void onScroll(ManasSkillInstance instance, LivingEntity entity, double delta) {
        if (instance.getMode() == 1) {
            if (instance instanceof TensuraSkillInstance skillInstance) {
                CompoundTag tag = skillInstance.getOrCreateTag();
                double newRange = tag.getDouble("range") + delta;
                if (newRange > 30.0) {
                    newRange = 30.0;
                } else if (newRange < 0.0) {
                    newRange = 0.0;
                }

                tag.putDouble("range", newRange);
                skillInstance.markDirty();
            }
        }
    }
    private BlockHitResult result;

    public boolean onHeld(ManasSkillInstance instance, LivingEntity entity, int heldTicks) {
        if (instance.getMode() != 1 && instance.getMode() != 3 && !entity.getMainHandItem().isEmpty()) {
            return false;
        } else if (instance.getMode() == 1) {
            Level var5 = entity.getLevel();
            if (var5 instanceof ServerLevel) {
                ServerLevel level = (ServerLevel)var5;
                CompoundTag tag = instance.getOrCreateTag();
                double range = tag.getDouble("range");
                if (entity instanceof Player) {
                    Player player = (Player)entity;
                    player.displayClientMessage(Component.translatable("tensura.skill.range", new Object[]{range}).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)), true);
                }

                if (tag.contains("target")) {
                    Entity target = level.getEntity(tag.getUUID("target"));
                    if (target != null) {
                        Vec3 viewVector = entity.getViewVector(1.0F).scale(range);
                        double x = entity.getX() + viewVector.x;
                        double y = entity.getEyeY() + viewVector.y;
                        double z = entity.getZ() + viewVector.z;
                        Vec3 targetPos = new Vec3(x, y, z);
                        if (targetPos.distanceTo(target.position()) <= 6.0) {
                            if (heldTicks % 60 == 0 && heldTicks > 0) {
                                this.addMasteryPoint(instance, entity);
                            }

                            TensuraParticleHelper.addServerParticlesAroundSelf(target, ParticleTypes.PORTAL, 1.0);
                            Vec3 vec3 = targetPos.subtract(target.position()).normalize().scale(0.5);
                            if (vec3.length() > 0.2) {
                                target.setDeltaMovement(vec3);
                            }

                            target.resetFallDistance();
                            target.hurtMarked = true;
                        }
                    }
                }

                return true;
            } else {
                return false;
            }
        } else {
            if (result == null || entity.isShiftKeyDown()) {
                result = SkillHelper.getPlayerPOVHitResult(entity.level, entity, ClipContext.Fluid.ANY,
                        ClipContext.Block.OUTLINE, 10.0);
                List<LivingEntity> list = skillHelper.GetLivingEntitiesInRange(entity, result.getLocation(), 5);
                list.forEach((living) -> {
                    double range = tag.getDouble("range");
                    if (entity instanceof Player) {
                        Player player = (Player)entity;
                        player.displayClientMessage(Component.translatable("tensura.skill.range", new Object[]{range}).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)), true);
                    }
                    Vec3 viewVector = entity.getViewVector(1.0F).scale(5);
                    double x = entity.getX() + viewVector.x;
                    double y = entity.getEyeY() + viewVector.y;
                    double z = entity.getZ() + viewVector.z;
                    Vec3 targetPos = new Vec3(x, y, z);
                    if (targetPos.distanceTo(living.position()) <= 6.0) {
                        if (heldTicks % 60 == 0 && heldTicks > 0) {
                            this.addMasteryPoint(instance, entity);
                        }

                        Vec3 vec3 = targetPos.subtract(living.position()).normalize().scale(0.5);
                        if (vec3.length() > 0.2) {
                            living.setDeltaMovement(vec3);
                        }

                        living.resetFallDistance();
                        living.hurtMarked = true;
                    }
                });
            }
            List<LivingEntity> list = skillHelper.GetLivingEntitiesInRange(entity, result.getLocation(), 5);
            list.forEach((living) -> {
                living.addEffect(new MobEffectInstance(TensuraMobEffects.BURDEN.get(), 3, 10));
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 10));
                living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 0, 10));
            });
            return true;
        }
    }

    public void onRelease(ManasSkillInstance instance, LivingEntity entity, int heldTicks) {
        if (instance.getMode() == 1) {
            Level var5 = entity.getLevel();
            if (var5 instanceof ServerLevel) {
                ServerLevel level = (ServerLevel) var5;
                CompoundTag tag = instance.getOrCreateTag();
                Entity target = level.getEntity(tag.getUUID("target"));
                float speed = 1.0F;
                Vector3f vector3f = new Vector3f(entity.getViewVector(speed));
                if (entity.isShiftKeyDown()) {
                    vector3f.mul(-1);
                    target.setDeltaMovement((double) vector3f.x(), (double) vector3f.y(), (double) vector3f.z());
                    target.resetFallDistance();
                } else {
                    target.setDeltaMovement((double) vector3f.x(), (double) vector3f.y(), (double) vector3f.z());
                    target.resetFallDistance();
                }
                instance.setCoolDown(5);
            }
        } else if (instance.getMode() == 2) {
            instance.setCoolDown(15);
            result = null;
        }
    }

    @Override
    public int getMaxHeldTime(ManasSkillInstance instance, LivingEntity living) {
        return instance.getMode() == 3 ? 100 : super.getMaxHeldTime(instance, living);
    }

    public CompoundTag getTag() {
        return tag;
    }

    public void sync(ManasSkillInstance instance){
        this.tag = instance.getOrCreateTag().getCompound("cap");
        instance.getOrCreateTag().put("cap", this.tag);
    }

    private void pushForward(LivingEntity entity, float riptideLevel) {
        float f7 = entity.getYRot();
        float f = entity.getXRot();
        float f1 = -Mth.sin(f7 * 0.017453292F) * Mth.cos(f * 0.017453292F);
        float f2 = -Mth.sin(f * 0.017453292F);
        float f3 = Mth.cos(f7 * 0.017453292F) * Mth.cos(f * 0.017453292F);
        float f4 = Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
        float f5 = 3.0F * ((1.0F + riptideLevel) / 4.0F);
        f1 *= f5 / f4;
        f2 *= f5 / f4;
        f3 *= f5 / f4;
        entity.setDeltaMovement((double)f1, (double)f2, (double)f3);
        entity.hasImpulse = true;
        entity.hurtMarked = true;
    }

    private Projectile getProjectile(Level level, LivingEntity entity, ItemStack stack, @Nullable ManasSkillInstance instance) {
        Item var5 = stack.getItem();
        if (var5 instanceof ArrowItem arrowItem) {
            AbstractArrow arrow = arrowItem.createArrow(level, stack, entity);
            double baseDamage = arrow.getBaseDamage() * (double)(instance != null && tag.getBoolean("awakened") ? 5 : 1.5);
            if (SkillUtils.isSkillToggled(entity, (ManasSkill)ExtraSkills.GRAVITY_DOMINATION.get())) {
                baseDamage *= 3.0;
            } else if (SkillUtils.isSkillToggled(entity, (ManasSkill)ExtraSkills.GRAVITY_MANIPULATION.get())) {
                baseDamage *= 2.0;
            }

            arrow.setBaseDamage(baseDamage);
            return arrow;
        } else if (stack.getItem() instanceof ExperienceBottleItem) {
            return new ThrownExperienceBottle(level, entity);
        } else if (stack.getItem() instanceof EggItem) {
            return new ThrownEgg(level, entity);
        } else if (stack.getItem() instanceof EnderpearlItem) {
            return new ThrownEnderpearl(level, entity);
        } else if (stack.getItem() instanceof SnowballItem) {
            return new Snowball(level, entity);
        } else if (stack.getItem() instanceof FireworkRocketItem) {
            return new FireworkRocketEntity(level, stack, entity, entity.getX(), entity.getY(), entity.getZ(), true);
        } else if (stack.getItem() instanceof ThrowablePotionItem) {
            return new ThrownPotion(level, entity);
        } else if (stack.getItem() instanceof HolyWaterItem) {
            return new ThrownHolyWater(level, entity);
        } else if (stack.getItem() instanceof WebCartridgeItem) {
            return new WebBulletProjectile(level, entity, true, stack, stack);
        } else {
            float baseDamage;
            if (stack.isEmpty()) {
                baseDamage = instance != null && instance.isMastered(entity) ? 50.0F : 30.0F;
            } else {
                baseDamage = instance != null && instance.isMastered(entity) ? 100.0F : 50.0F;
            }

            int multiplier = 1;
            if (SkillUtils.isSkillToggled(entity, (ManasSkill)ExtraSkills.GRAVITY_DOMINATION.get())) {
                multiplier = 3;
            } else if (SkillUtils.isSkillToggled(entity, (ManasSkill)ExtraSkills.GRAVITY_MANIPULATION.get())) {
                multiplier = 2;
            }

            baseDamage *= (float)multiplier;
            if (stack.getItem().equals(TensuraToolItems.SEVERER_BLADE.get())) {
                SevererBladeProjectile blade = new SevererBladeProjectile(level, entity, true, stack);
                blade.setBaseDamage(baseDamage);
                return blade;
            } else if (stack.getItem() instanceof TridentItem) {
                ThrownTrident trident = new ThrownTrident(level, entity, stack);
                trident.setBaseDamage((double)baseDamage);
                return trident;
            } else if (stack.getItem() instanceof SimpleSpearItem) {
                SpearProjectile spear = new SpearProjectile(level, entity, stack, true);
                spear.setBaseDamage((double)baseDamage);
                if (instance != null && instance.isMastered(entity)) {
                    spear.setLoyaltyLevel(Math.max(multiplier, spear.getLoyaltyLevel()));
                }

                return spear;
            } else if (stack.getItem() instanceof KunaiItem) {
                ItemStack kunai = stack.copy();
                kunai.setCount(1);
                KunaiProjectile kunaiProjectile = new KunaiProjectile(level, entity, kunai, true);
                kunaiProjectile.setBaseDamage((double)baseDamage);
                if (instance != null && instance.isMastered(entity)) {
                    kunaiProjectile.setLoyaltyLevel(Math.max(multiplier, kunaiProjectile.getLoyaltyLevel()));
                }

                return kunaiProjectile;
            } else {
                ThrownItemProjectile projectile = new ThrownItemProjectile(level, entity, stack, true, baseDamage);
                projectile.getSourceItem().setCount(1);
                projectile.setWeaponDamage(DamageSourceHelper.getMainWeaponDamage(entity, (Entity)null));
                if (instance != null && instance.isMastered(entity)) {
                    projectile.setLoyaltyLevel(multiplier);
                }

                return projectile;
            }
        }
    }
}
