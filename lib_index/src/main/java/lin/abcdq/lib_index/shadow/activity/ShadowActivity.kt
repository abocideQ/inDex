package lin.abcdq.lib_index.shadow.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.View

abstract class ShadowActivity : ShadowContextWrapper() {

    private var mContentView: View? = null

    protected fun onCreate(context: Context, contentView: Int, inflated: Inflater) {
        super.context = context
        super.onCreate()
        runOnThread {
            mContentView = LayoutInflater.from(context).inflate(contentView, null)
            runOnMain { inflated.inflated(mContentView!!) }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}