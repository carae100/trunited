package io.github.dracosomething.trunited.registry;

import com.github.manasmods.tensura.registry.race.TensuraRaces;
import io.github.dracosomething.trunited.trunited;
import io.github.dracosomething.trunited.race.honkaiapostle;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = trunited.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class raceregistry {
    public static final ResourceLocation TEST = new ResourceLocation("testaddon", "testrace");

    public raceregistry(){}

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(((IForgeRegistry)TensuraRaces.RACE_REGISTRY.get()).getRegistryKey(), (helper) -> {
            helper.register("honkai_apostle", new honkaiapostle());
        });
    }
}
