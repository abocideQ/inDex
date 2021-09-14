package lin.abcdq.lib_index.hook

import android.os.IBinder
import android.os.IInterface
import android.util.Log
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * hook binder
 */
class IBinderInvoke {
    /**
     * linux Kernel内核空间 动态加载 Binder驱动作为内核模块 到内核
     *
     * Binder: Process A/B/C -> Binder Proxy A/B/C
     *
     * ServiceManager: manager of BinderProxyABC/ServerA/ServerB
     *
     * Service使用流程 :
     *
     * 1. IBinder iBinder = ServiceManager.getService("xx_service"); // 获取原始的IBinder对象
     *
     * 2. Interface iService = Interface.Stub.asInterface(iBinder); // 转换为Service接口
     *
     * 3. asInterface() -> queryLocalInterface() -> 缓存查询 -> 进程通信获取Binder代理对象
     *
     * 流程 （ServiceManager查找Service时 首先通过sCache缓存map查询,没有缓存的情况进程通信进行获取）:
     *
     * 1. 伪造 IBinder对象
     *
     * 2. 将伪造的IBinder对象 转换成Interface后 替换/插入 ServiceManager.sCache缓存map中
     */

    /**
     * example
     *
     * service: "clipboard" ,  _pack: "android.content.IClipboard"
     */
    fun hookBinder(service_name: String, service_pack: String) {
        try {
            //1. ServiceManager.getService("service")
            val serviceManagerClazz = JReflection.clazz("android.os.ServiceManager")
            val iBinder = JReflection.method(
                null,
                serviceManagerClazz,
                "getService",
                arrayOf(String::class.java),
                arrayOf(service_name)
            ) as IBinder
            //2. Interface.Stub.asInterface(iBinder) -> queryLocalInterface
            val iService = JProxy.proxy(
                serviceManagerClazz,
                InterfaceHandler(iBinder, service_pack)
            )
            //3. sCache.put(k,v)
            val sCache = (JReflection.field(
                null,
                serviceManagerClazz,
                "sCache"
            ) as Map<*, *>).toMutableMap()
            sCache[service_name] = iService
            Log.e("IBinderInvoke", "iService= $iService")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private class BinderHandler(iBinder: IBinder, service_pack: String) : InvocationHandler {

        private val mIService: Any by lazy {
            val stubClazz = JReflection.clazz("$service_pack\$Stub")
            JReflection.method(
                null,
                stubClazz,
                "asInterface",
                arrayOf(IBinder::class.java),
                arrayOf(iBinder)
            )!!
        }

        override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
            Log.e("IBinderInvoke", "BinderHandler invoke look look!!!")
            return if (args == null) method?.invoke(mIService)!!
            else method?.invoke(mIService, *args)!!
        }
    }

    private class InterfaceHandler(iBinder: IBinder, service_pack: String) :
        InvocationHandler {

        private val queryLocalMethod = "queryLocalInterface"
        private val mIBinder = iBinder
        private var mServicePack = service_pack

        override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
            Log.e("IBinderInvoke", "InterfaceHandler invoke look look!!!")
            if (queryLocalMethod == method?.name) {
                return JProxy.proxy(
                    proxy!!::class.java,
                    BinderHandler(mIBinder, mServicePack)
                )!!
            }
            return if (args == null) method?.invoke(mIBinder)!!
            else method?.invoke(mIBinder, *args)!!
        }
    }
}