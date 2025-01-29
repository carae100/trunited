package io.github.dracosomething.trawakened.registry;

import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import io.github.dracosomething.trawakened.enchantment.KojimaParticleEnchantment;
import io.github.dracosomething.trawakened.enchantment.PrimalArmorEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class enchantRegistry {
    private static final DeferredRegister<Enchantment> registry;
    public static final RegistryObject<EngravingEnchantment> KOJIMA_PARTICLE;
    public static final RegistryObject<EngravingEnchantment> PRIMAL_ARMOR;

    public enchantRegistry(){}

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, "trawakened");
        KOJIMA_PARTICLE = registry.register("kojima_particle", KojimaParticleEnchantment::new);
        PRIMAL_ARMOR = registry.register("primal_armor", PrimalArmorEnchantment::new);
    }
}
