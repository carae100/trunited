package io.github.dracosomething.trawakened.menu;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.dracosomething.trawakened.ability.skill.ultimate.ShadowMonarch;
import io.github.dracosomething.trawakened.network.TRAwakenedNetwork;
import io.github.dracosomething.trawakened.network.play2server.SummonShadowPacket;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;

import java.util.*;
import java.util.stream.Collectors;

public class ShadowSummonScreen extends Screen {
    private final ShadowMonarch skill;
    private final ManasSkillInstance instance;
    private final Map<String, List<ShadowData>> groupedShadows;
    private final List<String> selectedShadows;
    private final Map<String, Integer> summonQuantities;
    private final Map<String, Boolean> sortAscending;
    
    private Button summonButton;
    private int scrollOffset = 0;
    private final int maxVisiblePerColumn = 12;
    private final int columns = 2;
    private final int maxVisibleTotal = maxVisiblePerColumn * columns;

    public ShadowSummonScreen(ShadowMonarch skill, ManasSkillInstance instance) {
        super(Component.translatable("trawakened.screen.shadow_summon.title"));
        this.skill = skill;
        this.instance = instance;
        this.selectedShadows = new ArrayList<>();
        this.summonQuantities = new HashMap<>();
        this.sortAscending = new HashMap<>();
        this.groupedShadows = groupShadowsByName();
    }

    private Map<String, List<ShadowData>> groupShadowsByName() {
        Map<String, List<ShadowData>> grouped = new HashMap<>();
        CompoundTag storage = skill.getShadowStorage();
        
        for (String key : storage.getAllKeys()) {
            System.out.println(key);
            CompoundTag shadowData = storage.getCompound(key);
            String entityType = shadowData.getString("entityType");
            String name = shadowData.getString("name");
            double ep = shadowData.getDouble("EP");
            
            if (name.isEmpty()) {
                EntityType<?> type = EntityType.byString(entityType).orElse(null);
                if (type != null) {
                    name = type.getDescription().getString();
                }
            }
            
            ShadowData data = new ShadowData(key, name, ep, entityType);
            grouped.computeIfAbsent(name, k -> new ArrayList<>()).add(data);
        }
        
        grouped.values().forEach(list -> list.sort(Comparator.comparingDouble(s -> s.ep)));
        
        return grouped;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int startY = 60;
        int buttonWidth = 180;
        int buttonHeight = 18;
        int spacing = 20;
        int columnSpacing = 200;
        
        this.clearWidgets();
        selectedShadows.clear();
        
        int index = 0;
        for (Map.Entry<String, List<ShadowData>> entry : groupedShadows.entrySet()) {
            if (index < scrollOffset) {
                index++;
                continue;
            }
            if (index - scrollOffset >= maxVisibleTotal) break;
            
            String shadowName = entry.getKey();
            List<ShadowData> shadows = entry.getValue();
            
            int displayIndex = index - scrollOffset;
            int column = displayIndex / maxVisiblePerColumn;
            int row = displayIndex % maxVisiblePerColumn;
            
            int x = centerX - columnSpacing/2 + (column * columnSpacing) - buttonWidth/2;
            int y = startY + row * spacing;
            
            if (shadows.size() == 1) {
                ShadowData shadow = shadows.get(0);
                Button shadowButton = new Button(
                    x, y, buttonWidth, buttonHeight,
                    Component.literal(shadowName + " (EP: " + (int)shadow.ep + ")"),
                    button -> toggleShadowSelection(shadow.key, shadowName)
                );
                
                this.addRenderableWidget(shadowButton);
            } else {
                Button shadowButton = new Button(
                    x, y, buttonWidth - 50, buttonHeight,
                    Component.literal(shadowName + " (" + shadows.size() + "x)"),
                    button -> toggleGroupSelection(shadowName)
                );
                
                this.addRenderableWidget(shadowButton);
                
                EditBox quantityBox = new EditBox(this.font, x + buttonWidth - 45, y, 20, buttonHeight, Component.empty());
                quantityBox.setMaxLength(3);
                quantityBox.setValue(String.valueOf(summonQuantities.getOrDefault(shadowName, 1)));
                quantityBox.setResponder(text -> {
                    try {
                        int quantity = Math.max(1, Math.min(Integer.parseInt(text), shadows.size()));
                        summonQuantities.put(shadowName, quantity);
                    } catch (NumberFormatException ignored) {
                        summonQuantities.put(shadowName, 1);
                    }
                });
                this.addRenderableWidget(quantityBox);
                
                Button sortButton = new Button(
                    x + buttonWidth - 20, y, 15, buttonHeight,
                    Component.literal(sortAscending.getOrDefault(shadowName, true) ? "↑" : "↓"),
                    button -> {
                        boolean ascending = !sortAscending.getOrDefault(shadowName, true);
                        sortAscending.put(shadowName, ascending);
                        button.setMessage(Component.literal(ascending ? "↑" : "↓"));
                    }
                );
                
                this.addRenderableWidget(sortButton);
            }
            index++;
        }
        
        if (scrollOffset > 0) {
            this.addRenderableWidget(new Button(
                centerX - 80, startY + maxVisiblePerColumn * spacing + 5, 50, 18,
                Component.literal("◀ Prev"),
                button -> {
                    scrollOffset = Math.max(0, scrollOffset - maxVisibleTotal);
                    init();
                }
            ));
        }
        
        if (scrollOffset + maxVisibleTotal < groupedShadows.size()) {
            this.addRenderableWidget(new Button(
                centerX + 30, startY + maxVisiblePerColumn * spacing + 5, 50, 18,
                Component.literal("Next ▶"),
                button -> {
                    scrollOffset = Math.min(groupedShadows.size() - maxVisibleTotal, scrollOffset + maxVisibleTotal);
                    init();
                }
            ));
        }
        
        summonButton = new Button(
            centerX - 50, startY + maxVisiblePerColumn * spacing + 30, 100, 22,
            Component.translatable("trawakened.screen.shadow_summon.summon"),
            button -> summonSelectedShadows()
        );
        
        summonButton.active = !selectedShadows.isEmpty();
        this.addRenderableWidget(summonButton);
        
        this.addRenderableWidget(new Button(
            centerX - 40, startY + maxVisiblePerColumn * spacing + 55, 80, 18,
            Component.translatable("gui.cancel"),
            button -> this.onClose()
        ));
        
        updateSummonButton();
    }

