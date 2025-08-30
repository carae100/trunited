package io.github.dracosomething.trawakened.network.play2server;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import io.github.dracosomething.trawakened.ability.skill.ultimate.ShadowMonarch;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class SummonShadowPacket {
    private final List<String> shadowKeys;

    public SummonShadowPacket(List<String> shadowKeys) {
        this.shadowKeys = shadowKeys;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.shadowKeys.size());
        for (String key : this.shadowKeys) {
            buf.writeUtf(key);
        }
    }

    public SummonShadowPacket(FriendlyByteBuf buf) {
        int size = buf.readInt();
        this.shadowKeys = new java.util.ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.shadowKeys.add(buf.readUtf());
        }
    }

    public static void handle(SummonShadowPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                handleServer(msg, player);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private static void handleServer(SummonShadowPacket msg, ServerPlayer player) {
        ManasSkillInstance instance = SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SHADOW_MONARCH.get()).orElse(null);
        if (instance != null && instance.getSkill() instanceof ShadowMonarch skill) {
            CompoundTag storage = skill.getShadowStorage();
            
            for (String shadowKey : msg.shadowKeys) {
                if (storage.contains(shadowKey)) {
                    CompoundTag shadowData = storage.getCompound(shadowKey);
                    
                    EntityType<?> entityType = EntityType.byString(shadowData.getString("entityType")).orElse(null);
                    if (entityType != null) {
                        LivingEntity entity = (LivingEntity) entityType.create(player.level);
                        if (entity != null) {
                            entity.deserializeNBT(shadowData.getCompound("EntityData"));
                            
                            entity.setPos(player.position().add(
                                (Math.random() - 0.5) * 4,
                                0,
                                (Math.random() - 0.5) * 4
                            ));
                            
                            entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100));
                            
                            player.level.addFreshEntity(entity);
                            
                            storage.remove(shadowKey);
                        }
                    }
                }
            }
            
            instance.getOrCreateTag().put("ShadowStorage", storage);
            skill.setShadowStorage(storage);
        }
    }
}
