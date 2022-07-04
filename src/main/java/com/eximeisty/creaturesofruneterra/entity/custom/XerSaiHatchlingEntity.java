package com.eximeisty.creaturesofruneterra.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.ClimberPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
//import net.minecraft.potion.EffectInstance;
//import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class XerSaiHatchlingEntity extends CreatureEntity {
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(XerSaiHatchlingEntity.class, DataSerializers.BYTE);

    public XerSaiHatchlingEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 6)
        .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.6)
        .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2)
        .createMutableAttribute(Attributes.FOLLOW_RANGE, 50)
        .createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 0)
        .createMutableAttribute(Attributes.ATTACK_SPEED, 2);
    }

    @Override
    protected void registerGoals(){
        super.registerGoals();
        this.goalSelector.addGoal( 1, new NearestAttackableTargetGoal<>( this, PlayerEntity.class, true ));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(1, new RandomWalkingGoal(this, 0.6,80));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp(XerSaiHatchlingEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player){ return 1+this.world.rand.nextInt(2); }

    @Override
    protected SoundEvent getAmbientSound(){ return SoundEvents.ENTITY_SILVERFISH_AMBIENT; }
    
    @Override
    protected SoundEvent getDeathSound(){ return SoundEvents.ENTITY_SILVERFISH_DEATH; }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn){ return SoundEvents.ENTITY_SPIDER_HURT; }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn){ this.playSound(SoundEvents.ENTITY_SILVERFISH_STEP, 0.20F, 0.5F); }

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
    protected void updateLeashedState() {
        super.updateLeashedState();
        this.clearLeashed(true, true);
        return;
    }

    protected PathNavigator createNavigator(World worldIn) {
        return new ClimberPathNavigator(this, worldIn);
    }
    
    protected void registerData() {
        super.registerData();
        this.dataManager.register(CLIMBING, (byte)0);
    }
    
    public void tick() {
        super.tick();
        if (!this.world.isRemote) {
           this.setBesideClimbableBlock(this.collidedHorizontally);
        }
    }
    
    public boolean isOnLadder() {
        return this.isBesideClimbableBlock();
    }
    
    public boolean isBesideClimbableBlock() {
        return (this.dataManager.get(CLIMBING) & 1) != 0;
    }
    
    public void setBesideClimbableBlock(boolean climbing) {
        byte b0 = this.dataManager.get(CLIMBING);
        if (climbing) {
           b0 = (byte)(b0 | 1);
        } else {
           b0 = (byte)(b0 & -2);
        }
        this.dataManager.set(CLIMBING, b0);
    }
}
