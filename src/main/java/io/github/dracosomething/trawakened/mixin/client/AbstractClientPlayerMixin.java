package io.github.dracosomething.trawakened.mixin.client;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import io.github.dracosomething.trawakened.ability.skill.unique.Alternate;
import io.github.dracosomething.trawakened.entity.client.renderer.CustomPlayerRenderer.*;
import io.github.dracosomething.trawakened.registry.skillregistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin extends Player {
    @Deprecated
    private AbstractClientPlayerMixin(Level p_250508_, BlockPos p_250289_, float p_251702_, GameProfile p_252153_, @Nullable ProfilePublicKey p_234114_) {
        super(p_250508_, p_250289_, p_251702_, p_252153_, p_234114_);
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return false;
    }

    @Inject(method = "getModelName", at = @At("HEAD"), cancellable = true)
    private void getNewModelNames(CallbackInfoReturnable<String> cir) {
        AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(player, skillregistry.ALTERNATE.get());
        if (instance != null) {
            CompoundTag tag = instance.getOrCreateTag();
            Alternate.Assimilation assimilation = Alternate.Assimilation.fromNBT(tag.getCompound("assimilation"));
            if (assimilation == Alternate.Assimilation.FLAWED) {
                FlawedPlayerRenderer.getFlawedRenderer(((AbstractClientPlayer) (Object) this)).ifPresent(cir::setReturnValue);
            } else {
                OverdrivenPlayerRenderer.getOverdrivenRenderer(((AbstractClientPlayer) (Object) this)).ifPresent(cir::setReturnValue);
            }
            Alternate.AlternateType alternateType = Alternate.AlternateType.fromNBT(tag.getCompound("alternate_type"));
            if (alternateType == Alternate.AlternateType.INTRUDER) {
                IntruderPlayerRenderer.getIntruderRenderer(((AbstractClientPlayer) (Object) this)).ifPresent(cir::setReturnValue);
            }
        }
    }
}
