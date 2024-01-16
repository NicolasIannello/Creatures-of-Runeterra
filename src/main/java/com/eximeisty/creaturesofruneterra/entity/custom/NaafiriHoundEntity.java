package com.eximeisty.creaturesofruneterra.entity.custom;

import com.eximeisty.creaturesofruneterra.util.ModSoundEvents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class NaafiriHoundEntity extends TamableAnimal implements IAnimatable {
    private final AnimationFactory cache = GeckoLibUtil.createFactory(this);
    public static final EntityDataAccessor<Integer> ATTACK = SynchedEntityData.defineId(NaafiriHoundEntity.class, EntityDataSerializers.INT);
    private int ticks=0;
    private int ticksDash=0;
    private static final AnimationBuilder IDLE_ANIM = new AnimationBuilder().addAnimation("animation.naafiri.idle", ILoopType.EDefaultLoopTypes.LOOP);
    private static final AnimationBuilder WALK_ANIM = new AnimationBuilder().addAnimation("animation.naafiri.walk", ILoopType.EDefaultLoopTypes.LOOP);
    private static final AnimationBuilder ATTACK_ANIM = new AnimationBuilder().addAnimation("animation.naafiri.bite", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
    private static final AnimationBuilder DASH_ANIM = new AnimationBuilder().addAnimation("animation.naafiri.dash", ILoopType.EDefaultLoopTypes.LOOP);

    public NaafiriHoundEntity(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier setAttributes(){
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.MOVEMENT_SPEED, 0.37)
                .add(Attributes.ATTACK_DAMAGE, 5)
                .build();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true){
            @Override
            public boolean canUse() {
                if(this.mob.getTarget()!=null ? this.mob.getTarget() instanceof TamableAnimal animal && animal.getOwner()!= null && animal.getOwner().is(((NaafiriHoundEntity) this.mob).getOwner()) : false) return false;
                return super.canUse();
            }
            @Override
            protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
                double d0 = this.getAttackReachSqr(enemy);
                if(d0 >= 2 && this.mob.getEntityData().get(ATTACK)==0 && ((NaafiriHoundEntity)this.mob).ticksDash==0){
                    this.mob.getEntityData().set(ATTACK, 3);
                    Vec3 vec31 = new Vec3((this.mob.getTarget().getX()-this.mob.getX())*0.3, 0.4D, (this.mob.getTarget().getZ()-this.mob.getZ())*0.3);
                    this.mob.setDeltaMovement(vec31);
                }
                if (distToEnemySqr <= d0 && this.mob.getEntityData().get(ATTACK)==3) {
                    this.resetAttackCooldown();
                    enemy.hurt(DamageSource.mobAttack(this.mob),16F);
                    ((NaafiriHoundEntity)this.mob).ticks=0;
                    ((NaafiriHoundEntity)this.mob).ticksDash=200;
                    this.mob.getEntityData().set(ATTACK, 1);
                }
                if (distToEnemySqr <= d0 && this.mob.getEntityData().get(ATTACK)==0) {
                    this.resetAttackCooldown();
                    this.mob.doHurtTarget(enemy);
                    this.mob.getEntityData().set(ATTACK, 1);
                }
            }
        });
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)));
    }

    @Override @Nullable
    public NaafiriHoundEntity getBreedOffspring(ServerLevel p_149088_, AgeableMob p_149089_) {
        return null;
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(entityData.get(ATTACK)==0){
            if (event.isMoving()) {
                event.getController().setAnimation(WALK_ANIM);
                return PlayState.CONTINUE;
            }
            event.getController().setAnimation(IDLE_ANIM);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    public <E extends IAnimatable> PlayState predicate2(AnimationEvent<E> event) {
        if(entityData.get(ATTACK)==1){
            event.getController().setAnimation(ATTACK_ANIM);
            return PlayState.CONTINUE;
        }
        if(entityData.get(ATTACK)==3){
            event.getController().setAnimation(DASH_ANIM);
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
        return this.cache;
    }

    public void tick() {
        super.tick();
        if(entityData.get(ATTACK)==1){
            ticks++;
            if(ticks==5) this.level.playSound(null, this.blockPosition(), ModSoundEvents.NAAFIRI_ATTACK.get(), SoundSource.NEUTRAL, 0.5F, 2);
            if(ticks==15){
                ticks=0;
                entityData.set(ATTACK, 0);
            }
        }
        if(entityData.get(ATTACK)==3){
            ticks++;
            if(ticks>3 && this.isOnGround()){
                ticks=0;
                ticksDash=200;
                entityData.set(ATTACK, 0);
            }
        }
        //if((tickCount > 1000 || this.getOwner() != null) && !this.getOwner().isAlive()) this.discard();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ATTACK, 0);
    }

//    protected SoundEvent getDeathSound() { return SoundEvents.BAT_DEATH; }
//    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SoundEvents.FOX_HURT; }
//    public SoundEvent getAmbientSound() { return SoundEvents.FOX_AMBIENT; }
}