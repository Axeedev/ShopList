package domain

import androidx.lifecycle.LiveData

class GetShopsListUseCase (
    private val shopListRepository: ShopListRepository
){
    fun getShopList(): LiveData<List<ShopItem>> = shopListRepository.getShopList()

}