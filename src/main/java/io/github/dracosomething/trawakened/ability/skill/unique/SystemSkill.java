package io.github.dracosomething.trawakened.ability.skill.unique;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import com.github.manasmods.tensura.ability.ISpatialStorage;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.menu.SpatialStorageMenu;
import com.github.manasmods.tensura.menu.container.SpatialStorageContainer;
import com.github.manasmods.tensura.network.TensuraNetwork;
import com.github.manasmods.tensura.network.play2client.ClientboundSpatialStorageOpenPacket;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.google.common.collect.Multimap;
import io.github.dracosomething.trawakened.event.SystemLevelUpEvent;
import io.github.dracosomething.trawakened.registry.itemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import org.stringtemplate.v4.misc.MultiMap;

import java.util.List;
import java.util.Random;

public class SystemSkill extends Skill implements ISpatialStorage {
    private CompoundTag tag;

    public SystemSkill() {
        super(SkillType.UNIQUE);
        tag = new CompoundTag();
    }

    @Override
    public @Nullable MutableComponent getName() {
        if (tag.getBoolean("isGui")) {
            return Component.translatable("trawakened.skill.system", new Object[]{tag.getInt("level")});
        } else {
            return Component.translatable("trawakened.skill.system_normal");
        }
    }

    @Override
    public List<MobEffect> getImmuneEffects(ManasSkillInstance instance, LivingEntity entity) {
        return List.of(MobEffects.MOVEMENT_SLOWDOWN, MobEffects.WEAKNESS, MobEffects.BLINDNESS, MobEffects.DARKNESS, MobEffects.POISON, MobEffects.WITHER, MobEffects.HUNGER, TensuraMobEffects.PARALYSIS.get(), TensuraMobEffects.INFECTION.get());
    }

    @Override
    public int modes() {
        return 1;
    }

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        openSpatialStorage(entity, instance);
    }

    @Override
    public SpatialStorageContainer getSpatialStorage(ManasSkillInstance instance) {
        SpatialStorageContainer container = new SpatialStorageContainer(90, 999);
        container.fromTag(instance.getOrCreateTag().getList("SpatialStorage", 10));
        return container;
    }

    public void openSpatialStorage(LivingEntity entity, ManasSkillInstance instance) {
        if (entity instanceof ServerPlayer player) {
            player.closeContainer();
            player.nextContainerCounter();
            player.playNotifySound(SoundEvents.ENDER_CHEST_OPEN, SoundSource.PLAYERS, 1.0F, 1.0F);
            ManasSkill skill = instance.getSkill();
            SpatialStorageContainer container = this.getSpatialStorage(instance);
            tag.putBoolean("isGui", true);
            instance.getOrCreateTag().put("data", this.tag);
            System.out.println(instance.getTag());
            TensuraNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> {
                return player;
            }), new ClientboundSpatialStorageOpenPacket(player.getId(), player.containerCounter, container.getContainerSize(), container.getMaxStackSize(), SkillUtils.getSkillId(skill)));
            player.containerMenu = new SpatialStorageMenu(player.containerCounter, player.getInventory(), player, container, skill);
            player.initMenu(player.containerMenu);
            MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, player.containerMenu));
        }
    }

    @Override
    public void onLearnSkill(ManasSkillInstance instance, LivingEntity living, UnlockSkillEvent event) {
        CompoundTag tag = instance.getOrCreateTag();
        tag.put("owner", living.serializeNBT());
        tag.putInt("level", 0);
        tag.putInt("nextLevel", 1150);
        tag.putInt("maxLevel", 140);
        tag.putBoolean("isGui", false);
        this.tag.putBoolean("isGui", tag.getBoolean("isGui"));
        this.tag.putInt("level", tag.getInt("level"));
        tag.put("data", this.tag);
    }

    public void onLevelUp(ManasSkillInstance instance, LivingEntity entity, SystemLevelUpEvent event) {
        int level = instance.getOrCreateTag().getInt("level");
        AttributeModifier modifier = new AttributeModifier("f444bd0f-d420-4c07-ba1a-f23118508a6f", 0.5*level, AttributeModifier.Operation.ADDITION);
        entity.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).addPermanentModifier(modifier);
        if (level%4 == 0) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, -1, level / 4));
        }
        if (level%30 == 0) {
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, -1, level / 30));
        }
    }

    @Override
    public void onTakenDamage(ManasSkillInstance instance, LivingDamageEvent event) {
        int level = instance.getOrCreateTag().getInt("level");
        if (level >= 30) {
            if (event.getEntity().getHealth() < event.getEntity().getMaxHealth()*0.3) {
                event.setAmount(event.getAmount() / 2);
            }
        }
    }

    @Override
    public void onDamageEntity(ManasSkillInstance instance, LivingEntity entity, LivingHurtEvent event) {
        Random random = new Random();
        float dmg = event.getEntity().getHealth() - event.getAmount();
        if (dmg <= 0.0) {
            if (event.getEntity().getType().equals(EntityType.CREEPER) || event.getEntity().getType().equals(EntityType.WANDERING_TRADER)) {
                if (random.nextInt(0, 100) <= (event.getEntity().getType().equals(EntityType.CREEPER) ? 3 : 33)) {
                    newDrop(itemRegistry.STEALTH_STONE.get(), event, entity);
                }
            }
        }
    }

    private void newDrop(Item drop, LivingHurtEvent event, LivingEntity source) {
        Vec3 pos = event.getEntity().position();
        ItemEntity itemDrop = new ItemEntity(event.getEntity().level, pos.x, pos.y, pos.z, drop.getDefaultInstance());
        source.level.addFreshEntity(itemDrop);
    }

    public CompoundTag getTag() {
        return tag;
    }
}
