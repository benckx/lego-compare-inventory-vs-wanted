package dev.encelade.inventory.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import dev.encelade.inventory.model.*

import java.io.File
import kotlin.text.Charsets.UTF_8

class XmlParser {

    private val colorsMap =
        File("data/colors.csv").readLines().associate { line ->
            val colorCode = line.split(",")[0]
            val colorName = line.split(",")[1]
            colorCode.toInt() to colorName
        }

    private val delegate =
        XmlMapper()
            .registerKotlinModule()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)

    private val collectionType = delegate.typeFactory.constructCollectionType(List::class.java, XmlItem::class.java)!!

    fun parse(fileName: String): List<InventoryPart> {
        val fileContent = File(fileName).readLines(UTF_8).joinToString("\n")
        val xmlItems = delegate.readValue<List<XmlItem>>(fileContent, collectionType)!!
        return xmlItems.map { xmlItem ->
            InventoryPart(
                itemType = ItemType.fromXml(xmlItem.itemType),
                partId = xmlItem.itemId,
                quantity = xmlItem.minQty,
                color = Color(xmlItem.color, colorsMap[xmlItem.color])
            )
        }
    }

    fun writeValueAsString(xmlInventory: XmlInventory): String {
        return delegate.writeValueAsString(xmlInventory)
    }

}
