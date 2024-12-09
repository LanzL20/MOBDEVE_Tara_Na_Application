package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.animation.ArgbEvaluator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityLakwatsaAvailabilityBinding
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class LakwatsaAvailabilityActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLakwatsaAvailabilityBinding

    fun getGradientColorBg(availabilityScore: Float): Int {
        val red = Color.parseColor("#F7E0D7")    // Muted Red
        val yellow = Color.parseColor("#F8EECC") // Muted Yellow/Orange
        val green = Color.parseColor("#79D997")  // Muted Green

        return when {
            availabilityScore <= 0.5f -> {
                // Interpolate between red and yellow
                val fraction = availabilityScore / 0.5f
                ArgbEvaluator().evaluate(fraction, green, yellow) as Int
            }
            else -> {
                // Interpolate between yellow and green
                val fraction = (availabilityScore - 0.5f) / 0.5f
                ArgbEvaluator().evaluate(fraction, yellow, red) as Int
            }
        }
    }

    fun getGradientColorText(availabilityScore: Float): Int {
        val red = Color.parseColor("#F6895A")    // Muted Red
        val yellow = Color.parseColor("#F7CE21") // Muted Yellow/Orange
        val green = Color.parseColor("#4A9662")  // Muted Green

        return when {
            availabilityScore <= 0.5f -> {
                // Interpolate between red and yellow
                val fraction = availabilityScore / 0.5f
                ArgbEvaluator().evaluate(fraction, green, yellow) as Int
            }
            else -> {
                // Interpolate between yellow and green
                val fraction = (availabilityScore - 0.5f) / 0.5f
                ArgbEvaluator().evaluate(fraction, yellow, red) as Int
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLakwatsaAvailabilityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // NAVIGATION BUTTONS
        binding.activityLakwatsaAvailabilityIbSettings.setOnClickListener {
            val intent = Intent(this, ProfileSettingActivity::class.java)
            startActivity(intent)
        }
        binding.activityLakwatsaAvailabilityIbInbox.setOnClickListener {
            val intent = Intent(this, ProfileInboxActivity::class.java)
            startActivity(intent)
        }
        binding.editBtn.setOnClickListener {
            val intent = Intent(this, ScheduleMainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume(){
        super.onResume()
        lifecycleScope.launch {
            var lakwatsa = DBHelper.getLakwatsa(intent.getStringExtra("lakwatsaId")!!)
            var lakwatsaUsers = DBHelper.getUsers(lakwatsa.lakwatsaUsers)
            var lakwatsaForEachUser: HashMap<String, ArrayList<Lakwatsa>> = HashMap()
            for(users in lakwatsaUsers){
                lakwatsaForEachUser[users.username] = DBHelper.getAllLakwatsaFromList(users.lakwatsaList)
            }
            binding.activityLakwatsaAvailabilityCalendarView.dayBinder =
                object : MonthDayBinder<DayViewContainer> {
                    // Called only when a new container is needed.
                    override fun create(view: View) = DayViewContainer(view)

                    // Called every time we need to reuse a container.
                    override fun bind(container: DayViewContainer, data: CalendarDay) {
                        // TODO: Temp code for testing multiple colors
                        // for every lawaktsa event, set the background to a different color

                        var unavailableCount = 0


                        for(user in lakwatsaUsers){
                            var breakFlag = false
                            for (lakwatsa in lakwatsaForEachUser[user.username]!!) {
                                if (data.date.dayOfMonth == lakwatsa.date.split("/")[2].toInt() && data.date.monthValue == lakwatsa.date.split(
                                        "/"
                                    )[1].toInt() && data.date.year == lakwatsa.date.split("/")[0].toInt()
                                ) {
                                    unavailableCount++
                                    breakFlag = true
                                    break
                                }
                            }
                            if(breakFlag){
                                continue
                            }
                            for (unavailable in user.unavailableList) {
                                val blockDateFormatted = data.date.toString().replace("-", "/")
                                val startDateFormatted = unavailable.startDate
                                val endDateFormatted = unavailable.endDate

                                if (blockDateFormatted >= startDateFormatted && blockDateFormatted <= endDateFormatted) {
                                    unavailableCount++
                                    breakFlag = true
                                    break
                                }
                            }
                            if(breakFlag)
                                continue
                        }
                        Log.e("LakwatsaAvailabilityActivity", "Date: ${data.date}")
                        Log.e("LakwatsaAvailabilityActivity", "Unavailable Count: $unavailableCount")
                        Log.e("LakwatsaAvailabilityActivity", "Lakwatsa Users Size: ${lakwatsaUsers.size}")

                        var unavailabilityScore = unavailableCount.toFloat() / lakwatsaUsers.size.toFloat()
                        val color = getGradientColorBg(unavailabilityScore) // Your gradient function
                        val drawable = ContextCompat.getDrawable(this@LakwatsaAvailabilityActivity, R.drawable.date_round_dark_orange_10_no_outline)?.mutate()
                        drawable?.setTint(color)
                        container.constraintLayout.background = drawable
                        container.textView.setTextColor(getGradientColorText(unavailabilityScore)) // Adjust text color if needed
                        container.textView.text = data.date.dayOfMonth.toString()
                    }
                }
            val currentMonth = YearMonth.now()
            val startMonth = currentMonth.minusMonths(100) // Adjust as needed
            val endMonth = currentMonth.plusMonths(100) // Adjust as needed
            val daysOfWeek = daysOfWeek() // Available from the library
            binding.activityLakwatsaAvailabilityCalendarView.setup(startMonth, endMonth, daysOfWeek.first())
            binding.activityLakwatsaAvailabilityCalendarView.scrollToMonth(currentMonth)

            binding.activityLakwatsaAvailabilityCalendarView.monthHeaderBinder =
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
                                binding.activityLakwatsaAvailabilityCalendarView.smoothScrollToMonth(
                                    data.yearMonth.minusMonths(
                                        1
                                    )
                                )
                            }
                        container.titlesContainer.findViewById<ImageButton>(R.id.calendar_day_titles_container_ib_right)
                            .setOnClickListener {
                                binding.activityLakwatsaAvailabilityCalendarView.smoothScrollToMonth(
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
        }
        if(UserSession.hasUnreadNotifications()){
            binding.activityLakwatsaAvailabilityIbInbox.setImageResource(R.drawable.ic_inbox_unread)
        }
        else{
            binding.activityLakwatsaAvailabilityIbInbox.setImageResource(R.drawable.ic_inbox)
        }

        binding.activityLakwatsaAvailabilityProfileUser.setImageResource(UserSession.getUser().getDrawableProfilePicture())

    }
}