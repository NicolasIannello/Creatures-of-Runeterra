package com.eximeisty.creaturesofruneterra.entity.custom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.eximeisty.creaturesofruneterra.container.PorobotContainer;
import com.eximeisty.creaturesofruneterra.item.ModItems;

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
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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

public class PatchedPorobotEntity extends TameableEntity implements IAnimatable{
   private AnimationFactory factory = new AnimationFactory(this);
   private final ItemStackHandler itemHandler = createHandler();
   private final LazyOptional<IItemHandler> handler = LazyOptional.of(()-> itemHandler);
   private static final DataParameter<Boolean> STATE = EntityDataManager.createKey(PoroEntity.class, DataSerializers.BOOLEAN);
   private static final DataParameter<Boolean> OPEN = EntityDataManager.createKey(PoroEntity.class, DataSerializers.BOOLEAN);
   private static final DataParameter<Boolean> CLOSE = EntityDataManager.createKey(PoroEntity.class, DataSerializers.BOOLEAN);
   public int playersUsing=0;
   public boolean playSound=false;
   public int animTicks=0;

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
      super.readAdditional(compound);
   }

   @Override
   public void writeAdditional(CompoundNBT compound) {
      compound.put("inv", itemHandler.serializeNBT());
      super.writeAdditional(compound);
   }

   private ItemStackHandler createHandler(){
      return new ItemStackHandler(17){
         @Override
         protected void onContentsChanged(int slot){
            markLoadedDirty();
         }

         @Override
         public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            switch (slot) {
               case 15: return stack.getItem()==Items.DISPENSER;
               case 16: return (stack.getItem()==Items.ARROW || stack.getItem()==Items.TNT || stack.getItem()==Items.SPLASH_POTION || stack.getItem()==ModItems.HEXCORE.get());
               default: return super.isItemValid(slot, stack);     
            }
         }

         @Override
         public int getSlotLimit(int slot) {
            switch (slot) {
               case 15: return 1;
               case 16: return 10;
               default: return super.getSlotLimit(slot);     
            }
         }
      };
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