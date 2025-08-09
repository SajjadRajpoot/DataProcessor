package com.example.spytech;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
public class FileUtils {
    public static String getPath(Context context, Uri uri) {
        String path = null;
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null){
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(column_index);
            }
            cursor.close();
        }
        return path;
    }
}
