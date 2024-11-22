package presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.shoplist.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import domain.ShopItem
import kotlin.math.E

class ShopItemActivity : AppCompatActivity() {
    private lateinit var tiEditEdit: TextInputLayout
    private lateinit var tiInputCount: TextInputLayout
    private lateinit var tiInput: TextInputEditText
    private lateinit var viewModel: ShopItemViewModel
    private lateinit var tiCount: TextInputEditText
    private lateinit var button: Button

    private var id = ShopItem.UNDEFINED_ID
    private var screenMode = MODE_UNKNOWN
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        parseIntent()
        enableEdgeToEdge()
        setContentView(R.layout.activity_shop_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
        tiInput.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        tiCount.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetCount()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        when(screenMode){
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
        viewModel.liveDataErrorCount.observe(this){
            val msg = if (it){
                getString(R.string.Error_input_count)
            }
            else{
                null
            }
            tiInputCount.error = msg
        }
        viewModel.liveDataError.observe(this){
            val msg = if (it){
                getString(R.string.Error_input)
            }
            else{
                null
            }
            tiEditEdit.error = msg
        }
        viewModel.shouldCloseScreen.observe(this){
            finish()
        }
    }
    private fun launchEditMode(){
        viewModel.getShopItem(id)
        viewModel.shopItem.observe(this){
            tiInput.setText(it.name)
            tiCount.setText(it.count.toString())
        }
        button.setOnClickListener{
            viewModel.editShopItem(tiInput.text?.toString(), tiCount.text?.toString())
        }

    }
    private fun launchAddMode(){
        button.setOnClickListener{
            viewModel.addShopItem(tiInput.text.toString(), tiCount.text?.toString())
        }

    }
    companion object{

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""



        fun newIntentAdd(context: Context) : Intent{
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }
        fun newIntentEdit(context: Context, id : Int) : Intent{
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, id).putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            return intent
        }


    }
    private fun initViews(){
        tiEditEdit = findViewById(R.id.textInputName)
        tiInputCount = findViewById(R.id.textinput_count)
        tiCount = findViewById(R.id.count)
        tiInput = findViewById(R.id.name)
        button = findViewById(R.id.button)
    }
    private fun parseIntent(){
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)){
            throw RuntimeException("Screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("mode is unknown")
        }
        screenMode = mode
        if (mode == MODE_EDIT){
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)){
                throw RuntimeException("Item id is absent")
            }
            id = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, -1)
        }
    }
}