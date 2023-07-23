package com.eximeisty.creaturesofruneterra.entity;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.HexcoreEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.MisilEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.PatchedPorobotEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.PlunderPoroEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.PoroEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.RekSaiEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.BulletEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.CoRPartEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.DBShieldEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.ExaltedPoroEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.FabledPoroEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.FiddleProyectileEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.FiddlesticksEntity;
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

    //BOSSES
    public static final RegistryObject<EntityType<RekSaiEntity>> REKSAI=
    ENTITY_TYPES.register("reksai", 
        ()-> EntityType.Builder.create(RekSaiEntity::new, EntityClassification.MONSTER).immuneToFire().size(1f,1f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "reksai").toString())
    );
    public static final RegistryObject<EntityType<FiddlesticksEntity>> FIDDLESTICKS=
    ENTITY_TYPES.register("fiddlesticks", 
        ()-> EntityType.Builder.create(FiddlesticksEntity::new, EntityClassification.MONSTER).size(0.75f,2.5f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "reksai").toString())
    );

    //POROS
    public static final RegistryObject<EntityType<PoroEntity>> PORO=
    ENTITY_TYPES.register("poro", 
        ()-> EntityType.Builder.<PoroEntity>create(PoroEntity::new, EntityClassification.CREATURE).size(0.6f,0.8f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "poro").toString())
    );

    public static final RegistryObject<EntityType<FabledPoroEntity>> FABLEDPORO=
    ENTITY_TYPES.register("fabledporo", 
        ()-> EntityType.Builder.create(FabledPoroEntity::new, EntityClassification.CREATURE).size(0.8f,1f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "fabledporo").toString())
    );

    public static final RegistryObject<EntityType<PlunderPoroEntity>> PLUNDERPORO=
    ENTITY_TYPES.register("plunderporo", 
        ()-> EntityType.Builder.create(PlunderPoroEntity::new, EntityClassification.CREATURE).size(0.6f,0.8f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "plunderporo").toString())
    );

    public static final RegistryObject<EntityType<PatchedPorobotEntity>> PATCHEDPOROBOT=
    ENTITY_TYPES.register("patchedporobot", 
        ()-> EntityType.Builder.create(PatchedPorobotEntity::new, EntityClassification.CREATURE).size(0.6f,0.8f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "patchedporobot").toString())
    );

    public static final RegistryObject<EntityType<ExaltedPoroEntity>> EXALTEDPORO=
    ENTITY_TYPES.register("exaltedporo",
        ()-> EntityType.Builder.create(ExaltedPoroEntity::new, EntityClassification.CREATURE).size(0.6f,0.8f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "exaltedporo").toString())
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
        ()-> EntityType.Builder.<MisilEntity>create(MisilEntity::new, EntityClassification.MISC).size(0.5F, 0.5F).trackingRange(4).updateInterval(20).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "misil").toString())
    );

    public static final RegistryObject<EntityType<DBShieldEntity>> DBSHIELD = 
    ENTITY_TYPES.register("dbshield", 
        ()-> EntityType.Builder.<DBShieldEntity>create(DBShieldEntity::new, EntityClassification.MISC).size(2.5F, 2.0F).trackingRange(4).updateInterval(20).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "dbshield").toString())
    );

    public static final RegistryObject<EntityType<HexcoreEntity>> HEXCORE = 
    ENTITY_TYPES.register("hexcore", 
        ()-> EntityType.Builder.<HexcoreEntity>create(HexcoreEntity::new, EntityClassification.MISC).size(0.25F, 0.25F).trackingRange(4).updateInterval(10).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "hexcore").toString())
    );

    public static final RegistryObject<EntityType<BulletEntity>> BULLET = 
    ENTITY_TYPES.register("bullet", 
        ()-> EntityType.Builder.<BulletEntity>create(BulletEntity::new, EntityClassification.MISC).size(0.25F, 0.25F).trackingRange(4).updateInterval(20).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "bullet").toString())
    );

    public static final RegistryObject<EntityType<FiddleProyectileEntity>> FIDDLE_PROYECTILE = 
    ENTITY_TYPES.register("fiddle_proyectile", 
        ()-> EntityType.Builder.<FiddleProyectileEntity>create(FiddleProyectileEntity::new, EntityClassification.MISC).size(0.25F, 0.25F).trackingRange(8).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "fiddle_proyectile").toString())
    );

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
