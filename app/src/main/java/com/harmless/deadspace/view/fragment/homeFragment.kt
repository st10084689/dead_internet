package com.harmless.deadspace.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.harmless.deadspace.R
import com.harmless.deadspace.databinding.FragmentHomeBinding
import android.view.WindowManager
import androidx.core.content.ContextCompat.getSystemService
import com.harmless.deadspace.model.Users
import com.harmless.deadspace.view.activity.MatchFindingActivity
import kotlin.random.Random



class homeFragment : Fragment() {

    private lateinit var ranking : TextView
    private lateinit var startButton: ImageView

    private lateinit var adView: AdView

    private lateinit var binding: FragmentHomeBinding

    companion object{
        private val TAG = "homeFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        MobileAds.initialize(requireContext()) {}
        init(view)
        return view

    }

    private fun init(view:View){
        loadBannerAd()

        //going to the match finding section
        binding.startBtn.setOnClickListener{
            val toFindingMatch = Intent(view.context, MatchFindingActivity::class.java)
            startActivity(toFindingMatch)
        }
    }

//    private fun matchmaking(currentUser: Users, allUsers: List<Users>): Users?{
//            val filteredUsers = allUsers.filter { user -> user.userId != currentUser.userId }
//            return if (filteredUsers.isNotEmpty()) {
//                val randomIndex = Random.nextInt(filteredUsers.size)
//                filteredUsers[randomIndex]
//            } else {
//                null
//            }
//    }

    private fun loadBannerAd() {
        // Create a new ad view.
        val adView = AdView(requireContext())

        // Add the ad view to the container
        val adSize = AdSize(300, 250)
        adView.setAdSize(adSize)

        adView.adUnitId = "ca-app-pub-5672195872456028/5294132586"
        binding.adViewContainer.addView(adView)
        // Create an ad request.
        val adRequest = AdRequest.Builder().build()

        // Start loading the ad in the background.
        adView.loadAd(adRequest)

        adView.adListener = object: AdListener(){
            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                Log.d(TAG, "onAdFailedToLoad: failed+ $adError")
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }


    }
}