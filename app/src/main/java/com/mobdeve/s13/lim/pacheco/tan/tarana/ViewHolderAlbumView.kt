import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.Lakwatsa
import com.mobdeve.s13.lim.pacheco.tan.tarana.Notification
import com.mobdeve.s13.lim.pacheco.tan.tarana.R
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ItemAlbumViewBinding
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ItemLayoutNotificationBinding

class ViewHolderAlbumView(private var viewBinding: ItemAlbumViewBinding) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bindData(lakwatsa: Lakwatsa, position: Int) {
        viewBinding.albumText.text = lakwatsa.lakwatsaTitle + "\n" + lakwatsa.album.size + " photos"
    }
}