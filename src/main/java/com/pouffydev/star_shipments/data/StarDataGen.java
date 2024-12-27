package com.pouffydev.star_shipments.data;

import com.pouffydev.star_shipments.StarShipments;
import com.pouffydev.star_shipments.data.client.StarBlockStateGen;
import com.pouffydev.star_shipments.data.client.StarItemModelGen;
import com.pouffydev.star_shipments.data.client.StarLangGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class StarDataGen {
    public static void gatherDataEvent(GatherDataEvent event) {
        StarShipments.LOGGER.info("[S.T.A.R.] Data Generation starting.");
        String modId = StarShipments.MODID;
        DataGenerator dataGenerator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        PackOutput packOutput = dataGenerator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        dataGenerator.addProvider(event.includeClient() && event.includeServer(), new StarLangGen(packOutput, modId, "en_us"));
        dataGenerator.addProvider(event.includeClient(), new StarItemModelGen(packOutput, modId, fileHelper));
        dataGenerator.addProvider(event.includeClient(), new StarBlockStateGen(packOutput, modId, fileHelper));
        StarBuiltinDataGen.gatherDataEvent(dataGenerator, event.includeServer(), lookupProvider);
    }
}
