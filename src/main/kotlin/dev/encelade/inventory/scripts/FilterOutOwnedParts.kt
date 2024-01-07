package dev.encelade.inventory.scripts

import dev.encelade.inventory.services.WantedListModificationReportBuilder

fun main() {
    val reportBuilder = WantedListModificationReportBuilder()
    reportBuilder.outputReportToCsv("data/wanted-razor-crest-75331.xml")
}
