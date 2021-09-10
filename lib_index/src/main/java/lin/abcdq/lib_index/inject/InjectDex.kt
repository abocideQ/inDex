package lin.abcdq.lib_index.inject

import android.annotation.SuppressLint
import dalvik.system.BaseDexClassLoader
import dalvik.system.DexClassLoader
import java.io.File

/**
 * dexElement 倒桩插入Dex
 */
object InjectDex {

    private const val DEX_SUFFIX = ".dex"

    fun injectDex(newDexFolder: String, outDexFolder: String) {
        try {
            val pathClassLoader = classLoader()
            val dexOutFolder = File(outDexFolder)
            if (!dexOutFolder.exists()) dexOutFolder.mkdirs()
            val dexFolder = File(newDexFolder)
            if (!dexFolder.exists()) dexFolder.mkdirs()
            val list = dexFolder.listFiles() ?: return
            list.forEach {
                if (it.name.endsWith(DEX_SUFFIX)) {
                    val oldElement = getDexElements(pathClassLoader!!)
                    val classLoader = DexClassLoader(
                        it.absolutePath,
                        outDexFolder,
                        it.absolutePath,
                        pathClassLoader
                    )
                    val newElement = getDexElements(classLoader)
                    val mergeElement = combineArray(newElement!!, oldElement!!)
                    injectDex(pathClassLoader, mergeElement!!)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun classLoader(): ClassLoader? {
        return this.javaClass.classLoader
    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun getDexElements(obj: ClassLoader): Any? {
        //pathList
        val pathListField = BaseDexClassLoader::class.java.getDeclaredField("pathList")
        pathListField.isAccessible = true
        val pathList = pathListField.get(obj)
        //dexElement
        val pathListClass = pathList::class.java
        val dexElementsField = pathListClass.getDeclaredField("dexElements")
        dexElementsField.isAccessible = true
        return dexElementsField.get(pathList)
    }

    private fun combineArray(new: Any, old: Any): Any? {
        val localClass = new.javaClass.componentType ?: return null
        val newLength: Int = java.lang.reflect.Array.getLength(new)
        val oldLength: Int = java.lang.reflect.Array.getLength(old)
        val allLength: Int = newLength + oldLength
        val array: Any = java.lang.reflect.Array.newInstance(localClass, allLength)
        System.arraycopy(new, 0, array, 0, newLength)
        System.arraycopy(old, 0, array, newLength, oldLength)
        return array
    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun injectDex(obj: ClassLoader, dexElement: Any) {
        //pathList
        val pathListField = BaseDexClassLoader::class.java.getDeclaredField("pathList")
        pathListField.isAccessible = true
        val pathList = pathListField.get(obj)
        //dexElement
        val pathListClass = pathList::class.java
        val dexElementsField = pathListClass.getDeclaredField("dexElements")
        dexElementsField.isAccessible = true
        dexElementsField.set(pathList, dexElement)
    }
}