package io.github.dracosomething.trawakened.ability.skill.unique;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.capability.skill.TensuraSkillCapability;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.config.TensuraConfig;
import com.github.manasmods.tensura.entity.human.CloneEntity;
import com.github.manasmods.tensura.event.NamingEvent;
import com.github.manasmods.tensura.network.play2server.RequestNamingGUIPacket;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import com.github.manasmods.tensura.util.TensuraAdvancementsHelper;
import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.dracosomething.trawakened.capability.skillCapability;
import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.registry.particleRegistry;
import io.github.dracosomething.trawakened.registry.skillregistry;
import io.github.dracosomething.trawakened.util.trawakenedDamage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static com.lowdragmc.lowdraglib.LDLib.random;

public class akashic_plane extends Skill {
    public ResourceLocation getSkillIcon() {
        return new ResourceLocation("trawakened", "textures/skill/analog_horror_skills/unique/akashic.png");
    }

    public akashic_plane() {
        super(SkillType.UNIQUE);
    }

    @Override
    public boolean meetEPRequirement(Player entity, double newEP) {
        return !(SkillUtils.hasSkill(entity, skillregistry.AZAZEL.get()) && SkillUtils.hasSkill(entity, skillregistry.STARKILL.get())) && SkillUtils.isSkillMastered(entity, skillregistry.CONCEPTOFINFINITY.get()) && TensuraPlayerCapability.isTrueDemonLord(entity) || TensuraPlayerCapability.isTrueHero(entity) && TensuraPlayerCapability.getCurrentEP(entity) == 1000000;
    }

    @Override
    public int modes() {
        return 4;
    }

    @Override
    public double getObtainingEpCost() {
        return 10000;
    }

    @Override
    public double learningCost() {
        return 200;
    }

    @Override
    public int nextMode(LivingEntity entity, TensuraSkillInstance instance, boolean reverse) {
        if (reverse) {
            return instance.getMode() == 1 ? 4 : instance.getMode() - 1;
        } else {
            return instance.getMode() == 4 ? 1 : instance.getMode() + 1;
        }
    }

    @Override
    public double magiculeCost(LivingEntity entity, ManasSkillInstance instance) {
        double var10000 = 0;
        switch (instance.getMode()) {
            case 1 -> var10000 = 0;
            case 2 -> var10000 = 250;
            case 3 -> var10000 = 100;
            case 4 -> var10000 = 150;
            default -> var10000 = 0;
        }

        return var10000;
    }

    @Override
    public Component getModeName(int mode) {
        MutableComponent var10000;
        switch (mode) {
            case 1 -> var10000 = Component.translatable("trawakened.skill.mode.akashic.analyze");
            case 2 -> var10000 = Component.translatable("trawakened.skill.mode.akashic.overwhelmed");
            case 3 -> var10000 = Component.translatable("trawakened.skill.mode.akashic.spatial_attack");
            case 4 -> var10000 = Component.translatable("trawakened.skill.mode.akashic.podolsky");
            default -> var10000 = Component.empty();
        }

        return var10000;
    }

    private boolean on;

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        switch (instance.getMode()) {
            case 1:
                this.analize(instance, entity, on);
                on = !on;
                break;
            case 2:
                if (!SkillHelper.outOfMagicule(entity, instance)) {
                    List<Entity> ret = DrawCircle(entity, 15, true);

                    for (Entity entity2 : ret) {
                        if (entity2 instanceof LivingEntity) {
                            if (entity2 != entity) {
                                SkillHelper.checkThenAddEffectSource((LivingEntity) entity2, entity, (MobEffect) effectRegistry.BRAINDAMAGE.get(), entity.level.random.nextInt(600, 6000), 2);
                            }
                            if (entity2 instanceof Player player) {
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
                if (!SkillHelper.outOfMagicule(entity, instance)) {
                    LivingEntity target2 = SkillHelper.getTargetingEntity(entity, 25.0, false);
                    assert target2 != null;
                    target2.hurt(TensuraDamageSources.SEVERANCE_UPDATE, 25);
                    TensuraParticleHelper.addServerParticlesAroundSelf(target2, ParticleTypes.SWEEP_ATTACK, 0.5);
                    target2.addEffect(new MobEffectInstance(effectRegistry.HEALPOISON.get(), 2000, 1, false, false, false));
                    instance.setCoolDown(10);
                }
                break;
            case 4:
                if(entity.isShiftKeyDown()) {
                    String newRange = switch (entity.getPersistentData().getString("thought_mode")) {
                        case "aggressive" -> "passive";
                        case "passive" -> "neutral";
                        case "neutral" -> "wander";
                        case "wander" -> "follow";
                        case "follow" -> "stay";
                        case "stay" -> "aggressive";
                        default -> "";
                    };
                    System.out.println(entity.getPersistentData().getString("thought_mode"));
                    if (entity.getPersistentData().getString("thought_mode") != newRange) {
                        entity.getPersistentData().putString("thought_mode", newRange);
                        if (entity instanceof Player) {
                            Player player = (Player) entity;
                            player.displayClientMessage(Component.translatable("trawakened.skill.thought.mode", new Object[]{newRange}).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)), true);
                        }

                        instance.markDirty();
                    }
                } else if (!SkillHelper.outOfMagicule(entity, instance) && !entity.isSprinting()) {
                    List<Entity> ret = DrawCircle(entity, 60, false);

                    for (Entity entity2 : ret) {
                        if (entity2 instanceof LivingEntity living && SkillHelper.isSubordinate(entity, living)) {
                            switch (entity.getPersistentData().getString("thought_mode")) {
                                case "aggressive":
                                    SkillHelper.setAggressive(living);
                                    break;
                                case "passive":
                                    SkillHelper.setPassive(living);
                                    break;
                                case "neutral":
                                    SkillHelper.setNeutral(living);
                                    break;
                                case "wander":
                                    SkillHelper.setWander(living);
                                    break;
                                case "follow":
                                    SkillHelper.setFollow(living);
                                    break;
                                case "stay":
                                    if (SkillHelper.isOrderedToStay(living)) {
                                        SkillHelper.setWander(living);
                                    } else {
                                        SkillHelper.setStay(living);
                                    }
                                    break;
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public boolean canIgnoreCoolDown(ManasSkillInstance instance, LivingEntity entity) {
        switch (instance.getMode()) {
            case 1 -> {
                return true;
            }
            case 2, 3 -> {
                return false;
            }
            case 4 -> {
                return entity.isShiftKeyDown();
            }
            default -> {
                return true;
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

    private List<Entity> DrawCircle(LivingEntity entity, int radius, boolean allie_check){
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
                if(allie_check) {
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

    @Override
    public List<MobEffect> getImmuneEffects(ManasSkillInstance instance, LivingEntity entity) {
        return List.of(effectRegistry.OVERWHELMED.get(), effectRegistry.BRAINDAMAGE.get());
    }

    @Override
    public void onDeath(ManasSkillInstance instance, LivingDeathEvent event) {
        event.getEntity().getPersistentData().putString("thought_mode", "aggressive");
    }

    @Override
    public void onRespawn(ManasSkillInstance instance, PlayerEvent.PlayerRespawnEvent event) {
        event.getEntity().getPersistentData().putString("thought_mode", "aggressive");
    }

    @Override
    public void onLearnSkill(ManasSkillInstance instance, LivingEntity living, UnlockSkillEvent event) {
        if(living instanceof Player player) {
            player.displayClientMessage(Component.translatable("akashic").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_RED)), false);
        }

        living.getPersistentData().putString("thought_mode", "aggressive");
        on = false;
    }
}
