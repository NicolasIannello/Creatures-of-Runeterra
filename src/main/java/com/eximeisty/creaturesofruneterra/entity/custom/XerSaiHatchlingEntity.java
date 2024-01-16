package com.eximeisty.creaturesofruneterra.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class XerSaiHatchlingEntity extends PathfinderMob {
    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(XerSaiHatchlingEntity.class, EntityDataSerializers.BYTE);

    public XerSaiHatchlingEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier setAttributes(){
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 2)
                .add(Attributes.MOVEMENT_SPEED, 0.6)
                .add(Attributes.ATTACK_DAMAGE, 2)
                .add(Attributes.FOLLOW_RANGE, 40)
                .add(Attributes.ATTACK_KNOCKBACK, 0)
                /*.add(Attributes.ATTACK_SPEED, 2)*/.build();
    }

    @Override
    protected void registerGoals(){
        super.registerGoals();
        this.goalSelector.addGoal( 1, new NearestAttackableTargetGoal<>( this, Player.class, true ));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.6,80));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(XerSaiHatchlingEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    public int getExperienceReward(){ return 1+this.level.random.nextInt(2); }

    @Override
    protected SoundEvent getAmbientSound(){ return SoundEvents.SILVERFISH_AMBIENT; }
    
    @Override
    protected SoundEvent getDeathSound(){ return SoundEvents.SILVERFISH_DEATH; }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn){ return SoundEvents.SPIDER_HURT; }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn){ this.playSound(SoundEvents.SILVERFISH_STEP, 0.20F, 0.5F); }

    /*@Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (!super.attackEntityAsMob(entityIn)) {
            return false;
        } else {
            // if (entityIn instanceof LivingEntity) {
            //     ((LivingEntity)entityIn).addPotionEffect(new EffectInstance(Effects.SLOWNESS, 200,3));
            //     ((LivingEntity)entityIn).addPotionEffect(new EffectInstance(Effects.WEAKNESS, 200));
            //     ((LivingEntity)entityIn).addPotionEffect(new EffectInstance(Effects.NAUSEA, 200));
            // }
            return true;
        }
    }*/

    @Override
    protected void tickLeash() {
        super.tickLeash();
        this.dropLeash(true, true);
    }

    protected PathNavigation createNavigation(Level p_33802_) {
        return new WallClimberNavigation(this, p_33802_);
    }
    
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CLIMBING, (byte)0);
    }
    
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
           this.setBesideClimbableBlock(this.horizontalCollision);
        }
    }

    public boolean onClimbable() {
        return this.isClimbing();
    }

    public boolean isClimbing() {
        return (this.entityData.get(CLIMBING) & 1) != 0;
    }

    public void setBesideClimbableBlock(boolean p_33820_) {
        byte b0 = this.entityData.get(CLIMBING);
        if (p_33820_) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }

        this.entityData.set(CLIMBING, b0);
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource p_146830_) { return false; }
    @Override
    public boolean canBeLeashed(Player entityIn) { return false; }
    @Override
    public boolean canChangeDimensions() { return false; }
}
