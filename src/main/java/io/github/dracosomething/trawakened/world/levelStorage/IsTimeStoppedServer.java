package io.github.dracosomething.trawakened.world.levelStorage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.UUID;

public class IsTimeStoppedServer extends SavedData {
    private boolean timeStopped;

    public static IsTimeStoppedServer get(final ServerLevel overworld) {
        return overworld.getDataStorage().computeIfAbsent(IsTimeStoppedServer::new, IsTimeStoppedServer::new, "trawakened:time_stopped");
    }

    public IsTimeStoppedServer() {
        timeStopped = false;
    }

    private IsTimeStoppedServer(CompoundTag tag) {
        this();
        this.timeStopped = tag.getBoolean("timeStopped");
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putBoolean("timeStopped", timeStopped);
        return compoundTag;
    }

    public boolean isTimeStopped() {
        return timeStopped;
    }

    public void setTimeStopped(boolean timeStopped) {
        this.timeStopped = timeStopped;
    }
}
