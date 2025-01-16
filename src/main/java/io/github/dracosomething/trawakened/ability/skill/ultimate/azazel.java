package io.github.dracosomething.trawakened.ability.skill.ultimate;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.capability.effects.TensuraEffectsCapability;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.capability.skill.TensuraSkillCapability;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.config.TensuraConfig;
import com.github.manasmods.tensura.data.TensuraTags;
import com.github.manasmods.tensura.entity.human.CloneEntity;
import com.github.manasmods.tensura.entity.magic.barrier.BarrierEntity;
import com.github.manasmods.tensura.entity.magic.breath.BreathEntity;
import com.github.manasmods.tensura.event.NamingEvent;
import com.github.manasmods.tensura.network.play2server.RequestNamingGUIPacket;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import com.github.manasmods.tensura.registry.magic.SpiritualMagics;
import com.github.manasmods.tensura.registry.particle.TensuraParticles;
import com.github.manasmods.tensura.util.TensuraAdvancementsHelper;
import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.dracosomething.trawakened.capability.skillCapability;
import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import io.github.dracosomething.trawakened.mixin.GluttonyMistProjectileMixin;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.registry.particleRegistry;
import io.github.dracosomething.trawakened.registry.skillregistry;
import io.github.dracosomething.trawakened.util.trawakenedDamage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.*;

import static com.github.manasmods.tensura.ability.skill.common.ThoughtCommunicationSkill.targetingBehaviour;
import static com.github.manasmods.tensura.util.damage.TensuraDamageSources.SPACE_ATTACK;
import static com.lowdragmc.lowdraglib.LDLib.random;

public class azazel extends Skill {
    private List<EntityType> summonEntities = List.of(EntityType.ZOMBIE, EntityType.PIGLIN_BRUTE, EntityType.SKELETON, EntityType.ZOMBIFIED_PIGLIN, EntityType.ZOMBIE_VILLAGER, EntityType.ZOGLIN, EntityType.CREEPER, EntityType.EVOKER, EntityType.PIGLIN_BRUTE, EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN, EntityType.ILLUSIONER, EntityType.PILLAGER, EntityType.VINDICATOR, EntityType.SILVERFISH, EntityType.RAVAGER, EntityType.HUSK, EntityType.DROWNED, EntityType.HOGLIN, EntityType.PIGLIN, EntityType.SPIDER, EntityType.CAVE_SPIDER, EntityType.PHANTOM, EntityType.STRAY, TensuraEntityTypes.BARGHEST.get(), TensuraEntityTypes.ARMOURSAURUS.get(), TensuraEntityTypes.BLACK_SPIDER.get(), TensuraEntityTypes.KNIGHT_SPIDER.get());

    public ResourceLocation getSkillIcon() {
        return new ResourceLocation("trawakened", "textures/skill/analog_horror_skills/ultimate/azazel.png");
    }

    public azazel() {
        super(SkillType.ULTIMATE);
    }

