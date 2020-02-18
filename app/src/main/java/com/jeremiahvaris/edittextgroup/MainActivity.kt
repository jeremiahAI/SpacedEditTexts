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
        editText1.addTextChangedListener(CardLastEightTextWatcher(this, editText1))
        editText2.addTextChangedListener(CardLastEightTextWatcher(this, editText2))
        editText3.addTextChangedListener(CardLastEightTextWatcher(this, editText3))
        editText4.addTextChangedListener(CardLastEightTextWatcher(this, editText4))
        editText5.addTextChangedListener(CardLastEightTextWatcher(this, editText5))
        editText6.addTextChangedListener(CardLastEightTextWatcher(this, editText6))
        editText7.addTextChangedListener(CardLastEightTextWatcher(this, editText7))
        editText8.addTextChangedListener(CardLastEightTextWatcher(this, editText8))
    }

    private fun setOnFocusChangeListeners() {
        editText1.onFocusChangeListener = this
        editText2.onFocusChangeListener = this
        editText3.onFocusChangeListener = this
        editText4.onFocusChangeListener = this
        editText5.onFocusChangeListener = this
        editText6.onFocusChangeListener = this
        editText7.onFocusChangeListener = this
        editText8.onFocusChangeListener = this
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
        when (cardPan.length) {
            0 -> editText1.requestFocus()
            1 -> editText2.requestFocus()
            2 -> editText3.requestFocus()
            3 -> editText4.requestFocus()
            4 -> editText5.requestFocus()
            5 -> editText6.requestFocus()
            6 -> editText7.requestFocus()
            else -> editText8.requestFocus()
        }
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
        if (canMoveCursorToNext)
            when (editText.id) {
                editText1.id -> editText2.requestFocus()
                editText2.id -> editText3.requestFocus()
                editText3.id -> editText4.requestFocus()
                editText4.id -> editText5.requestFocus()
                editText5.id -> editText6.requestFocus()
                editText6.id -> editText7.requestFocus()
                editText7.id -> editText8.requestFocus()
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
        when (editText.id) {
            editText2.id -> setCursorIfFieldNotEmpty(1)
            editText3.id -> setCursorIfFieldNotEmpty(2)
            editText4.id -> editText3.requestFocus()
            editText5.id -> editText4.requestFocus()
            editText6.id -> editText5.requestFocus()
            editText7.id -> editText6.requestFocus()
            editText8.id -> editText7.requestFocus()
        }
    }

    private fun getCardPan(): String {
        return editText1.text.toString() + editText2.text.toString() + editText3.text.toString() + editText4.text.toString() + editText5.text.toString() + editText6.text.toString() + editText7.text.toString() + editText8.text.toString()
    }

    override fun onFocusChange(p0: View?, hasFocus: Boolean) {
        if (hasFocus) {
            canMoveCursorToNext = true
            when (p0?.id) {
                editText1.id -> this.setCursorIfFieldNotEmpty(1)
                editText2.id -> this.setCursorIfFieldNotEmpty(2)
                editText3.id -> this.setCursorIfFieldNotEmpty(3)
                editText4.id -> this.setCursorIfFieldNotEmpty(4)
                editText5.id -> this.setCursorIfFieldNotEmpty(5)
                editText6.id -> this.setCursorIfFieldNotEmpty(6)
                editText7.id -> this.setCursorIfFieldNotEmpty(7)
                editText8.id -> this.setCursorIfFieldNotEmpty(8)

            }
        }
    }
}
