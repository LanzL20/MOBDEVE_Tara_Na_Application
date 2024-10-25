package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ItemLayoutScheduleBinding

class AdapterSchedule(private var data: ArrayList<Event>): RecyclerView.Adapter<ViewHolderSchedule>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSchedule {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemLayoutScheduleBinding.inflate(inflater, parent, false)
        val viewHolder = ViewHolderSchedule(viewBinding)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolderSchedule, position: Int) {
        holder.bindData(data[position])
    }
}