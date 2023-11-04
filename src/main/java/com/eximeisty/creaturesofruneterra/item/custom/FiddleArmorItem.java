package com.eximeisty.creaturesofruneterra.item.custom;

import java.util.List;

import com.eximeisty.creaturesofruneterra.entity.custom.FiddleProyectileEntity;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;

public class FiddleArmorItem extends GeoArmorItem implements IAnimatable{
    private AnimationFactory factory = new AnimationFactory(this);
    final String quote = "["+Minecraft.getInstance().gameSettings.keyBindSneak.getKeyDescription().replace("key.", "")+"]+["+Minecraft.getInstance().gameSettings.keyBindPickBlock.getKeyDescription().replace("key.", "")+"] to use hability";//"[SHIFT]+[LClick] to use hability";
    public String controllerName = "controller";
    public int cd=0;
    public int tiros=0;
    private List<Entity> targets = Lists.newArrayList();
    //private static final AnimationBuilder ANIM = new AnimationBuilder().addAnimation("animation.fiddle_armor2.open", false);

    public FiddleArmorItem(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builder) {
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

    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(this.getEquipmentSlot()==EquipmentSlotType.CHEST && entityIn.getArmorInventoryList().toString().contains("fiddle_birdcage") && Minecraft.getInstance().gameSettings.keyBindSneak.isKeyDown() && Minecraft.getInstance().gameSettings.keyBindPickBlock.isKeyDown() && cd<=0 && !worldIn.isRemote){
                worldIn.getEntitiesWithinAABB(LivingEntity.class, entityIn.getBoundingBox().grow(5)).stream().forEach(entity ->{
                    if(targets.size()<6 && entity!=entityIn)targets.add(entity); 
                });
            cd=1000;
        }
        if(!targets.isEmpty()){
            worldIn.addEntity(new FiddleProyectileEntity(worldIn, (LivingEntity)entityIn, targets.get(0), Direction.DOWN.getAxis(), null));
            worldIn.playSound(null, entityIn.getPosition(), SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.AMBIENT, (float)(Math.random() * 5)+5, (float)(Math.random() * 2)+1);
            tiros++;
            if(tiros>3){
                targets.remove(0);
                tiros=0;
            }
        }
        if(cd>0) cd--;
    }

    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(this.getEquipmentSlot()==EquipmentSlotType.CHEST && this.getArmorMaterial()==ArmorMaterial.NETHERITE) tooltip.add(new TranslationTextComponent(quote));
	}

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) { 
        return this.getArmorMaterial()==ArmorMaterial.CHAIN ? Items.CHAIN==repair.getItem() : false; 
    }
}