package dev.encelade.inventory.scripts

import dev.encelade.inventory.services.InventoryFilterer

fun main() {
    val filterer = InventoryFilterer("data/razor-crest-75331.xml")
    filterer.execute()
}
