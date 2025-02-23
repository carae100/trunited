package io.github.dracosomething.trawakened.entity.client.model.CustomPlayerModel;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

public class PlayerModel<T extends LivingEntity> extends net.minecraft.client.model.PlayerModel<T> {
    public PlayerModel(ModelPart p_170821_) {
        super(p_170821_, false);
    }

    public static MeshDefinition createMesh(CubeDeformation p_170812_) {
        MeshDefinition meshdefinition = net.minecraft.client.model.PlayerModel.createMesh(p_170812_, false);
        return meshdefinition;
    }
}
