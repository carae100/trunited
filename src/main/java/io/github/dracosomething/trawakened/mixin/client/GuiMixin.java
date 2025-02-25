package io.github.dracosomething.trawakened.mixin.client;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.handler.client.HUDHandler;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.race.TensuraRaces;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.dracosomething.trawakened.ability.skill.unique.Alternate;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
@Mixin(HUDHandler.class)
public class GuiMixin {
    @Shadow
    private static void renderMobAnalysis(LivingEntity target, int level) {
        return;
    }

    @Shadow
    private static LocalPlayer player;

    @Unique
    private static final ResourceLocation DARK_BLUE_HEARTS = new ResourceLocation("trawakened", "textures/gui/hearts/dark_blue_bar.png");

    @Unique
    private static final ResourceLocation HP_BAR = new ResourceLocation("trawakened", "textures/gui/hearts/hp_bar.png");

    @Unique
    private static final ResourceLocation MELT_HP_BAR = new ResourceLocation("trawakened", "textures/gui/hearts/melt_hp_bar.png");

    @Unique
    private static final ResourceLocation PLAGUE_HP_BAR = new ResourceLocation("trawakened", "textures/gui/hearts/plague_hp_bar.png");

    @Inject(method = "getHealthBarToRender", at = @At("HEAD"), cancellable = true, remap = false)
    private static void setCustomHearts(boolean spiritual, CallbackInfoReturnable<ResourceLocation> cir) {
        if (spiritual) {
            if (player.hasEffect((MobEffect)effectRegistry.BRAINDAMAGE.get())) {
                cir.setReturnValue(DARK_BLUE_HEARTS);
            } else if (player.hasEffect(effectRegistry.SHPPOISON.get())) {
                cir.setReturnValue(HP_BAR);
            }
        } else if (player.hasEffect((MobEffect) effectRegistry.MELT.get())) {
            cir.setReturnValue(MELT_HP_BAR);
        } else if (player.hasEffect(effectRegistry.PLAGUEEFFECT.get())) {
            cir.setReturnValue(PLAGUE_HP_BAR);
        } else if (player.hasEffect(effectRegistry.HEALPOISON.get())) {
            cir.setReturnValue(DARK_BLUE_HEARTS);
        }
    }

    @ModifyVariable(
            method = "renderMobAnalysis",
            at = @At(
                    value = "STORE"
            ),
            ordinal = 1,
            remap = false
    )
    private static int HealthNormalIfAlternate(int value) {
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(player, skillRegistry.ALTERNATE.get());
        if (instance != null) {
            Alternate.Assimilation assimilation = Alternate.Assimilation.fromNBT(instance.getOrCreateTag().getCompound("assimilation"));
            if (assimilation == Alternate.Assimilation.COMPLETE) {
                return 20;
            }
        }
        return value;
    }

    @ModifyVariable(
            method = "renderMobAnalysis",
            at = @At(
                    value = "STORE"
            ),
            ordinal = 2,
            remap = false
    )
    private static int SpiritualNormalIfAlternate(int value) {
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(player, skillRegistry.ALTERNATE.get());
        if (instance != null) {
            Alternate.Assimilation assimilation = Alternate.Assimilation.fromNBT(instance.getOrCreateTag().getCompound("assimilation"));
            if (assimilation == Alternate.Assimilation.COMPLETE) {
                return 60;
            }
        }
        return value;
    }

    @ModifyVariable(
            method = "renderMobAnalysis",
            at = @At(
                    value = "STORE"
            ),
            ordinal = 3,
            remap = false
    )
    private static int ArmorNormalIfAlternate(int value) {
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(player, skillRegistry.ALTERNATE.get());
        if (instance != null) {
            Alternate.Assimilation assimilation = Alternate.Assimilation.fromNBT(instance.getOrCreateTag().getCompound("assimilation"));
            if (assimilation == Alternate.Assimilation.COMPLETE) {
                return 0;
            }
        }
        return value;
    }

    @ModifyVariable(
            method = "renderMobAnalysis",
            at = @At(
                    value = "STORE"
            ),
            ordinal = 3,
            remap = false
    )
    private static int MPNormalIfAlternate(int value) {
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(player, skillRegistry.ALTERNATE.get());
        if (instance != null) {
            Alternate.Assimilation assimilation = Alternate.Assimilation.fromNBT(instance.getOrCreateTag().getCompound("assimilation"));
            if (assimilation == Alternate.Assimilation.COMPLETE) {
                return 0;
            }
        }
        return value;
    }

    @ModifyVariable(
            method = "renderMobAnalysis",
            at = @At(
                    value = "STORE"
            ),
            ordinal = 0,
            remap = false
    )
    private static Race RaceHumanifAlternate(Race value) {
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(player, skillRegistry.ALTERNATE.get());
        if (instance != null) {
            Alternate.Assimilation assimilation = Alternate.Assimilation.fromNBT(instance.getOrCreateTag().getCompound("assimilation"));
            if (assimilation == Alternate.Assimilation.COMPLETE) {
                return TensuraRaces.HUMAN.get();
            }
        }
        return value;
    }

