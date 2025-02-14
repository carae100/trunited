package io.github.dracosomething.trawakened.entity.client.renderer.CustomPlayerRenderer;

import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

import java.util.Optional;

/**
 * Based on @TeamLapen's Werewolf Rendering codes: <a href="https://github.com/TeamLapen/Werewolves/blob/1.20/src/main/java/de/teamlapen/werewolves/client/render/WerewolfPlayerRenderer.java">...</a>
 */
public abstract class OverdrivenPlayerRenderer<T extends AbstractClientPlayer, E extends PlayerModel<T>> extends PlayerRenderer {

    public static Optional<String> getLichRenderer(AbstractClientPlayer player) {
        return Optional.of(trawakened.MODID +":lich");
    }

    public OverdrivenPlayerRenderer(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> model) {
        super(context, true);
        this.model = model;
    }
}
