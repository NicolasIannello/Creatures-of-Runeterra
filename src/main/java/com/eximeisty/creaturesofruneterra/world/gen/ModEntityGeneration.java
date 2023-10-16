package com.eximeisty.creaturesofruneterra.world.gen;

import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class ModEntityGeneration {
    public static void onEntitySpawn(final BiomeLoadingEvent event) {
        addEntityToSpecificBiomes(event, ModEntityTypes.XERSAI_HATCHLING.get(), 35, 2, 4, Biomes.DESERT,Biomes.BADLANDS,Biomes.WOODED_BADLANDS,Biomes.ERODED_BADLANDS, Biomes.DRIPSTONE_CAVES);
        addEntityToSpecificBiomes(event, ModEntityTypes.PORO.get(), 45, 1, 3, Biomes.SNOWY_BEACH, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_SLOPES, Biomes.ICE_SPIKES, Biomes.FROZEN_PEAKS, Biomes.FROZEN_RIVER, Biomes.GROVE);
    }

    private static void addEntityToSpecificBiomes(BiomeLoadingEvent event, EntityType<?> type, int weight, int minCount, int maxCount, ResourceKey<Biome>... biomes) {
        boolean isBiomeSelected = Arrays.stream(biomes).map(ResourceKey::location).map(Object::toString).anyMatch(s -> s.equals(event.getName().toString()));

        if(isBiomeSelected) {
            addEntityToAllBiomes(event, type, weight, minCount, maxCount);
        }
    }

    private static void addEntityToAllBiomes(BiomeLoadingEvent event, EntityType<?> type, int weight, int minCount, int maxCount) {
        List<MobSpawnSettings.SpawnerData> base = event.getSpawns().getSpawner(type.getCategory());
        base.add(new MobSpawnSettings.SpawnerData(type,weight, minCount, maxCount));
    }
}