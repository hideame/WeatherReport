package com.example.weatherreport

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)        // Realmライブラリの初期化
        val config = RealmConfiguration.Builder().build()
        Realm.setDefaultConfiguration(config)   // RealmConfigurationの設定
    }
}