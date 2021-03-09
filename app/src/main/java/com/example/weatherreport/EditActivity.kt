package com.example.weatherreport

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*
import java.util.*

class EditActivity : AppCompatActivity() {
    private val tag = "WeatherReport"
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        realm = Realm.getDefaultInstance()

        saveBtn.setOnClickListener{
            var title: String = ""
            var place: String = ""
            var name: String = ""

            if (!titleEdit.text.isNullOrEmpty()) {
                title = titleEdit.text.toString()
            }
            if (!placeEdit.text.isNullOrEmpty()) {
                place = placeEdit.text.toString()
            }
            if (!nameEdit.text.isNullOrEmpty()) {
                name = nameEdit.text.toString()
            }
            realm.executeTransaction {
                val maxId = realm.where<WeatherReport>().max("id")
                val nextId = (maxId?.toLong() ?: 0L) + 1L
                val weatherReport = realm.createObject<WeatherReport>(nextId)
                weatherReport.dateTime = Date()
                weatherReport.title = title
                weatherReport.place = place
                weatherReport.name = name
            }
            Toast.makeText(applicationContext, "保存しました", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
