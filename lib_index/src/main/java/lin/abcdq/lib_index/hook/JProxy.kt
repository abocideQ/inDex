package lin.abcdq.lib_index.hook

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

/**
 * Java 动态代理/?静态代理呢?
 */
object JProxy {

    fun proxy(obj: Any, handler: InvocationHandler): Any? {
        return Proxy.newProxyInstance(
            obj::class.java.classLoader,
            obj::class.java.interfaces
        ) { p, m, a -> handler.invoke(p, m, a) }
    }

    fun proxy(clazz: Class<out Any>, handler: InvocationHandler): Any? {
        return Proxy.newProxyInstance(
            clazz.classLoader,
            clazz.interfaces
        ) { p, m, a -> handler.invoke(p, m, a) }
    }

}