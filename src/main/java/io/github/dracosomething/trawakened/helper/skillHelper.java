package io.github.dracosomething.trawakened.helper;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class skillHelper {
    public static List<Entity> DrawCircle(LivingEntity entity, int radius, boolean areNotAllie){
        AABB aabb = new AABB((double) (entity.getX() - radius), (double) (entity.getY() - radius), (double) (entity.getZ() - radius), (double) (entity.getX() + radius), (double) (entity.getY() + radius), (double) (entity.getZ() + radius));
        List<Entity> entities = entity.level.getEntities((Entity) null, aabb, Entity::isAlive);
        List<Entity> ret = new ArrayList();
        new Vec3((double) entity.getX(), (double) entity.getY(), (double) entity.getZ());
        Iterator var16 = entities.iterator();

        while (var16.hasNext()) {
            Entity entity2 = (Entity) var16.next();

            double x = entity2.getX();
            double y = entity2.getY();
            double z = entity2.getZ();
            double cmp = (double) (radius * radius) - ((double) entity2.getX() - x) * ((double) entity2.getX() - x) - ((double) entity2.getY() - y) * ((double) entity2.getY() - y) - ((double) entity2.getZ() - z) * ((double) entity2.getZ() - z);
            if (cmp > 0.0) {
                if(areNotAllie) {
                    if (!entity2.isAlliedTo(entity)) {
                        ret.add(entity2);
                    }
                } else {
                    ret.add(entity2);
                }
            }
        }

        return ret;
    }

    public static void ParticleCircle(LivingEntity entity, double radius, ParticleOptions type) {
        for(float x = (float) (entity.getX() - (float)radius); x < entity.getX() + (float)radius; ++x) {
            for(float y = (float) (entity.getY() - (float)radius); y < entity.getY() + (float)radius; ++y) {
                for(float z = (float) (entity.getZ() - (float)radius); z < entity.getZ() + (float)radius; ++z) {
                    float cmp = (float) ((float)(radius * radius) - (entity.getX() - x) * (entity.getX() - x) - (entity.getY() - y) * (entity.getY() - y) - (entity.getZ() - z) * (entity.getZ() - z));
                    if (cmp > 0.0F && cmp < 6.1F && entity.level instanceof ServerLevel world) {
                        world.sendParticles(type, (double) x, (double) y, (double) z, 5, 1.0, 1.0, 1.0, 1.0);
                    }
                }
            }
        }
    }
}
