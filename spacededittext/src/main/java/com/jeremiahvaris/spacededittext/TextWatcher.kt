package com.jeremiahvaris.spacededittext

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class TextWatcher(
    private val spacedEditTextCollector: SpacedEditTextCollector,
    private val editText: EditText
) : TextWatcher {
    private var formerText = ""
    override fun afterTextChanged(p0: Editable?) {
        spacedEditTextCollector.afterTextChanged(editText)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        formerText = editText.text.toString()

    }

    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
        spacedEditTextCollector.onTextChanged(
            charSequence,
            start,
            before,
            count,
            formerText,
            editText
        )
    }

}