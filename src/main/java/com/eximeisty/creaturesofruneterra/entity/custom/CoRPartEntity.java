package com.eximeisty.creaturesofruneterra.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;

public class CoRPartEntity extends PathfinderMob {
    public PathfinderMob parent;

    public CoRPartEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier setAttributes(){
        return PathfinderMob.createMobAttributes().add(Attributes.MAX_HEALTH, 1).build();
    }

    public void setParent(PathfinderMob rek){
        this.parent=rek;
    }

    public PathfinderMob getParent(){
        return this.parent;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(source==DamageSource.IN_WALL){
            if(parent instanceof RekSaiEntity) if(!(Boolean)((RekSaiEntity)parent).getEntityData().get(RekSaiEntity.HEAL)) breakBB(getBoundingBox());
            return false;
        }
        if(source==DamageSource.DROWN) return false;
        if(this.parent==null && !this.level.isClientSide) this.discard();
        return this.parent==null ? super.hurt(source, amount) : this.parent.hurt(source, amount);
    }

    protected void breakBB(AABB bb){
        BlockPos.betweenClosedStream(bb).forEach(pos ->{
            if(this.level.getBlockState(pos)!= Blocks.AIR.defaultBlockState() && this.level.getBlockState(pos)!=Blocks.WATER.defaultBlockState() && this.level.getBlockState(pos)!=Blocks.LAVA.defaultBlockState() && this.level.getBlockState(pos)!=Blocks.BEDROCK.defaultBlockState()) {
                this.level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            }
        });
    }

    @Override
    protected void tickLeash() {
        super.tickLeash();
        this.dropLeash(true, true);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if(!this.level.isClientSide && this.getParent()==null) this.discard();
    }
    
    @Override
    public boolean causeFallDamage(float p_146828_, float p_146829_, DamageSource p_146830_) { return false; }
    @Override
    public boolean addEffect(MobEffectInstance effectInstanceIn, @Nullable Entity p_147209_) { return false; }
    @Override
    protected boolean canAddPassenger(Entity entityIn) { return false; }
    @Override
    public boolean shouldRiderSit() { return false; }
    @Override
    public boolean canBeLeashed(Player p_21418_) { return false; }
//    @Override
//    public boolean canBeRiddenInWater(Entity rider) { return true; }
    @Override
    public boolean canChangeDimensions() { return false; }
    @Override
    public boolean isPersistenceRequired() { return true; }
    @Override
    public boolean requiresCustomPersistence() { return true; }
    @Override
    protected boolean shouldDespawnInPeaceful() { return true; }
//    @Override
//    public boolean canBeControlledByRider() { return false; }
//    @Override
//    public boolean canCollideWith(Entity pEntity) { return false; }
}