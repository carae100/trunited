package io.github.dracosomething.trawakened.mixin;

import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.config.TensuraConfig;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(TensuraPlayerCapability.class)
public class TensuraPlayerCapabilityMixin {
    @Inject(
            method = "loadRaces",
            at = @At("HEAD")
    )
    private static void injectRaces(CallbackInfoReturnable<List<ResourceLocation>> cir) {
        List<ResourceLocation> races = new ArrayList(((List)TensuraConfig.INSTANCE.racesConfig.startingRaces.get()).stream().map(ResourceLocation::new).toList());
        races.add(new ResourceLocation("trawakened", "honkai_apostle")); // Add your new race here

        // Set the modified list as the return value
        cir.setReturnValue(races);
    }
}