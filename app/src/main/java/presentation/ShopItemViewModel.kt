package presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import data.ShopListRepositoryImpl
import domain.AddShopItemUseCase
import domain.EditShopItemUseCase
import domain.GetItemByIdUseCase
import domain.ShopItem

class ShopItemViewModel : ViewModel() {
    val rep = ShopListRepositoryImpl

    private val add = AddShopItemUseCase(rep)
    private val get = GetItemByIdUseCase(rep)
    private val edit = EditShopItemUseCase(rep)

    private val _liveDataErrorName = MutableLiveData<Boolean>()
    val liveDataError: LiveData<Boolean>
        get() = _liveDataErrorName
    private val _liveDataErrorCount = MutableLiveData<Boolean>()
    val liveDataErrorCount: LiveData<Boolean>
        get() = _liveDataErrorCount

    fun addShopItem(nameInput: String, countInput: String?) {
        val name = parseName(nameInput)
        val count = parseCount(countInput)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            add.addShopItem(ShopItem(name, count, true))
            shouldCloseScreen()
        }
    }

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem
    fun getShopItem(shopItemId: Int) {
        val item = get.getItem(shopItemId)
        _shopItem.value = item
    }
    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen : LiveData<Unit>
        get() = _shouldCloseScreen
    private fun shouldCloseScreen() {
        _shouldCloseScreen.value = Unit
    }
    fun editShopItem(nameInput: String?, countInput: String?) {
        val name = parseName(nameInput)
        val count = parseCount(countInput)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            _shopItem.value?.let {
                val item = it.copy(name = name, count = count)
                edit.editItem(item)
                shouldCloseScreen()
            }
        }
    }

    private fun parseName(name: String?): String = try {
        name?.trim() ?: ""
    } catch (e: Exception) {
        ""
    }

    private fun parseCount(count: String?): Int = try {
        count?.trim()?.toInt() ?: 0
    } catch (e: Exception) {
        0
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var res = true
        if (name.isBlank()) {
            _liveDataErrorName.value = true
            res = false
        }
        if (count <= 0) {
            _liveDataErrorCount.value = true
            res = false
        }
        return res
    }

    fun resetName() {
        _liveDataErrorName.value = false
    }

    fun resetCount() {
        _liveDataErrorCount.value = false
    }
}