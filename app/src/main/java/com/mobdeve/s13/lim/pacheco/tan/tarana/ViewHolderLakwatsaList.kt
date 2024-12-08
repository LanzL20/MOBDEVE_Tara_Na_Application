package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.app.Activity
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityLakwatsaListBinding
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ItemLayoutLakwatsaListBinding
import kotlinx.coroutines.launch

class ViewHolderLakwatsaList(private val viewBinding: ItemLayoutLakwatsaListBinding): RecyclerView.ViewHolder(viewBinding.root) {
    fun getMonth(date: String): String{
        // Assuming format is YYYY/MM/DD (delimited by /)
        val month = date.split("/")[1]

        // Translate to month name abbreviated in all caps
        when (month) {
            "01" -> return "JAN"
            "02" -> return "FEB"
            "03" -> return "MAR"
            "04" -> return "APR"
            "05" -> return "MAY"
            "06" -> return "JUN"
            "07" -> return "JUL"
            "08" -> return "AUG"
            "09" -> return "SEP"
            "10" -> return "OCT"
            "11" -> return "NOV"
            "12" -> return "DEC"
        }
        return "ERR"
    }

    fun getDay(date: String): String{
        // Assuming format is YYYY/MM/DD
        val day = date.split("/")[2]
        return day
    }

    fun bindData(lakwatsa: Lakwatsa) {
        //TODO: Change the naming scheme of items in the layout file
        viewBinding.upcomingLakwatsaTitle1.text = lakwatsa.lakwatsaTitle
        viewBinding.upcomingDay1.text = getDay(lakwatsa.date)
        viewBinding.upcomingMonth1.text = getMonth(lakwatsa.date)
        viewBinding.upcomingLakwatsaFriend1Iv.visibility = View.GONE
        viewBinding.upcomingLakwatsaFriend2Iv.visibility = View.GONE
        viewBinding.upcomingLakwatsaFriend3Iv.visibility = View.GONE
        viewBinding.moreFriends.visibility = View.GONE
        (viewBinding.root.context as AppCompatActivity).lifecycleScope.launch {
            val lakwatsaUsers = DBHelper.getUsers(lakwatsa.lakwatsaUsers)
            if(lakwatsa.lakwatsaUsers.size == 1){
                val resourceId1 = viewBinding.root.context.getResources().getIdentifier("asset_profile" + lakwatsaUsers[0].profilePicture, "drawable", viewBinding.root.context.getPackageName());
                viewBinding.upcomingLakwatsaFriend1Iv.setImageResource(resourceId1)
                viewBinding.upcomingLakwatsaFriend1Iv.visibility = View.VISIBLE
                viewBinding.upcomingLakwatsaFriend2Iv.visibility = View.GONE
                viewBinding.upcomingLakwatsaFriend3Iv.visibility = View.GONE
                viewBinding.moreFriends.visibility = View.GONE
            } else if (lakwatsa.lakwatsaUsers.size == 2){
                val resourceId1 = viewBinding.root.context.getResources().getIdentifier("asset_profile" + lakwatsaUsers[0].profilePicture, "drawable", viewBinding.root.context.getPackageName());
                viewBinding.upcomingLakwatsaFriend1Iv.setImageResource(resourceId1)
                val resourceId2 = viewBinding.root.context.getResources().getIdentifier("asset_profile" + lakwatsaUsers[1].profilePicture, "drawable", viewBinding.root.context.getPackageName());
                viewBinding.upcomingLakwatsaFriend2Iv.setImageResource(resourceId2)
                viewBinding.upcomingLakwatsaFriend1Iv.visibility = View.VISIBLE
                viewBinding.upcomingLakwatsaFriend2Iv.visibility = View.VISIBLE
                viewBinding.upcomingLakwatsaFriend3Iv.visibility = View.GONE
                viewBinding.moreFriends.visibility = View.GONE
            } else if (lakwatsa.lakwatsaUsers.size == 3){
                val resourceId1 = viewBinding.root.context.getResources().getIdentifier("asset_profile" + lakwatsaUsers[0].profilePicture, "drawable", viewBinding.root.context.getPackageName());
                viewBinding.upcomingLakwatsaFriend1Iv.setImageResource(resourceId1)
                val resourceId2 = viewBinding.root.context.getResources().getIdentifier("asset_profile" + lakwatsaUsers[1].profilePicture, "drawable", viewBinding.root.context.getPackageName());
                viewBinding.upcomingLakwatsaFriend2Iv.setImageResource(resourceId2)
                val resourceId3 = viewBinding.root.context.getResources().getIdentifier("asset_profile" + lakwatsaUsers[2].profilePicture, "drawable", viewBinding.root.context.getPackageName());
                viewBinding.upcomingLakwatsaFriend3Iv.setImageResource(resourceId3)
                viewBinding.upcomingLakwatsaFriend1Iv.visibility = View.VISIBLE
                viewBinding.upcomingLakwatsaFriend2Iv.visibility = View.VISIBLE
                viewBinding.upcomingLakwatsaFriend3Iv.visibility = View.VISIBLE
                viewBinding.moreFriends.visibility = View.GONE
            } else {
                val resourceId1 = viewBinding.root.context.getResources().getIdentifier("asset_profile" + lakwatsaUsers[0].profilePicture, "drawable", viewBinding.root.context.getPackageName());
                viewBinding.upcomingLakwatsaFriend1Iv.setImageResource(resourceId1)
                val resourceId2 = viewBinding.root.context.getResources().getIdentifier("asset_profile" + lakwatsaUsers[1].profilePicture, "drawable", viewBinding.root.context.getPackageName());
                viewBinding.upcomingLakwatsaFriend2Iv.setImageResource(resourceId2)
                val resourceId3 = viewBinding.root.context.getResources().getIdentifier("asset_profile" + lakwatsaUsers[2].profilePicture, "drawable", viewBinding.root.context.getPackageName());
                viewBinding.upcomingLakwatsaFriend3Iv.setImageResource(resourceId3)
                viewBinding.moreFriendsTv.text = "+" + (lakwatsa.lakwatsaUsers.size - 3).toString()
                viewBinding.upcomingLakwatsaFriend1Iv.visibility = View.VISIBLE
                viewBinding.upcomingLakwatsaFriend2Iv.visibility = View.VISIBLE
                viewBinding.upcomingLakwatsaFriend3Iv.visibility = View.VISIBLE
                viewBinding.moreFriends.visibility = View.VISIBLE
            }
        }
    }
}