package com.eximeisty.creaturesofruneterra.events;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import com.eximeisty.creaturesofruneterra.entity.custom.RekSaiEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.CoRPartEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.PoroEntity;
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
        event.put(ModEntityTypes.REKSAI.get(), RekSaiEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.PORO.get(), PoroEntity.setCustomAttributes().create());
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