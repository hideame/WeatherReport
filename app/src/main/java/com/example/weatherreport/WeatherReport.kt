package com.example.weatherreport

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class WeatherReport : RealmObject() {
    @PrimaryKey
    var id: Long = 0        // プライマリーキーを設定
    var dateTime: Date = Date()
    var title: String = ""
    var place: String = ""
    var name: String = ""
}