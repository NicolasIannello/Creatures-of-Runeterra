package com.eximeisty.creaturesofruneterra.entity.custom;

import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import com.eximeisty.creaturesofruneterra.util.KeyBindings;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class SilverwingEntity extends TameableEntity implements IAnimatable, IEquipable {
    private AnimationFactory factory = new AnimationFactory(this);
    public static final double TICKS_PER_FLAP = Math.ceil(24.166098F);
    Vector3d moveTargetPoint = Vector3d.ZERO;
    BlockPos anchorPoint = BlockPos.ZERO;
    Integer groundTicks = 0;
    Integer flyTicks = 0;
    Integer targetTicks = 0;
    Boolean land = isTamed();
    Integer partnerBiome = getBiome();
    Integer partnerBiomeLayer = getBiomeLayer();
    Integer partnerVariant = getVariant();
    Integer partnerColor = getVariantColor();
    AttackPhase attackPhase = AttackPhase.CIRCLE;
    public static final DataParameter<Float> SIZE = EntityDataManager.createKey(SilverwingEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> STATE = EntityDataManager.createKey(SilverwingEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(SilverwingEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> FLYING = EntityDataManager.createKey(SilverwingEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Byte> SADDLED = EntityDataManager.createKey(SilverwingEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(SilverwingEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(SilverwingEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> BIOME = EntityDataManager.createKey(SilverwingEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> BIOME_LAYER = EntityDataManager.createKey(SilverwingEntity.class, DataSerializers.VARINT);
    private static final Ingredient FOOD_ITEMS = Ingredient.fromItems(Items.CHICKEN, Items.BEEF, Items.COD, Items.MUTTON, Items.PORKCHOP, Items.RABBIT, Items.SALMON);
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(()-> itemHandler);
    private static final AnimationBuilder WALK = new AnimationBuilder().addAnimation("animation.silverwing.walk", true);
    private static final AnimationBuilder IDLE_GROUND = new AnimationBuilder().addAnimation("animation.silverwing.idle_ground", true);
    private static final AnimationBuilder FLY = new AnimationBuilder().addAnimation("animation.silverwing.fly", true);
    private static final AnimationBuilder IDLE_FLY = new AnimationBuilder().addAnimation("animation.silverwing.fly_place", true);
    private static final AnimationBuilder SIT = new AnimationBuilder().addAnimation("animation.silverwing.sit", true);
    private static final AnimationBuilder BABY_WALK = new AnimationBuilder().addAnimation("animation.silverwing.baby_walk", true);
    private static final AnimationBuilder BABY_IDLE = new AnimationBuilder().addAnimation("animation.silverwing.baby_idle", true);
    private static final AnimationBuilder BABY_SIT = new AnimationBuilder().addAnimation("animation.silverwing.baby_sit", true);

    static enum AttackPhase {
        CIRCLE,
        SWOOP,
        BREED;
    }

    public SilverwingEntity(EntityType<? extends TameableEntity> p_21803_, World p_21804_) {
        super(p_21803_, p_21804_);
        //this.fixupDimensions();
        this.moveController = new PhantomMovementController(this);
        this.lookController = new PhantomLookController(this);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(1, new PhantomAttackStrategyGoal());
        this.goalSelector.addGoal(2, new PhantomSweepAttackGoal());
        this.goalSelector.addGoal(3, new PhantomCircleAroundAnchorGoal());
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D){
            public boolean shouldExecute() {
                return isChild() && super.shouldExecute();
            }
        });
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D){
            public void spawnBaby() {
                SilverwingEntity.this.partnerVariant = ((SilverwingEntity)targetMate).getVariant();
                SilverwingEntity.this.partnerColor = ((SilverwingEntity)targetMate).getVariantColor();
                SilverwingEntity.this.partnerBiome = ((SilverwingEntity)targetMate).getBiome();
                SilverwingEntity.this.partnerBiomeLayer = ((SilverwingEntity)targetMate).getBiomeLayer();
                super.spawnBaby();
            }

            public boolean shouldContinueExecuting() {
                if(targetMate!=null) SilverwingEntity.this.attackPhase = AttackPhase.BREED;
                return  super.shouldContinueExecuting();
            }

            public void resetTask() {
                SilverwingEntity.this.attackPhase = AttackPhase.CIRCLE;
                super.resetTask();
            }

            public void tick(){
                LivingEntity livingentity = targetMate;
                if (livingentity != null) SilverwingEntity.this.moveTargetPoint = new Vector3d(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());
                super.tick();
            }
        });
        this.targetSelector.addGoal(1, new PhantomTargetGoal());
        this.targetSelector.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 30)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 1)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 7)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 70)
                .createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 0)
                .createMutableAttribute(Attributes.ATTACK_SPEED, 0.8);
    }

    protected void registerData() {
        super.registerData();
        dataManager.register(SIZE, 1F);
        dataManager.register(STATE, false);
        dataManager.register(CLIMBING, (byte)0);
        dataManager.register(SADDLED, (byte)0);
        dataManager.register(FLYING, false);
        dataManager.register(VARIANT, 0);
        dataManager.register(COLOR, 0);
        dataManager.register(BIOME, 0);
        dataManager.register(BIOME_LAYER, 0);
    }

    @Nullable
    @Override
    public AgeableEntity createChild(ServerWorld p_146743_, AgeableEntity mate) {
        SilverwingEntity silverwing = ModEntityTypes.SILVERWING.get().create(p_146743_);
        if (silverwing != null) {
            UUID uuid = this.getOwnerId();
            if (uuid != null) {
                int variant = world.getRandom().nextInt(2);
                int feathers = world.getRandom().nextInt(2);
                int type = world.getRandom().nextInt(4);

                switch (type) {
                    case 0 :
                        silverwing.setVariant(variant == 0 ? getVariant() : getVariantColor());
                        silverwing.setBiome(variant == 0 ? getBiome() : getBiomeLayer());
                        silverwing.setVariantColor(feathers == 0 ? getVariantColor() : getVariant());
                        silverwing.setBiomeLayer(feathers == 0 ? getBiomeLayer() : getBiome());
                        break;
                    case 1 :
                        silverwing.setVariant(variant == 0 ? partnerVariant : partnerColor);
                        silverwing.setBiome(variant == 0 ? partnerBiome : partnerBiomeLayer);
                        silverwing.setVariantColor(feathers == 0 ? partnerColor : partnerVariant);
                        silverwing.setBiomeLayer(feathers == 0 ? partnerBiomeLayer : partnerBiome);
                        break;
                    case 2 :
                        silverwing.setVariant(variant == 0 ? getVariant() : partnerVariant);
                        silverwing.setBiome(variant == 0 ? getBiome() : partnerBiome);
                        silverwing.setVariantColor(feathers == 0 ? getVariantColor() : partnerColor);
                        silverwing.setBiomeLayer(feathers == 0 ? getBiomeLayer() : partnerBiomeLayer);
                        break;
                    case 3 :
                        silverwing.setVariant(variant == 0 ? getVariantColor() : partnerColor);
                        silverwing.setBiome(variant == 0 ? getBiomeLayer() : partnerBiomeLayer);
                        silverwing.setVariantColor(feathers == 0 ? getVariant() : partnerVariant);
                        silverwing.setBiomeLayer(feathers == 0 ? getBiome() : partnerBiome);
                        break;
                }

                silverwing.setOwnerId(uuid);
                silverwing.setTamed(true);
                silverwing.setChild(true);
                silverwing.dataManager.set(SIZE, 0.25F);
            }
        }

        return silverwing;
    }

    public boolean isFlapping() {
        return (this.getUniqueFlapTickOffset() + this.ticksExisted) % TICKS_PER_FLAP == 0;
    }

    public int getUniqueFlapTickOffset() {
        return this.getEntityId() * 3;
    }

    public void tick() {
        super.tick();
        if (this.world.isRemote) {
            double f = Math.cos((float)(this.getUniqueFlapTickOffset() + this.ticksExisted) * 7.448451F * ((float)Math.PI / 180F) + (float)Math.PI);
            double f1 = Math.cos((float)(this.getUniqueFlapTickOffset() + this.ticksExisted + 1) * 7.448451F * ((float)Math.PI / 180F) + (float)Math.PI);
            if (f > 0.0F && f1 <= 0.0F && !this.isOnGround()) {
                this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_PHANTOM_FLAP, SoundCategory.NEUTRAL, 4.5F + this.rand.nextFloat() * 0.05F, 0.2F + this.rand.nextFloat() * 0.05F, false);
            }
        }
        if (!this.world.isRemote) {
            this.setBesideClimbableBlock(this.collidedHorizontally);
        }
        if(!isOnGround() && !dataManager.get(FLYING)) dataManager.set(FLYING, true);
        if(isOnGround() && dataManager.get(FLYING)) dataManager.set(FLYING, false);
    }

    public ActionResultType getEntityInteractionResult(PlayerEntity playerIn, Hand hand) {
        ItemStack itemstack = playerIn.getHeldItem(hand);
        Item item = itemstack.getItem();

        if (this.world.isRemote) {
            boolean flag = this.isOwner(playerIn) || this.isTamed() || FOOD_ITEMS.test(itemstack) && !this.isTamed();
            return flag ? ActionResultType.CONSUME : ActionResultType.PASS;
        }else if(!playerIn.isCrouching()){
            if(!this.isTamed() && FOOD_ITEMS.test(itemstack)) {
                if (!playerIn.abilities.isCreativeMode) itemstack.shrink(1);

                if (this.rand.nextInt(5) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, playerIn)) {
                    this.setTamedBy(playerIn);
                    this.navigator.clearPath();
                    this.setSitting(true);
                    this.setAttackTarget(null);
                    this.world.setEntityState(this, (byte)7);
                } else {
                    this.world.setEntityState(this, (byte)6);
                }
                return ActionResultType.SUCCESS;
            }
            if(this.isTamed() && this.isOwner(playerIn)){
                if(FOOD_ITEMS.test(itemstack)){
                    setSize();
                    heal(6);
                    if (!playerIn.abilities.isCreativeMode) itemstack.shrink(1);
                    return ActionResultType.SUCCESS;
                }
//                if (this.canWearArmor() && this.isArmor(itemstack) && !this.isWearingArmor()) {
//                    this.equipArmor(playerIn, itemstack);
//                    return ActionResultType.SUCCESS(this.world.isRemote);
//                }
                if(isBreedingItem(itemstack)) return super.getEntityInteractionResult(playerIn, hand);
                if(!isChild()) {
                    this.setSitting(false);
                    this.mountTo(playerIn);
                }
                return ActionResultType.SUCCESS;
            }
        }
        if(this.isTamed() && this.isOwner(playerIn)) this.setSitting(!dataManager.get(STATE));
        return super.getEntityInteractionResult(playerIn, hand);
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
    }

    public boolean isBreedingItem(ItemStack p_30440_) {
        Item item = p_30440_.getItem();
        return (item == Items.COOKED_COD || item == Items.COOKED_SALMON) && !isChild();
    }

    //HANDLING--SIZE----------------------------------------------------------------------------------------------------
    public void setSize() {
        dataManager.set(SIZE, (dataManager.get(SIZE)+0.025F));
        if(isChild() && dataManager.get(SIZE)>=1) setChild(false);
        this.recenterBoundingBox();
        this.recalculateSize();
        if(!isChild()) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(getMaxHealth() + 1);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(this.getAttributeValue(Attributes.ATTACK_DAMAGE) + 2);
        }
    }

    public void recalculateSize() {
        double d0 = this.getPosX();
        double d1 = this.getPosY();
        double d2 = this.getPosZ();
        super.recalculateSize();
        this.setPosition(d0, d1, d2);
    }

    public void notifyDataManagerChange(DataParameter<?> p_33609_) {
        if (SIZE.equals(p_33609_)) {
            this.recalculateSize();
            this.rotationYaw = this.rotationYawHead;
            this.renderYawOffset = this.rotationYawHead;
        }
        super.notifyDataManagerChange(p_33609_);
    }

    public EntitySize getSize(Pose p_21047_) {
        return super.getSize(p_21047_).scale(dataManager.get(SIZE));
    }
    //HANDLING--SIZE--END-----------------------------------------------------------------------------------------------

    public boolean attackEntityFrom(DamageSource p_27567_, float p_27568_) {
        if (getControllingPassenger()!=null && p_27567_.getTrueSource()!=null && getControllingPassenger().isRidingSameEntity(this)) {
            return false;
        } else {
            return super.attackEntityFrom(p_27567_, p_27568_);
        }
    }

    @Override
    public void updateLeashedState(){
        if(isTamed() && !land && getLeashed()) land=true;
        super.updateLeashedState();
    }

    @Override
    public void setSitting(boolean sit) {
        this.dataManager.set(STATE, sit);
        super.setSitting(sit);
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.dataManager.set(STATE, compound.getBoolean("Sitting"));
        this.dataManager.set(SIZE, compound.getFloat("size"));
        this.dataManager.set(SADDLED, compound.getByte("saddled"));
        this.dataManager.set(VARIANT, compound.getInt("variant"));
        this.dataManager.set(COLOR, compound.getInt("color"));
        this.dataManager.set(BIOME, compound.getInt("biome"));
        this.dataManager.set(BIOME_LAYER, compound.getInt("biomelayer"));
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putFloat("size", this.dataManager.get(SIZE));
        compound.putByte("saddled", this.dataManager.get(SADDLED));
        compound.putInt("variant", this.dataManager.get(VARIANT));
        compound.putInt("color", this.dataManager.get(COLOR));
        compound.putInt("biome", this.dataManager.get(BIOME));
        compound.putInt("biomelayer", this.dataManager.get(BIOME_LAYER));
    }

    public boolean canWearArmor() {
        return true;
    }

    public boolean isArmor(ItemStack p_30731_) {
        return p_30731_.getItem() instanceof HorseArmorItem;
    }

    public boolean isWearingArmor() {
        return !this.getItemStackFromSlot(EquipmentSlotType.CHEST).isEmpty();
    }

    public void equipArmor(PlayerEntity p_251330_, ItemStack p_248855_) {
        if (this.isArmor(p_248855_)) {
            //this.inventory.setItem(1, p_248855_.copyWithCount(1));
            if (!p_251330_.abilities.isCreativeMode) {
                p_248855_.shrink(1);
            }
        }

    }

    protected void dropInventory() {
        super.dropInventory();
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
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    public boolean onClimbable() {
        return this.isClimbing();
    }

    public boolean isClimbing() {
        return (this.dataManager.get(CLIMBING) & 1) != 0;
    }

    public void setBesideClimbableBlock(boolean p_33820_) {
        byte b0 = this.dataManager.get(CLIMBING);
        if (p_33820_) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }
        this.dataManager.set(CLIMBING, b0);
    }


    class PhantomTargetGoal extends Goal {
        private final EntityPredicate attackTargeting = (new EntityPredicate()).setDistance(64.0D);
        private int nextScanTick = 20;
        
        public boolean shouldExecute() {
            if(isTamed()) return false;
            if(this.nextScanTick > 0) {
                --this.nextScanTick;
                return false;
            } else {
                this.nextScanTick = 60;
                List<AnimalEntity> target = SilverwingEntity.this.world.getTargettableEntitiesWithinAABB(AnimalEntity.class, attackTargeting, SilverwingEntity.this, SilverwingEntity.this.getBoundingBox().grow(16.0D, 64.0D, 16.0D));
                if(!target.isEmpty()){
                    if (SilverwingEntity.this.canAttack(target.get(0), EntityPredicate.DEFAULT)) {
                        SilverwingEntity.this.setAttackTarget(target.get(0));
                        return true;
                    }
                }
                return false;
            }
        }

        public boolean shouldContinueExecuting() {
            LivingEntity livingentity = SilverwingEntity.this.getAttackTarget();
            return livingentity != null ? SilverwingEntity.this.canAttack(livingentity, EntityPredicate.DEFAULT) : false;
        }
    }

    class PhantomAttackStrategyGoal extends Goal {
        private int nextSweepTick;

        public boolean shouldExecute() {
            LivingEntity livingentity = SilverwingEntity.this.getAttackTarget();
            return livingentity != null ? SilverwingEntity.this.canAttack(livingentity, EntityPredicate.DEFAULT) : false;
        }

        public void startExecuting() {
            this.nextSweepTick = 10;
            SilverwingEntity.this.attackPhase = AttackPhase.CIRCLE;
            this.setAnchorAboveTarget();
        }

        public void resetTask() {
            SilverwingEntity.this.anchorPoint = SilverwingEntity.this.world.getHeight(Heightmap.Type.MOTION_BLOCKING, SilverwingEntity.this.anchorPoint).up(20 + SilverwingEntity.this.rand.nextInt(20));
        }

        public void tick() {
            if (SilverwingEntity.this.attackPhase == AttackPhase.CIRCLE) {
                --this.nextSweepTick;
                if (this.nextSweepTick <= 0) {
                    SilverwingEntity.this.attackPhase = AttackPhase.SWOOP;
                    this.setAnchorAboveTarget();
                    this.nextSweepTick = (8 + SilverwingEntity.this.rand.nextInt(4)) * 20;
                }
            }

        }

        private void setAnchorAboveTarget() {
            SilverwingEntity.this.anchorPoint = SilverwingEntity.this.getAttackTarget().getPosition().up(20 + SilverwingEntity.this.rand.nextInt(20));
            if (SilverwingEntity.this.anchorPoint.getY() < SilverwingEntity.this.world.getSeaLevel()) {
                SilverwingEntity.this.anchorPoint = new BlockPos(SilverwingEntity.this.anchorPoint.getX(), SilverwingEntity.this.world.getSeaLevel() + 1, SilverwingEntity.this.anchorPoint.getZ());
            }

        }
    }

    class PhantomCircleAroundAnchorGoal extends PhantomMoveTargetGoal {
        private float angle;
        private float distance;
        private float height;
        private float clockwise;

        public boolean shouldExecute() {
            return SilverwingEntity.this.getAttackTarget() == null || SilverwingEntity.this.attackPhase == AttackPhase.CIRCLE;
        }

        public void startExecuting() {
            this.distance = 5.0F + SilverwingEntity.this.rand.nextFloat() * 10.0F;
            this.height = -4.0F + SilverwingEntity.this.rand.nextFloat() * 9.0F;
            this.clockwise = SilverwingEntity.this.rand.nextBoolean() ? 1.0F : -1.0F;
            this.selectNext();
        }

        public void tick() {
            if (SilverwingEntity.this.rand.nextInt(350) == 0) {
                this.height = -4.0F + SilverwingEntity.this.rand.nextFloat() * 9.0F;
            }

            if (SilverwingEntity.this.rand.nextInt(250) == 0) {
                ++this.distance;
                if (this.distance > 15.0F) {
                    this.distance = 5.0F;
                    this.clockwise = -this.clockwise;
                }
            }

            if (SilverwingEntity.this.rand.nextInt(450) == 0) {
                this.angle = SilverwingEntity.this.rand.nextFloat() * 2.0F * (float)Math.PI;
                this.selectNext();
            }

            if (this.touchingTarget()) {
                this.selectNext();
            }

            if (SilverwingEntity.this.moveTargetPoint.y < SilverwingEntity.this.getPosY() && !SilverwingEntity.this.world.isAirBlock(SilverwingEntity.this.getPosition().down(1))) {
                this.height = Math.max(1.0F, this.height);
                this.selectNext();
            }

            if (SilverwingEntity.this.moveTargetPoint.y > SilverwingEntity.this.getPosY() && !SilverwingEntity.this.world.isAirBlock(SilverwingEntity.this.getPosition().up(1))) {
                this.height = Math.min(-1.0F, this.height);
                this.selectNext();
            }

        }

        private void selectNext() {
            if (BlockPos.ZERO.equals(SilverwingEntity.this.anchorPoint)) {
                SilverwingEntity.this.anchorPoint = SilverwingEntity.this.getPosition();
            }

            this.angle += this.clockwise * 15.0F * ((float)Math.PI / 180F);
            SilverwingEntity.this.moveTargetPoint = Vector3d.copy(SilverwingEntity.this.anchorPoint).add((double)(this.distance * Math.cos(this.angle)), (double)(-4.0F + this.height), (double)(this.distance * Math.sin(this.angle)));
        }
    }

    class PhantomLookController extends LookController {
        public PhantomLookController(MobEntity p_33235_) {
            super(p_33235_);
        }

        public void tick() {
        }
    }

    class PhantomMovementController extends MovementController {
        private float speed = 0.3F;

        public PhantomMovementController(MobEntity p_33241_) {
            super(p_33241_);
        }

        public void tick() {
            if (SilverwingEntity.this.collidedHorizontally) {
                SilverwingEntity.this.rotationYaw = (SilverwingEntity.this.rotationYaw + 180.0F);
                this.speed = 0.3F;
            }

            double d0 = SilverwingEntity.this.moveTargetPoint.x - SilverwingEntity.this.getPosX();
            double d1 = SilverwingEntity.this.moveTargetPoint.y - SilverwingEntity.this.getPosY();
            double d2 = SilverwingEntity.this.moveTargetPoint.z - SilverwingEntity.this.getPosZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            if (Math.abs(d3) > (double)1.0E-5F && !isQueuedToSit()) {
                double d4 = 1.0D - Math.abs(d1 * (double)0.7F) / d3;
                d0 *= d4;
                d2 *= d4;
                d3 = Math.sqrt(d0 * d0 + d2 * d2);
                double d5 = Math.sqrt(d0 * d0 + d2 * d2 + d1 * d1);
                float f = SilverwingEntity.this.rotationYaw;
                float f1 = (float)Math.atan2(d2, d0);
                float f2 = MathHelper.wrapDegrees(SilverwingEntity.this.rotationYaw + 90.0F);
                float f3 = MathHelper.wrapDegrees(f1 * (180F / (float)Math.PI));
                SilverwingEntity.this.rotationYaw = (MathHelper.approachDegrees(f2, f3, 4.0F) - 90.0F);
                SilverwingEntity.this.renderYawOffset = SilverwingEntity.this.rotationYaw;
                if (MathHelper.degreesDifferenceAbs(f, SilverwingEntity.this.rotationYaw) < 3.0F) {
                    this.speed = MathHelper.approach(this.speed, 1.8F, 0.005F * (1.8F / this.speed));
                } else {
                    this.speed = MathHelper.approach(this.speed, 0.2F, 0.025F);
                }

                float f4 = (float)(-(Math.atan2(-d1, d3) * (double)(180F / (float)Math.PI)));
                SilverwingEntity.this.rotationPitch =(f4);
                float f5 = SilverwingEntity.this.rotationYaw + 90.0F;
                double d6 = (double)(this.speed * Math.cos(f5 * ((float)Math.PI / 180F))) * Math.abs(d0 / d5);
                double d7 = (double)(this.speed * Math.sin(f5 * ((float)Math.PI / 180F))) * Math.abs(d2 / d5);
                double d8 = (double)(this.speed * Math.sin(f4 * ((float)Math.PI / 180F))) * Math.abs(d1 / d5);
                Vector3d vec3 = SilverwingEntity.this.getMotion();
                double y = (!SilverwingEntity.this.land) ? d8 : 0;
                if(SilverwingEntity.this.isOnGround()) {
                    SilverwingEntity.this.groundTicks++;
                    if ((SilverwingEntity.this.groundTicks > 1000 && !SilverwingEntity.this.isTamed()) || (SilverwingEntity.this.groundTicks > 2500 && SilverwingEntity.this.isTamed()) ) {
                        SilverwingEntity.this.land = false;
                        SilverwingEntity.this.groundTicks = 0;
                        SilverwingEntity.this.anchorPoint = SilverwingEntity.this.world.getHeight(Heightmap.Type.MOTION_BLOCKING, SilverwingEntity.this.anchorPoint).up(20 + SilverwingEntity.this.rand.nextInt(20));
                    }
                }
                if(SilverwingEntity.this.getAttackTarget()==null && !SilverwingEntity.this.isTamed()) {
                    SilverwingEntity.this.targetTicks++;
                    if (SilverwingEntity.this.targetTicks > 2500) {
                        SilverwingEntity.this.targetTicks = 0;
                        SilverwingEntity.this.anchorPoint = SilverwingEntity.this.world.getHeight(Heightmap.Type.MOTION_BLOCKING, SilverwingEntity.this.anchorPoint).up(20 + SilverwingEntity.this.rand.nextInt(20)).east(SilverwingEntity.this.rand.nextInt(40)).south(SilverwingEntity.this.rand.nextInt(40));
                    }
                }
                if(!SilverwingEntity.this.isOnGround() && SilverwingEntity.this.getAttackTarget()==null){
                    SilverwingEntity.this.flyTicks++;
                    if ((SilverwingEntity.this.flyTicks > 10000 && !SilverwingEntity.this.isTamed()) || (SilverwingEntity.this.flyTicks > 700 && SilverwingEntity.this.isTamed()) ) {
                        SilverwingEntity.this.flyTicks = 0;
                        SilverwingEntity.this.land=true;
                        SilverwingEntity.this.anchorPoint = SilverwingEntity.this.world.getHeight(Heightmap.Type.MOTION_BLOCKING, SilverwingEntity.this.getPosition());
                    }
                }
                SilverwingEntity.this.setMotion(vec3.add((new Vector3d(d6, y, d7)).subtract(vec3).scale(0.2D)));
            }
            if(isQueuedToSit() && (SilverwingEntity.this.flyTicks!=0 ||SilverwingEntity.this.groundTicks!=0 || !SilverwingEntity.this.land)){
                SilverwingEntity.this.flyTicks = 0;
                SilverwingEntity.this.groundTicks = 0;
                SilverwingEntity.this.land=true;
                SilverwingEntity.this.anchorPoint = SilverwingEntity.this.world.getHeight(Heightmap.Type.MOTION_BLOCKING, SilverwingEntity.this.getPosition());
            }
        }
    }

    abstract class PhantomMoveTargetGoal extends Goal {
        public PhantomMoveTargetGoal() {
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        protected boolean touchingTarget() {
            return SilverwingEntity.this.moveTargetPoint.squareDistanceTo(SilverwingEntity.this.getPosX(), SilverwingEntity.this.getPosY(), SilverwingEntity.this.getPosZ()) < 4.0D;
        }
    }

    class PhantomSweepAttackGoal extends PhantomMoveTargetGoal {

        public boolean shouldExecute() {
            return SilverwingEntity.this.getAttackTarget() != null && SilverwingEntity.this.attackPhase == AttackPhase.SWOOP;
        }

        public boolean shouldContinueExecuting() {
            LivingEntity livingentity = SilverwingEntity.this.getAttackTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                if (livingentity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity)livingentity;
                    if (livingentity.isSpectator() || player.isCreative()) {
                        return false;
                    }
                }
                if (!this.shouldExecute()) {
                    return false;
                } else {
                    return true;
                }
            }
        }

        public void startExecuting() {
        }

        public void resetTask() {
            SilverwingEntity.this.setAttackTarget((LivingEntity)null);
            SilverwingEntity.this.attackPhase = AttackPhase.CIRCLE;
        }

        public void tick() {
            LivingEntity livingentity = SilverwingEntity.this.getAttackTarget();
            if (livingentity != null) {
                SilverwingEntity.this.moveTargetPoint = new Vector3d(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());
                if (SilverwingEntity.this.getBoundingBox().grow((double)0.2F).intersects(livingentity.getBoundingBox())) {
                    SilverwingEntity.this.attackEntityAsMob(livingentity);
                    SilverwingEntity.this.attackPhase = AttackPhase.CIRCLE;
                    if (!SilverwingEntity.this.isSilent()) {
                        SilverwingEntity.this.world.playEvent(1039, SilverwingEntity.this.getPosition(), 0);
                    }
                } else if (SilverwingEntity.this.collidedHorizontally || SilverwingEntity.this.hurtTime > 0) {
                    SilverwingEntity.this.attackPhase = AttackPhase.CIRCLE;
                }
            }
        }
    }
    //END---FLYING------------------------------------------------------------------------------------------------------

    //MOUNT-------------------------------------------------------------------------------------------------------------
    @Override
    public boolean func_230264_L__() {
        return this.isAlive() && this.isTamed() && !isChild();
    }

    @Override
    public void func_230266_a_(@Nullable SoundCategory p_21748_) {
        itemHandler.setStackInSlot(0, new ItemStack(Items.SADDLE));
        byte b0 = (byte)(this.dataManager.get(SADDLED) | 1);
        this.dataManager.set(SADDLED, b0);
    }

    @Override
    public boolean isHorseSaddled() {
        return (this.dataManager.get(SADDLED) & 1) != 0;
    }

    protected void mountTo(PlayerEntity p_30634_) {
        if (!this.world.isRemote) {
            p_30634_.rotationYaw=(this.rotationYaw);
            p_30634_.rotationPitch=(this.rotationPitch);
            p_30634_.startRiding(this);
        }
    }

