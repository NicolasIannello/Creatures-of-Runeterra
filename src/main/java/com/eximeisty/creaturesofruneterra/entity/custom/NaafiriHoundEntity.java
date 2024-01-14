package com.eximeisty.creaturesofruneterra.entity.custom;

import com.eximeisty.creaturesofruneterra.util.ModSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class NaafiriHoundEntity extends TamableAnimal implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final EntityDataAccessor<Integer> ATTACK = SynchedEntityData.defineId(NaafiriHoundEntity.class, EntityDataSerializers.INT);
    private int ticks=0;
    private int ticksDash=0;
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().then("animation.naafiri.idle", Animation.LoopType.LOOP);
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().then("animation.naafiri.walk", Animation.LoopType.LOOP);
    private static final RawAnimation ATTACK_ANIM = RawAnimation.begin().then("animation.naafiri.bite", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation DASH_ANIM = RawAnimation.begin().then("animation.naafiri.dash", Animation.LoopType.LOOP);

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
                    enemy.hurt(damageSources().mobAttack(this.mob),16F);
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

    public <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState)  {
        if(entityData.get(ATTACK)==0){
            if (tAnimationState.isMoving()) {
                tAnimationState.getController().setAnimation(WALK_ANIM);
                return PlayState.CONTINUE;
            }
            tAnimationState.getController().setAnimation(IDLE_ANIM);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    public <T extends GeoAnimatable> PlayState predicate2(AnimationState<T> tAnimationState)  {
        if(entityData.get(ATTACK)==1){
            tAnimationState.getController().setAnimation(ATTACK_ANIM);
            return PlayState.CONTINUE;
        }
        if(entityData.get(ATTACK)==3){
            tAnimationState.getController().setAnimation(DASH_ANIM);
            return PlayState.CONTINUE;
        }
        tAnimationState.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers){
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
        controllers.add(new AnimationController<>(this, "attacks", 0, this::predicate2));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public void tick() {
        super.tick();
        if(entityData.get(ATTACK)==1){
            ticks++;
            if(ticks==5) this.level().playSound(null, this.blockPosition(), ModSounds.NAAFIRI_ATTACK.get(), SoundSource.NEUTRAL, 0.5F, 2);
            if(ticks==15){
                ticks=0;
                entityData.set(ATTACK, 0);
            }
        }
        if(entityData.get(ATTACK)==3){
            ticks++;
            if(ticks>3 && this.onGround()){
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