package io.github.dracosomething.trawakened.entity.barrier;

import com.github.manasmods.manascore.skill.SkillRegistry;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.entity.magic.barrier.BarrierEntity;
import com.github.manasmods.tensura.entity.magic.barrier.HolyFieldEntity;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import io.github.dracosomething.trawakened.registry.entityRegistry;
import io.github.dracosomething.trawakened.registry.skillregistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

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
            return !SkillUtils.hasSkill(entity, skillregistry.ALTERNATE.get());
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
            if (entity.getPersistentData().hasUUID("original_scared")) {
                this.kill();
            }
        }
    }
}
