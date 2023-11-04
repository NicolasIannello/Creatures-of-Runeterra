package com.eximeisty.creaturesofruneterra.item.custom;

import com.eximeisty.creaturesofruneterra.entity.custom.FiddleProyectileEntity;
import com.eximeisty.creaturesofruneterra.item.client.armor.fiddle.FiddleArmorRenderer;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class FiddleArmorItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    final String quote = "["+Minecraft.getInstance().options.keyShift.getKey().toString().replace("keyboard.", "").replace("."," ").replace("key","")+" ]+["+Minecraft.getInstance().options.keyPickItem.getKey().toString().replace("keyboard.", "").replace("."," ").replace("key","")+" ] to use hability";//"[SHIFT]+[LClick] to use hability";
    public String controllerName = "controller";
    public int cd=0;
    public int tiros=0;
    private List<Entity> targets = Lists.newArrayList();
    //private static final AnimationBuilder ANIM = new AnimationBuilder().addAnimation("animation.fiddle_armor2.open", false);

    public FiddleArmorItem(ArmorMaterial materialIn, Type slot, Properties builder) {
        super(materialIn, slot, builder);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private FiddleArmorRenderer renderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null) this.renderer = new FiddleArmorRenderer();

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    private PlayState predicate(AnimationState animationState) {
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(this.getEquipmentSlot()==EquipmentSlot.CHEST && entityIn.getArmorSlots().toString().contains("fiddle_birdcage") && Minecraft.getInstance().options.keyShift.isDown() && Minecraft.getInstance().options.keyPickItem.isDown() && cd<=0 && !worldIn.isClientSide){
            worldIn.getEntities(null, entityIn.getBoundingBox().inflate(5)).stream().forEach(entity ->{
                if(targets.size()<6 && entity!=entityIn)targets.add(entity);
            });
            cd=700;
        }
        if(!targets.isEmpty()){
            worldIn.addFreshEntity(new FiddleProyectileEntity(worldIn, (LivingEntity)entityIn, targets.get(0), Direction.DOWN.getAxis(), null));
            worldIn.playSound(null, entityIn.blockPosition(), SoundEvents.SOUL_ESCAPE, SoundSource.AMBIENT, (float)(Math.random() * 5)+5, (float)(Math.random() * 2)+1);
            tiros++;
            if(tiros>3){
                targets.remove(0);
                tiros=0;
            }
        }
        if(cd>0) cd--;
    }

    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if(this.getEquipmentSlot()==EquipmentSlot.CHEST && this.getMaterial()==ArmorMaterials.NETHERITE) tooltip.add(Component.nullToEmpty(quote));
	}

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return this.getMaterial()==ArmorMaterials.CHAIN ? Items.CHAIN==repair.getItem() : false;
    }
}