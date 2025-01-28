package io.github.dracosomething.trawakened.mobeffect;

import com.github.manasmods.tensura.effect.template.TensuraMobEffect;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
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
    public void addAttributeModifiers(LivingEntity p_19478_, AttributeMap p_19479_, int p_19480_) {
        p_19478_.addEffect(new MobEffectInstance(effectRegistry.CREATIVE_MENU.get(), 2400, 50, false, false, false));
        super.addAttributeModifiers(p_19478_, p_19479_, p_19480_);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity pEntity, int pAmpilifier) {
        if(pEntity instanceof Player player){
            if(!player.isCreative()){
                System.out.println("ewrwer");
                if(player instanceof ServerPlayer serverPlayer){
                    System.out.println("trewtrewt");
                    serverPlayer.setGameMode(GameType.CREATIVE);
                    serverPlayer.getAbilities().invulnerable = false;
                    serverPlayer.getAbilities().instabuild = false;
                }
            }
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity p_19469_, AttributeMap p_19470_, int p_19471_) {
        if(p_19469_ instanceof Player player){
            if(player.isCreative()){
                if(player instanceof ServerPlayer serverPlayer){
                    serverPlayer.setGameMode(GameType.SURVIVAL);
                }
            }
        }
        p_19469_.addEffect(new MobEffectInstance(effectRegistry.CREATIVE_MENU.get(), 2400, 50, false, false, false));
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return p_19455_%20==0;
    }
}
