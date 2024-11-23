package com.mobdeve.s13.lim.pacheco.tan.tarana

import AdapterAlbumAlbum
import MarginItemDecoration
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
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
import kotlinx.coroutines.launch
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

    private fun showDeleteConfirmationDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.modal_delete_item, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val btnCancel = dialogView.findViewById<ImageButton>(R.id.modal_delete_item_btn_close)
        val btnConfirm = dialogView.findViewById<Button>(R.id.modal_delete_item_btn_delete)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            // TODO: DELETE ALBUM LOGIC
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityAlbumAlbumBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // NAVIGATION BUTTONS

        viewBinding.optionsIcon.setOnClickListener { view ->
            val contextWrapper = ContextThemeWrapper(this, R.style.CustomPopupMenu)
            val popupMenu = PopupMenu(contextWrapper, view)

            popupMenu.menu.add(0, 1, 0, "Delete album")
            popupMenu.menu.add(0, 2, 1, "Rename album")
            popupMenu.menu.add(0, 3, 2, "Change album color")

            popupMenu.show()

            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    1 -> {
                        showDeleteConfirmationDialog()
                        true
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
        }
    }
}