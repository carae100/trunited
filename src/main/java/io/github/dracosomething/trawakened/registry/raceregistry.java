package io.github.dracosomething.trawakened.registry;

import com.github.manasmods.tensura.registry.race.TensuraRaces;
import io.github.dracosomething.trawakened.race.awakenedapostle;
import io.github.dracosomething.trawakened.race.enslavedapostle;
import io.github.dracosomething.trawakened.trawakened;
import io.github.dracosomething.trawakened.race.honkaiapostle;
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


    public raceregistry(){}

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(((IForgeRegistry)TensuraRaces.RACE_REGISTRY.get()).getRegistryKey(), (helper) -> {
            helper.register("honkai_apostle", new honkaiapostle());
            helper.register("awakened_apostle", new awakenedapostle());
            helper.register("enslaved_apostle", new enslavedapostle());
        });
    }
}
