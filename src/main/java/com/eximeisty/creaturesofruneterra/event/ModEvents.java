package com.eximeisty.creaturesofruneterra.event;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.ModEntities;
import com.eximeisty.creaturesofruneterra.entity.custom.*;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreaturesofRuneterra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.XERSAI_DUNEBREAKER.get(), XerSaiDunebreakerEntity.setAttributes());
        event.put(ModEntities.PORO.get(), PoroEntity.setAttributes());
        event.put(ModEntities.FABLEDPORO.get(), FabledPoroEntity.setAttributes());
        event.put(ModEntities.PLUNDERPORO.get(), PlunderPoroEntity.setAttributes());
        event.put(ModEntities.EXALTEDPORO.get(), ExaltedPoroEntity.setAttributes());
    }
}