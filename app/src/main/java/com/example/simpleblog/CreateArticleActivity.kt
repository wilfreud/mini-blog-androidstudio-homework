package com.example.simpleblog

import Article
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class CreateArticleActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        titleEditText = findViewById(R.id.post_title)
        contentEditText = findViewById(R.id.post_content)
        submitButton = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            val title = titleEditText.text.toString().trim()
            val content = contentEditText.text.toString().trim()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Remplissez tous les champs", Toast.LENGTH_SHORT).show()
            } else {
                val updatedAt = getCurrentDate() // Get the current date

                // Insert the article into the database
                val dbHelper = DatabaseHelper(this)
                val article = Article(title, content, updatedAt, 0) // Create Article instance with current date and empty id
                dbHelper.insertArticle(article)

                // Set the result as OK and finish the activity
                setResult(RESULT_OK)
                Toast.makeText(this, "Article ajouté", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        val returnButton = findViewById<FloatingActionButton>(R.id.returnButton)
        returnButton.setOnClickListener {
            setResult(RESULT_CANCELED) // Set the result as canceled and finish the activity
            finish()
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
}
