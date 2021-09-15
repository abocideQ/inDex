package lin.abcdq.lib_index.shadow

import lin.abcdq.lib_index.shadow.activity.ShadowHostActivity
import lin.abcdq.lib_index.shadow.model.IBundle

internal object ShadowPlugin {

    fun enter(bundle: IBundle) {
        bundle.cls = findPlugin(bundle.clsPack)
        ShadowHostActivity.startActivity(bundle)
    }

    private val mACaches = hashMapOf<String, Class<out Any>>()

    private fun findPlugin(clsString: String): Class<out Any>? {
        if (mACaches[clsString] != null) {
            return mACaches[clsString]!!
        }
        try {
            //try first
            val clazz = Class.forName(clsString)
            mACaches[clsString] = clazz!!
            return clazz
        } catch (e: Exception) {
            return try {
                //try second
                val apk = unZipApk()
                val dex = unZipDex(apk)
                val clazz = findClazzInDexElement(dex)
                mACaches[clsString] = clazz!!
                clazz
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun unZipApk(): List<Any> {
        //todo()!!!!!!!!!
        return arrayListOf()
    }

    private fun unZipDex(list: List<Any>): List<Any> {
        //todo()!!!!!!!!!
        return arrayListOf()
    }

    private fun findClazzInDexElement(list: List<Any>): Class<out Any>? {
        //todo()!!!!!!!!!
        return null
    }
}