package io.github.dracosomething.trawakened.ability.skill.ultimate;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkill;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.ability.skill.intrinsic.CharmSkill;
import com.github.manasmods.tensura.ability.skill.unique.AntiSkill;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.capability.skill.TensuraSkillCapability;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.effect.template.SkillMobEffect;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.race.TensuraRaces;
import com.github.manasmods.tensura.registry.skill.UniqueSkills;
import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import com.github.manasmods.tensura.util.damage.TensuraDamageSource;
import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import io.github.dracosomething.trawakened.registry.raceregistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;

public class herrscherofdestruction extends Skill {
    public ResourceLocation getSkillIcon() {
        return new ResourceLocation("trawakened", "textures/skill/ultimate/herrscherofdestruction.png");
    }

    public herrscherofdestruction() {
        super(SkillType.ULTIMATE);
    }

    public double getObtainingEpCost() {
        return 5000.0;
    }

    public double learningCost() {
        return 5000.0;
    }

    @Override
    public int modes() {
        return 5;
    }

    @Override
    public int nextMode(LivingEntity entity, TensuraSkillInstance instance, boolean reverse) {
        int var10000;
        if (reverse) {
            switch (instance.getMode()) {
                case 1 -> var10000 = this.isMastered(instance, entity) ? 3 : 4;
                case 2 -> var10000 = 1;
                case 3 -> var10000 = 2;
                case 4 -> var10000 = 3;
                default -> var10000 = 0;
            }

            return var10000;
        } else {
            switch (instance.getMode()) {
                case 1 -> var10000 = 2;
                case 2 -> var10000 = this.isMastered(instance, entity) ? 3 : 4;
                case 3 -> var10000 = 4;
                case 4 -> var10000 = 1;
                default -> var10000 = 0;
            }

            return var10000;
        }
    }

    public Component getModeName(int mode) {
        MutableComponent var10000;
        switch (mode) {
            case 1 ->
                    var10000 = Component.translatable("trawakened.skill.mode.herrscherofdestructionskill.destroyblock");
            case 2 ->
                    var10000 = Component.translatable("trawakened.skill.mode.herrscherofdestructionskill.destroyheart");
            case 3 ->
                    var10000 = Component.translatable("trawakened.skill.mode.herrscherofdestructionskill.destroymind");
            case 4 ->
                    var10000 = Component.translatable("trawakened.skill.mode.herrscherofdestructionskill.destroychunk");
            default -> var10000 = Component.empty();
        }

        return var10000;
    }

    public double magiculeCost(LivingEntity entity, ManasSkillInstance instance) {
        double var10000;
        switch (instance.getMode()) {
            case 1:
                var10000 = 500.0;
                break;
            case 2:
                var10000 = 10000.0;
                break;
            case 3:
                var10000 = 50000.0;
                break;
            case 4:
                var10000 = 5000;
                break;
            default:
                var10000 = 0.0;
                break;
        }

        return var10000;
    }

    public boolean canBeToggled(ManasSkillInstance instance, LivingEntity entity) {
        return true;
    }

