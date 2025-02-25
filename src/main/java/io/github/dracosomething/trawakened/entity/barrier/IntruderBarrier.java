package io.github.dracosomething.trawakened.entity.barrier;

import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.entity.magic.barrier.HolyFieldEntity;
import io.github.dracosomething.trawakened.registry.entityRegistry;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class IntruderBarrier extends HolyFieldEntity {
    public IntruderBarrier(EntityType<? extends HolyFieldEntity> entityType, Level level) {
        super(entityType, level);
    }

    public IntruderBarrier(Level level, LivingEntity entity) {
        this(entityRegistry.INTRUDER_BARRIER.get(), level);
        this.setOwner(entity);
    }

    @Override
    public void applyEffect(LivingEntity entity) {
        return;
    }

    public boolean canWalkThrough(Entity entity) {
        Entity owner = this.getOwner();
        if (entity == owner) {
            return true;
        } else {
            return !SkillUtils.hasSkill(entity, skillRegistry.ALTERNATE.get());
        }
    }

    public void tick() {
        super.tick();
        Entity owner = this.getOwner();
        if (owner != null) {
            this.setPos(owner.getX(), owner.getY() - (double)(this.getRadius() / 2.0F), owner.getZ());
        } else {
            this.discard();
        }
        if (owner instanceof LivingEntity entity) {
            if (!entity.getPersistentData().hasUUID("alternate_UUID")) {
                this.kill();
            }
        }
        if (owner != null) {
            if (owner.getPersistentData().hasUUID("alternate_UUID")) {
                LivingEntity alternate = owner.level.getPlayerByUUID(owner.getPersistentData().getUUID("alternate_UUID"));
                AABB radius = this.getAffectedArea();
                List<Entity> list = owner.level.getEntities((Entity) null, new AABB(new Vec3(radius.maxX + 25, radius.maxY + 25, radius.maxZ + 25), new Vec3(radius.minX - 25, radius.minY - 25, radius.minZ - 25)));
                for (Entity entity : list) {
                    if (entity == alternate) {
                        Vec3 position = new Vec3(entity.getX(), entity.getY(), entity.getZ());
                        if (!radius.contains(position)) {
                            alternate.setPos(radius.getCenter());
                        }
                    }
                }
            }
        }
    }
}
