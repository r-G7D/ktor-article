package com.rg7d.dao

import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.HikariConfig
import com.rg7d.models.Articles
import io.ktor.server.application.ApplicationEnvironment
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

fun hikari(url: String, user: String, pass: String, pool: Int): HikariDataSource {
    val config = HikariConfig()
    config.driverClassName = "org.postgresql.Driver"
    config.jdbcUrl = url
    config.username = user
    config.password = pass
    config.maximumPoolSize = pool
    config.isAutoCommit = false
    config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    config.validate()
    return HikariDataSource(config)
}

object DbFactory {
    fun init (environment: ApplicationEnvironment) {
        val url = environment.config.property("db.jdbcUrl").getString()
        val user = environment.config.property("db.user").getString()
        val pass = environment.config.property("db.password").getString()
        val pool = environment.config.property("db.poolSize").getString().toInt()
        val dataSource = hikari(url, user, pass, pool)
        Database.connect(dataSource)

        transaction {
            SchemaUtils.create(Articles)
        }
    }
}

suspend fun<T> query(block: () -> T): T = withContext(Dispatchers.IO) {
    transaction { block() }
}