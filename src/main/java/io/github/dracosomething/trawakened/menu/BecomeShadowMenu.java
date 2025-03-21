package io.github.dracosomething.trawakened.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class BecomeShadowMenu extends AbstractContainerMenu {
    private UUID targetUUID;

    public BecomeShadowMenu(int containerID, Inventory inventory, FriendlyByteBuf buf) {
        this(pContainerId, inventory, inventory.player);
        this.targetUUID = buf.readUUID();
    }

    public BecomeShadowMenu(int containerId, Inventory inventory, Player player) {
        super();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }
}
