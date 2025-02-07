package io.github.dracosomething.trawakened.entity.barrier;

import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.entity.magic.barrier.BarrierEntity;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import io.github.dracosomething.trawakened.registry.entityRegistry;
import io.github.dracosomething.trawakened.registry.skillregistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class IntruderBarrier extends BarrierEntity {
    public IntruderBarrier(EntityType<? extends BarrierEntity> entityType, Level level) {
        super(entityType, level);
    }

    public IntruderBarrier(Level level, LivingEntity entity) {
        this(entityRegistry.INTRUDER_BARRIER.get(), level);
        this.setOwner(entity);
    }

    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return true;
    }

    public boolean canWalkThrough(Entity entity) {
        Entity owner = this.getOwner();
        if (entity == owner) {
            return true;
        } else {
            if (SkillUtils.hasSkill(entity, skillregistry.ALTERNATE.get()) && entity.getUUID().equals(owner.getPersistentData().getUUID("alternate_UUID"))) {
                return false;
            } else {
                return true;
            }
        }
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
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
            if (entity.getPersistentData().hasUUID("original_scared")) {
                this.kill();
            }
        }
    }
}
