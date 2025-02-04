package io.github.dracosomething.trawakened.ability.skill.extra;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.mojang.math.Vector3f;
import io.github.dracosomething.trawakened.helper.skillHelper;
import io.github.dracosomething.trawakened.registry.enchantRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PrimalArmor extends Skill {
    public PrimalArmor() {
        super(SkillType.EXTRA);
    }

    @Override
    public int modes() {
        return 2;
    }

    @Override
    public int nextMode(LivingEntity entity, TensuraSkillInstance instance, boolean reverse) {
        if(instance.isMastered(entity)) {
            if (reverse) {
                return instance.getMode() == 1 ? 2 : instance.getMode() - 1;
            } else {
                return instance.getMode() == 2 ? 1 : instance.getMode() + 1;
            }
        }
        return 1;
    }

    @Override
    public Component getModeName(int mode) {
        switch (mode){
            case 1 -> {
                return Component.translatable("trawakened.skill.mode.primal_armor.assault_armor");
            }
            case 2 -> {
                return Component.translatable("trawakened.skill.mode.primal_armor.coral");
            }
        }
        return null;
    }

    @Override
    public boolean onHeld(ManasSkillInstance instance, LivingEntity living, int heldTicks) {
        CompoundTag tag = instance.getOrCreateTag();
        switch (instance.getMode()) {
            case 1:
                boolean shouldIncrease = tag.getInt("Time") >= 10 ? heldTicks % 1 == 0 : heldTicks % 2 == 0;
                if (heldTicks > 0 && shouldIncrease) {
                    tag.putInt("Time", tag.getInt("Time") + 1);
                    instance.markDirty();
                    int time = tag.getInt("Time") / 10;
                    if (tag.getInt("Time") >= 10) {
                        skillHelper.ParticleCircle(living, time, new DustParticleOptions(new Vector3f(Vec3.fromRGB24(6223797)), 1));
                        List<Entity> list = skillHelper.DrawCircle(living, time, false);
                        for (Entity entity : list) {
                            if(entity != living) {
                                if (entity instanceof LivingEntity entity1) {
                                    entity1.getAllSlots().forEach((itemStack) -> {
                                        itemStack.enchant(enchantRegistry.KOJIMA_PARTICLE.get(), instance.isMastered(living) ? 20 : 10);
                                    });
                                    entity1.addEffect(new MobEffectInstance(TensuraMobEffects.MAGICULE_POISON.get(), 120, 5, false, false, false));
                                }
                            }
                        }
                    } else {
                        Vec3 sight = living.getEyePosition();
                        if (living.level instanceof ServerLevel world) {
                            world.sendParticles(new DustParticleOptions(new Vector3f(Vec3.fromRGB24(6223797)), 1), (double) sight.x, (double) sight.y, (double) sight.z, 1, 0.0, 0.0, 0.0, 1.0);
                        }
                    }
                }

                if (living instanceof Player player) {
                    player.displayClientMessage(Component.translatable("tensura.magic.cast_time", new Object[]{(double)tag.getInt("Time") / 10.0}).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN)), true);
                }

                return true;
            case 2:
                boolean shouldIncrease2 = tag.getInt("Time") >= 10 ? heldTicks % 1 == 0 : heldTicks % 2 == 0;
                if (heldTicks > 0 && shouldIncrease2) {
                    tag.putInt("Time", tag.getInt("Time") + 1);
                    instance.markDirty();
                    int time = tag.getInt("Time") / 10;
                    if (tag.getInt("Time") >= 10) {
                        skillHelper.ParticleCircle(living, time, new DustParticleOptions(new Vector3f(Vec3.fromRGB24(14702884)), 1));
                        List<Entity> list = skillHelper.DrawCircle(living, time, false);
                        for (Entity entity : list) {
                            if(entity != living) {
                                if (entity instanceof LivingEntity entity1) {
                                    entity1.getAllSlots().forEach((itemStack) -> {
                                        itemStack.enchant(enchantRegistry.CORAL.get(), instance.isMastered(living) ? 20 : 10);
                                    });
                                    entity1.addEffect(new MobEffectInstance(TensuraMobEffects.MAGICULE_POISON.get(), 120, 5, false, false, false));
                                    entity1.setSecondsOnFire(100);
                                }
                            }
                        }
                    } else {
                        Vec3 sight = living.getEyePosition();
                        if (living.level instanceof ServerLevel world) {
                            world.sendParticles(new DustParticleOptions(new Vector3f(Vec3.fromRGB24(14702884)), 1), (double) sight.x, (double) sight.y, (double) sight.z, 1, 0.0, 0.0, 0.0, 1.0);
                        }
                    }
                }

                if (living instanceof Player player) {
                    player.displayClientMessage(Component.translatable("tensura.magic.cast_time", new Object[]{(double)tag.getInt("Time") / 10.0}).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                }

                return true;
        }
        return false;
    }

    @Override
    public int getMaxHeldTime(ManasSkillInstance instance, LivingEntity living) {
        return 5*20;
    }

    @Override
    public void onRelease(ManasSkillInstance instance, LivingEntity entity, int heldTicks) {
        CompoundTag tag = instance.getOrCreateTag();
        tag.putInt("Time", 0);
    }

    @Override
    public boolean canBeToggled(ManasSkillInstance instance, LivingEntity entity) {
        return true;
    }

    @Override
    public boolean canTick(ManasSkillInstance instance, LivingEntity entity) {
        return instance.isToggled();
    }

    @Override
    public void onTick(ManasSkillInstance instance, LivingEntity living) {
        if (instance.isToggled()) {
            List<ItemStack> list = List.of(living.getItemBySlot(EquipmentSlot.CHEST),
                    living.getItemBySlot(EquipmentSlot.FEET),
                    living.getItemBySlot(EquipmentSlot.HEAD),
                    living.getItemBySlot(EquipmentSlot.LEGS),
                    living.getItemBySlot(EquipmentSlot.MAINHAND),
                    living.getItemBySlot(EquipmentSlot.OFFHAND));
            for (ItemStack item : list) {
                item.enchant(enchantRegistry.PRIMAL_ARMOR.get(), instance.isMastered(living) ? 5 : 2);
            }
        }
    }
}
