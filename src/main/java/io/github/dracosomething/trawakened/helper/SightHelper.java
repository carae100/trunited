package io.github.dracosomething.trawakened.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SightHelper {
    public static Vec3 getSightPos1(LivingEntity entity) {
        return entity.getEyePosition().subtract(0, 10, 0);
    }

    public static Vec3 getSightPos2(LivingEntity entity) {
        return entity.getEyePosition();
    }

    public static AABB getLineOfSight(LivingEntity entity, Vec3 vec3, Vec3 vec31) {
        if (entity.getYHeadRot() >= -45.0F && entity.getYHeadRot() <= 45.0F) {
            vec31 = vec31.add(5, 10, 10);
            vec3 = vec3.subtract(5, 0,0);
        } else if (entity.getYHeadRot() >= 45.1F && entity.getYHeadRot() <= 135.0F) {
            vec31 = vec31.add(0, 10, 5);
            vec31 = vec31.subtract(10, 0, 0);
            vec3 = vec3.subtract(0,0,5);
        } else if (entity.getYHeadRot() >= -135F && entity.getYHeadRot() <= -45.1F) {
            vec31 = vec31.add(10, 10, 5);
            vec3 = vec3.subtract(0,0,5);
        } else if(entity.getYHeadRot() >= 135.1F && entity.getYHeadRot() <= 180F) {
            vec31 = vec31.add(5, 10, 0);
            vec31 = vec31.subtract(0,0,10);
            vec3 = vec3.subtract(5,0,0);
        } else if (entity.getYHeadRot() <= -135.1F && entity.getYHeadRot() >= -180F) {
            vec31 = vec31.add(5, 10, 0);
            vec31 = vec31.subtract(0,0,10);
            vec3 = vec3.subtract(5,0,0);
        }
        AABB sight = new AABB(vec3, vec31);
        return sight;
    }

    public static Iterable<BlockPos> getBlocksInSight(Vec3 vec3, Vec3 vec31) {
        return BlockPos.betweenClosed(new BlockPos(vec3), new BlockPos(vec31));
    }

    public static List<Entity> getEntitiesInSight(LivingEntity entity, AABB sight) {
        return entity.level.getEntities(entity, sight);
    }
}
