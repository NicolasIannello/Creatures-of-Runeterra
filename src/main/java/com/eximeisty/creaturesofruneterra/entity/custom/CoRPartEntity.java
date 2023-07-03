package com.eximeisty.creaturesofruneterra.entity.custom;

import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CoRPartEntity extends CreatureEntity{
    public CreatureEntity parent;

    public CoRPartEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 1);
    }

    public void setParent(CreatureEntity rek){
        this.parent=rek;
    }

    public CreatureEntity getParent(){
        return this.parent;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source==DamageSource.IN_WALL){
            if(parent instanceof RekSaiEntity) if(!(Boolean)((RekSaiEntity)parent).getDataManager().get(RekSaiEntity.HEAL)) breakBB(getBoundingBox());
            return false;
        }
        if(source==DamageSource.DROWN) return false;
        return this.parent==null ? super.attackEntityFrom(source, amount) : this.parent.attackEntityFrom(source, amount);
    }

    protected void breakBB(AxisAlignedBB bb){
        BlockPos.getAllInBox(bb).forEach(pos->{
            if( this.world.getBlockState(pos)!=Blocks.AIR.getDefaultState() && this.world.getBlockState(pos)!=Blocks.WATER.getDefaultState() && this.world.getBlockState(pos)!=Blocks.LAVA.getDefaultState() && this.world.getBlockState(pos)!=Blocks.BEDROCK.getDefaultState()){
                this.world.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        });
    }

    @Override
    protected void updateLeashedState() {
        super.updateLeashedState();
        this.clearLeashed(true, false);
        return;
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if(!this.world.isRemote && this.getParent()==null) this.remove();
    }
    
    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) { return false; }
    @Override
    public boolean addPotionEffect(EffectInstance effectInstanceIn) { return false; }
    @Override
    protected boolean canBeRidden(Entity entityIn) { return false; }
    @Override
    public boolean canPassengerSteer() { return false; }
    @Override
    public boolean shouldRiderSit() { return false; }
    @Override
    public boolean canBePushed() { return true; }
    @Override
    public boolean canBeRiddenInWater(Entity rider) { return true; }
    @Override
    public boolean canChangeDimension() { return false; }
    @Override
    public boolean canDespawn(double distanceToClosestPlayer) { return false; }
    @Override
    public boolean preventDespawn() { return true; }
    @Override
    public boolean isNoDespawnRequired() { return true; }
}