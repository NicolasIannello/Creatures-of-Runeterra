package com.eximeisty.creaturesofruneterra.entity.custom;

import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import com.eximeisty.creaturesofruneterra.item.ModItems;
import com.eximeisty.creaturesofruneterra.util.ModSoundEvents;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class NaafiriEntity extends TameableEntity implements IAnimatable {
    private final AnimationFactory cache = new AnimationFactory(this);
    private static final DataParameter<Boolean> STATE = EntityDataManager.createKey(NaafiriEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Integer> ATTACK = EntityDataManager.createKey(NaafiriEntity.class, DataSerializers.VARINT);
    private int ticks=0;
    private int ticksHound=0;
    private int ticksDagger=0;
    private static final AnimationBuilder IDLE_ANIM = new AnimationBuilder().addAnimation("animation.naafiri.idle", true);
    private static final AnimationBuilder WALK_ANIM = new AnimationBuilder().addAnimation("animation.naafiri.walk", true);
    private static final AnimationBuilder SIT_ANIM = new AnimationBuilder().addAnimation("animation.naafiri.sit", true);
    private static final AnimationBuilder ATTACK_ANIM = new AnimationBuilder().addAnimation("animation.naafiri.bite", false);
    private static final AnimationBuilder SUMMON_ANIM = new AnimationBuilder().addAnimation("animation.naafiri.spawn", false);

    public NaafiriEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setTamed(false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 30)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.33)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 12)
                .createMutableAttribute(Attributes.ARMOR, 7)
                .createMutableAttribute(Attributes.ARMOR_TOUGHNESS, 7)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true){
            @Override
            protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
                double d0 = this.getAttackReachSqr(enemy);
                if(d0 >= 5 && this.attacker.getDataManager().get(ATTACK)==0 && ((NaafiriEntity)this.attacker).ticksHound==0){
                    this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
                    this.attacker.getDataManager().set(ATTACK, 2);
                }
                if(d0 >= 3 && this.attacker.getDataManager().get(ATTACK)==0 && ((NaafiriEntity)this.attacker).ticksDagger==0){
                    this.attacker.getDataManager().set(ATTACK, 4);
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
        if(dataManager.get(ATTACK)==1){
            event.getController().setAnimation(ATTACK_ANIM);
            return PlayState.CONTINUE;
        }
        if(dataManager.get(ATTACK)==2){
            event.getController().setAnimation(SUMMON_ANIM);
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
        if(ticksHound>0 && getAttackTarget()!=null) ticksHound--;
        if(ticksDagger>0 && getAttackTarget()!=null) ticksDagger--;
        if(dataManager.get(ATTACK)==1){
            ticks++;
            if(ticks==5) this.world.playSound(null, new BlockPos(this.getPosX(), this.getPosY(), this.getPosZ()), ModSoundEvents.NAAFIRI_ATTACK.get(), SoundCategory.NEUTRAL, 0.5f, 1);
            if(ticks>15){
                ticks=0;
                dataManager.set(ATTACK, 0);
            }
        }
        if(dataManager.get(ATTACK)==2){
            ticks++;
            if(ticks==10) this.world.playSound(null, new BlockPos(this.getPosX(), this.getPosY(), this.getPosZ()), ModSoundEvents.NAAFIRI_SPAWN.get(), SoundCategory.NEUTRAL, 0.5f, 1);
            if(ticks==55 && !this.world.isRemote){
                NaafiriHoundEntity hound= (NaafiriHoundEntity) ModEntityTypes.NAAFIRI_HOUND.get().spawn(this.world.getServer().getWorld(world.getDimensionKey()), (ItemStack) null, null, new BlockPos(this.getPosX()+2, this.getPosY()+2, this.getPosZ()), SpawnReason.NATURAL, false, false);
                NaafiriHoundEntity hound2= (NaafiriHoundEntity) ModEntityTypes.NAAFIRI_HOUND.get().spawn(this.world.getServer().getWorld(world.getDimensionKey()), (ItemStack) null, null, new BlockPos(this.getPosX()-2, this.getPosY()+2, this.getPosZ()), SpawnReason.NATURAL, false, false);
                NaafiriHoundEntity hound3= (NaafiriHoundEntity) ModEntityTypes.NAAFIRI_HOUND.get().spawn(this.world.getServer().getWorld(world.getDimensionKey()), (ItemStack) null, null, new BlockPos(this.getPosX(), this.getPosY()+2, this.getPosZ()+2), SpawnReason.NATURAL, false, false);
                NaafiriHoundEntity hound4= (NaafiriHoundEntity) ModEntityTypes.NAAFIRI_HOUND.get().spawn(this.world.getServer().getWorld(world.getDimensionKey()), (ItemStack) null, null, new BlockPos(this.getPosX(), this.getPosY()+2, this.getPosZ()-2), SpawnReason.NATURAL, false, false);
                hound.setAttackTarget(getAttackTarget());hound2.setAttackTarget(getAttackTarget());hound3.setAttackTarget(getAttackTarget());hound4.setAttackTarget(getAttackTarget());
                hound.setTamedBy((PlayerEntity)(this).getOwner());hound2.setTamedBy((PlayerEntity)(this).getOwner());hound3.setTamedBy((PlayerEntity)(this).getOwner());hound4.setTamedBy((PlayerEntity)(this).getOwner());
            }
            if(ticks>70){
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.33);
                ticks=0;
                ticksHound=500;
                dataManager.set(ATTACK, 0);
            }
        }
        if(dataManager.get(ATTACK)==4){
            ticks++;
            if(ticks==8 && !this.world.isRemote && getAttackTarget()!=null){
                NaafiriDaggerEntity dagger= new NaafiriDaggerEntity(world, this);
                dagger.setHitSound(SoundEvents.BLOCK_BLASTFURNACE_FIRE_CRACKLE);
                dagger.setIsCritical(true);
                dagger.setNoGravity(true);
                double d0 = getAttackTarget().getPosX() - this.getPosX();
                double d1 = getAttackTarget().getPosY() - dagger.getPosY();
                double d2 = getAttackTarget().getPosZ() - this.getPosZ();
                double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                dagger.shoot(d0, (d1 + d3 * (double)0.2F)*0, d2, 1.6F, (float)(14 - this.world.getDifficulty().getId() * 4));
                this.world.addEntity(dagger);
            }
            if(ticks>15){
                ticks=0;
                ticksDagger=150;
                dataManager.set(ATTACK, 0);
            }
        }
    }

    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
    }

    public ActionResultType getEntityInteractionResult(PlayerEntity playerIn, Hand hand) {
        if (this.world.isRemote) {
            boolean flag = this.isOwner(playerIn) || this.isTamed() && !this.isTamed();
            return flag ? ActionResultType.CONSUME : ActionResultType.PASS;
        }else{
            ItemStack item = playerIn.getHeldItem(hand);
            if(item.isItemEqual(new ItemStack(ModItems.NAAFIRI.get()))){
                item.getAttributeModifiers(EquipmentSlotType.MAINHAND).forEach((atr, modifier) -> {
                    if (atr == Attributes.ATTACK_DAMAGE) {
                        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(modifier.getAmount()*4);
                        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(modifier.getAmount()+5);
                        this.getAttribute(Attributes.ARMOR).setBaseValue(modifier.getAmount());
                        this.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(modifier.getAmount());
                    }
                });
                if(!this.world.isRemote && ticks%10==0) this.world.setEntityState(this, (byte)14);
                this.setHealth(getMaxHealth());
                item.shrink(1);
                return ActionResultType.SUCCESS;
            }

            if(this.isTamed() && this.isOwner(playerIn)){
                this.setSitting(!dataManager.get(STATE));
            }
            return ActionResultType.SUCCESS;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 14) {
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
            this.world.addParticle(particleData, this.getPosXRandom(1.0D), this.getPosYRandom(), this.getPosZRandom(1.0D), d0, d1, d2);
        }
    }

    protected void registerData() {
        super.registerData();
        dataManager.register(STATE, false);
        dataManager.register(ATTACK, 0);
    }

    @Override
    public void setSitting(boolean sit) {
        this.dataManager.set(STATE, sit);
        super.setSitting(sit);
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.dataManager.set(STATE, compound.getBoolean("Sitting"));
        this.dataManager.set(ATTACK, compound.getInt("attack"));
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("attack", this.dataManager.get(ATTACK));
    }

    protected void dropInventory() {
        super.dropInventory();
        //this.world.addFreshEntity(new ItemEntity(this.level, this.getPosX(), this.getPosY(), this.getPosZ(), this.getItemInHand(InteractionHand.MAIN_HAND)));
    }

    protected SoundEvent getDeathSound() { return ModSoundEvents.NAAFIRI_DEATH.get(); }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return ModSoundEvents.NAAFIRI_HURT.get(); }
    public SoundEvent getAmbientSound() { return null; }
}