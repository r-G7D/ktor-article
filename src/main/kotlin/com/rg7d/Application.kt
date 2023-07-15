package com.rg7d

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.http.HttpStatusCode
import com.rg7d.dao.DbFactory
import com.rg7d.dao.ArticleDao
import kotlinx.serialization.json.Json
import kotlinx.serialization.ExperimentalSerializationApi
import com.rg7d.routes.configureRouting

fun main(args: Array<String>) = EngineMain.main(args)

@OptIn(ExperimentalSerializationApi::class)
fun Application.module() {
    install(DefaultHeaders)

    install(ContentNegotiation) {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            explicitNulls = true // still experimental so it needs opt-in
        }
    }

    install(StatusPages) {
        exception<Throwable> {call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }

    DbFactory.init(environment)

    val articleDto = ArticleDao()
    configureRouting(
        articleDto
    )

    // install(Routing) { 
    //     article(articleDto)
    // }
}
