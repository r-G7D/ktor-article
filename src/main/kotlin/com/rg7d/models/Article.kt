package com.rg7d.models

import org.jetbrains.exposed.sql.*
import com.rg7d.dao.query


object Articles : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 255)
    val content = text("content")
    val author = varchar("author", 255)
    val createdAt = long("created_at")

    override val primaryKey = PrimaryKey(id)
}

@kotlinx.serialization.Serializable
data class Article(
    val id: Int,
    val title: String,
    val content: String,
    val author: String,
    val createdAt: Long
)

@kotlinx.serialization.Serializable
data class ArticleDto(
    val id: Int?,
    val title: String?,
    val content: String?,
    val author: String?,
)