package com.eximeisty.creaturesofruneterra.event;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.ModEntities;
import com.eximeisty.creaturesofruneterra.entity.custom.*;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreaturesofRuneterra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.XERSAI_HATCHLING.get(), XerSaiHatchlingEntity.setAttributes());
//        event.put(ModEntities.XERXARETH.get(), XerxarethEntity.setAttributes());
        event.put(ModEntities.XERSAI_DUNEBREAKER.get(), XerSaiDunebreakerEntity.setAttributes());
        event.put(ModEntities.REKSAI.get(), RekSaiEntity.setAttributes());
        event.put(ModEntities.PORO.get(), PoroEntity.setAttributes());
        event.put(ModEntities.FABLEDPORO.get(), FabledPoroEntity.setAttributes());
        event.put(ModEntities.PLUNDERPORO.get(), PlunderPoroEntity.setAttributes());
        event.put(ModEntities.PATCHEDPOROBOT.get(), PatchedPorobotEntity.setAttributes());
        event.put(ModEntities.EXALTEDPORO.get(), ExaltedPoroEntity.setAttributes());
        event.put(ModEntities.FIDDLESTICKS.get(), FiddlesticksEntity.setAttributes());
        event.put(ModEntities.FIDDLEDUMMY.get(), FiddleDummyEntity.setAttributes());
        event.put(ModEntities.SILVERWING.get(), SilverwingEntity.setAttributes());
        event.put(ModEntities.NAAFIRI.get(), NaafiriEntity.setAttributes());
        event.put(ModEntities.NAAFIRI_HOUND.get(), NaafiriHoundEntity.setAttributes());
        //PART ENTITIES
        event.put(ModEntities.WIVHIV.get(), CoRPartEntity.setAttributes());
        event.put(ModEntities.WVIHV.get(), CoRPartEntity.setAttributes());
        event.put(ModEntities.WVIIHVI.get(), CoRPartEntity.setAttributes());
        event.put(ModEntities.WIIIHIII.get(), CoRPartEntity.setAttributes());
    }

    @SubscribeEvent
    public static void entitySpawnRestriction(SpawnPlacementRegisterEvent event) {
        event.register(ModEntities.PORO.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ModEntities.SILVERWING.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
}