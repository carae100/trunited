package io.github.dracosomething.trawakened.library;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.mojang.math.Vector3f;
import io.github.dracosomething.trawakened.ability.skill.ultimate.ShadowMonarch;
import io.github.dracosomething.trawakened.capability.ShadowCapability.AwakenedShadowCapability;
import io.github.dracosomething.trawakened.helper.skillHelper;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class MonarchsDomain {
    private Vec3 position;
    private LivingEntity owner;
    private int life;
    private double width;
    private double length;
    private UUID uuid;
    private final Timer update;
    private Level level;
    private ManasSkillInstance instance;

    public MonarchsDomain(Vec3 position, LivingEntity owner, int life, double width, double length) {
        this.position = position;
        this.owner = owner;
        this.life = life;
        this.width = width;
        this.length = length;
        this.uuid = UUID.randomUUID();
        this.update = new Timer("ticker");
        this.level = owner.level;
    }

    public MonarchsDomain(LivingEntity owner, int life, double width, double length) {
        this.position = owner.position();
        this.owner = owner;
        this.life = life;
        this.width = width;
        this.length = length;
        this.uuid = UUID.randomUUID();
        this.update = new Timer("ticker");
        this.level = owner.level;
    }

    public MonarchsDomain(LivingEntity owner, int life, double size) {
        this.position = owner.position();
        this.owner = owner;
        this.life = life;
        this.width = size;
        this.length = size;
        this.uuid = UUID.randomUUID();
        this.update = new Timer("ticker");
        this.level = owner.level;
    }

    public MonarchsDomain(LivingEntity owner, Vec3 position, int life, double width, double length, UUID uuid) {
        this.position = position;
        this.owner = owner;
        this.life = life;
        this.width = width;
        this.length = length;
        this.uuid = uuid;
        this.update = new Timer("ticker");
        this.level = owner.level;
    }

    public void place() {
        Timer timer = new Timer("domain");
        timer.schedule(new Task() {
            int i = 0;

            public void run() {
                i++;
                if (i <= 10) {
                    skillHelper.ParticleCircle(position, level, i, 1, new DustParticleOptions(new Vector3f(Vec3.fromRGB24(11557101)), 1));
                }
                skillHelper.ParticleRing(position, level, i, 2, 5, new DustParticleOptions(new Vector3f(Vec3.fromRGB24(11557101)), 1));
                if (i == 15) {
                    timer.cancel();
                }
            }
        }, 0, 1);
        update.schedule(new Task() {
            public void run() {
                Tick();
            }
        }, 0, 1);
    }

    protected void Tick() {
        skillHelper.ParticleRing(position, level, (int) calculateRadius(), 2, 3, new DustParticleOptions(new Vector3f(Vec3.fromRGB24(11557101)), 1));
        List<LivingEntity> list = skillHelper.GetLivingEntitiesInRange(this.owner, this.position, (int) this.calculateRadius());
        list.forEach((living) -> {
            if (living != this.owner) {
                if (AwakenedShadowCapability.isShadow(living) &&
                        AwakenedShadowCapability.isArisen(living) &&
                        AwakenedShadowCapability.getOwnerUUID(living).equals(this.owner.getUUID())) {
                    SkillHelper.addEffectWithSource(living, this.owner, effectRegistry.MONARCHS_DOMAIN.get(), 2, 1, false, false, false, false);
                }
                if (living instanceof Player player) {
                    if (this.instance.getSkill() instanceof ShadowMonarch skill) {
                        AwakenedShadowCapability.setHasShadow(player, true);
                        CompoundTag storage = skill.getShadowStorage();
                        if (!storage.isEmpty()) {
                            storage.getAllKeys().stream().sorted((shadow1, shadow2) -> {
                                CompoundTag tag1 = storage.getCompound(shadow1).getCompound("EntityData");
                                CompoundTag tag2 = storage.getCompound(shadow2).getCompound("EntityData");
                                return Double.compare(tag1.getCompound("ForgeCaps").getCompound("tensura:ep").getDouble("currentEP"), tag2.getCompound("ForgeCaps").getCompound("tensura:ep").getDouble("currentEP"));
                            });
                            CompoundTag shadow = storage.getCompound(storage.getAllKeys().stream().findFirst().get());
                            AwakenedShadowCapability.setStorage(player, shadow);
                        }
                    }
                }
            }
        });
        if (life <= 0) {
            this.remove();
        }
        --life;
    }

    private double calculateRadius() {
        return this.length/2;
    }

    public int calculateMPCost() {
        int mpCost = 0;
        List<LivingEntity> list = skillHelper.GetLivingEntitiesInRange(this.owner, (int) this.calculateRadius(), false);
        for (LivingEntity living : list) {
            mpCost += 1150;
        }
        return mpCost;
    }

    public void remove() {
        this.instance.getOrCreateTag().getCompound("data").remove("domain");
        if (this.instance.getSkill() instanceof ShadowMonarch skill) {
            skill.setData(this.instance.getOrCreateTag().getCompound("data"));
        }
        this.position = null;
        this.life = 0;
        this.owner = null;
        this.width = 0.0;
        this.length = 0.0;
        this.uuid = null;
        update.cancel();
        this.level = null;
        this.instance = null;
    }

    public UUID getUUID() {
        return uuid;
    }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        CompoundTag vec3 = new CompoundTag();
        vec3.putDouble("x", position.x);
        vec3.putDouble("y", position.y);
        vec3.putDouble("z", position.z);
        tag.put("position", vec3);
        tag.putInt("life", life);
        tag.putDouble("width", width);
        tag.putDouble("length", length);
        tag.putUUID("uuid", uuid);
        return tag;
    }

    public static MonarchsDomain fromNBT(CompoundTag tag, LivingEntity sender) {
        CompoundTag vec3 = tag.getCompound("position");
        return new MonarchsDomain(sender,
                new Vec3(vec3.getDouble("x"), vec3.getDouble("y"), vec3.getDouble("z")),
                tag.getInt("life"),
                tag.getDouble("width"),
                tag.getDouble("length"),
                tag.hasUUID("uuid") ? tag.getUUID("uuid") : UUID.randomUUID()
        );
    }

    public void setInstance(ManasSkillInstance instance) {
        this.instance = instance;
    }
}
