package io.github.dracosomething.trawakened.registry;

import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.entity.human.ShogoTaguchiEntity;
import io.github.dracosomething.trawakened.enchantment.KojimaParticleEnchantment;
import io.github.dracosomething.trawakened.enchantment.PrimalArmorEnchantment;
import io.github.dracosomething.trawakened.entity.otherwolder.defaultOtherWolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class entityRegistry {
    private static final DeferredRegister<EntityType<?>> registry;
    public static final RegistryObject<EntityType<defaultOtherWolder>> DEFAULT_OTHER_WORLDER;

    public entityRegistry(){}

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "trawakened");
        DEFAULT_OTHER_WORLDER = registry.register("default_other_worlder", () -> {
            return EntityType.Builder.of(defaultOtherWolder::new, MobCategory.MONSTER).canSpawnFarFromPlayer().updateInterval(2).clientTrackingRange(32).sized(0.6F, 1.8F).build((new ResourceLocation("trawakened", "default_other_worlder")).toString());
        });
    }
}
