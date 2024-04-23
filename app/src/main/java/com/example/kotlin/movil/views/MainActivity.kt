package com.example.kotlin.movil.views
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.kotlin.intento.R
import com.example.kotlin.movil.model.RetrofitClient
import com.example.kotlin.movil.model.ninja.ninjaRespones
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var textViewData: TextView
    private lateinit var domainText: EditText
    private var listNames: ArrayList<String> = ArrayList()
    lateinit var pieChart: PieChart

    // Interaccion con el suuaio y llama a fetch data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewData = findViewById(R.id.textViewData)
        domainText = findViewById(R.id.domainText)

        val buttonGetData: Button = findViewById(R.id.buttonGetData)
        buttonGetData.setOnClickListener {
            // Obtiene el dominio ingresado por el usuario
            val domain = domainText.text.toString().trim()

            // Verifica si el dominio está vacío antes de hacer la llamada a la API
            if (domain.isNotEmpty()) {
                fetchData(domain)
            } else {
                Toast.makeText(this, "Ingrese un dominio válido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // con los datos llama al servicio con el domain y la api key y obtiene los datos de record types y los guarda en un arrglo
    private fun fetchData(domain: String) {
        // Antes de realizar la llamada, limpia la lista de datos
        listNames.clear()

        val apiKey = "CmZ0fFuL0UXBN77mDQQ/rw==ljDCeYJuHYqttvgf"

        val service = RetrofitClient.retrofitService
        val call = service.getDNSLookup(domain, apiKey)

        call.enqueue(object : Callback<ninjaRespones> {
            override fun onResponse(call: Call<ninjaRespones>, response: Response<ninjaRespones>) {
                if (response.isSuccessful) {
                    val data = response.body()

                    val stringBuilder = StringBuilder()
                    data?.forEach { item ->
                        listNames.add(item.record_type)

                    }
                    createPieChart(listNames)
                } else {
                    // Maneja el error de respuesta
                }
            }

            override fun onFailure(call: Call<ninjaRespones>, t: Throwable) {
                // Maneja el error de conexión
            }
        })
    }

    // Crea un hashmap donde se guarda el type record (key) y su cantidad (value)
    private fun findFrequency(array: List<String>): HashMap<String, Int> {
        val frequencyMap = HashMap<String, Int>()

        for (element in array) {
            if (frequencyMap.containsKey(element)) {
                frequencyMap[element] = frequencyMap[element]!! + 1
            } else {
                frequencyMap[element] = 1
            }
        }
        return frequencyMap
    }

    // Genera el gráfico de pastel de la cantidad de record types
    private fun createPieChart(listNames: ArrayList<String>) {
        val list: ArrayList<PieEntry> = ArrayList()
        val hashBazar = findFrequency(listNames)

        val entries = hashBazar.entries
        pieChart = findViewById(R.id.pie_chart_rv)

        // itera por el hashmap para graficar key(bazar) y value(cantidad de record types)
        for ((key, value) in entries) {
            val valueFloat = value.toFloat()
            list.add(PieEntry(valueFloat, key))
            println(key)
            println("--<>---")
            println(valueFloat)
        }

        // grafica y la detalla
        val pieDataSet = PieDataSet(list, "List")
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
        pieDataSet.valueTextSize = 8f
        pieDataSet.valueTextColor = Color.BLACK
        val pieData = PieData(pieDataSet)
        pieChart.data = pieData
        pieChart.description.text = "Ventas por bazar"
        pieChart.centerText = "List"
        pieChart.notifyDataSetChanged()
        pieChart.invalidate()
    }
}






