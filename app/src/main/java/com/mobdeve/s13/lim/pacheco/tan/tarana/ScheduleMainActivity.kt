package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.split
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityFriendsViewBinding
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityScheduleMainBinding
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ModalScheduleAddEventBinding
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale


class ScheduleMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScheduleMainBinding
    private lateinit var modalBinding: ModalScheduleAddEventBinding
    private lateinit var modal: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityScheduleMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        modal = Dialog(this)
        modalBinding = ModalScheduleAddEventBinding.inflate(layoutInflater)
        modal.setContentView(modalBinding.root)
        modal.window!!.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )

        modal.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        binding.activityScheduleMainIb.setOnClickListener {
            // reset fields
            modalBinding.modalScheduleAddEventEt1.setText("")
            modalBinding.modalScheduleAddEventEt2.setText("")
            modalBinding.modalScheduleAddEventEt3.setText("")
            modal.show()
        }

        modalBinding.modalScheduleAddEventBtn.setOnClickListener {
            if (modalBinding.modalScheduleAddEventEt1.text.toString()
                    .isEmpty() || modalBinding.modalScheduleAddEventEt2.text.toString()
                    .isEmpty() || modalBinding.modalScheduleAddEventEt3.text.toString().isEmpty()
            ) {
                Toast.makeText(this, "Please fill up all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val startDate = modalBinding.modalScheduleAddEventEt2.text.toString()
            val endDate = modalBinding.modalScheduleAddEventEt3.text.toString()

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
                Toast.makeText(this, "Invalid date range", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val unavailable = Unavailable(
                modalBinding.modalScheduleAddEventEt1.text.toString(),
                startDateFormatted,
                endDateFormatted
            )
            UserSession.getUser().addUnavailable(unavailable)
            DBHelper.updateUser(UserSession.getUser())
            modal.dismiss()
            finish()
            startActivity(getIntent())
        }

        modalBinding.modalScheduleAddEventEt2.setOnClickListener {
            var cal = Calendar.getInstance()
            var year = cal.get(Calendar.YEAR)
            var month = cal.get(Calendar.MONTH)
            var day = cal.get(Calendar.DAY_OF_MONTH)

            var datePickerDialog = DatePickerDialog(this, { view, year, month, dayOfMonth ->
                var month = month + 1
                var date = "$year/$month/$dayOfMonth"
                modalBinding.modalScheduleAddEventEt2.setText(date)
            }, year, month, day)
            datePickerDialog.show()
        }

        modalBinding.modalScheduleAddEventEt3.setOnClickListener {
            var cal = Calendar.getInstance()
            var year = cal.get(Calendar.YEAR)
            var month = cal.get(Calendar.MONTH)
            var day = cal.get(Calendar.DAY_OF_MONTH)

            var datePickerDialog = DatePickerDialog(this, { view, year, month, dayOfMonth ->
                var month = month + 1
                var date = "$year/$month/$dayOfMonth"
                modalBinding.modalScheduleAddEventEt3.setText(date)
            }, year, month, day)
            datePickerDialog.show()
        }

        lifecycleScope.launch {
            var username = intent.getStringExtra(ProfileFriendActivity.USER_KEY)
            var user: User
            if(username == null){
                user = UserSession.getUser()
            } else {
                binding.activityScheduleMainIb.visibility = View.GONE
                user = DBHelper.getUser(username)
            }

            val lakwatsaIds = user.lakwatsaList
            val lakwatsaList = DBHelper.getAllLakwatsaFromList(lakwatsaIds)
            binding.activityScheduleMainCalendarView.dayBinder =
                object : MonthDayBinder<DayViewContainer> {
                    // Called only when a new container is needed.
                    override fun create(view: View) = DayViewContainer(view)

                    // Called every time we need to reuse a container.
                    override fun bind(container: DayViewContainer, data: CalendarDay) {
                        // TODO: Temp code for testing multiple colors
                        // for every lawaktsa event, set the background to a different color

                        container.constraintLayout.setBackgroundResource(R.drawable.date_round_white_10_gray_outline)
                        container.textView.setTextColor(0xFF000000.toInt())

                        for (lakwatsa in lakwatsaList) {
                            if (data.date.dayOfMonth == lakwatsa.date.split("/")[2].toInt() && data.date.monthValue == lakwatsa.date.split("/")[1].toInt() && data.date.year == lakwatsa.date.split("/")[0].toInt()) {
                                container.constraintLayout.setBackgroundResource(R.drawable.date_round_light_yellow_10_no_outline)
                                container.textView.setTextColor(0xFFF7CE21.toInt())
                            }
                        }

                        //TODO: For every unavailable...
                        for(unavailable in user.unavailableList) {
                            val blockDateFormatted = data.date.toString().replace("-", "/")
                            val startDateFormatted = unavailable.startDate
                            val endDateFormatted = unavailable.endDate

                            if(blockDateFormatted >= startDateFormatted && blockDateFormatted <= endDateFormatted){
                                container.constraintLayout.setBackgroundResource(R.drawable.date_round_light_orange_10_no_outline)
                                container.textView.setTextColor(0xFFF6895A.toInt())
                            }

                        }

                        // get current date
                        val current = LocalDate.now()
                        if (data.date == current) {
                            container.constraintLayout.setBackgroundResource(R.drawable.date_round_dark_orange_10_no_outline)
                            container.textView.setTextColor(0xFFF8F6F6.toInt())
                        }

                        container.textView.text = data.date.dayOfMonth.toString()

                        /*
                        container.constraintLayout.setOnClickListener {
                            Toast.makeText(
                                this@ScheduleMainActivity,
                                "Clicked: ${data.date}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }*/
                    }
                }
            val currentMonth = YearMonth.now()
            val startMonth = currentMonth.minusMonths(100) // Adjust as needed
            val endMonth = currentMonth.plusMonths(100) // Adjust as needed
            val daysOfWeek = daysOfWeek() // Available from the library
            binding.activityScheduleMainCalendarView.setup(startMonth, endMonth, daysOfWeek.first())
            binding.activityScheduleMainCalendarView.scrollToMonth(currentMonth)

            binding.activityScheduleMainCalendarView.monthHeaderBinder =
                object : MonthHeaderFooterBinder<MonthViewContainer> {
                    override fun create(view: View) = MonthViewContainer(view)
                    override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                        (container.titlesContainer.findViewById<TextView>(R.id.calendar_day_titles_container_tv)).text =
                            data.yearMonth.month.getDisplayName(
                                TextStyle.FULL,
                                Locale.getDefault()
                            ) + " " + data.yearMonth.year

                        container.titlesContainer.findViewById<ImageButton>(R.id.calendar_day_titles_container_ib_left)
                            .setOnClickListener {
                                binding.activityScheduleMainCalendarView.smoothScrollToMonth(
                                    data.yearMonth.minusMonths(
                                        1
                                    )
                                )
                            }
                        container.titlesContainer.findViewById<ImageButton>(R.id.calendar_day_titles_container_ib_right)
                            .setOnClickListener {
                                binding.activityScheduleMainCalendarView.smoothScrollToMonth(
                                    data.yearMonth.plusMonths(
                                        1
                                    )
                                )
                            }

                        if (container.titlesContainer.tag == null) {
                            container.titlesContainer.tag = data.yearMonth
                            container.titlesContainer.findViewById<LinearLayout>(R.id.calendar_day_titles_container_ll).children.map { it as TextView }
                                .forEachIndexed { index, textView ->
                                    val dayOfWeek = daysOfWeek[index]
                                    val title = dayOfWeek.getDisplayName(
                                        TextStyle.SHORT,
                                        Locale.getDefault()
                                    )
                                    textView.text = title
                                }
                        }
                    }
                }


            val adapter = AdapterSchedule(user.unavailableList, lakwatsaList, username != null)
            binding.activityScheduleMainRv.adapter = adapter
            binding.activityScheduleMainRv.layoutManager = LinearLayoutManager(
                binding.activityScheduleMainRv.context, LinearLayoutManager.VERTICAL, false
            )

            val dividerItemDecoration = DividerItemDecoration(
                binding.activityScheduleMainRv.context, LinearLayoutManager.VERTICAL
            )
            binding.activityScheduleMainRv.addItemDecoration(dividerItemDecoration)

            // NAVIGATION BUTTONS

            binding.activityScheduleMainIbInbox.setOnClickListener {
                val intent =
                    Intent(binding.activityScheduleMainRv.context, ProfileInboxActivity::class.java)
                startActivity(intent)
            }
            binding.activityScheduleMainIbSettings.setOnClickListener {
                val intent = Intent(
                    binding.activityScheduleMainRv.context, ProfileSettingActivity::class.java
                )
                startActivity(intent)
            }
            binding.activityScheduleMainProfileUser.setOnClickListener {
                val intent =
                    Intent(binding.activityScheduleMainRv.context, ProfileUserActivity::class.java)
                startActivity(intent)
            }
        }


    }
}