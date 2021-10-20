package com.example.terrorcellar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.terrorcellar.LoginActivity
import com.example.terrorcellar.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordFragment : Fragment() {
  private lateinit var auth: FirebaseAuth

  companion object{
      fun newInstance(): ForgotPasswordFragment{
          return ForgotPasswordFragment()
      }
  }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val resetPwBtn: Button = view.findViewById(R.id.reset_pw_btn)
        resetPwBtn.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword() {
        val email: String = view?.findViewById<EditText>(R.id.forgot_pw_et)?.text.toString().trim{it <= ' '}

        if(email.isEmpty()){
          Toast.makeText(activity, "Please enter email address.", Toast.LENGTH_SHORT).show()
        }else{
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        Toast.makeText(activity, "Email sent successfully to reset your password!",
                        Toast.LENGTH_SHORT).show()
                        (activity as LoginActivity?)!!.showLoginFragemnt()
                    }else{
                        Toast.makeText(activity, task.exception!!.message.toString(),
                        Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}