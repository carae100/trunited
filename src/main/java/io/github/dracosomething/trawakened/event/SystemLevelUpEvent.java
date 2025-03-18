package io.github.dracosomething.trawakened.event;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.event.SkillEvent;
import net.minecraft.world.entity.LivingEntity;

public class SystemLevelUpEvent extends SkillEvent {
    private LivingEntity entity;
    private int oldLevel;
    private int newLevel;

    public SystemLevelUpEvent(ManasSkillInstance skillInstance, LivingEntity entity, int oldLevel, int newLevel) {
        super(skillInstance);
        this.entity = entity;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public int getNewLevel() {
            return newLevel;
    }

    public int getOldLevel() {
        return oldLevel;
    }

    public void setNewLevel(int newLevel) {
        this.newLevel = newLevel;
    }
}
