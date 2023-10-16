package com.eximeisty.creaturesofruneterra.events;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import com.eximeisty.creaturesofruneterra.entity.custom.*;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreaturesofRuneterra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.XERSAI_HATCHLING.get(), XerSaiHatchlingEntity.setAttributes());
//        event.put(ModEntityTypes.XERXARETH.get(), XerxarethEntity.setAttributes());
        event.put(ModEntityTypes.XERSAI_DUNEBREAKER.get(), XerSaiDunebreakerEntity.setAttributes());
//        event.put(ModEntityTypes.REKSAI.get(), RekSaiEntity.setAttributes());
        event.put(ModEntityTypes.PORO.get(), PoroEntity.setAttributes());
        event.put(ModEntityTypes.FABLEDPORO.get(), FabledPoroEntity.setAttributes());
        event.put(ModEntityTypes.PLUNDERPORO.get(), PlunderPoroEntity.setAttributes());
//        event.put(ModEntityTypes.PATCHEDPOROBOT.get(), PatchedPorobotEntity.setAttributes());
        event.put(ModEntityTypes.EXALTEDPORO.get(), ExaltedPoroEntity.setAttributes());
//        event.put(ModEntityTypes.FIDDLESTICKS.get(), FiddlesticksEntity.setAttributes());
//        event.put(ModEntityTypes.FIDDLEDUMMY.get(), FiddleDummyEntity.setAttributes());
        //PART ENTITIES
//        event.put(ModEntityTypes.WIVHIV.get(), CoRPartEntity.setAttributes());
//        event.put(ModEntityTypes.WVIHV.get(), CoRPartEntity.setAttributes());
//        event.put(ModEntityTypes.WVIIHVI.get(), CoRPartEntity.setAttributes());
//        event.put(ModEntityTypes.WIIIHIII.get(), CoRPartEntity.setAttributes());
    }
}