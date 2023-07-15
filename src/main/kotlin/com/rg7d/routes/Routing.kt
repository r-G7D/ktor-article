package com.rg7d.routes

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.receive
import com.rg7d.dao.ArticleDao
import com.rg7d.models.ArticleDto

fun Application.configureRouting(
    articleDao : ArticleDao
) {
    routing {
        route("/api/article") {
            get {
                call.respond(articleDao.getAll())
            }
            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw IllegalStateException("Numeric ID expected")
                val article = articleDao.getById(id)
                if (article == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                } else {
                call.respond(article)
                }
            }
            post {
                val article = call.receive<ArticleDto>()
                articleDao.create(article)
                call.respond(HttpStatusCode.Created)
            }
            patch {
                val article = call.receive<ArticleDto>()
                articleDao.update(article)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
