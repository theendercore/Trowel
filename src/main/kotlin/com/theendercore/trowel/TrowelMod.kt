package com.theendercore.trowel

import net.fabricmc.api.ModInitializer
import net.minecraft.item.Item
import net.minecraft.item.Items
import org.slf4j.LoggerFactory
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry;

object TrowelMod : ModInitializer {
    const val MODID = "trowel"
    val LOGGER = LoggerFactory.getLogger(MODID)
    val TROWEL: Item = Trowel()
    override fun onInitialize() {
        LOGGER.info("Seizing the means of block placement!")

        Registry.register(Registry.ITEM, Identifier(MODID, MODID), TROWEL)
        // ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ModifyEntries { it.addAfter(Items.SHEARS,TROWEL) })
    }
}