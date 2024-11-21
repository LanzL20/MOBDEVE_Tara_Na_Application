package com.mobdeve.s13.lim.pacheco.tan.tarana

import AdapterAlbumAlbum
import MarginItemDecoration
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
                    val fileRef = FirebaseStorage.getInstance().getReference().child("gallery").child("insertLakwatsaIdHere").child(System.currentTimeMillis().toString() + "_" + getFileName(file));
                    fileRef.putFile(file).addOnSuccessListener {
                        Log.d("MainActivity", "Successfully uploaded image: ${it.metadata?.path}")
                        lifecycleScope.launch {
                            val lakwatsaId = intent.getStringExtra(Lakwatsa.ID_KEY)
                            Log.d("AlbumAlbumActivity", "Lakwatsa ID: $lakwatsaId")
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
        }
    }
}