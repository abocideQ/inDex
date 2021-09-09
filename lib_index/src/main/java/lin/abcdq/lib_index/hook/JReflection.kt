package lin.abcdq.lib_index.hook

import android.view.View
import java.lang.reflect.Constructor
import java.lang.reflect.Method

object JReflection {

    fun <T> field(obj: Any, clazz: Class<T>, name: String): Any? {
        try {
            val field = clazz.getDeclaredField(name)
            field.isAccessible = true
            return field.get(obj)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun <T> field(obj: Any, clazz: Class<T>, name: String, value: Any) {
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

    fun <T> method(obj: Any, clazz: Class<T>, name: String, vararg value: Any) {
        try {
            when {
                value.isEmpty() -> {
                    val method = clazz.getDeclaredMethod(name)
                    method.isAccessible = true
                    method.invoke(obj)
                    return
                }
                value.size == 1 -> {
                    val method = clazz.getDeclaredMethod(name, value[0]::class.java)
                    method.isAccessible = true
                    method.invoke(obj, value[0])
                }
                value.size == 2 -> {
                    val method = clazz.getDeclaredMethod(
                        name,
                        value[0]::class.java,
                        value[1]::class.java
                    )
                    method.isAccessible = true
                    method.invoke(obj, value[0], value[1])
                }
                value.size == 3 -> {
                    val method = clazz.getDeclaredMethod(
                        name,
                        value[0]::class.java,
                        value[1]::class.java,
                        value[2]::class.java
                    )
                    method.isAccessible = true
                    method.invoke(obj, value[0], value[1], value[2])
                }
                value.size == 4 -> {
                    val method = clazz.getDeclaredMethod(
                        name,
                        value[0]::class.java,
                        value[1]::class.java,
                        value[2]::class.java,
                        value[3]::class.java
                    )
                    method.isAccessible = true
                    method.invoke(obj, value[0], value[1], value[2], value[3])
                }
                value.size == 5 -> {
                    val method = clazz.getDeclaredMethod(
                        name,
                        value[0]::class.java,
                        value[1]::class.java,
                        value[2]::class.java,
                        value[3]::class.java,
                        value[4]::class.java
                    )
                    method.isAccessible = true
                    method.invoke(obj, value[0], value[1], value[2], value[3], value[4])
                }
                value.size == 6 -> {
                    val method = clazz.getDeclaredMethod(
                        name,
                        value[0]::class.java,
                        value[1]::class.java,
                        value[2]::class.java,
                        value[3]::class.java,
                        value[4]::class.java,
                        value[5]::class.java
                    )
                    method.invoke(obj, value[0], value[1], value[2], value[3], value[4], value[5])

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun <T> construct(clazz: Class<T>, vararg value: Any): Any? {
        try {
            var constructor: Constructor<T>? = null
            when {
                value.isEmpty() -> {
                    constructor = clazz.getDeclaredConstructor()
                    constructor.newInstance()
                }
                value.size == 1 -> {
                    constructor = clazz.getDeclaredConstructor(value[0]::class.java)
                    constructor?.newInstance(value[0])
                }
                value.size == 2 -> {
                    constructor = clazz.getDeclaredConstructor(
                        value[0]::class.java,
                        value[1]::class.java
                    )
                    constructor?.newInstance(
                        value[0],
                        value[1]
                    )
                }
                value.size == 3 -> {
                    constructor = clazz.getDeclaredConstructor(
                        value[0]::class.java,
                        value[1]::class.java,
                        value[2]::class.java
                    )
                    constructor?.newInstance(
                        value[0],
                        value[1],
                        value[2]
                    )
                }
                value.size == 4 -> {
                    constructor = clazz.getDeclaredConstructor(
                        value[0]::class.java,
                        value[1]::class.java,
                        value[2]::class.java,
                        value[3]::class.java
                    )
                    constructor?.newInstance(
                        value[0],
                        value[1],
                        value[2],
                        value[3]
                    )
                }
                value.size == 5 -> {
                    constructor = clazz.getDeclaredConstructor(
                        value[0]::class.java,
                        value[1]::class.java,
                        value[2]::class.java,
                        value[3]::class.java,
                        value[4]::class.java
                    )
                    constructor?.newInstance(
                        value[0],
                        value[1],
                        value[2],
                        value[3],
                        value[4]
                    )
                }
                value.size == 6 -> {
                    constructor = clazz.getDeclaredConstructor(
                        value[0]::class.java,
                        value[1]::class.java,
                        value[2]::class.java,
                        value[3]::class.java,
                        value[4]::class.java,
                        value[5]::class.java
                    )
                    constructor?.newInstance(
                        value[0],
                        value[1],
                        value[2],
                        value[3],
                        value[4],
                        value[5]
                    )
                }
            }
            return constructor
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}