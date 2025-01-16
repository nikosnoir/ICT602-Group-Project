package com.example.fyp2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fyp22.R
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load the CSV file from assets and pass the content to the next activity
        val syntheticModelData = loadModelFromAssets()
        if (syntheticModelData.isEmpty()) {
            // Show an error message and stop further processing if the file couldn't be loaded
            Toast.makeText(this, "Failed to load personality model data.", Toast.LENGTH_LONG).show()
            Log.e("MainActivity", "CSV content is empty or could not be loaded.")
            return
        }

        // Start QuestionnaireActivity with the loaded model data
        // Inside MainActivity's onCreate() or onClick event
        val startButton: Button = findViewById(R.id.startButton)
        startButton.setOnClickListener {
            val intent = Intent(this, QuestionnaireActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loadModelFromAssets(): String {
        return try {
            // Open the CSV file from the assets folder
            val inputStream = assets.open("synthetic_personality_data.csv")s
            val reader = BufferedReader(InputStreamReader(inputStream))
            val csvContent = reader.readText()
            reader.close()

            // Log the successful loading of data
            Log.d("MainActivity", "CSV content loaded successfully.")
            csvContent
        } catch (e: Exception) {
            // Log the exception for debugging purposes
            e.printStackTrace()
            Log.e("MainActivity", "Error loading model from assets", e)
            ""
        }
    }
}
