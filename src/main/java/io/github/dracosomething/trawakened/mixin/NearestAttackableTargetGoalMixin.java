package io.github.dracosomething.trawakened.mixin;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillUtils;
import io.github.dracosomething.trawakened.library.AlternateType;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NearestAttackableTargetGoal.class)
public class NearestAttackableTargetGoalMixin extends TargetGoal {
    public NearestAttackableTargetGoalMixin(Mob p_26140_, boolean p_26141_) {
        super(p_26140_, p_26141_);
    }

    @Shadow
    protected void findTarget() {};
    @Shadow
    protected LivingEntity target;
    @Shadow
    protected TargetingConditions targetConditions;

    @Inject(
            method = "findTarget",
            at = @At("HEAD")
    )
    private void AlternateTarget(CallbackInfo ci) {
//        LivingEntity alternate = ((NearestAttackableTargetGoal<?>)(Object)this).mob.level.getNearestPlayer(this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
//        System.out.println(alternate);
//        ManasSkillInstance instance = SkillUtils.getSkillOrNull(alternate, skillRegistry.ALTERNATE.get());
//        System.out.println(instance);
//        if (instance != null) {
//            CompoundTag tag = instance.getOrCreateTag();
//            System.out.println(tag);
//            AlternateType.Assimilation assimilation = AlternateType.Assimilation.fromNBT(tag.getCompound("assimilation"));
//            System.out.println(assimilation);
//            if (assimilation == AlternateType.Assimilation.OVERDRIVEN || assimilation == AlternateType.Assimilation.FLAWED) {
//                this.target = alternate;
//                System.out.println(this.target);
//            }
//        }
    }

    @Override
    public boolean canUse() {
        return false;
    }
}
