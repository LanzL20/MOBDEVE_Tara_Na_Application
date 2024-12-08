import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.AlbumAlbumActivity
import com.mobdeve.s13.lim.pacheco.tan.tarana.Lakwatsa
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ItemAlbumViewBinding

class AdapterAlbumView(private var data: ArrayList<Lakwatsa>): RecyclerView.Adapter<ViewHolderAlbumView>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAlbumView {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemAlbumViewBinding.inflate(inflater, parent, false)
        val viewHolder = ViewHolderAlbumView(viewBinding)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolderAlbumView, position: Int) {
        holder.bindData(data[position], position)
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, AlbumAlbumActivity::class.java)
            intent.putExtra(Lakwatsa.ID_KEY, data[position].lakwatsaId)
            holder.itemView.context.startActivity(intent)
        }
    }

    fun setData(data: ArrayList<Lakwatsa>){
        this.data = data
        notifyDataSetChanged()
    }
}