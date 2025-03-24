package io.github.dracosomething.trawakened.library;

import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public enum shadowRank {
    BASIC("Basic", 10000),
    NORMAL("Normal", 10000, 50000),
    ELITE("Elite", 50000, 100000),
    KNIGHT("Knight", 100000, 200000),
    ELITE_KNIGHT("Elite Knight", 200000, 500000),
    COMMANDER("Commander", 500000, 1000000),
    MARSHAL("Marshal", 1000000, 2000000),
    GRAND_MARSHAL("Grand Marshal", 2000000, Integer.MAX_VALUE-1);
    private String name;
    private int min;
    private int max;
    private static final Map<String, shadowRank> SHADOWRANKS_BY_NAME = Arrays.stream(shadowRank.values()).collect(Collectors.toMap((type) -> {
        return cleanName(type.name);
    }, (type) -> {
        return type;
    }));

    private static String cleanName(String p_126663_) {
        return p_126663_.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
    }

    private shadowRank(String name, int min, int max) {
        this.max = max;
        this.name = name;
        this.min = min;
    }

    private shadowRank(String name, int max) {
        this(name, Integer.MIN_VALUE+1, max);
    }

    @Nullable
    public static shadowRank getByName(@Nullable String p_126658_) {
        return p_126658_ == null ? null : (shadowRank) SHADOWRANKS_BY_NAME.get(cleanName(p_126658_));
    }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", name);
        tag.putInt("min", min);
        tag.putInt("max", max);
        return tag;
    }

    public static shadowRank fromNBT(CompoundTag tag) {
        return getByName(tag.getString("name"));
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public String getName() {
        return name;
    }

    private static boolean matchEP(double ep, shadowRank rank) {
        return ep >= rank.min && ep <= rank.max;
    }

    public static shadowRank calculateRank(LivingEntity shadow) {
        double ep = TensuraEPCapability.getCurrentEP(shadow);
        AtomicReference<shadowRank> returnValue = new AtomicReference<>(BASIC);
        Arrays.stream(values()).toList().forEach(rank -> {
            if (matchEP(ep, rank)) {
                returnValue.set(rank);
            }
        });
        return returnValue.get();
    }

    public ResourceLocation toLocation() {
        return new ResourceLocation("trawakened" , name.toLowerCase().replace(" ", "_"));
    }

    @Override
    public String toString() {
        return "shadowRank{" +
                "name='" + name + '\'' +
                ", min=" + min +
                ", max=" + max +
                '}';
    }
}
