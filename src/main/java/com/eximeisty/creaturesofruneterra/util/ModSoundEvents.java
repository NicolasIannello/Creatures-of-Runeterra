package com.eximeisty.creaturesofruneterra.util;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSoundEvents {
    public static final DeferredRegister<SoundEvent>  SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CreaturesofRuneterra.MOD_ID);

    //Rek'sai
    public static final RegistryObject<SoundEvent> REKSAI_APP = registerSoundEvent("reksai_app");
    public static final RegistryObject<SoundEvent> REKSAI_AWAKEN = registerSoundEvent("reksai_awaken");
    public static final RegistryObject<SoundEvent> REKSAI_ESCAPE = registerSoundEvent("reksai_escape");
    public static final RegistryObject<SoundEvent> REKSAI_HIT = registerSoundEvent("reksai_hit");
    public static final RegistryObject<SoundEvent> REKSAI_JUMP = registerSoundEvent("reksai_jump");
    public static final RegistryObject<SoundEvent> REKSAI_ANGRY = registerSoundEvent("reksai_angry");
    //Fiddle
    public static final RegistryObject<SoundEvent> FIDDLESTICKS_ATTACK = registerSoundEvent("fiddlesticks_attack");
    public static final RegistryObject<SoundEvent> FIDDLESTICKS_CHANNEL = registerSoundEvent("fiddlesticks_channel");
    public static final RegistryObject<SoundEvent> FIDDLESTICKS_DEATH = registerSoundEvent("fiddlesticks_death");
    public static final RegistryObject<SoundEvent> FIDDLESTICKS_HURT = registerSoundEvent("fiddlesticks_hurt");
    public static final RegistryObject<SoundEvent> FIDDLESTICKS_RUN = registerSoundEvent("fiddlesticks_run");
    public static final RegistryObject<SoundEvent> FIDDLESTICKS_LINE = registerSoundEvent("fiddlesticks_line");
    //Naafiri
    public static final RegistryObject<SoundEvent> NAAFIRI_ATTACK = registerSoundEvent("naafiri_attack");
    public static final RegistryObject<SoundEvent> NAAFIRI_DEATH = registerSoundEvent("naafiri_death");
    public static final RegistryObject<SoundEvent> NAAFIRI_SPAWN = registerSoundEvent("naafiri_spawn");
    public static final RegistryObject<SoundEvent> NAAFIRI_HURT = registerSoundEvent("naafiri_hurt");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name){
        return SOUND_EVENTS.register(name, ()-> new SoundEvent(new ResourceLocation(CreaturesofRuneterra.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus){
        SOUND_EVENTS.register(eventBus);
    }
}