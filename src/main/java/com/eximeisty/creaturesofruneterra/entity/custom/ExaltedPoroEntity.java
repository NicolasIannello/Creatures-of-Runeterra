package com.eximeisty.creaturesofruneterra.entity.custom;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class ExaltedPoroEntity extends TameableEntity implements IAnimatable{
    private AnimationFactory factory = new AnimationFactory(this);
    private static final DataParameter<Boolean> STATE = EntityDataManager.createKey(PoroEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CAST = EntityDataManager.createKey(PoroEntity.class, DataSerializers.BOOLEAN);
    private static final AnimationBuilder IDLE_ANIM = new AnimationBuilder().addAnimation("animation.exaltedporo.idle", true);
    private static final AnimationBuilder WALK_ANIM = new AnimationBuilder().addAnimation("animation.exaltedporo.walk", true);
    private static final AnimationBuilder SIT_ANIM = new AnimationBuilder().addAnimation("animation.exaltedporo.sit", true);
    private static final AnimationBuilder CAST_ANIM = new AnimationBuilder().addAnimation("animation.exaltedporo.cast", false);
    private int ticks=0;
    private int cd;
    private PlayerEntity toCast;
    private List<Effect> effects = Lists.newArrayList(ForgeRegistries.POTIONS.getValues());

    public ExaltedPoroEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setTamed(false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 15)
        .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.30)
        .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2)
        .createMutableAttribute(Attributes.ARMOR, 2)
        .createMutableAttribute(Attributes.ARMOR_TOUGHNESS, 0)
        .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)));
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(!dataManager.get(CAST)){
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
        if(dataManager.get(CAST)){
            event.getController().setAnimation(CAST_ANIM);
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
        return this.factory;
    }

    protected void registerData() {
        super.registerData();
        dataManager.register(STATE, false);
        dataManager.register(CAST, false);
    }

    public void tick() {
        super.tick();
        if(cd>0) cd--;
        if(dataManager.get(CAST)){
            ticks++;
            if(!this.world.isRemote && ticks%10==0) this.world.setEntityState(this, (byte)14);
            if(ticks==100){
                ticks=0;
                if(!this.world.isRemote) toCast.addPotionEffect(new EffectInstance(effects.get(new Random().nextInt(effects.size()-1)), 20*60));
                dataManager.set(CAST, false);
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3);
            }
        }
    }

    public ActionResultType getEntityInteractionResult(PlayerEntity playerIn, Hand hand) {
        ItemStack itemstack = playerIn.getHeldItem(hand);
        Item item = itemstack.getItem();

        if (this.world.isRemote) {
            boolean flag = this.isOwner(playerIn);
            return flag ? ActionResultType.CONSUME : ActionResultType.PASS;
        }else{
            if(item==Items.GOLD_INGOT){
                if(cd<=0){
                    this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
                    itemstack.shrink(1);
                    toCast=playerIn;
                    cd=20*120;
                    dataManager.set(CAST, true);
                }else{
                    if(!this.world.isRemote) this.world.setEntityState(this, (byte)13);
                }
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
            this.spawnParticles(ParticleTypes.ANGRY_VILLAGER,0);
        }else if (id == 14) {
            this.spawnParticles(ParticleTypes.HAPPY_VILLAGER,1);
        }else {
            super.handleStatusUpdate(id);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void spawnParticles(IParticleData particleData, int y) {
        for(int i = 0; i < 5; ++i) {
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.world.addParticle(particleData, this.getPosXRandom(1.0D), this.getPosYRandom()+y, this.getPosZRandom(1.0D), d0, d1, d2);
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
        cd=compound.getInt("cd");
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("cd", cd);
    }

    protected void dropInventory() {
        super.dropInventory();
        this.entityDropItem(new ItemStack(Items.GOLD_NUGGET, 4));
    }
    
    @Override
    public AgeableEntity createChild(ServerWorld world, AgeableEntity mate) { return null; }
    protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_BAT_DEATH; }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SoundEvents.ENTITY_FOX_HURT; }
    public SoundEvent getAmbientSound() { return SoundEvents.ENTITY_FOX_AMBIENT; }
}