package io.github.dracosomething.trawakened.ability.skill.extra;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import io.github.dracosomething.trawakened.registry.enchantRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PrimalArmor extends Skill {
    public PrimalArmor() {
        super(SkillType.EXTRA);
    }

    @Override
    public int modes() {
        return 2;
    }

    @Override
    public int nextMode(LivingEntity entity, TensuraSkillInstance instance, boolean reverse) {
        if (reverse) {
            return instance.getMode() == 1 ? 2 : instance.getMode() - 1;
        } else {
            return instance.getMode() == 2 ? 1 : instance.getMode() + 1;
        }
    }



    @Override
    public boolean canBeToggled(ManasSkillInstance instance, LivingEntity entity) {
        return true;
    }

    @Override
    public boolean canTick(ManasSkillInstance instance, LivingEntity entity) {
        return instance.isToggled();
    }

    @Override
    public void onTick(ManasSkillInstance instance, LivingEntity living) {
        if (instance.isToggled()) {
            List<ItemStack> list = List.of(living.getItemBySlot(EquipmentSlot.CHEST),
                    living.getItemBySlot(EquipmentSlot.FEET),
                    living.getItemBySlot(EquipmentSlot.HEAD),
                    living.getItemBySlot(EquipmentSlot.LEGS),
                    living.getItemBySlot(EquipmentSlot.MAINHAND),
                    living.getItemBySlot(EquipmentSlot.OFFHAND));
            for (ItemStack item : list) {
                item.enchant(enchantRegistry.PRIMAL_ARMOR.get(), instance.isMastered(living) ? 5 : 2);
            }
        }
    }
}
