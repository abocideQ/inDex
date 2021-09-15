package lin.abcdq.lib_index.shadow.model

import android.content.Context
import lin.abcdq.lib_index.shadow.ShadowPlugin

class IBundle(val context: Context, val clsPack: String) {

    var cls: Class<out Any>? = null

    fun enter() {
        ShadowPlugin.enter(this)
    }
}