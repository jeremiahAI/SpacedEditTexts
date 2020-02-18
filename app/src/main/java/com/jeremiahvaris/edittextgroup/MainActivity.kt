package com.jeremiahvaris.edittextgroup

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    com.jeremiahvaris.spacededittext.SpacedEditextCollector,
     View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerEditTexts()
        continueButton.setOnClickListener(this)
    }

    private fun registerEditTexts() {
        registerEditText(editText1)
        registerEditText(editText2)
        registerEditText(editText3)
        registerEditText(editText4)
        registerEditText(editText5)
        registerEditText(editText6)
        registerEditText(editText7)
        registerEditText(editText8)
        connectEditTexts()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            continueButton.id -> showResult()
        }
    }

    private fun showResult() {
        resultTv.text = getFullText()
    }

    override fun onFieldFullChanged(isFull: Boolean) {
            continueButton.isEnabled = isFull
    }


}
