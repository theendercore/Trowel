package com.theendercore.trowel

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.*
import net.minecraft.stat.Stats
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import java.util.Random
import java.util.*

class Trowel : Item(FabricItemSettings().maxCount(1).group(ItemGroup.TOOLS)) {
    var count = 0
    override fun useOnBlock(c: ItemUsageContext?): ActionResult {
        if (c != null && !c.world.isClient) {
            count++
            val player: PlayerEntity? = c.player
            val pool = LinkedList<ItemStack>()
            for (i in 0..9) {
                val itemStack = player?.inventory?.getStack(i)
                if (itemStack?.item !is AirBlockItem && itemStack?.item is BlockItem) pool.add(itemStack)
            }
            if (pool.size > 0) {
                val item = pool[random(0, pool.size - 1)]
                val actionResult: ActionResult = item.useOnBlock(
                    ItemPlacementContext(
                        player,
                        c.hand,
                        item,
                        BlockHitResult(c.hitPos, c.side, c.blockPos, c.hitsInsideBlock())
                    )
                )
                if (actionResult.shouldIncrementStat()) {
                    count = 0
                    player?.incrementStat(Stats.USED.getOrCreateStat(this))
                    return ActionResult.SUCCESS
                } else if (count < pool.size) this.useOnBlock(c)
            }
        }
        return ActionResult.PASS
    }

     fun random(min:Int, max:Int): Int {
        return( min + (Math.random() * ((max - min) + 1))).toInt()
    }

}