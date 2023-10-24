package com.eximeisty.creaturesofruneterra.entity.custom;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.eximeisty.creaturesofruneterra.container.PorobotContainer;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PatchedPorobotEntity extends TamableAnimal implements GeoEntity {
   private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
   private final ItemStackHandler itemHandler = createHandler();
   private final LazyOptional<IItemHandler> handler = LazyOptional.of(()-> itemHandler);
   private static final EntityDataAccessor<Boolean> STATE = SynchedEntityData.defineId(PatchedPorobotEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> OPEN = SynchedEntityData.defineId(PatchedPorobotEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> CLOSE = SynchedEntityData.defineId(PatchedPorobotEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Integer> BURNTIME = SynchedEntityData.defineId(PatchedPorobotEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> BURNTIMETOTAL = SynchedEntityData.defineId(PatchedPorobotEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> COOKTIME = SynchedEntityData.defineId(PatchedPorobotEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> COOKTIMETOTAL = SynchedEntityData.defineId(PatchedPorobotEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<CompoundTag> INVENTORY = SynchedEntityData.defineId(PatchedPorobotEntity.class, EntityDataSerializers.COMPOUND_TAG);
   public int playersUsing=0;
   public boolean playSound=false;
   public int animTicks=0;
   public int cd=400;
   public int burnTime, burnTimeTotal, cookTime, cookTimeTotal;
   private static final RawAnimation IDLE_ANIM = RawAnimation.begin().then("animation.porobot.idle", Animation.LoopType.LOOP);
   private static final RawAnimation WALK_ANIM = RawAnimation.begin().then("animation.porobot.walk", Animation.LoopType.LOOP);
   private static final RawAnimation SIT_ANIM = RawAnimation.begin().then("animation.porobot.sit", Animation.LoopType.LOOP);
   private static final RawAnimation OPEN_ANIM = RawAnimation.begin().then("animation.porobot.open", Animation.LoopType.PLAY_ONCE).then("animation.porobot.hold", Animation.LoopType.LOOP);
   private static final RawAnimation CLOSE_ANIM = RawAnimation.begin().then("animation.porobot.close", Animation.LoopType.PLAY_ONCE);


   public PatchedPorobotEntity(EntityType<? extends TamableAnimal> type, Level worldIn) {
      super(type, worldIn);
      this.setTame(false);
   }

   public static AttributeSupplier setAttributes(){
      return TamableAnimal.createMobAttributes().add(Attributes.MAX_HEALTH, 50)
      .add(Attributes.MOVEMENT_SPEED, 0.23).build();
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(1, new PanicGoal(this, 1.2D){
         @Override
         public void start() {
            TamableAnimal poro =(TamableAnimal)this.mob;
            this.mob.getEntityData().set(STATE, false);
            poro.setOrderedToSit(false);
            super.canUse();
         }
      });
      this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.5D, 3.5F, 1.5F, false));
   }

   public <T extends GeoAnimatable> PlayState predicate(AnimationState<T> event)  {
      if(!entityData.get(OPEN) || !entityData.get(CLOSE)){
         if (event.isMoving()) {
            event.getController().setAnimation(WALK_ANIM);
            return PlayState.CONTINUE;
         }
         if(entityData.get(STATE)){
            event.getController().setAnimation(SIT_ANIM);
            return PlayState.CONTINUE;
         }
         event.getController().setAnimation(IDLE_ANIM);
         return PlayState.CONTINUE;
      }
      return PlayState.STOP;
   }

   public <T extends GeoAnimatable> PlayState predicate2(AnimationState<T> event)  {
      if(entityData.get(OPEN)){
         event.getController().setAnimation(OPEN_ANIM);
         return PlayState.CONTINUE;
      }
      if(entityData.get(CLOSE)){
         event.getController().setAnimation(CLOSE_ANIM);
         return PlayState.CONTINUE;
      }
      event.getController().forceAnimationReset();
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

   @Override
   public PoroEntity getBreedOffspring(ServerLevel p_149088_, AgeableMob p_149089_) {
      return null;
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      entityData.define(STATE, false);
      entityData.define(OPEN, false);
      entityData.define(CLOSE, false);
      entityData.define(BURNTIME, 0);
      entityData.define(BURNTIMETOTAL, 0);
      entityData.define(COOKTIME, 0);
      entityData.define(COOKTIMETOTAL, 0);
   }

   @Override
   public void setOrderedToSit(boolean sit) {
      this.entityData.set(STATE, sit);
      super.setOrderedToSit(sit);
   }

   public void tick() {
      super.tick();
      if(this.entityData.get(CLOSE)){
         animTicks++;
         if(animTicks>10){
            animTicks=0;
            this.entityData.set(CLOSE, false);
         }
      }
      if(playersUsing==0 && playSound){ 
         this.level.playSound(null, this.blockPosition(), SoundEvents.CHEST_CLOSE, SoundSource.NEUTRAL, (float)0.5, (float)4);
         playSound=false;
         this.entityData.set(OPEN, false);
         this.entityData.set(CLOSE, true);
      }
      if(cd<=0){
         boolean flag= (itemHandler.getStackInSlot(15).getItem()== Items.DISPENSER && (itemHandler.getStackInSlot(16).getItem()==Items.SPLASH_POTION || itemHandler.getStackInSlot(17).getItem()==Items.SPLASH_POTION || itemHandler.getStackInSlot(18).getItem()==Items.SPLASH_POTION || itemHandler.getStackInSlot(19).getItem()==Items.SPLASH_POTION));
         if(this.isOnFire() && flag) this.throwPotion("fire", this, 100, 50);
         if(this.getHealth()<=this.getMaxHealth()/2 && flag) this.throwPotion("health|regeneration|strength|slowness|water", this, 100, 50);
         if(cd<=0 && this.getOwner()!=null){
            if(this.getOwner().isOnFire() && flag && this.distanceToSqr(this.getOwner())<=20) this.throwPotion("fire", (LivingEntity) this.getOwner(), 1000, 150);
            if(this.getOwner().getHealth()<=this.getOwner().getMaxHealth()/2 && flag && this.distanceToSqr(this.getOwner())<=20) this.throwPotion("health|regeneration|strength|slowness|water", this.getOwner(), 1000, 150);
         }
      }else if(cd>0){
         cd--;
      }
      if(itemHandler.getStackInSlot(20).getItem()==Items.FURNACE) furnaceLogic();
   }

   public void throwPotion(String match, LivingEntity obj, int cd1, int cd2){
      Vec3 vector3d = obj.getDeltaMovement();
      double d0 = obj.getX() + vector3d.x - this.getX();
      double d1 = obj.getEyeY() - (double)1.1F - this.getY();
      double d2 = obj.getZ() + vector3d.z - this.getZ();
      double f = Math.sqrt(d0 * d0 + d2 * d2);
      
      Potion potion = null;
      int i=16;
      do {
         if(!itemHandler.getStackInSlot(i).isEmpty()){
            Potion potionPlaceHolder = PotionUtils.getPotion(itemHandler.getStackInSlot(i));
            Pattern pattern = Pattern.compile(match, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(potionPlaceHolder.getEffects().get(0).getDescriptionId());
            boolean matchFound = matcher.find();
            if(matchFound) potion = potionPlaceHolder;
         }
         i++;
      } while (potion==null && i<=19);
      if(potion!=null){
         ThrownPotion potionentity = new ThrownPotion(this.level, this);
         potionentity.setItem(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), potion));
         potionentity.setXRot(potionentity.getXRot() - -20.0F);
         potionentity.shoot(d0, d1 + (double)(f * 0.2F), d2, 0.75F, 8.0F);
         this.level.addFreshEntity(potionentity);
         itemHandler.extractItem(i-1, 1, false);
         cd=cd1;
      }else{
         cd=cd2;
      }
   }

   public int getCookProgressionScaled() {
      int i = entityData.get(COOKTIME);
      int j = entityData.get(COOKTIMETOTAL);
      return j != 0 && i != 0 ? i * 48 / j : 0;
   }

   public int getBurnLeftScaled() {
      int i = entityData.get(BURNTIMETOTAL);
      if (i == 0) i = 200;
      return entityData.get(BURNTIME) * 17 / i;
   }

   public boolean isLit(){
      return entityData.get(BURNTIMETOTAL)>0;
   }

   public boolean isHeating(){
      return this.burnTime>0;
   }

   protected boolean canSmelt(@Nullable Recipe<?> recipeIn) {
      if (!itemHandler.getStackInSlot(22).isEmpty() && recipeIn != null) {
         ItemStack itemstack = recipeIn.getResultItem(level.registryAccess());
         if (itemstack.isEmpty()) {
            return false;
         } else {
            ItemStack itemstack1 = itemHandler.getStackInSlot(24);
            if (itemstack1.isEmpty()) {
               return true;
            } else if (!itemstack1.sameItem(itemstack)) {
               return false;
            } else if (itemstack1.getCount() + itemstack.getCount() <= itemHandler.getSlotLimit(24) && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
               return true;
            } else {
               return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
            }
         }
      } else {
         return false;
      }
   }

   private void smelt(@Nullable Recipe<?> recipe) {
      if (recipe != null && this.canSmelt(recipe)) {
         ItemStack itemstack = itemHandler.getStackInSlot(22);
         ItemStack itemstack1 = recipe.getResultItem(level.registryAccess());//??
         ItemStack itemstack2 = itemHandler.getStackInSlot(24);
         if (itemstack2.isEmpty()) {
            itemHandler.setStackInSlot(24, itemstack1);
         } else if (itemstack2.getItem() == itemstack1.getItem()) {
            itemHandler.getStackInSlot(24).grow(itemstack1.getCount());
         }
         if (itemstack.getItem() == Blocks.WET_SPONGE.asItem() && !itemHandler.getStackInSlot(23).isEmpty() && itemHandler.getStackInSlot(23).getItem() == Items.BUCKET) {
            itemHandler.insertItem(23, new ItemStack(Items.WATER_BUCKET), false);
         }
         itemstack.shrink(1);
      }
   }

   protected int getCookTime(SimpleContainer inv) {
      return this.level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, inv, this.level).map(AbstractCookingRecipe::getCookingTime).orElse(this.level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, inv, this.level).map(AbstractCookingRecipe::getCookingTime).orElse(200));
   }

   @Deprecated
   public void furnaceLogic(){
      boolean flag = this.isHeating();
      boolean flag1 = false;
      if(this.isHeating()) {
         burnTime--;
         if (random.nextDouble() < 0.1D) {
            level.playSound(null, this.blockPosition(), SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.NEUTRAL, 1F, 1F);
            this.level.broadcastEntityEvent(this, (byte)13);
         }
      }
      if(!this.level.isClientSide){
         if (this.isHeating() || !itemHandler.getStackInSlot(23).isEmpty() && !itemHandler.getStackInSlot(22).isEmpty()) {
            SimpleContainer inv = new SimpleContainer(itemHandler.getStackInSlot(22), itemHandler.getStackInSlot(23));
            Recipe<?> irecipe = this.level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, inv, this.level).orElse(null);
            if (!this.isHeating() && this.canSmelt(irecipe)) {
               this.burnTime = ForgeHooks.getBurnTime(itemHandler.getStackInSlot(23), RecipeType.SMELTING);
               this.burnTimeTotal = this.burnTime;
               this.cookTimeTotal= this.getCookTime(inv);
               if (this.isHeating()) {
                  flag1 = true;
                  if (itemHandler.getStackInSlot(23).hasCraftingRemainingItem())
                     itemHandler.insertItem(23, itemHandler.getStackInSlot(23).getCraftingRemainingItem(), false);
                  else if (!itemHandler.getStackInSlot(23).isEmpty()) {
                     itemHandler.getStackInSlot(23).shrink(1);
                     if (itemHandler.getStackInSlot(23).isEmpty()) {
                        itemHandler.insertItem(23, itemHandler.getStackInSlot(23).getCraftingRemainingItem(), false);
                     }
                  }
               }
            }
            if (this.isHeating() && this.canSmelt(irecipe)) {
               ++this.cookTime;
               if (this.cookTime == this.cookTimeTotal) {
                  this.cookTime = 0;
                  this.cookTimeTotal = this.getCookTime(inv);
                  this.smelt(irecipe);
                  flag1 = true;
               }
            } else {
               this.cookTime = 0;
            }
         }else if (!this.isHeating() && this.cookTime > 0) {
            this.cookTime = Mth.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
         }

         if (flag != this.isHeating()) {
            flag1 = true;
         }
         entityData.set(BURNTIME, burnTime);
         entityData.set(BURNTIMETOTAL, burnTimeTotal);
         entityData.set(COOKTIME, cookTime);
         entityData.set(COOKTIMETOTAL, cookTimeTotal);
      }
      if (flag1) {
         //this.markLoadedDirty();
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void handleEntityEvent(byte id) {
      if (id == 13) {
         this.spawnParticles(ParticleTypes.SMOKE, ParticleTypes.FLAME);
         this.spawnParticles(ParticleTypes.SMOKE, ParticleTypes.FLAME);
      } else {
         super.handleEntityEvent(id);
      }
   }

   @OnlyIn(Dist.CLIENT)
   protected void spawnParticles(ParticleOptions particleData, ParticleOptions particleData2) {
      double d0 = this.random.nextGaussian() * 0.02D, d1 = this.random.nextGaussian() * 0.02D, d2 = this.random.nextGaussian() * 0.02D;
      level.addParticle(particleData, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
      level.addParticle(particleData2, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
   }
/*-------------------------------INVENTORY------------------------------------------------------- */
   public InteractionResult mobInteract(Player playerIn, InteractionHand hand) {
      if (!level.isClientSide) {
         if(playerIn.isSecondaryUseActive()){
            if(this.getOwner()==playerIn) this.setOrderedToSit(!entityData.get(STATE));
         }else if(playerIn.distanceTo(this)<=3){
            MenuProvider containerProvider = createContainerProvider(level, this.getId());
            NetworkHooks.openScreen((ServerPlayer)playerIn, containerProvider, buf -> buf.writeInt(this.getId()));
            this.level.playSound(null, this.blockPosition(), SoundEvents.CHEST_OPEN, SoundSource.NEUTRAL, (float)0.5, (float)6);
            this.entityData.set(OPEN, true);
            playersUsing++;
            playSound=true;
         }
      }
      return InteractionResult.SUCCESS;
   }

   private MenuProvider createContainerProvider(Level world, int id) {
      return new MenuProvider() {

         @Override
         public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
            return new PorobotContainer(i, world, id, playerInventory, playerEntity);
         }

         @Override
         public Component getDisplayName() {
            return world.getEntity(id).getDisplayName();
         }
         
      };
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      itemHandler.deserializeNBT(compound.getCompound("inv"));
      this.entityData.set(STATE, compound.getBoolean("Sitting"));
      this.entityData.set(BURNTIME, compound.getInt("burnTime"));
      this.entityData.set(BURNTIMETOTAL, compound.getInt("burnTimeTotal"));
      this.entityData.set(COOKTIME, compound.getInt("cookTime"));
      this.entityData.set(COOKTIMETOTAL, compound.getInt("cookTimeTotal"));
      burnTime=compound.getInt("burnTime");
      burnTimeTotal=compound.getInt("burnTimeTotal");
      cookTime=compound.getInt("cookTime");
      cookTimeTotal=compound.getInt("cookTimeTotal");
      cd= compound.getInt("cd")>100?compound.getInt("cd"):100;
      super.readAdditionalSaveData(compound);
   }

//   public void addAdditionalSaveDataNoSuper() {
//      CompoundTag compound = new CompoundTag();
//      itemHandler.deserializeNBT(compound.getCompound("inv"));
//      this.entityData.set(STATE, compound.getBoolean("Sitting"));
//      this.entityData.set(BURNTIME, compound.getInt("burnTime"));
//      this.entityData.set(BURNTIMETOTAL, compound.getInt("burnTimeTotal"));
//      this.entityData.set(COOKTIME, compound.getInt("cookTime"));
//      this.entityData.set(COOKTIMETOTAL, compound.getInt("cookTimeTotal"));
//      burnTime=compound.getInt("burnTime");
//      burnTimeTotal=compound.getInt("burnTimeTotal");
//      cookTime=compound.getInt("cookTime");
//      cookTimeTotal=compound.getInt("cookTimeTotal");
//      cd= compound.getInt("cd")>100?compound.getInt("cd"):100;
//      //super.addAdditionalSaveData(compound);
//   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      compound.put("inv", itemHandler.serializeNBT());
      compound.putInt("burnTime", entityData.get(BURNTIME));
      compound.putInt("burnTimeTotal", entityData.get(BURNTIMETOTAL));
      compound.putInt("cookTime", entityData.get(COOKTIME));
      compound.putInt("cookTimeTotal", entityData.get(COOKTIMETOTAL));
      compound.putInt("cd", cd);
      super.addAdditionalSaveData(compound);
   }

   private ItemStackHandler createHandler(){
      return new ItemStackHandler(34){
         @Override
         protected void onContentsChanged(int slot){
            if(slot==15) changeItem(InteractionHand.MAIN_HAND, slot);
            if(slot==20) changeItem(InteractionHand.OFF_HAND, slot);
            //markLoadedDirty();
         }

         @Override @Deprecated
         public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            switch (slot) {
               case 15: return stack.getItem()==Items.DISPENSER;
               case 16: 
               case 17: 
               case 18: 
               case 19: return stack.getItem()==Items.SPLASH_POTION;
               case 20: return (stack.getItem()==Items.CRAFTING_TABLE || stack.getItem()==Items.FURNACE);
               case 23: return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING)>0 ? true : ForgeHooks.getBurnTime(stack, RecipeType.CAMPFIRE_COOKING)>0;
               case 24: return !(stack.getEntityRepresentation()==null);
               default: return super.isItemValid(slot, stack);     
            }
         }

         @Override
         public int getSlotLimit(int slot) {
            switch (slot) {
               case 15: return 1;
               case 16: return 10;
               case 17: return 1;
               default: return super.getSlotLimit(slot);     
            }
         }
      };
   }

   public void changeItem(InteractionHand hand, int slot){
      this.setItemInHand(hand, this.itemHandler.getStackInSlot(slot));
   }

   @Nonnull
   @Override
   public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side){
      if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return handler.cast();
      return super.getCapability(cap, side);
   }

   protected void dropEquipment() {
      super.dropEquipment();
      for(int i = 0; i < itemHandler.getSlots(); ++i) {
         ItemStack itemstack = itemHandler.getStackInSlot(i);
         if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack)) {
            this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), itemstack));
         }
      }
      this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), new ItemStack(Items.OAK_PLANKS, 5)));
      this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), new ItemStack(Items.DARK_OAK_PLANKS, 4)));
   }
/*-------------------------------------------------------------------------------------- */
   protected SoundEvent getDeathSound() { return SoundEvents.BAT_DEATH; }
   protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SoundEvents.FOX_HURT; }
   public SoundEvent getAmbientSound() { return SoundEvents.FOX_AMBIENT; }
}