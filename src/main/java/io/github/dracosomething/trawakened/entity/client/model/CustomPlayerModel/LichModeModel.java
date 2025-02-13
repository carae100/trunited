package io.github.dracosomething.trawakened.entity.client.model.CustomPlayerModel;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

public class LichModeModel<T extends LivingEntity> extends PlayerModel<T> {
    private final ModelPart root;
    private final ModelPart leftWingBase;
    private final ModelPart leftWingTip;
    private final ModelPart rightWingBase;
    private final ModelPart rightWingTip;
    private final ModelPart tailBase;
    private final ModelPart tailTip;
    private final ModelPart head;
    private final ModelPart body;

    public LichModeModel(ModelPart p_170821_) {
        super(p_170821_, false);
        this.root = p_170821_;
        ModelPart main = p_170821_.getChild("body");
        this.body = p_170821_.getChild("body");
        this.tailBase = main.getChild("tail_base");
        this.tailTip = this.tailBase.getChild("tail_tip");
        this.leftWingBase = main.getChild("left_wing_base");
        this.leftWingTip = this.leftWingBase.getChild("left_wing_tip");
        this.rightWingBase = main.getChild("right_wing_base");
        this.rightWingTip = this.rightWingBase.getChild("right_wing_tip");
        this.head = main.getChild("head");
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
        createBodyLayer(meshdefinition);
        return meshdefinition;
    }

    public static LayerDefinition createBodyLayer(MeshDefinition definition) {
        PartDefinition partDefinition = definition.getRoot();
        PartDefinition body = partDefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 8).addBox(-3.0F, -2.0F, -8.0F, 5.0F, 3.0F, 9.0F), PartPose.rotation(-0.1F, 0.0F, 0.0F));
        PartDefinition tail = body.addOrReplaceChild("tail_base", CubeListBuilder.create().texOffs(3, 20).addBox(-2.0F, 0.0F, 0.0F, 3.0F, 2.0F, 6.0F), PartPose.offset(0.0F, -2.0F, 1.0F));
        tail.addOrReplaceChild("tail_tip", CubeListBuilder.create().texOffs(4, 29).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 6.0F), PartPose.offset(0.0F, 0.5F, 6.0F));
        PartDefinition left_wing = body.addOrReplaceChild("left_wing_base", CubeListBuilder.create().texOffs(23, 12).addBox(0.0F, 0.0F, 0.0F, 6.0F, 2.0F, 9.0F), PartPose.offsetAndRotation(2.0F, -2.0F, -8.0F, 0.0F, 0.0F, 0.1F));
        left_wing.addOrReplaceChild("left_wing_tip", CubeListBuilder.create().texOffs(16, 24).addBox(0.0F, 0.0F, 0.0F, 13.0F, 1.0F, 9.0F), PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1F));
        PartDefinition right_wing = body.addOrReplaceChild("right_wing_base", CubeListBuilder.create().texOffs(23, 12).mirror().addBox(-6.0F, 0.0F, 0.0F, 6.0F, 2.0F, 9.0F), PartPose.offsetAndRotation(-3.0F, -2.0F, -8.0F, 0.0F, 0.0F, -0.1F));
        right_wing.addOrReplaceChild("right_wing_tip", CubeListBuilder.create().texOffs(16, 24).mirror().addBox(-13.0F, 0.0F, 0.0F, 13.0F, 1.0F, 9.0F), PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1F));
        body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -2.0F, -5.0F, 7.0F, 3.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 1.0F, -7.0F, 0.2F, 0.0F, 0.0F));
        return LayerDefinition.create(definition, 64, 64);
    }

    public void setupAnim(T p_102866_, float p_102867_, float p_102868_, float p_102869_, float p_102870_, float p_102871_) {
        float $$6 = ((float)3 + p_102869_) * 7.448451F * 0.017453292F;
        float $$7 = 16.0F;
        this.leftWingBase.zRot = Mth.cos($$6) * 16.0F * 0.017453292F;
        this.leftWingTip.zRot = Mth.cos($$6) * 16.0F * 0.017453292F;
        this.rightWingBase.zRot = -this.leftWingBase.zRot;
        this.rightWingTip.zRot = -this.leftWingTip.zRot;
        this.tailBase.xRot = -(5.0F + Mth.cos($$6 * 2.0F) * 5.0F) * 0.017453292F;
        this.tailTip.xRot = -(5.0F + Mth.cos($$6 * 2.0F) * 5.0F) * 0.017453292F;
        boolean flag = p_102866_.getFallFlyingTicks() > 4;
        boolean flag1 = p_102866_.isVisuallySwimming();
        this.head.yRot = p_102870_ * 0.017453292F;
        if (flag) {
            this.head.xRot = -0.7853982F;
        } else if (this.swimAmount > 0.0F) {
            if (flag1) {
                this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, -0.7853982F);
            } else {
                this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, p_102871_ * 0.017453292F);
            }
        } else {
            this.head.xRot = p_102871_ * 0.017453292F;
        }

        this.body.yRot = 0.0F;
        this.rightArm.z = 0.0F;
        this.rightArm.x = -5.0F;
        this.leftArm.z = 0.0F;
        this.leftArm.x = 5.0F;
        float f = 1.0F;
        if (flag) {
            f = (float)p_102866_.getDeltaMovement().lengthSqr();
            f /= 0.2F;
            f *= f * f;
        }

        if (f < 1.0F) {
            f = 1.0F;
        }
    }

    public Iterable<ModelPart> modifiedBodyParts() {
        return ImmutableList.of(this.head, this.body, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg);
    }

    public void translateToHead(PoseStack stack) {
        this.body.translateAndRotate(stack);
        this.head.translateAndRotate(stack);
    }
}
