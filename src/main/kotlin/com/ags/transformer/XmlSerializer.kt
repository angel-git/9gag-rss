package com.ags.transformer

import com.ags.domain.Rss
import com.ctc.wstx.stax.WstxInputFactory
import com.ctc.wstx.stax.WstxOutputFactory
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper

class XmlSerializer {

    private val xml = XmlMapper(XmlFactory(WstxInputFactory(), WstxOutputFactory()),JacksonXmlModule()
            .apply { setDefaultUseWrapper(false) })
            .apply { enable(SerializationFeature.INDENT_OUTPUT) }
            .apply { configure(MapperFeature.USE_ANNOTATIONS, true) }

    fun toXml(rss: Rss): String {
        return xml.writeValueAsString(rss)
    }


}
