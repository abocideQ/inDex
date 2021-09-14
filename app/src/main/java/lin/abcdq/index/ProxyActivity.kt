package lin.abcdq.index

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class ProxyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proxy)
        Toast.makeText(this, "ProxyActivity onCreate", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this, "ProxyActivity onResume", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(this, "ProxyActivity onPause", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        Toast.makeText(this, "ProxyActivity onStop", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "ProxyActivity onDestroy", Toast.LENGTH_SHORT).show()
    }
}