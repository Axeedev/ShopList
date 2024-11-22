package domain

import androidx.lifecycle.LiveData

interface ShopListRepository{
    fun deleteShopItem(shopItem: ShopItem)
    fun editItem(shopItem: ShopItem)
    fun addShopItem(shopItem: ShopItem)
    fun getShopList(): LiveData<List<ShopItem>>
    fun getItem(id : Int) : ShopItem
}