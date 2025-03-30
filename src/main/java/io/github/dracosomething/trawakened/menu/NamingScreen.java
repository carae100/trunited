package io.github.dracosomething.trawakened.menu;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.dracosomething.trawakened.network.TRAwakenedNetwork;
import io.github.dracosomething.trawakened.network.play2client.ArisePlayerPacket;
import io.github.dracosomething.trawakened.network.play2client.NamePacket;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.UUID;

public class NamingScreen extends Screen {
    EditBox input;
    Button check;
    private final UUID targetId;

    private static final Component TITLE = Component.literal("Name");

    public NamingScreen(UUID targetId) {
        super(TITLE);
        this.width = 176;
        this.height = 166;
        this.targetId = targetId;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        drawString(pPoseStack, font, "What should the target be named?", width/2-89, height/2-75, 16777215);
        drawString(pPoseStack, font, "Name is requiered", width/2-89, height/2-25, 16777215);
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
        this.input = this.addRenderableWidget(new EditBox(this.font, this.width / 2 - 50, this.height / 2 - 40, 120, 20, Component.literal("name: required")));
        this.check = this.addRenderableWidget(new Button(this.width / 2 - 50, this.height / 2, 120, 20, Component.literal("confirm"), (button) -> {
            if (!this.input.getValue().isEmpty()) {
                TRAwakenedNetwork.toServer(new NamePacket(targetId, this.input.getValue()));
                this.minecraft.player.closeContainer();
            }
        }));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
