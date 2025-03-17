package io.github.dracosomething.trawakened.handler;

import com.github.manasmods.tensura.entity.*;
import com.github.manasmods.tensura.entity.human.*;
import com.github.manasmods.tensura.entity.magic.barrier.MagicEngineBarrierEntity;
import com.github.manasmods.tensura.entity.magic.misc.MadOgreOrbsEntity;
import com.github.manasmods.tensura.entity.multipart.EvilCentipedeEntity;
import com.github.manasmods.tensura.entity.multipart.TempestSerpentEntity;
import com.github.manasmods.tensura.entity.template.FLyingTamableEntity;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import io.github.dracosomething.trawakened.entity.otherwolder.defaultOtherWolder;
import io.github.dracosomething.trawakened.registry.entityRegistry;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(
        modid = "trawakened",
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class AwakenedEntityHandler {
    public AwakenedEntityHandler(){}

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(entityRegistry.DEFAULT_OTHER_WORLDER.get(), defaultOtherWolder.setAttributes());
    }

    @SubscribeEvent
    public static void registerEntityPlacements(SpawnPlacementRegisterEvent event) {
        event.register(entityRegistry.DEFAULT_OTHER_WORLDER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TensuraTamableEntity::checkTensuraMobSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
    }
}
