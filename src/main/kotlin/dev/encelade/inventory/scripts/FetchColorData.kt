package dev.encelade.inventory.scripts

import org.jsoup.Jsoup
import java.io.File

fun main() {
    val table =
        Jsoup
            .connect("https://rebrickable.com/colors/")
            .get()
            .getElementById("colors_table")!!

    val labelToIndexMap =
        table
            .getElementsByTag("th")
            .toList()
            .filterNotNull()
            .mapIndexed { index, element ->
                element.text() to index
            }
            .toMap()

    val brickLinkColIdx = labelToIndexMap["BrickLink"]!!
    val output = mutableListOf<List<String>>()

    table.getElementsByTag("tr").toList().filterNotNull().forEach { row ->
        val cells = row.getElementsByTag("td").toList().filterNotNull()
        if (cells.isNotEmpty()) {
            val brickLinkText = cells[brickLinkColIdx].text()
            if (brickLinkText.isNotBlank()) {
                val colorCode = brickLinkText.split(" ")[0]
                val from = brickLinkText.indexOf("'")
                val to = brickLinkText.lastIndexOf("'")
                val colorName = brickLinkText.substring(from + 1, to)
                output.add(listOf(colorCode, colorName))
            }
        }
    }

    val csvOutput = output.sortedBy { it.first().toInt() }.joinToString("\n") { line -> line.joinToString(",") }
    File("colors.csv").writeText(csvOutput)

}
