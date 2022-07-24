package com.ags.domain.donglespace

import io.quarkus.runtime.annotations.RegisterForReflection
import java.util.*

// all data classes here need to be `var` so it works on native mode :(
@RegisterForReflection
data class DonglespaceBettingJson(
    var data: BettingData,
)

@RegisterForReflection
data class BettingData(
    var bettings: Nodes,
)

@RegisterForReflection
data class Nodes(
    var nodes: Array<Node>
)

@RegisterForReflection
data class Node(
    var code: String = "",
    var title: String = "",
    var post: Post,
    var createdAt: Date,
    var createdOn: Long = Date().time,
    // fixme: timestamp to be used on Repository.read so we can orderBy
    var creationTs: Long = 0
)
@RegisterForReflection
data class Post(
    var detail: String = "",
)