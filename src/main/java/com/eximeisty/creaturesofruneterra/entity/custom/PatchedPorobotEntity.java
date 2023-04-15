package com.eximeisty.creaturesofruneterra.entity.custom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.eximeisty.creaturesofruneterra.container.PorobotContainer;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
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
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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

   public PatchedPorobotEntity(EntityType<? extends TameableEntity> type, World worldIn) {
      super(type, worldIn);
      this.setTamed(false);
   }

   public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
      return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 50)
      .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(1, new PanicGoal(this, 1.2D));
      this.goalSelector.addGoal(0, new SwimGoal(this));
      this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
   }

   public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.poro.idle", true));
      return PlayState.CONTINUE;
   }

   @Override
   public void registerControllers(AnimationData data) {
      data.addAnimationController(new AnimationController<IAnimatable>(this, "controller", 0, this::predicate));
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
   }
/*-------------------------------------------------------------------------------------- */
   public ActionResultType getEntityInteractionResult(PlayerEntity playerIn, Hand hand) {
      if (!world.isRemote) {
         INamedContainerProvider containerProvider = createContainerProvider(world, this.getEntityId());
         NetworkHooks.openGui((ServerPlayerEntity)playerIn, containerProvider, buf -> buf.writeInt(this.getEntityId()));
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
            return new TranslationTextComponent(world.getEntityByID(id).getDisplayName()+"'s insides");
         }
         
      };
   }

   @Override
   public void readAdditional(CompoundNBT compound) {
      itemHandler.deserializeNBT(compound.getCompound("inv"));
      super.readAdditional(compound);
   }

   @Override
   public void writeAdditional(CompoundNBT compound) {
      compound.put("inv", itemHandler.serializeNBT());
      super.writeAdditional(compound);
   }

   private ItemStackHandler createHandler(){
      return new ItemStackHandler(2){//might need to change later
         @Override
         protected void onContentsChanged(int slot){
            markLoadedDirty();
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
      this.entityDropItem(new ItemStack(Items.OAK_WOOD, 9));
   }
/*-------------------------------------------------------------------------------------- */
   protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_BAT_DEATH; }
   protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SoundEvents.ENTITY_FOX_HURT; }
   public SoundEvent getAmbientSound() { return SoundEvents.ENTITY_FOX_AMBIENT; }
}