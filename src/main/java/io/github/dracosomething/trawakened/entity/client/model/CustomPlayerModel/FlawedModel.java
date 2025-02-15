package io.github.dracosomething.trawakened.entity.client.model.CustomPlayerModel;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class FlawedModel<T extends LivingEntity> extends PlayerModel<T> {
    public boolean carrying;
    public boolean creepy;
    private final ModelPart hat;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public FlawedModel(ModelPart modelPart) {
        super(modelPart, false);
        this.hat = modelPart.getChild("hat");
        this.head = modelPart.getChild("head");
        this.body = modelPart.getChild("body");
        this.leftArm = modelPart.getChild("left_arm");
        this.rightArm = modelPart.getChild("right_arm");
        this.rightLeg = modelPart.getChild("right_leg");
        this.leftLeg = modelPart.getChild("left_leg");
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

    public Iterable<ModelPart> modifiedBodyParts() {
        return ImmutableList.of(this.head, this.body, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg);
    }

    public void translateToHead(PoseStack stack) {
        this.body.translateAndRotate(stack);
        this.head.translateAndRotate(stack);
    }

    public static LayerDefinition createBodyLayer(MeshDefinition definition) {
        PartDefinition partDefinition = definition.getRoot();
        PartPose pose = PartPose.offset(0.0F, -13.0F, 0.0F);
        partDefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)), pose);
        partDefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), pose);
        partDefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F), PartPose.offset(0.0F, -14.0F, 0.0F));
        partDefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 30.0F, 2.0F), PartPose.offset(-5.0F, -12.0F, 0.0F));
        partDefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(56, 0).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 30.0F, 2.0F), PartPose.offset(5.0F, -12.0F, 0.0F));
        partDefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 30.0F, 2.0F), PartPose.offset(-2.0F, -5.0F, 0.0F));
        partDefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(56, 0).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 30.0F, 2.0F), PartPose.offset(2.0F, -5.0F, 0.0F));
        return LayerDefinition.create(definition, 64, 32);
    }

    public void setupAnim(T p_102588_, float p_102589_, float p_102590_, float p_102591_, float p_102592_, float p_102593_) {
        super.setupAnim(p_102588_, p_102589_, p_102590_, p_102591_, p_102592_, p_102593_);
        this.head.visible = true;
        this.body.xRot = 0.0F;
        this.body.y = -14.0F;
        this.body.z = -0.0F;
        ModelPart var10000 = this.rightLeg;
        var10000.xRot -= 0.0F;
        var10000 = this.leftLeg;
        var10000.xRot -= 0.0F;
        var10000 = this.rightArm;
        var10000.xRot *= 0.5F;
        var10000 = this.leftArm;
        var10000.xRot *= 0.5F;
        var10000 = this.rightLeg;
        var10000.xRot *= 0.5F;
        var10000 = this.leftLeg;
        var10000.xRot *= 0.5F;
        float $$7 = 0.4F;
        if (this.rightArm.xRot > 0.4F) {
            this.rightArm.xRot = 0.4F;
        }

        if (this.leftArm.xRot > 0.4F) {
            this.leftArm.xRot = 0.4F;
        }

        if (this.rightArm.xRot < -0.4F) {
            this.rightArm.xRot = -0.4F;
        }

        if (this.leftArm.xRot < -0.4F) {
            this.leftArm.xRot = -0.4F;
        }

        if (this.rightLeg.xRot > 0.4F) {
            this.rightLeg.xRot = 0.4F;
        }

        if (this.leftLeg.xRot > 0.4F) {
            this.leftLeg.xRot = 0.4F;
        }

        if (this.rightLeg.xRot < -0.4F) {
            this.rightLeg.xRot = -0.4F;
        }

        if (this.leftLeg.xRot < -0.4F) {
            this.leftLeg.xRot = -0.4F;
        }

        if (this.carrying) {
            this.rightArm.xRot = -0.5F;
            this.leftArm.xRot = -0.5F;
            this.rightArm.zRot = 0.05F;
            this.leftArm.zRot = -0.05F;
        }

        this.rightLeg.z = 0.0F;
        this.leftLeg.z = 0.0F;
        this.rightLeg.y = -5.0F;
        this.leftLeg.y = -5.0F;
        this.head.z = -0.0F;
        this.head.y = -13.0F;
        this.hat.x = this.head.x;
        this.hat.y = this.head.y;
        this.hat.z = this.head.z;
        this.hat.xRot = this.head.xRot;
        this.hat.yRot = this.head.yRot;
        this.hat.zRot = this.head.zRot;
        if (this.creepy) {
            float $$8 = 1.0F;
            var10000 = this.head;
            var10000.y -= 5.0F;
        }

        this.rightArm.setPos(-5.0F, -12.0F, 0.0F);
        this.leftArm.setPos(5.0F, -12.0F, 0.0F);
    }
}
