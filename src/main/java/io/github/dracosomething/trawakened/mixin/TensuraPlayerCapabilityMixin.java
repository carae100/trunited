package io.github.dracosomething.trawakened.mixin;

import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.config.TensuraConfig;
import com.github.manasmods.tensura.menu.RaceSelectionMenu;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.race.TensuraRaces;
import com.github.manasmods.tensura.world.TensuraGameRules;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.NetworkHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.github.manasmods.tensura.capability.race.TensuraPlayerCapability.getFrom;

@Mixin(TensuraPlayerCapability.class)
public abstract class TensuraPlayerCapabilityMixin {
    @Shadow
    public static List<ResourceLocation> loadRaces() {
        return null;
    }

    private TensuraPlayerCapabilityMixin(){}

    @Inject(
            method = "loadRaces",
            at = @At("HEAD"),
            cancellable = true,
            remap=false
    )
    private static void injectRaces(CallbackInfoReturnable<List<ResourceLocation>> cir) {
        System.out.println("ewrqqwrwet");

        List<ResourceLocation> races = new ArrayList(((List<String>)TensuraConfig.INSTANCE.racesConfig.startingRaces.get()).stream().map(ResourceLocation::new).toList());
        if (!races.contains(new ResourceLocation("trawakened", "honkai_apostle"))) {
            races.add(new ResourceLocation("trawakened", "honkai_apostle"));
        }
        List<String> randomRaces = (List)TensuraConfig.INSTANCE.racesConfig.randomRaces.get();
        if (!randomRaces.contains(new String("trawakened:honkai_apostle"))) {
            randomRaces.add(new String("trawakened:honkai_apostle"));
        }

        System.out.println(races);
        System.out.println(randomRaces);

        if (randomRaces.isEmpty()) {
            cir.setReturnValue(races);
        } else {
            Random random = new Random();
            String race = (String)randomRaces.get(random.nextInt(randomRaces.size()));
            if (!race.isEmpty() && !race.isBlank()) {
                races.add(new ResourceLocation(race));
            }

            cir.setReturnValue(races);
            System.out.println(races);
        }


//        cir.setReturnValue(races);
    }

    @Inject(
            method = "checkForFirstLogin(Lnet/minecraftforge/event/entity/player/PlayerEvent$PlayerLoggedInEvent;)V",
            at = @At("HEAD"),
            remap = false
    )
    private static void checkForFirstLogin(PlayerEvent.PlayerLoggedInEvent e, CallbackInfo ci) {
        Player var2 = e.getEntity();
        if (var2 instanceof ServerPlayer player) {
            getFrom(player).ifPresent((cap) -> {
                if (cap.getRace() == null) {
                    if (player.getLevel().getGameRules().getBoolean(TensuraGameRules.RIMURU_MODE)) {
                        RaceSelectionMenu.reincarnateAsRimuru(player);
                    } else {
                        if (player.getLevel().getGameRules().getBoolean(TensuraGameRules.SKILL_BEFORE_RACE)) {
                            cap.setRace(player, (Race)TensuraRaces.HUMAN.get(), true);
                            RaceSelectionMenu.grantUniqueSkill(player);
                        }

                        player.setInvulnerable(true);
                        List<ResourceLocation> races = loadRaces();
//                        races.add(new ResourceLocation("trawakened", "honkai_apostle"));

                        System.out.println(races);

                        NetworkHooks.openScreen(player, new SimpleMenuProvider(RaceSelectionMenu::new, Component.translatable("tensura.race.selection")), (buf) -> {
                            buf.writeBoolean(false);
                            buf.writeCollection(races, FriendlyByteBuf::writeResourceLocation);
                        });

                        System.out.println(races);
                    }

                    RaceSelectionMenu.grantLearningResistance(player);
                }

            });
        }
    }

}