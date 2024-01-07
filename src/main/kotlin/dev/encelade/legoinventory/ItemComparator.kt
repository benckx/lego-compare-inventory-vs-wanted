package dev.encelade.legoinventory

import dev.encelade.legoinventory.model.Item
import dev.encelade.legoinventory.model.Item.Companion.areEqualsByIdAndColor

object ItemComparator {

    fun analyze(inventory: List<Item>, wanted: List<Item>) {
        wanted.forEach { wantedItem ->
            inventory.find { inventoryItem -> areEqualsByIdAndColor(inventoryItem, wantedItem) }?.let { matchingItem ->
                if (matchingItem.minQty >= wantedItem.minQty) {
                    println("you already have the part in quantity -> $wantedItem (you have ${matchingItem.minQty} and you need ${wantedItem.minQty})")
                } else {
                    println("you already have the part, but in insufficient quantity -> $wantedItem (you have ${matchingItem.minQty} but need ${wantedItem.minQty})")
                }
            }
        }
    }

}
