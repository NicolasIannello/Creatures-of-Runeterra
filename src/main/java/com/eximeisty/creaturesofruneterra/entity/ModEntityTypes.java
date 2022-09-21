package com.eximeisty.creaturesofruneterra.entity;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.HexcoreEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.MisilEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.RekSaiEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.CoRPartEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.DBShieldEntity;
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
        ()-> EntityType.Builder.create(RekSaiEntity::new, EntityClassification.MONSTER).immuneToFire().size(0.6f,2f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "reksai").toString())
    );

    //PART ENTITIES
    public static final RegistryObject<EntityType<CoRPartEntity>> WIVHIV=
    ENTITY_TYPES.register("wichiv", 
        ()-> EntityType.Builder.create(CoRPartEntity::new, EntityClassification.MONSTER).immuneToFire().size(4f,4f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "wichiv").toString())
    );
    public static final RegistryObject<EntityType<CoRPartEntity>> WVIHV=
    ENTITY_TYPES.register("wvihv", 
        ()-> EntityType.Builder.create(CoRPartEntity::new, EntityClassification.MONSTER).immuneToFire().size(6f,5f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "wvihv").toString())
    );
    public static final RegistryObject<EntityType<CoRPartEntity>> WVIIHVI=
    ENTITY_TYPES.register("wviihvi", 
        ()-> EntityType.Builder.create(CoRPartEntity::new, EntityClassification.MONSTER).immuneToFire().size(7f,6f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "wviihvi").toString())
    );
    public static final RegistryObject<EntityType<CoRPartEntity>> WIIIHIII=
    ENTITY_TYPES.register("wiiihiii", 
        ()-> EntityType.Builder.create(CoRPartEntity::new, EntityClassification.MONSTER).immuneToFire().size(3f,3f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "wiiihiii").toString())
    );

    //ITEMS
    public static final RegistryObject<EntityType<MisilEntity>> MISIL = 
    ENTITY_TYPES.register("misil", 
        ()-> EntityType.Builder.<MisilEntity>create(MisilEntity::new, EntityClassification.MISC).size(2.0F, 1.5F).trackingRange(4).updateInterval(20).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "misil").toString())
    );

    public static final RegistryObject<EntityType<DBShieldEntity>> DBSHIELD = 
    ENTITY_TYPES.register("dbshield", 
        ()-> EntityType.Builder.<DBShieldEntity>create(DBShieldEntity::new, EntityClassification.MISC).size(2.5F, 2.0F).trackingRange(4).updateInterval(20).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "dbshield").toString())
    );

    public static final RegistryObject<EntityType<HexcoreEntity>> HEXCORE = 
    ENTITY_TYPES.register("hexcore", 
        ()-> EntityType.Builder.<HexcoreEntity>create(HexcoreEntity::new, EntityClassification.MISC).size(0.25F, 0.25F).trackingRange(4).updateInterval(10).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "hexcore").toString())
    );

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
