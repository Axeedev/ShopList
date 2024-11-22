package data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import domain.ShopItem
import domain.ShopListRepository
import kotlin.random.Random

object ShopListRepositoryImpl : ShopListRepository {
    private val items = sortedSetOf<ShopItem>({o1, o2-> o1.id.compareTo(o2.id)})
    private var autoId : Int = 0
    private val shopList = MutableLiveData<List<ShopItem>>()

    init {
        for (i in 0 until  1000){
            val item = ShopItem("name$i",i, Random.nextBoolean())
            addShopItem(item)
        }
    }
    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoId++
        }
        items.add(shopItem)
        update()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        items.remove(shopItem)
        update()
    }

    override fun editItem(shopItem: ShopItem) {
        val old = getItem(shopItem.id)
        deleteShopItem(old)
        addShopItem(shopItem)
    }

    override fun getItem(id: Int): ShopItem {
        return items.find { it.id == id } ?: throw Exception("wrong")
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopList
    }
    private fun update(){
        shopList.value = items.toList()
    }
}