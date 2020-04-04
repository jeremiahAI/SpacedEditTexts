package com.jeremiahvaris.spacededittext

import android.text.Spanned
import android.view.View
import android.widget.EditText

interface SpacedEditTextCollector:View.OnFocusChangeListener {

    companion object{
        private val editTextsArrayList = ArrayList<EditText>()

        private var cursorIndex = 0
        private var canMoveCursorToNext = true

    }

    private val numberOfEditTexts
        get() = editTextsArrayList.size
    private val rootView:View?
        get() {
            return if (editTextsArrayList.isNotEmpty()) editTextsArrayList[0].rootView else null
        }


    private fun setTextWatchers() {
        for (index in 1..numberOfEditTexts)
            getEditTextByIndex(index)?.apply {
                addTextChangedListener(
                    TextWatcher(
                        this@SpacedEditTextCollector,
                        this
                    )
                )
            }
    }

    private fun setOnFocusChangeListeners() {
        for (index in 1..numberOfEditTexts)
            getEditTextByIndex(index)?.onFocusChangeListener = this
    }

     fun setCursorAtEnd() {
        val fullText = getFullText()
        fullText.replace(" ", "")
        getEditTextByIndex(fullText.length)?.apply{
            requestFocus()
            setSelection(text.length)
        }
    }

    fun connectEditTexts(){
        setOnFocusChangeListeners()
        setTextWatchers()
    }

    fun registerEditTexts(vararg editTexts: EditText) {
        editTextsArrayList.clear()
        editTextsArrayList.addAll(editTexts)
        connectEditTexts()
    }
    fun setCursorIfFieldNotEmpty(index: Int =-1) {
        getEditTextByIndex(index)?.let {
            if (it.text.isNotEmpty()) {
                it.requestFocus()
                it.setSelection(it.text.length)
            } else setCursorAtEnd()
        }
    }

     fun onSingleEditTextOverflowed(
        viewId: Int,
        sequence: CharSequence,
        isRecursiveCall: Boolean = false
    ) {
        if (sequence.isNotEmpty() && rootView!=null) {
            val editText: EditText = rootView!!.findViewById(viewId)
            val index = getEditTextIndex(editText)
            val nextEditText = getEditTextByIndex(index+1)
            val newSequence: String

            if (isRecursiveCall) {
                newSequence = "" + sequence.removeRange(0, 1) + editText.text
            } else {
                newSequence = "" + sequence.removeRange(0, 1)
                cursorIndex = newSequence.length + index
                canMoveCursorToNext = false
            }
            setCursorIfFieldNotEmpty(cursorIndex)
            editText.setText(sequence[0].toString())

            if (newSequence.isNotEmpty())
                nextEditText?.let {
                    onSingleEditTextOverflowed(it.id, newSequence, true)
                }
        }

    }

     fun setCursorOnNext(editText: EditText) {
        if (canMoveCursorToNext) {
            val index = getEditTextIndex(editText)
            getEditTextByIndex(index + 1)?.requestFocus()
        }
    }

     fun onDeleteText(editText: EditText) {
        val index = getEditTextIndex(editText)
        if (index != numberOfEditTexts && index > -1) {
            val nextID = getEditTextID(index + 1)
            val nextIsEmpty = checkIfEmptyById(nextID)
            nextIsEmpty?.let { nextIsEmpty ->
                if (nextIsEmpty) setCursorOnPrevious(editText)
                else onMidTextDeletion(index)
            }
        }

    }

     fun afterTextChanged(editText: EditText) {
        if (getFullText().length > numberOfEditTexts-1) onFieldFullChanged(true)
        else onFieldFullChanged(false)

         editText.text.setSpan(TextSpanWatcher(editText), 0, 0, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
     }

    fun setCursorOnPrevious(editText: EditText) {
        setCursorIfFieldNotEmpty(getEditTextIndex(editText)-1)
    }

    fun getFullText(): String {
        var value = ""
        for (index in 1..numberOfEditTexts)
            value +=getEditTextByIndex(index)?.text.toString()
        return value
    }

    override fun onFocusChange(p0: View?, hasFocus: Boolean) {
        if (hasFocus) {
            canMoveCursorToNext = true
            if (p0 is EditText) setCursorIfFieldNotEmpty(getEditTextIndex(p0))
        }
    }

     fun onFieldFullChanged(isFull: Boolean){}

    private fun onMidTextDeletion(index: Int) {
        var fullText = getFullText()
        fullText = fullText.replace(" ", "")
        setFullText(fullText)
        setCursorIfFieldNotEmpty(index)
    }

    private fun setFullText(fullText: String, startIndex: Int = 0) {
        if (startIndex < numberOfEditTexts) {
            getEditTextByIndex(startIndex + 1)?.apply {
                if (fullText.length > startIndex) setText(fullText[startIndex].toString())
                else setText("")
            }
            setFullText(fullText, startIndex + 1)
        }
    }

    private fun getEditTextByIndex(index: Int): EditText? {
        return if (editTextsArrayList.size >= index && index >0) editTextsArrayList[index-1] else null
    }

    private fun checkIfEmptyById(editTextId: Int): Boolean? {
        return if (editTextId > -1 && rootView!=null) {
            rootView!!.findViewById<EditText>(editTextId).text.isEmpty()
        } else null
    }

    private fun getEditTextID(index: Int): Int {
        return getEditTextByIndex(index)?.id ?: -1
    }

    private fun getEditTextIndex(editText: EditText): Int {
        return editTextsArrayList.indexOf(editText)+1
    }

    fun onTextChanged(
        charSequence: CharSequence?,
        start: Int,
        before: Int,
        count: Int,
        formerText: String,
        editText: EditText
    ) {
        charSequence?.let {
            if (it.length > 1 || (start > 0 && it.isNotEmpty()))
                onSingleEditTextOverflowed(editText.id, it)
            else if (formerText.isEmpty() && charSequence.isNotEmpty()) // If it's not a deletion
               setCursorOnNext(editText)
            else if (formerText.isNotEmpty() && charSequence.isEmpty()) // If it's a deletion
                onDeleteText(editText)
        }
    }
}