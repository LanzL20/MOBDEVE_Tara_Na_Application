package com.mobdeve.s13.lim.pacheco.tan.tarana

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ItemLayoutScheduleBinding
import kotlin.time.Duration.Companion.days

class ViewHolderSchedule(private val viewBinding: ItemLayoutScheduleBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bindData(event: Event) {
        viewBinding.itemLayoutScheduleTvDate.text = event.date.dayOfMonth.toString()
        viewBinding.itemLayoutScheduleTvMonth.text = event.date.month.name
        viewBinding.itemLayoutScheduleTvEventName.text = event.eventName
        viewBinding.itemLayoutScheduleTvReason.text = event.eventType
    }
}