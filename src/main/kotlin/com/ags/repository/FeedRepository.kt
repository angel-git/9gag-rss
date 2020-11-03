package com.ags.repository

import com.ags.domain.GagData
import com.ags.domain.GagGroup
import com.ags.domain.GagJson
import com.ags.domain.GagPost
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.FirestoreOptions
import com.google.cloud.firestore.Query
import org.slf4j.LoggerFactory
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FeedRepository {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val db = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId("gag2-293807").setCredentials(GoogleCredentials.getApplicationDefault()).build().service!!

    fun add(group: String, json: GagJson) {
        json.data.posts.forEach {
            val documentReference = db.collection(group).document(it.id)
            documentReference.set(it)
        }
    }

    fun read(group: String): GagJson {
        val querySnapshot = db.collection(group).orderBy("creationTs", Query.Direction.DESCENDING).get().get().documents
        val posts = querySnapshot.map { it.toObject(GagPost::class.java) }.toTypedArray()
        return GagJson(GagData(posts, GagGroup(group, "fake description")))
    }

    fun deleteDayOldPosts() {
        val dayMs = 86400000
        val yesterday = Date().time - dayMs
        db.listCollections().forEach {
            it.whereLessThanOrEqualTo("createdOn", yesterday).get().get().documents.forEach { doc ->
                logger.info("going to delete doc ${doc.id} created ${doc.getLong("createdOn")}")
                doc.reference.delete()
            }
        }
    }
}
