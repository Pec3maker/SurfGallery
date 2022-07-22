package com.example.surfgallery.ui.login

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.surfgallery.R
import com.example.surfgallery.databinding.EnterButtonBinding

class EnterButtonView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: EnterButtonBinding
    private lateinit var text: String

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context = context,
        attrs = attrs,
        defStyleAttr = defStyleAttr,
        defStyleRes = 0
    )

    constructor(context: Context, attrs: AttributeSet?) : this(
        context = context,
        attrs = attrs,
        defStyleAttr = 0
    )

    constructor(context: Context) : this(context = context, attrs = null)

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.enter_button, this, true)
        binding = EnterButtonBinding.bind(this)
        initAttrs(attrs = attrs, defStyleAttr = defStyleAttr, defStyleRes = defStyleRes)
    }

    fun setIsLoading(isLoading: Boolean) {
        binding.enterButton.text =
            if (isLoading) resources.getString(R.string.empty_string) else text
        binding.progressCircular.visibility = if (isLoading) VISIBLE else INVISIBLE
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.EnterButtonView,
            defStyleAttr,
            defStyleRes
        )

        with(binding) {
            val isLoading = typedArray.getBoolean(R.styleable.EnterButtonView_isLoading, false)
            text = typedArray.getString(R.styleable.EnterButtonView_buttonText) ?: ""

            enterButton.text = if (isLoading) resources.getString(R.string.empty_string) else text
            progressCircular.visibility = if (isLoading) VISIBLE else INVISIBLE
        }

        typedArray.recycle()
    }
}