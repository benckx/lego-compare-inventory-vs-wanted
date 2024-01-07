package dev.encelade.inventory.scripts

import dev.encelade.inventory.model.WantedListModification
import dev.encelade.inventory.services.InventoryFilterer.analyzePartsToFilterOut
import dev.encelade.inventory.services.XmlParser
import java.io.File

const val INVENTORY_LOCATION = "data/inventory.xml"
const val WANTED_LIST_FILE_NAME = "data/wanted-razor-crest-75331.xml"

const val CSV_CELL_SEPARATOR = ","

fun main() {
    val mapper = XmlParser()
    val inventory = mapper.parse(INVENTORY_LOCATION)
    val wanted = mapper.parse(WANTED_LIST_FILE_NAME)
    val reportLines = analyzePartsToFilterOut(inventory, wanted)
    val csvLine = mutableListOf<String>()

    csvLine += listOf(
        "modification type",
        "part id",
        "color code",
        "color",
        "needed qty",
        "inventory qty",
        "qty to remove"
    ).joinToString(CSV_CELL_SEPARATOR)

    reportLines.forEach { line ->
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
                line.quantityToRemove()
            ).joinToString(CSV_CELL_SEPARATOR) { it.toString() }
    }

    val wantedListNoExtension = WANTED_LIST_FILE_NAME.split("/").last().split(".").first()
    val csvFileName = "$wantedListNoExtension-modifications.csv"
    val csvFilePath = "data/$csvFileName"
    File(csvFilePath).writeText(csvLine.joinToString(separator = "\n"))
}
