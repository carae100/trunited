package io.github.dracosomething.trawakened.ability.skill.unique;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.dracosomething.trawakened.api.FearTypes;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.AwakenedFearCapability;
import io.github.dracosomething.trawakened.entity.barrier.IntruderBarrier;
import io.github.dracosomething.trawakened.helper.FearHelper;
import io.github.dracosomething.trawakened.helper.skillHelper;
import io.github.dracosomething.trawakened.world.trawakenedGamerules;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class Alternate extends Skill {
    public Alternate() {
        super(SkillType.UNIQUE);
    }

    @Override
    public int modes() {
        return 8;
    }

    @Override
    public double magiculeCost(LivingEntity entity, ManasSkillInstance instance) {
        double var10000;
        CompoundTag tag = instance.getOrCreateTag();
        switch (instance.getMode()) {
            case 1 -> var10000 = 0;
            case 2 -> var10000 = 0;
            case 3 -> var10000 = 1000;
            default -> var10000 = 0;
        }

        return var10000;
    }

    @Override
    public Component getModeName(int mode) {
        MutableComponent var10000;
        switch (mode) {
            case 1, 6 -> var10000 = Component.translatable("trawakened.skill.mode.alternate.fear");
            case 2, 7 -> var10000 = Component.translatable("trawakened.skill.mode.alternate.scary");
            case 3, 8 -> var10000 = Component.translatable("trawakened.skill.mode.alternate.kill");
            case 4 -> var10000 = Component.translatable("trawakened.skill.mode.alternate.new_fear");
            case 5 -> var10000 = Component.translatable("trawakened.skill.mode.alternate.scare");
            default -> var10000 = Component.empty();
        }

        return var10000;
    }

    @Override
    public int nextMode(LivingEntity entity, TensuraSkillInstance instance, boolean reverse) {
        int var10000;
        CompoundTag tag = instance.getOrCreateTag();
        if (!AwakenedFearCapability.GetIsAlternate(entity)) {
            if (reverse) {
                switch (instance.getMode()) {
                    case 1 -> var10000 = 3;
                    case 2 -> var10000 = 1;
                    case 3 -> var10000 = 2;
                    default -> var10000 = 0;
                }

                return var10000;
            } else {
                switch (instance.getMode()) {
                    case 1 -> var10000 = 2;
                    case 2 -> var10000 = 3;
                    case 3 -> var10000 = 1;
                    default -> var10000 = 0;
                }

                return var10000;
            }
        } else {
            if (reverse) {
                switch (instance.getMode()) {
                    case 4 -> var10000 = 8;
                    case 5 -> var10000 = 4;
                    case 6 -> var10000 = 5;
                    case 7 -> var10000 = 6;
                    case 8 -> var10000 = 7;
                    default -> var10000 = 0;
                }

                return var10000;
            } else {
                switch (instance.getMode()) {
                    case 4 -> var10000 = 5;
                    case 5 -> var10000 = 6;
                    case 6 -> var10000 = 7;
                    case 7 -> var10000 = 8;
                    case 8 -> var10000 = 4;
                    default -> var10000 = 0;
                }

                return var10000;
            }
        }
    }

    @Override
    public List<MobEffect> getImmuneEffects(ManasSkillInstance instance, LivingEntity entity) {
        if (!AwakenedFearCapability.GetIsAlternate(entity)) {
            return List.of(MobEffects.GLOWING);
        } else {
            return List.of();
        }
    }

    @Override
    public void onDamageEntity(ManasSkillInstance instance, LivingEntity entity, LivingHurtEvent event) {
        if (!AwakenedFearCapability.GetIsAlternate(entity)) {
            LivingEntity target = event.getEntity();
            CompoundTag tag = instance.getOrCreateTag();
            if (!target.getPersistentData().hasUUID("alternate_UUID") && !tag.getBoolean("is_locked")) {
                if (!SkillHelper.outOfMagicule(entity, 100)) {
                    target.getPersistentData().putUUID("alternate_UUID", entity.getUUID());
                    IntruderBarrier holyField = new IntruderBarrier(target.level, target);
                    holyField.setRadius(25.0F);
                    holyField.setLife(-1);
                    holyField.setPos(target.position().add(0.0, -12.5, 0.0));
                    target.level.addFreshEntity(holyField);
                    tag.putInt("original_scared", AwakenedFearCapability.getScared(target));
                    tag.putBoolean("is_locked", true);
                    if (entity instanceof Player player) {
                        player.displayClientMessage(Component.translatable("trawakened.fear.scared", new Object[]{AwakenedFearCapability.getScared(target)}).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)), false);
                        if (AwakenedFearCapability.getScared(target) >= 1) {
                            player.displayClientMessage(Component.translatable("trawakened.fear.learn", new Object[]{AwakenedFearCapability.getFearType(target).toString()}).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)), false);
                        }
                    }
                } else {
                    event.setCanceled(true);
                }
            }
        }
    }

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        LivingEntity living = SkillHelper.getTargetingEntity(entity, ForgeMod.REACH_DISTANCE.get().getDefaultValue(), true);
        CompoundTag tag = instance.getOrCreateTag();
        switch (instance.getMode()) {
            case 1:
                if (AwakenedFearCapability.getScared(living) >= 3) {
                    if (entity instanceof Player player) {
                        player.displayClientMessage(Component.translatable("trawakened.fear.scared", new Object[]{AwakenedFearCapability.getScared(living)}).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)), false);
                        player.displayClientMessage(Component.translatable("trawakened.fear.learn", new Object[]{AwakenedFearCapability.getFearType(living).toString()}).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)), false);
                    }
                } else {
                    if (entity instanceof Player player) {
                        player.displayClientMessage(Component.translatable("trawakened.fear.brave").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                    }
                }
                break;
            case 2:
                if (AwakenedFearCapability.getScared(living) >= 3) {
                    if (living.getPersistentData().getUUID("alternate_UUID").equals(entity.getUUID())) {
                        if (entity instanceof Player player) {
                            player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", AwakenedFearCapability.getFearType(living).getItem().toString().replace("[", "").replace("]", ""), living.getName()), false);
                            player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", AwakenedFearCapability.getFearType(living).getBlock().toString().replace("[", "").replace("Block{", "").replace("}", "").replace("]", ""), living.getName()), false);
                            player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", AwakenedFearCapability.getFearType(living).getEntity().toString().replace("[", "").replace("]", ""), living.getName()), false);
                            AwakenedFearCapability.getFearType(living).getEffect().forEach((effect) -> {
                                player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", effect.getDisplayName(), living.getName()), false);
                            });
                        }
                    }
                } else {
                    if (entity instanceof Player player) {
                        player.displayClientMessage(Component.translatable("trawakened.fear.brave").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                    }
                }
                break;
            case 3:
                if (!SkillHelper.outOfMagicule(entity, instance)) {
                    if (AwakenedFearCapability.getScared(living) >= (tag.getInt("original_scared") + 10) || AwakenedFearCapability.getFearType(living).equals(FearTypes.TRUTH)) {
                        living.hurt(TensuraDamageSources.insanity(living).bypassArmor().bypassMagic().bypassEnchantments().bypassInvul(), living.getMaxHealth() * 10);
                        AwakenedFearCapability.SetIsAlternate(entity, true);
                        entity.removeEffect(TensuraMobEffects.PRESENCE_CONCEALMENT.get());
                        if(entity instanceof Player) {
                            this.CopySkills(living, (Player) entity);
                        }
                        if (entity instanceof Player player) {
                            if (!player.isCreative()) {
                                player.getAbilities().mayfly = false;
                                player.getAbilities().invulnerable = false;
                                player.getAbilities().mayBuild = true;
                                player.onUpdateAbilities();
                            }
                        }
                        Assimilation assimilation = Assimilation.getRandomAssimilation();
                        tag.put("assimilation", assimilation.toNBT());
                        tag.put("alternate_type", assimilation.getType().toNBT());
                        instance.setMode(4);
                        if (living.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES)) {
                            Iterator var9 = living.level.players().iterator();

                            while (var9.hasNext()) {
                                Player everyone = (Player) var9.next();
                                if (everyone != entity) {
                                    everyone.sendSystemMessage(Component.translatable("trawakened.fake_attack.suicide", new Object[]{living}));
                                }
                            }
                        }
                    } else {
                        if (entity instanceof Player player) {
                            player.displayClientMessage(Component.translatable("trawakened.fear.brave").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                        }
                    }
                }
                break;
            case 4:
                if (!SkillHelper.outOfMagicule(entity, instance)) {
                    if (AwakenedFearCapability.getScared(living) >= 3) {
                        AwakenedFearCapability.setScared(living, 0);
                        AwakenedFearCapability.setScaredCooldown(living, 0);
                        AwakenedFearCapability.setFearType(living, FearTypes.getRandom());
                        if (entity instanceof Player player) {
                            player.displayClientMessage(Component.translatable("trawakened.fear.learn", new Object[]{AwakenedFearCapability.getFearType(living).toString()}).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)), false);
                        }
                    } else {
                        if (entity instanceof Player player) {
                            player.displayClientMessage(Component.translatable("trawakened.fear.brave").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                        }
                    }
                }
                break;
            case 5:
                List<Entity> entities = skillHelper.DrawCircle(entity, 25, false);
                if (!entities.isEmpty()) {
                    if (!SkillHelper.outOfMagicule(entity, instance)) {
                        entities.forEach((Entity) -> {
                            if (Entity instanceof LivingEntity livingEntity) {
                                if (entity.level.getGameRules().getBoolean(trawakenedGamerules.NORMAL_FEAR)) {
                                    AwakenedFearCapability.setScared(livingEntity, AwakenedFearCapability.getScared(livingEntity) + 10);
                                } else {
                                    FearHelper.fearPenalty(livingEntity, AwakenedFearCapability.getScared(livingEntity) + 10);
                                }
                            }
                        });
                        if (entity instanceof Player) {
                            Player player = (Player) entity;
                            player.displayClientMessage(Component.translatable("trawakened.fear.inspire_fear", new Object[]{entities.size()}).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                        }
                    }
                } else {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        player.displayClientMessage(Component.translatable("tensura.targeting.not_targeted").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                    }
                }
                break;
            case 6:
                if (living != null) {
                    if (!SkillHelper.outOfMagicule(entity, instance)) {
                        if (AwakenedFearCapability.getScared(living) >= (tag.getInt("original_scared") + 10) || AwakenedFearCapability.getFearType(living).equals(FearTypes.TRUTH)) {
                            living.hurt(TensuraDamageSources.insanity(living).bypassArmor().bypassMagic().bypassEnchantments().bypassInvul(), AwakenedFearCapability.getScared(living) * 5);
                            if (living.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES)) {
                                Iterator var9 = living.level.players().iterator();

                                while (var9.hasNext()) {
                                    Player everyone = (Player) var9.next();
                                    if (everyone != entity) {
                                        everyone.sendSystemMessage(Component.translatable("trawakened.fake_attack.suicide", new Object[]{living}));
                                    }
                                }
                            }
                        } else {
                            if (entity instanceof Player player) {
                                player.displayClientMessage(Component.translatable("trawakened.fear.brave").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                            }
                        }
                    }
                } else {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        player.displayClientMessage(Component.translatable("tensura.targeting.not_targeted").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                    }
                }
                break;
            case 7:
                if (!SkillHelper.outOfMagicule(entity, instance)) {
                    if (AwakenedFearCapability.getScared(living) >= 3) {
                        if (living.getPersistentData().getUUID("alternate_UUID").equals(entity.getUUID())) {
                            if (entity instanceof Player player) {
                                player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", AwakenedFearCapability.getFearType(living).getItem().toString().replace("[", "").replace("]", ""), living.getName()), false);
                                player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", AwakenedFearCapability.getFearType(living).getBlock().toString().replace("[", "").replace("Block{", "").replace("}", "").replace("]", ""), living.getName()), false);
                                player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", AwakenedFearCapability.getFearType(living).getEntity().toString().replace("[", "").replace("]", ""), living.getName()), false);
                                AwakenedFearCapability.getFearType(living).getEffect().forEach((effect) -> {
                                    player.displayClientMessage(Component.translatable("trawakened.fear.trigger_objects", effect.getDisplayName(), living.getName()), false);
                                });
                            }
                        }
                    } else {
                        if (entity instanceof Player player) {
                            player.displayClientMessage(Component.translatable("trawakened.fear.brave").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                        }
                    }
                }
                break;
            case 8:
                if (AwakenedFearCapability.getScared(living) >= 3) {
                    if (entity instanceof Player player) {
                        player.displayClientMessage(Component.translatable("trawakened.fear.scared", new Object[]{AwakenedFearCapability.getScared(living)}).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)), false);
                        player.displayClientMessage(Component.translatable("trawakened.fear.learn", new Object[]{AwakenedFearCapability.getFearType(living).toString()}).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)), false);
                    }
                } else {
                    if (entity instanceof Player player) {
                        player.displayClientMessage(Component.translatable("trawakened.fear.brave").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                    }
                }
                break;
        }
    }

    @Override
    public boolean canTick(ManasSkillInstance instance, LivingEntity entity) {
        return true;
    }

    @Override
    public void onTick(ManasSkillInstance instance, LivingEntity living) {
        if (!AwakenedFearCapability.GetIsAlternate(living)) {
            living.addEffect(new MobEffectInstance(TensuraMobEffects.PRESENCE_CONCEALMENT.get(), 120, 255, false, false, false));
            if (living instanceof Player player) {
                if (!player.isCreative()) {
                    player.getAbilities().mayfly = true;
                    player.getAbilities().invulnerable = true;
                    player.getAbilities().mayBuild = false;
                    player.onUpdateAbilities();
                }
            }
        } else {
            switch (Assimilation.fromNBT(instance.getOrCreateTag().getCompound("assimilation"))) {
                case FLAWED -> Assimilation.flawedBuff(living);
                case OVERDRIVEN -> Assimilation.overdrivenBuff(living);
                case COMPLETE -> Assimilation.completeBuff(living);
            }
        }
    }

    @Override
    public void onDeath(ManasSkillInstance instance, LivingDeathEvent event) {
        CompoundTag tag = instance.getOrCreateTag();
        if (!AwakenedFearCapability.GetIsAlternate(event.getEntity())) {
            tag.putInt("original_scared", 0);
            tag.putBoolean("is_locked", false);
            AwakenedFearCapability.SetIsAlternate(event.getEntity(), false);
            tag.put("alternate_type", AlternateType.INTRUDER.toNBT());
        }
    }

    @Override
    public void onLearnSkill(ManasSkillInstance instance, LivingEntity living, UnlockSkillEvent event) {
        AwakenedFearCapability.SetIsAlternate(living, false);
        instance.getOrCreateTag().putInt("original_scared", 0);
        instance.getOrCreateTag().putBoolean("is_locked", false);
        instance.getOrCreateTag().put("alternate_type", AlternateType.INTRUDER.toNBT());
        if (living instanceof Player player) {
            player.displayClientMessage(Component.translatable("trawakened.fear.learn_self", new Object[]{AwakenedFearCapability.getFearType(player).toString()}).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)), false);
        }
    }

    private void CopySkills(LivingEntity target, Player player) {
        Iterator<ManasSkillInstance> skills = List.copyOf(SkillAPI.getSkillsFrom(target).getLearnedSkills()).iterator();

        while (skills.hasNext()) {
            ManasSkillInstance skill = skills.next();
            if (skill.getMastery() >= 0 && SkillUtils.hasSkill(player, skill.getSkill())) {
                System.out.println(skill);
                SkillUtils.learnSkill(player, skill.getSkill());
            }
        }
    }

    public enum AlternateType {
        INTRUDER("intruder"),
        DOPPELGANGER("doppelgänger"),
        DETECTABLE("detectable"),
        REDACTED("redacted");
        private String name;

        private AlternateType(String name) {
            this.name = name;
        }

        private static final Map<String, AlternateType> ALTERNATETYPES_BY_NAME = Arrays.stream(values()).collect(Collectors.toMap((type) -> {
            return cleanName(type.name);
        }, (type) -> {
            return type;
        }));

        private static String cleanName(String p_126663_) {
            return p_126663_.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }

        public static AlternateType getRandomType() {
            List<AlternateType> list = List.of(values());
            Random random = new Random();
            return list.get(random.nextInt(0, list.size()));
        }

        public final CompoundTag toNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putString("name", name);
            return tag;
        }

        @Nullable
        private static AlternateType getByName(@Nullable String p_126658_) {
            return p_126658_ == null ? null : (AlternateType) ALTERNATETYPES_BY_NAME.get(cleanName(p_126658_));
        }

        public static AlternateType fromNBT(CompoundTag tag) {
            return getByName(tag.getString("name"));
        }
    }

    public enum Assimilation {
        FLAWED("flawed", AlternateType.REDACTED),
        COMPLETE("complete", AlternateType.DOPPELGANGER),
        OVERDRIVEN("overdriven", AlternateType.REDACTED);
        private String name;
        private AlternateType type;

        private Assimilation(String name, AlternateType type) {
            this.name = name;
            this.type = type;
        }

        private static final Map<String, Assimilation> ASSIMILATION_BY_NAME = Arrays.stream(values()).collect(Collectors.toMap((assimilation) -> {
            return cleanName(assimilation.name);
        }, (assimilation) -> {
            return assimilation;
        }));

        private static String cleanName(String p_126663_) {
            return p_126663_.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
        }

        public String getName() {
            return name;
        }

        public AlternateType getType() {
            return type;
        }

        @Override
        public String toString() {
            return name;
        }

        public static Assimilation getRandomAssimilation() {
            Random random = new Random();
            if (random.nextInt(0, 100) < 75) {
                return random.nextInt(0, 100) < 75 ? Assimilation.FLAWED : Assimilation.OVERDRIVEN;
            } else {
                return Assimilation.COMPLETE;
            }
        }

        @Nullable
        private static Assimilation getByName(@Nullable String p_126658_) {
            return p_126658_ == null ? null : (Assimilation) ASSIMILATION_BY_NAME.get(cleanName(p_126658_));
        }

        public final CompoundTag toNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putString("name", name);
            tag.put("type", getType().toNBT());
            return tag;
        }

        public static Assimilation fromNBT(CompoundTag tag) {
            return getByName(tag.getString("name"));
        }

        public static void overdrivenBuff(LivingEntity entity) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 2, false, false, false));
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 3, false, false, false));
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 5, false, false, false));
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1, false, false, false));
        }

        public static void completeBuff(LivingEntity entity) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 3, false, false, false));
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 3, false, false, false));
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 3, false, false, false));
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1, false, false, false));
        }

        public static void flawedBuff(LivingEntity entity) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1, false, false, false));
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 1, false, false, false));
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 1, false, false, false));
        }
    }
}
