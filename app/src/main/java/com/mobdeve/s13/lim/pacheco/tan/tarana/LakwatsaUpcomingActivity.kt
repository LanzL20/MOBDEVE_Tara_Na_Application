package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityLakwatsaUpcomingBinding
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ModalDeleteItemBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LakwatsaUpcomingActivity: AppCompatActivity() {
    private lateinit var viewBinding: ActivityLakwatsaUpcomingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLakwatsaUpcomingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.optionsIcon.visibility = View.GONE
        lifecycleScope.launch {
            val lakwatsa = DBHelper.getLakwatsa(intent.getStringExtra(Lakwatsa.ID_KEY)!!)
            if(lakwatsa.lakwatsaAdmin == UserSession.getUser().username){
                viewBinding.optionsIcon.visibility = View.VISIBLE
            }
            viewBinding.activityTitleTv.text = lakwatsa.lakwatsaTitle
            viewBinding.activityLakwtsaUpcomingDate.text = lakwatsa.date
            if(lakwatsa.time.isNullOrBlank()){
                viewBinding.activityLakwtsaUpcomingTime.text = "(No time set yet...)"
            } else{
                viewBinding.activityLakwtsaUpcomingTime.text = lakwatsa.time
            }

            viewBinding.activityLakwatsaUpcomingFriend1Iv.visibility = View.GONE
            viewBinding.activityLakwatsaUpcomingFriend2Iv.visibility = View.GONE
            viewBinding.moreFriends.visibility = View.GONE
            if(lakwatsa.lakwatsaUsers.size == 1){
                viewBinding.activityLakwatsaUpcomingFriend1Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[0]).profilePicture, "drawable", packageName))
                viewBinding.activityLakwatsaUpcomingFriend1Iv.visibility = View.VISIBLE
            }
            else if (lakwatsa.lakwatsaUsers.size == 2){
                viewBinding.activityLakwatsaUpcomingFriend1Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[0]).profilePicture, "drawable", packageName))
                viewBinding.activityLakwatsaUpcomingFriend2Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[1]).profilePicture, "drawable", packageName))
                viewBinding.activityLakwatsaUpcomingFriend1Iv.visibility = View.VISIBLE
                viewBinding.activityLakwatsaUpcomingFriend2Iv.visibility = View.VISIBLE
            }
            else {
                viewBinding.activityLakwatsaUpcomingFriend1Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[0]).profilePicture, "drawable", packageName))
                viewBinding.activityLakwatsaUpcomingFriend1Iv.visibility = View.VISIBLE
                viewBinding.activityLakwatsaUpcomingFriend2Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[1]).profilePicture, "drawable", packageName))
                viewBinding.activityLakwatsaUpcomingFriend2Iv.visibility = View.VISIBLE
                viewBinding.moreFriendsTv.setText("+" + (lakwatsa.lakwatsaUsers.size - 2).toString())
            }
            // TODO: Add more friends
            viewBinding.addFriends.setOnClickListener{

            }
            var time = ""
            if(viewBinding.activityLakwtsaUpcomingTime.text.toString() != "(No time set yet...)"){
                time = viewBinding.activityLakwtsaUpcomingTime.text.toString()
            }
            viewBinding.activityLakwtsaUpcomingStartBtn.setOnClickListener{
                val newLakwatsa = Lakwatsa(
                    lakwatsa.lakwatsaId,
                    lakwatsa.lakwatsaUsers,
                    lakwatsa.locationLatitude,
                    lakwatsa.locationLongitude,
                    viewBinding.activityTitleTv.text.toString(),
                    viewBinding.activityLakwtsaUpcomingDate.text.toString(),
                    time,
                    lakwatsa.pollingList,
                    lakwatsa.album,
                    Lakwatsa.LAKWATSA_ONGOING,
                    lakwatsa.lakwatsaAdmin
                )
                DBHelper.updateLakwatsa(newLakwatsa)
                val intent2 = Intent(this@LakwatsaUpcomingActivity, LakwatsaOngoingActivity::class.java)
                intent2.putExtra(Lakwatsa.ID_KEY, intent.getStringExtra(Lakwatsa.ID_KEY))
                startActivity(intent2)
                finish()
            }
        }

        // NAVIGATION BUTTONS

        viewBinding.inboxIcon.setOnClickListener {
            val intent = Intent(this, ProfileInboxActivity::class.java)
            startActivity(intent)
        }
        viewBinding.settingsIcon.setOnClickListener {
            val intent = Intent(this, ProfileSettingActivity::class.java)
            startActivity(intent)
        }
        viewBinding.profileUser1.setOnClickListener{
            val intent = Intent(this, ProfileUserActivity::class.java)
            startActivity(intent)
        }
        viewBinding.availabilityBtn.setOnClickListener{
            val intent = Intent(this, LakwatsaAvailabilityActivity::class.java)
            startActivity(intent)
        }

        viewBinding.optionsIcon.setOnClickListener { view ->
            val contextWrapper = ContextThemeWrapper(this, R.style.CustomPopupMenu)
            val popupMenu = PopupMenu(contextWrapper, view)

            popupMenu.menu.add(0, 1, 0, "Delete lakwatsa")
            popupMenu.menu.add(0, 2, 1, "Edit details")

            popupMenu.show()

            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    1 -> {
                        showDeleteConfirmationDialog()
                        true
                    }
                    2 -> {
                        showEditDetailsDateDialog()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun showDeleteConfirmationDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.modal_delete_item, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val btnCancel = dialogView.findViewById<ImageButton>(R.id.modal_delete_item_btn_close)
        val btnConfirm = dialogView.findViewById<Button>(R.id.modal_delete_item_btn_delete)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            // TODO: DELETE LAKWATSA LOGIC
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showEditDetailsDateDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.modal_edit_details_date, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val btnCancel = dialogView.findViewById<ImageButton>(R.id.modal_edit_details_date_btn_close)
        val btnConfirm = dialogView.findViewById<Button>(R.id.modal_edit_details_date_btn_save)
        val editName = dialogView.findViewById<EditText>(R.id.modal_schedule_edit_name_et)
        val editDate = dialogView.findViewById<EditText>(R.id.modal_lakwatsa_edit_date_et)
        val editTime = dialogView.findViewById<EditText>(R.id.modal_lakwatsa_edit_time_et)

        editName.setText(viewBinding.activityTitleTv.text)
        editDate.setText(viewBinding.activityLakwtsaUpcomingDate.text)
        if(viewBinding.activityLakwtsaUpcomingTime.text == "(No time set yet...)"){
            editTime.setText("")
        } else{
            editTime.setText(viewBinding.activityLakwtsaUpcomingTime.text)
        }

        editDate.setOnClickListener {
            var cal = Calendar.getInstance()
            var year = cal.get(Calendar.YEAR)
            var month = cal.get(Calendar.MONTH)
            var day = cal.get(Calendar.DAY_OF_MONTH)

            var datePickerDialog = DatePickerDialog(this, { view, year, month, dayOfMonth ->
                var month = month + 1
                var date = "$year/$month/$dayOfMonth"
                editDate.setText(date)
            }, year, month, day)
            datePickerDialog.show()
        }

        editTime.setOnClickListener {
            val cal = Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minute = cal.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minuteOfHour ->
                val format = SimpleDateFormat("h:mm a", Locale.getDefault())
                val time = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minuteOfHour)
                }.time
                val formattedTime = format.format(time)

                editTime.setText(formattedTime)
            }, hour, minute, false)
            timePickerDialog.show()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            lifecycleScope.launch {
                val lakwatsa = DBHelper.getLakwatsa(intent.getStringExtra(Lakwatsa.ID_KEY)!!)

                val newLakwatsa = Lakwatsa(lakwatsa.lakwatsaId,
                    lakwatsa.lakwatsaUsers,
                    lakwatsa.locationLatitude,
                    lakwatsa.locationLongitude,
                    editName.text.toString(),
                    editDate.text.toString(),
                    editTime.text.toString(),
                    lakwatsa.pollingList,
                    lakwatsa.album,
                    lakwatsa.status,
                    lakwatsa.lakwatsaAdmin)
                DBHelper.updateLakwatsa(newLakwatsa)
            }

            dialog.dismiss()
            viewBinding.activityTitleTv.text = editName.text.toString()
            viewBinding.activityLakwtsaUpcomingDate.text = editDate.text.toString()
            viewBinding.activityLakwtsaUpcomingTime.text = editTime.text.toString()
        }

        dialog.show()
    }
}