package lin.abcdq.lib_index.shadow.activity

import android.content.Context
import android.os.Handler
import android.os.Looper

abstract class ShadowContextWrapper : ShadowContext {

    init {
        Thread {
            Looper.prepare()
            mThread = Handler(Looper.myLooper() ?: return@Thread)
            Looper.loop()
        }.start()
    }

    private val mMainThread: Handler = Handler(Looper.getMainLooper())
    private lateinit var mThread: Handler
    protected lateinit var context: Context

    protected fun runOnThread(runnable: Runnable) {
        if (!this::mThread.isInitialized) {
            Thread {
                while (!this::mThread.isInitialized) {
                    Thread.sleep(1)
                    continue
                }
                mThread.post { runnable.run() }
            }.start()
        } else {
            mThread.post { runnable.run() }
        }
    }

    protected fun runOnMain(runnable: Runnable) {
        mMainThread.post { runnable.run() }
    }

    abstract fun onCreate(context: Context, inflated: Inflater)
    override fun onCreate() {}
    override fun onResume() {}
    override fun onPause() {}
    override fun onStop() {}
    override fun onDestroy() {
        removeLooper()
    }

    private fun removeLooper() {
        mThread.looper.quitSafely()
        mMainThread.removeCallbacksAndMessages(null)
    }

}