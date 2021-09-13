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
     * MediaStore for public folder Picture/Movie/Music/Download
     * api > 29
     */

    fun mediaStoreQuery(context: Context, mediaUri: Uri, tag: String): ArrayList<File> {
        val list = ArrayList<File>()
        try {
            val selection = (""
                    + "   (" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0")
            val args = arrayOf(
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
                MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO.toString(),
                MediaStore.Files.FileColumns.MEDIA_TYPE_NONE.toString()
            )
            val order = "datetaken DESC"
            context.contentResolver.query(mediaUri, null, selection, args, order)?.use { cursor ->
                while (cursor.moveToNext()) {
                    try {
                        val path: String = cursor.getString(cursor.getColumnIndexOrThrow("_data"))
                        val name: String =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))
                                ?: ""
                        if (name.contains("" + tag) || path.contains("" + tag)) {
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

    fun mediaStoreCreate(
        context: Context,
        mediaUri: Uri,
        mediaValue: ContentValues
    ): OutputStream? {
        try {
            val uri = context.contentResolver.insert(mediaUri, mediaValue)
            return context.contentResolver.openOutputStream(uri ?: return null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun mediaStoreDelete(context: Context, mediaUri: Uri, file: String) {
        try {
            val selection = "_display_name=? OR _data=?"
            val args = arrayOf(file, file)
            context.contentResolver.delete(mediaUri, selection, args)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(VERSION_CODES.Q)
    fun mediaUriDownload(): Uri {
        return MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    }

    fun mediaUriImage(): Uri {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    fun mediaUriMovie(): Uri {
        return MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    }

    fun mediaUriMusic(): Uri {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }

    @RequiresApi(VERSION_CODES.Q)
    fun mediaValueDownload(folder: String, name: String): ContentValues {
        return ContentValues().apply {
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS + "/$folder"
            )
            put(MediaStore.Files.FileColumns.DISPLAY_NAME, name)
        }
    }

    @RequiresApi(VERSION_CODES.Q)
    fun mediaValueImage(folder: String, name: String, mime: String): ContentValues {
        return ContentValues().apply {
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + "/$folder"
            )
            put(MediaStore.Images.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.MIME_TYPE, mime)
        }
    }

    @RequiresApi(VERSION_CODES.Q)
    fun mediaValueVideo(folder: String, name: String, mime: String): ContentValues {
        return ContentValues().apply {
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_MOVIES + "/$folder"
            )
            put(MediaStore.Video.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.MIME_TYPE, mime)
        }
    }

    @RequiresApi(VERSION_CODES.Q)
    fun mediaValueMusic(folder: String, name: String, mime: String): ContentValues {
        return ContentValues().apply {
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_MUSIC + "/$folder"
            )
            put(MediaStore.Audio.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.MIME_TYPE, mime)
        }
    }
}