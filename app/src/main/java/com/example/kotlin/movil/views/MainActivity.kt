package com.example.kotlin.movil.views
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
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
    private lateinit var textViewData: TextView // Declara la variable para la vista textViewData
    private var listNames: ArrayList<String> = ArrayList() // ArrayList para almacenar los nombres

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewData = findViewById(R.id.textViewData) // Inicializa la variable de la vista

        val domain = "example.com"
        val apiKey = "CmZ0fFuL0UXBN77mDQQ/rw==ljDCeYJuHYqttvgf"

        val service = RetrofitClient.retrofitService
        val call = service.getDNSLookup(domain, apiKey)

        call.enqueue(object : Callback<ninjaRespones> {
            override fun onResponse(call: Call<ninjaRespones>, response: Response<ninjaRespones>) {
                if (response.isSuccessful) {
                    val data = response.body()

                    // Accede al TextView desde el layout por su ID
                    val textViewData = findViewById<TextView>(R.id.textViewData)

                    // Construye una cadena de texto con los datos de cada ninjaResponesItem
                    val stringBuilder = StringBuilder()
                    data?.forEach { item ->
                        stringBuilder.append("Expire: ${item.expire}\n")
                        stringBuilder.append("Flags: ${item.flags}\n")
                        stringBuilder.append("Mname: ${item.mname}\n")
                        listNames.add(item.record_type) // Añade el nombre a la lista
                        stringBuilder.append("Record Type: ${item.record_type}\n")
                        stringBuilder.append("Refresh: ${item.refresh}\n")
                        stringBuilder.append("Retry: ${item.retry}\n")
                        stringBuilder.append("Rname: ${item.rname}\n")
                        stringBuilder.append("Serial: ${item.serial}\n")
                        stringBuilder.append("Tag: ${item.tag}\n")
                        stringBuilder.append("TTL: ${item.ttl}\n")
                        stringBuilder.append("Value: ${item.value}\n\n")
                    }

                    // Actualiza el texto del TextView con los datos construidos
                    textViewData.text = stringBuilder.toString()

                    // Una vez que tienes los nombres, puedes usarlos para crear el gráfico de pastel
                    println(listNames)
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

    private fun frequencyNumber(arr: ArrayList<String>): HashMap<String, Int> {
        val freqMap: HashMap<String, Int> = HashMap()

        for (name in arr) {
            if (freqMap.containsKey(name)) {
                freqMap[name] = freqMap[name]!! + 1
            } else {
                freqMap[name] = 1
            }
        }
        return freqMap
    }

    // Genera el gráfico de pastel de la cantidad de ventas por bazar
    private fun createPieChart(listNames: ArrayList<String>) {
        lateinit var pieChart: PieChart
        val list: ArrayList<PieEntry> = ArrayList()
        val hashBazar = frequencyNumber(listNames)
        val entries = hashBazar.entries
        pieChart = findViewById(R.id.bar_chart_rv)

        // itera por el hashmap para graficar key(bazar) y value(cantidad de ventas)
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





