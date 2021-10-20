package com.example.terrorcellar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView


class SplashScreenActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash_screen)
        val textView = findViewById<TextView>(R.id.splashText)
        val logoView = findViewById<ImageView>(R.id.terrorCellarLogo)
        logoView.alpha = 0f
        textView.alpha = 0f
        textView.animate().duration=500
        textView.animate().setDuration(4000).alpha(1f)
        textView.animate().translationX(1f)
        logoView.animate().setDuration(3000).alpha(1f)
        logoView.animate().setDuration(3000).alpha(1f).withEndAction {
            val loginPage = Intent(this, LoginActivity::class.java)
            startActivity(loginPage)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}