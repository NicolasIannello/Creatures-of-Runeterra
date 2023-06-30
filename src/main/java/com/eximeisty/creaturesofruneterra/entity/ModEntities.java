package com.eximeisty.creaturesofruneterra.entity;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.*;
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
    public static final RegistryObject<EntityType<RekSaiEntity>> REKSAI=
            ENTITY_TYPES.register("reksai",
                    ()-> EntityType.Builder.of(RekSaiEntity::new, MobCategory.MONSTER).fireImmune().sized(1f,1f)
                            .build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "reksai").toString())
            );

    //POROS
    public static final RegistryObject<EntityType<PoroEntity>> PORO=
            ENTITY_TYPES.register("poro",
                    ()-> EntityType.Builder.of(PoroEntity::new, MobCategory.CREATURE).sized(0.8f,1f)
                            .build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "poro").toString()));
    public static final RegistryObject<EntityType<FabledPoroEntity>> FABLEDPORO=
            ENTITY_TYPES.register("fabledporo",
                    ()-> EntityType.Builder.of(FabledPoroEntity::new, MobCategory.CREATURE).sized(0.8f,1f)
                            .build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "fabledporo").toString()));
    public static final RegistryObject<EntityType<PlunderPoroEntity>> PLUNDERPORO=
            ENTITY_TYPES.register("plunderporo",
                    ()-> EntityType.Builder.of(PlunderPoroEntity::new, MobCategory.CREATURE).sized(0.6f,0.8f)
                            .build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "plunderporo").toString())
            );
    public static final RegistryObject<EntityType<ExaltedPoroEntity>> EXALTEDPORO=
            ENTITY_TYPES.register("exaltedporo",
                    ()-> EntityType.Builder.of(ExaltedPoroEntity::new, MobCategory.CREATURE).sized(0.6f,0.8f)
                            .build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "exaltedporo").toString())
            );

    //PART ENTITIES
    public static final RegistryObject<EntityType<CoRPartEntity>> WIVHIV=
            ENTITY_TYPES.register("wichiv",
                    ()-> EntityType.Builder.of(CoRPartEntity::new, MobCategory.MONSTER).fireImmune().sized(4f,4f)
                            .build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "wichiv").toString())
            );
    public static final RegistryObject<EntityType<CoRPartEntity>> WVIHV=
            ENTITY_TYPES.register("wvihv",
                    ()-> EntityType.Builder.of(CoRPartEntity::new, MobCategory.MONSTER).fireImmune().sized(6f,5f)
                            .build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "wvihv").toString())
            );
    public static final RegistryObject<EntityType<CoRPartEntity>> WVIIHVI=
            ENTITY_TYPES.register("wviihvi",
                    ()-> EntityType.Builder.of(CoRPartEntity::new, MobCategory.MONSTER).fireImmune().sized(7f,6f)
                            .build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "wviihvi").toString())
            );
    public static final RegistryObject<EntityType<CoRPartEntity>> WIIIHIII=
            ENTITY_TYPES.register("wiiihiii",
                    ()-> EntityType.Builder.of(CoRPartEntity::new, MobCategory.MONSTER).fireImmune().sized(3f,3f)
                            .build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "wiiihiii").toString())
            );

    //ITEMS
    public static final RegistryObject<EntityType<BulletEntity>> BULLET =
            ENTITY_TYPES.register("bullet",
                    ()-> EntityType.Builder.<BulletEntity>of(BulletEntity::new, MobCategory.MISC).sized(0.25F, 0.25F)
                            .setTrackingRange(4).updateInterval(20).build(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "bullet").toString())
            );

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}