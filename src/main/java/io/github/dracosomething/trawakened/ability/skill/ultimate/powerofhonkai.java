package io.github.dracosomething.trawakened.ability.skill.ultimate;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkill;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.ability.skill.extra.DemonLordHakiSkill;
import com.github.manasmods.tensura.ability.skill.extra.HakiSkill;
import com.github.manasmods.tensura.ability.skill.intrinsic.CharmSkill;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.capability.skill.TensuraSkillCapability;
import com.github.manasmods.tensura.config.TensuraConfig;
import com.github.manasmods.tensura.effect.template.TensuraMobEffect;
import com.github.manasmods.tensura.network.TensuraNetwork;
import com.github.manasmods.tensura.network.play2client.RequestFxSpawningPacket;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.race.TensuraRaces;
import com.github.manasmods.tensura.registry.skill.UniqueSkills;
import io.github.dracosomething.trawakened.registry.raceregistry;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Iterator;
import java.util.List;

public class powerofhonkai extends Skill {

    public ResourceLocation getSkillIcon() {
        return new ResourceLocation("trawakened", "textures/skill/ultimate/powerofhonkai.png");
    }

    public powerofhonkai() {
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

    public Component getModeName(int mode) {
        MutableComponent var10000;
        switch (mode) {
            case 1 -> var10000 = Component.translatable("trawakened.skill.mode.willofhonkai.copy");
            case 2 -> var10000 = Component.translatable("trawakened.skill.mode.powerofhonkai.enslave");
            case 3 -> var10000 = Component.translatable("trawakened.skill.mode.powerofhonkai.honkairelease");
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
                var10000 = 100.0;
                break;
            case 3:
                var10000 = 50.0;
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
        analize(instance, entity, true);
        TensuraPlayerCapability.getFrom((Player) entity).ifPresent((data) -> {
            data.setBlessed(true);
        });
    }

    @Override
    public void onToggleOff(ManasSkillInstance instance, LivingEntity entity) {
        analize(instance, entity, false);
        TensuraPlayerCapability.getFrom((Player) entity).ifPresent((data) -> {
            data.setBlessed(false);
        });
    }

    public void analize(ManasSkillInstance instance, LivingEntity entity, boolean on) {
        if (entity instanceof Player) {
            if (on) {
                Player player = (Player) entity;
                TensuraSkillCapability.getFrom(player).ifPresent((cap) -> {
                    int level;
                    level = 5;
                    if (cap.getAnalysisLevel() != level) {
                        cap.setAnalysisLevel(level);
                        cap.setAnalysisDistance(instance.isMastered(entity) ? 15 : 5);
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

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        LivingEntity target = SkillHelper.getTargetingEntity(entity, 10.0, false);
        switch (instance.getMode()) {
            case 1:
                if (!SkillHelper.outOfMagicule(entity, instance)) {
                    if (target != null) {
                        label52:
                        {
                            entity.swing(InteractionHand.MAIN_HAND, true);
                            ServerLevel level = (ServerLevel) entity.getLevel();
                            int chance = 50;
                            boolean failed = true;
                            if (entity.getRandom().nextInt(100) <= chance) {
                                List<ManasSkillInstance> collection = SkillAPI.getSkillsFrom(target).getLearnedSkills().stream().filter(this::canCopy).toList();
                                if (!collection.isEmpty()) {
                                    ManasSkill skill = ((ManasSkillInstance) collection.get(target.getRandom().nextInt(collection.size()))).getSkill();
                                    if (SkillUtils.learnSkill(entity, skill)) {
                                        this.addMasteryPoint(instance, entity);
                                        instance.setCoolDown(15);
                                        failed = false;
                                        if (entity instanceof Player) {
                                            Player player = (Player) entity;
                                            player.displayClientMessage(Component.translatable("tensura.skill.acquire", new Object[]{skill.getName()}).setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)), false);
                                        }

                                        level.playSound((Player) null, entity.getX(), entity.getY(), entity.getY(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.0F);
                                    }
                                }
                            }

                            if (failed && entity instanceof Player) {
                                Player player = (Player) entity;
                                player.displayClientMessage(Component.translatable("tensura.ability.activation_failed").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                                level.playSound((Player) null, entity.getX(), entity.getY(), entity.getY(), SoundEvents.PLAYER_ATTACK_WEAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                                instance.setCoolDown(5);
                            }

                            return;
                        }
                    } else if (entity instanceof Player) {
                        Player player = (Player) entity;
                        player.displayClientMessage(Component.translatable("tensura.targeting.not_targeted").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                    }
                }
                break;

            case 2:
                if (!SkillHelper.outOfMagicule(entity, instance)) {
                    if (target != null) {
                        CharmSkill.charm(instance, entity);
                        instance.setCoolDown(5);
                    } else if (entity instanceof Player) {
                        Player player = (Player) entity;
                        player.displayClientMessage(Component.translatable("tensura.targeting.not_targeted").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                    }
                }
                break;
            case 3:
            default:
                break;
        }
    }

    public boolean canCopy(ManasSkillInstance instance) {
        if (!instance.isTemporarySkill() && instance.getMastery() >= 0) {
            ManasSkill var3 = instance.getSkill();
            if (!(var3 instanceof Skill)) {
                return false;
            } else {
                Skill skill = (Skill) var3;
                return skill.getType().equals(SkillType.INTRINSIC) ||
                        skill.getType().equals(SkillType.COMMON) ||
                        skill.getType().equals(SkillType.EXTRA) ||
                        skill.getType().equals(SkillType.UNIQUE) ||
                        skill.getType().equals(SkillType.RESISTANCE) ||
                        skill.getType().equals(SkillType.ULTIMATE) ||
                        !skill.equals(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:willofhonkai"))) ||
                        !skill.equals(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:herrscherofdestructionskill"))) ||
                        !skill.equals(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:herrscherofpestilenceskill")));
            }
        } else {
            return false;
        }
    }

    private int heldseconds = 0;

    @Override
    public boolean onHeld(ManasSkillInstance instance, LivingEntity living, int heldTicks) {
        if (instance.getMode() != 3){
            return false;
        } else if (heldTicks % 20 == 0 && SkillHelper.outOfMagicule(living, instance)) {
            return false;
        }
        else {
            if (heldTicks % 200 == 0 && heldTicks >0){
                this.addMasteryPoint(instance, living);
            } else if (heldTicks % 20 == 0){
                heldseconds++;
            }

            HonkaiRelease(instance, living, heldTicks);
            return true;
        }
    }

    public static void HonkaiRelease(ManasSkillInstance instance, LivingEntity entity, int heldTicks) {
        entity.getLevel().playSound((Player)null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 1.0F, 1.0F);
        if (heldTicks % 2 == 0) {
            TensuraNetwork.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> {
                return entity;
            }), new RequestFxSpawningPacket(new ResourceLocation("tensura:demon_lord_haki"), entity.getId(), 0.0, 1.0, 0.0, true));
        }

        List<LivingEntity> list = entity.getLevel().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(15.0), (living) -> {
            return !living.is(entity) && living.isAlive() && !living.isAlliedTo(entity);
        });
        if (!list.isEmpty()) {
            Iterator var10 = list.iterator();

            while(true) {
                LivingEntity target;
                Player player;
                do {
                    if (!var10.hasNext()) {
                        return;
                    }

                    target = (LivingEntity)var10.next();
                    if (!(target instanceof Player)) {
                        break;
                    }

                    player = (Player)target;
                } while(player.getAbilities().invulnerable);
                SkillHelper.checkThenAddEffectSource(target, entity, (MobEffect)TensuraMobEffects.MAGICULE_POISON.get(), 200, 5);
                HakiSkill.hakiPush(target, entity, 5);
            }
        }
    }

    @Override
    public void onRelease(ManasSkillInstance instance, LivingEntity entity, int heldTicks) {
        if (instance.getMode() == 3){
            instance.setCoolDown(2 * heldseconds);
            heldseconds = 0;
        }
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
    public void onDamageEntity(ManasSkillInstance instance, LivingEntity entity, LivingHurtEvent event) {
        if (true) {
            LivingEntity target = event.getEntity();
            target.addEffect(new MobEffectInstance((MobEffect) TensuraMobEffects.MAGICULE_POISON.get(), 120, 2, false, false, true));
        }
    }

    @Override
    public void onTick(ManasSkillInstance instance, LivingEntity living) {
        if (instance.isToggled()) {
            this.gainMastery(instance, living);
        }

        if (living instanceof Player) {
            Player player = (Player) living;
            if (!TensuraPlayerCapability.getRace(player).equals((Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceregistry.AWAKENED_APOSTLE)) &&
                !TensuraPlayerCapability.getRace(player).equals((Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceregistry.HERRSCHER_SEED_AWAKENED)) &&
                !TensuraPlayerCapability.getRace(player).equals((Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceregistry.HERRSCHER_OF_DESTRUCTION)))
            {
                SkillAPI.getSkillsFrom(player).forgetSkill((TensuraSkill) SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:powerofhonkai")));
                player.displayClientMessage(Component.translatable("unworthy").setStyle(Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE)), false);
            }
        }
    }
}
