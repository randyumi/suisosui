package com.randyumi.suisosui

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Vibrator
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.widget.Button
import com.nineoldandroids.animation.ArgbEvaluator
import com.nineoldandroids.animation.ValueAnimator

/**
 * 波動を放出するアクティビティ
 * 波動を出すボタンをタップするか近接センサーにより「水」を検出すると波動が放出される
 * AndroidSDKでは現在近接センサーによる水の誤検知が多いため稀によく「水」以外のものが接近しても波動が放出されてしまう。
 */
class HadoActivity : AppCompatActivity(), SensorEventListener {
    var vib: Vibrator? = null
    var sensorManager: SensorManager? = null
    var proximateSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vib = getSystemService(VIBRATOR_SERVICE) as Vibrator
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        proximateSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_second)
        val suspendButton = findViewById(R.id.suspend_button) as Button
        suspendButton.setOnClickListener { v -> finish() }
        val startButton = findViewById(R.id.hajimari) as Button
        startButton.setOnClickListener {v -> startHado()}
    }

    private fun startHado() { /* TODO もっとそれっぽい波動画面にする */
        val colorFrom = ContextCompat.getColor(applicationContext, R.color.colorRed)
        val colorTo = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
        val animation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        animation.duration = 1000/* millis */
        animation.addUpdateListener { animation ->
            findViewById(R.id.hado)?.setBackgroundColor(animation.animatedValue as Int)
        }
        animation.repeatCount = Animation.INFINITE
        animation.start()
        val densityValue = intent.getIntExtra("density", 50)
        val density = densityValue * 10L
        val interval = 50L
        vib?.vibrate(longArrayOf(200L, density, 10L, interval, 400L, density * 2), /* repeat = */0)
    }

    override fun onResume() {
        super.onResume()
        val sensors = sensorManager?.getSensorList(Sensor.TYPE_PROXIMITY)
        if (sensors != null && sensors.size > 0) {
            val s = sensors[0]
            sensorManager?.registerListener(this, s, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        vib?.cancel()
        if (sensorManager != null) {
            sensorManager?.unregisterListener(this)
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_PROXIMITY && event.values[0] < 5.0) {
            startHado()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        /* やること無いからなにもしない */
    }
}

