package lin.abcdq.lib_index.shadow.activity

interface ShadowContext {
    fun onCreate()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onDestroy()
}