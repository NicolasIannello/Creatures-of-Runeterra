package com.eximeisty.creaturesofruneterra.entity.custom;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtTargetGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FabledPoroEntity extends TameableEntity implements IAnimatable{
    private AnimationFactory factory = new AnimationFactory(this);
    private static final DataParameter<Boolean> STATE = EntityDataManager.createKey(FabledPoroEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FORGE = EntityDataManager.createKey(FabledPoroEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ATTACK = EntityDataManager.createKey(FabledPoroEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> DAY = EntityDataManager.createKey(FabledPoroEntity.class, DataSerializers.VARINT);
    private double velocidad=0.25;
    private int ticks=0;
    public ItemStack forgeItem=ItemStack.EMPTY;
    private static final AnimationBuilder FORGE_ANIM = new AnimationBuilder().addAnimation("animation.fabledporo.forge", true);
    private static final AnimationBuilder SIT_ANIM = new AnimationBuilder().addAnimation("animation.fabledporo.sit", true);
    private static final AnimationBuilder WALK_ANIM = new AnimationBuilder().addAnimation("animation.fabledporo.walk", true);
    private static final AnimationBuilder IDLE_ANIM = new AnimationBuilder().addAnimation("animation.fabledporo.idle", true);
    private static final AnimationBuilder ATTACK_ANIM = new AnimationBuilder().addAnimation("animation.fabledporo.attack", false);

    public FabledPoroEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setTamed(false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 40)
        .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23)
        .createMutableAttribute(Attributes.ATTACK_DAMAGE, 7)
        .createMutableAttribute(Attributes.ATTACK_SPEED, 1.2)
        .createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1)
        .createMutableAttribute(Attributes.ARMOR, 4)
        .createMutableAttribute(Attributes.ARMOR_TOUGHNESS, 1);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true){
            @Override
            protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
                double d0 = this.getAttackReachSqr(enemy);
                if (distToEnemySqr <= d0 && !this.attacker.getDataManager().get(ATTACK) && !this.attacker.getDataManager().get(FORGE)) {
                   this.resetSwingCooldown();
                   this.attacker.swingArm(Hand.MAIN_HAND);
                   this.attacker.attackEntityAsMob(enemy);
                   this.attacker.getDataManager().set(ATTACK, true);
                }
            }
        });
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)));
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(!dataManager.get(ATTACK)){
            if(dataManager.get(FORGE)){
                event.getController().setAnimation(FORGE_ANIM);
                return PlayState.CONTINUE;
            }
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
        return PlayState.STOP;
    }

    public <E extends IAnimatable> PlayState predicate2(AnimationEvent<E> event) {
        if(dataManager.get(ATTACK)){
            event.getController().setAnimation(ATTACK_ANIM);
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
        dataManager.register(FORGE, false);
        dataManager.register(ATTACK, false);
        dataManager.register(DAY, -1);
    }

    @Override
    public AgeableEntity createChild(ServerWorld world, AgeableEntity mate) {
        return null;
    }

    public void tick() {
        super.tick();
        if(this.getHeldItem(Hand.MAIN_HAND)!=ItemStack.EMPTY){//dataManager.get(FORGE)){
            ticks++;
            if(ticks==100){
                dataManager.set(FORGE, false);
                if(!dataManager.get(STATE)) this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
                forgeItem.setDamage((forgeItem.getDamage()-forgeItem.getDamage()/2));
                this.entityDropItem(forgeItem);
                this.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
                ticks=0;
            }
        }
        if(dataManager.get(ATTACK)){
            ticks++;
            if(ticks==22){
                ticks=0;
                dataManager.set(ATTACK, false);
            }
        }
    }

    public ActionResultType getEntityInteractionResult(PlayerEntity playerIn, Hand hand) {
        ItemStack itemstack = playerIn.getHeldItem(hand);

        if (this.world.isRemote) {
            boolean flag = this.isOwner(playerIn);
            return flag ? ActionResultType.CONSUME : ActionResultType.PASS;
        }else{
            int day=(int)(this.world.getDayTime()/24000);
            if(itemstack.isRepairable() && dataManager.get(DAY)!=day){
                dataManager.set(DAY, day);
                dataManager.set(FORGE, true);
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
                forgeItem=itemstack.copy();
                this.setHeldItem(Hand.MAIN_HAND, forgeItem);
                itemstack.shrink(1);
                return ActionResultType.SUCCESS;
            }else if(itemstack.isRepairable()){
                if(!this.world.isRemote) this.world.setEntityState(this, (byte)13);
                return ActionResultType.SUCCESS;
            }else if(this.isOwner(playerIn)){
                this.setSitting(!dataManager.get(STATE));
            }
            return super.getEntityInteractionResult(playerIn, hand);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 13) {
            this.spawnParticles(ParticleTypes.ANGRY_VILLAGER);
        } else {
            super.handleStatusUpdate(id);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void spawnParticles(IParticleData particleData) {
        for(int i = 0; i < 5; ++i) {
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.world.addParticle(particleData, this.getPosXRandom(1.0D), this.getPosYRandom(), this.getPosZRandom(1.0D), d0, d1, d2);
        }
    }

    @Override
    public void setSitting(boolean sit) {
        this.dataManager.set(STATE, sit);
        super.setSitting(sit);
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("day", this.dataManager.get(DAY));
     }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.dataManager.set(STATE, compound.getBoolean("Sitting"));
        this.dataManager.set(DAY, compound.getInt("day"));
    }

    protected SoundEvent getAmbientSound() { return SoundEvents.ENTITY_EVOKER_AMBIENT; }

    protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_EVOKER_DEATH; }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SoundEvents.ENTITY_EVOKER_HURT; }
}