package com.ags.repository

import com.ags.domain.GagData
import com.ags.domain.GagGroup
import com.ags.domain.GagJson
import com.ags.domain.GagPost
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.FirestoreOptions
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FeedRepository {

    private val db = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId("gag-293510").setCredentials(GoogleCredentials.getApplicationDefault()).build().service!!

    fun add(group: String, json: GagJson) {
        json.data.posts.forEach {
            val documentReference = db.collection(group).document(it.id)
            documentReference.set(it)
        }
    }

    fun read(group: String): GagJson {
        val querySnapshot = db.collection(group).get().get().documents
        val posts = querySnapshot.map {
            it.toObject(GagPost::class.java)
        }.toTypedArray()

        return GagJson(GagData(posts, GagGroup(group, "fake description")))
    }
}
