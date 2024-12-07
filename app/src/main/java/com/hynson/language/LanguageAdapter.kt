package com.hynson.language

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.hynson.R

class LanguageAdapter(context: Context, val resource: Int, objects: List<AppLanguage>) :
    ArrayAdapter<AppLanguage>(context, resource, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val view = LayoutInflater.from(context).inflate(resource, parent, false)
        view.findViewById<TextView>(R.id.tv_name).text = item?.name
        return view
    }
}