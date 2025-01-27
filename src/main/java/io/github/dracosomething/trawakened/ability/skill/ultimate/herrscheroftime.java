package io.github.dracosomething.trawakened.ability.skill.ultimate;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.handler.SkillHandler;
import io.github.dracosomething.trawakened.ability.skill.unique.Starkill;
import io.github.dracosomething.trawakened.helper.skillHelper;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class herrscheroftime extends Skill {
    public static long time = 0L;

    public static Timer timer;

    public static ScheduledExecutorService service;

    public static float PERCENT = 20.0F;

    public static double millisF = 0.0D;

    public herrscheroftime() {
        super(SkillType.ULTIMATE);
    }

    public double getObtainingEpCost() {
        return 5000.0;
    }

    public double learningCost() {
        return 5000.0;
    }

    @Override
    public int modes() {
        return 3;
    }

    @Override
    public int nextMode(LivingEntity entity, TensuraSkillInstance instance, boolean reverse) {
        int var10000;
        if (reverse) {
            switch (instance.getMode()) {
                case 1 -> var10000 = 3;
                case 2 -> var10000 = 1;
                case 3 -> var10000 = 2;
                default -> var10000 = 0;
            }

            return var10000;
        } else {
            switch (instance.getMode()) {
                case 1 -> var10000 = 2;
                case 2 -> var10000 = 3;
                case 3 -> var10000 = 1;
                default -> var10000 = 0;
            }

            return var10000;
        }
    }

    @Override
    public List<MobEffect> getImmuneEffects(ManasSkillInstance instance, LivingEntity entity) {
        return List.of(effectRegistry.TIMESTOP.get());
    }

    public Component getModeName(int mode) {
        MutableComponent var10000;
        switch (mode) {
            case 1 ->
                    var10000 = Component.translatable("trawakened.skill.mode.herrscheroftimeskill.time_manip_world");
            case 2 ->
                    var10000 = Component.translatable("trawakened.skill.mode.herrscheroftimeskill.time_manip_self");
            case 3 ->
                    var10000 = Component.translatable("trawakened.skill.mode.herrscheroftimeskill.time_stop");
            default -> var10000 = Component.empty();
        }

        return var10000;
    }

    public double magiculeCost(LivingEntity entity, ManasSkillInstance instance) {
        double var10000;
        switch (instance.getMode()) {
            case 1:
                var10000 = 1000.0;
                break;
            case 2:
                var10000 = 100.0;
                break;
            case 3:
                var10000 = 5000.0;
                break;
            default:
                var10000 = 0.0;
                break;
        }

        return var10000;
    }

    public boolean canBeToggled(ManasSkillInstance instance, LivingEntity entity) {
        return true;
    }

    @Override
    public void onToggleOn(ManasSkillInstance instance, LivingEntity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.getAbilities().mayfly = true;
            player.getAbilities().flying = true;
            ((Player) entity).onUpdateAbilities();
        }
    }

    @Override
    public void onToggleOff(ManasSkillInstance instance, LivingEntity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.getAbilities().mayfly = false;
            player.getAbilities().flying = false;
            ((Player) entity).onUpdateAbilities();
        }
    }

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        switch (instance.getMode()) {
            case 1:
                if(!entity.isShiftKeyDown()) {
                    CompoundTag tag = instance.getOrCreateTag();
                    if (tag.getDouble("time") < 1.0) {
                        tag.putDouble("time", 20.0);
                    }
                    changeAll((float) tag.getDouble("time"));
                    update();
                    Objects.requireNonNull(
                            entity.getAttributes().getInstance(Attributes.MOVEMENT_SPEED))
                            .setBaseValue(tag.getDouble("time") < 20?
                                    entity.getAttributeBaseValue(Attributes.MOVEMENT_SPEED) + (1-(tag.getDouble("time")/100)):
                                    0.10000000149011612);
                    Objects.requireNonNull(
                                    entity.getAttributes().getInstance(Attributes.FLYING_SPEED))
                            .setBaseValue(tag.getDouble("time") < 20?
                                    entity.getAttributeBaseValue(Attributes.FLYING_SPEED) + (1-(tag.getDouble("time")/100)):
                                    0.02);
                    Objects.requireNonNull(
                                    entity.getAttributes().getInstance(ForgeMod.ENTITY_GRAVITY.get()))
                            .setBaseValue(tag.getDouble("time") < 20?
                                    entity.getAttributeBaseValue(ForgeMod.ENTITY_GRAVITY.get()) + (1-(tag.getDouble("time")/100)):
                                    ForgeMod.ENTITY_GRAVITY.get().getDefaultValue());
                    Objects.requireNonNull(
                                    entity.getAttributes().getInstance(ForgeMod.SWIM_SPEED.get()))
                            .setBaseValue(tag.getDouble("time") < 20?
                                    entity.getAttributeBaseValue(ForgeMod.SWIM_SPEED.get()) + (1-(tag.getDouble("time")/100)):
                                    ForgeMod.SWIM_SPEED.get().getDefaultValue());
                }
                break;
            case 2:
                if(!entity.isShiftKeyDown()) {
                    CompoundTag tag = instance.getOrCreateTag();
                    if (!(tag.getDouble("time_self") == 20)) {
                        Objects.requireNonNull(
                                        entity.getAttributes().getInstance(Attributes.MOVEMENT_SPEED))
                                .setBaseValue(tag.getDouble("time_self") > 20 ?
                                        entity.getAttributeBaseValue(Attributes.MOVEMENT_SPEED) + (1 - (tag.getDouble("time_self") / 100)) :
                                        entity.getAttributeBaseValue(Attributes.MOVEMENT_SPEED) - (1 - (tag.getDouble("time_self") / 100))
                                );
                        Objects.requireNonNull(
                                        entity.getAttributes().getInstance(Attributes.FLYING_SPEED))
                                .setBaseValue(tag.getDouble("time_self") > 20 ?
                                        entity.getAttributeBaseValue(Attributes.FLYING_SPEED) + (1 - (tag.getDouble("time_self") / 100)) :
                                        entity.getAttributeBaseValue(Attributes.FLYING_SPEED) - (1 - (tag.getDouble("time_self") / 100))
                                );
                        Objects.requireNonNull(
                                        entity.getAttributes().getInstance(ForgeMod.ENTITY_GRAVITY.get()))
                                .setBaseValue(tag.getDouble("time_self") > 20 ?
                                        entity.getAttributeBaseValue(ForgeMod.ENTITY_GRAVITY.get()) + (1 - (tag.getDouble("time_self") / 100)) :
                                        entity.getAttributeBaseValue(ForgeMod.ENTITY_GRAVITY.get()) - (1 - (tag.getDouble("time_self") / 100))
                                );
                        Objects.requireNonNull(
                                        entity.getAttributes().getInstance(ForgeMod.SWIM_SPEED.get()))
                                .setBaseValue(tag.getDouble("time_self") > 20 ?
                                        entity.getAttributeBaseValue(ForgeMod.SWIM_SPEED.get()) + (1 - (tag.getDouble("time_self") / 100)) :
                                        entity.getAttributeBaseValue(ForgeMod.SWIM_SPEED.get()) - (1 - (tag.getDouble("time_self") / 100))
                                );
                    } else {
                        Objects.requireNonNull(
                                        entity.getAttributes().getInstance(Attributes.MOVEMENT_SPEED))
                                .setBaseValue(0.10000000149011612);
                        Objects.requireNonNull(
                                        entity.getAttributes().getInstance(Attributes.FLYING_SPEED))
                                .setBaseValue(0.02);
                        Objects.requireNonNull(
                                        entity.getAttributes().getInstance(ForgeMod.ENTITY_GRAVITY.get()))
                                .setBaseValue(ForgeMod.ENTITY_GRAVITY.get().getDefaultValue());
                        Objects.requireNonNull(
                                        entity.getAttributes().getInstance(ForgeMod.SWIM_SPEED.get()))
                                .setBaseValue(ForgeMod.SWIM_SPEED.get().getDefaultValue());
                    }
                }
                break;
            case 3:
                entity.addEffect(new MobEffectInstance(effectRegistry.TIMESTOP_CORE.get(), 9998, 1));
                List<Entity> list = skillHelper.DrawCircle(entity, 160, false);
                for (Entity entity1 : list){
                    if(entity1 instanceof LivingEntity living && living != entity) {
                        living.addEffect(new MobEffectInstance(effectRegistry.TIMESTOP.get(), 9998, 1));
                    }
                }
        }
    }

    public void onScroll(ManasSkillInstance instance, LivingEntity entity, double delta) {
        if (instance.getMode() == 1 && entity.isShiftKeyDown()) {
            CompoundTag tag = instance.getOrCreateTag();
            double newRange = tag.getDouble("time") + delta;
            int maxRange = instance.isMastered(entity) ? 60 : 40;
            if (newRange > (double)maxRange) {
                newRange = 1.0;
            } else if (newRange < 1.0) {
                newRange = (double)maxRange;
            }

            if (tag.getDouble("time") != newRange) {
                tag.putDouble("time", newRange);
                if (entity instanceof Player) {
                    Player player = (Player)entity;
                    player.displayClientMessage(Component.translatable("trawakened.skill.time", new Object[]{newRange}).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)), true);
                }
                instance.markDirty();
            }
        } else if(instance.getMode() == 2 && entity.isShiftKeyDown()){
            CompoundTag tag = instance.getOrCreateTag();
            double newRange = tag.getDouble("time_self") + delta;
            int maxRange = instance.isMastered(entity) ? 60 : 40;
            if (newRange > (double)maxRange) {
                newRange = 1.0;
            } else if (newRange < 1.0) {
                newRange = (double)maxRange;
            }

            if (tag.getDouble("time_self") != newRange) {
                tag.putDouble("time_self", newRange);
                if (entity instanceof Player) {
                    Player player = (Player)entity;
                    player.displayClientMessage(Component.translatable("trawakened.skill.time_self", new Object[]{newRange}).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)), true);
                }
                instance.markDirty();
            }
        }
    }

    public static void create() {
        if (service == null)
            service = Executors.newSingleThreadScheduledExecutor();
        if (timer == null)
            timer = new Timer();
        try {
            timer.schedule(new TimerTask() {
                public void run() {
                    herrscheroftime.service.scheduleAtFixedRate(herrscheroftime::update, 1L, 1L, TimeUnit.MILLISECONDS);
                }
            }, 1L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void update() {
        float p = PERCENT / 20.0F;
        millisF = p + millisF;
        time = (long)millisF;
    }

    public static void changeAll(float percent) {
        PERCENT = percent;
        System.out.println("PER " + PERCENT);
        System.out.println("add " + percent / 20.0F);
        System.out.println("TickLengthChanged ->" + PERCENT);
    }

}
