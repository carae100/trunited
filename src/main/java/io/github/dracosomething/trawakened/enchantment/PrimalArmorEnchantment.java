package io.github.dracosomething.trawakened.enchantment;

import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.effect.template.TensuraMobEffect;
import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import io.github.dracosomething.trawakened.registry.enchantRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.List;
import java.util.Random;

public class PrimalArmorEnchantment extends EngravingEnchantment {
    public PrimalArmorEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR, new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.FEET, EquipmentSlot.LEGS});
    }

    @Override
    public float getDamageProtection(int pLevel, DamageSource source, LivingEntity wearer, EquipmentSlot slot, float damage) {
        return (float) ((pLevel) * (TensuraEPCapability.getCurrentEP(wearer)/damage*2));
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public void doPostHurt(LivingEntity pTarget, Entity Attacker, int pLevel) {
    if(Attacker instanceof LivingEntity pAttacker) {
        pAttacker.addEffect(new MobEffectInstance(TensuraMobEffects.MAGICULE_POISON.get(), 480, 5, false, false, false));
        if (!pAttacker.getLevel().isClientSide()) {
            SkillHelper.reduceEnergy(pAttacker, pTarget, 0.01 * (double) pLevel, true);
        }

        Random random = new Random();
        int chance = (int) Math.ceil((double) pLevel / 2);
        List<ItemStack> list = List.of(pAttacker.getItemBySlot(EquipmentSlot.CHEST),
            pAttacker.getItemBySlot(EquipmentSlot.FEET),
            pAttacker.getItemBySlot(EquipmentSlot.HEAD),
            pAttacker.getItemBySlot(EquipmentSlot.LEGS),
            pAttacker.getItemBySlot(EquipmentSlot.MAINHAND),
            pAttacker.getItemBySlot(EquipmentSlot.OFFHAND));
        for (ItemStack item : list) {
            if (random.nextInt() >= chance) {
                item.enchant(enchantRegistry.KOJIMA_PARTICLE.get(), (item.getEnchantmentLevel(enchantRegistry.PRIMAL_ARMOR.get()) + 5));
            }
        }
    }
    }
}
