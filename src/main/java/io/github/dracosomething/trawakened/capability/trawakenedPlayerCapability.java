package io.github.dracosomething.trawakened.capability;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.capability.race.ITensuraPlayerCapability;
import com.github.manasmods.tensura.handler.CapabilityHandler;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import io.github.dracosomething.trawakened.ability.skill.ultimate.herrscherofplague;
import io.github.dracosomething.trawakened.mobeffect.PlagueEffect;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.List;

import static com.github.manasmods.tensura.capability.race.TensuraPlayerCapability.CAPABILITY;

public class trawakenedPlayerCapability {

    public static boolean isDemonLordSeed(Player player) {
        ITensuraPlayerCapability capability = (ITensuraPlayerCapability) CapabilityHandler.getCapability(player, CAPABILITY);
        return capability == null ? false : capability.isDemonLordSeed();
    }

    public static boolean isHeroEgg(Player player) {
        ITensuraPlayerCapability capability = (ITensuraPlayerCapability) CapabilityHandler.getCapability(player, CAPABILITY);
        return capability == null ? false : capability.isHeroEgg();
    }

    public static int getSoulPoints(Player player) {
        ITensuraPlayerCapability capability = (ITensuraPlayerCapability) CapabilityHandler.getCapability(player, CAPABILITY);
        return capability == null ? 0 : capability.getSoulPoints();
    }


    public static float getMaxSpiritualHealth(LivingEntity entity) {
        return (float) entity.getAttributeValue((Attribute) TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get());
    }

    public static LivingEntity setOwnerSkill(LivingEntity player, ManasSkillInstance instance) {
        return player;
    }

    public static boolean hasPlague(@Nullable LivingEntity entity) {
        if (herrscherofplague.active) {
            if (PlagueEffect.getOwner(entity) != null && PlagueEffect.getOwner(entity) == herrscherofplague.Owner) {
                return entity != null &&
                        entity.hasEffect(new MobEffectInstance((MobEffect) effectRegistry.PLAGUEEFFECT.get()).getEffect()) &&
                        !entity.isSpectator() &&
                        !(entity instanceof Player player && player.isCreative()) &&
                        !entity.isDeadOrDying();
            }
        }
        return false;
    }

    public static boolean isOverwhelmed(@Nullable LivingEntity entity) {
        return entity != null &&
                entity.hasEffect(new MobEffectInstance((MobEffect) effectRegistry.BRAINDAMAGE.get()).getEffect()) &&
                !entity.isSpectator() &&
                !(entity instanceof Player player && player.isCreative());

    }

    public static boolean hasHealPoison(@Nullable LivingEntity entity) {
        return entity != null &&
                (entity.hasEffect(new MobEffectInstance((MobEffect) effectRegistry.HEALPOISON.get()).getEffect()) || entity.hasEffect(new MobEffectInstance((MobEffect) effectRegistry.TIMESTOP.get()).getEffect())) &&
                !entity.isSpectator() &&
                !(entity instanceof Player player && player.isCreative());

    }

    public static boolean hasTimeStop(@Nullable LivingEntity entity) {
        return entity != null &&
                (entity.hasEffect(new MobEffectInstance((MobEffect) effectRegistry.TIMESTOP.get()).getEffect()) || entity.hasEffect(new MobEffectInstance((MobEffect) effectRegistry.TIMESTOP.get()).getEffect())) &&
                !entity.isSpectator() &&
                !(entity instanceof Player player && player.isCreative()) &&
                !entity.isDeadOrDying();

    }
}
