package lin.abcdq.index

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import lin.abcdq.index.utils.CAO
import lin.abcdq.index.utils.Permission
import lin.abcdq.index.utils.PermissionHandler
import lin.abcdq.lib_index.hook.IBinderInvoke
import lin.abcdq.lib_index.hook.IStartActivity
import lin.abcdq.lib_index.hook.JReflection
import lin.abcdq.lib_index.inject.InjectDex
import lin.abcdq.lib_index.shadow.ShadowPlugin
import lin.abcdq.lib_index.shadow.model.IBundle
import java.io.File


class MainActivity : AppCompatActivity() {

    //inject dexElement need API < Android P
    private val mButtonInject: Button by lazy { findViewById(R.id.bt_inject) }
    private val mButtonTest: Button by lazy { findViewById(R.id.bt_test) }
    private val mButtonMethod: Button by lazy { findViewById(R.id.bt_method) }

    private val dexPath by lazy { "${obbDir.absolutePath}/dex" }
    private val dexOutPath by lazy { "${obbDir.absolutePath}/dexOut" }

    //hook startActivity need API < Android P
    private val mButtonHookStartActivity: Button by lazy { findViewById(R.id.bt_hook_start_activity) }
    private val mButtonHookStartContext: Button by lazy { findViewById(R.id.bt_hook_start_context) }
    private val mButtonStartActivity: Button by lazy { findViewById(R.id.bt_start) }

    //hook binder need API < Android P
    private val mButtonHookBinder: Button by lazy { findViewById(R.id.bt_binder) }

    //no hook start Plugin Activity
    private val mButtonStartActivityNoHook: Button by lazy { findViewById(R.id.bt_start_activity_no_hook) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Permission.request(this, object : PermissionHandler.PermissionsResponse {
            override fun response(all: Boolean) {
                val folder = File(dexPath)
                if (!folder.exists()) folder.mkdirs()
                CAO.copyAssetsDirToSDCard(this@MainActivity, "dex", obbDir.absolutePath)
            }
        })
        //inject dexElement
        mButtonInject.setOnClickListener { InjectDex.injectDex(dexPath, dexOutPath) }
        mButtonTest.setOnClickListener { DOOO().tttt(this@MainActivity) }
        mButtonMethod.setOnClickListener {
            val listener = View.OnClickListener {
                Toast.makeText(this@MainActivity, "???????????", Toast.LENGTH_SHORT)
                    .show()
            }
            JReflection.methodViewClick(
                mButtonTest,
                View::class.java,
                "setOnClickListener",
                listener
            )
        }
        //hook startActivity
        mButtonHookStartActivity.setOnClickListener {
            IStartActivity().hookInstrumentation1(
                this,
                EmptyActivity::class.java.name,
                ProxyActivity::class.java.name
            )
            val intent = Intent(this, EmptyActivity::class.java)
            startActivity(intent)
        }
        mButtonHookStartContext.setOnClickListener {
            IStartActivity().hookInstrumentation2(
                applicationContext,
                EmptyActivity::class.java.name,
                ProxyActivity::class.java.name
            )
            val intent = Intent(applicationContext, EmptyActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(intent)
        }
        mButtonStartActivity.setOnClickListener {
            startActivity(Intent(this, EmptyActivity::class.java))
        }
        //hook binder
        mButtonHookBinder.setOnClickListener {
            IBinderInvoke().hookBinder("clipboard", "android.content.IClipboard")
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val data = ClipData.newPlainText("Label", "iBinder hook hook")
            cm.setPrimaryClip(data)
        }
        //start plugin activity no hook
        mButtonStartActivityNoHook.setOnClickListener {
            val iBundle = IBundle(
                this,
                Class.forName("lin.abcdq.lib_index.shadow.test.TestActivity")
            )
            ShadowPlugin.enter(iBundle)
        }
    }
}