package io.github.dracosomething.trawakened.ability.skill.extra;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.capability.skill.TensuraSkillCapability;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.registry.skill.ExtraSkills;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.registry.skillregistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static com.github.manasmods.tensura.util.damage.TensuraDamageSources.SPACE_ATTACK;

public class conceptofinfinity extends Skill {
    public ResourceLocation getSkillIcon() {
        return new ResourceLocation("trawakened", "textures/skill/extra/concept_of_infinity.png");
    }

    @Override
    public List<MobEffect> getImmuneEffects(ManasSkillInstance instance, LivingEntity entity) {
        return List.of(effectRegistry.OVERWHELMED.get(), effectRegistry.BRAINDAMAGE.get());
    }

    public conceptofinfinity() {
        super(SkillType.EXTRA);
    }

    public boolean meetEPRequirement(Player entity, double newEP) {
        return SkillUtils.isSkillMastered(entity, ExtraSkills.SAGE.get()) && trawakenedPlayerCapability.isDemonLordSeed(entity) || trawakenedPlayerCapability.isHeroEgg(entity) && !(SkillUtils.hasSkill(entity, skillregistry.AZAZEL.get()) && SkillUtils.hasSkill(entity, skillregistry.STARKILL.get()));
    }

    @Override
    public Component getModeName(int mode) {
        MutableComponent var10000;
        switch (mode) {
            case 1 -> var10000 = Component.translatable("trawakened.skill.mode.infinity.analyze");
            case 2 -> var10000 = Component.translatable("trawakened.skill.mode.infinity.overwhelmed");
            case 3 -> var10000 = Component.translatable("trawakened.skill.mode.infinity.spatial_attack");
            default -> var10000 = Component.empty();
        }

        return var10000;
    }

    @Override
    public int modes() {
        return 3;
    }

    @Override
    public int nextMode(LivingEntity entity, TensuraSkillInstance instance, boolean reverse) {
        if (reverse) {
            return instance.getMode() == 1 ? 3 : instance.getMode() - 1;
        } else {
            return instance.getMode() == 3 ? 1 : instance.getMode() + 1;
        }
    }

    @Override
    public double magiculeCost(LivingEntity entity, ManasSkillInstance instance) {
        switch (instance.getMode()) {
            case 1 -> {
                return 0;
            }
            case 2 -> {
                return  250;
            }
            case 3 -> {
                return  100;
            }
            default -> {
                return 0;
            }
        }
    }

    private void analize(ManasSkillInstance instance, LivingEntity entity, boolean on) {
        if (entity instanceof Player) {
            if (on) {
                Player player = (Player) entity;
                TensuraSkillCapability.getFrom(player).ifPresent((cap) -> {
                    int level;
                    level = 5;
                    if (cap.getAnalysisLevel() != level) {
                        cap.setAnalysisLevel(level);
                        cap.setAnalysisDistance(instance.isMastered(entity) ? 35 : 15);
                        entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                    } else {
                        cap.setAnalysisLevel(0);
                        entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }

                    TensuraSkillCapability.sync(player);
                });
            }
        }
    }

    private boolean on;

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        switch (instance.getMode()){
            case 1:
                this.analize(instance, entity, on);
                on = !on;
                break;
            case 2:
                if (!SkillHelper.outOfMagicule(entity, instance)) {
                    AABB aabb = new AABB((double) (entity.getX() - 15), (double) (entity.getY() - 15), (double) (entity.getZ() - 15), (double) (entity.getX() + 15), (double) (entity.getY() + 15), (double) (entity.getZ() + 15));
                    List<Entity> entities = entity.level.getEntities((Entity) null, aabb, Entity::isAlive);
                    List<Entity> ret = new ArrayList();
                    new Vec3((double) entity.getX(), (double) entity.getY(), (double) entity.getZ());
                    Iterator var16 = entities.iterator();

                    while (var16.hasNext()) {
                        Entity entity2 = (Entity) var16.next();

                        int radius = 15;

                        double x = entity2.getX();
                        double y = entity2.getY();
                        double z = entity2.getZ();
                        double cmp = (double) (radius * radius) - ((double) entity2.getX() - x) * ((double) entity2.getX() - x) - ((double) entity2.getY() - y) * ((double) entity2.getY() - y) - ((double) entity2.getZ() - z) * ((double) entity2.getZ() - z);
                        if (cmp > 0.0) {
                            ret.add(entity2);
                        }
                    }

                    for (Entity entity2 : ret) {
                        if (entity2 instanceof LivingEntity) {
                            if (entity2 != entity) {
                                SkillHelper.checkThenAddEffectSource((LivingEntity) entity2, entity, (MobEffect) effectRegistry.OVERWHELMED.get(), entity.level.random.nextInt(600, 6000), 1);
                            }
                            if(entity2 instanceof Player player){
                                player.displayClientMessage(Component.translatable("hidden_Knowledge").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)), false);
                            }
                        }
                    }
                    TensuraParticleHelper.addServerParticlesAroundSelf(entity, ParticleTypes.FLASH, 0.1);
                    entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.WITHER_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    instance.setCoolDown(40);
                }
                break;
            case 3:
                LivingEntity target2 = SkillHelper.getTargetingEntity(entity, 15.0, false);
                target2.hurt(TensuraDamageSources.elementalAttack(SPACE_ATTACK, entity, false), 6);
                TensuraParticleHelper.addServerParticlesAroundSelf(target2, ParticleTypes.SWEEP_ATTACK, 0.5);
                target2.addEffect(new MobEffectInstance(effectRegistry.HEALPOISON.get(), 1000, 1, false, false, false));
                instance.setCoolDown(10);
                break;
        }
    }

    @Override
    public boolean canIgnoreCoolDown(ManasSkillInstance instance, LivingEntity entity) {
        switch (instance.getMode()){
            case 1 ->{
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public void onLearnSkill(ManasSkillInstance instance, LivingEntity living, UnlockSkillEvent event) {
        if(living instanceof Player player) {
            player.displayClientMessage(Component.translatable("learn_infinity").setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)), false);
        }

        on = false;
    }
}
