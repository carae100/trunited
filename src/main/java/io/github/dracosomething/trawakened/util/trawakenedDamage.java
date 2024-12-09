package io.github.dracosomething.trawakened.util;

import com.github.manasmods.tensura.util.damage.TensuraDamageSource;
import com.github.manasmods.tensura.util.damage.TensuraEntityDamageSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public class trawakenedDamage {
    public static DamageSource PLAGUE = (new TensuraDamageSource("trawakened.plague")).setNotTensuraMagic().setIgnoreBarrier(100.0F).setIgnoreResistance(100.0F).bypassArmor().bypassEnchantments().bypassInvul().bypassMagic().setMagic();

    public trawakenedDamage(){}

    public static DamageSource plague(Entity pSource) {
        return (new TensuraEntityDamageSource("trawakened.plague", pSource)).setNoKnock().setNotTensuraMagic().setIgnoreBarrier(100.0F).setIgnoreResistance(100.0F).bypassArmor().bypassEnchantments().bypassInvul().bypassMagic().setMagic();
    }
}
