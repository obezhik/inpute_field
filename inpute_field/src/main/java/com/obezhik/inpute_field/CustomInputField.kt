package com.obezhik.inpute_field


import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import androidx.core.view.isNotEmpty
import androidx.core.widget.addTextChangedListener
import androidx.databinding.*
import com.google.android.material.textfield.TextInputLayout

 class CustomInputField
 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(
  context,
    attrs){

    private var textView: AutoCompleteTextView
    private var inputLayout: TextInputLayout

    private var textWatcher: TextWatcher? = null
    private var textFocusable: View.OnFocusChangeListener? = null

    private var mHint = ""
    private var mHelper = ""
    private var mText = ""
    private var mError = ""
    private var mStartDrawableRes: Int = -1
    private var mEndDrawableRes: Int = -1
    private var mEndDrawableColor: Int = -1
    private var mStartDrawableColor: Int = -1
    private var mEnablePasswordToggle = false
    private var isDropDawnList = false

    private var mPrefixText = ""
    private var mPrefixTextColor = -1

    private var mSuffixText = ""
    private var mSuffixTextColor = -1

    private var mClearDrawableRes: Int = android.R.drawable.ic_menu_close_clear_cancel

    companion object {
        @BindingAdapter("textAttrChanged")
        @JvmStatic
        fun setListener(v: CustomInputField, listener: InverseBindingListener){
            val textView: AutoCompleteTextView = v.getTextView()
            textView.addTextChangedListener {
                    listener.onChange()
            }
        }

        @BindingAdapter("text")
        @JvmStatic
        fun setTextValue(v: CustomInputField, value: String){
            val textView: AutoCompleteTextView  = v.getTextView()
            val currentValue = textView.text.toString()
            if (currentValue != value){
               textView.setText(value, false)
            }
        }

        @InverseBindingAdapter(attribute = "text")
        @JvmStatic
        fun gettextValue(v: CustomInputField): String {
            val textView: AutoCompleteTextView  = v.getTextView()
            return textView.text.toString()
        }
    }

    /* initialization */
    init {
        LayoutInflater.from(context).inflate(R.layout.custom_inpute_field_view, this, true)

        inputLayout = findViewById(R.id.customComponentsInputLayout)
        textView = findViewById(R.id.customComponentsText)
        orientation = HORIZONTAL

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomInputField,
            0, 0).apply {

            try {

               getString(R.styleable.CustomInputField_text)?.let {
                   mText = it
               }

               getString(R.styleable.CustomInputField_hint)?.let {
                    mHint = it
                }

               getString(R.styleable.CustomInputField_helperText)?.takeIf { isNotEmpty() }?.let {
                   mHelper = it
               }

               getString(R.styleable.CustomInputField_error)?.let {
                   mError = it
               }

               getString(R.styleable.CustomInputField_prefixText)?.let {
                   mPrefixText = it
               }

               getString(R.styleable.CustomInputField_suffixText)?.let {
                   mSuffixText = it
               }

                getColor(R.styleable.CustomInputField_prefixTextColor, -1).let {
                    mPrefixTextColor = it
                }

                getColor(R.styleable.CustomInputField_suffixTextColor, -1).let {
                    mSuffixTextColor = it
                }

               getBoolean(R.styleable.CustomInputField_passwordToggleEnable, false).let {
                   mEnablePasswordToggle = it
               }

               getResourceId(R.styleable.CustomInputField_startIconDrawable, -1).let {
                    mStartDrawableRes = it
                }

               getResourceId(R.styleable.CustomInputField_endIconDrawable, -1).let {
                    mEndDrawableRes = it
                }

               getColor(R.styleable.CustomInputField_endIconTint, -1).let {
                    mEndDrawableColor = it
               }

                getColor(R.styleable.CustomInputField_startIconTint, -1).let {
                    mStartDrawableColor = it
               }

               getResourceId(R.styleable.CustomInputField_clearIconDrawable, -1).takeIf { it != -1 }?.let {
                    mClearDrawableRes = it
                }

               hasValue(R.styleable.CustomInputField_entity).let {
                    isDropDawnList = it
                }

            } finally {
                recycle()
            }
        }
        init()
    }

    private fun init(){
        initTextView()
        initInputLayout()
    }

    private fun initInputLayout() = inputLayout.apply {

            if (mEnablePasswordToggle){
                endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
            }

            if (isDropDawnList) {

                    endIconMode = TextInputLayout.END_ICON_DROPDOWN_MENU

                } else if (!mEnablePasswordToggle && mEndDrawableRes == -1) {
                    endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                    setEndIconDrawable(mClearDrawableRes)
                }

          //  isExpandedHintEnabled = false

            hint = mHint

            mHelper.takeIf { isNotEmpty() }.let {
                helperText = it
            }

            mError.takeIf { isNotEmpty() }.let {
                error = it
                isErrorEnabled = true
            }

            mSuffixText.takeIf { isNotEmpty() }.also {
                suffixText = it
            }

            mSuffixTextColor.takeIf { it != -1 }?.let {
                setSuffixTextColor(ColorStateList.valueOf(it))
            }

            mPrefixText.takeIf { isNotEmpty() }.also {
                prefixText = it
            }

            mPrefixTextColor.takeIf { it != -1 }?.let {
                setPrefixTextColor(ColorStateList.valueOf(it))
            }

            mStartDrawableRes.takeIf { it != -1 }?.let {
                setStartIconDrawable(it)
            }

            mEndDrawableRes.takeIf { it != -1 }?.let {
                setEndIconDrawable(it)
            }

            mEndDrawableColor.takeIf { it != -1 }?.let {
                setEndIconTintList(ColorStateList.valueOf(it))
            }

            mStartDrawableColor.takeIf { it != -1 }?.let {
                setStartIconTintList(ColorStateList.valueOf(it))
            }

        }

    private fun initTextView() = textView.apply {

        setText(mText)

        inputType = if (mEnablePasswordToggle){
                InputType.TYPE_TEXT_VARIATION_PASSWORD
        } else if (isDropDawnList) {
                InputType.TYPE_NULL
        } else {
                InputType.TYPE_CLASS_TEXT
                InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE
        }

    }

    private fun initTextWatcher(view: AutoCompleteTextView?){

        if (view == null){
            textView.addTextChangedListener(null)
            textWatcher = null
        }

        view?.addTextChangedListener{
            if (view.adapter != null){

                val isEmpty = it.isNullOrEmpty()//toString() == ""

                if (isEmpty){

                    inputLayout.setEndIconOnClickListener(null)

                    inputLayout.endIconMode = TextInputLayout.END_ICON_DROPDOWN_MENU

                } else {

                    inputLayout.setEndIconOnClickListener{ view.setText("") }

                    inputLayout.setEndIconDrawable(mClearDrawableRes)

                }
            }
        }

    }

    private fun initTextFocusListener(view: AutoCompleteTextView?){

        if (view == null){
            textView.onFocusChangeListener = null
            textFocusable = null
         //   textView.addTextChangedListener(null)
            return
        }

        if (textFocusable != null){
            return
        }

        textFocusable = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                if (inputLayout.isErrorEnabled){
                    setError("")
                }
            }
        }

        view.addTextChangedListener {
            if (it != null) {
                if (it.isNotEmpty()){
                    setError("")
                }
            }
        }

        view.onFocusChangeListener = textFocusable

    }

    /* setters */
    fun setEntity(entity: List<String>){
        if (mEnablePasswordToggle){
            return
        }
            val adapter =
                ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, entity)
            textView.setAdapter(adapter)

        if (textView.text.isEmpty()) {
            inputLayout.endIconMode = TextInputLayout.END_ICON_DROPDOWN_MENU
            textView.inputType = InputType.TYPE_NULL
        }

        initTextWatcher(textView)
    }

    fun setHint(hint: String){
        inputLayout.hint = hint
    }

    fun setHelperText(helper: String){
        inputLayout.apply {
            helperText = helper
        }
    }

    fun setError(error: String) {
        if (error.isEmpty()){
            inputLayout.isErrorEnabled = false
            mError = ""
            inputLayout.error = mError
            initTextFocusListener(null)
        } else {
            mError = error
            inputLayout.error = mError
            inputLayout.isErrorEnabled = true
            initTextFocusListener(textView)
        }
    }

    fun setOnStartIconOnClick(onClick: OnClickListener) {
        inputLayout.setStartIconOnClickListener(onClick)
    }

    fun setOnEndIconOnClick(onClick: OnClickListener) {
        inputLayout.setEndIconOnClickListener(onClick)
    }

    fun getTextView(): AutoCompleteTextView = textView

}