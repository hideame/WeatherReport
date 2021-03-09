package com.example.weatherreport

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.one_result.view.*

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var dateText: TextView? = null
    var titleText: TextView? = null
    var placeText: TextView? = null
    var nameText: TextView? = null
    init {
        // ビューホルダーのプロパティとレイアウトのViewの対応
        dateText = itemView.dateText
        titleText = itemView.titleText
        placeText = itemView.placeText
        nameText = itemView.nameText
    }
}