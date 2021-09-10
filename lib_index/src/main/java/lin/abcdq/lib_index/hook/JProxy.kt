package lin.abcdq.lib_index.hook

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

/**
 * Java 对象代理
 */
object JProxy {

    fun proxyInterface(obj: Any, handler: InvocationHandler) {
        Proxy.newProxyInstance(
            obj::class.java.classLoader,
            obj::class.java.interfaces
        ) { p, m, a -> handler.invoke(p, m, a) }
    }

}