package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityScheduleMainBinding
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ModalScheduleAddEventBinding
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale


class ScheduleMainActivity: AppCompatActivity() {
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
        modal.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        binding.activityScheduleMainIb.setOnClickListener {
            // reset fields
            modalBinding.modalScheduleAddEventEt1.setText("")
            modalBinding.modalScheduleAddEventEt2.setText("")
            modalBinding.modalScheduleAddEventEt3.setText("")
            modal.show()
        }
        modalBinding.modalScheduleAddEventBtn.setOnClickListener {
            if(modalBinding.modalScheduleAddEventEt1.text.toString().isEmpty() || modalBinding.modalScheduleAddEventEt2.text.toString().isEmpty() || modalBinding.modalScheduleAddEventEt3.text.toString().isEmpty()){
                Toast.makeText(this, "Please fill up all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            modal.dismiss()
        }

        modalBinding.modalScheduleAddEventEt2.setOnClickListener{
            var cal = Calendar.getInstance()
            var year = cal.get(Calendar.YEAR)
            var month = cal.get(Calendar.MONTH)
            var day = cal.get(Calendar.DAY_OF_MONTH)

            var datePickerDialog = DatePickerDialog(this,  { view, year, month, dayOfMonth ->
                var month = month + 1
                var date = "$year/$month/$dayOfMonth"
                modalBinding.modalScheduleAddEventEt2.setText(date)
            }, year, month, day)
            datePickerDialog.show()
        }

        modalBinding.modalScheduleAddEventEt3.setOnClickListener{
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

        binding.activityScheduleMainCalendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                // TODO: Temp code for testing multiple colors
                if(data.date.dayOfMonth == 20){
                    container.constraintLayout.setBackgroundResource(R.drawable.date_round_light_orange_10_no_outline)
                    container.textView.setTextColor(0xFFF6895A.toInt())
                }else if(data.date.dayOfMonth == 21){
                    container.constraintLayout.setBackgroundResource(R.drawable.date_round_dark_orange_10_no_outline)
                    container.textView.setTextColor(0xFFF8F6F6.toInt())
                }else if(data.date.dayOfMonth == 22){
                    container.constraintLayout.setBackgroundResource(R.drawable.date_round_light_yellow_10_no_outline)
                    container.textView.setTextColor(0xFFF7CE21.toInt())
                }else{
                    container.constraintLayout.setBackgroundResource(R.drawable.date_round_white_10_gray_outline)
                    container.textView.setTextColor(0xFF000000.toInt())
                }
                container.textView.text = data.date.dayOfMonth.toString()

                container.constraintLayout.setOnClickListener {
                    Toast.makeText(this@ScheduleMainActivity, "Clicked: ${data.date}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100) // Adjust as needed
        val endMonth = currentMonth.plusMonths(100) // Adjust as needed
        val daysOfWeek = daysOfWeek() // Available from the library
        binding.activityScheduleMainCalendarView.setup(startMonth, endMonth, daysOfWeek.first())
        binding.activityScheduleMainCalendarView.scrollToMonth(currentMonth)

        binding.activityScheduleMainCalendarView.monthHeaderBinder = object :
            MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                (container.titlesContainer.findViewById<TextView>(R.id.calendar_day_titles_container_tv)).text =
                    data.yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()) +
                            " " + data.yearMonth.year

                container.titlesContainer.findViewById<ImageButton>(R.id.calendar_day_titles_container_ib_left).setOnClickListener {
                    binding.activityScheduleMainCalendarView.smoothScrollToMonth(data.yearMonth.minusMonths(1))
                }
                container.titlesContainer.findViewById<ImageButton>(R.id.calendar_day_titles_container_ib_right).setOnClickListener {
                    binding.activityScheduleMainCalendarView.smoothScrollToMonth(data.yearMonth.plusMonths(1))
                }

                if (container.titlesContainer.tag == null) {
                    container.titlesContainer.tag = data.yearMonth
                    container.titlesContainer.findViewById<LinearLayout>(R.id.calendar_day_titles_container_ll).children.map { it as TextView }
                        .forEachIndexed { index, textView ->
                            val dayOfWeek = daysOfWeek[index]
                            val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                            textView.text = title
                        }
                }
            }
        }

        var eventList = ArrayList<Event>()
        eventList.add(Event(1, "Lunch with friends", LocalDate.of(2021, 8, 20), Event.EVENT_TYPE_LAKWATSA))
        eventList.add(Event(2, "Dentist Appointment", LocalDate.of(2021, 8, 21), Event.EVENT_TYPE_UNAVAILABLE))
        eventList.add(Event(3, "Movie Night", LocalDate.of(2021, 8, 22), Event.EVENT_TYPE_LAKWATSA))

        val adapter = AdapterSchedule(eventList)
        binding.activityScheduleMainRv.adapter = adapter
        binding.activityScheduleMainRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val dividerItemDecoration = DividerItemDecoration(
            this, LinearLayoutManager.VERTICAL
        )
        binding.activityScheduleMainRv.addItemDecoration(dividerItemDecoration)

    }
}