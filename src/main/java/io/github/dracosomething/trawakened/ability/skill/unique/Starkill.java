package io.github.dracosomething.trawakened.ability.skill.unique;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.capability.skill.TensuraSkillCapability;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.entity.human.CloneEntity;
import com.github.manasmods.tensura.entity.magic.breath.BreathEntity;
import com.github.manasmods.tensura.network.play2server.RequestNamingGUIPacket;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.registry.particleRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.*;

import static com.github.manasmods.tensura.util.damage.TensuraDamageSources.SPACE_ATTACK;
import static com.lowdragmc.lowdraglib.LDLib.random;

public class Starkill extends Skill {
    public ResourceLocation getSkillIcon() {
        return new ResourceLocation("trawakened", "textures/skill/analog_horror_skills/unique/starkill.png");
    }

    public Starkill() {
        super(SkillType.UNIQUE);
    }

    @Override
    public int modes() {
        return 3;
    }

    @Override
    public double getObtainingEpCost() {
        return 10000;
    }

    @Override
    public double learningCost() {
        return 1000;
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
        int mode = entity.getPersistentData().getInt("mode");
        double var10000 = 0;
        switch (instance.getMode()) {
            case 1 -> var10000 = 1000;
            case 2 -> var10000 = 20;
            case 3 -> {
                if(entity.isShiftKeyDown()){
                    return 0;
                }
                else {
                    switch (mode) {
                        case 1 -> var10000 = 0;
                        case 2 -> var10000 = 250;
                        case 3 -> var10000 = 100;
                        default -> var10000 = 0;
                    }
                }
            }
        }

        return var10000;
    }

    @Override
    public Component getModeName(int mode) {
        MutableComponent var10000;
        switch (mode) {
            case 1 -> var10000 = Component.translatable("trawakened.skill.mode.starkill.create_apostle");
            case 2 -> var10000 = Component.translatable("trawakened.skill.mode.starkill.assimilation");
            case 3 -> var10000 = Component.translatable("trawakened.skill.mode.starkill.infinity");
            default -> var10000 = Component.empty();
        }

        return var10000;
    }

