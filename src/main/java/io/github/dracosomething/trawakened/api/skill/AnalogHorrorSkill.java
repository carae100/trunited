package io.github.dracosomething.trawakened.api.skill;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import com.github.manasmods.tensura.ability.skill.Skill;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;

public class AnalogHorrorSkill extends Skill {
    private final List<MobEffect> list;
    private final Dictionary<String, Integer> modesList;
    private final Dictionary<String, String> StringModesList;
    private final String message;

    public AnalogHorrorSkill(SkillType type, List<MobEffect> List,
                             Dictionary<String, Integer> modesList,
                             Dictionary<String, String> StringModesList,
                             String message) {
        super(type);
        this.list = List;
        this.modesList = modesList;
        this.StringModesList = StringModesList;
        this.message = message;
    }

    @Override
    public List<MobEffect> getImmuneEffects(ManasSkillInstance instance, LivingEntity entity) {
        return list;
    }

    @Override
    public void onDeath(ManasSkillInstance instance, LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        while (modesList.keys().hasMoreElements()) {
            String name = modesList.keys().nextElement();
            entity.getPersistentData().putInt(name, modesList.get(name));
        }
        while (StringModesList.keys().hasMoreElements()) {
            String name = StringModesList.keys().nextElement();
            entity.getPersistentData().putString(name, StringModesList.get(name));
        }
    }

    @Override
    public void onRespawn(ManasSkillInstance instance, PlayerEvent.PlayerRespawnEvent event) {
        LivingEntity entity = event.getEntity();
        while (modesList.keys().hasMoreElements()) {
            String name = modesList.keys().nextElement();
            entity.getPersistentData().putInt(name, modesList.get(name));
        }
        while (StringModesList.keys().hasMoreElements()) {
            String name = StringModesList.keys().nextElement();
            entity.getPersistentData().putString(name, StringModesList.get(name));
        }
    }

    public void ParticleSummon(LivingEntity entity, int radius, ParticleOptions particle){
        AABB aabb = new AABB((double) (entity.getX() - radius), (double) (entity.getY() - radius), (double) (entity.getZ() - radius), (double) (entity.getX() + radius), (double) (entity.getY() + radius), (double) (entity.getZ() + radius));
        for(float x = (float) (entity.getX() - (float)radius); x < entity.getX() + (float)radius + 1.0F; ++x) {
            for(float y = (float) (entity.getY() - (float)radius); y < entity.getY() + (float)radius + 1.0F; ++y) {
                for(float z = (float) (entity.getZ() - (float)radius); z < entity.getZ() + (float)radius + 1.0F; ++z) {
                    if (entity.level instanceof ServerLevel world) {
                        world.sendParticles(particle, (double) x, (double) y, (double) z, 5, 1.0, 1.0, 1.0, 1.0);
                    }
                }
            }
        }
    }

    public List<Entity> DrawCircle(LivingEntity entity, int radius, boolean allie_check){
        AABB aabb = new AABB((double) (entity.getX() - radius), (double) (entity.getY() - radius), (double) (entity.getZ() - radius), (double) (entity.getX() + radius), (double) (entity.getY() + radius), (double) (entity.getZ() + radius));
        List<Entity> entities = entity.level.getEntities((Entity) null, aabb, Entity::isAlive);
        List<Entity> ret = new ArrayList();
        new Vec3((double) entity.getX(), (double) entity.getY(), (double) entity.getZ());
        Iterator var16 = entities.iterator();

        while (var16.hasNext()) {
            Entity entity2 = (Entity) var16.next();

            double x = entity2.getX();
            double y = entity2.getY();
            double z = entity2.getZ();
            double cmp = (double) (radius * radius) - ((double) entity2.getX() - x) * ((double) entity2.getX() - x) - ((double) entity2.getY() - y) * ((double) entity2.getY() - y) - ((double) entity2.getZ() - z) * ((double) entity2.getZ() - z);
            if (cmp > 0.0) {
                if(allie_check) {
                    if (!entity2.isAlliedTo(entity)) {
                        ret.add(entity2);
                    }
                } else {
                    ret.add(entity2);
                }
            }
        }

        return ret;
    }

    @Override
    public void onLearnSkill(ManasSkillInstance instance, LivingEntity living, UnlockSkillEvent event) {
        SkillAPI.getSkillsFrom(living).forgetSkill(this);
        for(int z = 0; z < 1000; z++) {
            addLearnPoint(instance, living);
        }

        if(living instanceof Player player) {
            player.displayClientMessage(Component.translatable(message).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_RED)), false);
        }

        while (modesList.keys().hasMoreElements()) {
            String name = modesList.keys().nextElement();
            living.getPersistentData().putInt(name, modesList.get(name));
        }
        while (StringModesList.keys().hasMoreElements()) {
            String name = StringModesList.keys().nextElement();
            living.getPersistentData().putString(name, StringModesList.get(name));
        }
    }
}
