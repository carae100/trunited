package io.github.dracosomething.trawakened.api.race;

import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.util.JumpPowerHelper;
import com.mojang.datafixers.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class HerrscherRace extends Race {
    private final Generation gen;

    public HerrscherRace(Generation gen) {
        super(Difficulty.EXTREME);
        this.gen = gen;
    }

    @Override
    public double getBaseHealth() {
        return gen.getHealth();
    }

    @Override
    public float getPlayerSize() {
        return 2.0F;
    }

    @Override
    public double getBaseAttackDamage() {
        return gen.getDamage();
    }

    @Override
    public double getBaseAttackSpeed() {
        return gen.getAttackspeed();
    }

    @Override
    public double getKnockbackResistance() {
        return gen.getKnockbackresistance();
    }

    @Override
    public double getMovementSpeed() {
        return gen.getSpeed();
    }

    public double getJumpHeight() {
        return gen.getJump();
    }

    @Override
    public Pair<Double, Double> getBaseAuraRange() {
        return gen.getAp();
    }

    @Override
    public Pair<Double, Double> getBaseMagiculeRange() {
        return gen.getMp();
    }

    @Override
    public void raceTick(Player player) {
        player.addEffect(new MobEffectInstance(gen.getEffect(), 40, gen.getAmplifier(), false, false, false));
    }

    @Getter
    @AllArgsConstructor
    public enum Generation{
        FIRST(500, 15, 4, 10, 0.20, JumpPowerHelper.defaultPlayer()+0.15, Pair.of(5000.0, 10000.0), Pair.of(50000.0, 100000.0), MobEffects.REGENERATION, 10),
        SECOND(2500, 20, 3, 12, 0.22, JumpPowerHelper.defaultPlayer()+0.20, Pair.of(15000.0, 50000.0), Pair.of(150000.0, 500000.0), TensuraMobEffects.INSTANT_REGENERATION.get(), 1);
        private final double health;
        private final double damage;
        private final double attackspeed;
        private final double knockbackresistance;
        private final double speed;
        private final double jump;
        private final Pair<Double, Double> ap;
        private final Pair<Double, Double> mp;
        private final MobEffect effect;
        private final int amplifier;
    }
}
