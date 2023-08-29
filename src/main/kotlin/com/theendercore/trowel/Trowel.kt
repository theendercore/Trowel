package com.theendercore.trowel

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.*
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.random.Random
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
                val item = pool[Random.create().nextBetween(0, pool.size - 1)]
                val iCtx = ItemPlacementContext(
                    player, c.hand, item, BlockHitResult(c.hitPos, c.side, c.blockPos, c.hitsInsideBlock())
                )
                val actionResult = item.useOnBlock(iCtx)

                if (actionResult.shouldIncrementStat()) {
                    count = 0
                    val state: BlockState = c.world.getBlockState(iCtx.blockPos)
                    val blockSoundGroup: BlockSoundGroup = state.soundGroup
                    c.world.playSound(
                        null, iCtx.blockPos, getPlaceSound(state), SoundCategory.BLOCKS,
                        (blockSoundGroup.getVolume() + 1.0f) / 2.0f,
                        blockSoundGroup.getPitch() * 0.8f
                    )
                    return ActionResult.SUCCESS
                } else if (count < pool.size) this.useOnBlock(c)
            }
        }
        return ActionResult.PASS
    }
    private fun getPlaceSound(state: BlockState): SoundEvent = state.soundGroup.placeSound
}