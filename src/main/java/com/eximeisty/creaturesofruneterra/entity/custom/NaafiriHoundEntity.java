package com.eximeisty.creaturesofruneterra.entity.custom;

import com.eximeisty.creaturesofruneterra.util.ModSoundEvents;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class NaafiriHoundEntity extends TameableEntity implements IAnimatable {
    private AnimationFactory cache = new AnimationFactory(this);
    public static final DataParameter<Integer> ATTACK = EntityDataManager.createKey(NaafiriHoundEntity.class, DataSerializers.VARINT);
    private int ticks=0;
    private int ticksDash=0;
    private static final AnimationBuilder IDLE_ANIM = new AnimationBuilder().addAnimation("animation.naafiri.idle", true);
    private static final AnimationBuilder WALK_ANIM = new AnimationBuilder().addAnimation("animation.naafiri.walk", true);
    private static final AnimationBuilder ATTACK_ANIM = new AnimationBuilder().addAnimation("animation.naafiri.bite", false);
    private static final AnimationBuilder DASH_ANIM = new AnimationBuilder().addAnimation("animation.naafiri.dash", true);

    public NaafiriHoundEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 20)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.37)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 5);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true){
            @Override
            public boolean shouldExecute() {
                if(this.attacker.getAttackTarget()!=null ? this.attacker.getAttackTarget() instanceof TameableEntity && ((TameableEntity)this.attacker.getAttackTarget()).getOwner()!= null && ((TameableEntity)this.attacker.getAttackTarget()).getOwner().isEntityEqual(((NaafiriHoundEntity) this.attacker).getOwner()) : false) return false;
                return super.shouldExecute();
            }
            @Override
            protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
                double d0 = this.getAttackReachSqr(enemy);
                if(d0 >= 2 && this.attacker.getDataManager().get(ATTACK)==0 && ((NaafiriHoundEntity)this.attacker).ticksDash==0){
                    this.attacker.getDataManager().set(ATTACK, 3);
                    Vector3d vec31 = new Vector3d((this.attacker.getAttackTarget().getPosX()-this.attacker.getPosX())*0.3, 0.4D, (this.attacker.getAttackTarget().getPosZ()-this.attacker.getPosZ())*0.3);
                    this.attacker.setMotion(vec31);
                }
                if (distToEnemySqr <= d0 && this.attacker.getDataManager().get(ATTACK)==3) {
                    this.resetSwingCooldown();
                    enemy.attackEntityFrom(DamageSource.causeMobDamage(this.attacker),16F);
                    ((NaafiriHoundEntity)this.attacker).ticks=0;
                    ((NaafiriHoundEntity)this.attacker).ticksDash=200;
                    this.attacker.getDataManager().set(ATTACK, 1);
                }
                if (distToEnemySqr <= d0 && this.attacker.getDataManager().get(ATTACK)==0) {
                    this.resetSwingCooldown();
                    this.attacker.attackEntityAsMob(enemy);
                    this.attacker.getDataManager().set(ATTACK, 1);
                }
            }
        });
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)));
    }

    @Override
    public AgeableEntity createChild(ServerWorld world, AgeableEntity mate) {
        return null;
    }
    
    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(dataManager.get(ATTACK)==0){
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
        if(dataManager.get(ATTACK)==1){
            event.getController().setAnimation(ATTACK_ANIM);
            return PlayState.CONTINUE;
        }
        if(dataManager.get(ATTACK)==3){
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
        if(dataManager.get(ATTACK)==1){
            ticks++;
            if(ticks==5) this.world.playSound(null, new BlockPos(this.getPosX(), this.getPosY(), this.getPosZ()), ModSoundEvents.NAAFIRI_ATTACK.get(), SoundCategory.NEUTRAL, 0.5F, 2);
            if(ticks==15){
                ticks=0;
                dataManager.set(ATTACK, 0);
            }
        }
        if(dataManager.get(ATTACK)==3){
            ticks++;
            if(ticks>3 && this.isOnGround()){
                ticks=0;
                ticksDash=200;
                dataManager.set(ATTACK, 0);
            }
        }
        //if((tickCount > 1000 || this.getOwner() != null) && !this.getOwner().isAlive()) this.discard();
    }

    protected void registerData() {
        super.registerData();
        dataManager.register(ATTACK, 0);
    }

//    protected SoundEvent getDeathSound() { return SoundEvents.BAT_DEATH; }
//    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SoundEvents.FOX_HURT; }
//    public SoundEvent getAmbientSound() { return SoundEvents.FOX_AMBIENT; }
}