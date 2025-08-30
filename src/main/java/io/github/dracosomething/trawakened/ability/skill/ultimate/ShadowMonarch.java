package io.github.dracosomething.trawakened.ability.skill.ultimate;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import com.github.manasmods.tensura.Tensura;
import com.github.manasmods.tensura.ability.ISpatialStorage;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.menu.SpatialStorageMenu;
import com.github.manasmods.tensura.menu.container.SpatialStorageContainer;
import com.github.manasmods.tensura.network.TensuraNetwork;
import com.github.manasmods.tensura.network.play2client.ClientboundSpatialStorageOpenPacket;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.mojang.math.Vector3f;
import io.github.dracosomething.trawakened.ability.skill.unique.SystemSkill;
import io.github.dracosomething.trawakened.capability.ShadowCapability.AwakenedShadowCapability;
import io.github.dracosomething.trawakened.event.BecomeShadowEvent;
import io.github.dracosomething.trawakened.helper.skillHelper;
import io.github.dracosomething.trawakened.library.MonarchsDomain;
import io.github.dracosomething.trawakened.library.shadowRank;
import io.github.dracosomething.trawakened.mobeffect.MonarchsDomainEffect;
import io.github.dracosomething.trawakened.network.TRAwakenedNetwork;
import io.github.dracosomething.trawakened.network.play2client.OpenBecomeShadowscreen;
import io.github.dracosomething.trawakened.network.play2client.OpenNamingscreen;
import io.github.dracosomething.trawakened.network.play2client.OpenShadowSummonScreen;
import io.github.dracosomething.trawakened.network.play2client.openWordScreen;
import io.github.dracosomething.trawakened.registry.dimensionRegistry;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.registry.particleRegistry;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.html.HTML;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.github.manasmods.tensura.race.RaceHelper.evolveMobs;

public class ShadowMonarch extends Skill implements ISpatialStorage {
    private CompoundTag ShadowStorage;
    private CompoundTag data;

    public ShadowMonarch() {
        super(SkillType.ULTIMATE);
        ShadowStorage = new CompoundTag();
        data = new CompoundTag();
    }

    @Override
    public @Nullable MutableComponent getName() {
        return data.getBoolean("awakened") ? Component.translatable("trawakened.skill.shadow_monarch.awakened") : Component.translatable("trawakened.skill.shadow_monarch");
    }

    @Override
    public boolean meetEPRequirement(Player entity, double newEP) {
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(entity, skillRegistry.SYSTEM.get());
        if (instance != null) {
            if (instance.getSkill() instanceof SystemSkill skill) {
                return hasSystemSkills(entity) && skill.getLevel() >= 110;
            }
        }
        return false;
    }

    public int modes() {
        return 8;
    }

    public int nextMode(LivingEntity entity, TensuraSkillInstance instance, boolean reverse) {
        if (reverse) {
            return switch (instance.getMode()) {
                case 1 -> data.getBoolean("kamish") ? 8 : data.getBoolean("awakened") ? 7 : 6;
                case 2 -> 1;
                case 3 -> 2;
                case 4 -> 3;
                case 5 -> 4;
                case 6 -> 5;
                case 7 -> 6;
                case 8 -> 7;
                default -> 0;
            };
        } else {
            return switch (instance.getMode()) {
                case 1 -> 2;
                case 2 -> 3;
                case 3 -> 4;
                case 4 -> 5;
                case 5 -> 6;
                case 6 -> data.getBoolean("awakened") ? 7 : 1;
                case 7 -> data.getBoolean("kamish") ? 8 : 1;
                case 8 -> 1;
                default -> 0;
            };
        }
    }

