package com.example.covidinfo_world

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hbb20.CountryCodePicker
import com.hbb20.CountryCodePicker.OnCountryChangeListener
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity(), OnItemSelectedListener {
    var countryCodePicker: CountryCodePicker? = null
    var mtodaytotal: TextView? = null
    var mtotal: TextView? = null
    var mactive: TextView? = null
    var mtodayactive: TextView? = null
    var mrecovered: TextView? = null
    var mtodayrecovered: TextView? = null
    var mdeaths: TextView? = null
    var mtodaydeaths: TextView? = null
    var country: String? = null
    var mfilter: TextView? = null
    var spinner: Spinner? = null
    var types = arrayOf<String?>("cases", "deaths", "recovered", "active")
    private var modelClassList: MutableList<ModelClass>? = null
    private var modelClassList2: MutableList<ModelClass>? = null
    var mpiechart: PieChart? = null
    private var recyclerview: RecyclerView? = null
    var adapter: Adapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        countryCodePicker = findViewById(R.id.ccp)
        mtodayactive = findViewById(R.id.todayactive)
        mactive = findViewById(R.id.activecase)
        mdeaths = findViewById(R.id.totaldeath)
        mtodaydeaths = findViewById(R.id.todaydeath)
        mrecovered = findViewById(R.id.recoveredcase)
        mtodayrecovered = findViewById(R.id.todayrecovered)
        mtotal = findViewById(R.id.totalcase)
        mtodaytotal = findViewById(R.id.todaytotal)
        mpiechart = findViewById(R.id.piechart)
        spinner = findViewById(R.id.spinner)
        mfilter = findViewById(R.id.flter)
        recyclerview = findViewById(R.id.recyclerview)
        modelClassList = ArrayList()
        modelClassList2 = ArrayList()
        spinner.setOnItemSelectedListener(this)
        val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_dropdown_item, types)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(arrayAdapter)
        ApiUtilities.aPIInterface.getcountrydata().enqueue(object : Callback<List<ModelClass>?> {
            override fun onResponse(call: Call<List<ModelClass>?>, response: Response<List<ModelClass>?>) {
                modelClassList2.addAll(response.body()!!)
                //adapter.notify
                adapter!!.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<ModelClass>?>, t: Throwable) {}
        })
        adapter = Adapter(applicationContext, modelClassList2)
        recyclerview.setLayoutManager(LinearLayoutManager(this))
        recyclerview.setHasFixedSize(true)
        recyclerview.setAdapter(adapter)
        countryCodePicker.setAutoDetectedCountry(true)
        country = countryCodePicker.getSelectedCountryName()
        countryCodePicker.setOnCountryChangeListener(OnCountryChangeListener {
            country = countryCodePicker.getSelectedCountryName()
            fetchdata()
        })
        fetchdata()
    }

    private fun fetchdata() {
        ApiUtilities.aPIInterface.getcountrydata().enqueue(object : Callback<List<ModelClass>?> {
            override fun onResponse(call: Call<List<ModelClass>?>, response: Response<List<ModelClass>?>) {
                modelClassList!!.addAll(response.body()!!)
                for (i in modelClassList!!.indices) {
                    if (modelClassList!![i].country == country) {
                        mactive!!.text = modelClassList!![i].active
                        mtodaydeaths!!.text = modelClassList!![i].todayDeaths
                        mtodayrecovered!!.text = modelClassList!![i].todayRecovered
                        mtodaytotal!!.text = modelClassList!![i].todayCases
                        mtotal!!.text = modelClassList!![i].cases
                        mdeaths!!.text = modelClassList!![i].deaths
                        mrecovered!!.text = modelClassList!![i].recovered
                        var active: Int
                        var total: Int
                        var recovered: Int
                        var deaths: Int
                        active = modelClassList!![i].active.toInt()
                        total = modelClassList!![i].cases.toInt()
                        recovered = modelClassList!![i].recovered.toInt()
                        deaths = modelClassList!![i].deaths.toInt()
                        updateGraph(active, total, recovered, deaths)
                    }
                }
            }

            override fun onFailure(call: Call<List<ModelClass>?>, t: Throwable) {}
        })
    }

    private fun updateGraph(active: Int, total: Int, recovered: Int, deaths: Int) {
        mpiechart!!.clearChart()
        mpiechart!!.addPieSlice(PieModel("Confirm", total.toFloat(), Color.parseColor("#FFB701")))
        mpiechart!!.addPieSlice(PieModel("Active", active.toFloat(), Color.parseColor("#FF4caf50")))
        mpiechart!!.addPieSlice(PieModel("Recovered", recovered.toFloat(), Color.parseColor("#38ACCD")))
        mpiechart!!.addPieSlice(PieModel("Deaths", deaths.toFloat(), Color.parseColor("#F55c47")))
        mpiechart!!.startAnimation()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        val item = types[position]
        mfilter!!.text = item
        adapter!!.filter(item)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}