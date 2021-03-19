package com.example.weatherreport

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*
import java.util.*
import android.content.Intent
import android.view.View
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.net.Uri
import android.os.ParcelFileDescriptor
import io.realm.internal.IOException


class EditActivity : AppCompatActivity() {
    private val tag = "WeatherReport"
    private lateinit var realm: Realm

    private val RESULT_PICK_IMAGEFILE = 1000
    private var imageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        realm = Realm.getDefaultInstance()

        val bpId = intent.getLongExtra("id",0L)
        if (bpId > 0L) {
            val weatherReport = realm.where<WeatherReport>().equalTo("id", bpId).findFirst()
            titleEdit.setText(weatherReport?.title.toString())
            placeEdit.setText(weatherReport?.place.toString())
            nameEdit.setText(weatherReport?.name.toString())
            deleteBtn.visibility = View.VISIBLE
        } else {
            deleteBtn.visibility = View.INVISIBLE       // データがない場合は削除ボタンを表示しない
        }

        imageView = findViewById(R.id.image_view) as ImageView

        findViewById<View>(R.id.imageBtn).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)        // ボタンクリック時に画像フォルダを開く
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                startActivityForResult(intent, RESULT_PICK_IMAGEFILE)   // 画像を取得
            }
        })

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

            when (bpId) {
                0L -> {
                    realm.executeTransaction {
                        val maxId = realm.where<WeatherReport>().max("id")
                        val nextId = (maxId?.toLong() ?: 0L) + 1L
                        val weatherReport = realm.createObject<WeatherReport>(nextId)
                        weatherReport.dateTime = Date()
                        weatherReport.title = title
                        weatherReport.place = place
                        weatherReport.name = name
                    }
                }
                // 修正処理
                else -> {
                    realm.executeTransaction {
                        val weatherReport = realm.where<WeatherReport>().equalTo("id", bpId).findFirst()
                        weatherReport?.title = title
                        weatherReport?.place = place
                        weatherReport?.name = name
                    }
                }
            }
            Toast.makeText(applicationContext, "保存しました", Toast.LENGTH_SHORT).show()
            finish()
        }

        deleteBtn.setOnClickListener {
            realm.executeTransaction {
                val weatherReport = realm.where<WeatherReport>().equalTo("id", bpId)?.findFirst()?.deleteFromRealm()
            }
            Toast.makeText(applicationContext, "削除しました", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode == RESULT_PICK_IMAGEFILE && resultCode == Activity.RESULT_OK) {
            var uri: Uri? = null
            if (resultData != null) {
                uri = resultData.data       // resultDataの中身は画像パス

                try {
                    val bmp = getBitmapFromUri(uri)     // 画像パスからBitmapを作成
                    imageView?.setImageBitmap(bmp)      // BitmapをLayoutのImageViewにセット
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    @Throws(IOException::class)
    private fun getBitmapFromUri(uri: Uri?): Bitmap {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(uri!!, "r")
        val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
