package lin.abcdq.lib_index.shadow.test

import android.content.Context
import lin.abcdq.lib_index.R
import lin.abcdq.lib_index.shadow.activity.Inflater
import lin.abcdq.lib_index.shadow.activity.ShadowActivity

class TestActivity : ShadowActivity() {

    override fun onCreate(context: Context, inflated: Inflater) {
        super.onCreate(context, R.layout.activity_test, inflated)
    }

}