package io.github.dracosomething.trawakened.ability.skill.extra.System;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.data.TensuraTags;
import com.github.manasmods.tensura.registry.skill.ExtraSkills;
import com.mojang.math.Vector3f;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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
        return 2;
    }

    @Override
    public int nextMode(LivingEntity entity, TensuraSkillInstance instance, boolean reverse) {
        return instance.getMode() == 1 ? 2 : 1;
    }

    @Override
    public Component getModeName(int mode) {
        return mode == 1 ? Component.translatable("trawakened.skill.system.extra.rulers_authority.mode1") : Component.translatable("trawakened.skill.system.extra.rulers_authority.mode2");
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
                Entity target = SkillHelper.getTargetingEntity(entity, 30.0, 1.0, false, true);
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

                double maxSize = instance.isMastered(entity) ? 2.0 : 0.5;
                if (SkillUtils.isSkillToggled(entity, (ManasSkill)ExtraSkills.GRAVITY_DOMINATION.get())) {
                    maxSize += 5.0;
                } else if (SkillUtils.isSkillToggled(entity, (ManasSkill)ExtraSkills.GRAVITY_MANIPULATION.get())) {
                    maxSize += 3.0;
                }

                if (target.getBoundingBox().getSize() > maxSize) {
                    return;
                }

                CompoundTag tag = instance.getOrCreateTag();
                tag.putUUID("target", target.getUUID());
                System.out.println(tag.getUUID("target"));
                double range = Math.min(30.0, target.position().subtract(entity.getEyePosition()).length());
                tag.putDouble("range", (double)((int)range));
            }
        } else {
                System.out.println(entity);
                float speed = 10.0F;
                Vector3f vector3f = new Vector3f(entity.getViewVector(speed));
                if (entity.isShiftKeyDown()) {
                    vector3f.mul(-1);
                    entity.setDeltaMovement(entity.getDeltaMovement().add(100, 100, 100));
                    entity.resetFallDistance();
                } else {
                    entity.setDeltaMovement(entity.getDeltaMovement().add(100, 100, 100));
                    entity.resetFallDistance();
                }
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

    public boolean onHeld(ManasSkillInstance instance, LivingEntity entity, int heldTicks) {
        if (instance.getMode() != 1 && !entity.getMainHandItem().isEmpty()) {
            return false;
        } else {
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
        }
    }

    @Override
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
            }
        }
    }

    public CompoundTag getTag() {
        return tag;
    }

    public void sync(ManasSkillInstance instance){
        this.tag = instance.getOrCreateTag().getCompound("cap");
        instance.getOrCreateTag().put("cap", this.tag);
    }
}
