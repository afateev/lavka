package ru.ip_fateev.lavka.documents

import android.graphics.*

data class ReceiptHelper(val receipt: Receipt? = null,
                         var positions: List<Position>? = null,
                         var transactions: List<Transaction>? = null,) {
    companion object {
        val COLOR_BACKGROUND = Color.rgb(250, 250, 250)
        val COLOR_TEXT = Color.BLACK
        val FONT_SIZE = 20F

        val ALIGN_LEFT = 0
        val ALIGN_CENTER = 1
        val ALIGN_RIGHT = 2
    }

    val paintText = Paint().apply {
        style = Paint.Style.FILL
        color = COLOR_TEXT
        isAntiAlias = false
        textSize = FONT_SIZE
        typeface = Typeface.MONOSPACE
    }

    fun getAmount(): Double {
        var result = 0.0

        if (positions != null) {
            for (p in positions!!) {
                val sum = p.price

                result += sum
            }
        }

        return result
    }

    fun getState(): ReceiptState {
        if (receipt != null) {
            return receipt.state
        }

        return ReceiptState.NEW
    }

    fun getTotal(): Double {
        var result = 0.0

        result += getCashTotal()
        result += getCardTotal()

        return result
    }

    fun getCashTotal(): Double {
        var result = 0.0
        if (transactions != null) {
            for (t in transactions!!) {
                if (t.type == TransactionType.CASH) {
                    if (t.amount > 0) {
                        result += t.amount
                    }
                }
            }
        }

        return result
    }

    fun getCashChangeTotal(): Double {
        var result = 0.0
        if (transactions != null) {
            for (t in transactions!!) {
                if (t.type == TransactionType.CASHCHAGE) {
                    if (t.amount > 0) {
                        result += t.amount
                    }
                }
            }
        }

        return result
    }

    fun getCardTotal(): Double {
        var result = 0.0
        if (transactions != null) {
            for (t in transactions!!) {
                if (t.type == TransactionType.CARD) {
                    if (t.amount > 0) {
                        result += t.amount
                    }
                }
            }
        }

        return result
    }

    fun getRemainder(): Double {
        var result = 0.0

        result += getAmount()
        result -= getTotal()

        return result
    }

    fun toStrings(limit: Int): List<String> {
        var lines = mutableListOf<String>()

        if (receipt != null) {
            lines.add(strF(limit, "КАССОВЫЙ ЧЕК", ALIGN_CENTER))
            lines.add(strF(limit, "Продажа №2553 Смена №84", ALIGN_CENTER))
            lines.add(strF(limit, ""))
        }

        if (positions != null) {
            for (p in positions!!) {
                var s1 = strF(3, (1 + p.number).toString() + ": ")
                var s2 = strF(limit - 3, p.productName)
                var s = s1 + s2
                lines.add(s)
                lines.add(strF(limit, "1 шт x " + p.price.toString() + " = " + p.price.toString(), ALIGN_RIGHT))
            }
        }

        lines.add(strF(limit, "ОПЛАТА:"))

        val cashTotal = getCashTotal()
        if (cashTotal > 0) {
            val s1 = strF(15, "НАЛИЧНЫМИ")
            val s2 = strF(limit - 15, cashTotal.toString())
            val s = " " + s1 + s2
            lines.add(s)
        }

        val cashChangeTotal = getCashChangeTotal()
        if (cashChangeTotal > 0) {
            val s1 = strF(15, "СДАЧА")
            val s2 = strF(limit - 15, cashChangeTotal.toString())
            val s = " " + s1 + s2
            lines.add(s)
        }

        val cardTotal = getCardTotal()
        if (cardTotal > 0) {
            val s1 = strF(15, "БЕЗНАЛИЧНЫМИ")
            val s2 = strF(limit - 15, cardTotal.toString())
            val s = " " + s1 + s2
            lines.add(s)
        }

        return lines
    }

    fun toBimap(): Bitmap {
        val strHeight = FONT_SIZE + 1
        val lines = toStrings(46)
        val bitmapHeight = ((lines.size + 10) * strHeight).toInt()

        val bitmap = Bitmap.createBitmap(570, bitmapHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(COLOR_BACKGROUND)

        var y = strHeight
        for(l in lines) {
            canvas.drawText(l, 10F, y, paintText)

            y += strHeight
        }

        return bitmap
    }

    private fun strF(limit: Int, s: String, align: Int = ALIGN_LEFT): String {
        return strAlign(limit, strClean(limit, s), align)
    }

    private fun strClean(limit: Int, s: String): String {
        var str = s
        str = str.replace("\\s+".toRegex(), " ")
        str = str.trim()

        if (str.length > limit)
            str = str.substring(0, limit)

        return str
    }

    private fun strAlign(limit: Int, s: String, align: Int): String {
        var str = s

        val diff = limit - str.length
        if (diff < 1) {
            return str
        }

        if (align == ALIGN_LEFT)
        {
            str = str.padEnd(limit)
        }

        if (align == ALIGN_CENTER)
        {
            var padLeft = diff / 2

            padLeft += str.length
            str = str.padStart(padLeft)
            str = str.padEnd(limit)
        }

        if (align == ALIGN_RIGHT)
        {
            str = str.padStart(limit)
        }

        return str
    }
}