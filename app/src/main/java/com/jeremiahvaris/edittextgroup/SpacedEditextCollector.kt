package com.jeremiahvaris.edittextgroup

import android.widget.EditText

interface SpacedEditextCollector {

    fun setCursorAtEnd()
    fun setCursorIfFieldNotEmpty(index: Int = -1)

    fun onSingleEditTextOverflowed(
        viewId: Int,
        sequence: CharSequence,
        isRecursiveCall: Boolean = false
    )

    fun setCursorOnNext(editText: EditText)
    fun onDeleteText(editText: EditText)
    fun afterTextChanged()
}