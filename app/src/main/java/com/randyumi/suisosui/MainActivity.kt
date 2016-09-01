package com.randyumi.suisosui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.Button
import android.widget.SeekBar

/**
 * Suisoの発生について制御し、Suiso発生画面へ遷移する画面
 */
class MainActivity : AppCompatActivity() {
    val SUISO_AVAILABLE_BATTERY_LOWER_LIMIT_PERCENT = 50
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val hadoStartButton = this.findViewById(R.id.hadoStartButton) as Button
        hadoStartButton.setOnClickListener { v ->
            val intent = Intent(applicationContext, HadoActivity::class.java)
            intent.putExtra("density", (findViewById(R.id.density) as SeekBar).progress)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        val pref = getSharedPreferences("density", MODE_PRIVATE)
        val density = findViewById(R.id.density) as SeekBar
        if (pref != null) {
            val editor = pref.edit()
            editor.putInt("density", density.progress)
            editor.apply()
        }
    }

    override fun onResume() {
        super.onResume()
        val pref = getSharedPreferences("density", MODE_PRIVATE)
        val density = findViewById(R.id.density) as SeekBar
        density.progress = pref.getInt("density", SUISO_AVAILABLE_BATTERY_LOWER_LIMIT_PERCENT)
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(BatteryBroadCastReceiver((findViewById(R.id.hadoStartButton)) as Button), filter)
    }
}

/**
 * バッテリー残量を確認し、Suisoが発生可能な状態にあるかをチェックする
 */
class BatteryBroadCastReceiver(labeledButton: Button): BroadcastReceiver() {
    val button = labeledButton
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals(Intent.ACTION_BATTERY_CHANGED)) {
            val level = intent.getIntExtra("level", 0)
            if (level < 50) {
                button.setText(R.string.dame)
            } else {
                button.setText(R.string.suiso_ok)
            }
        }
    }
}