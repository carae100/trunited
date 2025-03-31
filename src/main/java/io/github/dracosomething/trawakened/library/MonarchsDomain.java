package io.github.dracosomething.trawakened.library;

import com.github.manasmods.tensura.ability.SkillHelper;
import io.github.dracosomething.trawakened.capability.ShadowCapability.AwakenedShadowCapability;
import io.github.dracosomething.trawakened.helper.skillHelper;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MonarchsDomain {
    private final Vec3 position;
    private final LivingEntity owner;
    private int life;
    private final double width;
    private final double length;

    public MonarchsDomain(Vec3 position, LivingEntity owner, int life, double width, double length) {
        this.position = position;
        this.owner = owner;
        this.life = life;
        this.width = width;
        this.length = length;
    }

    public MonarchsDomain(LivingEntity owner, int life, double width, double length) {
        this.position = owner.position();
        this.owner = owner;
        this.life = life;
        this.width = width;
        this.length = length;
    }

    public void Tick() {
        List<LivingEntity> list = skillHelper.GetLivingEntitiesInRange(this.owner, (int) this.calculateRadius(), false);
        list.forEach((living) -> {
            if (living != this.owner &&
                    AwakenedShadowCapability.isShadow(living) &&
                    AwakenedShadowCapability.isArisen(living) &&
                    AwakenedShadowCapability.getOwnerUUID(living).equals(this.owner.getUUID())) {
                SkillHelper.addEffectWithSource(living, this.owner, effectRegistry.MONARCHS_DOMAIN.get(), 1000, 1, false, false, false, false);
            }
        });
        if (life <= 0) {
            
        }
        --life;
    }

    private double calculateRadius() {
        return this.length/2;
    }

    private int calculateMPCost() {
        int mpCost = 0;
        List<LivingEntity> list = skillHelper.GetLivingEntitiesInRange(this.owner, (int) this.calculateRadius(), false);
        for (LivingEntity living : list) {
            mpCost += 1150;
        }
        return mpCost;
    }
}
