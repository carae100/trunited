package io.github.dracosomething.trawakened.event;

import io.github.dracosomething.trawakened.library.FearTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;

import java.util.List;

public class FearPenaltyEvent extends FearEvent {

    public FearPenaltyEvent(FearTypes fearTypes, LivingEntity entity) {
        super(fearTypes, entity);
    }

    public static class FearPenaltyEffectEvent extends FearEvent {
        protected List<MobEffect> effects;

        public FearPenaltyEffectEvent(FearTypes fearTypes, LivingEntity entity, List<MobEffect> effects) {
            super(fearTypes, entity);
            this.effects = effects;
        }

        public List<MobEffect> getEffects() {
            return effects;
        }

        @Cancelable
        public static class Pre extends FearPenaltyEffectEvent {

            public Pre(FearTypes fearTypes, LivingEntity entity, List<MobEffect> effects) {
                super(fearTypes, entity, effects);
            }

            public void setEffects(List<MobEffect> effects) {
                this.effects = effects;
            }
        }

        public static class Post extends FearPenaltyEffectEvent {

            public Post(FearTypes fearTypes, LivingEntity entity, List<MobEffect> effects) {
                super(fearTypes, entity, effects);
            }
        }
    }

    public static class FearPenaltySHPEvent extends FearEvent {
        protected int amount;

        public FearPenaltySHPEvent(FearTypes fearTypes, LivingEntity entity, int amount) {
            super(fearTypes, entity);
            this.amount = amount;
        }

        public int getAmount() {
            return amount;
        }

        @Cancelable
        public static class Pre extends FearPenaltySHPEvent {
            private double oldAmount;

            public Pre(FearTypes fearTypes, LivingEntity entity, int amount, double oldAmount) {
                super(fearTypes, entity, amount);
                this.oldAmount = oldAmount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public double getOldAmount() {
                return oldAmount;
            }
        }

        public static class Post extends FearPenaltySHPEvent {
            private double newAmount;

            public Post(FearTypes fearTypes, LivingEntity entity, int amount, double newAmount) {
                super(fearTypes, entity, amount);
                this.newAmount = newAmount;
            }

            public double getNewAmount() {
                return newAmount;
            }
        }
    }
}
