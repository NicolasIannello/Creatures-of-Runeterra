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
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class CoRPartEntity extends PathfinderMob implements GeoEntity {
    public PathfinderMob parent;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

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
        if(source==damageSources().inWall()){
            if(parent instanceof RekSaiEntity) if(!(Boolean)((RekSaiEntity)parent).getEntityData().get((((RekSaiEntity) parent).HEAL))) breakBB(getBoundingBox());//if(!(Boolean)((RekSaiEntity)parent).getDataManager().getAll().get(17).getValue()) breakBB(getBoundingBox());
            return false;
        }
        if(source==damageSources().drown()) return false;
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
        if(!this.level.isClientSide && this.getParent()==null) this.remove(RemovalReason.DISCARDED);
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
    public boolean canChangeDimensions() { return false; }
    @Override
    public boolean isPersistenceRequired() { return true; }
    @Override
    public boolean requiresCustomPersistence() { return true; }
    @Override
    protected boolean shouldDespawnInPeaceful() { return true; }
    @Override
    public boolean canBeLeashed(Player p_21418_) { return false; }

    public <T extends GeoAnimatable> PlayState predicate(AnimationState<T> event)  {
        return PlayState.CONTINUE;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers){
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}