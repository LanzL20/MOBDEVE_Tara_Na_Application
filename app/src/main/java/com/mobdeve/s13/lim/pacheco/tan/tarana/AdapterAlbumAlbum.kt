import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.Image


class AdapterAlbumAlbum(private var data: ArrayList<String>, private var imagesActivityResultLauncher: ActivityResultLauncher<Intent>): RecyclerView.Adapter<ViewHolderAlbumAlbum>() {

    override fun getItemViewType(position: Int): Int {
        return if ((position == data.size)) com.mobdeve.s13.lim.pacheco.tan.tarana.R.layout.item_layout_album_album_button else com.mobdeve.s13.lim.pacheco.tan.tarana.R.layout.item_layout_album_album_photo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAlbumAlbum {
        val itemView: View

        if (viewType == com.mobdeve.s13.lim.pacheco.tan.tarana.R.layout.item_layout_album_album_photo) {
            itemView = LayoutInflater.from(parent.context).inflate(com.mobdeve.s13.lim.pacheco.tan.tarana.R.layout.item_layout_album_album_photo, parent, false)
        } else {
            itemView = LayoutInflater.from(parent.context).inflate(com.mobdeve.s13.lim.pacheco.tan.tarana.R.layout.item_layout_album_album_button, parent, false)
        }

        return ViewHolderAlbumAlbum(itemView, viewType)
    }

    override fun getItemCount(): Int {
        return data.size + 1
    }

    fun addImage(image: String) {
        data.add(image)
        notifyItemInserted(data.size)
    }

    override fun onBindViewHolder(holder: ViewHolderAlbumAlbum, position: Int) {
        if (position == data.size) {
            holder.button.setOnClickListener( {
                val intent = Intent()
                intent.setType("image/*")
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                imagesActivityResultLauncher.launch(intent)
            })
        } else {
            holder.bindData(data[position])
        }

    }
}