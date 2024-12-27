package com.pouffydev.star_shipments.data;

import com.pouffydev.star_shipments.StarShipments;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class StarBuiltinDataGen {
    public static void gatherDataEvent(DataGenerator dataGenerator, boolean includeServer, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        StarShipments.LOGGER.info("[S.T.A.R.] Starting Builtin Data Generation.");
        //dataGenerator.addProvider(includeServer,
        //        (DataProvider.Factory<DatapackBuiltinEntriesProvider>) output -> new DatapackBuiltinEntriesProvider(output, lookupProvider, new RegistrySetBuilder()
        //                .add()
        //                , Set.of(StarShipments.MODID))
        //);

    }
}
