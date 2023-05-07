package com.eximeisty.creaturesofruneterra.entity.custom;

import java.util.EnumSet;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtTargetGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class PlunderPoroEntity extends TameableEntity implements IAnimatable, IRangedAttackMob{
    private AnimationFactory factory = new AnimationFactory(this);
    private static final DataParameter<Boolean> STATE = EntityDataManager.createKey(PlunderPoroEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ATTACK = EntityDataManager.createKey(PlunderPoroEntity.class, DataSerializers.BOOLEAN);
    private int ticks=0;
    public ItemStack forgeItem=ItemStack.EMPTY;
    private static final AnimationBuilder IDLE_ANIM = new AnimationBuilder().addAnimation("animation.plunderporo.idle", true);
    private static final AnimationBuilder WALK_ANIM = new AnimationBuilder().addAnimation("animation.plunderporo.walk", true);
    private static final AnimationBuilder SIT_ANIM = new AnimationBuilder().addAnimation("animation.plunderporo.sit", true);
    private static final AnimationBuilder SHOT_ANIM = new AnimationBuilder().addAnimation("animation.plunderporo.shoot", false);

    public PlunderPoroEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setTamed(false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 20)
        .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.24)
        .createMutableAttribute(Attributes.ATTACK_DAMAGE, 7)
        .createMutableAttribute(Attributes.ATTACK_SPEED, 1.2)
        .createMutableAttribute(Attributes.ARMOR, 3)
        .createMutableAttribute(Attributes.ARMOR_TOUGHNESS, 1);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(5, new PlunderPoroEntity.RangedAttackGoal(this, 1, 70, 13));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, MonsterEntity.class, 3.0F, 1D, 1.4D));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(dataManager.get(STATE)){
            event.getController().setAnimation(SIT_ANIM);
            return PlayState.CONTINUE;
        }
        if (event.isMoving()) {
            event.getController().setAnimation(WALK_ANIM);
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(IDLE_ANIM);
        return PlayState.CONTINUE;
    }

    public <E extends IAnimatable> PlayState predicate2(AnimationEvent<E> event) {
        if(dataManager.get(ATTACK)){
            event.getController().setAnimation(SHOT_ANIM);
            return PlayState.CONTINUE;
        }
        event.getController().clearAnimationCache();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<IAnimatable>(this, "controller", 0, this::predicate));
        data.addAnimationController(new AnimationController<IAnimatable>(this, "attacks", 0, this::predicate2));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    protected void registerData() {
        super.registerData();
        dataManager.register(STATE, false);
        dataManager.register(ATTACK, false);
    }

    @Override
    public AgeableEntity createChild(ServerWorld world, AgeableEntity mate) {
        return null;
    }

    public void tick() {
        super.tick();
        if(dataManager.get(ATTACK)){
            ticks++;
            if(ticks==12){
                ticks=0;
                dataManager.set(ATTACK, false);
            }
        }
    }

    public ActionResultType getEntityInteractionResult(PlayerEntity playerIn, Hand hand) {
        if (this.world.isRemote) {
            boolean flag = this.isOwner(playerIn);
            return flag ? ActionResultType.CONSUME : ActionResultType.PASS;
        }else{
            if(this.isOwner(playerIn)){
                this.setSitting(!dataManager.get(STATE));
            }
            return super.getEntityInteractionResult(playerIn, hand);
        } 
    }

    @Override
    public void setSitting(boolean sit) {
        this.dataManager.set(STATE, sit);
        super.setSitting(sit);
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.dataManager.set(STATE, compound.getBoolean("Sitting"));
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        AbstractArrowEntity abstractarrowentity = this.fireArrow(new ItemStack(Items.ARROW), distanceFactor);
        abstractarrowentity = new ArrowEntity(this.world, this);
        double d0 = target.getPosX() - this.getPosX();
        double d1 = target.getPosYHeight(0.3333333333333333D) - abstractarrowentity.getPosY();
        double d2 = target.getPosZ() - this.getPosZ();
        double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
        abstractarrowentity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.world.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        abstractarrowentity.setIsCritical(true);
        abstractarrowentity.setDamage(abstractarrowentity.getDamage() + 0D);
        this.world.addEntity(abstractarrowentity);        
    }

    protected AbstractArrowEntity fireArrow(ItemStack arrowStack, float distanceFactor) {
        return ProjectileHelper.fireArrow(this, arrowStack, distanceFactor);
    }

    public class RangedAttackGoal extends Goal {
        private final MobEntity entityHost;
        private final IRangedAttackMob rangedAttackEntityHost;
        private LivingEntity attackTarget;
        private int rangedAttackTime = -1;
        private final double entityMoveSpeed;
        private int seeTime;
        private final int attackIntervalMin;
        private final int maxRangedAttackTime;
        private final float attackRadius;
        private final float maxAttackDistance;
        private boolean strafingClockwise;
        private boolean strafingBackwards;
        private int strafingTime = -1;
     
        public RangedAttackGoal(IRangedAttackMob attacker, double movespeed, int maxAttackTime, float maxAttackDistanceIn) {
            this(attacker, movespeed, maxAttackTime, maxAttackTime, maxAttackDistanceIn);
        }
     
        public RangedAttackGoal(IRangedAttackMob attacker, double movespeed, int minAttackTime, int maxAttackTime, float maxAttackDistanceIn) {
            if (!(attacker instanceof LivingEntity)) {
                throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
            } else {
                this.rangedAttackEntityHost = attacker;
                this.entityHost = (MobEntity)attacker;
                this.entityMoveSpeed = movespeed;
                this.attackIntervalMin = minAttackTime;
                this.maxRangedAttackTime = maxAttackTime;
                this.attackRadius = maxAttackDistanceIn;
                this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
                this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
            }
        }
     
        public boolean shouldExecute() {
            LivingEntity livingentity = this.entityHost.getAttackTarget();
            if (livingentity != null && livingentity.isAlive()) {
                this.attackTarget = livingentity;
                return true;
            } else {
                return false;
            }
        }
     
        public boolean shouldContinueExecuting() {
            return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
        }
     
        public void resetTask() {
            this.attackTarget = null;
            this.seeTime = 0;
            this.rangedAttackTime = -1;
        }
     
        public void tick() {
            double d0 = this.entityHost.getDistanceSq(this.attackTarget.getPosX(), this.attackTarget.getPosY(), this.attackTarget.getPosZ());
            boolean flag = this.entityHost.getEntitySenses().canSee(this.attackTarget);
            if (flag) {
                ++this.seeTime;
            } else {
                this.seeTime = 0;
            }

            if (!(d0 > (double)this.maxAttackDistance) && this.seeTime >= 20){
                this.entityHost.getNavigator().clearPath();
                ++this.strafingTime;
            } else {
                this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if ((double)this.entityHost.getRNG().nextFloat() < 0.3D) this.strafingClockwise = !this.strafingClockwise;
                if ((double)this.entityHost.getRNG().nextFloat() < 0.3D) this.strafingBackwards = !this.strafingBackwards;
                this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
                if (d0 > (double)(this.maxAttackDistance * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (d0 < (double)(this.maxAttackDistance * 0.25F)) {
                    this.strafingBackwards = true;
                }
                this.entityHost.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.entityHost.faceEntity(this.attackTarget, 30.0F, 30.0F);
            } else {
                this.entityHost.getLookController().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
            }       
                 
            if (--this.rangedAttackTime == 0) {
                if (!flag) return;
                float f = MathHelper.sqrt(d0) / this.attackRadius;
                float lvt_5_1_ = MathHelper.clamp(f, 0.1F, 1.0F);
                dataManager.set(ATTACK, true);
                this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, lvt_5_1_);
                this.rangedAttackTime = MathHelper.floor(f * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
            } else if (this.rangedAttackTime < 0) {
                float f2 = MathHelper.sqrt(d0) / this.attackRadius;
                this.rangedAttackTime = MathHelper.floor(f2 * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
            }
        }
    }

    protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_BAT_DEATH; }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SoundEvents.ENTITY_FOX_HURT; }

    public SoundEvent getAmbientSound() { return SoundEvents.ENTITY_FOX_AMBIENT; }
}