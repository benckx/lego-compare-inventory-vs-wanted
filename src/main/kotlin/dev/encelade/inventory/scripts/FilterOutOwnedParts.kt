package dev.encelade.inventory.scripts

import dev.encelade.inventory.services.InventoryFilterer.analyzeToFilterOut
import dev.encelade.inventory.services.XmlParser

fun main() {
    val mapper = XmlParser()
    val inventory = mapper.parse("inventory.xml")
    val wanted = mapper.parse("wanted-razor-crest-75331.xml")
    analyzeToFilterOut(inventory, wanted)
}
