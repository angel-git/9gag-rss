package com.ags.transformer.dongle

import com.ags.domain.*
import com.ags.domain.donglespace.DonglespaceBettingJson
import com.ags.domain.donglespace.Node
import com.ags.transformer.Function

class DongleToAtom : Function<DonglespaceBettingJson, Rss>() {

    override fun apply(input: DonglespaceBettingJson): Rss {

        val items: List<Item> = input.data.bettings.nodes.map { DongleNodeToItem().apply(it) }
        return Rss(
            Channel(
                title = "https://www.donglespace.com/ NSFW",
                link = "https://www.donglespace.com/",
                description = "NSFW",
                item = items,
                atomLink = AtomLink("hub", "https://pubsubhubbub.appspot.com/"),
                pubDate = parseCreationTsToRF(input.data.bettings.nodes[0].creationTs)
            )
        )
    }
}

class DongleNodeToItem : Function<Node, Item>() {

    override fun apply(input: Node): Item {
        return Item(
            guid = input.code,
            description = parserDetail(input.post.detail),
            title = input.title,
            link = input.code,
            pubDate = parseCreationTsToRF(input.creationTs)
        )
    }

    private fun parserDetail(detail: String): String {
        return if (detail.startsWith("<iframe")) {
            val source = """src="(.*)"""".toRegex().find(detail)!!
            """<video class="ql-video" frameborder="0" allowfullscreen="true" src="${source.groups[1]?.value}"></video>"""
        } else {
            detail
        }
    }
}


