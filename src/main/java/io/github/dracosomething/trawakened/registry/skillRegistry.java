package io.github.dracosomething.trawakened.registry;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import io.github.dracosomething.trawakened.ability.skill.extra.PrimalArmor;
import io.github.dracosomething.trawakened.ability.skill.extra.System.Stealth;
import io.github.dracosomething.trawakened.ability.skill.extra.conceptofinfinity;
import io.github.dracosomething.trawakened.ability.skill.ultimate.*;
import io.github.dracosomething.trawakened.ability.skill.unique.*;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = trawakened.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class skillRegistry {
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
    public static final RegistryObject<PrimalArmor> PRIMAL_ARMOR;
    public static final RegistryObject<Alternate> ALTERNATE;
    public static final RegistryObject<falseGabriel> FALSE_GABRIEL;
    public static final RegistryObject<SystemSkill> SYSTEM;
    public static final RegistryObject<Stealth> STEALTH;

    public skillRegistry() {
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
        PRIMAL_ARMOR = registry.register("primal_armor", PrimalArmor::new);
        ALTERNATE = registry.register("alternate", Alternate::new);
        FALSE_GABRIEL = registry.register("false_gabriel", falseGabriel::new);
        SYSTEM = registry.register("system", SystemSkill::new);
        STEALTH = registry.register("stealth", Stealth::new);
    }
}