    @Override
    public void onToggleOn(ManasSkillInstance instance, LivingEntity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.getAbilities().mayfly = true;
            player.getAbilities().flying = true;
            ((Player) entity).onUpdateAbilities();
        }
    }

    @Override
    public void onToggleOff(ManasSkillInstance instance, LivingEntity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.getAbilities().mayfly = false;
            player.getAbilities().flying = false;
            ((Player) entity).onUpdateAbilities();
        }
    }

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        LivingEntity target = SkillHelper.getTargetingEntity(entity, 10.0, false);
        switch (instance.getMode()) {
            case 1:
                if (!SkillHelper.outOfMagicule(entity, instance)) {
                    Level level = entity.getLevel();
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        if (!SkillHelper.outOfMagicule(entity, instance)) {
                            BlockHitResult result = SkillHelper.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE, ClipContext.Block.OUTLINE, 30.0);
                            if (result.getType() == HitResult.Type.BLOCK) {
                                BlockPos pos = result.getBlockPos();
                                BlockState state = level.getBlockState(pos);
                                state.getBlock().playerWillDestroy(level, pos, state, player);
                                if (!(state.getBlock() instanceof DoublePlantBlock) && !(state.getBlock() instanceof DoorBlock) && !(state.getBlock() instanceof BedBlock) && !(state.getBlock() instanceof PistonHeadBlock)) {
                                    label82:
                                    {
                                        BlockEntity blockentity = level.getBlockEntity(pos);
                                        if (blockentity instanceof BeehiveBlockEntity) {
                                            BeehiveBlockEntity beehiveblockentity = (BeehiveBlockEntity) blockentity;
                                            beehiveblockentity.emptyAllLivingFromHive(player, state, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
                                        }

                                        if (blockentity instanceof ShulkerBoxBlockEntity) {
                                            ShulkerBoxBlockEntity shulker = (ShulkerBoxBlockEntity) blockentity;
                                            if (!shulker.isEmpty()) {
                                                level.destroyBlock(pos, !player.isCreative(), player);
                                                break label82;
                                            }
                                        }

                                        level.destroyBlock(pos, false, player);
                                    }
                                } else {
                                    level.destroyBlock(pos, false, player);
                                }
                                instance.setCoolDown(30);
                                instance.addMasteryPoint(entity);
                            }
                        }
                    }
                }
                break;

            case 2:
                if (!SkillHelper.outOfMagicule(entity, instance)) {
                    if (target != null) {

                        int mastery = instance.getMastery();
                        double chance = (-20 + (mastery * 0.05));
                        System.out.println(chance);

                        if (entity.getRandom().nextInt(100) <= chance) {
                            float maxhealth = target.getMaxHealth();
                            float currenthealth = target.getHealth();
                            float damage = (float) Math.floor(currenthealth - (maxhealth * 0.90F));
                            if (damage <= 0) {
                                target.kill();
                            } else {
                                target.setHealth(damage);
                            }
                            instance.addMasteryPoint(entity);
                        } else {
                            float maxhealth = entity.getMaxHealth();
                            float currenthealth = entity.getHealth();
                            float damage = (float) Math.floor(currenthealth - (maxhealth * 0.90F));
                            if (damage <= 0) {
                                entity.kill();
                            } else {
                                entity.setHealth(damage);
                            }
                        }
                        instance.setCoolDown(this.isMastered(instance, entity) ? 1800 : 3600);
                    } else if (entity instanceof Player) {
                        Player player = (Player) entity;
                        player.displayClientMessage(Component.translatable("tensura.targeting.not_targeted").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY)), false);
                    }
                }
                break;
            case 3:
                if (!SkillHelper.outOfMagicule(entity, instance)) {
                    if (target != null) {
                        int mastery = instance.getMastery();
                        double chance = (-45 + (mastery * 0.05));
                        if (entity.getRandom().nextInt(100) <= chance) {
                            float maxshp = trawakenedPlayerCapability.getMaxSpiritualHealth(target);
                            float currentshp = (float) TensuraEPCapability.getSpiritualHealth(target);
                            float damage = (float) Math.floor(currentshp - (maxshp * 0.90F));
                            if (damage <= 0) {
                                target.kill();
                            } else {
                                TensuraEPCapability.setSpiritualHealth(target, damage);
                            }
                            instance.addMasteryPoint(entity);
                        } else {
                            float maxshp = trawakenedPlayerCapability.getMaxSpiritualHealth(entity);
                            float currentshp = (float) TensuraEPCapability.getSpiritualHealth(entity);
                            float damage = (float) Math.floor(currentshp - (maxshp * 0.90F));
                            if (damage <= 0) {
                                entity.kill();
                            } else {
                                TensuraEPCapability.setSpiritualHealth(entity, damage);
                            }
                        }
                        instance.setCoolDown(this.isMastered(instance, entity) ? 1800 : 3600);
                    } else if (entity instanceof Player) {
                        Player player = (Player) entity;
                        player.displayClientMessage(Component.translatable("tensura.targeting.not_targeted").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                    }
                }
                break;
            case 4:
                if (!SkillHelper.outOfMagicule(entity, instance)) {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        BlockHitResult result = SkillHelper.getPlayerPOVHitResult(player.level, player, ClipContext.Fluid.NONE, ClipContext.Block.OUTLINE, 30.0);
                        int radius = 8;
                        BlockPos startpos = new BlockPos(result.getBlockPos().getX() - radius, result.getBlockPos().getY() - radius, result.getBlockPos().getZ() - radius);
                        BlockPos endpos = new BlockPos(result.getBlockPos().getX() + radius, result.getBlockPos().getY() + radius, result.getBlockPos().getZ() + radius);
                        for (int x = startpos.getX(); x < endpos.getX(); ++x) {
                            for (int y = startpos.getY(); y < endpos.getY(); ++y) {
                                for (int z = startpos.getZ(); z < endpos.getZ(); ++z) {
                                    player.level.removeBlock(new BlockPos(x, y, z), false);
                                }
                            }
                        }
                        instance.setCoolDown(1500);
                        instance.addMasteryPoint(entity);
                    }
                }
                break;
        }
    }

    public void onBeingDamaged(ManasSkillInstance instance, LivingAttackEvent event) {
        if (!event.isCanceled()) {
            LivingEntity entity = event.getEntity();
            DamageSource damageSource = event.getSource();
            if (!damageSource.isBypassInvul()) {
                if (damageSource instanceof TensuraDamageSource) {
                    TensuraDamageSource source = (TensuraDamageSource) damageSource;
                    if (source.getIgnoreBarrier() >= 2.0F) {
                        return;
                    }
                }

                boolean var10000;
                label52:
                {
                    if (damageSource instanceof TensuraDamageSource) {
                        TensuraDamageSource source = (TensuraDamageSource) damageSource;
                        if (source.getSkill() != null || source.getMpCost() != 0.0 || source.getApCost() != 0.0) {
                            var10000 = true;
                            break label52;
                        }
                    }

                    var10000 = false;
                }

                boolean anti = var10000;
                if (damageSource.isMagic() || anti) {
                    entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_ATTACK_WEAK, SoundSource.PLAYERS, 2.0F, 1.0F);
                    event.setCanceled(true);
                }
            }

        }
    }

    public void onDamageEntity(ManasSkillInstance instance, LivingEntity entity, LivingHurtEvent e) {
        if (e.getSource().getDirectEntity() == entity) {
            if (DamageSourceHelper.isPhysicalAttack(e.getSource())) {
                if (entity.getMainHandItem().isEmpty() && entity.getOffhandItem().isEmpty()) {
                    LivingEntity target = e.getEntity();
                    AttributeInstance barrier = target.getAttribute((Attribute) TensuraAttributeRegistry.BARRIER.get());
                    if (barrier != null && !(barrier.getValue() <= 0.0)) {
                        entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                        barrier.removeModifiers();
                    }
                }
            }
        }
    }

    public void onTouchEntity(ManasSkillInstance instance, LivingEntity entity, LivingHurtEvent e) {
        if (e.getSource().getDirectEntity() == entity) {
            if (DamageSourceHelper.isPhysicalAttack(e.getSource())) {
                if (instance.isMastered(entity)) {
                    if (!entity.getMainHandItem().isEmpty()) {
                        return;
                    }
                } else if (!entity.getMainHandItem().isEmpty() || !entity.getOffhandItem().isEmpty()) {
                    return;
                }

                LivingEntity target = e.getEntity();
                target.addEffect(new MobEffectInstance((MobEffect) TensuraMobEffects.ANTI_SKILL.get(), 100, 0, false, false, false));
                SkillHelper.removePredicateEffect(target, (effect) -> {
                    return effect.isBeneficial() && effect instanceof SkillMobEffect && !this.getNonSkillMobEffects().contains(effect);
                });
                TensuraParticleHelper.addServerParticlesAroundSelf(target, ParticleTypes.ENCHANTED_HIT, 1.0);
                entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    private List<MobEffect> getNonSkillMobEffects() {
        return List.of((MobEffect) TensuraMobEffects.AURA_SWORD.get(), (MobEffect) TensuraMobEffects.DIAMOND_PATH.get(), (MobEffect) TensuraMobEffects.OGRE_GUILLOTINE.get(), (MobEffect) TensuraMobEffects.BATS_MODE.get(), (MobEffect) TensuraMobEffects.MAGICULE_REGENERATION.get());
    }

    private void gainMastery(ManasSkillInstance instance, LivingEntity entity) {
        CompoundTag tag = instance.getOrCreateTag();
        int Time = tag.getInt("activatedTimes");
        if (Time % 150 == 0) {
            int chance = 10;
            if (entity.getRandom().nextInt(100) <= chance) {
                this.addMasteryPoint(instance, entity);
            }
        }

        tag.putInt("artivatedTimes", Time + 1);
    }

    @Override
    public boolean canTick(ManasSkillInstance instance, LivingEntity entity) {
        return true;
    }

    @Override
    public void onTick(ManasSkillInstance instance, LivingEntity living) {
        if (living instanceof Player) {
            Player player = (Player) living;
            if (!TensuraPlayerCapability.getRace(player).equals((Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceregistry.HERRSCHER_OF_DESTRUCTION))) {
                SkillAPI.getSkillsFrom(player).forgetSkill((TensuraSkill) SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:herrscherofdestructionskill")));
                SkillUtils.learnSkill(player, UniqueSkills.GREAT_SAGE.get());
            }
        }
    }
}
