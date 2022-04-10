package com.example.covidinfo_world

import android.content.Context
import android.icu.text.NumberFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

class Adapter(context: Context, countrylist: List<ModelClass>) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    var m = 1
    var context: Context
    var countrylist: List<ModelClass>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_item, null, false)
        return ViewHolder(view)
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelClass = countrylist[position]
        if (m == 1) {
            holder.cases.text = NumberFormat.getInstance().format(modelClass.cases.toInt().toLong())
        } else if (m == 2) {
            holder.cases.text = NumberFormat.getInstance().format(modelClass.recovered.toInt().toLong())
        } else if (m == 3) {
            holder.cases.text = NumberFormat.getInstance().format(modelClass.deaths.toInt().toLong())
        } else {
            holder.cases.text = NumberFormat.getInstance().format(modelClass.active.toInt().toLong())
        }
        holder.country.text = modelClass.country
    }

    override fun getItemCount(): Int {
        return countrylist.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cases: TextView
        var country: TextView

        init {
            cases = itemView.findViewById(R.id.countrycase)
            country = itemView.findViewById(R.id.countryname)
        }
    }

    fun filter(charText: String) {
        if (charText == "cases") {
            m = 1
        }
    }

    override fun notifyDataSetChanged()

    init {
        this.context = context
        this.countrylist = countrylist
    }

    init {
        m = 2
    }

    init {
        m = 3
    }

    init {
        m = 4
    }
}