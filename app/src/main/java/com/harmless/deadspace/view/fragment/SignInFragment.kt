package com.harmless.deadspace.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.harmless.deadspace.R
import com.harmless.deadspace.databinding.FragmentSignInBinding
import com.harmless.deadspace.view.activity.MainActivity

class SignInFragment : Fragment() {

    private lateinit var auth: FirebaseAuth;

    private lateinit var binding:FragmentSignInBinding

    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var confirmPassword:EditText

    companion object{
        private const val TAG="SignInFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        init()
        return binding.root
    }

    private fun init(){
        auth = Firebase.auth

        val signInBtn = binding.loginBtn
        signInBtn.setOnClickListener {
            val registerFragment = LoginFragment()

            parentFragmentManager.beginTransaction()
                .replace(R.id.RegisterContainer, registerFragment)
                .addToBackStack(null)
                .commit()
        }

        email = binding.emailEditText
        password = binding.passwordEditText
        confirmPassword = binding.confirmPasswordEditText

        val signUpButton = binding.signInBtn
        signUpButton.setOnClickListener {
            if(validations()){
                SignUpUser()
            }
        }
    }

        private fun validations():Boolean{
            var isValid = true
            if(email.text.isEmpty()){
                isValid = false
            }
            if(password.text.isEmpty()){
                isValid = false
            }
            if(confirmPassword.text.isEmpty()){
                isValid = false

            }
            else{

                if(confirmPassword.text.toString() != password.text.toString()){
                    isValid = false
                    confirmPassword.setText("")
                    Toast.makeText(requireContext(), "passwords do not match", Toast.LENGTH_SHORT).show()


                }
            }

            return isValid
        }


    private fun updateUI(user: FirebaseUser?) {
        if(user != null){
            val toMainMenu = Intent(requireContext(), MainActivity::class.java)
            startActivity(toMainMenu)
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

fun SignUpUser() {
    Log.d(TAG, "SignUpUser: Working")
    try {
        val email = email.text.toString().trim()
        val password = password.text.toString().trim()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity(), OnCompleteListener { task ->
                Log.d(TAG, "onComplete: create")
                if (task.isSuccessful) {
                    Log.d(TAG, "is successful")
                    val toMainView = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(toMainView)

                } else {
                    Toast.makeText(requireActivity(), "Login failed", Toast.LENGTH_LONG)
                        .show()
                }
            })

    } catch (e: Exception) {
        Toast.makeText(requireContext(), "error", Toast.LENGTH_LONG).show()
    }
}

}
