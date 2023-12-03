package com.eximeisty.creaturesofruneterra.entity.custom;

import com.eximeisty.creaturesofruneterra.entity.ModEntities;
import com.eximeisty.creaturesofruneterra.util.KeyBinding;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.UUID;

public class SilverwingEntity extends TamableAnimal implements GeoEntity, Saddleable, PlayerRideable {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final int TICKS_PER_FLAP = Mth.ceil(24.166098F);
    Vec3 moveTargetPoint = Vec3.ZERO;
    BlockPos anchorPoint = BlockPos.ZERO;
    Integer groundTicks = 0;
    Integer flyTicks = 0;
    Integer targetTicks = 0;
    Boolean land = isTame();
    SilverwingEntity.AttackPhase attackPhase = SilverwingEntity.AttackPhase.CIRCLE;
    public static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(SilverwingEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> STATE = SynchedEntityData.defineId(SilverwingEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(SilverwingEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(SilverwingEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> SADDLED = SynchedEntityData.defineId(SilverwingEntity.class, EntityDataSerializers.BYTE);
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.CHICKEN, Items.BEEF, Items.COD, Items.MUTTON, Items.PORKCHOP, Items.RABBIT, Items.SALMON);
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(()-> itemHandler);
    private static final RawAnimation WALK = RawAnimation.begin().then("animation.silverwing.walk", Animation.LoopType.LOOP);
    private static final RawAnimation IDLE_GROUND = RawAnimation.begin().then("animation.silverwing.idle_ground", Animation.LoopType.LOOP);
    private static final RawAnimation FLY = RawAnimation.begin().then("animation.silverwing.fly", Animation.LoopType.LOOP);
    private static final RawAnimation IDLE_FLY = RawAnimation.begin().then("animation.silverwing.fly_place", Animation.LoopType.LOOP);

    static enum AttackPhase {
        CIRCLE,
        SWOOP;
    }

    public SilverwingEntity(EntityType<? extends TamableAnimal> p_21803_, Level p_21804_) {
        super(p_21803_, p_21804_);
        this.moveControl = new SilverwingEntity.PhantomMoveControl(this);
        this.lookControl = new SilverwingEntity.PhantomLookControl(this);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(1, new SilverwingEntity.PhantomAttackStrategyGoal());
        this.goalSelector.addGoal(2, new SilverwingEntity.PhantomSweepAttackGoal());
        this.goalSelector.addGoal(3, new SilverwingEntity.PhantomCircleAroundAnchorGoal());
        this.targetSelector.addGoal(1, new SilverwingEntity.PhantomTargetGoal());
        this.targetSelector.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
    }

    public static AttributeSupplier setAttributes(){
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.MOVEMENT_SPEED, 1)
                .add(Attributes.ATTACK_DAMAGE, 7)
                .add(Attributes.FOLLOW_RANGE, 70)
                .add(Attributes.ATTACK_KNOCKBACK, 0)
                .add(Attributes.ATTACK_SPEED, 0.8).build();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(SIZE, 1F);
        entityData.define(STATE, false);
        entityData.define(CLIMBING, (byte)0);
        entityData.define(SADDLED, (byte)0);
        entityData.define(FLYING, false);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        SilverwingEntity silverwing = ModEntities.SILVERWING.get().create(p_146743_);
        if (silverwing != null) {
            UUID uuid = this.getOwnerUUID();
            if (uuid != null) {
                silverwing.setOwnerUUID(uuid);
                silverwing.setTame(true);
            }
        }

        return silverwing;
    }

    public boolean isFlapping() {
        return (this.getUniqueFlapTickOffset() + this.tickCount) % TICKS_PER_FLAP == 0;
    }

    public int getUniqueFlapTickOffset() {
        return this.getId() * 3;
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            float f = Mth.cos((float)(this.getUniqueFlapTickOffset() + this.tickCount) * 7.448451F * ((float)Math.PI / 180F) + (float)Math.PI);
            float f1 = Mth.cos((float)(this.getUniqueFlapTickOffset() + this.tickCount + 1) * 7.448451F * ((float)Math.PI / 180F) + (float)Math.PI);
            if (f > 0.0F && f1 <= 0.0F && !this.onGround()) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.PHANTOM_FLAP, this.getSoundSource(), 4.5F + this.random.nextFloat() * 0.05F, 0.2F + this.random.nextFloat() * 0.05F, false);
            }
        }
        if (!this.level().isClientSide) {
            this.setBesideClimbableBlock(this.horizontalCollision);
        }
        if(!onGround() && !entityData.get(FLYING)) entityData.set(FLYING, true);
        if(onGround() && entityData.get(FLYING)) entityData.set(FLYING, false);
    }

    public InteractionResult mobInteract(Player playerIn, InteractionHand hand) {
        ItemStack itemstack = playerIn.getItemInHand(hand);
        Item item = itemstack.getItem();

        if (this.level().isClientSide) {
            boolean flag = this.isOwnedBy(playerIn) || this.isTame() || FOOD_ITEMS.test(itemstack) && !this.isTame();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        }else if(!playerIn.isCrouching()){
            if(!this.isTame() && FOOD_ITEMS.test(itemstack)) {
                if (!playerIn.getAbilities().instabuild) itemstack.shrink(1);

                if (this.random.nextInt(5) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, playerIn)) {
                    this.tame(playerIn);
                    this.navigation.stop();
                    this.setOrderedToSit(true);
                    this.level().broadcastEntityEvent(this, (byte)7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte)6);
                }
                return InteractionResult.SUCCESS;
            }
            if(this.isTame() && this.isOwnedBy(playerIn)){
                if(FOOD_ITEMS.test(itemstack)){
                    entityData.set(SIZE, (entityData.get(SIZE)+0.05F));
                    return InteractionResult.SUCCESS;
                }
                if (this.canWearArmor() && this.isArmor(itemstack) && !this.isWearingArmor()) {
                    this.equipArmor(playerIn, itemstack);
                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }
                this.doPlayerRide(playerIn);
                return InteractionResult.sidedSuccess(this.level().isClientSide);

            }
        }
        this.setOrderedToSit(!entityData.get(STATE));
        return super.mobInteract(playerIn, hand);
    }

    @Override
    public void tickLeash(){
        if(isTame() && !land && isLeashed()) land=true;
        super.tickLeash();
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.entityData.set(STATE, sit);
        super.setOrderedToSit(sit);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(STATE, compound.getBoolean("Sitting"));
        this.entityData.set(SIZE, compound.getFloat("size"));
        this.entityData.set(SADDLED, compound.getByte("saddled"));
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("size", this.entityData.get(SIZE));
        compound.putByte("saddled", this.entityData.get(SADDLED));
    }

    public boolean canWearArmor() {
        return true;
    }

    public boolean isArmor(ItemStack p_30731_) {
        return p_30731_.getItem() instanceof HorseArmorItem;
    }

    public boolean isWearingArmor() {
        return !this.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
    }

    public void equipArmor(Player p_251330_, ItemStack p_248855_) {
        if (this.isArmor(p_248855_)) {
            //this.inventory.setItem(1, p_248855_.copyWithCount(1));
            if (!p_251330_.getAbilities().instabuild) {
                p_248855_.shrink(1);
            }
        }

    }

    protected void dropEquipment() {
        super.dropEquipment();
//        if (this.inventory != null) {
//            for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
//                ItemStack itemstack = this.inventory.getItem(i);
//                if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack)) {
//                    this.spawnAtLocation(itemstack);
//                }
//            }
//        }
    }
    //FLYING------------------------------------------------------------------------------------------------------------
    protected void checkFallDamage(double p_20809_, boolean p_20810_, BlockState p_20811_, BlockPos p_20812_) {
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


    class PhantomTargetGoal extends Goal {
        private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0D);
        private int nextScanTick = reducedTickDelay(20);

        public boolean canUse() {
            if(isTame()) return false;
            if (this.nextScanTick > 0) {
                --this.nextScanTick;
                return false;
            } else {
                this.nextScanTick = reducedTickDelay(60);
                Animal target = SilverwingEntity.this.level().getNearestEntity(Animal.class, attackTargeting, SilverwingEntity.this, SilverwingEntity.this.getX(), SilverwingEntity.this.getY(), SilverwingEntity.this.getZ(), SilverwingEntity.this.getBoundingBox().inflate(16.0D, 64.0D, 16.0D));
                if(target!=null){
                    if (SilverwingEntity.this.canAttack(target, TargetingConditions.DEFAULT)) {
                        SilverwingEntity.this.setTarget(target);
                        return true;
                    }
                }
                return false;
            }
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = SilverwingEntity.this.getTarget();
            return livingentity != null ? SilverwingEntity.this.canAttack(livingentity, TargetingConditions.DEFAULT) : false;
        }
    }

    class PhantomAttackStrategyGoal extends Goal {
        private int nextSweepTick;

        public boolean canUse() {
            LivingEntity livingentity = SilverwingEntity.this.getTarget();
            return livingentity != null ? SilverwingEntity.this.canAttack(livingentity, TargetingConditions.DEFAULT) : false;
        }

        public void start() {
            this.nextSweepTick = this.adjustedTickDelay(10);
            SilverwingEntity.this.attackPhase = SilverwingEntity.AttackPhase.CIRCLE;
            this.setAnchorAboveTarget();
        }

        public void stop() {
            SilverwingEntity.this.anchorPoint = SilverwingEntity.this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, SilverwingEntity.this.anchorPoint).above(20 + SilverwingEntity.this.random.nextInt(20));
        }

        public void tick() {
            if (SilverwingEntity.this.attackPhase == SilverwingEntity.AttackPhase.CIRCLE) {
                --this.nextSweepTick;
                if (this.nextSweepTick <= 0) {
                    SilverwingEntity.this.attackPhase = SilverwingEntity.AttackPhase.SWOOP;
                    this.setAnchorAboveTarget();
                    this.nextSweepTick = this.adjustedTickDelay((8 + SilverwingEntity.this.random.nextInt(4)) * 20);
                }
            }

        }

        private void setAnchorAboveTarget() {
            SilverwingEntity.this.anchorPoint = SilverwingEntity.this.getTarget().blockPosition().above(20 + SilverwingEntity.this.random.nextInt(20));
            if (SilverwingEntity.this.anchorPoint.getY() < SilverwingEntity.this.level().getSeaLevel()) {
                SilverwingEntity.this.anchorPoint = new BlockPos(SilverwingEntity.this.anchorPoint.getX(), SilverwingEntity.this.level().getSeaLevel() + 1, SilverwingEntity.this.anchorPoint.getZ());
            }

        }
    }

    class PhantomCircleAroundAnchorGoal extends SilverwingEntity.PhantomMoveTargetGoal {
        private float angle;
        private float distance;
        private float height;
        private float clockwise;

        public boolean canUse() {
            return SilverwingEntity.this.getTarget() == null || SilverwingEntity.this.attackPhase == SilverwingEntity.AttackPhase.CIRCLE;
        }

        public void start() {
            this.distance = 5.0F + SilverwingEntity.this.random.nextFloat() * 10.0F;
            this.height = -4.0F + SilverwingEntity.this.random.nextFloat() * 9.0F;
            this.clockwise = SilverwingEntity.this.random.nextBoolean() ? 1.0F : -1.0F;
            this.selectNext();
        }

        public void tick() {
            if (SilverwingEntity.this.random.nextInt(this.adjustedTickDelay(350)) == 0) {
                this.height = -4.0F + SilverwingEntity.this.random.nextFloat() * 9.0F;
            }

            if (SilverwingEntity.this.random.nextInt(this.adjustedTickDelay(250)) == 0) {
                ++this.distance;
                if (this.distance > 15.0F) {
                    this.distance = 5.0F;
                    this.clockwise = -this.clockwise;
                }
            }

            if (SilverwingEntity.this.random.nextInt(this.adjustedTickDelay(450)) == 0) {
                this.angle = SilverwingEntity.this.random.nextFloat() * 2.0F * (float)Math.PI;
                this.selectNext();
            }

            if (this.touchingTarget()) {
                this.selectNext();
            }

            if (SilverwingEntity.this.moveTargetPoint.y < SilverwingEntity.this.getY() && !SilverwingEntity.this.level().isEmptyBlock(SilverwingEntity.this.blockPosition().below(1))) {
                this.height = Math.max(1.0F, this.height);
                this.selectNext();
            }

            if (SilverwingEntity.this.moveTargetPoint.y > SilverwingEntity.this.getY() && !SilverwingEntity.this.level().isEmptyBlock(SilverwingEntity.this.blockPosition().above(1))) {
                this.height = Math.min(-1.0F, this.height);
                this.selectNext();
            }

        }

        private void selectNext() {
            if (BlockPos.ZERO.equals(SilverwingEntity.this.anchorPoint)) {
                SilverwingEntity.this.anchorPoint = SilverwingEntity.this.blockPosition();
            }

            this.angle += this.clockwise * 15.0F * ((float)Math.PI / 180F);
            SilverwingEntity.this.moveTargetPoint = Vec3.atLowerCornerOf(SilverwingEntity.this.anchorPoint).add((double)(this.distance * Mth.cos(this.angle)), (double)(-4.0F + this.height), (double)(this.distance * Mth.sin(this.angle)));
        }
    }

    class PhantomLookControl extends LookControl {
        public PhantomLookControl(Mob p_33235_) {
            super(p_33235_);
        }

        public void tick() {
        }
    }

    class PhantomMoveControl extends MoveControl {
        private float speed = 0.3F;

        public PhantomMoveControl(Mob p_33241_) {
            super(p_33241_);
        }

        public void tick() {
            if (SilverwingEntity.this.horizontalCollision) {
                SilverwingEntity.this.setYRot(SilverwingEntity.this.getYRot() + 180.0F);
                this.speed = 0.3F;
            }

            double d0 = SilverwingEntity.this.moveTargetPoint.x - SilverwingEntity.this.getX();
            double d1 = SilverwingEntity.this.moveTargetPoint.y - SilverwingEntity.this.getY();
            double d2 = SilverwingEntity.this.moveTargetPoint.z - SilverwingEntity.this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            if (Math.abs(d3) > (double)1.0E-5F && !isOrderedToSit()) {
                double d4 = 1.0D - Math.abs(d1 * (double)0.7F) / d3;
                d0 *= d4;
                d2 *= d4;
                d3 = Math.sqrt(d0 * d0 + d2 * d2);
                double d5 = Math.sqrt(d0 * d0 + d2 * d2 + d1 * d1);
                float f = SilverwingEntity.this.getYRot();
                float f1 = (float)Mth.atan2(d2, d0);
                float f2 = Mth.wrapDegrees(SilverwingEntity.this.getYRot() + 90.0F);
                float f3 = Mth.wrapDegrees(f1 * (180F / (float)Math.PI));
                SilverwingEntity.this.setYRot(Mth.approachDegrees(f2, f3, 4.0F) - 90.0F);
                SilverwingEntity.this.yBodyRot = SilverwingEntity.this.getYRot();
                if (Mth.degreesDifferenceAbs(f, SilverwingEntity.this.getYRot()) < 3.0F) {
                    this.speed = Mth.approach(this.speed, 1.8F, 0.005F * (1.8F / this.speed));
                } else {
                    this.speed = Mth.approach(this.speed, 0.2F, 0.025F);
                }

                float f4 = (float)(-(Mth.atan2(-d1, d3) * (double)(180F / (float)Math.PI)));
                SilverwingEntity.this.setXRot(f4);
                float f5 = SilverwingEntity.this.getYRot() + 90.0F;
                double d6 = (double)(this.speed * Mth.cos(f5 * ((float)Math.PI / 180F))) * Math.abs(d0 / d5);
                double d7 = (double)(this.speed * Mth.sin(f5 * ((float)Math.PI / 180F))) * Math.abs(d2 / d5);
                double d8 = (double)(this.speed * Mth.sin(f4 * ((float)Math.PI / 180F))) * Math.abs(d1 / d5);
                Vec3 vec3 = SilverwingEntity.this.getDeltaMovement();
                double y = (!SilverwingEntity.this.land) ? d8 : 0;
                System.out.println(SilverwingEntity.this.groundTicks+" "+SilverwingEntity.this.land);
                if(SilverwingEntity.this.onGround()) {
                    SilverwingEntity.this.groundTicks++;
                    if ((SilverwingEntity.this.groundTicks > 1000 && !SilverwingEntity.this.isTame()) || (SilverwingEntity.this.groundTicks > 2500 && SilverwingEntity.this.isTame()) ) {
                        SilverwingEntity.this.land = false;
                        SilverwingEntity.this.groundTicks = 0;
                        SilverwingEntity.this.anchorPoint = SilverwingEntity.this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, SilverwingEntity.this.anchorPoint).above(20 + SilverwingEntity.this.random.nextInt(20));
                    }
                }
                if(SilverwingEntity.this.getTarget()==null && !SilverwingEntity.this.isTame()) {
                    SilverwingEntity.this.targetTicks++;
                    if (SilverwingEntity.this.targetTicks > 2500) {
                        SilverwingEntity.this.targetTicks = 0;
                        SilverwingEntity.this.anchorPoint = SilverwingEntity.this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, SilverwingEntity.this.anchorPoint).above(20 + SilverwingEntity.this.random.nextInt(20)).east(SilverwingEntity.this.random.nextInt(40)).south(SilverwingEntity.this.random.nextInt(40));
                    }
                }
                if(!SilverwingEntity.this.onGround() && SilverwingEntity.this.getTarget()==null){
                    SilverwingEntity.this.flyTicks++;
                    if ((SilverwingEntity.this.flyTicks > 10000 && !SilverwingEntity.this.isTame()) || (SilverwingEntity.this.flyTicks > 700 && SilverwingEntity.this.isTame()) ) {
                        SilverwingEntity.this.flyTicks = 0;
                        SilverwingEntity.this.land=true;
                        SilverwingEntity.this.anchorPoint = SilverwingEntity.this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, SilverwingEntity.this.blockPosition());
                    }
                }
                SilverwingEntity.this.setDeltaMovement(vec3.add((new Vec3(d6, y, d7)).subtract(vec3).scale(0.2D)));
            }
            if(isOrderedToSit() && (SilverwingEntity.this.flyTicks!=0 ||SilverwingEntity.this.groundTicks!=0 || !SilverwingEntity.this.land)){
                SilverwingEntity.this.flyTicks = 0;
                SilverwingEntity.this.groundTicks = 0;
                SilverwingEntity.this.land=true;
                SilverwingEntity.this.anchorPoint = SilverwingEntity.this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, SilverwingEntity.this.blockPosition());
            }
        }
    }

    abstract class PhantomMoveTargetGoal extends Goal {
        public PhantomMoveTargetGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        protected boolean touchingTarget() {
            return SilverwingEntity.this.moveTargetPoint.distanceToSqr(SilverwingEntity.this.getX(), SilverwingEntity.this.getY(), SilverwingEntity.this.getZ()) < 4.0D;
        }
    }

    class PhantomSweepAttackGoal extends SilverwingEntity.PhantomMoveTargetGoal {

        public boolean canUse() {
            return SilverwingEntity.this.getTarget() != null && SilverwingEntity.this.attackPhase == SilverwingEntity.AttackPhase.SWOOP;
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = SilverwingEntity.this.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                if (livingentity instanceof Player) {
                    Player player = (Player)livingentity;
                    if (livingentity.isSpectator() || player.isCreative()) {
                        return false;
                    }
                }
                if (!this.canUse()) {
                    return false;
                } else {
                    return true;
                }
            }
        }

        public void start() {
        }

        public void stop() {
            SilverwingEntity.this.setTarget((LivingEntity)null);
            SilverwingEntity.this.attackPhase = SilverwingEntity.AttackPhase.CIRCLE;
        }

        public void tick() {
            LivingEntity livingentity = SilverwingEntity.this.getTarget();
            if (livingentity != null) {
                SilverwingEntity.this.moveTargetPoint = new Vec3(livingentity.getX(), livingentity.getY(0.5D), livingentity.getZ());
                if (SilverwingEntity.this.getBoundingBox().inflate((double)0.2F).intersects(livingentity.getBoundingBox())) {
                    SilverwingEntity.this.doHurtTarget(livingentity);
                    SilverwingEntity.this.attackPhase = SilverwingEntity.AttackPhase.CIRCLE;
                    if (!SilverwingEntity.this.isSilent()) {
                        SilverwingEntity.this.level().levelEvent(1039, SilverwingEntity.this.blockPosition(), 0);
                    }
                } else if (SilverwingEntity.this.horizontalCollision || SilverwingEntity.this.hurtTime > 0) {
                    SilverwingEntity.this.attackPhase = SilverwingEntity.AttackPhase.CIRCLE;
                }
            }
        }
    }
    //END---FLYING------------------------------------------------------------------------------------------------------

    //MOUNT-------------------------------------------------------------------------------------------------------------
    @Override
    public boolean isSaddleable() {
        return this.isAlive() && this.isTame();
    }

    @Override
    public void equipSaddle(@Nullable SoundSource p_21748_) {
        itemHandler.setStackInSlot(0, new ItemStack(Items.SADDLE));
        byte b0 = (byte)(this.entityData.get(SADDLED) | 1);
        this.entityData.set(SADDLED, b0);
    }

    @Override
    public boolean isSaddled() {
        return (this.entityData.get(SADDLED) & 1) != 0;
    }

    protected void doPlayerRide(Player p_30634_) {
        if (!this.level().isClientSide) {
            p_30634_.setYRot(this.getYRot());
            p_30634_.setXRot(this.getXRot());
            p_30634_.startRiding(this);
        }

    }

    public boolean isImmobile() {
        return super.isImmobile() && this.isVehicle() && this.isSaddled();
    }

    protected void tickRidden(Player p_278233_, Vec3 p_275693_) {
        super.tickRidden(p_278233_, p_275693_);
        Vec2 vec2 = this.getRiddenRotation(p_278233_);
        this.setRot(vec2.y, vec2.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        if(level().isClientSide){
            if(KeyBinding.FLY_UP.isDown()){
                addDeltaMovement(new Vec3(0, -getDeltaMovement().y+0.2, 0));
            }else if(KeyBinding.FLY_DOWN.isDown()){
                addDeltaMovement(new Vec3(0, -getDeltaMovement().y-0.2, 0));
            }else if(getDeltaMovement().y<0){
                addDeltaMovement(new Vec3(0, -getDeltaMovement().y-0.001, 0));
            }
        }
    }

    protected Vec2 getRiddenRotation(LivingEntity p_275502_) {
        return new Vec2(p_275502_.getXRot() * 0.5F, p_275502_.getYRot());
    }

    protected Vec3 getRiddenInput(Player p_278278_, Vec3 p_275506_) {
        if(onGround() || isInWater()){
            float f = p_278278_.xxa * 0.5F;
            float f1 = p_278278_.zza;
            if (f1 <= 0.0F) f1 *= 0.25F;
            return new Vec3((double)f, 0.0D, (double)f1).scale(0.1);
        }else{
            float f = p_278278_.xxa * 8.5F;
            float f1 = p_278278_.zza * 8;
            if (f1 <= 0.0F) f1 *= 0.25F;
            return new Vec3(f, 0.0D, f1);
        }
    }

    protected float getRiddenSpeed(Player p_278336_) {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    protected void positionRider(Entity p_289569_, Entity.MoveFunction p_289558_) {
        if (this.hasPassenger(p_289569_)) {
            double d0 = this.getY() + this.getPassengersRidingOffset() + p_289569_.getMyRidingOffset();
            if(!onGround() && (getDeltaMovement().x!=0 || getDeltaMovement().z!=0)) d0-=0.5;
            p_289558_.accept(p_289569_, this.getX(), d0, this.getZ());
        }
    }

    @javax.annotation.Nullable
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        if (entity instanceof Mob) {
            return (Mob)entity;
        } else {
            if (this.isSaddled()) {
                entity = this.getFirstPassenger();
                if (entity instanceof Player) {
                    return (Player)entity;
                }
            }

            return null;
        }
    }

    @javax.annotation.Nullable
    private Vec3 getDismountLocationInDirection(Vec3 p_30562_, LivingEntity p_30563_) {
        SilverwingEntity.this.anchorPoint = SilverwingEntity.this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, SilverwingEntity.this.blockPosition());
        double d0 = this.getX() + p_30562_.x;
        double d1 = this.getBoundingBox().minY;
        double d2 = this.getZ() + p_30562_.z;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(Pose pose : p_30563_.getDismountPoses()) {
            blockpos$mutableblockpos.set(d0, d1, d2);
            double d3 = this.getBoundingBox().maxY + 0.75D;

            while(true) {
                double d4 = this.level().getBlockFloorHeight(blockpos$mutableblockpos);
                if ((double)blockpos$mutableblockpos.getY() + d4 > d3) {
                    break;
                }

                if (DismountHelper.isBlockFloorValid(d4)) {
                    AABB aabb = p_30563_.getLocalBoundsForPose(pose);
                    Vec3 vec3 = new Vec3(d0, (double)blockpos$mutableblockpos.getY() + d4, d2);
                    if (DismountHelper.canDismountTo(this.level(), p_30563_, aabb.move(vec3))) {
                        p_30563_.setPose(pose);
                        return vec3;
                    }
                }

                blockpos$mutableblockpos.move(Direction.UP);
                if (!((double)blockpos$mutableblockpos.getY() < d3)) {
                    break;
                }
            }
        }

        return null;
    }

    public Vec3 getDismountLocationForPassenger(LivingEntity p_30576_) {
        Vec3 vec3 = getCollisionHorizontalEscapeVector((double)this.getBbWidth(), (double)p_30576_.getBbWidth(), this.getYRot() + (p_30576_.getMainArm() == HumanoidArm.RIGHT ? 90.0F : -90.0F));
        Vec3 vec31 = this.getDismountLocationInDirection(vec3, p_30576_);
        if (vec31 != null) {
            return vec31;
        } else {
            Vec3 vec32 = getCollisionHorizontalEscapeVector((double)this.getBbWidth(), (double)p_30576_.getBbWidth(), this.getYRot() + (p_30576_.getMainArm() == HumanoidArm.LEFT ? 90.0F : -90.0F));
            Vec3 vec33 = this.getDismountLocationInDirection(vec32, p_30576_);
            return vec33 != null ? vec33 : this.position();
        }
    }
    //END---MOUNT-------------------------------------------------------------------------------------------------------
    private ItemStackHandler createHandler(){
        return new ItemStackHandler(2){
//            @Override
//            protected void onContentsChanged(int slot){
//                if(slot==15) changeItem(InteractionHand.MAIN_HAND, slot);
//                if(slot==20) changeItem(InteractionHand.OFF_HAND, slot);
//            }

            @Override @Deprecated
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                switch (slot) {
                    case 0: return stack.getItem()==Items.SADDLE;
                    case 1: return isArmor(stack);
                    default: return super.isItemValid(slot, stack);
                }
            }

            @Override
            public int getSlotLimit(int slot) {
                switch (slot) {
                    case 0, 1: return 1;
                    default: return super.getSlotLimit(slot);
                }
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side){
        if(cap == ForgeCapabilities.ITEM_HANDLER) return handler.cast();
        return super.getCapability(cap, side);
    }

    public <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState)  {
        if (tAnimationState.isMoving()) {
            RawAnimation anim= entityData.get(FLYING) ? FLY : WALK;
            tAnimationState.getController().setAnimation(anim);
            return PlayState.CONTINUE;
        }
        RawAnimation anim= entityData.get(FLYING) ? IDLE_FLY : IDLE_GROUND;
        tAnimationState.getController().setAnimation(anim);
        return PlayState.CONTINUE;
    }

//    public <T extends GeoAnimatable> PlayState predicate2(AnimationState<T> tAnimationState)  {
//        tAnimationState.getController().forceAnimationReset();
//        return PlayState.STOP;
//    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers){
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
        //controllers.add(new AnimationController<>(this, "attacks", 0, this::predicate2));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}