package com.eximeisty.creaturesofruneterra.item.custom;

import com.eximeisty.creaturesofruneterra.item.client.drill.DrillItemRenderer;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class DrillItem extends BlockItem implements IAnimatable{
    public AnimationFactory factory = new AnimationFactory(this);

    public DrillItem(Block block, Properties settings) {
        super(block, settings.setISTER(()-> DrillItemRenderer::new));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    @Override @SuppressWarnings({"rawtypes","unchecked"})
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
    
}
