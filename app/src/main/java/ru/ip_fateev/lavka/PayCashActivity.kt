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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import java.lang.Boolean
import kotlin.CharSequence
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.arrayOf
import kotlin.getValue
import kotlin.lazy
import kotlin.toString


class PayCashActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_AMOUNT = "Amount"
        const val EXTRA_SUM = "Sum"
        const val EXTRA_CHANGE = "Change"
    }

    lateinit var payCashAmount: TextView
    val payAmount: MutableLiveData<Double?> by lazy { MutableLiveData<Double?>(null) }
    lateinit var payCashSum: EditText
    val paySum: MutableLiveData<Double?> by lazy { MutableLiveData<Double?>(null) }
    lateinit var payCashChange: TextView
    val payChange: MutableLiveData<Double?> by lazy { MutableLiveData<Double?>(null) }
    lateinit var payCashCancel: Button
    lateinit var payCashDone: Button
    lateinit var payCashNoChange: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_cash)

        payCashAmount = findViewById(R.id.payCashAmount)
        payAmount.value = intent.getDoubleExtra(EXTRA_AMOUNT, 0.0)

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

        payCashChange = findViewById(R.id.payCashChange)
        payChange.value = 0.0

        payCashCancel = findViewById(R.id.payCashCancel)
        payCashCancel.setOnClickListener {

        }

        payCashDone = findViewById(R.id.payCashDone)
        payCashDone.setOnClickListener {
            payDone()
        }

        payCashNoChange = findViewById(R.id.payCashNoChange)
        payCashNoChange.setOnClickListener {
            paySum.value = payAmount.value
            payDone()
        }

        payAmount.observe(this) {
            if (it != null) {
                payCashAmount.text = payAmount.value.toString()
                payCashCancel.isVisible = true
                payCashDone.isVisible = true
                payCashNoChange.isVisible = true
            }
            else
            {
                payCashAmount.text = "---"
                payCashCancel.isVisible = false
                payCashDone.isVisible = false
                payCashNoChange.isVisible = false
            }
        }

        paySum.observe(this) {
            if (it == null) {
                payCashCancel.isVisible = false
                payCashDone.isVisible = false
                payCashNoChange.isVisible = true
            }
            else
            {
                payCashCancel.isVisible = true
                payCashDone.isVisible = true
                payCashNoChange.isVisible = false
                if (payAmount.value != null) {
                    if (paySum.value!! >= payAmount.value!!) {
                        payCashDone.isEnabled = true
                    }
                    else
                    {
                        payCashDone.isEnabled = false
                    }

                    if (paySum.value!! > payAmount.value!!) {
                        payChange.value = paySum.value!! - payAmount.value!!
                    }
                    else {
                        payChange.value = 0.0
                    }
                }
            }
        }

        payChange.observe(this) {
            if (it != null) {
                val v = payChange.value
                if (v!! > 0.0) {
                    payCashChange.text = v.toString()
                }
                else {
                    payCashChange.text = "---"
                }
            }
        }
    }

    private fun payDone() {
        val intent = Intent()

        var sum = paySum.value!!
        var change = 0.0
        if (sum > payAmount.value!!) {
            change = sum - payAmount.value!!
            sum = payAmount.value!!
        }

        intent.putExtra(EXTRA_SUM, sum)
        intent.putExtra(EXTRA_CHANGE, change)
        setResult(RESULT_OK, intent)
        finish()
    }
}