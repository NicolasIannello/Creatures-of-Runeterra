package com.eximeisty.creaturesofruneterra.events;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import com.eximeisty.creaturesofruneterra.entity.custom.*;
import com.eximeisty.creaturesofruneterra.item.custom.ModSpawnEggItem;

import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreaturesofRuneterra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.XERSAI_HATCHLING.get(), XerSaiHatchlingEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.XERXARETH.get(), XerxarethEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.XERSAI_DUNEBREAKER.get(), XerSaiDunebreakerEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.REKSAI.get(), RekSaiEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.PORO.get(), PoroEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.FABLEDPORO.get(), FabledPoroEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.PLUNDERPORO.get(), PlunderPoroEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.PATCHEDPOROBOT.get(), PatchedPorobotEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.EXALTEDPORO.get(), ExaltedPoroEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.FIDDLESTICKS.get(), FiddlesticksEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.FIDDLEDUMMY.get(), FiddleDummyEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.SILVERWING.get(), SilverwingEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.NAAFIRI.get(), NaafiriEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.NAAFIRI_HOUND.get(), NaafiriHoundEntity.setCustomAttributes().create());
        //PART ENTITIES
        event.put(ModEntityTypes.WIVHIV.get(), CoRPartEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.WVIHV.get(), CoRPartEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.WVIIHVI.get(), CoRPartEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.WIIIHIII.get(), CoRPartEntity.setCustomAttributes().create());
    }

    @SubscribeEvent
    public static void onRegisterEntities(RegistryEvent.Register<EntityType<?>> event) {
        ModSpawnEggItem.initSpawnEggs();
    }
}