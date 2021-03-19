package com.example.weatherreport

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import io.realm.RealmResults

class CustomRecyclerViewAdapter(realmResults: RealmResults<WeatherReport>) : RecyclerView.Adapter<ViewHolder>() {
    private val rResults: RealmResults<WeatherReport> = realmResults

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.one_result, parent, false)
        val viewholder = ViewHolder(view)
        return viewholder
    }

    override fun getItemCount(): Int {
        return rResults.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val weatherReport = rResults[position]
        holder.dateText?.text = DateFormat.format("yyyy/MM/dd kk:mm", weatherReport?.dateTime)
        holder.titleText?.text = weatherReport?.title.toString()
        holder.placeText?.text = weatherReport?.place.toString()
        holder.nameText?.text = weatherReport?.name.toString()
        holder.itemView.setOnClickListener{
            val intent = Intent(it.context, EditActivity::class.java)
            intent.putExtra("id", weatherReport?.id)
            it.context.startActivity(intent)
        }
    }
}