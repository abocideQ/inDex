package lin.abcdq.index

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import lin.abcdq.index.utils.CAO
import lin.abcdq.index.utils.Permission
import lin.abcdq.index.utils.PermissionHandler
import lin.abcdq.lib_index.inject.InjectDex
import java.io.File

class MainActivity : AppCompatActivity() {

    private val mButtonInject: Button by lazy { findViewById(R.id.bt_inject) }
    private val mButton: Button by lazy { findViewById(R.id.bt_test) }

    private val dexPath by lazy { "${obbDir.absolutePath}/dex" }
    private val dexOutPath by lazy { "${obbDir.absolutePath}/dexOut" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val folder = File(dexPath)
        if (!folder.exists()) folder.mkdirs()
        CAO.copyAssetsDirToSDCard(this@MainActivity, "dex", obbDir.absolutePath)
        Permission.request(this, object : PermissionHandler.PermissionsResponse {
            override fun response(all: Boolean) {
                mButtonInject.setOnClickListener { InjectDex.injectDex(dexPath, dexOutPath) }
                mButton.setOnClickListener { DOOO().tttt(this@MainActivity) }
            }
        })
    }
}