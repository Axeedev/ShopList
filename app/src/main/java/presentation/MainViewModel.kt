package presentation

import androidx.lifecycle.ViewModel
import data.ShopListRepositoryImpl
import domain.DeleteShopItemUseCase
import domain.EditShopItemUseCase
import domain.GetShopsListUseCase
import domain.ShopItem

class MainViewModel : ViewModel() {
    private val rep = ShopListRepositoryImpl

    private val change = EditShopItemUseCase(rep)
    private val del = DeleteShopItemUseCase(rep)
    private val getShopsListUseCase = GetShopsListUseCase(rep)

    val shopList = getShopsListUseCase.getShopList()


    fun deleteShopItem(shopItem: ShopItem)
    {
        del.deleteShopItem(shopItem)
    }
    fun editShopItem(shopItem: ShopItem)
    {
        val new = shopItem.copy(enabled = !shopItem.enabled)
        change.editItem(new)
    }


}
