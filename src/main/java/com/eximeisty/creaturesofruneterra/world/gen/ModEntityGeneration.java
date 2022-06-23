package com.eximeisty.creaturesofruneterra.world.gen;

import net.minecraft.entity.EntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.Arrays;
import java.util.List;

import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;

public class ModEntityGeneration {
    public static void onEntitySpawn(final BiomeLoadingEvent event) {
        addEntityToSpecificBiomes(event, ModEntityTypes.XERSAI_HATCHLING.get(), 40, 3, 5, Biomes.DESERT,Biomes.DESERT_HILLS,Biomes.DESERT_LAKES);
    }
    
    private static void addEntityToSpecificBiomes(BiomeLoadingEvent event, EntityType<?> type, int weight, int minCount, int maxCount, RegistryKey<Biome>... biomes) {
        boolean isBiomeSelected = Arrays.stream(biomes).map(RegistryKey::getLocation).map(Object::toString).anyMatch(s -> s.equals(event.getName().toString()));

        if(isBiomeSelected) {
            addEntityToAllBiomes(event.getSpawns(), type, weight, minCount, maxCount);
        }
    }

    private static void addEntityToAllBiomes(MobSpawnInfoBuilder spawns, EntityType<?> type, int weight, int minCount, int maxCount) {
        List<MobSpawnInfo.Spawners> base = spawns.getSpawner(type.getClassification());
        base.add(new MobSpawnInfo.Spawners(type,weight, minCount, maxCount));
    }
}
