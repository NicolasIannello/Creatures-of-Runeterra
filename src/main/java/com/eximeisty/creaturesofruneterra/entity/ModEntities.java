package com.eximeisty.creaturesofruneterra.entity;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.XerSaiDunebreakerEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES=
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CreaturesofRuneterra.MOD_ID);

    //Void creatures
    public static final RegistryObject<EntityType<XerSaiDunebreakerEntity>> XERSAI_DUNEBREAKER =
            ENTITY_TYPES.register("xersai_dunebreaker",
                    () -> EntityType.Builder.of(XerSaiDunebreakerEntity::new, MobCategory.CREATURE).sized(3f, 2.5f)
                            .build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "xersai_dunebreaker").toString()));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
