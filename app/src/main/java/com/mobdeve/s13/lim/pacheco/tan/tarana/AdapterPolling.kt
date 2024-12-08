package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.input.key.Key.Companion.U
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ItemPollBinding
import kotlinx.coroutines.launch

class AdapterPolling(private var data: HashMap<String, ArrayList<String>>, private val lakwatsaId: String): RecyclerView.Adapter<AdapterPolling.ViewHolderPolling>() {

    private val pollItems = ArrayList<PollItem>()

    init {
        for (entry in data.entries){
            val key = entry.key
            val value = entry.value
            pollItems.add(PollItem(key, value))
        }
        pollItems.sortBy { it.datetime }
    }

    class ViewHolderPolling(private val viewBinding: ItemPollBinding): RecyclerView.ViewHolder(viewBinding.root){
        fun bindData(item: PollItem){
            (viewBinding.root.context as AppCompatActivity).lifecycleScope.launch {
                viewBinding.pollCheckbox.setText(item.datetime)
                if(item.voters.contains(UserSession.getUser().username)){
                    viewBinding.pollCheckbox.isChecked = true
                }
                val voters = DBHelper.getUsers(item.voters)
                viewBinding.pollFriend1.visibility = ViewGroup.GONE
                viewBinding.pollFriend2.visibility = ViewGroup.GONE
                viewBinding.moreFriends.visibility = ViewGroup.GONE
                if(voters.size == 1){
                    viewBinding.pollFriend1.setImageResource(voters[0].getDrawableProfilePicture())
                    viewBinding.pollFriend1.visibility = ViewGroup.VISIBLE
                } else if (voters.size == 2){
                    viewBinding.pollFriend1.setImageResource(voters[0].getDrawableProfilePicture())
                    viewBinding.pollFriend2.setImageResource(voters[1].getDrawableProfilePicture())
                    viewBinding.pollFriend1.visibility = ViewGroup.VISIBLE
                    viewBinding.pollFriend2.visibility = ViewGroup.VISIBLE
                } else if (voters.size > 2) {
                    viewBinding.pollFriend1.setImageResource(voters[0].getDrawableProfilePicture())
                    viewBinding.pollFriend1.visibility = ViewGroup.VISIBLE
                    viewBinding.pollFriend2.setImageResource(voters[1].getDrawableProfilePicture())
                    viewBinding.pollFriend2.visibility = ViewGroup.VISIBLE
                    viewBinding.moreFriends.visibility = ViewGroup.VISIBLE
                    viewBinding.moreFriendsTv.text = "+${voters.size - 2}"
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPolling {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemPollBinding.inflate(inflater, parent, false)
        val viewHolder = ViewHolderPolling(viewBinding)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolderPolling, position: Int) {
        val pollItem = pollItems[position]
        holder.bindData(pollItem)
        val checkBox = holder.itemView.findViewById<CheckBox>(R.id.poll_checkbox)

        // Set the initial state
        checkBox.setOnCheckedChangeListener(null) // Remove existing listener
        checkBox.isChecked = pollItem.voters.contains(UserSession.getUser().username) // Set current state

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            (holder.itemView.context as AppCompatActivity).lifecycleScope.launch {
                val lakwatsa = DBHelper.getLakwatsa(lakwatsaId)
                if (isChecked) {
                    lakwatsa.addPolling(pollItem.datetime, UserSession.getUser().username)
                } else {
                    lakwatsa.removePolling(pollItem.datetime, UserSession.getUser().username)
                }
                DBHelper.updateLakwatsa(lakwatsa)
            }
        }

        holder.itemView.setOnClickListener {
            // Toggle the checkbox (this triggers animation)
            checkBox.performClick()
        }
    }

    fun setData(data: HashMap<String, ArrayList<String>>){
        this.data = data
        pollItems.clear()
        for (entry in data.entries){
            val key = entry.key
            val value = entry.value
            pollItems.add(PollItem(key, value))
        }
        pollItems.sortBy { it.datetime }
        notifyDataSetChanged()
        notifyItemRangeChanged(0, pollItems.size)
    }


}

data class PollItem(
    val datetime: String,
    val voters: ArrayList<String>
)
