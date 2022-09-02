package com.eximeisty.creaturesofruneterra.entity.client.entities.reksai;

import com.eximeisty.creaturesofruneterra.entity.custom.RekSaiEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;

public class RekSaiPartEntity extends net.minecraftforge.entity.PartEntity<RekSaiEntity> {
    public final RekSaiEntity reksai;
    public final String partName;
    private final EntitySize partSize;
 
    public RekSaiPartEntity(RekSaiEntity reksai, String name, float width, float height) {
        super(reksai);
        this.partSize = EntitySize.flexible(width, height);
        this.recalculateSize();
        this.reksai = reksai;
        this.partName = name;
    }
 
    protected void registerData() { }
 
    protected void readAdditional(CompoundNBT compound) { }
 
    protected void writeAdditional(CompoundNBT compound) { }
 
    public boolean canBeCollidedWith() {
        return true;
    }
 
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return this.isInvulnerableTo(source) ? false : this.reksai.attackEntityPartFrom(this, source, amount);
    }
 
    public boolean isEntityEqual(Entity entityIn) {
        return this == entityIn || this.reksai == entityIn;
    }
 
    public IPacket<?> createSpawnPacket() {
        throw new UnsupportedOperationException();
    }
 
    public EntitySize getSize(Pose poseIn) {
        return this.partSize;
    }
}