package io.github.dracosomething.trawakened.menu;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import io.github.dracosomething.trawakened.ability.skill.ultimate.ShadowMonarch;
import io.github.dracosomething.trawakened.network.TRAwakenedNetwork;
import io.github.dracosomething.trawakened.network.play2client.NamePacket;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.UUID;

public class WordScreen extends Screen {
    EditBox input;
    Button check;
    private final UUID targetId;
    private final ManasSkillInstance instance;

    private static final Component TITLE = Component.literal("Word");

    public WordScreen(UUID targetId, ManasSkillInstance instance) {
        super(TITLE);
        this.width = 176;
        this.height = 166;
        this.targetId = targetId;
        this.instance = instance;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        drawCenteredString(pPoseStack, font, "Please designate a command word", width/2+5, height/2-30, 13158600);
        drawCenteredString(pPoseStack, font, "for                            ", width/2, height/2-10, 13158600);
        drawCenteredString(pPoseStack, font, "    [Skill: Shadow Extraction]", width/2+10, height/2-10, 12500735);
        drawCenteredString(pPoseStack, font, "                              .", width/2+22, height/2-10, 13158600);
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
        this.input = this.addRenderableWidget(new EditBox(this.font, this.width / 2 - 50, this.height / 2 + 20, 120, 20, Component.literal("name: required")));
        this.check = this.addRenderableWidget(new Button(this.width / 2 - 50, this.height / 2 + 50, 120, 20, Component.literal("confirm"), (button) -> {
            if (!this.input.getValue().isEmpty()) {
                    if (instance.getSkill() instanceof ShadowMonarch skill) {
                        System.out.println(input.getValue());
                        CompoundTag tag = new CompoundTag();
                        tag.putString("commandWord", input.getValue());
                        instance.getOrCreateTag().put("command", tag);
                        skill.setCommandWord(tag);
                        System.out.println(skill.getCommandWord());
                        System.out.println(instance.getOrCreateTag());
                    }
                this.minecraft.player.closeContainer();
            }
        }));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
