package dev.encelade.inventory.services

import dev.encelade.inventory.model.InventoryPart
import dev.encelade.inventory.model.WantedListUpdate
import dev.encelade.inventory.model.XmlInventory
import dev.encelade.inventory.model.XmlItem
import java.io.File

class InventoryFilterer(
    private val wantedListName: String,
    private val inventoryFileName: String = DEFAULT_INVENTORY_LOCATION,
    private val cellSeparator: String = DEFAULT_CELL_SEPARATOR,
) {

    private val mapper = XmlParser()
    private val wantedListNoExtension = wantedListName.split("/").last().split(".").first()
    private val wantedListUpdates = mutableListOf<WantedListUpdate>()

    fun execute() {
        val inventory = mapper.parse(inventoryFileName)
        val wanted = mapper.parse(wantedListName)
        wantedListUpdates += analyzePartsToFilterOut(inventory, wanted)

        val totalToRemoveFromWantedList = wantedListUpdates.sumOf { it.quantityToRemove() }
        val totalWantedParts = wanted.sumOf { it.quantity }
        println("total to remove from wanted list: $totalToRemoveFromWantedList / $totalWantedParts")

        outputToXml(wanted)
        outputCsvReport()
    }

    private fun outputToXml(
        wanted: List<InventoryPart>,
    ) {
        val filteredInventoryParts =
            wanted.mapNotNull { wantedItem ->
                val modification = wantedListUpdates
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

    private fun outputCsvReport() {
        val csvReportLines = mutableListOf<String>()

        csvReportLines += listOf(
            "modification type",
            "part id",
            "color code",
            "color",
            "qty needed",
            "qty inventory",
            "qty missing"
        ).joinToString(cellSeparator)

        wantedListUpdates.forEach { line ->
            val modificationType =
                when (line) {
                    is WantedListUpdate.SufficientQuantity -> "sufficient"
                    is WantedListUpdate.InsufficientQuantity -> "insufficient"
                    else -> "?"
                }

            csvReportLines +=
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


        val csvFileName = "$wantedListNoExtension-output-report.csv"
        val csvFilePath = "data/$csvFileName"
        println("writing report to $csvFilePath")
        File(csvFilePath).writeText(csvReportLines.joinToString(separator = "\n"))
    }

    companion object {

        const val DEFAULT_INVENTORY_LOCATION = "data/inventory.xml"
        const val DEFAULT_CELL_SEPARATOR = ","

        private fun analyzePartsToFilterOut(
            inventory: List<InventoryPart>,
            wanted: List<InventoryPart>,
        ): List<WantedListUpdate> {
            val result = mutableListOf<WantedListUpdate>()

            wanted.forEach { wantedItem ->
                inventory
                    .find { inventoryItem -> InventoryPart.areEqualsByIdAndColor(inventoryItem, wantedItem) }
                    ?.let { inventoryMatchingItem ->
                        if (inventoryMatchingItem.quantity >= wantedItem.quantity) {
                            result += WantedListUpdate.SufficientQuantity(
                                wantedItem.partId,
                                wantedItem.color,
                                wantedItem.quantity,
                                inventoryMatchingItem.quantity
                            )
                        } else {
                            result += WantedListUpdate.InsufficientQuantity(
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
                        is WantedListUpdate.SufficientQuantity -> 0
                        is WantedListUpdate.InsufficientQuantity -> 1
                        else -> Int.MAX_VALUE
                    }
                }

        }

    }

}
