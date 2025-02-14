package io.github.dracosomething.trawakened.entity.client.model.CustomPlayerModel;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class IntruderModel<T extends LivingEntity> extends PlayerModel<T> {
    public IntruderModel(ModelPart p_170821_) {
        super(p_170821_, false);
    }

    public static MeshDefinition createMesh(CubeDeformation p_170812_) {
        MeshDefinition meshdefinition = PlayerModel.createMesh(p_170812_, false);
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(0F, 0F,0F,0F,0F,0F), PartPose.offset(0,0,0));
        partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 0).addBox(0F, 0F,0F,0F,0F,0F), PartPose.offset(0,0,0));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(0F, 0F,0F,0F,0F,0F), PartPose.offset(0,0,0));
        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 0).addBox(0F, 0F,0F,0F,0F,0F), PartPose.offset(0,0,0));
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 0).addBox(0F, 0F,0F,0F,0F,0F), PartPose.offset(0,0,0));
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 0).addBox(0F, 0F,0F,0F,0F,0F), PartPose.offset(0,0,0));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 0).addBox(0F, 0F,0F,0F,0F,0F), PartPose.offset(0,0,0));
        partdefinition.addOrReplaceChild("ear", CubeListBuilder.create().texOffs(0, 0).addBox(0F, 0F,0F,0F,0F,0F), PartPose.offset(0,0,0));
        partdefinition.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(0, 0).addBox(0F, 0F,0F,0F,0F,0F), PartPose.offset(0,0,0));
        partdefinition.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(0, 0).addBox(0F, 0F,0F,0F,0F,0F), PartPose.offset(0,0,0));
        partdefinition.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(0, 0).addBox(0F, 0F,0F,0F,0F,0F), PartPose.offset(0,0,0));
        partdefinition.addOrReplaceChild("right_pants", CubeListBuilder.create().texOffs(0, 0).addBox(0F, 0F,0F,0F,0F,0F), PartPose.offset(0,0,0));
        partdefinition.addOrReplaceChild("left_pants", CubeListBuilder.create().texOffs(0, 0).addBox(0F, 0F,0F,0F,0F,0F), PartPose.offset(0,0,0));
        partdefinition.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(0, 0).addBox(0F, 0F,0F,0F,0F,0F), PartPose.offset(0,0,0));
        return meshdefinition;
    }

    public Iterable<ModelPart> modifiedBodyParts() {
        return ImmutableList.of(this.head, this.body, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg);
    }

    public void translateToHead(PoseStack stack) {
        this.body.translateAndRotate(stack);
        this.head.translateAndRotate(stack);
    }
}
