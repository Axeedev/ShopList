package domain

class GetItemByIdUseCase(
    private val shopListRepository: ShopListRepository
) {
    fun getItem(id : Int) : ShopItem
    {
        return shopListRepository.getItem(id)
    }
}