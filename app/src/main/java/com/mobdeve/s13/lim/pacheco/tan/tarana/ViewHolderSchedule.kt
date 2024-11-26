package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ItemLayoutScheduleBinding
import java.util.Calendar
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
                        showEditEventNameDialog(event)
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

    private fun showEditEventNameDialog(event: Event) {
        val context = itemView.context
        val dialogView = LayoutInflater.from(context).inflate(R.layout.modal_schedule_edit_event, null)

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

        val btnCancel = dialogView.findViewById<ImageButton>(R.id.modal_schedule_edit_event_btn_close)
        val btnEdit = dialogView.findViewById<Button>(R.id.modal_schedule_edit_event_btn_save)
        val editStart = dialogView.findViewById<EditText>(R.id.modal_schedule_edit_start_et)
        val editEnd = dialogView.findViewById<EditText>(R.id.modal_schedule_edit_end_et)

        editStart.setOnClickListener {
            var cal = Calendar.getInstance()
            var year = cal.get(Calendar.YEAR)
            var month = cal.get(Calendar.MONTH)
            var day = cal.get(Calendar.DAY_OF_MONTH)

            var datePickerDialog = DatePickerDialog(context, { view, year, month, dayOfMonth ->
                var month = month + 1
                var date = "$year/$month/$dayOfMonth"
                editStart.setText(date)
            }, year, month, day)
            datePickerDialog.show()
        }

        editEnd.setOnClickListener {
            var cal = Calendar.getInstance()
            var year = cal.get(Calendar.YEAR)
            var month = cal.get(Calendar.MONTH)
            var day = cal.get(Calendar.DAY_OF_MONTH)

            var datePickerDialog = DatePickerDialog(context, { view, year, month, dayOfMonth ->
                var month = month + 1
                var date = "$year/$month/$dayOfMonth"
                editEnd.setText(date)
            }, year, month, day)
            datePickerDialog.show()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnEdit.setOnClickListener {
            // TODO: EDIT EVENT LOGIC
            dialog.dismiss()
        }

        dialog.show()
    }
}