//    public boolean isImmobile() {
//        return super.isImmobile() && this.isVehicle() && this.func_230264_L__();
//    }

    public void travel(Vector3d pTravelVector) {
        if (this.isAlive()) {
            LivingEntity livingentity = this.getControllingPassenger();
            if (this.isBeingRidden() && livingentity != null && this.isHorseSaddled()) {
                this.rotationYaw=(livingentity.rotationYaw);
                this.prevRotationYaw = this.rotationYaw;
                this.rotationPitch=(livingentity.rotationPitch * 0.5F);
                this.setRotation(this.rotationYaw, this.rotationPitch);
                this.renderYawOffset = this.rotationYaw;
                this.rotationYawHead = this.renderYawOffset;
                this.jumpMovementFactor = this.getAIMoveSpeed() * 0.03F;

                if(world.isRemote){
                    if(KeyBindings.FLY_UP.isKeyDown()){
                        setMotion(new Vector3d(getMotion().x, -getMotion().y + 0.4, getMotion().z));
                    }else if(KeyBindings.FLY_DOWN.isKeyDown()){
                        setMotion(new Vector3d(getMotion().x, -getMotion().y-0.4, getMotion().z));
                    }else if(getMotion().y<0){
                        setMotion(new Vector3d(getMotion().x, -0.001, getMotion().z));
                    }
                }

                if (this.canPassengerSteer()) {
                    float f,f1;
                    if(isOnGround() || isInWater()){
                        this.setAIMoveSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                        f = livingentity.moveStrafing * 0.5F;
                        f1 = livingentity.moveForward;
                        if (f1 <= 0.0F) f1 *= 0.25F;
                        super.travel(new Vector3d((double)f, 0.0D, (double)f1).scale(0.15));
                    }else{
                        this.setAIMoveSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED)*4);
                        f = livingentity.moveStrafing * 8.5F;
                        f1 = livingentity.moveForward * 8;
                        if (f1 <= 0.0F) f1 *= 0.25F;
                        super.travel(new Vector3d(f, 0.0D, f1));
                    }
                }else if (livingentity instanceof PlayerEntity) {
                    this.setMotion(Vector3d.ZERO);
                }

                this.func_233629_a_(this, false);
            } else {
                this.jumpMovementFactor = 0.02F;
                super.travel(pTravelVector);
            }
        }
    }

    public boolean canPassengerSteer() {
        return this.getControllingPassenger() instanceof LivingEntity;
    }

    public void updatePassenger(Entity p_289569_) {
        if (p_289569_ instanceof MobEntity) {
            MobEntity mobentity = (MobEntity)p_289569_;
            this.renderYawOffset = mobentity.renderYawOffset;
        }

        double d0 = this.getPosY() + this.getMountedYOffset() + p_289569_.getYOffset();
        //if(!isOnGround() && (getMotion().x!=0 || getMotion().z!=0)) d0-=0.5;
        p_289569_.setPosition(this.getPosX(), d0, this.getPosZ());
        if (p_289569_ instanceof LivingEntity) {
            ((LivingEntity)p_289569_).renderYawOffset = this.renderYawOffset;
        }
    }

    @javax.annotation.Nullable
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
        if (entity instanceof MobEntity) {
            return (MobEntity)entity;
        } else {
            if (this.func_230264_L__()) {
                entity = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
                if (entity instanceof PlayerEntity) {
                    return (PlayerEntity)entity;
                }
            }

            return null;
        }
    }

    @javax.annotation.Nullable
    private Vector3d getDismountLocationInDirection(Vector3d p_30562_, LivingEntity p_30563_) {
        SilverwingEntity.this.anchorPoint = SilverwingEntity.this.world.getHeight(Heightmap.Type.MOTION_BLOCKING, SilverwingEntity.this.getPosition());
        double d0 = this.getPosX() + p_30562_.x;
        double d1 = this.getBoundingBox().minY;
        double d2 = this.getPosZ() + p_30562_.z;
        BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable();

        for(Pose pose : p_30563_.getAvailablePoses()) {
            blockpos$mutableblockpos.setPos(d0, d1, d2);
            double d3 = this.getBoundingBox().maxY + 0.75D;

            while(true) {
                double d4 = this.world.func_242403_h(blockpos$mutableblockpos);
                if ((double)blockpos$mutableblockpos.getY() + d4 > d3) {
                    break;
                }

                if (TransportationHelper.func_234630_a_(d4)) {
                    AxisAlignedBB aabb = p_30563_.getPoseAABB(pose);
                    Vector3d vec3 = new Vector3d(d0, (double)blockpos$mutableblockpos.getY() + d4, d2);
                    if (TransportationHelper.func_234631_a_(this.world, p_30563_, aabb.offset(vec3))) {
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

    public Vector3d getDismountPosition(LivingEntity p_30576_) {
        Vector3d vec3 = getRiderDismountPlacementOffset((double)this.getWidth(), (double)p_30576_.getWidth(), this.rotationYaw + (p_30576_.getPrimaryHand() == HandSide.RIGHT ? 90.0F : -90.0F));
        Vector3d vec31 = this.getDismountLocationInDirection(vec3, p_30576_);
        if (vec31 != null) {
            return vec31;
        } else {
            Vector3d vec32 = getRiderDismountPlacementOffset((double)this.getWidth(), (double)p_30576_.getWidth(), this.rotationYaw + (p_30576_.getPrimaryHand() == HandSide.RIGHT ? 90.0F : -90.0F));
            Vector3d vec33 = this.getDismountLocationInDirection(vec32, p_30576_);
            return vec33 != null ? vec33 : this.getPositionVec();
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
                    case 0:
                    case 1:
                        return 1;
                    default: return super.getSlotLimit(slot);
                }
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side){
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return handler.cast();
        return super.getCapability(cap, side);
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(dataManager.get(STATE)){
            AnimationBuilder anim= isChild() ? BABY_SIT : SIT;
            event.getController().setAnimation(anim);
            return PlayState.CONTINUE;
        }
        if (event.isMoving()) {
            AnimationBuilder anim= isChild() ? BABY_WALK : dataManager.get(FLYING) ? FLY : WALK;
            event.getController().setAnimation(anim);
            return PlayState.CONTINUE;
        }
        AnimationBuilder anim= isChild() ? BABY_IDLE : dataManager.get(FLYING) ? IDLE_FLY : IDLE_GROUND;
        event.getController().setAnimation(anim);
        return PlayState.CONTINUE;
    }

//    public <T extends GeoAnimatable> PlayState predicate2(AnimationState<T> tAnimationState)  {
//        tAnimationState.getController().forceAnimationReset();
//        return PlayState.STOP;
//    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<IAnimatable>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    //VARIANTS----------------------------------------------------------------------------------------------------------
    public int getVariant(){
        return dataManager.get(VARIANT);
    }

    public void setVariant(int variant){
        dataManager.set(VARIANT, variant);
    }

    public int getVariantColor(){
        return dataManager.get(COLOR);
    }

    public void setVariantColor(int feathers){
        dataManager.set(COLOR, feathers);
    }

    public int getBiome(){
        return dataManager.get(BIOME);
    }

    public void setBiome(int biome){
        dataManager.set(BIOME, biome);
    }

    public int getBiomeLayer(){
        return dataManager.get(BIOME_LAYER);
    }

    public void setBiomeLayer(int biome){
        dataManager.set(BIOME_LAYER, biome);
    }

    @javax.annotation.Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld p_30703_, DifficultyInstance p_30704_, SpawnReason p_30705_, @Nullable ILivingEntityData p_30706_, @Nullable CompoundNBT p_30707_) {
        int variant = p_30703_.getRandom().nextInt(3);
        int feathers = p_30703_.getRandom().nextInt(3);
        float temp = p_30703_.getBiome(getPosition()).getTemperature();
        if (temp>1.5) {
            setBiome(0); setBiomeLayer(0);
        } else if (temp>0.5) {
            setBiome(1); setBiomeLayer(1);
        } else if (temp<=0.5) {
            setBiome(2); setBiomeLayer(2);
        } else{
            setBiome(0); setBiomeLayer(0); variant = 0; feathers = 0;
        }
        setVariant(variant);
        setVariantColor(feathers);
        return super.onInitialSpawn(p_30703_, p_30704_, p_30705_, p_30706_, p_30707_);
    }
    //END--VARIANTS-----------------------------------------------------------------------------------------------------
    protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_PANDA_DEATH; }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SoundEvents.ENTITY_DONKEY_HURT; }
    public SoundEvent getAmbientSound() { return SoundEvents.ENTITY_PARROT_AMBIENT; }
}