package io.github.dracosomething.trawakened.handler;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.capability.effects.ITensuraEffectsCapability;
import com.github.manasmods.tensura.capability.effects.TensuraEffectsCapability;
import com.github.manasmods.tensura.capability.ep.ITensuraEPCapability;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.capability.magicule.MagiculeChunkCapability;
import com.github.manasmods.tensura.capability.magicule.MagiculeChunkCapabilityImpl;
import com.github.manasmods.tensura.capability.race.ITensuraPlayerCapability;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.capability.skill.ITensuraSkillCapability;
import com.github.manasmods.tensura.capability.skill.TensuraSkillCapability;
import com.github.manasmods.tensura.capability.smithing.ISmithingCapability;
import com.github.manasmods.tensura.capability.smithing.SmithingCapability;
import com.github.manasmods.tensura.entity.human.CloneEntity;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.race.TensuraRaces;
import io.github.dracosomething.trawakened.api.FearTypes;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.AwakenedFearCapability;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.IFearCapability;
import io.github.dracosomething.trawakened.helper.FearHelper;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.registry.raceregistry;
import io.github.dracosomething.trawakened.registry.skillregistry;
import io.github.dracosomething.trawakened.trawakened;
import io.github.dracosomething.trawakened.world.trawakenedGamerules;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = trawakened.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class alternateFearHandler {
    public alternateFearHandler() {
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IFearCapability.class);
    }

    @SubscribeEvent
    static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        AwakenedFearCapability.sync(event.getEntity());
        System.out.println( AwakenedFearCapability.getFearType(event.getEntity()));
    }

    @SubscribeEvent
    static void onPlayerTrack(PlayerEvent.StartTracking e) {
        Entity var2 = e.getTarget();
        if (var2 instanceof LivingEntity living) {
            AwakenedFearCapability.sync(living);
            AwakenedFearCapability.sync(e.getEntity());
        }
    }

    @SubscribeEvent
    static void onPlayerClone(PlayerEvent.Clone e) {
        e.getOriginal().reviveCaps();
        AwakenedFearCapability.getFrom(e.getOriginal()).ifPresent((oldData) -> {
            AwakenedFearCapability.getFrom(e.getEntity()).ifPresent((data) -> {
                data.deserializeNBT((CompoundTag)oldData.serializeNBT());
            });
        });
        e.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent e) {
        AwakenedFearCapability.sync(e.getEntity());
    }

    @SubscribeEvent
    static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent e) {
        AwakenedFearCapability.sync(e.getEntity());
    }

    @SubscribeEvent
    static void onTickLiving(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        FearHelper.fearUpdates(entity);
        if (entity.level.getGameRules().getBoolean(trawakenedGamerules.NORMAL_FEAR)) {
            FearHelper.fearPenalty(entity);
        }
        FearHelper.resetData(entity);
    }

    @Nullable
    public static <T> T getCapability(Entity entity, Capability<T> capability) {
        return entity.getCapability(capability).isPresent() ? entity.getCapability(capability).orElseThrow(() -> {
            return new IllegalArgumentException("Lazy optional must not be empty");
        }) : null;
    }
}
