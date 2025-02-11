package io.github.dracosomething.trawakened.capability.alternateFearCapability;

import com.github.manasmods.tensura.handler.CapabilityHandler;
import com.github.manasmods.tensura.network.TensuraNetwork;
import com.github.manasmods.tensura.network.play2client.SyncEPCapabilityPacket;
import io.github.dracosomething.trawakened.api.FearTypes;
import io.github.dracosomething.trawakened.network.TRAwakenedNetwork;
import io.github.dracosomething.trawakened.network.play2client.SyncFearCapabilityPacket;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

@Mod.EventBusSubscriber(
        modid = trawakened.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class AwakenedFearCapability implements IFearCapability{
    public static final Capability<IFearCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<IFearCapability>() {});
    private static final ResourceLocation ID = new ResourceLocation("trawakened", "fear");

    private FearTypes fear = FearTypes.getRandom();
    private int scaredAmount = 0;
    private int cooldown = 0;
    private boolean isAlternate = false;

    @SubscribeEvent
    public static void attach(AttachCapabilitiesEvent<Entity> e) {
        if (e.getObject() instanceof LivingEntity) {
            e.addCapability(ID, new AwakenedFearCapabilityProvider());
        }
    }

    public static LazyOptional<IFearCapability> getFrom(LivingEntity player) {
        return player.getCapability(CAPABILITY);
    }

    public static void sync(LivingEntity entity) {
        if (!entity.level.isClientSide()) {
            getFrom(entity).ifPresent(data -> TRAwakenedNetwork.INSTANCE.send(
                    PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity),
                    new SyncFearCapabilityPacket(data, entity.getId())
            ));
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.putString("fear", this.fear.getName());
        tag.putInt("scared_amount", this.scaredAmount);
        tag.putInt("cooldown", this.cooldown);
        tag.putBoolean("is_alternate", this.isAlternate);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.fear = FearTypes.getByName(tag.getString("fear"));
        this.scaredAmount = tag.getInt("scared_amount");
        this.cooldown = tag.getInt("cooldown");
        this.isAlternate = tag.getBoolean("is_alternate");
    }

    public static void setFearType(LivingEntity entity, FearTypes fear) {
        IFearCapability capability = (IFearCapability) CapabilityHandler.getCapability(entity, CAPABILITY);
        if (capability == null) return;
        capability.setFear(entity, fear);
    }

    public static FearTypes getFearType(LivingEntity entity) {
        IFearCapability capability = (IFearCapability) CapabilityHandler.getCapability(entity, CAPABILITY);
        return capability == null ? FearTypes.ALTERNATES : capability.getFear(entity);
    }

    public static void setScared(LivingEntity entity, int amount) {
        IFearCapability capability = (IFearCapability) CapabilityHandler.getCapability(entity, CAPABILITY);
        if (capability == null) return;
        capability.setScaredAmount(amount, entity);
    }

    public static int getScared(LivingEntity entity) {
        IFearCapability capability = (IFearCapability) CapabilityHandler.getCapability(entity, CAPABILITY);
        return capability == null ? 0 : capability.getScaredAmount(entity);
    }

    public static void setScaredCooldown(LivingEntity entity, int amount) {
        IFearCapability capability = (IFearCapability) CapabilityHandler.getCapability(entity, CAPABILITY);
        if (capability == null) return;
        capability.setCooldown(entity, amount);
    }

    public static int getScaredCooldown(LivingEntity entity) {
        IFearCapability capability = (IFearCapability) CapabilityHandler.getCapability(entity, CAPABILITY);
        return capability == null ? 0 : capability.getCooldown(entity);
    }

    public static void SetIsAlternate(LivingEntity entity, boolean isAlternate) {
        IFearCapability capability = (IFearCapability) CapabilityHandler.getCapability(entity, CAPABILITY);
        if (capability == null) return;
        capability.setIsAlternate(entity, isAlternate);
    }

    public static boolean GetIsAlternate(LivingEntity entity) {
        IFearCapability capability = (IFearCapability) CapabilityHandler.getCapability(entity, CAPABILITY);
        return capability == null ? false : capability.getIsAlternate(entity);
    }

    public static void increaseScared(LivingEntity entity) {
        setScared(entity, getScared(entity) + 1);
    }

    public static void decreaseCooldown(LivingEntity entity) {
        setScaredCooldown(entity, getScaredCooldown(entity) - 1);
    }

    public static boolean onCooldown(LivingEntity entity) {
        return getScaredCooldown(entity) > 0;
    }

    public FearTypes getFear(LivingEntity entity) {
        return fear;
    }

    public void setFear(LivingEntity entity, FearTypes types) {
        this.fear = types;
    }

    public int getScaredAmount(LivingEntity entity) {
        return scaredAmount;
    }

    public void setScaredAmount(int amount, LivingEntity entity) {
        this.scaredAmount = amount;
    }

    public int getCooldown(LivingEntity entity) {
        return cooldown;
    }

    public void setCooldown(LivingEntity entity, int amount) {
        this.cooldown = amount;
    }

    public boolean getIsAlternate(LivingEntity entity) {
        return isAlternate;
    }

    public void setIsAlternate(LivingEntity entity, boolean isAlternate) {
        this.isAlternate = isAlternate;
    }
}
