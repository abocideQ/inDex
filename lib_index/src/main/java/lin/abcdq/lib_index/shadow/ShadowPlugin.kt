package lin.abcdq.lib_index.shadow

import lin.abcdq.lib_index.shadow.activity.ShadowHostActivity
import lin.abcdq.lib_index.shadow.model.IBundle

object ShadowPlugin {

    private val mACaches = arrayListOf<Class<out Any>>()

    private fun unZip() {
    }

    private fun findActivity(cls: Class<out Any>) {
        if (mACaches.contains(cls)) {
            return
        }
    }

    fun enter(bundle: IBundle) {
        ShadowHostActivity.startActivity(bundle)
    }
}