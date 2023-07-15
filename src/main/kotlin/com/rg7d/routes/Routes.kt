package com.rg7d.routes

import com.rg7d.dao.ArticleDao
import com.rg7d.models.ArticleDto
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*

fun Route.article(
    articelDao : ArticleDao
) {
    route("/api/article") {
        get {
            call.respond(articelDao.getAll())
        }
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw IllegalStateException("Numeric ID expected")
            val article = articelDao.getById(id)
            if (article == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            } else {
            call.respond(article)
            }
        }
        post {
            val article = call.receive<ArticleDto>()
            articelDao.create(article)
            call.respond(HttpStatusCode.Created)
        }
        patch {
            val article = call.receive<ArticleDto>()
            articelDao.update(article)
            call.respond(HttpStatusCode.OK)
        }
    }
}