    private void toggleShadowSelection(String shadowKey, String shadowName) {
        if (selectedShadows.contains(shadowKey)) {
            selectedShadows.remove(shadowKey);
        } else {
            selectedShadows.add(shadowKey);
        }
        updateSummonButton();
    }

    private void toggleGroupSelection(String shadowName) {
        List<ShadowData> shadows = groupedShadows.get(shadowName);
        if (shadows == null) return;
        
        boolean hasSelected = shadows.stream().anyMatch(s -> selectedShadows.contains(s.key));
        
        selectedShadows.removeIf(key -> shadows.stream().anyMatch(s -> s.key.equals(key)));
        
        if (!hasSelected) {
            int quantity = summonQuantities.getOrDefault(shadowName, 1);
            boolean ascending = sortAscending.getOrDefault(shadowName, true);
            
            List<ShadowData> sortedShadows = new ArrayList<>(shadows);
            if (ascending) {
                sortedShadows.sort(Comparator.comparingDouble(s -> s.ep));
            } else {
                sortedShadows.sort((s1, s2) -> Double.compare(s2.ep, s1.ep));
            }
            
            for (int i = 0; i < Math.min(quantity, sortedShadows.size()); i++) {
                selectedShadows.add(sortedShadows.get(i).key);
            }
        }
        
        updateSummonButton();
    }

    private void updateSummonButton() {
        if (summonButton != null) {
            summonButton.active = !selectedShadows.isEmpty();
            if (!selectedShadows.isEmpty()) {
                summonButton.setMessage(Component.literal("Summon (" + selectedShadows.size() + ")"));
            } else {
                summonButton.setMessage(Component.translatable("trawakened.screen.shadow_summon.summon"));
            }
        }
    }

    private void summonSelectedShadows() {
        if (selectedShadows.isEmpty()) return;
        
        TRAwakenedNetwork.INSTANCE.sendToServer(new SummonShadowPacket(selectedShadows));
        this.onClose();
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        
        super.render(poseStack, mouseX, mouseY, partialTick);
        
        drawCenteredString(poseStack, this.font, this.title, this.width / 2, 30, 0xFFFFFF);
        
        int totalShadows = groupedShadows.size();
        int currentPage = (scrollOffset / maxVisibleTotal) + 1;
        int totalPages = (int) Math.ceil((double) totalShadows / maxVisibleTotal);
        
        if (totalPages > 1) {
            drawCenteredString(poseStack, this.font, 
                Component.literal("Page " + currentPage + "/" + totalPages + " (" + totalShadows + " types)"),
                this.width / 2, 45, 0xAAAAAA);
        } else {
            drawCenteredString(poseStack, this.font, 
                Component.literal(totalShadows + " shadow types"),
                this.width / 2, 45, 0xAAAAAA);
        }
        
        this.renderables.forEach(renderable -> {
            if (renderable instanceof Button button) {
                String buttonText = button.getMessage().getString();
                
                if (buttonText.contains("Summon") || buttonText.contains("Cancel") || buttonText.equals("▲") || buttonText.equals("▼") || buttonText.equals("↑") || buttonText.equals("↓")) {
                    return;
                }
                
                boolean isSelected = false;
                
                // Check if this button represents a selected shadow/group
                for (Map.Entry<String, List<ShadowData>> entry : groupedShadows.entrySet()) {
                    String shadowName = entry.getKey();
                    List<ShadowData> shadows = entry.getValue();
                    
                    if (buttonText.contains(shadowName)) {
                        // Check if any shadow from this group is selected
                        isSelected = shadows.stream().anyMatch(s -> selectedShadows.contains(s.key));
                        break;
                    }
                }
                
                if (isSelected) {
                    // Draw green highlight border
                    fill(poseStack, button.x - 2, button.y - 2, 
                         button.x + button.getWidth() + 2, button.y + button.getHeight() + 2, 
                         0x6600FF00); // Semi-transparent green border
                }
            }
        });
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static class ShadowData {
        final String key;
        final String name;
        final double ep;
        final String entityType;

        ShadowData(String key, String name, double ep, String entityType) {
            this.key = key;
            this.name = name;
            this.ep = ep;
            this.entityType = entityType;
        }
    }
}
