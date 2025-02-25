package io.github.dracosomething.trawakened.ability.skill.ultimate;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.capability.skill.TensuraSkillCapability;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.item.templates.custom.SimpleSpearItem;
import com.github.manasmods.tensura.item.templates.custom.SmithingSchematicItem;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.race.TensuraRaces;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.registry.enchantRegistry;
import io.github.dracosomething.trawakened.registry.raceregistry;
import io.github.dracosomething.trawakened.registry.skillregistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.github.manasmods.tensura.util.damage.TensuraDamageSources.SPACE_ATTACK;

public class herrscheroftheworld extends Skill {
    @Override
    public @Nullable ResourceLocation getSkillIcon() {
        return new ResourceLocation("trawakened", "textures/skill/ultimate/herrscher_of_the_world.png");
    }

    public herrscheroftheworld() {
        super(SkillType.ULTIMATE);
    }

    @Override
    public int modes() {
        return 3;
    }

    public Component getModeName(int mode) {
        MutableComponent var10000;
        switch (mode) {
            case 1 ->
                    var10000 = Component.translatable("trawakened.skill.mode.starkill.infinity");
            case 2 ->
                    var10000 = Component.translatable("trawakened.skill.mode.herrscheroftheworldskill.spiritual_block");
            case 3 ->
                    var10000 = Component.translatable("trawakened.skill.mode.herrscheroftheworldskill.arcane_knowledge");
            default -> var10000 = Component.empty();
        }

        return var10000;
    }

    @Override
    public int nextMode(LivingEntity entity, TensuraSkillInstance instance, boolean reverse) {
        if (reverse) {
            return instance.getMode() == 1 ? 3 : instance.getMode() - 1;
        } else {
            return instance.getMode() == 3 ? 1 : instance.getMode() + 1;
        }
    }

    @Override
    public double magiculeCost(LivingEntity entity, ManasSkillInstance instance) {
        int mode = entity.getPersistentData().getInt("mode");
        double var10000 = 0;
        switch (instance.getMode()) {
            case 1 -> {
                if(entity.isShiftKeyDown()){
                    return 0;
                }
                else {
                    switch (mode) {
                        case 1 -> var10000 = 0;
                        case 2 -> var10000 = 250;
                        case 3 -> var10000 = 100;
                        default -> var10000 = 0;
                    }
                }
            }
            case 2 -> var10000 = 500;
            case 3 -> var10000 = 1000000;
        }

        return var10000;
    }

