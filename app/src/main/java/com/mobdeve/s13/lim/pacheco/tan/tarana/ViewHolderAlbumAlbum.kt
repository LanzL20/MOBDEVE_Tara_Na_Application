import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.mobdeve.s13.lim.pacheco.tan.tarana.Image
import com.mobdeve.s13.lim.pacheco.tan.tarana.R


class ViewHolderAlbumAlbum(var view: View, viewType: Int) : RecyclerView.ViewHolder(view) {

    lateinit var imageView: ImageView
    lateinit var button: FrameLayout

    init {
        if (viewType == R.layout.item_layout_album_album_photo) {
            imageView = view.findViewById(R.id.sampleImg)
        } else {
            button = view.findViewById(R.id.add_img_btn)
        }
    }

    fun bindData(image: String) {
        // Reference to an image file in Cloud Storage
        Log.d("ViewHolderAlbumAlbum", "Image: $image")
        val storageReference = Firebase.storage.reference.child(image)

        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        Glide.with(view.context)
            .load(storageReference)
            .into(imageView)
    }

}