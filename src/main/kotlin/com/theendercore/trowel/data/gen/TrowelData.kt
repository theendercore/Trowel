package com.theendercore.trowel.data.gen

import com.theendercore.trowel.Trowel
import com.theendercore.trowel.Trowel.MODID
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementEntry
import net.minecraft.advancement.AdvancementFrame
import net.minecraft.advancement.criterion.InventoryChangedCriterion
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.RegistryWrapper
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer


class TrowelData : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        val pack = gen.createPack()

        pack.addProvider(::RecipeProvider)
        pack.addProvider(::ModelProvider)
        pack.addProvider(::AdvancementProvider)
    }
}

class RecipeProvider(o: FabricDataOutput, r: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricRecipeProvider(o, r) {
    override fun generate(c: RecipeExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, Trowel.TROWEL)
            .pattern(" II")
            .pattern("S  ")
            .input('I', Items.IRON_INGOT)
            .input('S', Items.STICK)
            .criterion("has_iron_ingot", conditionsFromItem(Items.IRON_INGOT))
            .criterion("has_stick", conditionsFromItem(Items.STICK))
            .criterion("has_trowel", conditionsFromItem(Trowel.TROWEL))
            .offerTo(c, Identifier.of(MODID, MODID))
    }
}

class AdvancementProvider(o: FabricDataOutput, r: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricAdvancementProvider(o, r) {
    override fun generateAdvancement(reg: RegistryWrapper.WrapperLookup, entry: Consumer<AdvancementEntry>) {
        val parent = AdvancementEntry(Identifier.ofVanilla("story/smelt_iron"), null)
        Advancement.Builder.create()
            .parent(parent)
            .display(
                Trowel.TROWEL,
                Text.translatable("advancements.story.trowel.title"),
                Text.translatable("advancements.story.trowel.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .criterion(MODID, InventoryChangedCriterion.Conditions.items(Trowel.TROWEL))
            .build(entry, "minecraft:story/$MODID")
    }
}

class ModelProvider(o: FabricDataOutput) : FabricModelProvider(o) {
    override fun generateBlockStateModels(gen: BlockStateModelGenerator) {}
    override fun generateItemModels(gen: ItemModelGenerator) {
        gen.register(Trowel.TROWEL, Models.HANDHELD)
    }
}