    @Override
    public boolean meetEPRequirement(Player entity, double newEP) {
        return entity.getPersistentData().getInt("assimilation_kills") == 300 && SkillUtils.isSkillMastered(entity, skillregistry.STARKILL.get()) && trawakenedPlayerCapability.isDemonLordSeed(entity) || trawakenedPlayerCapability.isHeroEgg(entity);
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
        return 1;
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
        int mode = entity.getPersistentData().getInt("mode_azazel");
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
                        case 4 -> var10000 = 150;
                        default -> var10000 = 0;
                    }
                }
            }
            case 4 -> var10000 = 500;
        }

        return var10000;
    }

    @Override
    public Component getModeName(int mode) {
        MutableComponent var10000;
        switch (mode) {
            case 1 -> var10000 = Component.translatable("trawakened.skill.mode.azazel.infect");
            case 2 -> var10000 = Component.translatable("trawakened.skill.mode.azazel.assimilation");
            case 3 -> var10000 = Component.translatable("trawakened.skill.mode.azazel.akashic");
            case 4 -> var10000 = Component.translatable("trawakened.skill.mode.azazel.apostle");
            default -> var10000 = Component.empty();
        }

        return var10000;
    }

    private boolean on;

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        int mode = entity.getPersistentData().getInt("mode_azazel");
        int infect_mode = entity.getPersistentData().getInt("infect_mode");
        switch (instance.getMode()){
            case 1:
                List<Entity> entityList = DrawCircle(entity, 15, false);

                if(entity.isShiftKeyDown()){
                    if(entity instanceof Player player) {
                        switch (infect_mode) {
                            case 1:
                                player.displayClientMessage(Component.translatable("trawakened.skill.mode.azazel.infect.melt").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                                player.getPersistentData().putInt("infect_mode", 2);                                    break;
                            case 2:
                                player.displayClientMessage(Component.translatable("trawakened.skill.mode.azazel.infect.apostle").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                                player.getPersistentData().putInt("infect_mode", 1);                                    break;
                        }
                        if(instance.onCoolDown()) {
                            instance.setCoolDown(instance.getCoolDown());
                        }
                    }
                } else {
                    switch (infect_mode) {
                        case 1: {
                            for (Entity entity2 : entityList) {
                                Level level = entity.getLevel();
                                double var10000;
                                if (entity instanceof Player) {
                                    Player player = (Player) entity;
                                    var10000 = TensuraPlayerCapability.getBaseMagicule(player);
                                } else {
                                    var10000 = TensuraEPCapability.getEP(entity);
                                }

                                double MP = var10000;
                                double EP = MP * 0.1;
                                if (!SkillHelper.outOfMagicule(entity, EP)) {
                                    if (entity2 instanceof Player player && player != entity) {
                                        BlockHitResult result = SkillHelper.getPlayerPOVHitResult(player.level, player, ClipContext.Fluid.NONE, 0.0);
                                        CloneEntity clone = this.summonClone(player, entity, level, EP, result.getLocation());
                                        CloneEntity.copyEffects(player, clone);
                                        EquipmentSlot[] var10 = EquipmentSlot.values();
                                        int var11 = var10.length;

                                        for (int var12 = 0; var12 < var11; ++var12) {
                                            EquipmentSlot slot = var10[var12];
                                            clone.setItemSlot(slot, player.getItemBySlot(slot).copy());
                                        }
                                        double damage = player.getHealth() - (player.getMaxHealth()*0.75);
                                        if (damage <= player.getHealth()){
                                            player.kill();
                                        } else {
                                            player.setHealth((float) damage);
                                        }
                                        this.addMasteryPoint(instance, entity);
                                    } else {
                                        if (entity2 instanceof TamableAnimal animal) {
                                            if (entity instanceof Player player) {
                                                animal.tame(player);
                                                this.addMasteryPoint(instance, entity);
                                            }
                                        } else {
                                            assert entity2 instanceof LivingEntity;
                                            double ep = TensuraEPCapability.getEP((LivingEntity) entity2);
                                            TensuraEPCapability.setLivingEP((LivingEntity) entity2, 100);
                                            assert entity instanceof ServerPlayer;
                                            name((LivingEntity) entity2, (ServerPlayer) entity, RequestNamingGUIPacket.NamingType.HIGH, "");
                                            entity2.setCustomName(Component.empty());
                                            entity2.setCustomNameVisible(true);
                                            TensuraEPCapability.setLivingEP((LivingEntity) entity2, ep);
                                        }
                                    }
                                    instance.setCoolDown(35);
                                }
                            }
                        }
                        break;
                        case 2: {
                            for (Entity entity1 : entityList) {
                                if (entity1 instanceof LivingEntity entity2 && entity2 != entity) {
                                    if(!entity2.hasEffect(effectRegistry.MELT.get())) {
                                        entity2.addEffect(new MobEffectInstance(effectRegistry.MELT.get(), 5000, 1, true, true, true));
                                    }
                                }
                            }
                        }
                    }
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
                                    player.displayClientMessage(Component.translatable("trawakened.skill.mode.azazel.akashic.overwhelmed").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                                    player.getPersistentData().putInt("mode_azazel", 2);                                    break;
                                case 2:
                                    player.displayClientMessage(Component.translatable("trawakened.skill.mode.azazel.akashic.spatial_attack").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                                    player.getPersistentData().putInt("mode_azazel", 3);                                    break;
                                case 3:
                                    player.displayClientMessage(Component.translatable("trawakened.skill.mode.azazel.akashic.podolsky").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                                    player.getPersistentData().putInt("mode_azazel", 4);
                                    break;
                                case 4:
                                    player.displayClientMessage(Component.translatable("trawakened.skill.mode.azazel.akashic.analyze").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                                    player.getPersistentData().putInt("mode_azazel", 1);
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
                                    List<Entity> ret = DrawCircle(entity, 15, true);

                                    for (Entity entity2 : ret) {
                                        if (entity2 instanceof LivingEntity) {
                                            if (entity2 != entity) {
                                                SkillHelper.checkThenAddEffectSource((LivingEntity) entity2, entity, (MobEffect) effectRegistry.BRAINDAMAGE.get(), entity.level.random.nextInt(600, 6000), 2);
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
                                if (!SkillHelper.outOfMagicule(entity, instance)) {
                                    LivingEntity target2 = SkillHelper.getTargetingEntity(entity, 25.0, false);
                                    assert target2 != null;
                                    target2.hurt(TensuraDamageSources.elementalAttack(SPACE_ATTACK, entity, false), 25);
                                    TensuraParticleHelper.addServerParticlesAroundSelf(target2, ParticleTypes.SWEEP_ATTACK, 0.5);
                                    target2.addEffect(new MobEffectInstance(effectRegistry.HEALPOISON.get(), 2000, 1, false, false, false));
                                    instance.setCoolDown(10);
                                }
                                break;
                            case 4:
                                if (!SkillHelper.outOfMagicule(entity, instance) && !entity.isSprinting()) {
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
                                                    if(SkillHelper.isOrderedToStay(living)){
                                                        SkillHelper.setWander(living);
                                                    } else {
                                                        SkillHelper.setStay(living);
                                                    }
                                                    break;
                                            }
                                            instance.setCoolDown(20);
                                        }
                                    }
                                }
                        }
                    }
                }
                break;
            case 4:
                if(!SkillHelper.outOfMagicule(entity, instance)){
                    if(entity.getPersistentData().getInt("assimilation_kills") >= 1) {
                        Level level = entity.level;
                        System.out.println(level);
                        int Index = level.random.nextInt(0, summonEntities.toArray().length);
                        System.out.println(Index);
                        Entity entity1 = summonEntities.get(Index).create(level);
                        System.out.println(entity1);
                        TensuraParticleHelper.addServerParticlesAroundSelf(entity1, particleRegistry.FLESHPARTICLE.get(), 1.0);
                        TensuraParticleHelper.addServerParticlesAroundSelf(entity1, particleRegistry.FLESHPARTICLE.get(), 2.0);
                        TensuraParticleHelper.addServerParticlesAroundSelf(entity, particleRegistry.FLESHPARTICLE.get(), 1.0);
                        TensuraParticleHelper.addServerParticlesAroundSelf(entity, particleRegistry.FLESHPARTICLE.get(), 2.0);
                        Vec3 lookVec = entity.getEyePosition(1.0F);
                        System.out.println(lookVec);
                        entity1.setPos(lookVec);
                        System.out.println(entity1.getPosition(1.0F));
                        level.addFreshEntity(entity1);
                        name((LivingEntity) entity1, (ServerPlayer) entity, RequestNamingGUIPacket.NamingType.HIGH, "");
                        entity.getPersistentData().putInt("assimilation_kills", entity.getPersistentData().getInt("assimilation_kills")-1);
                    }
                }
                break;
        }
    }

    @Override
    public void onScroll(ManasSkillInstance instance, LivingEntity living, double delta) {
        int mode = living.getPersistentData().getInt("mode_azazel");
        if (instance.getMode() == 3 && mode == 4 && living.isSprinting()) {
            String newRange = switch (living.getPersistentData().getString("thought_mode")) {
                case "aggressive" -> "passive";
                case "passive" -> "neutral";
                case "neutral" -> "wander";
                case "wander" -> "follow";
                case "follow" -> "stay";
                case "stay" -> "aggressive";
                default -> "";
            };
            System.out.println(living.getPersistentData().getString("thought_mode"));
            if (living.getPersistentData().getString("thought_mode") != newRange) {
                living.getPersistentData().putString("thought_mode", newRange);
                if (living instanceof Player) {
                    Player player = (Player)living;
                    player.displayClientMessage(Component.translatable("trawakened.skill.thought.mode", new Object[]{newRange}).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)), true);
                }

                instance.markDirty();
            }
        }
    }

    public static void name(LivingEntity sub, @Nullable ServerPlayer owner, RequestNamingGUIPacket.NamingType type, String name) {
        double var20000;
        if (sub instanceof Player player) {
            var20000 = TensuraPlayerCapability.getBaseEP(player);
        } else {
            var20000 = TensuraEPCapability.getEP(sub);
        }

        double subEP = var20000;
        TensuraEPCapability.getFrom(sub).ifPresent((namingCap) -> {
            double var10000 = 0;

            double originalCost = var10000;
            NamingEvent event = new NamingEvent(sub, owner, originalCost, type, name);
            if (!MinecraftForge.EVENT_BUS.post(event)) {
                originalCost = event.getOriginalCost();
                double cost = Math.min(event.getCalculatedCost(), (Double) TensuraConfig.INSTANCE.namingConfig.maxCost.get());
                if (owner != null && TensuraPlayerCapability.getMagicule(owner) < cost) {
                    owner.displayClientMessage(Component.translatable("tensura.skill.lack_magicule").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                } else {
                    namingCap.setName(event.getName());
                    sub.setCustomName(Component.literal(event.getName()));
                    if (owner != null) {
                        namingCap.setPermanentOwner(owner.getUUID());
                        TensuraAdvancementsHelper.grant(owner, TensuraAdvancementsHelper.Advancements.NAME_A_MOB);
                    }
                    if (sub instanceof Player) {
                        Player player = (Player)sub;
                        player.refreshDisplayName();
                        if (owner != null) {
                            player.displayClientMessage(Component.translatable("tensura.naming.name_success", new Object[]{event.getName(), owner.getName()}).setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)), false);
                        } else {
                            player.displayClientMessage(Component.translatable("tensura.naming.name_success.no_namer", new Object[]{event.getName()}).setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)), false);
                        }

                        TensuraPlayerCapability.setTrackedRace(player, (Race)null);
                        TensuraPlayerCapability.sync(player);
                        TensuraEPCapability.sync(player);
                    } else if (owner != null) {
                        label45: {
                            if (sub instanceof TamableAnimal) {
                                TamableAnimal tamable = (TamableAnimal)sub;
                                if (!ForgeEventFactory.onAnimalTame(tamable, owner)) {
                                    tamable.tame(owner);
                                    break label45;
                                }
                            }

                            if (sub instanceof AbstractHorse) {
                                AbstractHorse horse = (AbstractHorse)sub;
                                if (!ForgeEventFactory.onAnimalTame(horse, owner)) {
                                    horse.tameWithName(owner);
                                }
                            }
                        }
                    }

                    sub.heal(sub.getMaxHealth());
                    SkillHelper.removePredicateEffect(sub, (effect) -> {
                        return effect.getCategory().equals(MobEffectCategory.HARMFUL);
                    });
                }
            }
        });
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

        lookVec = pLivingEntity.getLookAngle().add(0.5, 0,0.5);
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

        lookVec = pLivingEntity.getLookAngle().add(-0.5, 0,-0.5);
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

        lookVec = pLivingEntity.getLookAngle().add(1, 0,1);
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

        lookVec = pLivingEntity.getLookAngle().add(-1, 0,-1);
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
        lookVec = pLivingEntity.getLookAngle().add(-1.5, 0,-1.5);
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
        lookVec = pLivingEntity.getLookAngle().add(1.5, 0,1.5);
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

        lookVec = pLivingEntity.getLookAngle().add(2, 0,2);
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

        lookVec = pLivingEntity.getLookAngle().add(-2, 0,-2);
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
                createLineParticles(entity.level, entity, 1);
                List<Entity> ret = DrawCircle(entity, 15, true);
                for (Entity entity2 : ret) {
                    DamageSource damageSource = TensuraDamageSources.DEVOURED;
                    LivingEntity owner;
                    if (entity instanceof LivingEntity) {
                        owner = (LivingEntity)entity;
                        damageSource = trawakenedDamage.assimilation(owner);
                    }
                    if(entity != null){
                        if(SkillUtils.hasSkill(entity, skillregistry.AZAZEL.get())){
                            if(entity2.hurt(DamageSourceHelper.addSkillAndCost(damageSource, 20.0,  SkillUtils.getSkillOrNull(entity, Objects.requireNonNull(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:starkill"))))).bypassArmor().bypassEnchantments().bypassMagic(), 75)){
                                if (entity instanceof LivingEntity && entity2 instanceof LivingEntity) {
                                    owner = (LivingEntity)entity;
                                    owner.getPersistentData().putInt("assimilation_kills", owner.getPersistentData().getInt("assimilation_kills")+1);

                                    skillCapability.devourAllSkills((LivingEntity) entity2, owner);
                                    skillCapability.devourEP((LivingEntity) entity2, owner, 0.8F);
                                    if (owner instanceof Player) {
                                        Player player = (Player)owner;
                                        List<ItemEntity> list = owner.level.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(entity.position(), 2.0, 2.0, 2.0));
                                        Iterator var8 = list.iterator();

                                        while(var8.hasNext()) {
                                            ItemEntity item = (ItemEntity)var8.next();
                                            if (player.addItem(item.getItem())) {
                                                item.discard();
                                            } else {
                                                item.moveTo(owner.position());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return true;
            }
            return false;
        }

    }

    @Override
    public boolean canIgnoreCoolDown(ManasSkillInstance instance, LivingEntity entity) {
        int mode = entity.getPersistentData().getInt("mode_azazel");
        switch (instance.getMode()) {
            case 1 -> {
                return entity.isShiftKeyDown();
            }
            case 2, 4 -> {
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

    private void ParticleSummon(LivingEntity entity, int radius, ParticleOptions particle){
        AABB aabb = new AABB((double) (entity.getX() - radius), (double) (entity.getY() - radius), (double) (entity.getZ() - radius), (double) (entity.getX() + radius), (double) (entity.getY() + radius), (double) (entity.getZ() + radius));
        for(float x = (float) (entity.getX() - (float)radius); x < entity.getX() + (float)radius + 1.0F; ++x) {
            for(float y = (float) (entity.getY() - (float)radius); y < entity.getY() + (float)radius + 1.0F; ++y) {
                for(float z = (float) (entity.getZ() - (float)radius); z < entity.getZ() + (float)radius + 1.0F; ++z) {
                    if (entity.level instanceof ServerLevel world) {
                        world.sendParticles(particle, (double) x, (double) y, (double) z, 5, 1.0, 1.0, 1.0, 1.0);
                    }
                }
            }
        }
    }

    @Override
    public void onDeath(ManasSkillInstance instance, LivingDeathEvent event) {
        event.getEntity().getPersistentData().putInt("mode_azazel", 1);
        event.getEntity().getPersistentData().putInt("infect_mode", 1);
        event.getEntity().getPersistentData().putInt("assimilation_kills", 0);
        event.getEntity().getPersistentData().putString("thought_mode", "aggressive");
    }

    @Override
    public void onRespawn(ManasSkillInstance instance, PlayerEvent.PlayerRespawnEvent event) {
        event.getEntity().getPersistentData().putInt("mode_azazel", 1);
        event.getEntity().getPersistentData().putInt("infect_mode", 1);
        event.getEntity().getPersistentData().putInt("assimilation_kills", 0);
        event.getEntity().getPersistentData().putString("thought_mode", "aggressive");
    }

    @Override
    public void onLearnSkill(ManasSkillInstance instance, LivingEntity living, UnlockSkillEvent event) {
        SkillAPI.getSkillsFrom(living).forgetSkill(skillregistry.STARKILL.get());
        for(int z = 0; z < 1000; z++) {
            addLearnPoint(instance, living);
        }

        if(living instanceof Player player) {
            player.displayClientMessage(Component.translatable("azazel").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_RED)), false);
        }

        living.getPersistentData().putString("thought_mode", "aggressive");
        living.getPersistentData().putInt("mode_azazel", 1);
        living.getPersistentData().putInt("infect_mode", 1);
        living.getPersistentData().putInt("assimilation_kills", 0);
        on = false;
    }
}
