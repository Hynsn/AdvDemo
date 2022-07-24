package com.hynson.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.hynson.R

class TestItemAdapter(private val context: Context, var testItemList: List<TestItem>) :
    RecyclerView.Adapter<TestItemAdapter.TestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        return TestViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_test, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val testItem = testItemList[position]
        holder.tvItem.text = testItemList[position].title
        holder.tvItem.setOnClickListener { v ->
            testItem.intentClass?.let {
                val intent = Intent(context, testItem.intentClass)
                intent.apply {
                    testItem.bundle?.let {
                        putExtras(it)
                    }
                }
                context.startActivity(intent)
                return@setOnClickListener
            }
            testItem.resId?.let {
                val controller = Navigation.findNavController(v)
                controller.navigate(it,testItem.bundle)
            }
            testItem.clickAction?.invoke()
        }
    }

    override fun getItemCount(): Int {
        return testItemList.size
    }

    inner class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvItem: TextView = itemView.findViewById(R.id.btn_item)
    }
}


