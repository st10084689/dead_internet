package com.harmless.deadspace.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.harmless.deadspace.R
import com.harmless.deadspace.databinding.ActivityAccountBinding
import com.harmless.deadspace.view.fragment.LoginFragment

private lateinit var binding: ActivityAccountBinding

private lateinit var mAuth:FirebaseAuth
class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
        fragmentManager()

        mAuth = FirebaseAuth.getInstance()

        val currentUser = mAuth.currentUser

        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun fragmentManager(){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val accountFragment = LoginFragment()
        fragmentTransaction.add(R.id.RegisterContainer, accountFragment)
        fragmentTransaction.commit()
    }
}