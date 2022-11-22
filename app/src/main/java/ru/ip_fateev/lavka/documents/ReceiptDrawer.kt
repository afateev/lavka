package ru.ip_fateev.lavka.documents

import android.graphics.*

class ReceiptDrawer(_receipt: Receipt?, _positions: List<Position>?) {
    companion object {
        val COLOR_BACKGROUND = Color.rgb(200, 200, 200)
        val COLOR_TEXT = Color.BLACK
        val FONT_SIZE = 24F
    }

    val receipt: Receipt?
    val positions: List<Position>?

    val paintText = Paint().apply {
        style = Paint.Style.FILL
        color = COLOR_TEXT
        isAntiAlias = false
        textSize = FONT_SIZE
        typeface = Typeface.MONOSPACE
    }

    init{
        receipt = _receipt
        positions = _positions
    }

    fun toBimap(): Bitmap {
        val bitmap = Bitmap.createBitmap(600, 2000, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(COLOR_BACKGROUND)

        if (receipt != null) {

        }

        if (positions != null) {
            val strHeight = FONT_SIZE
            var y = strHeight
            for(p in positions) {
                var str = (1 + p.number).toString() + ": " + p.productName
                canvas.drawText(str, 1F, y, paintText)

                y += strHeight
            }
        }

        return bitmap
    }
}