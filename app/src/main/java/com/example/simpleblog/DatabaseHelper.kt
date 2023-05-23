package com.example.simpleblog

import Article
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "articles.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "articles"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_UPDATED_AT = "updatedAt"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT,
                $COLUMN_CONTENT TEXT,
                $COLUMN_UPDATED_AT TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertArticle(article: Article): Long {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_TITLE, article.title)
        contentValues.put(COLUMN_CONTENT, article.content)
        contentValues.put(COLUMN_UPDATED_AT, article.updatedAt)
        return db.insert(TABLE_NAME, null, contentValues)
    }

    fun getAllArticles(): List<Article> {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        val articles = mutableListOf<Article>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val updatedAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UPDATED_AT))

            val article = Article(title, content, updatedAt, id)
            articles.add(article)
        }

        cursor.close()
        return articles
    }

    fun getArticleById(id: Int): Article? {
        val db = readableDatabase
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id.toString())
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)

        if (cursor.moveToFirst()) {
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val updatedAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UPDATED_AT))

            return Article(title, content, updatedAt, id)
        }

        cursor.close()
        return null
    }

    fun updateArticle(article: Article): Int {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_TITLE, article.title)
        contentValues.put(COLUMN_CONTENT, article.content)
        contentValues.put(COLUMN_UPDATED_AT, article.updatedAt)

        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(article.id.toString())

        return db.update(TABLE_NAME, contentValues, selection, selectionArgs)
    }

    fun deleteArticle(article: Article): Int {
        val db = writableDatabase
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(article.id.toString())
        return db.delete(TABLE_NAME, selection, selectionArgs)
    }
}