    @Inject(
            method = "renderMobAnalysis",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lcom/github/manasmods/tensura/capability/race/TensuraPlayerCapability;getMagicule(Lnet/minecraft/world/entity/player/Player;)D",
                    shift = At.Shift.AFTER
            ),
            remap = false
    )
    private static void MPNormalIfAlternate(LivingEntity target, int level, CallbackInfo ci, @Local(ordinal = 8) LocalIntRef mana) {
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(player, skillRegistry.ALTERNATE.get());
        if (instance != null) {
            Alternate.Assimilation assimilation = Alternate.Assimilation.fromNBT(instance.getOrCreateTag().getCompound("assimilation"));
            if (assimilation == Alternate.Assimilation.COMPLETE) {
                Random random = new Random();
                mana.set(random.nextInt(50, 70));
            }
        }
        mana.set(mana.get());
    }

    @Inject(
            method = "renderMobAnalysis",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lcom/github/manasmods/tensura/capability/race/TensuraPlayerCapability;getAura(Lnet/minecraft/world/entity/player/Player;)D",
                    shift = At.Shift.AFTER
            ),
            remap = false
    )
    private static void APNormalIfAlternate(LivingEntity target, int level, CallbackInfo ci, @Local(ordinal = 7) LocalIntRef i) {
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(player, skillRegistry.ALTERNATE.get());
        if (instance != null) {
            Alternate.Assimilation assimilation = Alternate.Assimilation.fromNBT(instance.getOrCreateTag().getCompound("assimilation"));
            if (assimilation == Alternate.Assimilation.COMPLETE) {
                Random random = new Random();
                i.set(random.nextInt(760, 1140));
            }
        }
        i.set(i.get());
    }

    @Inject(
            method = "renderMobAnalysis",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lcom/github/manasmods/tensura/capability/skill/TensuraSkillCapability;getSkillInSlot(Lnet/minecraft/world/entity/LivingEntity;I)Lcom/github/manasmods/manascore/api/skills/ManasSkill;",
                    shift = At.Shift.AFTER
            ),
            remap = false
    )
    private static void SetNoAlternate(LivingEntity target, int level, CallbackInfo ci, @Local LocalRef<ManasSkill> skill) {
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(player, skillRegistry.ALTERNATE.get());
        if (instance != null) {
            Alternate.Assimilation assimilation = Alternate.Assimilation.fromNBT(instance.getOrCreateTag().getCompound("assimilation"));
            if (assimilation == Alternate.Assimilation.COMPLETE) {
                if (skill == skillRegistry.ALTERNATE.get()) {
                    skill.set(null);
                }
            }
        }
        skill.set(skill.get());
    }

    @Inject(
            method = "renderMobAnalysis",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lcom/github/manasmods/tensura/capability/ep/TensuraEPCapability;getEP(Lnet/minecraft/world/entity/LivingEntity;)D",
                    shift = At.Shift.AFTER,
                    ordinal = 2
            ),
            remap = false
    )
    private static void EPNormalIfAlternate(LivingEntity target, int level, CallbackInfo ci, @Local(ordinal = 8) LocalIntRef mana, @Local(ordinal = 7) LocalIntRef i, @Local(ordinal = 0) LocalRef<String> var10000, @Local LocalRef<String> text) {
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(player, skillRegistry.ALTERNATE.get());
        if (instance != null) {
            Alternate.Assimilation assimilation = Alternate.Assimilation.fromNBT(instance.getOrCreateTag().getCompound("assimilation"));
            if (assimilation == Alternate.Assimilation.COMPLETE) {
                int ep = mana.get()+ i.get();
                var10000.set(Component.translatable("tensura.attribute.existence_points.shortened_name").getString() + ":" + (ep));
            }
        }
        var10000.set(var10000.get());
    }

    @Inject(
            method = "renderMobAnalysis",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lcom/github/manasmods/tensura/capability/ep/TensuraEPCapability;getCurrentEP(Lnet/minecraft/world/entity/LivingEntity;)D",
                    shift = At.Shift.AFTER
            ),
            remap = false
    )
    private static void EPNormalIfAlternateMob(LivingEntity target, int level, CallbackInfo ci, @Local(ordinal = 8) LocalIntRef mana, @Local(ordinal = 7) LocalIntRef i, @Local(ordinal = -1) LocalRef<String> var10000) {
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(player, skillRegistry.ALTERNATE.get());
        if (instance != null) {
            Alternate.Assimilation assimilation = Alternate.Assimilation.fromNBT(instance.getOrCreateTag().getCompound("assimilation"));
            if (assimilation == Alternate.Assimilation.COMPLETE) {
                int ep = mana.get()+ i.get();
                var10000.set(Component.translatable("tensura.attribute.existence_points.shortened_name").getString() + ":" + (ep));
            }
        }
        var10000.set(var10000.get());
    }
}
