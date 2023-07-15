package com.rg7d.dao

import com.rg7d.models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import com.rg7d.dao.query

class ArticleDao {
    private fun rowToArticle(row: ResultRow): =
        Article(
            id = row[Articles.id],
            title = row[Articles.title],
            content = row[Articles.content],
            author = row[Articles.author],
            createdAt = row[Articles.createdAt]
        )

    suspend fun getAll(): List<Article> = query {
       Articles.selectAll().map { rowToArticle(it) }
    }

    suspend fun getById(id: Int): Article? = query {
        Articles.select( Articles.id eq id ).map { rowToArticle(it) }.singleOrNull()
    }

    suspend fun create(article: ArticleDto): Article = query {
        Articles.insert {
            it[title] = article.title!!
            it[content] = article.content!!
            it[author] = article.author!!
            it[createdAt] = System.currentTimeMillis()
        }
    }.let { getById(it[Articles.id])!! }

    suspend fun update(article: ArticleDto): Article = query {
        Articles.update({ Articles.id eq article.id!! }) {
            it[title] = article.title!!
            it[content] = article.content!!
            it[author] = article.author!!
        }
    }.let { getById(article.id!!)!! }
}