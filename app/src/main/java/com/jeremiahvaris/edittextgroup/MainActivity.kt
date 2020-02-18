package com.jeremiahvaris.edittextgroup

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SpacedEditextCollector,
    View.OnFocusChangeListener, View.OnClickListener {
    private val numberOfEditTexts = 8
    private var cursorIndex = 0
    private var canMoveCursorToNext = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setOnFocusChangeListeners()
        setTextWatchers()
        continueButton.setOnClickListener(this)
    }

    private fun setTextWatchers() {
        for (index in 1..numberOfEditTexts)
            getEditTextByIndex(index)?.apply {
                addTextChangedListener(
                    CardLastEightTextWatcher(this@MainActivity,this)
                )
            }
    }

    private fun setOnFocusChangeListeners() {
        for (index in 1..numberOfEditTexts)
            getEditTextByIndex(index)?.onFocusChangeListener = this
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            continueButton.id -> showResult()
        }
    }

    private fun showResult() {
        resultTv.text = getCardPan()
    }

    override fun setCursorAtEnd() {
        val cardPan = getCardPan()
        cardPan.replace(" ", "")
        getEditTextByIndex(cardPan.length+1)?.requestFocus()
    }

    override fun setCursorIfFieldNotEmpty(index: Int) {
        getEditTextByIndex(index)?.let {
            if (it.text.isNotEmpty()) {
                it.requestFocus()
                it.setSelection(it.text.lastIndex)
            } else setCursorAtEnd()
        }
    }


    override fun onSingleEditTextOverflowed(
        viewId: Int,
        sequence: CharSequence,
        isRecursiveCall: Boolean
    ) {
        if (sequence.isNotEmpty()) {
            val editText: EditText = findViewById(viewId)
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

    override fun setCursorOnNext(editText: EditText) {
        if (canMoveCursorToNext) {
            val index = getEditTextIndex(editText)
            getEditTextByIndex(index + 1)?.requestFocus()
        }
    }

    override fun onDeleteText(editText: EditText) {
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

    override fun afterTextChanged() {
        if (getCardPan().length == numberOfEditTexts) onFieldFullChanged(true)
        else onFieldFullChanged(false)
    }

    private fun onFieldFullChanged(isFull: Boolean) {
        continueButton.isEnabled = isFull
    }

    private fun onMidTextDeletion(index: Int) {
        var cardPan = getCardPan()
        cardPan = cardPan.replace(" ", "")
        setCardPan(cardPan)
        setCursorIfFieldNotEmpty(index)
    }

    private fun setCardPan(cardPan: String, startIndex: Int = 0) {
        if (startIndex < numberOfEditTexts) {
            getEditTextByIndex(startIndex + 1)?.apply {
                if (cardPan.length > startIndex) setText(cardPan[startIndex].toString())
                else setText("")
            }
            setCardPan(cardPan, startIndex + 1)
        }
    }

    private fun getEditTextByIndex(index: Int): EditText? {
        return when (index) {
            1 -> editText1
            2 -> editText2
            3 -> editText3
            4 -> editText4
            5 -> editText5
            6 -> editText6
            7 -> editText7
            8 -> editText8
            else -> null
        }
    }

    private fun checkIfEmptyById(editTextId: Int): Boolean? {
        return if (editTextId > -1) {
            findViewById<EditText>(editTextId).text.isEmpty()
        } else null
    }

    private fun getEditTextID(index: Int): Int {
        return when (index) {
            1 -> editText1.id
            2 -> editText2.id
            3 -> editText3.id
            4 -> editText4.id
            5 -> editText5.id
            6 -> editText6.id
            7 -> editText7.id
            8 -> editText8.id
            else -> -1
        }
    }

    private fun getEditTextIndex(editText: EditText): Int {
        return when (editText.id) {
            editText1.id -> 1
            editText2.id -> 2
            editText3.id -> 3
            editText4.id -> 4
            editText5.id -> 5
            editText6.id -> 6
            editText7.id -> 7
            editText8.id -> 8
            else -> -1
        }
    }

    fun setCursorOnPrevious(editText: EditText) {
        setCursorIfFieldNotEmpty(getEditTextIndex(editText)-1)
    }

    private fun getCardPan(): String {
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
}
