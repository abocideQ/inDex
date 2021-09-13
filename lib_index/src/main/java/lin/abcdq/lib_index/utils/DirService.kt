package lin.abcdq.lib_index.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build.VERSION_CODES
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.File
import java.io.OutputStream

object DirService {

    fun appCacheDir(context: Context): String {//storage/emulated/0/Android/data/com.xxx/cache/
        return context.cacheDir.absolutePath
    }

    fun appFilesDir(context: Context): String {//storage/emulated/0/Android/data/com.xxx/files/
        return context.filesDir.absolutePath
    }

    fun appObbDir(context: Context): String {//storage/emulated/0/Android/obb/com.xxx/
        return context.obbDir.absolutePath
    }

    private fun dataDir(): String {//data/
        return Environment.getDataDirectory().absolutePath
    }

    private fun dataCacheDir(): String {//data/cache/
        return Environment.getDownloadCacheDirectory().absolutePath
    }

    private fun systemDir(): String {//system/
        return Environment.getRootDirectory().absolutePath
    }

    /**
     * MediaStore for public folder image/audio/video/download
     * api > 29
     */

    @RequiresApi(VERSION_CODES.Q)
    fun mediaStoreQuery(context: Context, name_or_Path: String): ArrayList<File> {
        val list = ArrayList<File>()
        try {
            val selection = ("(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0")
            val args = arrayOf(
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
                MediaStore.Files.FileColumns.MEDIA_TYPE_NONE.toString()
            )
            val order = "datetaken DESC"
            context.contentResolver.query(
                mediaStoreDownload(),
                null,
                selection,
                args,
                order
            )?.use { cursor ->
                while (cursor.moveToNext()) {
                    try {
                        val path: String = cursor.getString(cursor.getColumnIndexOrThrow("_data"))
                        val name: String =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))
                                ?: ""
                        if (name.contains("" + name_or_Path) || path.contains("" + name_or_Path)) {
                            list.add(File(path))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        continue
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    @RequiresApi(VERSION_CODES.Q)
    fun mediaStore2Download(
        context: Context,
        name_file: String,
        name_folder: String
    ): OutputStream? {
        try {
            val values = ContentValues()
            values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, name_file)
            values.put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS + "/$name_folder"
            )
            val uri = context.contentResolver.insert(mediaStoreDownload(), values)
            return context.contentResolver.openOutputStream(uri ?: return null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @RequiresApi(VERSION_CODES.Q)
    fun mediaStoreDelete(context: Context, name_or_path: String) {
        try {
            val selection = "_display_name=? OR _data=?"
            val args = arrayOf(name_or_path, name_or_path)
            context.contentResolver.delete(mediaStoreDownload(), selection, args)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(VERSION_CODES.Q)
    private fun mediaStoreDownload(): Uri {
        return MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    }

    private fun mediaStoreImage(): Uri {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    private fun mediaStoreVideo(): Uri {
        return MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    }

    private fun mediaStoreAudio(): Uri {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }
}