package com.eximeisty.creaturesofruneterra.events;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import com.eximeisty.creaturesofruneterra.entity.custom.XerSaiDunebreakerEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.XerSaiHatchlingEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.XerxarethEntity;
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
    }

    @SubscribeEvent
    public static void onRegisterEntities(RegistryEvent.Register<EntityType<?>> event) {
        ModSpawnEggItem.initSpawnEggs();
    }
}