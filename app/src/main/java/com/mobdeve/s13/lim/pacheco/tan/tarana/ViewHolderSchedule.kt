package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent.getIntent
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ItemLayoutScheduleBinding
import java.time.LocalDate
import java.util.Calendar
import kotlin.time.Duration.Companion.days

class ViewHolderSchedule(private val viewBinding: ItemLayoutScheduleBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bindData(event: Event, hide: Boolean) {
        viewBinding.itemLayoutScheduleTvDate.text = event.dateDays
        viewBinding.itemLayoutScheduleTvMonth.text = event.dateMonths
        viewBinding.itemLayoutScheduleTvEventName.text = event.eventName
        viewBinding.itemLayoutScheduleTvReason.text = event.eventType

        if(event.eventType == Event.EVENT_TYPE_LAKWATSA || hide){
            viewBinding.itemLayoutScheduleTvDots.visibility = android.view.View.GONE
        } else {
            viewBinding.itemLayoutScheduleTvDots.visibility = android.view.View.VISIBLE
        }

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
            UserSession.getUser().removeUnavailableAtIndex(event.associatedId.toInt())
            DBHelper.updateUser(UserSession.getUser())
            dialog.dismiss()
            var activity = (viewBinding.itemLayoutScheduleTvMonth.context as Activity)
            activity.finish()
            activity.startActivity(activity.intent)
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
        val editName = dialogView.findViewById<EditText>(R.id.modal_schedule_edit_name_et)

        editStart.setText(event.startDateFull)
        editEnd.setText(event.endDateFull)
        editName.setText(event.eventName)

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
            if (editName.text.toString()
                    .isEmpty() || editStart.text.toString()
                    .isEmpty() || editEnd.text.toString().isEmpty()
            ) {
                Toast.makeText(viewBinding.root.context, "Please fill up all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val startDate = editStart.text.toString()
            val endDate = editEnd.text.toString()

            val startDateYear = startDate.split("/")[0]
            val startDateMonth = startDate.split("/")[1].padStart(2, '0')
            val startDateDay = startDate.split("/")[2].padStart(2, '0')

            val endDateYear = endDate.split("/")[0]
            val endDateMonth = endDate.split("/")[1].padStart(2, '0')
            val endDateDay = endDate.split("/")[2].padStart(2, '0')

            val startDateFormatted = "$startDateYear/$startDateMonth/$startDateDay"
            val endDateFormatted = "$endDateYear/$endDateMonth/$endDateDay"

            val current = LocalDate.now()
            val currentFormatted = current.toString().replace("-", "/")

            // if start date is before current date or end date is before start date, show error
            if (startDateFormatted < currentFormatted || endDateFormatted < startDateFormatted) {
                Toast.makeText(viewBinding.root.context, "Invalid date range", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val unavailable = Unavailable(
                editName.text.toString(),
                startDateFormatted,
                endDateFormatted
            )
            UserSession.getUser().updateUnavailableListAtIndex(event.associatedId.toInt(), unavailable)
            DBHelper.updateUser(UserSession.getUser())
            dialog.dismiss()
            var activity = (viewBinding.itemLayoutScheduleTvMonth.context as Activity)
            activity.finish()
            activity.startActivity(activity.intent)
        }

        dialog.show()
    }
}