package io.github.dracosomething.trawakened.menu;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.dracosomething.trawakened.network.TRAwakenedNetwork;
import io.github.dracosomething.trawakened.network.play2client.ArisePlayerPacket;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.UUID;

public class BecomeShadowScreen extends Screen {
    Button yes;
    net.minecraft.client.gui.components.Button no;
    private final UUID targetId;

    private static final Component TITLE = Component.literal("Do you want to become a shadow");


    public BecomeShadowScreen(UUID targetId) {
        super(TITLE);
        this.width = 176;
        this.height = 166;
        this.targetId = targetId;
    }

    @Override
    public void render(PoseStack p_96562_, int p_96563_, int p_96564_, float p_96565_) {
        this.renderBackground(p_96562_);
        super.render(p_96562_, p_96563_, p_96564_, p_96565_);
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            this.minecraft.player.closeContainer();
            return true;
        }
        return super.keyPressed(key, b, c);
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.yes = this.addRenderableWidget(new net.minecraft.client.gui.components.Button(this.width / 2 + 50, this.height / 2, 120, 20, Component.literal("yes"), (button) -> {
            TRAwakenedNetwork.toServer(new ArisePlayerPacket(targetId, true));
            this.minecraft.player.closeContainer();
        }));
        this.no = this.addRenderableWidget(new net.minecraft.client.gui.components.Button(this.width / 2 - 50, this.height / 2, 120, 20, Component.literal("yes"), (button) -> {
            TRAwakenedNetwork.toServer(new ArisePlayerPacket(targetId, false));
            this.minecraft.player.closeContainer();
        }));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
