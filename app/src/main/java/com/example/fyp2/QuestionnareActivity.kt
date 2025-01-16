package com.example.fyp2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fyp22.R
import java.io.BufferedReader
import java.io.InputStreamReader

class QuestionnaireActivity : AppCompatActivity() {

    private lateinit var questionText: TextView
    private lateinit var likertScaleGroup: RadioGroup
    private lateinit var nextButton: Button
    private lateinit var questionProgress: TextView

    private var currentQuestionIndex = 0
    private val questions = listOf(
        (R.string.question_1),
        (R.string.question_2),
        (R.string.question_3),
        (R.string.question_4),
        (R.string.question_5),
        (R.string.question_6),
        (R.string.question_7),
        (R.string.question_8),
        (R.string.question_9),
        (R.string.question_10)
    )

    private val answers = mutableListOf<Int>()
    private lateinit var modelData: List<List<Float>> // Changed to a list of floats

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)

        questionText = findViewById(R.id.questionText)
        likertScaleGroup = findViewById(R.id.likertScaleGroup)
        nextButton = findViewById(R.id.nextButton)
        questionProgress = findViewById(R.id.questionProgress)

        // Load the model from assets
        val csvContent = loadModelFromAssets()
        if (csvContent.isNotEmpty()) {
            modelData = csvContent // Now it's a list of lists containing the float data
        } else {
            Log.e("QuestionnaireActivity", "Model loading failed.")
            showMessage("Failed to load model data. Please restart the app.")
            finish()
            return
        }

        showQuestion()

        nextButton.setOnClickListener {
            saveAnswer()

            Log.d("QuestionnaireActivity", "Current Question Index: $currentQuestionIndex")
            Log.d("QuestionnaireActivity", "Answers: $answers")

            // Check if all questions have been answered
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                showQuestion()
            } else {
                // If it's the last question, check if all answers are collected
                if (answers.size == questions.size) {
                    Log.d("QuestionnaireActivity", "All questions answered. Calculating predictions...")

                    val predictions = calculatePredictions(answers, modelData)
                    if (predictions.isNotEmpty()) {
                        navigateToResults(predictions)
                    } else {
                        showMessage("Error calculating predictions.")
                    }
                } else {
                    // If not all questions are answered, show a message
                    showMessage("Please answer all questions.")
                }
            }
        }

    }

    private fun saveAnswer() {
        val selectedRadioButtonId = likertScaleGroup.checkedRadioButtonId
        if (selectedRadioButtonId != -1) {
            val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
            val answer = when (selectedRadioButton.text.toString()) {
                getString(R.string.strongly_disagree) -> 1
                getString(R.string.disagree) -> 2
                getString(R.string.neutral) -> 3
                getString(R.string.agree) -> 4
                getString(R.string.strongly_agree) -> 5
                else -> 0
            }
            answers.add(answer)
        } else {
            showMessage("Please select an option before proceeding.")
        }
    }

    private fun loadModelFromAssets(): List<List<Float>> {
        return try {
            val inputStream = assets.open("synthetic_personality_data.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val modelData = mutableListOf<List<Float>>()

            reader.forEachLine { line ->
                val columns = line.split(",")
                // Ensure we are reading only numeric data
                val numbers = columns.mapNotNull { it.toFloatOrNull() }
                if (numbers.isNotEmpty()) {
                    modelData.add(numbers)
                }
            }
            reader.close()
            modelData
        } catch (e: Exception) {
            Log.e("QuestionnaireActivity", "Error loading model from assets", e)
            emptyList()
        }
    }

    private fun calculatePredictions(answers: List<Int>, modelData: List<List<Float>>): FloatArray {
        if (answers.size != questions.size) {
            Log.e("QuestionnaireActivity", "Error: The number of answers doesn't match the number of questions.")
            return FloatArray(0) // Return an empty array if the sizes don't match
        }
        Log.d("QuestionnaireActivity", "Answers: $answers")

        // Assume the model data includes weights in the rows (excluding the last row as intercept)
        val weights = modelData.dropLast(1).map { it.toFloatArray() } // Each row contains weights
        val intercept = modelData.last().first() // Assuming intercept is a single value in the last row

        Log.d("QuestionnaireActivity", "Weights: $weights, Intercept: $intercept")

        val predictions = FloatArray(weights.size) { i ->
            // Perform a linear combination for predictions (weights * answers + intercept)
            val weightedSum = weights[i].zip(answers) { weight, answer -> weight * answer }
                .sum()
            weightedSum + intercept
        }

        Log.d("QuestionnaireActivity", "Predictions: ${predictions.joinToString(", ")}")
        return predictions
    }

    private fun navigateToResults(predictions: FloatArray) {
        if (predictions.isEmpty()) {
            Log.e("QuestionnaireActivity", "Predictions array is empty, cannot navigate to results.")
            showMessage("Error calculating predictions.")
            return
        }
        val intent = Intent(this, ResultsActivity::class.java)
        intent.putExtra("PREDICTIONS", predictions)
        // Check for API level before using ActivityOptions
        val options = android.app.ActivityOptions.makeCustomAnimation(
            this,
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        startActivity(intent, options.toBundle())

    }

    private fun showQuestion() {
        if (currentQuestionIndex < questions.size) {
            questionText.text = getString(questions[currentQuestionIndex])
            questionProgress.text = getString(
                R.string.question_progress,
                currentQuestionIndex + 1,
                questions.size
            )
            likertScaleGroup.clearCheck()
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
