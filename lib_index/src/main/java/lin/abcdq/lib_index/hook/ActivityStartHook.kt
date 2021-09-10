package lin.abcdq.lib_index.hook

import android.app.Activity
import android.app.Instrumentation
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder

class ActivityStartHook {

    /**
     * 流程
     * Instrumentation() ->
     * 1. execStartActivity()(用占用的空Activity验证) -> EmptyActivity (Registered in AndroidManifest.xml) ->
     * 2 .newActivity()(通过验证后替换Activity) -> ProxyActivity (Not Registered in AndroidManifest.xml)
     */
    fun hookActivityStart(context: Activity, emptyClassName: String, proxyClassName: String) {
        val mainThread = JReflection.field(context, Activity::class.java, "mMainThread")
        val activityThreadClazz = JReflection.clazz("android.app.ActivityThread")
        //instrument
        val baseIns = JReflection.field(mainThread!!, activityThreadClazz, "mInstrumentation")
        val ins = ActivityStartInstrumentation(
            baseIns as Instrumentation, context,
            emptyClassName, proxyClassName
        )
        JReflection.field(mainThread, activityThreadClazz, "mInstrumentation", ins)
    }

    fun hookActivityStartContext(
        context: Context,
        emptyClassName: String,
        proxyClassName: String
    ) {
        val activityThreadClazz = JReflection.clazz("android.app.ActivityThread")
        val mainThread =
            JReflection.method(null, activityThreadClazz, "currentActivityThread")
        //instrument
        val baseIns = JReflection.field(mainThread!!, activityThreadClazz, "mInstrumentation")
        val ins = ActivityStartInstrumentation(
            baseIns as Instrumentation, context,
            emptyClassName, proxyClassName
        )
        JReflection.field(mainThread, activityThreadClazz, "mInstrumentation", ins)
    }

    class ActivityStartInstrumentation(
        instrumentation: Instrumentation,
        context: Context,
        holderActClassName: String,
        targetActClassName: String
    ) : Instrumentation() {

        private val mBaseInstrumentation: Instrumentation = instrumentation
        private val mContext: Context = context
        private val mHolderClassName: String = holderActClassName
        private val mTargetClassName: String = targetActClassName

        fun execStartActivity(
            who: Context, contextThread: IBinder, token: IBinder, target: Activity,
            intent: Intent, requestCode: Int, options: Bundle
        ): ActivityResult? {
            try {
                intent.component = ComponentName(mContext, mHolderClassName)
                val method = JReflection.method(
                    mBaseInstrumentation,
                    Activity::class.java,
                    "execStartActivity",
                    who,
                    contextThread,
                    token,
                    target,
                    intent,
                    requestCode,
                    options
                )
                return (method as ActivityResult)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun newActivity(cl: ClassLoader, className: String, intent: Intent): Activity {
            intent.component = ComponentName(mContext, mTargetClassName)
            return super.newActivity(cl, mTargetClassName, intent)
        }
    }
}