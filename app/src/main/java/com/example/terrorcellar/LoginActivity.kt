package com.example.terrorcellar

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.terrorcellar.fragments.ForgotPasswordFragment
import com.example.terrorcellar.fragments.LoginFragment
import com.example.terrorcellar.fragments.SignUpFragment
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


const val RC_SIGN_IN = 0

class LoginActivity : AppCompatActivity() {
    private lateinit var actionBar: ActionBar
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login1)

        //configure actionbar
        actionBar = supportActionBar!!
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setBackgroundDrawable(ColorDrawable(Color.parseColor("#000000")))
        actionBar?.hide()

        if( savedInstanceState == null ) {
            showLoginFragemnt()
        }
    }

    private fun checkUser() {
        //check if user is logged in
        val firebaseUser = auth.currentUser
        if(firebaseUser != null){
            //User is already logged in
            startActivity(Intent(this, LandingPageActivity::class.java ))
            finish()
        }
    }

    fun showLoginFragemnt() {
        val fragment = LoginFragment.newInstance()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()

        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)

        transaction.commit()

        actionBar?.hide()

    }

    fun showSignupFragemnt() {
        val fragment = SignUpFragment.newInstance()

        val manager = supportFragmentManager

        val transaction = manager.beginTransaction()

        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)

        transaction.commit()

        actionBar?.show()

    }

    fun showForgotPwFragment(){
        val fragment = ForgotPasswordFragment.newInstance()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()

        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)

        transaction.commit()

        actionBar?.show()
    }
    public override fun onStart() {
        super.onStart()
    }

    private fun updateUI(currentUser: FirebaseUser?){
        if(currentUser != null){
                startActivity(Intent(this, LandingPageActivity::class.java))
                finish()
        } else{
            Toast.makeText(baseContext, "Login failed.",
                Toast.LENGTH_SHORT).show()
        }
    }

    fun logIn(){
        val email: EditText = findViewById(R.id.login_username)
        val password: EditText = findViewById(R.id.login_password)
        if(email.toString().isEmpty()){
            email.error = "Please enter email"
            email.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            email.error = "Please enter valid email"
            email.requestFocus()
            return
        }
        if(password.text.toString().isEmpty()) {
            password.error = "Please enter password"
            password.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    updateUI(null)
                }
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        showLoginFragemnt()
        return true
    }
}