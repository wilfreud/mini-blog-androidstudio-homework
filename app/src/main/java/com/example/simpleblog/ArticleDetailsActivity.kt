@file:Suppress("DEPRECATION")

package com.example.simpleblog

import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ArticleDetailsActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var contentTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var authorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_details)

        titleTextView = findViewById(R.id.articleTitle)
        contentTextView = findViewById(R.id.article_details_content)
        dateTextView = findViewById(R.id.date)
        authorTextView = findViewById(R.id.author)

        val articleId = intent.getIntExtra(EXTRA_ARTICLE_ID, -1)
        if (articleId != -1) {
            // Fetch article details from the database based on the articleId
            val dbHelper = DatabaseHelper(this)
            val article = dbHelper.getArticleById(articleId)
            if (article != null) {
                titleTextView.text = article.title
                contentTextView.text = article.content
                dateTextView.text = article.updatedAt
                authorTextView.text = "Ecrit par " + Html.fromHtml("<b>John Doe</b>")
            }
        }

        val returnButton = findViewById<FloatingActionButton>(R.id.returnButton)
        returnButton.setOnClickListener {
            finish() // Close the current activity and return to the previous activity
        }
    }

    companion object {
        const val EXTRA_ARTICLE_ID = "extra_article"
    }
}
