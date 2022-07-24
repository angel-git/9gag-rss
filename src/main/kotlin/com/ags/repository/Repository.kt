package com.ags.repository

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.FirestoreOptions
import org.slf4j.LoggerFactory
import java.util.*

abstract class Repository<Req, Res> {

    private val logger = LoggerFactory.getLogger(javaClass)

    protected val db = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId("gag2-293807").setCredentials(
        GoogleCredentials.getApplicationDefault()).build().service!!

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

    abstract fun add(id: String, json: Req): List<Res>

    abstract fun read(id: String): Req
}