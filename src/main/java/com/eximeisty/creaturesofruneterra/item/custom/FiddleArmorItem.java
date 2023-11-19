package com.eximeisty.creaturesofruneterra.item.custom;

import com.eximeisty.creaturesofruneterra.entity.custom.FiddleProyectileEntity;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

public class FiddleArmorItem extends GeoArmorItem implements IAnimatable{
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public String controllerName = "controller";
    public int cd=0;
    public int tiros=0;
    private List<Entity> targets = Lists.newArrayList();
    //private static final AnimationBuilder ANIM = new AnimationBuilder().addAnimation("animation.fiddle_armor2.open", false);

    public FiddleArmorItem(ArmorMaterial materialIn, EquipmentSlot slot, Properties builder) {
        super(materialIn, slot, builder);
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<IAnimatable>(this, controllerName, 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(this.getSlot()==EquipmentSlot.CHEST && entityIn.getArmorSlots().toString().contains("fiddle_birdcage") && Minecraft.getInstance().options.keyShift.isDown() && Minecraft.getInstance().options.keyPickItem.isDown() && cd<=0 && !worldIn.isClientSide){
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
        String quote = "["+Minecraft.getInstance().options.keyShift.getKey().toString().replace("keyboard.", "").replace("."," ").replace("key","")+" ]+["+Minecraft.getInstance().options.keyPickItem.getKey().toString().replace("keyboard.", "").replace("."," ").replace("key","")+" ] to use hability";//"[SHIFT]+[LClick] to use hability";
        if(this.getSlot()==EquipmentSlot.CHEST && this.getMaterial()==ArmorMaterials.NETHERITE) tooltip.add(Component.nullToEmpty(quote));
	}

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return this.getMaterial()==ArmorMaterials.CHAIN ? Items.CHAIN==repair.getItem() : false;
    }
}