package com.theendercore.trowel

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.ModifyEntries
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import org.slf4j.LoggerFactory
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object TrowelMod : ModInitializer {
    const val MODID = "trowel"
    val LOGGER = LoggerFactory.getLogger(MODID)
    val TROWEL: Item = Trowel()
    override fun onInitialize() {
        LOGGER.info("Seizing the means of block placement!")

        Registry.register(Registries.ITEM, Identifier(MODID, MODID), TROWEL)
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ModifyEntries { it.addAfter(Items.SHEARS,TROWEL) })
    }
}