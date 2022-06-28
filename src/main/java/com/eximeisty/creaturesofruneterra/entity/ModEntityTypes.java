package com.eximeisty.creaturesofruneterra.entity;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.XerSaiHatchlingEntity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {
    public static DeferredRegister<EntityType<?>>ENTITY_TYPES= 
    DeferredRegister.create(ForgeRegistries.ENTITIES, CreaturesofRuneterra.MOD_ID);
    
    //xer'sai
    public static final RegistryObject<EntityType<XerSaiHatchlingEntity>> XERSAI_HATCHLING=
        ENTITY_TYPES.register("xersai_hatchling", 
            ()-> EntityType.Builder.create(XerSaiHatchlingEntity::new, EntityClassification.MONSTER).size(1.5f,1f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "xersai_hatchling").toString())
        );

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
