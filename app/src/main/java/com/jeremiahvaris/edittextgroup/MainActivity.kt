package com.jeremiahvaris.edittextgroup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jeremiahvaris.spacededittext.SpacedEditTextCollector
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SpacedEditTextCollector{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerEditTexts()
        continueButton.setOnClickListener{showResult()}
    }

    private fun registerEditTexts() {
        registerEditTexts(
            editText1,
            editText2,
            editText3,
            editText4
        )
    }

    private fun showResult() {
        resultTv.text = collectTextFromEditTexts()
    }

    override fun onFieldFullChanged(isFull: Boolean) {
        continueButton.isEnabled = isFull
    }
}
