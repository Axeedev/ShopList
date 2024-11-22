package presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.R
import domain.ShopItem

class ShopListAdapter : ListAdapter<ShopItem, ShopListAdapter.ShopItemViewHolder>(ShopItemDiffCallback()) {

    class ShopItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById<TextView>(R.id.name)
        val count: TextView = view.findViewById<TextView>(R.id.count)
    }

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.name.text = item.name
        holder.count.text = item.count.toString()
        holder.itemView.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(item)
            true
        }
        holder.itemView.setOnClickListener {
            onShopItemClickListener?.invoke(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        val viewType = if (item.enabled) {
            SHOP_ITEM
        } else SHOP_ITEM_D
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when (viewType) {
            SHOP_ITEM -> R.layout.shop_item
            SHOP_ITEM_D -> R.layout.shop_item_disabled
            else -> throw RuntimeException()
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)
        return ShopItemViewHolder(view)
    }

    companion object {
        const val SHOP_ITEM = 100
        const val SHOP_ITEM_D = 101
        const val MAX_POOL_SIZE = 15
    }
}