package ru.ip_fateev.lavka

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import java.lang.Boolean
import kotlin.CharSequence
import kotlin.Int
import kotlin.String
import kotlin.arrayOf


class PayCashActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_AMOUNT = "Amount"
        const val EXTRA_SUM = "Sum"
    }

    lateinit var payCashSum: EditText
    val paySum: MutableLiveData<Double?> by lazy { MutableLiveData<Double?>(null) }
    lateinit var payCashDone: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_cash)

        payCashSum = findViewById(R.id.payCashSum)
        payCashSum.filters = arrayOf<InputFilter>(
            object : DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                var beforeDecimal = 5
                var afterDecimal = 2
                override fun filter(
                    source: CharSequence, start: Int, end: Int,
                    dest: Spanned, dstart: Int, dend: Int
                ): CharSequence {
                    var temp: String = payCashSum.text.toString() + source.toString()
                    if (temp == ".") {
                        return "0."
                    } else if (temp.indexOf(".") == -1) {
                        // no decimal point placed yet
                        if (temp.length > beforeDecimal) {
                            return ""
                        }
                    } else {
                        temp = temp.substring(temp.indexOf(".") + 1)
                        if (temp.length > afterDecimal) {
                            return ""
                        }
                    }
                    return source
                }
            }
        )
        payCashSum.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                paySum.value = payCashSum.text.toString().toDoubleOrNull()
            }

        })

        payCashDone = findViewById(R.id.payCashDone)
        payCashDone.setOnClickListener {
            payDone()
        }

        paySum.observe(this) {
            if (it != null) {
                payCashDone.isEnabled = true
            }
            else
            {
                payCashDone.isEnabled = false
            }
        }
    }

    private fun payDone() {
        val intent = Intent()
        intent.putExtra(EXTRA_SUM, paySum.value)
        setResult(RESULT_OK, intent)
        finish()
    }
}