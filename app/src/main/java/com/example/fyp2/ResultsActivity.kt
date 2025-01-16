package com.example.fyp2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fyp22.ChatbotActivity
import com.example.fyp22.R
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import kotlinx.coroutines.*

class ResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val radarChart: RadarChart = findViewById(R.id.radarChart)
        val feedbackText: TextView = findViewById(R.id.feedbackText)
        val resultDetails: TextView = findViewById(R.id.personalityDetails)
        val detailedDescriptions: TextView = findViewById(R.id.detailedDescriptions)
        val seeMoreButton: Button = findViewById(R.id.seeMoreButton)
        val chatBotButton: Button = findViewById(R.id.chatbotButton)
        val opennessResult: TextView = findViewById(R.id.opennessResult)
        val conscientiousnessResult: TextView = findViewById(R.id.conscientiousnessResult)
        val extraversionResult: TextView = findViewById(R.id.extraversionResult)
        val agreeablenessResult: TextView = findViewById(R.id.agreeablenessResult)
        val neuroticismResult: TextView = findViewById(R.id.neuroticismResult)

        val predictions = intent.getFloatArrayExtra("PREDICTIONS") ?: floatArrayOf(0f, 0f, 0f, 0f, 0f)

        setupRadarChart(radarChart, predictions)

        resultDetails.text = getString(
            R.string.personality_details_dynamic,
            predictions[0], predictions[1], predictions[2], predictions[3], predictions[4]
        )

        opennessResult.text = getString(R.string.openness_result, predictions[0])
        conscientiousnessResult.text = getString(R.string.conscientiousness_result, predictions[1])
        extraversionResult.text = getString(R.string.extraversion_result, predictions[2])
        agreeablenessResult.text = getString(R.string.agreeableness_result, predictions[3])
        neuroticismResult.text = getString(R.string.neuroticism_result, predictions[4])

        feedbackText.text = getString(R.string.feedback_message)
        animateText(feedbackText, getString(R.string.feedback_message))

        val detailedText = """
            Openness: ${getTraitDescription(predictions[0], "openness")}
            Conscientiousness: ${getTraitDescription(predictions[1], "conscientiousness")}
            Extraversion: ${getTraitDescription(predictions[2], "extraversion")}
            Agreeableness: ${getTraitDescription(predictions[3], "agreeableness")}
            Neuroticism: ${getTraitDescription(predictions[4], "neuroticism")}
        """.trimIndent()

        detailedDescriptions.text = detailedText

        seeMoreButton.setOnClickListener {
            if (detailedDescriptions.visibility == View.GONE) {
                detailedDescriptions.visibility = View.VISIBLE
                seeMoreButton.text = getString(R.string.see_less)
            } else {
                detailedDescriptions.visibility = View.GONE
                seeMoreButton.text = getString(R.string.see_more)
            }
        }

        chatBotButton.setOnClickListener {
            val intent = Intent(this, ChatbotActivity::class.java)
            startActivity(intent)
        }
    }

    private fun animateText(textView: TextView, text: String) {
        textView.text = ""
        CoroutineScope(Dispatchers.Main).launch {
            for (i in text.indices) {
                textView.text = text.substring(0, i + 1)
                delay(50) // Adjust delay to control the speed
            }
        }
    }

    private fun setupRadarChart(radarChart: RadarChart, predictions: FloatArray) {
        val entries = listOf(
            RadarEntry(predictions[0]),
            RadarEntry(predictions[1]),
            RadarEntry(predictions[2]),
            RadarEntry(predictions[3]),
            RadarEntry(predictions[4])
        )
        val dataSet = RadarDataSet(entries, getString(R.string.radar_chart_label)).apply {
            color = getColor(R.color.teal_700)
            setDrawFilled(true)
            radarChart.webColor = getColor(R.color.webColor)
            fillAlpha = 180
        }
        radarChart.data = RadarData(dataSet)
        radarChart.invalidate()
    }

    private fun getTraitDescription(value: Float, trait: String): String {
        return when (trait) {
            "openness" -> when {
                value < 0.3 -> getString(R.string.low_openness)
                value < 0.7 -> getString(R.string.medium_openness)
                else -> getString(R.string.high_openness)
            }
            "conscientiousness" -> when {
                value < 0.3 -> getString(R.string.low_conscientiousness)
                value < 0.7 -> getString(R.string.medium_conscientiousness)
                else -> getString(R.string.high_conscientiousness)
            }
            "extraversion" -> when {
                value < 0.3 -> getString(R.string.low_extraversion)
                value < 0.7 -> getString(R.string.medium_extraversion)
                else -> getString(R.string.high_extraversion)
            }
            "agreeableness" -> when {
                value < 0.3 -> getString(R.string.low_agreeableness)
                value < 0.7 -> getString(R.string.medium_agreeableness)
                else -> getString(R.string.high_agreeableness)
            }
            "neuroticism" -> when {
                value < 0.3 -> getString(R.string.low_neuroticism)
                value < 0.7 -> getString(R.string.medium_neuroticism)
                else -> getString(R.string.high_neuroticism)
            }
            else -> "Unknown trait"
        }
    }
}
