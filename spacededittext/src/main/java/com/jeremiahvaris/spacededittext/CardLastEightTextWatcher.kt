package com.jeremiahvaris.spacededittext

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class CardLastEightTextWatcher(
    private val spacedEditextCollector: SpacedEditextCollector,
    private val editText: EditText
) : TextWatcher {
    var formerText = ""
    override fun afterTextChanged(p0: Editable?) {
        spacedEditextCollector.afterTextChanged()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        formerText = editText.text.toString()

    }

    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
        charSequence?.let {
            if (it.length > 1 || (start > 0 && it.isNotEmpty()))
                spacedEditextCollector.onSingleEditTextOverflowed(editText.id, it)
            else if (formerText.isEmpty() && charSequence.isNotEmpty()) // If it's not a deletion
                spacedEditextCollector.setCursorOnNext(editText)
            else if (formerText.isNotEmpty() && charSequence.isEmpty()) // If it's a deletion
                spacedEditextCollector.onDeleteText(editText)
        }

    }

}