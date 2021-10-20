package com.example.terrorcellar.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.terrorcellar.LandingPageActivity
import com.example.terrorcellar.LoginActivity
import com.example.terrorcellar.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    private lateinit var mView: View

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        mView = view

        val registrationBtn: Button = view.findViewById(R.id.register_btn)
        val loginBtn: Button = view.findViewById(R.id.login_btn)
        val forgotPwBtn: Button = view.findViewById(R.id.forgot_pw_btn)
        val googleSignInBtn: Button = view.findViewById(R.id.google_signin_btn)

        //Registration Button Configuration
        registrationBtn.setOnClickListener {
            (activity as LoginActivity?)!!.showSignupFragemnt()
        }

        //Login Button Configuration
        loginBtn.setOnClickListener {
            logIn()
        }

        //Forgot Password Button Configuration
        forgotPwBtn.setOnClickListener {
            (activity as LoginActivity?)!!.showForgotPwFragment()
        }

        //Google Sign In Configuration
        googleSignInBtn.setOnClickListener {
            googleLogin()
        }
    }

    private fun googleLogin() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.webclient_id))
            .requestEmail()
            .build()
        val signInClient = GoogleSignIn.getClient(requireActivity(), gso)
        signInClient.signInIntent.also {
            startActivityForResult(it, com.example.terrorcellar.RC_SIGN_IN)
        }
    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount){
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        CoroutineScope(Dispatchers.IO).launch {
            try{
                auth.signInWithCredential(credentials)
                withContext(Dispatchers.Main){
                    Toast.makeText(activity, "Successfully logged in!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(activity, LandingPageActivity::class.java))
                    activity?.finish()
                }
            }catch(e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == com.example.terrorcellar.RC_SIGN_IN){
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let {
                googleAuthForFirebase(it)
            }
        }
    }

    private fun updateUI(currentUser: FirebaseUser?){
        if(currentUser != null){
                startActivity(Intent(activity, LandingPageActivity::class.java))
                activity?.finish()
        } else{
            Toast.makeText(activity, "Login failed. Incorrect email address or password",
                Toast.LENGTH_SHORT).show()
        }
    }

    fun logIn(){
        val email: EditText = mView.findViewById(R.id.login_username)
        val password: EditText = mView.findViewById(R.id.login_password)
        if(email.toString().isEmpty()){
            Toast.makeText(activity, "Please enter email.", Toast.LENGTH_SHORT).show()
            email.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            Toast.makeText(activity, "Please enter valid email.", Toast.LENGTH_SHORT).show()
            email.requestFocus()
            return
        }
        if(password.text.toString().isEmpty()) {
            Toast.makeText(activity, "Please enter password.", Toast.LENGTH_SHORT).show()
            password.requestFocus()
            return
        }
        if(password.text.toString().length<8){
            Toast.makeText(activity, "Password length invalid, must be at least 8 Characters. Please re-enter", Toast.LENGTH_SHORT).show()
            password.requestFocus()
            return
        }
        activity?.let {
            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        updateUI(null)
                    }
                }
        }
    }


}