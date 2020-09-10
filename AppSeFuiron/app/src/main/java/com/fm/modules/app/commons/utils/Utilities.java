package com.fm.modules.app.commons.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Utilities class has different static methods, that can be implemented in any other class
 **/

public class Utilities {


    //*********** Shares the Product with its Image and Url ********//

    public static void shareImage(Context context, String subject, ImageView imageView, String url) {

        Uri bmpUri = getLocalBitmapUri(context, imageView);

        if (bmpUri != null) {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");

            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, url);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

            context.startActivity(Intent.createChooser(shareIntent, "Share via"));

        } else {
            Toast.makeText(context, "Null bmpUri", Toast.LENGTH_SHORT).show();
        }
    }


    //*********** Convert Bitmap into Uri ********//

    public static Uri getLocalBitmapUri(Context context, ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            Toast.makeText(context, "drawable isn't instanceof BitmapDrawable", Toast.LENGTH_SHORT).show();
            return null;
        }

        // Store image to default external storage directory
        Uri bitmapUri = null;

        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();

            // **Warning:** This will fail for API > 24, use a FileProvider as shown below instead.
            bitmapUri = Uri.fromFile(file);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "IOException" + e, Toast.LENGTH_SHORT).show();
        }


        return bitmapUri;
    }


    //*********** Converts any Bitmap to Base64String ********//

    public static String getBase64ImageStringFromBitmap(Bitmap bitmap) {
        String imgString;

        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

            byte[] profileImage = byteArrayOutputStream.toByteArray();

            imgString = "data:image/jpeg;base64," + Base64.encodeToString(profileImage, Base64.NO_WRAP);

        } else {
            imgString = "";
        }


        return imgString;
    }


    //*********** Converts a Base64String to the Bitmap ********//

    public static Bitmap getBitmapFromBase64ImageString(String b64) {
        Bitmap bitmap = null;

        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        return bitmap;
    }

    public static void pirntBase64FromByte(byte[] byteArray){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String tt = java.util.Base64.getEncoder().encodeToString(byteArray);
            System.out.println(tt);
        }
    }

}
