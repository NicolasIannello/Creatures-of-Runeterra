package com.eximeisty.creaturesofruneterra.world.gen;

import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class ModEntityGeneration {
    public static void onEntitySpawn(final BiomeLoadingEvent event) {
        addEntityToSpecificBiomes(event, ModEntityTypes.XERSAI_HATCHLING.get(), 20, 1, 3, Biomes.DESERT,Biomes.DESERT_HILLS,Biomes.DESERT_LAKES,Biomes.BADLANDS,Biomes.BADLANDS_PLATEAU );
        addEntityToSpecificBiomes(event, ModEntityTypes.PORO.get(), 15, 1, 3, Biomes.SNOWY_BEACH, Biomes.SNOWY_MOUNTAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA_HILLS, Biomes.ICE_SPIKES, Biomes.SNOWY_TAIGA_MOUNTAINS, Biomes.FROZEN_RIVER, Biomes.SNOWY_TUNDRA);
        addEntityToSpecificBiomes(event, ModEntityTypes.SILVERWING.get(), 10, 1, 1, Biomes.BADLANDS, Biomes.DESERT, Biomes.ERODED_BADLANDS, Biomes.ICE_SPIKES, Biomes.PLAINS, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.SNOWY_TUNDRA, Biomes.SUNFLOWER_PLAINS, Biomes.WOODED_MOUNTAINS, Biomes.GRAVELLY_MOUNTAINS, Biomes.MOUNTAINS, Biomes.SHATTERED_SAVANNA, Biomes.WOODED_BADLANDS_PLATEAU);
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