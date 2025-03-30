package io.github.dracosomething.trawakened.menu;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.dracosomething.trawakened.network.TRAwakenedNetwork;
import io.github.dracosomething.trawakened.network.play2client.ArisePlayerPacket;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.util.UUID;

public class BecomeShadowScreen extends Screen {
    Button yes;
    net.minecraft.client.gui.components.Button no;
    Component mainText;
    private final UUID targetId;

    private static final Component TITLE = Component.literal("Do you want to become a shadow?");


    public BecomeShadowScreen(UUID targetId) {
        super(TITLE);
        this.width = 176;
        this.height = 166;
        this.targetId = targetId;
        this.mainText = TITLE;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        drawString(pPoseStack, font, mainText.getString(), width/2-89, height/2-75, 16777215);
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        return super.keyPressed(key, b, c);
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.yes = this.addRenderableWidget(new net.minecraft.client.gui.components.Button(this.width / 2 + 75, this.height / 2, 120, 20, Component.literal("yes"), (button) -> {
            TRAwakenedNetwork.toServer(new ArisePlayerPacket(targetId, true));
            this.minecraft.player.closeContainer();
        }));
        this.no = this.addRenderableWidget(new net.minecraft.client.gui.components.Button(this.width / 2 - 197, this.height / 2, 120, 20, Component.literal("no"), (button) -> {
            TRAwakenedNetwork.toServer(new ArisePlayerPacket(targetId, false));
            this.minecraft.player.closeContainer();
        }));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
