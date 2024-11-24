package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ItemLayoutScheduleBinding
import kotlin.time.Duration.Companion.days

class ViewHolderSchedule(private val viewBinding: ItemLayoutScheduleBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bindData(event: Event) {
        viewBinding.itemLayoutScheduleTvDate.text = event.date.dayOfMonth.toString()
        viewBinding.itemLayoutScheduleTvMonth.text = event.date.month.name
        viewBinding.itemLayoutScheduleTvEventName.text = event.eventName
        viewBinding.itemLayoutScheduleTvReason.text = event.eventType

        viewBinding.itemLayoutScheduleTvDots.setOnClickListener { view ->
            val contextWrapper = ContextThemeWrapper(itemView.context, R.style.CustomPopupMenu)
            val popupMenu = PopupMenu(contextWrapper, view)

            popupMenu.menu.add(0, 1, 0, "Delete event")
            popupMenu.menu.add(0, 2, 1, "Edit details")

            popupMenu.show()

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    1 -> {
                        showDeleteConfirmationDialog(event)
                        true
                    }
                    2 -> {
                        // showEditEventNameDialog()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun showDeleteConfirmationDialog(event: Event) {
        val context = itemView.context
        val dialogView = LayoutInflater.from(context).inflate(R.layout.modal_delete_item, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        dialog.setOnShowListener {
            val displayMetrics = context.resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val margin = (24 * displayMetrics.density).toInt()

            dialog.window?.setLayout(
                screenWidth - 2 * margin,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        val btnCancel = dialogView.findViewById<ImageButton>(R.id.modal_delete_item_btn_close)
        val btnConfirm = dialogView.findViewById<Button>(R.id.modal_delete_item_btn_delete)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            // TODO: DELETE EVENT LOGIC
            dialog.dismiss()
        }

        dialog.show()
    }
}