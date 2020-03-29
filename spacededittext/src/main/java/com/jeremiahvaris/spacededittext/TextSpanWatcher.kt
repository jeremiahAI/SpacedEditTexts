package com.jeremiahvaris.spacededittext

import android.text.SpanWatcher
import android.text.Spannable
import android.widget.EditText

class TextSpanWatcher(val editText: EditText):SpanWatcher{

    override fun onSpanChanged(
        text: Spannable?,
        what: Any?,
        ostart: Int,
        oend: Int,
        nstart: Int,
        nend: Int
    ) {
        editText.setSelection(editText.text.length)

        /*
        if (what == Selection.SELECTION_START) {
            // Selection start changed from ostart to nstart.
            editText.setSelection(editText.text.length)
        } else if (what == Selection.SELECTION_END) {
            // Selection end changed from ostart to nstart.
            editText.setSelection(editText.text.length)
        }*/
    }

    override fun onSpanRemoved(text: Spannable?, what: Any?, start: Int, end: Int) {
    }

    override fun onSpanAdded(text: Spannable?, what: Any?, start: Int, end: Int) {
    }
}
