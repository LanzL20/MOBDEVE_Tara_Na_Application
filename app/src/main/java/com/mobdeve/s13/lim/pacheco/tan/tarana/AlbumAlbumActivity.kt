package com.mobdeve.s13.lim.pacheco.tan.tarana

import AdapterAlbumAlbum
import MarginItemDecoration
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.storage.FirebaseStorage
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityAlbumAlbumBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.min


class AlbumAlbumActivity: AppCompatActivity() {

    lateinit var viewBinding: ActivityAlbumAlbumBinding

    fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(with(cursor) { getColumnIndex(OpenableColumns.DISPLAY_NAME) })
                }
            } finally {
                cursor!!.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result!!.substring(cut + 1)
            }
        }
        return result
    }

    private val imagesActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            if (null != result.data) {
                val files: ArrayList<Uri> = ArrayList()
                if (null != result.data!!.clipData) {
                    val count = result.data!!.clipData!!.itemCount
                    for (i in 0 until min(count.toDouble(), 10.0).toInt()) {
                        val uri = result.data!!.clipData!!.getItemAt(i).uri
                        files.add(uri)
                    }
                } else {
                    val uri = result.data!!.data
                    if (uri != null) {
                        files.add(uri)
                    }
                }
                for(file in files){
                    val lakwatsaId = intent.getStringExtra(Lakwatsa.ID_KEY)
                    val fileRef = FirebaseStorage.getInstance().getReference().child("album").child(lakwatsaId.toString()).child(System.currentTimeMillis().toString() + "_" + getFileName(file));
                    fileRef.putFile(file).addOnSuccessListener {
                        Log.d("MainActivity", "Successfully uploaded image: ${it.metadata?.path}")
                        lifecycleScope.launch {
                            val lakwatsa = DBHelper.getLakwatsa(lakwatsaId!!)
                            lakwatsa.album.add(it.metadata?.path.toString())
                            DBHelper.updateLakwatsa(lakwatsa)
                            (viewBinding.albumAlbumRv.adapter as AdapterAlbumAlbum).addImage(it.metadata?.path.toString())
                        }
                    }.addOnFailureListener {
                        Log.d("MainActivity", "Failed to upload image: $it")
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityAlbumAlbumBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // NAVIGATION BUTTONS

        viewBinding.optionsIcon.setOnClickListener { view ->
            val contextWrapper = ContextThemeWrapper(this, R.style.CustomPopupMenu)
            val popupMenu = PopupMenu(contextWrapper, view)

            popupMenu.menu.add(0, 1, 0, "Download Album")

            popupMenu.show()

            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    1 -> {
                        if(viewBinding.albumAlbumRv.adapter?.itemCount == 1){
                            Toast.makeText(this, "Album is empty!", Toast.LENGTH_SHORT).show()
                            false
                        }
                        else {
                            showDeleteConfirmationDialog()
                            true
                        }
                    }
                    else -> false
                }
            }
        }

        viewBinding.inboxIcon.setOnClickListener {
            val intent = Intent(this, ProfileInboxActivity::class.java)
            startActivity(intent)
        }

        viewBinding.settingsIcon.setOnClickListener {
            val intent = Intent(this, ProfileSettingActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            val lakwatsaId = intent.getStringExtra(Lakwatsa.ID_KEY)
            Log.d("AlbumAlbumActivity", "Lakwatsa ID: $lakwatsaId")
            val lakwatsa = DBHelper.getLakwatsa(lakwatsaId!!)
            viewBinding.albumAlbumRv.adapter = AdapterAlbumAlbum(lakwatsa.album, imagesActivityResultLauncher)
            viewBinding.albumAlbumRv.layoutManager = GridLayoutManager(viewBinding.albumAlbumRv.context, 4)
            viewBinding.albumAlbumRv.addItemDecoration(MarginItemDecoration(10))
            viewBinding.activityAlbumAlbumTitle.text = lakwatsa.lakwatsaTitle
            viewBinding.activityAlbumAlbumDate.text = lakwatsa.date.toString()
            viewBinding.activityAlbumAlbumFriend1Iv.visibility = View.GONE
            viewBinding.activityAlbumAlbumFriend2Iv.visibility = View.GONE
            viewBinding.activityAlbumAlbumFriend3Iv.visibility = View.GONE
            viewBinding.moreFriends.visibility = View.GONE
            if(lakwatsa.lakwatsaUsers.size == 1){
                viewBinding.activityAlbumAlbumFriend1Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[0]).profilePicture, "drawable", packageName))
                viewBinding.activityAlbumAlbumFriend1Iv.visibility = View.VISIBLE
            }
            else if (lakwatsa.lakwatsaUsers.size == 2){
                viewBinding.activityAlbumAlbumFriend1Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[0]).profilePicture, "drawable", packageName))
                viewBinding.activityAlbumAlbumFriend2Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[1]).profilePicture, "drawable", packageName))
                viewBinding.activityAlbumAlbumFriend1Iv.visibility = View.VISIBLE
                viewBinding.activityAlbumAlbumFriend2Iv.visibility = View.VISIBLE
            }
            else if (lakwatsa.lakwatsaUsers.size == 3){
                viewBinding.activityAlbumAlbumFriend1Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[0]).profilePicture, "drawable", packageName))
                viewBinding.activityAlbumAlbumFriend2Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[1]).profilePicture, "drawable", packageName))
                viewBinding.activityAlbumAlbumFriend3Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[2]).profilePicture, "drawable", packageName))
                viewBinding.activityAlbumAlbumFriend1Iv.visibility = View.VISIBLE
                viewBinding.activityAlbumAlbumFriend2Iv.visibility = View.VISIBLE
                viewBinding.activityAlbumAlbumFriend3Iv.visibility = View.VISIBLE
            }
            else {
                viewBinding.activityAlbumAlbumFriend1Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[0]).profilePicture, "drawable", packageName))
                viewBinding.activityAlbumAlbumFriend1Iv.visibility = View.VISIBLE
                viewBinding.activityAlbumAlbumFriend2Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[1]).profilePicture, "drawable", packageName))
                viewBinding.activityAlbumAlbumFriend2Iv.visibility = View.VISIBLE
                viewBinding.activityAlbumAlbumFriend3Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[2]).profilePicture, "drawable", packageName))
                viewBinding.activityAlbumAlbumFriend3Iv.visibility = View.VISIBLE
                viewBinding.moreFriendsTv.text = "+" + (lakwatsa.lakwatsaUsers.size - 3).toString()
                viewBinding.moreFriends.visibility = View.VISIBLE
            }
        }
    }

    suspend fun downloadAllPhotos(imageLinks: ArrayList<String>, albumName: String) {
        val storage = FirebaseStorage.getInstance()
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        // Create the album directory if it doesn't exist
        val albumDir = File(storageDir, albumName)
        if (!albumDir.exists()) {
            albumDir.mkdir()
        }

        // Iterate through all image links
        for (imageLink in imageLinks) {
            try {
                // Create the StorageReference
                val storageReference = storage.reference.child(imageLink)

                // Get the file name from the link
                val fileName = storageReference.name

                // Create a temporary file to download the image
                val tempFile = File(albumDir, fileName)

                // Download the file to local storage
                storageReference.getFile(tempFile).await()

                // Save to MediaStore (for gallery visibility)
                saveToGallery(tempFile, fileName, albumName)

            } catch (e: Exception) {
                e.printStackTrace()
                // Handle errors (e.g., show a toast or log)
            }
        }
    }

    // Save the file to MediaStore (Android Gallery)
    private suspend fun saveToGallery(file: File, fileName: String, albumName: String) {
        withContext(Dispatchers.IO) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + albumName) // Replace with your folder
            }

            val resolver = contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            uri?.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    file.inputStream().copyTo(outputStream)
                }
            }
        }
    }

    private fun showDeleteConfirmationDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.modal_delete_item, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.setOnShowListener {
            val displayMetrics = this.resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val margin = (24 * displayMetrics.density).toInt()

            dialog.window?.setLayout(
                screenWidth - 2 * margin,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        val btnCancel = dialogView.findViewById<ImageButton>(R.id.modal_delete_item_btn_close)
        val btnConfirm = dialogView.findViewById<Button>(R.id.modal_delete_item_btn_delete)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            val lakwatsaId = intent.getStringExtra(Lakwatsa.ID_KEY)
            lifecycleScope.launch {
                val lakwatsa = DBHelper.getLakwatsa(lakwatsaId!!)
                downloadAllPhotos(lakwatsa.album, lakwatsa.lakwatsaTitle)
                Toast.makeText(this@AlbumAlbumActivity, "Download done!", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        dialog.show()
    }
}