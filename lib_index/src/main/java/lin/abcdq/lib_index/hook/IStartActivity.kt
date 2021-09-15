package lin.abcdq.lib_index.hook

import android.app.Activity
import android.app.Instrumentation
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log

/**
 * hook startActivity
 * API < Android P
 */
class IStartActivity {

    /**
     * 流程
     *
     * Instrumentation() ->
     *
     * 1. execStartActivity()(用空Activity验证) -> EmptyActivity (Registered in AndroidManifest.xml) ->
     *
     * 2. newActivity()(通过验证后替换Activity) -> ProxyActivity (Not Registered in AndroidManifest.xml)
     */

    /**
     * example
     *
     * shellClass: ShellActivity::class.java ,  _pack: TargetActivity::class.java
     */
    fun hookInstrumentation1(
        activity: Activity,
        shellClass: String,
        targetClass: String
    ) {
        //activityThread
        val mainThread = JReflection.field(activity, Activity::class.java, "mMainThread")
        val activityThreadClazz = JReflection.clazz("android.app.ActivityThread")
        //instrument
        val base = JReflection.field(mainThread!!, activityThreadClazz, "mInstrumentation")
        val instrumentation = IInstrumentation(
            base as Instrumentation,
            activity,
            shellClass,
            targetClass
        )
        JReflection.field(mainThread, activityThreadClazz, "mInstrumentation", instrumentation)
    }

    fun hookInstrumentation2(
        context: Context,
        shellClass: String,
        targetClass: String
    ) {
        //activityThread
        val activityThreadClazz = JReflection.clazz("android.app.ActivityThread")
        val mainThread =
            JReflection.method(
                null,
                activityThreadClazz,
                "currentActivityThread",
                arrayOf(),
                arrayOf()
            )
        //instrument
        val base = JReflection.field(mainThread!!, activityThreadClazz, "mInstrumentation")
        val instrumentation = IInstrumentation(
            base as Instrumentation,
            context,
            shellClass,
            targetClass
        )
        JReflection.field(mainThread, activityThreadClazz, "mInstrumentation", instrumentation)
    }

    class IInstrumentation(
        instrumentation: Instrumentation,
        context: Context,
        shellClass: String,
        targetClass: String
    ) : Instrumentation() {

        private val mInstrumentation: Instrumentation = instrumentation
        private val mContext: Context = context
        private val mShellClass: String = shellClass
        private val mTargetClass: String = targetClass
        private var mActivityCreated = false

        fun execStartActivity(
            who: Context?, contextThread: IBinder?, token: IBinder?, target: Activity?,
            intent: Intent?, requestCode: Int, options: Bundle?
        ): ActivityResult? {
            try {
                if (!mActivityCreated) {
                    intent?.component = ComponentName(mContext, mShellClass)
                }
                val method = JReflection.method(
                    mInstrumentation,
                    Instrumentation::class.java,
                    "execStartActivity",
                    arrayOf(
                        Context::class.java,
                        IBinder::class.java,
                        IBinder::class.java,
                        Activity::class.java,
                        Intent::class.java,
                        Int::class.java,
                        Bundle::class.java
                    ),
                    arrayOf(
                        who,
                        contextThread,
                        token,
                        target,
                        intent,
                        requestCode,
                        options
                    )
                )
                return if (method != null) return method as ActivityResult else null
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun newActivity(cl: ClassLoader, className: String, intent: Intent): Activity {
            val activity = if (!mActivityCreated) {
                intent.component = ComponentName(mContext, mTargetClass)
                mInstrumentation.newActivity(cl, mTargetClass, intent)
            } else {
                mInstrumentation.newActivity(cl, className, intent)
            }
            mActivityCreated = true
            return activity
        }
    }
}