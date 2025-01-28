package io.github.dracosomething.trawakened.mobeffect;

import com.github.manasmods.tensura.effect.template.TensuraMobEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

public class CreativeMenuEffect extends TensuraMobEffect {
    public CreativeMenuEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "3959affe-d3f1-4bf7-a298-3f6609baa94d", -5, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "3959affe-d3f1-4bf7-a298-3f6609baa94d", -0.02, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, "3959affe-d3f1-4bf7-a298-3f6609baa94d", -5, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier((Attribute) ForgeMod.ATTACK_RANGE.get(), "3959affe-d3f1-4bf7-a298-3f6609baa94d", -2, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier((Attribute) ForgeMod.REACH_DISTANCE.get(), "3959affe-d3f1-4bf7-a298-3f6609baa94d", -2, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity pEntity, int pAmpilifier) {
        if(pEntity instanceof Player player){
            if(!player.isCreative()){
                if(player instanceof ServerPlayer serverPlayer){
                    serverPlayer.setGameMode(GameType.CREATIVE);
                    serverPlayer.getAbilities().invulnerable = false;
                    serverPlayer.getAbilities().instabuild = false;
                }
            }
        }
    }
}
