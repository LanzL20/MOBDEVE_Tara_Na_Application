package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.os.Bundle
import android.util.Log
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
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityCalendarMainBinding
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale


class CalendarMainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityCalendarMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activityCalendarMainCalendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
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
                    Toast.makeText(this@CalendarMainActivity, "Clicked: ${data.date}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100) // Adjust as needed
        val endMonth = currentMonth.plusMonths(100) // Adjust as needed
        val daysOfWeek = daysOfWeek() // Available from the library
        binding.activityCalendarMainCalendarView.setup(startMonth, endMonth, daysOfWeek.first())
        binding.activityCalendarMainCalendarView.scrollToMonth(currentMonth)

        binding.activityCalendarMainCalendarView.monthHeaderBinder = object :
            MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                (container.titlesContainer.findViewById<TextView>(R.id.calendar_day_titles_container_tv) as TextView).text =
                    data.yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()) +
                            " " + data.yearMonth.year

                container.titlesContainer.findViewById<ImageButton>(R.id.calendar_day_titles_container_ib_left).setOnClickListener {
                    binding.activityCalendarMainCalendarView.smoothScrollToMonth(data.yearMonth.minusMonths(1))
                }
                container.titlesContainer.findViewById<ImageButton>(R.id.calendar_day_titles_container_ib_right).setOnClickListener {
                    binding.activityCalendarMainCalendarView.smoothScrollToMonth(data.yearMonth.plusMonths(1))
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
        binding.activityCalendarMainRv.adapter = adapter
        binding.activityCalendarMainRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val dividerItemDecoration = DividerItemDecoration(
            this, LinearLayoutManager.VERTICAL
        )
        binding.activityCalendarMainRv.addItemDecoration(dividerItemDecoration)

    }
}