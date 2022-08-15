package com.eximeisty.creaturesofruneterra.entity;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.MisilEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.RekSaiEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.XerSaiDunebreakerEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.XerSaiHatchlingEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.XerxarethEntity;

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
    
    //Void creatures
    public static final RegistryObject<EntityType<XerSaiHatchlingEntity>> XERSAI_HATCHLING=
    ENTITY_TYPES.register("xersai_hatchling", 
        ()-> EntityType.Builder.create(XerSaiHatchlingEntity::new, EntityClassification.MONSTER).size(1.5f,1f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "xersai_hatchling").toString())
    );
    
    public static final RegistryObject<EntityType<XerxarethEntity>> XERXARETH=
    ENTITY_TYPES.register("xerxareth", 
        ()-> EntityType.Builder.create(XerxarethEntity::new, EntityClassification.MONSTER).size(8.5f,5.5f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "xerxareth").toString())
    );

    public static final RegistryObject<EntityType<XerSaiDunebreakerEntity>> XERSAI_DUNEBREAKER=
    ENTITY_TYPES.register("xersai_dunebreaker", 
        ()-> EntityType.Builder.create(XerSaiDunebreakerEntity::new, EntityClassification.MONSTER).size(3f,2.5f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "xersai_dunebreaker").toString())
    );

    public static final RegistryObject<EntityType<RekSaiEntity>> REKSAI=
    ENTITY_TYPES.register("reksai", 
        ()-> EntityType.Builder.create(RekSaiEntity::new, EntityClassification.MONSTER).size(8f,13f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "reksai").toString())
    );

    public static final RegistryObject<EntityType<MisilEntity>> MISIL = 
    ENTITY_TYPES.register("misil", 
        ()-> EntityType.Builder.<MisilEntity>create(MisilEntity::new, EntityClassification.MISC).size(4.0F, 2.0F).trackingRange(4).updateInterval(20).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "misil").toString())
    );


    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