    private boolean on;

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        int mode = entity.getPersistentData().getInt("mode");
        switch (instance.getMode()){
            case 1:
                Level level = entity.getLevel();
                double var10000;
                LivingEntity target = SkillHelper.getTargetingEntity(entity, 5.0, false);
                if (entity instanceof Player) {
                    Player player = (Player)entity;
                    var10000 = TensuraPlayerCapability.getBaseMagicule(player);
                } else {
                    var10000 = TensuraEPCapability.getEP(entity);
                }

                double MP = var10000;
                double EP = MP * 0.1;
                if (!SkillHelper.outOfMagicule(entity, EP)) {
                    if(target instanceof Player player) {
                        BlockHitResult result = SkillHelper.getPlayerPOVHitResult(target.level, target, ClipContext.Fluid.NONE, 0.0);
                        CloneEntity clone = this.summonClone(target, entity, level, EP, result.getLocation());
                        CloneEntity.copyEffects(target, clone);
                        EquipmentSlot[] var10 = EquipmentSlot.values();
                        int var11 = var10.length;

                        for (int var12 = 0; var12 < var11; ++var12) {
                            EquipmentSlot slot = var10[var12];
                            clone.setItemSlot(slot, target.getItemBySlot(slot).copy());
                        }
                        double damage = player.getHealth() - (player.getMaxHealth()*0.75);
                        if (damage <= player.getHealth()){
                            player.kill();
                        } else {
                            player.setHealth((float) damage);
                        }                        this.addMasteryPoint(instance, entity);
                    } else {
                        if(target instanceof TamableAnimal animal){
                            if(entity instanceof Player player) {
                                animal.tame(player);
                                this.addMasteryPoint(instance, entity);
                            }
                        } else {
                            double ep = TensuraEPCapability.getEP(target);
                            TensuraEPCapability.setLivingEP(target, 100);
                            assert entity instanceof ServerPlayer;
                            RequestNamingGUIPacket.name(target, (ServerPlayer) entity, RequestNamingGUIPacket.NamingType.LOW, "");
                            target.setCustomName(Component.empty());
                            target.setCustomNameVisible(true);
                            TensuraEPCapability.setLivingEP(target, ep);
                        }
                    }
                    instance.setCoolDown(35);
                    instance.addMasteryPoint(entity);
                }
            break;
            case 2:
            default:
                break;
            case 3:
                if(!SkillHelper.outOfMagicule(entity, instance)){
                    if(entity.isShiftKeyDown()){
                        if(entity instanceof Player player) {
                            switch (mode) {
                                case 1:
                                    player.displayClientMessage(Component.translatable("trawakened.skill.mode.starkill.infinity.overwhelmed").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                                    player.getPersistentData().putInt("mode", 2);                                    break;
                                case 2:
                                    player.displayClientMessage(Component.translatable("trawakened.skill.mode.starkill.infinity.spatial_attack").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                                    player.getPersistentData().putInt("mode", 3);                                    break;
                                case 3:
                                    player.displayClientMessage(Component.translatable("trawakened.skill.mode.starkill.infinity.analyze").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                                    player.getPersistentData().putInt("mode", 1);
                                    break;
                            }
                            if(instance.onCoolDown()) {
                                instance.setCoolDown(instance.getCoolDown());
                            }
                        }
                    } else {
                        switch (mode){
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
                                    instance.addMasteryPoint(entity);
                                }
                                break;
                            case 3:
                                if(!SkillHelper.outOfMagicule(entity, instance)) {
                                    LivingEntity target2 = SkillHelper.getTargetingEntity(entity, 15.0, false);
                                    target2.hurt(TensuraDamageSources.elementalAttack(SPACE_ATTACK, entity, false).bypassArmor().bypassEnchantments().bypassMagic(), 6);
                                    TensuraParticleHelper.addServerParticlesAroundSelf(target2, ParticleTypes.SWEEP_ATTACK, 0.5);
                                    target2.addEffect(new MobEffectInstance(effectRegistry.HEALPOISON.get(), 1000, 1, false, false, false));
                                    instance.setCoolDown(10);
                                    instance.addMasteryPoint(entity);
                                }
                                break;
                        }
                    }
                }
                break;
        }
    }

    @Override
    public List<MobEffect> getImmuneEffects(ManasSkillInstance instance, LivingEntity entity) {
        return List.of(effectRegistry.OVERWHELMED.get(), effectRegistry.BRAINDAMAGE.get());
    }

    private void createLineParticles(Level pLevel, LivingEntity pLivingEntity,double length) {
        Vec3 lookVec = pLivingEntity.getLookAngle();
        Vec3 startPos = pLivingEntity.getEyePosition(1.0F);
        System.out.println(lookVec);

        HitResult hitResult = pLevel.clip(new ClipContext(startPos, startPos.add(lookVec.scale(20.0)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, pLivingEntity));

        int particleCount = 20; // Number of particles in the line
        double swerveAmplitude = 0.8;
        double swerveFrequency = 2.0;

        for (int i = 0; i < particleCount; i++) {
            double t = i / (double) (particleCount - 1);
            Vec3 swerveVec = calculateSwerve(lookVec, t, swerveAmplitude, swerveFrequency);

            Vec3 pos = startPos.add(lookVec.scale(hitResult.getLocation().distanceTo(startPos) * t)).add(swerveVec);

//            System.out.println(length + "------" + pos + "--------" + pos.length());

            double speedMultiplier = 0.5 + (1 - t) * 0.1;

            for (double j = 0; j < length; j++) {
                TensuraParticleHelper.spawnServerParticles(pLevel, particleRegistry.FLESHPARTICLE.get(), pos.x, pos.y - 0.3, pos.z, 1, lookVec.x * speedMultiplier, lookVec.y * speedMultiplier, lookVec.z * speedMultiplier, 0.1, true);
            }
        }
    }
    private Vec3 calculateSwerve(Vec3 lookVec, double t, double amplitude, double frequency) {
        double swerveX = Math.sin(t * Math.PI * frequency) * amplitude;
        double swerveY = Math.cos(t * Math.PI * frequency) * amplitude;

        return lookVec.cross(new Vec3(0, 1, 0)).normalize().scale(swerveX)
                .add(lookVec.cross(new Vec3(1, 0, 0)).normalize().scale(swerveY))
                .add(new Vec3((random.nextDouble() - 0.5) * 0.1,
                        (random.nextDouble() - 0.5) * 0.1,
                        (random.nextDouble() - 0.5) * 0.1));
    }

    private int heldseconds = 0;

    public boolean onHeld(ManasSkillInstance instance, LivingEntity entity, int heldTicks) {
        if (instance.onCoolDown()) {
            return false;
        } else {
            if (instance.getMode() == 2) {
                if (heldTicks % 60 == 0 && heldTicks > 0) {
                    this.addMasteryPoint(instance, entity);
                } else if (heldTicks % 20 == 0){
                    heldseconds++;
                }
                createLineParticles(entity.level, entity, 5);
                BreathEntity.spawnPredationMist((EntityType) TensuraEntityTypes.GLUTTONY_MIST.get(), entity, instance, this.magiculeCost(entity, instance), (float) 5, 0, true);
                return true;
            }
            return false;
        }

    }

    @Override
    public void onSkillMastered(ManasSkillInstance instance, LivingEntity living) {
        living.getPersistentData().putInt("assimilation_kills", 0);
    }

    @Override
    public boolean canIgnoreCoolDown(ManasSkillInstance instance, LivingEntity entity) {
        int mode = entity.getPersistentData().getInt("mode");
        switch (instance.getMode()) {
            case 1, 2 -> {
                return false;
            }
            case 3 -> {
                if(entity.isShiftKeyDown()){
                    return true;
                }else {
                    switch (mode) {
                        case 2, 3 -> {
                            return false;
                        }
                        case 1 -> {
                            return true;
                        }
                        default -> {
                            return false;
                        }
                    }
                }
            }
            default -> {
                return true;
            }
        }
    }

    @Override
    public void onRelease(ManasSkillInstance instance, LivingEntity entity, int heldTicks) {
        if (instance.getMode() == 2){
            if(heldTicks >= 5) {
                if (heldseconds != 0) {
                    instance.setCoolDown(5 * heldseconds);
                } else {
                    instance.setCoolDown(5);
                }
                heldseconds = 0;
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

    private CloneEntity summonClone(LivingEntity entity, LivingEntity owner, Level level, double EP, Vec3 position) {
        EntityType<CloneEntity> type = entity.isShiftKeyDown() ? (EntityType) TensuraEntityTypes.CLONE_SLIM.get() : (EntityType)TensuraEntityTypes.CLONE_DEFAULT.get();
        CloneEntity clone = new CloneEntity(type, level);
        if (owner instanceof Player player) {
            clone.tame(player);
        }

        clone.setSkill(this);
        clone.copyStatsAndSkills(entity, true);
        TensuraEPCapability.setLivingEP(clone, EP);
        clone.setHealth(clone.getMaxHealth());
        clone.setPos(position);
        CloneEntity.copyRotation(entity, clone);
        level.addFreshEntity(clone);
        TensuraParticleHelper.addServerParticlesAroundSelf(owner, particleRegistry.FLESHPARTICLE.get(), 1.0);
        TensuraParticleHelper.addServerParticlesAroundSelf(owner, particleRegistry.FLESHPARTICLE.get(), 2.0);
        TensuraParticleHelper.addServerParticlesAroundSelf(entity, particleRegistry.FLESHPARTICLE.get(), 1.0);
        TensuraParticleHelper.addServerParticlesAroundSelf(entity, particleRegistry.FLESHPARTICLE.get(), 2.0);
        return clone;
    }

    @Override
    public void onDeath(ManasSkillInstance instance, LivingDeathEvent event) {
        event.getEntity().getPersistentData().putInt("mode", 1);
        if(instance.isMastered(event.getEntity())){
            event.getEntity().getPersistentData().putInt("assimilation_kills", 0);
        }
    }

    @Override
    public void onRespawn(ManasSkillInstance instance, PlayerEvent.PlayerRespawnEvent event) {
        event.getEntity().getPersistentData().putInt("mode", 1);
        if(instance.isMastered(event.getEntity())){
            event.getEntity().getPersistentData().putInt("assimilation_kills", 0);
        }
    }

    @Override
    public void onLearnSkill(ManasSkillInstance instance, LivingEntity living, UnlockSkillEvent event) {
        if(living instanceof Player player) {
            player.displayClientMessage(Component.translatable("starkill").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_RED)), false);
        }

        living.getPersistentData().putInt("mode", 1);
        on = false;
    }
}
