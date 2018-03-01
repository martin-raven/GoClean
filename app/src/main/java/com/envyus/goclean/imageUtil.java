package com.envyus.goclean;

/**
 * Created by seby on 3/2/2018.
 */

/**
 * Created by martin on 19-Jan-18.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayOutputStream;

public class imageUtil
{
    //converts base64 str to Bitmap
    public static Bitmap convert(String base64Str) throws IllegalArgumentException
    {
        //converting to byte form
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );
        //conversion
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
    //converts bitmap to base64str
    public static String convert(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //compressing the image
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        //conversion
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

}
