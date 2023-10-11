package com.eximeisty.creaturesofruneterra.entity;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.HexcoreEntity;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static DeferredRegister<EntityType<?>>ENTITY_TYPES=
    DeferredRegister.create(ForgeRegistries.ENTITIES, CreaturesofRuneterra.MOD_ID);
    
    //Void creatures
//    public static final RegistryObject<EntityType<XerSaiHatchlingEntity>> XERSAI_HATCHLING=
//    ENTITY_TYPES.register("xersai_hatchling",
//        ()-> EntityType.Builder.of(XerSaiHatchlingEntity::new, MobCategory.MONSTER).sized(1.5f,1f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "xersai_hatchling").toString())
//    );
    
//    public static final RegistryObject<EntityType<XerxarethEntity>> XERXARETH=
//    ENTITY_TYPES.register("xerxareth",
//        ()-> EntityType.Builder.of(XerxarethEntity::new, MobCategory.MONSTER).sized(8.5f,5.5f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "xerxareth").toString())
//    );

//    public static final RegistryObject<EntityType<XerSaiDunebreakerEntity>> XERSAI_DUNEBREAKER=
//    ENTITY_TYPES.register("xersai_dunebreaker",
//        ()-> EntityType.Builder.of(XerSaiDunebreakerEntity::new, MobCategory.MONSTER).sized(3f,2.5f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "xersai_dunebreaker").toString())
//    );

    //BOSSES
//    public static final RegistryObject<EntityType<RekSaiEntity>> REKSAI=
//    ENTITY_TYPES.register("reksai",
//        ()-> EntityType.Builder.of(RekSaiEntity::new, MobCategory.MONSTER).immuneToFire().sized(1f,1f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "reksai").toString())
//    );
//    public static final RegistryObject<EntityType<FiddlesticksEntity>> FIDDLESTICKS=
//    ENTITY_TYPES.register("fiddlesticks",
//        ()-> EntityType.Builder.of(FiddlesticksEntity::new, MobCategory.MONSTER).sized(0.75f,2.5f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "fiddlesticks").toString())
//    );
//
//    //POROS
//    public static final RegistryObject<EntityType<PoroEntity>> PORO=
//    ENTITY_TYPES.register("poro",
//        ()-> EntityType.Builder.<PoroEntity>of(PoroEntity::new, MobCategory.CREATURE).sized(0.6f,0.8f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "poro").toString())
//    );
//
//    public static final RegistryObject<EntityType<FabledPoroEntity>> FABLEDPORO=
//    ENTITY_TYPES.register("fabledporo",
//        ()-> EntityType.Builder.of(FabledPoroEntity::new, MobCategory.CREATURE).sized(0.8f,1f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "fabledporo").toString())
//    );
//
//    public static final RegistryObject<EntityType<PlunderPoroEntity>> PLUNDERPORO=
//    ENTITY_TYPES.register("plunderporo",
//        ()-> EntityType.Builder.of(PlunderPoroEntity::new, MobCategory.CREATURE).sized(0.6f,0.8f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "plunderporo").toString())
//    );
//
//    public static final RegistryObject<EntityType<PatchedPorobotEntity>> PATCHEDPOROBOT=
//    ENTITY_TYPES.register("patchedporobot",
//        ()-> EntityType.Builder.of(PatchedPorobotEntity::new, MobCategory.CREATURE).sized(0.6f,0.8f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "patchedporobot").toString())
//    );
//
//    public static final RegistryObject<EntityType<ExaltedPoroEntity>> EXALTEDPORO=
//    ENTITY_TYPES.register("exaltedporo",
//        ()-> EntityType.Builder.of(ExaltedPoroEntity::new, MobCategory.CREATURE).sized(0.6f,0.8f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "exaltedporo").toString())
//    );
//
//    //PART ENTITIES
//    public static final RegistryObject<EntityType<CoRPartEntity>> WIVHIV=
//    ENTITY_TYPES.register("wichiv",
//        ()-> EntityType.Builder.of(CoRPartEntity::new, MobCategory.MONSTER).immuneToFire().sized(4f,4f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "wichiv").toString())
//    );
//    public static final RegistryObject<EntityType<CoRPartEntity>> WVIHV=
//    ENTITY_TYPES.register("wvihv",
//        ()-> EntityType.Builder.of(CoRPartEntity::new, MobCategory.MONSTER).immuneToFire().sized(6f,5f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "wvihv").toString())
//    );
//    public static final RegistryObject<EntityType<CoRPartEntity>> WVIIHVI=
//    ENTITY_TYPES.register("wviihvi",
//        ()-> EntityType.Builder.of(CoRPartEntity::new, MobCategory.MONSTER).immuneToFire().sized(7f,6f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "wviihvi").toString())
//    );
//    public static final RegistryObject<EntityType<CoRPartEntity>> WIIIHIII=
//    ENTITY_TYPES.register("wiiihiii",
//        ()-> EntityType.Builder.of(CoRPartEntity::new, MobCategory.MONSTER).immuneToFire().sized(3f,3f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "wiiihiii").toString())
//    );
//    public static final RegistryObject<EntityType<FiddleDummyEntity>> FIDDLEDUMMY=
//    ENTITY_TYPES.register("fiddle_dummy",
//        ()-> EntityType.Builder.of(FiddleDummyEntity::new, MobCategory.MONSTER).sized(0.75f,2.5f).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "fiddle_dummy").toString())
//    );
//
//    //ITEMS
//    public static final RegistryObject<EntityType<MisilEntity>> MISIL =
//    ENTITY_TYPES.register("misil",
//        ()-> EntityType.Builder.<MisilEntity>of(MisilEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).setTrackingRange(4).updateInterval(20).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "misil").toString())
//    );
//
//    public static final RegistryObject<EntityType<DBShieldEntity>> DBSHIELD =
//    ENTITY_TYPES.register("dbshield",
//        ()-> EntityType.Builder.<DBShieldEntity>of(DBShieldEntity::new, MobCategory.MISC).sized(2.5F, 2.0F).setTrackingRange(4).updateInterval(20).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "dbshield").toString())
//    );

    public static final RegistryObject<EntityType<HexcoreEntity>> HEXCORE = 
    ENTITY_TYPES.register("hexcore", 
        ()-> EntityType.Builder.<HexcoreEntity>of(HexcoreEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).updateInterval(10).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "hexcore").toString())
    );

//    public static final RegistryObject<EntityType<BulletEntity>> BULLET =
//    ENTITY_TYPES.register("bullet",
//        ()-> EntityType.Builder.<BulletEntity>of(BulletEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).updateInterval(20).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "bullet").toString())
//    );
//
//    public static final RegistryObject<EntityType<FiddleProyectileEntity>> FIDDLE_PROYECTILE =
//    ENTITY_TYPES.register("fiddle_proyectile",
//        ()-> EntityType.Builder.<FiddleProyectileEntity>of(FiddleProyectileEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(8).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "fiddle_proyectile").toString())
//    );

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
