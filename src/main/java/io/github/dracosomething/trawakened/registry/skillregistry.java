package io.github.dracosomething.trawakened.registry;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import io.github.dracosomething.trawakened.ability.skill.extra.conceptofinfinity;
import io.github.dracosomething.trawakened.ability.skill.ultimate.*;
import io.github.dracosomething.trawakened.ability.skill.unique.Starkill;
import io.github.dracosomething.trawakened.ability.skill.unique.akashic_plane;
import io.github.dracosomething.trawakened.ability.skill.unique.voiceofhonkai;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = trawakened.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class skillregistry {
    private static final DeferredRegister<ManasSkill> registry;

    public static final RegistryObject<voiceofhonkai> VOICEOFHONKAI;
    public static final RegistryObject<powerofhonkai> POWEROFHONKAI;
    public static final RegistryObject<willofhonkai> WILLOFHONKAI;
    public static final RegistryObject<herrscherofdestruction> HERRSCHEROFDESTRUCTION;
    public static final RegistryObject<herrscherofplague> HERRSCHEROFPLAGUE;
    public static final RegistryObject<herrscheroftime> HERRSCHEROFTIME;
    public static final RegistryObject<herrscheroftheworld> HERRSCHEROFTHEWORLD;
    public static final RegistryObject<Starkill> STARKILL;
    public static final RegistryObject<conceptofinfinity> CONCEPTOFINFINITY;
    public static final RegistryObject<azazel> AZAZEL;
    public static final RegistryObject<akashic_plane> AKASHIC_PLANE;

    public skillregistry() {
    }

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(SkillAPI.getSkillRegistryKey(), "trawakened");
        VOICEOFHONKAI = registry.register("voiceofhonkai", voiceofhonkai::new);
        POWEROFHONKAI = registry.register("powerofhonkai", powerofhonkai::new);
        WILLOFHONKAI = registry.register("willofhonkai", willofhonkai::new);
        HERRSCHEROFDESTRUCTION = registry.register("herrscherofdestructionskill", herrscherofdestruction::new);
        HERRSCHEROFTIME = registry.register("herrscheroftimeskill", herrscheroftime::new);
        HERRSCHEROFPLAGUE = registry.register("herrscherofpestilenceskill", herrscherofplague::new);
        HERRSCHEROFTHEWORLD = registry.register("herrscheroftheworldskill", herrscheroftheworld::new);
        STARKILL = registry.register("starkill", Starkill::new);
        CONCEPTOFINFINITY = registry.register("concept_of_infinity", conceptofinfinity::new);
        AZAZEL = registry.register("azazel", azazel::new);
        AKASHIC_PLANE = registry.register("akashic_plane", akashic_plane::new);
    }
}
