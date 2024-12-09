package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ItemLayoutFriendToAddBinding


class ViewHolderFriendsAdd(private var viewBinding: ItemLayoutFriendToAddBinding) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bindData(user: User) {
        viewBinding.itemLayoutFriendToAddName.text = user.name
        viewBinding.itemLayoutFriendToAddUsername.text = "@" + user.username
        val resourceId = viewBinding.root.context.getResources().getIdentifier("asset_profile" + user.profilePicture, "drawable", viewBinding.root.context.getPackageName());
        viewBinding.itemLayoutFriendToAddIv.setImageResource(resourceId)
    }
}