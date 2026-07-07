// tv/.../MainActivity.kt
package mx.utng.smarthealthmonitor.tv

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/**
 * MainActivity del módulo Android TV.
 * Extiende FragmentActivity (requerido por Leanback).
 * El Fragment principal es MainFragment con BrowseSupportFragment.
 * Ejercicio 01 — S11.
 */
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_browse_fragment, MainFragment())
                .commit()
        }
    }
}
