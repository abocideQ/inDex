package lin.abcdq.lib_index.shadow.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import lin.abcdq.lib_index.shadow.model.IBundle

internal class ShadowHostActivity : AppCompatActivity() {

    companion object {

        private const val TAG_ActivityCls = "activityClass"

        fun startActivity(bundle: IBundle) {
            val intent = Intent(bundle.context, ShadowHostActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(TAG_ActivityCls, bundle.cls)
            bundle.context.startActivity(intent)
        }
    }

    private var mShadow: ShadowActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val clazz = (intent.getSerializableExtra(TAG_ActivityCls) ?: return) as Class<out Any>
        mShadow = clazz.newInstance() as ShadowActivity
        mShadow?.onCreate(this, object : Inflater {
            override fun inflated(view: View) {
                setContentView(view)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        mShadow?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mShadow?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mShadow?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mShadow?.onDestroy()
    }

}