package com.apk_home_service.customer.UI;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.UserData;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class QRScreen extends AppCompatActivity {
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }    setContentView(R.layout.activity_qrscreen);
        ImageView qrImage = findViewById(R.id.qrImage);
        try {
            userData = (UserData) PrefrenceHandler.getPreferences(this).getObjectValue(new UserData());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        findViewById(R.id.parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try {
            Bitmap bitmap = encodeAsBitmap(userData.getqrcode());
            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            Bitmap bitmap = null;
            try {
                bitmap = encodeAsBitmap("Home Service");
            } catch (WriterException e1) {
                e1.printStackTrace();
            }
            qrImage.setImageBitmap(bitmap);
        }
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 300, 300, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 300, 0, 0, w, h);
        return bitmap;
    }
}
