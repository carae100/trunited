package io.github.dracosomething.trawakened.mixin.client;

import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;

@Mixin(value = ClientLevel.class, priority = 1000000)
public abstract class ClientLevelMixin {
    public ClientLevelMixin() {
    }

    @Shadow
    @Final
    EntityTickList tickingEntities;

    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    public void stopTime(BooleanSupplier p_104727_, CallbackInfo ci) {
        List<MobEffect> time_stops = ForgeRegistries.MOB_EFFECTS.getValues().stream().filter(effect -> {
            return effect.getDisplayName().contains(Component.literal("time_stop_core"));
        }).toList();
        time_stops.forEach(effect -> {
            if (Minecraft.getInstance().player != null) {
                if (Minecraft.getInstance().player.hasEffect(effect)) {
                    ci.cancel();
                }
            }
        });
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void tickEntities() {
        List<MobEffect> time_stops = ForgeRegistries.MOB_EFFECTS.getValues().stream().filter(effect -> {
            return effect.getDisplayName().contains(Component.literal("time_stop_core"));
        }).toList();
        ClientLevel level = ((ClientLevel) (Object) this);
        ProfilerFiller profilerfiller = level.getProfiler();
        profilerfiller.push("entities");
        this.tickingEntities.forEach((entity) -> {
            time_stops.forEach(effect -> {
                if (Minecraft.getInstance().player != null) {
                    if (Minecraft.getInstance().player.hasEffect(effect)) {
                        if (entity != Minecraft.getInstance().player) {
//                        ci.cancel();
                            return;
                        }
                    }
                }
            });
            if (!entity.isRemoved() && !entity.isPassenger()) {
                level.guardEntityTick(level::tickNonPassenger, entity);
            }
        });
        profilerfiller.pop();
        level.tickBlockEntities();
    }
}
