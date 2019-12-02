package com.packetalk.utility

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

class MyTextView : TextView {
    constructor(context: Context) : super(context) {
        setFont()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setFont()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setFont()
    }

    private fun setFont() {
        val font = Typeface.createFromAsset(context.assets, "fonts/roboto_regular.ttf")
        setTypeface(font, Typeface.NORMAL)
    }
}
