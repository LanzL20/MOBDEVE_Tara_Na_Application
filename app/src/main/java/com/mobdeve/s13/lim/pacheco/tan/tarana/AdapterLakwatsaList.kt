package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityLakwatsaListBinding
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ItemLayoutLakwatsaListBinding

class AdapterLakwatsaList(private var data: ArrayList<Lakwatsa>): RecyclerView.Adapter<ViewHolderLakwatsaList>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderLakwatsaList {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemLayoutLakwatsaListBinding.inflate(layoutInflater, parent, false)

        return ViewHolderLakwatsaList(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolderLakwatsaList, position: Int) {
        holder.bindData(data[position])

        holder.itemView.setOnClickListener {
            if(data[position].status == Lakwatsa.LAKWATSA_UPCOMING){
                val intent = Intent(holder.itemView.context, LakwatsaUpcomingActivity::class.java)
                intent.putExtra(Lakwatsa.ID_KEY, data[position].lakwatsaId)
                holder.itemView.context.startActivity(intent)
            }
            else if (data[position].status == Lakwatsa.LAKWATSA_ONGOING){
                val intent = Intent(holder.itemView.context, LakwatsaOngoingActivity::class.java)
                intent.putExtra(Lakwatsa.ID_KEY, data[position].lakwatsaId)
                holder.itemView.context.startActivity(intent)
            }
            else if (data[position].status == Lakwatsa.LAKWATSA_COMPLETED){
                val intent = Intent(holder.itemView.context, LakwatsaCompletedActivity::class.java)
                intent.putExtra(Lakwatsa.ID_KEY, data[position].lakwatsaId)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(lakwatsas: ArrayList<Lakwatsa>){
        this.data = lakwatsas
    }
}