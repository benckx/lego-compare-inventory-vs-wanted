package dev.encelade.inventory.scripts

import dev.encelade.inventory.model.WantedListModification
import dev.encelade.inventory.services.InventoryFilterer.analyzePartsToFilterOut
import dev.encelade.inventory.services.XmlParser
import java.io.File

const val CELL_SEPARATOR = ","
const val CSV_OUTPUT_FILE = "wanted_list_suggested_modifications.csv"

fun main() {
    val mapper = XmlParser()
    val inventory = mapper.parse("inventory.xml")
    val wanted = mapper.parse("wanted-razor-crest-75331.xml")
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
    ).joinToString(CELL_SEPARATOR)

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
            ).joinToString(CELL_SEPARATOR) { it.toString() }
    }

    File(CSV_OUTPUT_FILE).writeText(csvLine.joinToString(separator = "\n"))
}
