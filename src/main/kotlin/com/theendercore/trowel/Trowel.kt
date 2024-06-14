package com.theendercore.trowel

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.ModifyEntries
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("MemberVisibilityCanBePrivate")
object Trowel : ModInitializer {
    const val MODID = "trowel"
    val log: Logger = LoggerFactory.getLogger(MODID)
    val TROWEL: Item = TrowelItem()
    override fun onInitialize() {
        log.info("Seizing the means of block placement!")

        Registry.register(Registries.ITEM, Identifier.of(MODID, MODID), TROWEL)

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
            .register(ModifyEntries { it.addAfter(Items.SHEARS, TROWEL) })
    }
}
