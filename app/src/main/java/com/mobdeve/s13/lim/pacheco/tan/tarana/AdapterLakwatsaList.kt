package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityLakwatsaListBinding

class AdapterLakwatsaList(private val data: ArrayList<Lakwatsa>): RecyclerView.Adapter<ViewHolderLakwatsaList>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderLakwatsaList {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ActivityLakwatsaListBinding.inflate(layoutInflater, parent, false)
        val viewHolder = ViewHolderLakwatsaList(itemBinding)

        viewHolder.itemView.setOnClickListener {
            //TODO: Add a click listener for the Lakwatsa item to redirect to the Lakwatsa details page

        }

        return ViewHolderLakwatsaList(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolderLakwatsaList, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}