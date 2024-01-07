package dev.encelade.inventory.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import dev.encelade.inventory.model.Color
import dev.encelade.inventory.model.InventoryPart

import dev.encelade.inventory.model.XmlItem
import java.io.File
import kotlin.text.Charsets.UTF_8

class XmlParser {

    private val colorsMap =
        File("colors.csv").readLines().associate { line ->
            val colorCode = line.split(",")[0]
            val colorName = line.split(",")[1]
            colorCode.toInt() to colorName
        }

    private val delegate =
        XmlMapper()
            .registerKotlinModule()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)


    private val collectionType = delegate.typeFactory.constructCollectionType(List::class.java, XmlItem::class.java)!!

    fun parse(resourceFileName: String): List<InventoryPart> {
        val fileContent = XmlParser::class.java.getResource("/$resourceFileName")!!.readText(UTF_8)
        val xmlItems = delegate.readValue<List<XmlItem>>(fileContent, collectionType)!!
        return xmlItems.map { xmlItem ->
            InventoryPart(
                xmlItem.itemId,
                xmlItem.minQty,
                Color(xmlItem.color, colorsMap[xmlItem.color])
            )
        }
    }

}
