package io.github.dracosomething.trawakened.event;

import com.github.manasmods.tensura.event.SpiritualHurtEvent;
import com.github.manasmods.tensura.registry.enchantment.TensuraEnchantments;
import io.github.dracosomething.trawakened.helper.EngravingHelper;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.registry.enchantRegistry;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
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
    public static void SlowBreak(TickEvent.PlayerTickEvent event){
        LivingEntity entity = event.player;
        Random random = new Random();
        List<ItemStack> list = List.of(entity.getItemBySlot(EquipmentSlot.CHEST),
                entity.getItemBySlot(EquipmentSlot.FEET),
                entity.getItemBySlot(EquipmentSlot.HEAD),
                entity.getItemBySlot(EquipmentSlot.LEGS),
                entity.getItemBySlot(EquipmentSlot.MAINHAND),
                entity.getItemBySlot(EquipmentSlot.OFFHAND));
        for(ItemStack item : list){
            if(random.nextInt(1, 100) <= 1) {
                if (item.getEnchantmentLevel(enchantRegistry.KOJIMA_PARTICLE.get()) >= 1) {
                    item.setDamageValue(item.getDamageValue() + 1);
                    int level = (item.getEnchantmentLevel(enchantRegistry.KOJIMA_PARTICLE.get()) - 1);
                    EngravingHelper.RemoveEnchantments(item, enchantRegistry.KOJIMA_PARTICLE.get());
                    if(level > 0) {
                        item.enchant(enchantRegistry.KOJIMA_PARTICLE.get(), level);
                    }
                }
            }
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
                    (float) (event.getAmount() *
                    Math.ceil(
                            (double) Objects.requireNonNull(
                                    event.getEntity().getEffect(
                                            effectRegistry.CREATIVE_MENU.get()
                                    )
                            ).getAmplifier() /2)
                    )
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
                    (float) (event.getAmount() *
                            Math.ceil(
                                    (double) Objects.requireNonNull(
                                            event.getEntity().getEffect(
                                                    effectRegistry.CREATIVE_MENU.get()
                                            )
                                    ).getAmplifier() /2)
                    )
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
