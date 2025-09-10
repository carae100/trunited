package io.github.dracosomething.trawakened.util;

import com.mojang.math.Matrix4f;
import com.mojang.math.Vector4f;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.extensions.IForgeBlockEntity;

public class Frustum {
    public static final int OFFSET_STEP = 4;
    private final Vector4f[] frustumData = new Vector4f[6];
    private Vector4f viewVector;
    private double camX;
    private double camY;
    private double camZ;

    public Frustum(Matrix4f p_113000_, Matrix4f p_113001_) {
        this.calculateFrustum(p_113000_, p_113001_);
    }

    public Frustum(Frustum p_194440_) {
        System.arraycopy(p_194440_.frustumData, 0, this.frustumData, 0, p_194440_.frustumData.length);
        this.camX = p_194440_.camX;
        this.camY = p_194440_.camY;
        this.camZ = p_194440_.camZ;
        this.viewVector = p_194440_.viewVector;
    }

    public Frustum offsetToFullyIncludeCameraCube(int p_194442_) {
        double $$1 = Math.floor(this.camX / (double) p_194442_) * (double) p_194442_;
        double $$2 = Math.floor(this.camY / (double) p_194442_) * (double) p_194442_;
        double $$3 = Math.floor(this.camZ / (double) p_194442_) * (double) p_194442_;
        double $$4 = Math.ceil(this.camX / (double) p_194442_) * (double) p_194442_;
        double $$5 = Math.ceil(this.camY / (double) p_194442_) * (double) p_194442_;

        for (double $$6 = Math.ceil(this.camZ / (double) p_194442_) * (double) p_194442_; !this.cubeCompletelyInFrustum((float) ($$1 - this.camX), (float) ($$2 - this.camY), (float) ($$3 - this.camZ), (float) ($$4 - this.camX), (float) ($$5 - this.camY), (float) ($$6 - this.camZ)); this.camZ -= (double) (this.viewVector.z() * 4.0F)) {
            this.camX -= (double) (this.viewVector.x() * 4.0F);
            this.camY -= (double) (this.viewVector.y() * 4.0F);
        }

        return this;
    }

    public void prepare(double p_113003_, double p_113004_, double p_113005_) {
        this.camX = p_113003_;
        this.camY = p_113004_;
        this.camZ = p_113005_;
    }

    private void calculateFrustum(Matrix4f p_113027_, Matrix4f p_113028_) {
        Matrix4f $$2 = p_113028_.copy();
        $$2.multiply(p_113027_);
        $$2.transpose();
        this.viewVector = new Vector4f(0.0F, 0.0F, 1.0F, 0.0F);
        this.viewVector.transform($$2);
        this.getPlane($$2, -1, 0, 0, 0);
        this.getPlane($$2, 1, 0, 0, 1);
        this.getPlane($$2, 0, -1, 0, 2);
        this.getPlane($$2, 0, 1, 0, 3);
        this.getPlane($$2, 0, 0, -1, 4);
        this.getPlane($$2, 0, 0, 1, 5);
    }

    private void getPlane(Matrix4f p_113021_, int p_113022_, int p_113023_, int p_113024_, int p_113025_) {
        Vector4f $$5 = new Vector4f((float) p_113022_, (float) p_113023_, (float) p_113024_, 1.0F);
        $$5.transform(p_113021_);
        $$5.normalize();
        this.frustumData[p_113025_] = $$5;
    }

    public boolean isVisible(AABB p_113030_) {
        return this.cubeInFrustum(p_113030_.minX, p_113030_.minY, p_113030_.minZ, p_113030_.maxX, p_113030_.maxY, p_113030_.maxZ);
    }

    private boolean cubeInFrustum(double p_113007_, double p_113008_, double p_113009_, double p_113010_, double p_113011_, double p_113012_) {
        float $$6 = (float) (p_113007_ - this.camX);
        float $$7 = (float) (p_113008_ - this.camY);
        float $$8 = (float) (p_113009_ - this.camZ);
        float $$9 = (float) (p_113010_ - this.camX);
        float $$10 = (float) (p_113011_ - this.camY);
        float $$11 = (float) (p_113012_ - this.camZ);
        return this.cubeInFrustum($$6, $$7, $$8, $$9, $$10, $$11);
    }

    private boolean cubeInFrustum(float p_113014_, float p_113015_, float p_113016_, float p_113017_, float p_113018_, float p_113019_) {
        for (int $$6 = 0; $$6 < 6; ++$$6) {
            Vector4f $$7 = this.frustumData[$$6];
            if (!($$7.dot(new Vector4f(p_113014_, p_113015_, p_113016_, 1.0F)) > 0.0F) && !($$7.dot(new Vector4f(p_113017_, p_113015_, p_113016_, 1.0F)) > 0.0F) && !($$7.dot(new Vector4f(p_113014_, p_113018_, p_113016_, 1.0F)) > 0.0F) && !($$7.dot(new Vector4f(p_113017_, p_113018_, p_113016_, 1.0F)) > 0.0F) && !($$7.dot(new Vector4f(p_113014_, p_113015_, p_113019_, 1.0F)) > 0.0F) && !($$7.dot(new Vector4f(p_113017_, p_113015_, p_113019_, 1.0F)) > 0.0F) && !($$7.dot(new Vector4f(p_113014_, p_113018_, p_113019_, 1.0F)) > 0.0F) && !($$7.dot(new Vector4f(p_113017_, p_113018_, p_113019_, 1.0F)) > 0.0F)) {
                return false;
            }
        }

        return true;
    }

    private boolean cubeCompletelyInFrustum(float p_194444_, float p_194445_, float p_194446_, float p_194447_, float p_194448_, float p_194449_) {
        for (int $$6 = 0; $$6 < 6; ++$$6) {
            Vector4f $$7 = this.frustumData[$$6];
            if ($$7.dot(new Vector4f(p_194444_, p_194445_, p_194446_, 1.0F)) <= 0.0F) {
                return false;
            }

            if ($$7.dot(new Vector4f(p_194447_, p_194445_, p_194446_, 1.0F)) <= 0.0F) {
                return false;
            }

            if ($$7.dot(new Vector4f(p_194444_, p_194448_, p_194446_, 1.0F)) <= 0.0F) {
                return false;
            }

            if ($$7.dot(new Vector4f(p_194447_, p_194448_, p_194446_, 1.0F)) <= 0.0F) {
                return false;
            }

            if ($$7.dot(new Vector4f(p_194444_, p_194445_, p_194449_, 1.0F)) <= 0.0F) {
                return false;
            }

            if ($$7.dot(new Vector4f(p_194447_, p_194445_, p_194449_, 1.0F)) <= 0.0F) {
                return false;
            }

            if ($$7.dot(new Vector4f(p_194444_, p_194448_, p_194449_, 1.0F)) <= 0.0F) {
                return false;
            }

            if ($$7.dot(new Vector4f(p_194447_, p_194448_, p_194449_, 1.0F)) <= 0.0F) {
                return false;
            }
        }

        return true;
    }
}