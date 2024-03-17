package com.harmless.deadspace.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.harmless.deadspace.R
import com.harmless.deadspace.databinding.ActivityMainBinding
import com.harmless.deadspace.databinding.ActivityMatchFindingBinding
import com.harmless.deadspace.viewModel.MessagingViewModel

class MatchFindingActivity : AppCompatActivity() {

    lateinit var binding:ActivityMatchFindingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchFindingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
       //matchmaking
        val matchMakingModel = MessagingViewModel()

        matchMakingModel.matchMaking(this)
    }
}