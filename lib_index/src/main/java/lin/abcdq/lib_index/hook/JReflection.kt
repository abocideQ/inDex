package lin.abcdq.lib_index.hook

import android.view.View
import java.lang.reflect.Constructor

/**
 * Java 反射
 */
object JReflection {

    fun clazz(packName: String): Class<*> {
        return Class.forName(packName)
    }

    fun <T> field(obj: Any?, clazz: Class<T>, name: String): Any? {
        try {
            val field = clazz.getDeclaredField(name)
            field.isAccessible = true
            return field.get(obj)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun <T> field(obj: Any?, clazz: Class<T>, name: String, value: Any?) {
        try {
            val field = clazz.getDeclaredField(name)
            field.isAccessible = true
            return field.set(obj, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun <T> methodViewClick(obj: Any, clazz: Class<T>, name: String, value: Any) {
        try {
            val method = clazz.getDeclaredMethod(name, View.OnClickListener::class.java)
            method.isAccessible = true
            method.invoke(obj, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun <T> method(
        obj: Any?,
        clazz: Class<T>,
        name: String,
        valueClazz: Array<Class<*>?>,
        values: Array<Any?>
    ): Any? {
        try {
            when {
                obj == null -> {//@JavaStatic Method
                    val method = clazz.getDeclaredMethod(name)
                    method.isAccessible = true
                    return method.invoke(null)
                }
                valueClazz.isEmpty() -> {
                    val method = clazz.getDeclaredMethod(name)
                    method.isAccessible = true
                    return method.invoke(obj)
                }
                values.isEmpty() -> {
                    val method = clazz.getDeclaredMethod(name)
                    method.isAccessible = true
                    return method.invoke(obj)
                }
                else -> {
                    val method = clazz.getDeclaredMethod(name, *valueClazz)
                    method.isAccessible = true
                    return method.invoke(obj, *values)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun <T> construct(
        clazz: Class<T>,
        valueClazz: Array<Class<*>?>,
        values: Array<Any?>
    ): Any? {
        try {
            val constructor: Constructor<T>?
            when {
                valueClazz.isEmpty() -> {
                    constructor = clazz.getDeclaredConstructor()
                    constructor.newInstance()
                }
                values.isEmpty() -> {
                    constructor = clazz.getDeclaredConstructor()
                    constructor.newInstance()
                }
                else -> {
                    constructor = clazz.getDeclaredConstructor(*valueClazz)
                    constructor?.newInstance(*values)
                }
            }
            return constructor
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}