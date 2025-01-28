package io.github.dracosomething.trawakened.mobeffect;

import com.github.manasmods.tensura.effect.template.TensuraMobEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import org.jetbrains.annotations.NotNull;

public class CreativeMenuEffect extends TensuraMobEffect {
    public CreativeMenuEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
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
