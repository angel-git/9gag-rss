package com.ags.repository

import com.ags.domain.ninegag.GagData
import com.ags.domain.ninegag.GagGroup
import com.ags.domain.ninegag.GagJson
import com.ags.domain.ninegag.GagPost
import com.google.cloud.firestore.Query
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class GagFeedRepository : Repository<GagJson, GagPost>() {

    override fun add(id: String, json: GagJson): List<GagPost> {
        val postsInserted = mutableListOf<GagPost>()
        json.data.posts.forEach {
            if (db.collection(id).whereEqualTo("id", it.id).get().get().isEmpty) {
                val documentReference = db.collection(id).document(it.id)
                documentReference.set(it)
                postsInserted.add(it)
            }
        }
        return postsInserted
    }

    override fun read(id: String): GagJson {
        val querySnapshot = db.collection(id).orderBy("creationTs", Query.Direction.DESCENDING).get().get().documents
        val posts = querySnapshot.map { it.toObject(GagPost::class.java) }.toTypedArray()
        return GagJson(GagData(posts, GagGroup(id, "fake description")))
    }

}
