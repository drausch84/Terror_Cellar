package com.example.terrorcellar.fragments

import android.os.Bundle
import android.util.Patterns
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


class SignUpFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    companion object {
        fun newInstance(): SignUpFragment {
            return SignUpFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val signupBtn: Button = view.findViewById(R.id.signup_btn)
        signupBtn.setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser() {
        val email: EditText = requireView().findViewById(R.id.signup_email)
        val password: EditText = requireView().findViewById(R.id.signup_password)

        if (email.toString().isEmpty()) {
            Toast.makeText(activity, "Please enter an email address", Toast.LENGTH_SHORT).show()
            email.error = "Please enter email"
            email.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            Toast.makeText(activity, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            email.error = "Please enter valid email"
            email.requestFocus()
            return
        }

        if (password.text.toString().isEmpty()) {
            Toast.makeText(activity, "Please enter a password", Toast.LENGTH_SHORT).show()
            password.error = "Please enter password"
            password.requestFocus()
            return
        }

        if (password.length() < 8) {
            Toast.makeText(activity, "Min password length must be at least 8 characters!", Toast.LENGTH_SHORT).show()
            password.error = "Min password length should be 8 characters!"
            password.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                        (activity as LoginActivity?)!!.showLoginFragemnt()
                        Toast.makeText(context,"Registration Complete.",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        context, "Registration Failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}