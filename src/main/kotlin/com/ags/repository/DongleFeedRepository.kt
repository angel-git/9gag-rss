package com.ags.repository

import com.ags.domain.donglespace.BettingData
import com.ags.domain.donglespace.DonglespaceBettingJson
import com.ags.domain.donglespace.Node
import com.ags.domain.donglespace.Nodes
import com.google.cloud.firestore.Query
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DongleFeedRepository {

    private val repository: Repository = Repository()
    fun add(id: String, json: DonglespaceBettingJson): List<Node> {
        val postsInserted = mutableListOf<Node>()
        json.data.bettings.nodes.forEach {
            if (repository.db.collection(id).whereEqualTo("id", it.code).get().get().isEmpty) {
                val documentReference = repository.db.collection(id).document(it.code)
                it.creationTs = it.createdAt.time
                documentReference.set(it)
                postsInserted.add(it)
            }
        }
        return postsInserted
    }

    fun read(id: String): DonglespaceBettingJson {
        val querySnapshot =
            repository.db.collection(id).orderBy("creationTs", Query.Direction.DESCENDING).get().get().documents
        val posts = querySnapshot.map { it.toObject(Node::class.java) }.toTypedArray()
        return DonglespaceBettingJson(BettingData(Nodes(posts)))
    }

    fun deleteDayOldPosts() {
        repository.deleteDayOldPosts()

    }

}
