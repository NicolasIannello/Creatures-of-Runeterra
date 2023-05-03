package com.eximeisty.creaturesofruneterra.entity.custom;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.eximeisty.creaturesofruneterra.container.PorobotContainer;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PatchedPorobotEntity extends TameableEntity implements IAnimatable{
   private AnimationFactory factory = new AnimationFactory(this);
   private final ItemStackHandler itemHandler = createHandler();
   private final LazyOptional<IItemHandler> handler = LazyOptional.of(()-> itemHandler);
   private static final DataParameter<Boolean> STATE = EntityDataManager.createKey(PoroEntity.class, DataSerializers.BOOLEAN);
   private static final DataParameter<Boolean> OPEN = EntityDataManager.createKey(PoroEntity.class, DataSerializers.BOOLEAN);
   private static final DataParameter<Boolean> CLOSE = EntityDataManager.createKey(PoroEntity.class, DataSerializers.BOOLEAN);
   private static final DataParameter<Integer> BURNTIME = EntityDataManager.createKey(PoroEntity.class, DataSerializers.VARINT);
   private static final DataParameter<Integer> BURNTIMETOTAL = EntityDataManager.createKey(PoroEntity.class, DataSerializers.VARINT);
   private static final DataParameter<Integer> COOKTIME = EntityDataManager.createKey(PoroEntity.class, DataSerializers.VARINT);
   private static final DataParameter<Integer> COOKTIMETOTAL = EntityDataManager.createKey(PoroEntity.class, DataSerializers.VARINT);
   public int playersUsing=0;
   public boolean playSound=false;
   public int animTicks=0;
   public int cd=400;
   public int burnTime, burnTimeTotal, cookTime, cookTimeTotal;

   public PatchedPorobotEntity(EntityType<? extends TameableEntity> type, World worldIn) {
      super(type, worldIn);
      this.setTamed(false);
   }

   public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
      return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 50)
      .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(1, new PanicGoal(this, 1.2D){
         @Override
         public void startExecuting() {
            TameableEntity poro =(TameableEntity)this.creature;
            this.creature.getDataManager().set(STATE, false);
            poro.setSitting(false);
            super.startExecuting();
         }
      });
      this.goalSelector.addGoal(2, new SitGoal(this));
      this.goalSelector.addGoal(0, new SwimGoal(this));
      this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.5D, 3.5F, 1.5F, false));
   }

   public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      if (event.isMoving()) {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.porobot.walk", true));
         return PlayState.CONTINUE;
      }
      if(dataManager.get(STATE)){
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.porobot.sit", true));
         return PlayState.CONTINUE;
      }
      event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.porobot.idle", true));
      return PlayState.CONTINUE;
   }

   public <E extends IAnimatable> PlayState predicate2(AnimationEvent<E> event) {
      if(dataManager.get(OPEN)){
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.porobot.open", false).addAnimation("animation.porobot.hold", true));
         return PlayState.CONTINUE;
      }
      if(dataManager.get(CLOSE)){
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.porobot.close", false));
         return PlayState.CONTINUE;
      }
      event.getController().clearAnimationCache();
      return PlayState.STOP;
  }

   @Override
   public void registerControllers(AnimationData data) {
      data.addAnimationController(new AnimationController<IAnimatable>(this, "controller", 0, this::predicate));
      data.addAnimationController(new AnimationController<IAnimatable>(this, "openClose", 0, this::predicate2));
   }

   @Override
   public AnimationFactory getFactory() {
      return factory;
   }

   @Override
   public AgeableEntity createChild(ServerWorld world, AgeableEntity mate) {
      return null;
   }

   protected void registerData() {
      super.registerData();
      dataManager.register(STATE, false);
      dataManager.register(OPEN, false);
      dataManager.register(CLOSE, false);
      dataManager.register(BURNTIME, 0);
      dataManager.register(BURNTIMETOTAL, 0);
      dataManager.register(COOKTIME, 0);
      dataManager.register(COOKTIMETOTAL, 0);
   }

   @Override
   public void setSitting(boolean sit) {
      this.dataManager.set(STATE, sit);
      super.setSitting(sit);
   }

   public void tick() {
      super.tick();
      if(this.dataManager.get(CLOSE)){
         animTicks++;
         if(animTicks>10){
            animTicks=0;
            this.dataManager.set(CLOSE, false);
         }
      }
      if(playersUsing==0 && playSound){ 
         this.world.playSound(null, this.getPosition(), SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.NEUTRAL, (float)0.5, (float)4);
         playSound=false;
         this.dataManager.set(OPEN, false);
         this.dataManager.set(CLOSE, true);
      }
      if(cd<=0){
         boolean flag= (this.getDistanceSq(this.getOwner())<=20 && itemHandler.getStackInSlot(15).getItem()==Items.DISPENSER && (itemHandler.getStackInSlot(16).getItem()==Items.SPLASH_POTION || itemHandler.getStackInSlot(17).getItem()==Items.SPLASH_POTION || itemHandler.getStackInSlot(18).getItem()==Items.SPLASH_POTION || itemHandler.getStackInSlot(19).getItem()==Items.SPLASH_POTION));
         if(this.getOwner().isBurning() && flag) this.throwPotion("fire");
         if(this.getOwner().getHealth()<=10 && flag) this.throwPotion("health|regeneration|strength|slowness|water");
      }else{
         cd--;
      }
      if(itemHandler.getStackInSlot(20).getItem()==Items.FURNACE) furnaceLogic();
   }

   public void throwPotion(String match){
      Vector3d vector3d = this.getOwner().getMotion();
      double d0 = this.getOwner().getPosX() + vector3d.x - this.getPosX();
      double d1 = this.getOwner().getPosYEye() - (double)1.1F - this.getPosY();
      double d2 = this.getOwner().getPosZ() + vector3d.z - this.getPosZ();
      float f = MathHelper.sqrt(d0 * d0 + d2 * d2);
      
      Potion potion = null;
      int i=15;
      do {
         i++;
         if(!itemHandler.getStackInSlot(i).isEmpty()){
            Potion potionPlaceHolder = PotionUtils.getPotionFromItem(itemHandler.getStackInSlot(i));
            Pattern pattern = Pattern.compile(match, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(potionPlaceHolder.getEffects().get(0).getEffectName());
            boolean matchFound = matcher.find();
            if(matchFound) potion = potionPlaceHolder;
         }
      } while (potion==null && i<=19);
      if(potion!=null){
         PotionEntity potionentity = new PotionEntity(this.world, this);
         potionentity.setItem(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potion));
         potionentity.rotationPitch -= -20.0F;
         potionentity.shoot(d0, d1 + (double)(f * 0.2F), d2, 0.75F, 8.0F);
         this.world.addEntity(potionentity);
         itemHandler.extractItem(i, 1, false);
         cd=1000;
      }else{
         cd=150;
      }
   }

   public int getCookProgressionScaled() {
      int i = dataManager.get(COOKTIME);
      int j = dataManager.get(COOKTIMETOTAL);
      return j != 0 && i != 0 ? i * 48 / j : 0;
   }

   public int getBurnLeftScaled() {
      int i = dataManager.get(BURNTIMETOTAL);
      if (i == 0) i = 200;
      return dataManager.get(BURNTIME) * 17 / i;
   }

   public boolean isLit(){
      return dataManager.get(BURNTIMETOTAL)>0;
   }

   public boolean isHeating(){
      return this.burnTime>0;
   }

   protected boolean canSmelt(@Nullable IRecipe<?> recipeIn) {
      if (!itemHandler.getStackInSlot(22).isEmpty() && recipeIn != null) {
         ItemStack itemstack = recipeIn.getRecipeOutput();
         if (itemstack.isEmpty()) {
            return false;
         } else {
            ItemStack itemstack1 = itemHandler.getStackInSlot(24);
            if (itemstack1.isEmpty()) {
               return true;
            } else if (!itemstack1.isItemEqual(itemstack)) {
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

   private void smelt(@Nullable IRecipe<?> recipe) {
      if (recipe != null && this.canSmelt(recipe)) {
         ItemStack itemstack = itemHandler.getStackInSlot(22);
         ItemStack itemstack1 = recipe.getRecipeOutput();
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

   protected int getCookTime(Inventory inv) {
      return this.world.getRecipeManager().getRecipe(IRecipeType.SMELTING, inv, this.world).map(AbstractCookingRecipe::getCookTime).orElse(this.world.getRecipeManager().getRecipe(IRecipeType.CAMPFIRE_COOKING, inv, this.world).map(AbstractCookingRecipe::getCookTime).orElse(200));
   }

   @Deprecated
   public void furnaceLogic(){
      boolean flag = this.isHeating();
      boolean flag1 = false;
      if(this.isHeating()) {
         burnTime--;
         if (rand.nextDouble() < 0.1D) {
            world.playSound(null, this.getPosition(), SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.NEUTRAL, 1F, 1F);     
            this.world.setEntityState(this, (byte)13);
         }
      }
      if(!this.world.isRemote){
         if (this.isHeating() || !itemHandler.getStackInSlot(23).isEmpty() && !itemHandler.getStackInSlot(22).isEmpty()) {
            Inventory inv = new Inventory(itemHandler.getStackInSlot(22), itemHandler.getStackInSlot(23));
            IRecipe<?> irecipe = this.world.getRecipeManager().getRecipe(IRecipeType.SMELTING, inv, this.world).orElse(null);
            if (!this.isHeating() && this.canSmelt(irecipe)) {
               this.burnTime = ForgeHooks.getBurnTime(itemHandler.getStackInSlot(23));
               this.burnTimeTotal = this.burnTime;
               this.cookTimeTotal= this.getCookTime(inv);
               if (this.isHeating()) {
                  flag1 = true;
                  if (itemHandler.getStackInSlot(23).hasContainerItem())
                     itemHandler.insertItem(23, itemHandler.getStackInSlot(23).getContainerItem(), false);
                  else if (!itemHandler.getStackInSlot(23).isEmpty()) {
                     itemHandler.getStackInSlot(23).shrink(1);
                     if (itemHandler.getStackInSlot(23).isEmpty()) {
                        itemHandler.insertItem(23, itemHandler.getStackInSlot(23).getContainerItem(), false);
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
            this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
         }

         if (flag != this.isHeating()) {
            flag1 = true;
         }
         dataManager.set(BURNTIME, burnTime);
         dataManager.set(BURNTIMETOTAL, burnTimeTotal);
         dataManager.set(COOKTIME, cookTime);
         dataManager.set(COOKTIMETOTAL, cookTimeTotal);
      }
      if (flag1) this.markLoadedDirty();
   }

   @OnlyIn(Dist.CLIENT)
   public void handleStatusUpdate(byte id) {
      if (id == 13) {
         this.spawnParticles(ParticleTypes.SMOKE, ParticleTypes.FLAME);
         this.spawnParticles(ParticleTypes.SMOKE, ParticleTypes.FLAME);
      } else {
         super.handleStatusUpdate(id);
      }
   }

   @OnlyIn(Dist.CLIENT)
   protected void spawnParticles(IParticleData particleData, IParticleData particleData2) {
      double d0 = this.rand.nextGaussian() * 0.02D, d1 = this.rand.nextGaussian() * 0.02D, d2 = this.rand.nextGaussian() * 0.02D;
      world.addParticle(particleData, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), d0, d1, d2);
      world.addParticle(particleData2, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), d0, d1, d2);
   }
/*-------------------------------INVENTORY------------------------------------------------------- */
   public ActionResultType getEntityInteractionResult(PlayerEntity playerIn, Hand hand) {
      if (!world.isRemote) {
         if(playerIn.isSecondaryUseActive()){
            this.setSitting(!dataManager.get(STATE));
         }else if(playerIn.getDistance(this)<=3){
            INamedContainerProvider containerProvider = createContainerProvider(world, this.getEntityId());
            NetworkHooks.openGui((ServerPlayerEntity)playerIn, containerProvider, buf -> buf.writeInt(this.getEntityId()));
            this.world.playSound(null, this.getPosition(), SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.NEUTRAL, (float)0.5, (float)6);
            this.dataManager.set(OPEN, true);
            playersUsing++;
            playSound=true;
         }
		}
      return ActionResultType.SUCCESS;
   }

   private INamedContainerProvider createContainerProvider(World world, int id) {
      return new INamedContainerProvider() {

         @Override
         public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
            return new PorobotContainer(i, world, id, playerInventory, playerEntity);
         }

         @Override
         public ITextComponent getDisplayName() {
            return world.getEntityByID(id).getDisplayName();
         }
         
      };
   }

   @Override
   public void readAdditional(CompoundNBT compound) {
      itemHandler.deserializeNBT(compound.getCompound("inv"));
      this.dataManager.set(STATE, compound.getBoolean("Sitting"));
      this.dataManager.set(BURNTIME, compound.getInt("burnTime"));
      this.dataManager.set(BURNTIMETOTAL, compound.getInt("burnTimeTotal"));
      this.dataManager.set(COOKTIME, compound.getInt("cookTime"));
      this.dataManager.set(COOKTIMETOTAL, compound.getInt("cookTimeTotal"));
      burnTime=compound.getInt("burnTime");
      burnTimeTotal=compound.getInt("burnTimeTotal");
      cookTime=compound.getInt("cookTime");
      cookTimeTotal=compound.getInt("cookTimeTotal");
      super.readAdditional(compound);
   }

   @Override
   public void writeAdditional(CompoundNBT compound) {
      compound.put("inv", itemHandler.serializeNBT());
      compound.putInt("burnTime", dataManager.get(BURNTIME));
      compound.putInt("burnTimeTotal", dataManager.get(BURNTIMETOTAL));
      compound.putInt("cookTime", dataManager.get(COOKTIME));
      compound.putInt("cookTimeTotal", dataManager.get(COOKTIMETOTAL));
      super.writeAdditional(compound);
   }

   private ItemStackHandler createHandler(){
      return new ItemStackHandler(34){
         @Override
         protected void onContentsChanged(int slot){
            if(slot==15) changeItem(Hand.MAIN_HAND, slot);
            if(slot==20) changeItem(Hand.OFF_HAND, slot);
            markLoadedDirty();
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
               case 23: return ForgeHooks.getBurnTime(stack)>0;
               case 24: return !(stack.getAttachedEntity()==null);
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

   public void changeItem(Hand hand, int slot){
      this.setHeldItem(hand, this.itemHandler.getStackInSlot(slot));
   }

   @Nonnull
   @Override
   public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side){
      if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return handler.cast();
      return super.getCapability(cap, side);
   }

   protected void dropInventory() {
      super.dropInventory();
      for(int i = 0; i < itemHandler.getSlots(); ++i) {
         ItemStack itemstack = itemHandler.getStackInSlot(i);
         if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack)) {
            this.entityDropItem(itemstack);
         }
      }
      this.entityDropItem(new ItemStack(Items.OAK_PLANKS, 5));
      this.entityDropItem(new ItemStack(Items.DARK_OAK_PLANKS, 4));
   }
/*-------------------------------------------------------------------------------------- */
   protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_BAT_DEATH; }
   protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SoundEvents.ENTITY_FOX_HURT; }
   public SoundEvent getAmbientSound() { return SoundEvents.ENTITY_FOX_AMBIENT; }
}