package dev.encelade.inventory.services

import dev.encelade.inventory.model.InventoryPart
import dev.encelade.inventory.model.WantedListModification
import dev.encelade.inventory.model.XmlInventory
import dev.encelade.inventory.model.XmlItem
import java.io.File

class InventoryFilterer(
    private val inventoryFileName: String = DEFAULT_INVENTORY_LOCATION,
    private val cellSeparator: String = DEFAULT_CELL_SEPARATOR,
) {

    private val mapper = XmlParser()

    fun filterOutInventoryParts(wantedListName: String) {
        val inventory = mapper.parse(inventoryFileName)
        val wanted = mapper.parse(wantedListName)
        val modifications = analyzePartsToFilterOut(inventory, wanted)
        val csvLine = mutableListOf<String>()

        val totalToRemoveFromWantedList = modifications.sumOf { it.quantityToRemove() }
        val totalWantedParts = wanted.sumOf { it.quantity }
        println("total to remove from wanted list: $totalToRemoveFromWantedList / $totalWantedParts")

        csvLine += listOf(
            "modification type",
            "part id",
            "color code",
            "color",
            "qty needed",
            "qty inventory",
            "qty missing"
        ).joinToString(cellSeparator)

        modifications.forEach { line ->
            val modificationType =
                when (line) {
                    is WantedListModification.SufficientQuantity -> "sufficient"
                    is WantedListModification.InsufficientQuantity -> "insufficient"
                    else -> "?"
                }

            csvLine +=
                listOf(
                    modificationType,
                    line.itemId,
                    line.color.code,
                    line.color.description ?: "",
                    line.neededQuantity,
                    line.inventoryQuantity,
                    line.quantityMissing()
                ).joinToString(cellSeparator) { it.toString() }
        }

        val wantedListNoExtension = wantedListName.split("/").last().split(".").first()

        // wml
        outputToXml(wanted, modifications, wantedListNoExtension)

        // report
        val csvFileName = "$wantedListNoExtension-report.csv"
        val csvFilePath = "data/$csvFileName"
        println("writing report to $csvFilePath")
        File(csvFilePath).writeText(csvLine.joinToString(separator = "\n"))

    }

    private fun outputToXml(
        wanted: List<InventoryPart>,
        modifications: List<WantedListModification>,
        wantedListNoExtension: String,
    ) {
        val filteredInventoryParts =
            wanted.mapNotNull { wantedItem ->
                val modification = modifications
                    .find { it.itemId == wantedItem.partId && it.color == wantedItem.color }

                if (modification == null) {
                    wantedItem
                } else {
                    if (modification.quantityMissing() > 0) {
                        wantedItem.copy(quantity = modification.quantityMissing())
                    } else {
                        null
                    }
                }
            }

        val xmlItems = filteredInventoryParts.map {
            XmlItem(
                itemType = it.itemType.asXml(),
                itemId = it.partId,
                color = it.color.code,
                minQty = it.quantity
            )
        }

        val xmlInventory = XmlInventory(xmlItems)
        val xml = mapper.outputToString(xmlInventory)
        val xmlFileName = "$wantedListNoExtension-updated.xml"
        val xmlFilePath = "data/$xmlFileName"
        println("writing report to $xmlFilePath")
        File(xmlFilePath).writeText(xml)
    }

    companion object {

        const val DEFAULT_INVENTORY_LOCATION = "data/inventory.xml"
        const val DEFAULT_CELL_SEPARATOR = ","

        private fun analyzePartsToFilterOut(
            inventory: List<InventoryPart>,
            wanted: List<InventoryPart>,
        ): List<WantedListModification> {
            val result = mutableListOf<WantedListModification>()

            wanted.forEach { wantedItem ->
                inventory
                    .find { inventoryItem -> InventoryPart.areEqualsByIdAndColor(inventoryItem, wantedItem) }
                    ?.let { inventoryMatchingItem ->
                        if (inventoryMatchingItem.quantity >= wantedItem.quantity) {
                            result += WantedListModification.SufficientQuantity(
                                wantedItem.partId,
                                wantedItem.color,
                                wantedItem.quantity,
                                inventoryMatchingItem.quantity
                            )
                        } else {
                            result += WantedListModification.InsufficientQuantity(
                                wantedItem.partId,
                                wantedItem.color,
                                wantedItem.quantity,
                                inventoryMatchingItem.quantity
                            )
                        }
                    }
            }

            return result
                .toList()
                .sortedBy { it.color.code }
                .sortedBy { it.itemId }
                .sortedBy {
                    when (it) {
                        is WantedListModification.SufficientQuantity -> 0
                        is WantedListModification.InsufficientQuantity -> 1
                        else -> Int.MAX_VALUE
                    }
                }

        }

    }

}
