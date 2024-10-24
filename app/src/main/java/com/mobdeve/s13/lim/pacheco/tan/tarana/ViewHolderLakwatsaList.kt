package com.mobdeve.s13.lim.pacheco.tan.tarana

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityLakwatsaListBinding

class ViewHolderLakwatsaList(private val viewBinding: ActivityLakwatsaListBinding): RecyclerView.ViewHolder(viewBinding.root) {
    fun bindData(lakwatsa: Lakwatsa) {
        //TODO: Change the naming scheme of items in the layout file
        viewBinding.upcomingTitle.text = lakwatsa.lakwatsaTitle
        viewBinding.upcomingDay1.text = lakwatsa.date.day.toString()
        viewBinding.upcomingMonth1.text = lakwatsa.date.month.toString()
    }
}