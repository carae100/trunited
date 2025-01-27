package io.github.dracosomething.trawakened.registry;

import com.github.manasmods.tensura.registry.race.TensuraRaces;
import io.github.dracosomething.trawakened.race.*;
import io.github.dracosomething.trawakened.race.herrscher.HerrscherOfTime;
import io.github.dracosomething.trawakened.race.herrscher.herrscherofdestruction;
import io.github.dracosomething.trawakened.race.herrscher.herrscherofplague;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = trawakened.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class raceregistry {
    public static final ResourceLocation HONKAI_APOSTLE = new ResourceLocation("trawakened", "honkai_apostle");
    public static final ResourceLocation AWAKENED_APOSTLE = new ResourceLocation("trawakened", "awakened_apostle");
    public static final ResourceLocation ENSLAVED_APOSTLE = new ResourceLocation("trawakened", "enslaved_apostle");
    public static final ResourceLocation HERRSCHER_SEED_AWAKENED = new ResourceLocation("trawakened", "herrscher_seed_awakened");
    public static final ResourceLocation HERRSCHER_SEED_ENSLAVED = new ResourceLocation("trawakened", "herrscher_seed_enslaved");
    public static final ResourceLocation HERRSCHER_OF_DESTRUCTION = new ResourceLocation("trawakened", "herrscher_of_destruction");
    public static final ResourceLocation HERRSCHER_OF_PLAGUE = new ResourceLocation("trawakened", "herrscher_of_pestilence");
    public static final ResourceLocation HERRSCHER_OF_TIME = new ResourceLocation("trawakened", "herrscher_of_time");

    public raceregistry(){}

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(((IForgeRegistry)TensuraRaces.RACE_REGISTRY.get()).getRegistryKey(), (helper) -> {
            helper.register("honkai_apostle", new honkaiapostle());
            helper.register("awakened_apostle", new awakenedapostle());
            helper.register("enslaved_apostle", new enslavedapostle());
            helper.register("herrscher_seed_awakened", new HerrscherSeedAwakened());
            helper.register("herrscher_seed_enslaved", new HerrscherSeedEnslaved());
            helper.register("herrscher_of_destruction", new herrscherofdestruction());
            helper.register("herrscher_of_pestilence", new herrscherofplague());
            helper.register("herrscher_of_time", new HerrscherOfTime());
        });
    }
}
