package dev.encelade.inventory.services

import dev.encelade.inventory.model.WantedListModification

import java.io.File

class WantedListModificationReportBuilder(
    private val inventoryFileName: String = DEFAULT_INVENTORY_LOCATION,
    private val cellSeparator: String = DEFAULT_CELL_SEPARATOR,
) {

    fun outputReportToCsv(wantedListName: String) {
        val mapper = XmlParser()
        val inventory = mapper.parse(inventoryFileName)
        val wanted = mapper.parse(wantedListName)
        val modifications = InventoryFilterer.analyzePartsToFilterOut(inventory, wanted)
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
        val csvFileName = "$wantedListNoExtension-modifications.csv"
        val csvFilePath = "data/$csvFileName"
        println("writing report to $csvFilePath")
        File(csvFilePath).writeText(csvLine.joinToString(separator = "\n"))
    }

    companion object {

        const val DEFAULT_INVENTORY_LOCATION = "data/inventory.xml"
        const val DEFAULT_CELL_SEPARATOR = ","

    }

}
