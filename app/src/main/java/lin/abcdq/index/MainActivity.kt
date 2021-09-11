package lin.abcdq.index

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import lin.abcdq.index.utils.CAO
import lin.abcdq.index.utils.Permission
import lin.abcdq.index.utils.PermissionHandler
import lin.abcdq.lib_index.hook.StartActivityHook
import lin.abcdq.lib_index.hook.JReflection
import lin.abcdq.lib_index.inject.InjectDex
import java.io.File


class MainActivity : AppCompatActivity() {

    private val mButtonInject: Button by lazy { findViewById(R.id.bt_inject) }
    private val mButtonTest: Button by lazy { findViewById(R.id.bt_test) }
    private val mButtonMethod: Button by lazy { findViewById(R.id.bt_method) }

    private val mButtonHookStartActivity: Button by lazy { findViewById(R.id.bt_hook_start_activity) }
    private val mButtonHookStartContext: Button by lazy { findViewById(R.id.bt_hook_start_context) }
    private val mButtonStartActivity: Button by lazy { findViewById(R.id.bt_tart) }

    private val dexPath by lazy { "${obbDir.absolutePath}/dex" }
    private val dexOutPath by lazy { "${obbDir.absolutePath}/dexOut" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Permission.request(this, object : PermissionHandler.PermissionsResponse {
            override fun response(all: Boolean) {
                val folder = File(dexPath)
                if (!folder.exists()) folder.mkdirs()
                CAO.copyAssetsDirToSDCard(this@MainActivity, "dex", obbDir.absolutePath)
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
            }
        })
        mButtonHookStartActivity.setOnClickListener {
            StartActivityHook().hookActivityStartContextImpl1(
                this,
                EmptyActivity::class.java.name,
                ProxyActivity::class.java.name
            )
            val intent = Intent(this, EmptyActivity::class.java)
            startActivity(intent)
        }
        mButtonHookStartContext.setOnClickListener {
            StartActivityHook().hookActivityStartContextImpl2(
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
    }
}