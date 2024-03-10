package com.harmless.deadspace.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.ads.MobileAds
import com.harmless.deadspace.R
import com.harmless.deadspace.databinding.ActivityMainBinding
import com.harmless.deadspace.view.fragment.homeFragment

class MainActivity : AppCompatActivity() {
private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        fragmentManager()
        init()
    }

    private fun init(){
        //initialising the ads in the main view
        MobileAds.initialize(this) {}
    }

    private fun fragmentManager(){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val homeFragment = homeFragment()
        fragmentTransaction.add(R.id.mainFragment, homeFragment)
        fragmentTransaction.commit()
    }
}