    private boolean on;

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        int mode = entity.getPersistentData().getInt("mode");
        switch (instance.getMode()){
            case 1:
                if(!SkillHelper.outOfMagicule(entity, instance)){
                    if(entity.isShiftKeyDown()){
                        if(entity instanceof Player player) {
                            switch (mode) {
                                case 1:
                                    player.displayClientMessage(Component.translatable("trawakened.skill.mode.starkill.infinity.overwhelmed").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                                    player.getPersistentData().putInt("mode", 2);                                    break;
                                case 2:
                                    player.displayClientMessage(Component.translatable("trawakened.skill.mode.starkill.infinity.spatial_attack").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                                    player.getPersistentData().putInt("mode", 3);                                    break;
                                case 3:
                                    player.displayClientMessage(Component.translatable("trawakened.skill.mode.starkill.infinity.analyze").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                                    player.getPersistentData().putInt("mode", 1);
                                    break;
                            }
                            if(instance.onCoolDown()) {
                                instance.setCoolDown(instance.getCoolDown());
                            }
                        }
                    } else {
                        switch (mode){
                            case 1:
                                this.analize(instance, entity, on);
                                on = !on;
                                break;
                            case 2:
                                if (!SkillHelper.outOfMagicule(entity, instance)) {
                                    AABB aabb = new AABB((double) (entity.getX() - 15), (double) (entity.getY() - 15), (double) (entity.getZ() - 15), (double) (entity.getX() + 15), (double) (entity.getY() + 15), (double) (entity.getZ() + 15));
                                    List<Entity> entities = entity.level.getEntities((Entity) null, aabb, Entity::isAlive);
                                    List<Entity> ret = new ArrayList();
                                    new Vec3((double) entity.getX(), (double) entity.getY(), (double) entity.getZ());
                                    Iterator var16 = entities.iterator();

                                    while (var16.hasNext()) {
                                        Entity entity2 = (Entity) var16.next();

                                        int radius = 15;

                                        double x = entity2.getX();
                                        double y = entity2.getY();
                                        double z = entity2.getZ();
                                        double cmp = (double) (radius * radius) - ((double) entity2.getX() - x) * ((double) entity2.getX() - x) - ((double) entity2.getY() - y) * ((double) entity2.getY() - y) - ((double) entity2.getZ() - z) * ((double) entity2.getZ() - z);
                                        if (cmp > 0.0) {
                                            ret.add(entity2);
                                        }
                                    }

                                    for (Entity entity2 : ret) {
                                        if (entity2 instanceof LivingEntity) {
                                            if (entity2 != entity) {
                                                SkillHelper.checkThenAddEffectSource((LivingEntity) entity2, entity, (MobEffect) effectRegistry.OVERWHELMED.get(), entity.level.random.nextInt(600, 6000), 1);
                                            }
                                            if(entity2 instanceof Player player){
                                                player.displayClientMessage(Component.translatable("hidden_Knowledge").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)), false);
                                            }
                                        }
                                    }

                                    TensuraParticleHelper.addServerParticlesAroundSelf(entity, ParticleTypes.FLASH, 0.1);
                                    entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.WITHER_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                                    instance.setCoolDown(40);
                                    instance.addMasteryPoint(entity);
                                }
                                break;
                            case 3:
                                if(!SkillHelper.outOfMagicule(entity, instance)) {
                                    LivingEntity target2 = SkillHelper.getTargetingEntity(entity, 15.0, false);
                                    target2.hurt(TensuraDamageSources.elementalAttack(SPACE_ATTACK, entity, false).bypassArmor().bypassEnchantments().bypassMagic(), 6);
                                    TensuraParticleHelper.addServerParticlesAroundSelf(target2, ParticleTypes.SWEEP_ATTACK, 0.5);
                                    target2.addEffect(new MobEffectInstance(effectRegistry.HEALPOISON.get(), 1000, 1, false, false, false));
                                    instance.setCoolDown(10);
                                    instance.addMasteryPoint(entity);
                                }
                                break;
                        }
                    }
                }
                break;
            case 2:
                if(!SkillHelper.outOfMagicule(entity, instance) && !entity.hasEffect(effectRegistry.SPIRITUAL_BLOCK.get())){
                    entity.addEffect(new MobEffectInstance(effectRegistry.SPIRITUAL_BLOCK.get(), instance.isMastered(entity)?3500:2000, instance.isMastered(entity)?55:50, false, false, false));
                    entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                    TensuraParticleHelper.addServerParticlesAroundSelf(entity, ParticleTypes.CRIT, 0.7);
                    instance.addMasteryPoint(entity);
                    instance.setCoolDown(50);
                }
                break;
            case 3:
                if(!SkillHelper.outOfMagicule(entity, instance)){
                    List<Item> itemList = Registry.ITEM.stream().filter((item1) ->
                            (item1.asItem() instanceof ArmorItem ||
                            item1.asItem() instanceof ShieldItem ||
                            item1.asItem() instanceof TridentItem ||
                            item1.asItem() instanceof ProjectileWeaponItem ||
                            item1.asItem() instanceof DiggerItem ||
                            item1.asItem() instanceof ElytraItem ||
                            item1.asItem() instanceof ShearsItem ||
                            item1.asItem() instanceof SwordItem)
                    ).toList();
                    if (entity instanceof Player player) {
                        Random random = new Random();
                        ItemStack stack = itemList.get(random.nextInt(0, itemList.size())).getDefaultInstance();
                        player.getInventory().add(stack);
                        stack.enchant(enchantRegistry.DECAY.get(), instance.isMastered(entity)?120:90);
                    }
                    entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                    entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, SoundSource.PLAYERS, 1.0F, 1.0F);
                    entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS, SoundSource.PLAYERS, 1.0F, 1.0F);
                    TensuraParticleHelper.addServerParticlesAroundSelf(entity, ParticleTypes.ENCHANT, 1);
                    instance.addMasteryPoint(entity);
                    instance.setCoolDown(120);
                }
                break;
        }
    }

    @Override
    public boolean canIgnoreCoolDown(ManasSkillInstance instance, LivingEntity entity) {
        int mode = entity.getPersistentData().getInt("mode");
        switch (instance.getMode()) {
            case 3, 2 -> {
                return false;
            }
            case 1 -> {
                if(entity.isShiftKeyDown()){
                    return true;
                }else {
                    switch (mode) {
                        case 2, 3 -> {
                            return false;
                        }
                        case 1 -> {
                            return true;
                        }
                        default -> {
                            return false;
                        }
                    }
                }
            }
            default -> {
                return true;
            }
        }
    }

    private void analize(ManasSkillInstance instance, LivingEntity entity, boolean on) {
        if (entity instanceof Player) {
            if (on) {
                Player player = (Player) entity;
                TensuraSkillCapability.getFrom(player).ifPresent((cap) -> {
                    int level;
                    level = 10;
                    if (cap.getAnalysisLevel() != level) {
                        cap.setAnalysisLevel(level);
                        cap.setAnalysisDistance(instance.isMastered(entity) ? 35 : 15);
                        entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                    } else {
                        cap.setAnalysisLevel(0);
                        entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }

                    TensuraSkillCapability.sync(player);
                });
            }
        }
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
    public boolean canTick(ManasSkillInstance instance, LivingEntity entity) {
        return true;
    }

    private void Unworthy(LivingEntity living){
        if (living instanceof Player) {
            Player player = (Player) living;
            if(!player.isCreative()) {
                if (!Objects
                        .equals(
                                TensuraPlayerCapability.getRace(player),
                                (Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get())
                                        .getValue(raceregistry.HERRSCHER_OF_THE_WORLD))) {
                    SkillAPI.getSkillsFrom(player).forgetSkill(skillregistry.HERRSCHEROFTHEWORLD.get());
                    player.displayClientMessage(Component.translatable("unworthy").setStyle(Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE)), false);
                }
            }
        }
    }

    private void Cure(LivingEntity living){
        List<MobEffectInstance> list = living.getActiveEffects().stream().toList();
        for(MobEffectInstance effect : list){
            if(effect.getEffect().getCategory() == MobEffectCategory.HARMFUL){
                living.removeEffect(effect.getEffect());
            }
        }
    }

    @Override
    public void onTick(ManasSkillInstance instance, LivingEntity living) {
        Cure(living);
        Unworthy(living);
    }

    @Override
    public List<MobEffect> getImmuneEffects(ManasSkillInstance instance, LivingEntity entity) {
        return List.of(effectRegistry.OVERWHELMED.get(), effectRegistry.BRAINDAMAGE.get(), effectRegistry.TIMESTOP.get());
    }

    @Override
    public void onDeath(ManasSkillInstance instance, LivingDeathEvent event) {
        event.getEntity().getPersistentData().putInt("mode", 1);
    }

    @Override
    public void onRespawn(ManasSkillInstance instance, PlayerEvent.PlayerRespawnEvent event) {
        event.getEntity().getPersistentData().putInt("mode", 1);
    }

    @Override
    public void onLearnSkill(ManasSkillInstance instance, LivingEntity living, UnlockSkillEvent event) {
        living.getPersistentData().putInt("mode", 1);
        on = false;
    }
}
