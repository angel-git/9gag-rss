package com.ags.repository

import com.ags.domain.donglespace.BettingData
import com.ags.domain.donglespace.DonglespaceBettingJson
import com.ags.domain.donglespace.Node
import com.ags.domain.donglespace.Nodes
import com.google.cloud.firestore.Query
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DongleFeedRepository : Repository<DonglespaceBettingJson, Node>() {

    override fun add(id: String, json: DonglespaceBettingJson): List<Node> {
        val postsInserted = mutableListOf<Node>()
        json.data.bettings.nodes.forEach {
            if (db.collection(id).whereEqualTo("id", it.code).get().get().isEmpty) {
                val documentReference = db.collection(id).document(it.code)
                it.creationTs = it.createdAt.time
                documentReference.set(it)
                postsInserted.add(it)
            }
        }
        return postsInserted
    }

    override fun read(id: String): DonglespaceBettingJson {
        val querySnapshot = db.collection(id).orderBy("creationTs", Query.Direction.DESCENDING).get().get().documents
        val posts = querySnapshot.map { it.toObject(Node::class.java) }.toTypedArray()
        return DonglespaceBettingJson(BettingData(Nodes(posts)))
    }

}