    public Component getModeName(int mode) {
        return switch (mode) {
            case 1 -> Component.translatable("trawakened.skill.shadow_monarch.mode.arise");
            case 2 -> Component.translatable("trawakened.skill.shadow_monarch.mode.storage");
            case 3 -> Component.translatable("trawakened.skill.shadow_monarch.mode.shadow_summon");
            case 4 -> Component.translatable("trawakened.skill.shadow_monarch.mode.mass_summoning");
            case 5 -> Component.translatable("trawakened.skill.shadow_monarch.mode.marking");
            case 6 -> data.getBoolean("awakened") ?
                    Component.translatable("trawakened.skill.shadow_monarch.awakened.mode.monarchs_domain", data.getString("mode_name")) :
                    Component.translatable("trawakened.skill.shadow_monarch.mode.monarchs_domain");
            case 7 -> Component.translatable("trawakened.skill.shadow_monarch.mode.shadow_inventory");
            case 8 -> Component.translatable("trawakened.skill.shadow_monarch.mode.dagger_summoning");
            default -> Component.empty();
        };
    }

    public void onLearnSkill(ManasSkillInstance instance, LivingEntity living, UnlockSkillEvent event) {
        instance.getOrCreateTag().putInt("maxStorage", 5);
        instance.getOrCreateTag().put("ShadowStorage", ShadowStorage);
        instance.getOrCreateTag().put("data", data);
        if (living instanceof ServerPlayer player) {
            TRAwakenedNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new openWordScreen(player.getUUID(), instance));
        }
        System.out.println(instance.getOrCreateTag());
    }

    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        Random random = new Random();
        LivingEntity target = SkillHelper.getTargetingEntity(entity, ForgeMod.REACH_DISTANCE.get().getDefaultValue(), false, true);
        List<LivingEntity> targetList = skillHelper.GetLivingEntitiesInRange(entity, 50, false);
        switch (instance.getMode()) {
            case 1:
                if (entity instanceof Player player && player.getInventory().contains(new ItemStack(Items.DRAGON_HEAD, 9))) {
                    data.putBoolean("kamish", true);
                    player.displayClientMessage(Component.translatable("message.arise.kamish"), false);
                    return;
                }
                if (!entity.isShiftKeyDown()) {
                    if (target != null && AwakenedShadowCapability.isShadow(target) && !AwakenedShadowCapability.isArisen(target)) {
                        if (AwakenedShadowCapability.getTries(target) > 0) {
                            skillHelper.sendMessageToNearbyPlayersWithSource(30, entity, Component.translatable("message.arise", getCommandWord()));
                            int chance;
                            if (TensuraEPCapability.getCurrentEP(target) * 0.75 <= TensuraEPCapability.getCurrentEP(entity)) {
                                chance = 100;
                            } else {
                                chance = (TensuraEPCapability.getCurrentEP(target) <= TensuraEPCapability.getCurrentEP(entity) ? 50 : 30);
                            }
                            if (target instanceof Player player && player instanceof ServerPlayer serverPlayer) {
                                TRAwakenedNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new OpenBecomeShadowscreen(entity.getUUID()));
                            } else {
                                AwakenedShadowCapability.setTries(target, AwakenedShadowCapability.getTries(target) - 1);
                                if (AwakenedShadowCapability.getTries(target) == 0) {
                                    target.discard();
                                    return;
                                }
                                if (random.nextInt(0, 100) <= chance) {
                                    target.setHealth(target.getMaxHealth());
                                    AwakenedShadowCapability.setArisen(target, true);
                                    target.removeEffect(TensuraMobEffects.PRESENCE_CONCEALMENT.get());
                                    target.removeEffect(MobEffects.DAMAGE_RESISTANCE);
                                    target.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                                    skillHelper.tameAnything(entity, target, this);
                                    shadowRank rank = shadowRank.calculateRank(target);
                                    SkillHelper.setFollow(target);
                                    AwakenedShadowCapability.setRank(target, rank);
                                    AwakenedShadowCapability.setOwnerUUID(target, entity.getUUID());
                                    if (target instanceof Mob mob && mob.isNoAi()) {
                                        mob.setNoAi(false);
                                    }
                                    BecomeShadowEvent event = new BecomeShadowEvent(target, entity, true);
                                    MinecraftForge.EVENT_BUS.post(event);
                                    if (target.getType().getTags().toList().contains(Tags.EntityTypes.BOSSES) ||
                                            target instanceof Player) {
                                        if (entity instanceof Player player && player instanceof ServerPlayer serverPlayer) {
                                            TRAwakenedNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new OpenNamingscreen(target.getUUID()));
                                        }
                                    }
                                    AwakenedShadowCapability.sync(target);
                                    instance.addMasteryPoint(entity);
                                } else if (entity instanceof Player player) {
                                    player.sendSystemMessage(Component.translatable("message.tries_left", AwakenedShadowCapability.getTries(target), target.getName()));
                                }
                            }
                        }
                    }
                } else {
                    skillHelper.sendMessageToNearbyPlayersWithSource(30, entity, Component.translatable("message.arise", getCommandWord()));
                    targetList.forEach((living -> {
                        if (living != null && AwakenedShadowCapability.isShadow(living) && !AwakenedShadowCapability.isArisen(living)) {
                            if (AwakenedShadowCapability.getTries(living) > 0) {
                                if (TensuraEPCapability.getCurrentEP(living) * 0.75 <= TensuraEPCapability.getCurrentEP(entity)) {
                                    living.setHealth(living.getMaxHealth());
                                    AwakenedShadowCapability.setArisen(living, true);
                                    living.removeEffect(TensuraMobEffects.PRESENCE_CONCEALMENT.get());
                                    living.removeEffect(MobEffects.DAMAGE_RESISTANCE);
                                    living.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                                    skillHelper.tameAnything(entity, living, this);
                                    shadowRank rank = shadowRank.calculateRank(living);
                                    SkillHelper.setFollow(living);
                                    AwakenedShadowCapability.setRank(living, rank);
                                    AwakenedShadowCapability.setOwnerUUID(living, entity.getUUID());
                                    if (living instanceof Mob mob && mob.isNoAi()) {
                                        mob.setNoAi(false);
                                    }
                                    BecomeShadowEvent event = new BecomeShadowEvent(living, entity, true);
                                    MinecraftForge.EVENT_BUS.post(event);
                                    if (living.getType().getTags().toList().contains(Tags.EntityTypes.BOSSES) ||
                                            living instanceof Player) {
                                        if (entity instanceof Player player && player instanceof ServerPlayer serverPlayer) {
                                            TRAwakenedNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new OpenNamingscreen(living.getUUID()));
                                        }
                                    }
                                    AwakenedShadowCapability.sync(living);
                                    instance.addMasteryPoint(entity);
                                }
                            }
                        }
                    }));
                }
                break;
            case 2:
                if (!entity.isShiftKeyDown()) {
                    if (target != null && AwakenedShadowCapability.isArisen(target) &&
                            Objects.equals(AwakenedShadowCapability.getOwnerUUID(target).toString(), entity.getUUID().toString()) &&
                            AwakenedShadowCapability.isShadow(target)) {
                        if (ShadowStorage.getAllKeys().size() < instance.getOrCreateTag().getInt("maxStorage")) {
                            System.out.println("erwwwwrwer");
                            ShadowStorage.put(target.getUUID().toString(), shadowToNBT(target));
                            instance.getOrCreateTag().put("ShadowStorage", ShadowStorage);
                            setShadowStorage(instance.getOrCreateTag().getCompound("ShadowStorage"));
                            target.discard();

                        } else if (entity instanceof Player player) {
                            player.sendSystemMessage(Component.translatable("trawakened.monarch_shadow.full_storage"));
                        }
                    }
                } else {
                    int maxStorage = instance.getOrCreateTag().getInt("maxStorage");
                    
                    targetList.forEach((living) -> {
                        if (living != null && AwakenedShadowCapability.isArisen(living) &&
                                Objects.equals(AwakenedShadowCapability.getOwnerUUID(living).toString(), entity.getUUID().toString()) &&
                                AwakenedShadowCapability.isShadow(living)) {
                            if (ShadowStorage.getAllKeys().size() < maxStorage) {
                                ShadowStorage.put(living.getUUID().toString(), shadowToNBT(living));
                                living.discard();
                            }
                        }
                    });
                    
                    instance.getOrCreateTag().put("ShadowStorage", ShadowStorage);
                    setShadowStorage(instance.getOrCreateTag().getCompound("ShadowStorage"));
                    
                    if (entity instanceof Player player && ShadowStorage.getAllKeys().size() >= maxStorage) {
                        player.sendSystemMessage(Component.translatable("trawakened.monarch_shadow.storage_area_full", ShadowStorage.getAllKeys().size(), maxStorage));
                    }
                }
                break;
            case 3:
                if (entity instanceof ServerPlayer player) {
                    TRAwakenedNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new OpenShadowSummonScreen(player.getUUID()));
                }
                break;
            case 4:
                int maxSummon = 50;
                if (instance.isMastered(entity)) {
                    maxSummon = 100;
                }
                if (data.getBoolean("awakened")) {
                    maxSummon = 255;
                }
                
                List<String> shadowKeys = new ArrayList<>(ShadowStorage.getAllKeys());
                shadowKeys.sort((key1, key2) -> {
                    double ep1 = ShadowStorage.getCompound(key1).getDouble("EP");
                    double ep2 = ShadowStorage.getCompound(key2).getDouble("EP");
                    return Double.compare(ep1, ep2);
                });
                
                int summoned = 0;
                for (String key : shadowKeys) {
                    if (summoned >= maxSummon) break;
                    
                    CompoundTag shadowData = ShadowStorage.getCompound(key);
                    if (shadowData != null && !shadowData.isEmpty()) {
                        String entityTypeStr = shadowData.getString("entityType");
                        Optional<EntityType<?>> entityTypeOpt = EntityType.byString(entityTypeStr);
                        
                        if (entityTypeOpt.isPresent()) {
                            Entity shadowEntity = entityTypeOpt.get().create(entity.getLevel());
                            if (shadowEntity instanceof LivingEntity shadowLiving) {
                                shadowLiving.deserializeNBT(shadowData.getCompound("EntityData"));
                                
                                Vec3 playerPos = entity.position();
                                double angle = 2 * Math.PI * summoned / Math.min(maxSummon, shadowKeys.size());
                                double radius = 3 + (summoned / 10.0);
                                double x = playerPos.x + radius * Math.cos(angle);
                                double z = playerPos.z + radius * Math.sin(angle);
                                
                                shadowLiving.setPos(x, playerPos.y, z);
                                entity.getLevel().addFreshEntity(shadowLiving);
                                
                                summoned++;
                            }
                        }
                        
                        ShadowStorage.remove(key);
                    }
                }
                
                instance.getOrCreateTag().put("ShadowStorage", ShadowStorage);
                setShadowStorage(instance.getOrCreateTag().getCompound("ShadowStorage"));
                break;
            case 5:
                if (target != null && target instanceof Player) {
                    if (!AwakenedShadowCapability.isShadow(target) &&
                            AwakenedShadowCapability.hasShadow(target)) {
                        if (ShadowStorage.getAllKeys().stream().findFirst().isPresent()) {
                            AwakenedShadowCapability.setHasShadow(target, true);
                            String first = ShadowStorage.getAllKeys().stream().findFirst().get();
                            ShadowStorage.getCompound(first).putString("UUID", first);
                            AwakenedShadowCapability.setStorage(target, ShadowStorage.getCompound(first));
                            ShadowStorage.getCompound(first).remove("UUID");
                            target.addEffect(new MobEffectInstance(MobEffects.GLOWING));
                            ShadowStorage.remove(first);
                        }
                    }
                }
                break;
            case 6:
                if (!data.getBoolean("awakened")) {
                    if (this.data.getCompound("domain").isEmpty()) {
                        MonarchsDomain domain = new MonarchsDomain(entity, 18000, 50, 50);
                        if (!SkillHelper.outOfMagicule(entity, domain.calculateMPCost())) {
                            domain.setInstance(instance);
                            this.data.put("domain", domain.toNBT());
                            instance.getOrCreateTag().put("data", data);
                            domain.place();
                            instance.setCoolDown(150);
                        }
                    }
                } else {
                    if (entity.isShiftKeyDown()) {
                        data.putInt("mode_domain", data.getInt("mode_domain") == 1 ? 2 : 1);
                        data.putString("mode_name", data.getString("mode_name").equals("world") ? "normal" : "world");
                    } else {
                        switch (data.getInt("mode_domain")) {
                            case 1:
                                if (this.data.getCompound("domain").isEmpty()) {
                                    MonarchsDomain domain = new MonarchsDomain(entity, 18000, 200);
                                    domain.setInstance(instance);
                                    this.data.put("domain", domain.toNBT());
                                    instance.getOrCreateTag().put("data", data);
                                    if (!SkillHelper.outOfMagicule(entity, domain.calculateMPCost())) {
                                        domain.place();
                                        instance.setCoolDown(150);
                                    }
                                }
                                break;
                            case 2:
                                if (this.data.getCompound("domain").isEmpty()) {
                                    MonarchsDomain domain = new MonarchsDomain(entity, 600, entity.level.getWorldBorder().getSize());
                                    domain.setInstance(instance);
                                    this.data.put("domain", domain.toNBT());
                                    instance.getOrCreateTag().put("data", data);
                                    if (!SkillHelper.outOfMagicule(entity, domain.calculateMPCost())) {
                                        domain.place();
                                        instance.setCoolDown(150);
                                    }
                                }
                                break;
                        }
                    }
                }
                break;
            case 7:
                if (entity.isShiftKeyDown()) {
                    if (data.getBoolean("awakened")) {
                        List<Player> list = skillHelper.getPlayersInRange(entity, entity.position(), 5, Player::isShiftKeyDown);
                        list.forEach((player) -> {
                            MinecraftServer server = player.getServer();
                            if (server != null) {
                                ServerLevel level = server.getLevel(dimensionRegistry.SHADOW);
                                if (entity.getLevel().dimension() == level.dimension()) {
                                    level = server.overworld();
                                }
                                if (level != null) {
                                    SkillHelper.moveAcrossDimensionTo(player, player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot(), level);
                                }
                            }
                        });
                    }
                } else {
                    openSpatialStorage(entity, instance);
                }
                break;
            case 8:
                if (entity instanceof Player player) {
                    player.displayClientMessage(Component.translatable("trawakened.skill.shadow_monarch.mode.dagger_summoning.todo"), false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onHeld(ManasSkillInstance instance, LivingEntity living, int heldTicks) {
        return super.onHeld(instance, living, heldTicks);
    }

    public boolean canTick(ManasSkillInstance instance, LivingEntity entity) {
        return true;
    }

    public void onTick(ManasSkillInstance instance, LivingEntity living) {
        int masteryPercentige = (int) ((float) (instance.getMastery() * 100 / instance.getMaxMastery()));
        int maxShadows = (5 * masteryPercentige) + 5;
        instance.getOrCreateTag().putInt("maxStorage", maxShadows);
        if (isInSlot(living) && data.getBoolean("awakened")) {
            TensuraParticleHelper.addServerParticlesAroundSelf(living, ParticleTypes.SMOKE, 0.5);
            TensuraParticleHelper.addServerParticlesAroundSelf(living, particleRegistry.PURPLE_FIRE.get(), 0.2);
            if (isScarlett(living)) {
                TensuraParticleHelper.addServerParticlesAroundSelf(living, new DustParticleOptions(new Vector3f(5, 5, 50), 1), 0.2);
            }
        }
    }

    public void onTakenDamage(ManasSkillInstance instance, LivingDamageEvent event) {
        if (instance.isMastered(event.getEntity())) {
            if (event.getEntity() instanceof Player player) {
                float dmg = event.getEntity().getHealth() - event.getAmount();
                if (dmg < (event.getEntity().getMaxHealth() * 0.05) && TensuraPlayerCapability.getMagicule(player) < (player.getAttribute(TensuraAttributeRegistry.MAX_MAGICULE.get()).getValue() * 0.05)) {
                    Entity source = event.getSource().getEntity();
                    if (source != null) {
                        if (source.getType().getTags().toList().contains(Tags.EntityTypes.BOSSES) ||
                                source instanceof Player) {
                            data.putBoolean("awakened", true);
                            event.getEntity().setHealth(event.getEntity().getMaxHealth());

                            TensuraPlayerCapability.getFrom(player).ifPresent((capability) -> {
                                capability.setBaseMagicule(capability.getBaseMagicule() * 3, player);
                                capability.setMagicule(capability.getBaseMagicule());
                            });
                            TensuraPlayerCapability.sync(player);
                            getShadowStorage().getAllKeys().forEach((key) -> {
                                CompoundTag entity = getShadowStorage().getCompound(key);
                                double ep = entity.getCompound("EntityData")
                                        .getCompound("ForgeCaps")
                                        .getCompound("tensura:ep")
                                        .getDouble("currentEP");
                                entity.getCompound("EntityData")
                                        .getCompound("ForgeCaps")
                                        .getCompound("tensura:ep")
                                        .putDouble("currentEP", ep * 2.0);
                                ep = entity.getCompound("EntityData")
                                        .getCompound("ForgeCaps")
                                        .getCompound("tensura:ep")
                                        .getDouble("currentEP");
                                entity.getCompound("EntityData")
                                        .getCompound("ForgeCaps")
                                        .getCompound("tensura:ep")
                                        .putDouble("EP", ep);
                            });
                            player.getLevel().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(30.0), (living) -> {
                                return !(living instanceof Player) && living.isAlive();
                            }).forEach((sub) -> {
                                if (Objects.equals(TensuraEPCapability.getPermanentOwner(sub), player.getUUID())) {
                                    TensuraEPCapability.getFrom(sub).ifPresent((epCap) -> {
                                        if (!epCap.isHarvestGift()) {
                                            epCap.setHarvestGift(true);
                                            epCap.setEP(sub, epCap.getEP() * 2.0);
                                            evolveMobs(sub);
                                            TensuraEPCapability.sync(sub);
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    private CompoundTag shadowToNBT(LivingEntity entity) {
        CompoundTag tag = new CompoundTag();
        tag.put("EntityData", entity.serializeNBT());
        tag.putString("entityType", EntityType.getKey(entity.getType()).toString());
        tag.put("rank", AwakenedShadowCapability.getRank(entity).toNBT());
        
        String displayName = entity.getDisplayName().getString();
        if (!displayName.isEmpty() && !displayName.equals(entity.getType().getDescription().getString())) {
            tag.putString("name", displayName);
        } else {
            tag.putString("name", entity.getType().getDescription().getString());
        }
        
        tag.putDouble("EP", TensuraEPCapability.getCurrentEP(entity));
        return tag;
    }

    public CompoundTag getShadowStorage() {
        return ShadowStorage;
    }

    public void setShadowStorage(CompoundTag shadowStorage) {
        ShadowStorage = shadowStorage;
    }

    public void setCommandWord(String commandWord) {
        this.data.putString("commandWord", commandWord);
    }

    public String getCommandWord() {
        return this.data.getString("commandWord");
    }

    public CompoundTag getData() {
        return data;
    }

    public void setData(CompoundTag data) {
        this.data = data;
    }

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
            System.out.println(instance.getTag());
            TensuraNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> {
                return player;
            }), new ClientboundSpatialStorageOpenPacket(player.getId(), player.containerCounter, container.getContainerSize(), container.getMaxStackSize(), SkillUtils.getSkillId(skill)));
            player.containerMenu = new SpatialStorageMenu(player.containerCounter, player.getInventory(), player, container, skill);
            player.initMenu(player.containerMenu);
            MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, player.containerMenu));
        }
    }

    private boolean isScarlett(LivingEntity living) {
        return living.getStringUUID().equals("8c20e4f8-c793-4699-ae1b-03dedd10e1b5");
    }

    private boolean hasSystemSkills(LivingEntity living) {
        return SkillUtils.hasSkill(living, skillRegistry.QUICKSILVER.get()) &&
                SkillUtils.hasSkill(living, skillRegistry.STEALTH.get()) &&
                SkillUtils.hasSkill(living, skillRegistry.BLOOD_LUST.get()) &&
                SkillUtils.hasSkill(living, skillRegistry.MUTILATION.get()) &&
                SkillUtils.hasSkill(living, skillRegistry.RULERS_AUTHORITY.get()) &&
                SkillUtils.hasSkill(living, skillRegistry.DRAGONS_FEAR.get());
    }
}
