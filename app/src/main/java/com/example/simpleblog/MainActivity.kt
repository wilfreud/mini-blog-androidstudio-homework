package com.example.simpleblog

import Article
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var articleAdapter: ArticleAdapter

    private val articleList = mutableListOf<Article>()
    private val CREATE_ARTICLE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.articleRecyclerView)
        articleAdapter = ArticleAdapter(articleList) { article ->
            // Handle item click here
            showArticleDetails(article.id)
        }

        recyclerView.apply {
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        // Fetch articles from the database and populate the article list
        fetchArticlesFromDatabase()

        // Handle the floating action button click
        val fabAdd: FloatingActionButton = findViewById(R.id.addArticleBtn)
        fabAdd.setOnClickListener {
            navigateToCreateArticle()
        }
    }

    private fun fetchArticlesFromDatabase() {
        // Clear the list (if needed)
        articleList.clear()

        // Fetch articles from the database using your DatabaseHelper class
        val dbHelper = DatabaseHelper(this)
        articleList.addAll(dbHelper.getAllArticles())

        // Notify the adapter that the data set has changed
        articleAdapter.notifyDataSetChanged()
    }

    private fun showArticleDetails(articleId: Int) {
        // Launch the com.example.simpleblog.ArticleDetailsActivity and pass the article ID
        val intent = Intent(this, ArticleDetailsActivity::class.java)
        intent.putExtra(ArticleDetailsActivity.EXTRA_ARTICLE_ID, articleId)
        startActivity(intent)
    }

    private fun navigateToCreateArticle() {
        // Launch the CreateArticleActivity to create a new article
        val intent = Intent(this, CreateArticleActivity::class.java)
        startActivityForResult(intent, CREATE_ARTICLE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_ARTICLE_REQUEST && resultCode == RESULT_OK) {
            // A new article was created, refetch the articles from the database
            fetchArticlesFromDatabase()
        }
    }
}
