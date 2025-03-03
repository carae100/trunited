package io.github.dracosomething.trawakened.library;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public enum AlternateType {
    DOPPELGANGER("doppelgänger"), DETECTABLE("detectable"), REDACTED("redacted"), INTRUDER("intruder");
    private String name;

    private AlternateType(String name) {
        this.name = name;
    }

    private static final Map<String, AlternateType> ALTERNATETYPES_BY_NAME = Arrays.stream(AlternateType.values()).collect(Collectors.toMap((type) -> {
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

    public final CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", name);
        return tag;
    }


    public static AlternateType getRandomType() {
        List<AlternateType> list = List.of(AlternateType.values());
        Random random = new Random();
        return list.get(random.nextInt(0, list.size()));
    }

    @Nullable
    private static AlternateType getByName(@Nullable String p_126658_) {
        return p_126658_ == null ? null : (AlternateType) ALTERNATETYPES_BY_NAME.get(cleanName(p_126658_));
    }

    public static AlternateType fromNBT(CompoundTag tag) {
        return getByName(tag.getString("name"));
    }

    public enum Assimilation {
        FLAWED("flawed", REDACTED),
        COMPLETE("complete", DOPPELGANGER),
        OVERDRIVEN("overdriven", REDACTED);
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
