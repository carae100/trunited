package io.github.dracosomething.trawakened.event;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.event.SpiritualHurtEvent;
import com.github.manasmods.tensura.registry.enchantment.TensuraEnchantments;
import com.github.manasmods.tensura.registry.items.TensuraArmorItems;
import com.github.manasmods.tensura.registry.items.TensuraToolItems;
import com.mojang.math.Vector3f;
import io.github.dracosomething.trawakened.ability.skill.ultimate.*;
import io.github.dracosomething.trawakened.ability.skill.unique.voiceofhonkai;
import io.github.dracosomething.trawakened.entity.otherwolder.*;
import io.github.dracosomething.trawakened.helper.EngravingHelper;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.registry.enchantRegistry;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = trawakened.MODID)
public class ModEvents {
    @SubscribeEvent
    public static void SlowBreak(TickEvent.PlayerTickEvent event) {
        LivingEntity entity = event.player;
        Random random = new Random();
        List<ItemStack> list = List.of(entity.getItemBySlot(EquipmentSlot.CHEST),
                entity.getItemBySlot(EquipmentSlot.FEET),
                entity.getItemBySlot(EquipmentSlot.HEAD),
                entity.getItemBySlot(EquipmentSlot.LEGS),
                entity.getItemBySlot(EquipmentSlot.MAINHAND),
                entity.getItemBySlot(EquipmentSlot.OFFHAND));
        for (ItemStack item : list) {
            if (random.nextInt(1, 100) <= 1) {
                if (item.getEnchantmentLevel(enchantRegistry.KOJIMA_PARTICLE.get()) >= 1) {
                    TensuraParticleHelper.addParticlesAroundSelf(entity, new DustParticleOptions(new Vector3f(Vec3.fromRGB24(6223797)), 1), 2);
                    item.setDamageValue(item.getDamageValue() + 1);
                    int level = (item.getEnchantmentLevel(enchantRegistry.KOJIMA_PARTICLE.get()) - 1);
                    EngravingHelper.RemoveEnchantments(item, enchantRegistry.KOJIMA_PARTICLE.get());
                    if (level > 0) {
                        item.enchant(enchantRegistry.KOJIMA_PARTICLE.get(), level);
                    }
                    TensuraParticleHelper.addParticlesAroundSelf(entity, new DustParticleOptions(new Vector3f(Vec3.fromRGB24(6223797)), 1), 2);
                }
                if (item.getEnchantmentLevel(enchantRegistry.CORAL.get()) >= 1) {
                    if (!((item.getEnchantmentLevel(Enchantments.FIRE_PROTECTION) >= 4 || item.getEnchantmentLevel(Enchantments.FIRE_ASPECT) >= 2) &&
                            item.getEnchantmentLevel(TensuraEnchantments.MAGIC_INTERFERENCE.get()) >= 1)) {
                        entity.setSecondsOnFire(120);
                        TensuraParticleHelper.addParticlesAroundSelf(entity, new DustParticleOptions(new Vector3f(Vec3.fromRGB24(14702884)), 1), 2);
                        item.setDamageValue(item.getDamageValue() + 3);
                        int level = (item.getEnchantmentLevel(enchantRegistry.CORAL.get()) - 1);
                        EngravingHelper.RemoveEnchantments(item, enchantRegistry.CORAL.get());
                        if (level > 0) {
                            item.enchant(enchantRegistry.CORAL.get(), level);
                        }
                        TensuraParticleHelper.addParticlesAroundSelf(entity, new DustParticleOptions(new Vector3f(Vec3.fromRGB24(14702884)), 1), 2);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void GrantUnique(LivingSpawnEvent event){
        LivingEntity entity = event.getEntity();
        event.setPhase(EventPriority.LOWEST);
        if(entity instanceof defaultOtherWolder) {
            List<ManasSkill> list_unique = SkillAPI.getSkillRegistry().getValues().stream().filter((manasSkill) -> {
                boolean var10000;
                if (manasSkill instanceof Skill skill) {
                    if (skill.getType().equals(Skill.SkillType.UNIQUE)) {
                        var10000 = true;
                        return var10000;
                    }
                }

                var10000 = false;
                return var10000;
            }).toList();
            List<ManasSkill> list_ultimate = SkillAPI.getSkillRegistry().getValues().stream().filter((manasSkill) -> {
                boolean var10000;
                if (manasSkill instanceof Skill skill) {
                    if (skill.getType().equals(Skill.SkillType.ULTIMATE)) {
                        var10000 = true;
                        return var10000;
                    }
                }

                var10000 = false;
                return var10000;
            }).toList();
            ManasSkill skill;
            while(!(SkillAPI.getSkillsFrom(entity).getLearnedSkills().size() >= 1)) {
                System.out.println(SkillAPI.getSkillsFrom(entity).getLearnedSkills().size());
                Random random1 = new Random();
                if (random1.nextInt(1, 100) >= 90) {
                    skill = list_unique.get(random1.nextInt(1, list_unique.size()));
                } else {
                    skill = list_ultimate.get(random1.nextInt(1, list_ultimate.size()));
                }
                if(!(skill instanceof voiceofhonkai || skill instanceof willofhonkai || skill instanceof powerofhonkai || skill instanceof herrscheroftime || skill instanceof  herrscherofdestruction || skill instanceof herrscherofplague || skill instanceof herrscheroftheworld)) {
                    SkillUtils.learnSkill(entity, skill);
                }
            }
            entity.setItemInHand(InteractionHand.MAIN_HAND, TensuraToolItems.ADAMANTITE_SCYTHE.get().getDefaultInstance());
            entity.setItemSlot(EquipmentSlot.HEAD, TensuraArmorItems.ADAMANTITE_HELMET.get().getDefaultInstance());
            entity.setItemSlot(EquipmentSlot.CHEST, TensuraArmorItems.ADAMANTITE_CHESTPLATE.get().getDefaultInstance());
            entity.setItemSlot(EquipmentSlot.LEGS, TensuraArmorItems.ADAMANTITE_LEGGINGS.get().getDefaultInstance());
            entity.setItemSlot(EquipmentSlot.FEET, TensuraArmorItems.ADAMANTITE_BOOTS.get().getDefaultInstance());
        }
    }

    @SubscribeEvent
    public static void cancelHealing(LivingHealEvent event){
        if(event.getEntity().hasEffect(effectRegistry.HEALPOISON.get())){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void DoubleSpiritualDamage(SpiritualHurtEvent event){
        if(event.getEntity().hasEffect(effectRegistry.CREATIVE_MENU.get())){
            event.setAmount(
                    (float) (Math.ceil(event.getAmount()) * (event.getEntity().getEffect(effectRegistry.CREATIVE_MENU.get()).getAmplifier() == 0?1:event.getEntity().getEffect(effectRegistry.CREATIVE_MENU.get()).getAmplifier()) * 10)
            );
        }
        if(event.getEntity().hasEffect(effectRegistry.SPIRITUAL_BLOCK.get())){
            event.setAmount(
                    (float) Math.floor(event.getAmount() -
                            Math.ceil(
                                    (double) Objects.requireNonNull(
                                            event.getEntity().getEffect(
                                                    effectRegistry.SPIRITUAL_BLOCK.get()
                                            )
                                    ).getAmplifier() / 15)
                    )
            );
        }
    }

    @SubscribeEvent
    public static void DoubleDamage(LivingDamageEvent event){
        if(event.getEntity().hasEffect(effectRegistry.CREATIVE_MENU.get())){
            event.setAmount(
                    (float) (Math.ceil(event.getAmount()) * (event.getEntity().getEffect(effectRegistry.CREATIVE_MENU.get()).getAmplifier() == 0?1:event.getEntity().getEffect(effectRegistry.CREATIVE_MENU.get()).getAmplifier()) * 10)
            );
        }
    }

    @SubscribeEvent
    public static void notRemoveEffect(MobEffectEvent.Remove event) {
        LivingEntity entity = event.getEntity();
        if (entity.hasEffect(new MobEffectInstance(effectRegistry.OVERWHELMED.get()).getEffect()) ||
                entity.hasEffect(new MobEffectInstance((MobEffect) effectRegistry.PLAGUEEFFECT.get()).getEffect()) ||
                entity.hasEffect(new MobEffectInstance((MobEffect) effectRegistry.BRAINDAMAGE.get()).getEffect()) ||
                entity.hasEffect(new MobEffectInstance((MobEffect) effectRegistry.TIMESTOP_CORE.get()).getEffect()) ||
                entity.hasEffect(new MobEffectInstance((MobEffect) effectRegistry.TIMESTOP.get()).getEffect())){
            event.setCanceled(true);
        }
        if(entity.hasEffect(new MobEffectInstance(effectRegistry.MELT.get()).getEffect())){
            Random rand = new Random();
            int chance = rand.nextInt(101);
            if(chance < 10){
                event.setCanceled(true);
            } else {
                event.setCanceled(false);
            }
        }
    }
}
