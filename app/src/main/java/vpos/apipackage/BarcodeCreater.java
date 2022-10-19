package vpos.apipackage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Map;

import androidx.core.view.ViewCompat;

public abstract class BarcodeCreater {
    private static BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;
    private static int marginW = 20;

    public static Bitmap creatBarcode(Context context, String str, int i, int i2, boolean z) {
        if (z) {
            return mixtureBitmap(encodeAsBitmap(str, barcodeFormat, i, i2), creatCodeBitmap(str, i + (marginW * 2), i2, context), new PointF(0.0f, (float) i2));
        }
        return encodeAsBitmap(str, barcodeFormat, i, i2);
    }

    public static Bitmap creatBarcode(String str, int i, int i2, BarcodeFormat barcodeFormat2) {
        return encodeAsBitmap(str, barcodeFormat2, i, i2);
    }

    protected static Bitmap creatCodeBitmap(String str, int i, int i2, Context context) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        textView.setText(str);
        textView.setHeight(i2);
        textView.setGravity(1);
        textView.setWidth(i);
        textView.setDrawingCacheEnabled(true);
        textView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        textView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
        textView.buildDrawingCache();
        return textView.getDrawingCache();
    }

    protected static Bitmap encodeAsBitmap(String str, BarcodeFormat barcodeFormat2, int i, int i2) {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(str, barcodeFormat2, i, i2, (Map) null);
        } catch (WriterException e) {
            e.printStackTrace();
            bitMatrix = null;
        }
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        int[] iArr = new int[(width * height)];
        for (int i3 = 0; i3 < height; i3++) {
            int i4 = i3 * width;
            for (int i5 = 0; i5 < width; i5++) {
                iArr[i4 + i5] = bitMatrix.get(i5, i3) ? ViewCompat.MEASURED_STATE_MASK : -1;
            }
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        createBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
        return createBitmap;
    }

    protected static Bitmap mixtureBitmap(Bitmap bitmap, Bitmap bitmap2, PointF pointF) {
        if (bitmap == null || bitmap2 == null || pointF == null) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth() + bitmap2.getWidth() + marginW, bitmap.getHeight() + bitmap2.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawBitmap(bitmap, (float) marginW, 0.0f, (Paint) null);
        canvas.drawBitmap(bitmap2, pointF.x, pointF.y, (Paint) null);
        canvas.save();
        canvas.restore();
        return createBitmap;
    }
}
