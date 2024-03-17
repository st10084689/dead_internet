package com.harmless.deadspace.viewModel

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.harmless.deadspace.model.Users
import com.harmless.deadspace.view.activity.ChatRoomActivity
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

class MessagingViewModel: ViewModel() {

   private fun getUsers(onComplete: (users: List<Users>, error: DatabaseError?) -> Unit){
        val databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        databaseReference.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                val users = mutableListOf<Users>()

                for (childSnapshot in snapshot.children){

                    val user = childSnapshot.getValue(Users::class.java)

                    if(user != null){
                        users.add(user)
                    }
                }
                onComplete(users, null)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(emptyList(), error)
            }
        })
    }

    fun getMyMatchId(context: Context, callback: (user: Users?, error: Exception?) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser!!.uid


        val databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(uid)
        databaseReference.get().addOnSuccessListener { dataSnapshot ->
            val user = dataSnapshot.getValue(Users::class.java)
            callback(user, null) // Pass retrieved user and null for success

            if (user != null && user.matchId.isNotEmpty()) {

                    val intent = Intent(context, ChatRoomActivity::class.java)
                    intent.putExtra("matchId", user.matchId)

                    context.startActivity(intent)
            }
        }.addOnFailureListener { error ->
            callback(null, error)
            Log.e("getMy", "Error retrieving user data: $error")
        }
    }

    fun matchMaking(context: Context){

        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser!!.uid
        val currentDateTime = Date()

            val user = Users(uid, currentUser!!.displayName!!,false,false,"",currentDateTime)
        sendUserToDatabase(user)
        getUsers { users, error ->
            if(error == null){
                Log.d("MatchMaking", "matchMaking: all the users are ass ;) follows : $users")
                val hosts = sortByHost(users)
                val currentUser = FirebaseAuth.getInstance().currentUser
                val uid = currentUser!!.uid
                val currentDateTime = Date()
                if (hosts.size >= users.size/2 ){
                    val user = Users(uid, currentUser!!.displayName!!,false,isWaiting = true,"",currentDateTime)
                    sendUserToDatabase(user)
                    getMyMatchId(context){
                        user, error ->
                    }

                }else{
                    val matchId = uid+UUID.randomUUID().toString()
                    val user = Users(uid, currentUser!!.displayName!!, true, false,matchId,currentDateTime)
                    sendUserToDatabase(user)
                    var availablePlayers = findAvailablePlayer(users)

                    availablePlayers =  sortUsersByDate(availablePlayers)

                    val pairedplayer = availablePlayers.first()
                    val pairUserAdjusted = Users(pairedplayer.userId,pairedplayer.name,pairedplayer.isHost,false,matchId,pairedplayer.date)
                    sendUserToDatabase(pairUserAdjusted)


                }

            }
            else{
                Log.e("MatchMaking", "matchMaking: Error retrieving user's ass ;): $error")
            }
        }
    }

    private fun sortUsersByDate(users: List<Users>): List<Users> {
        return users.sortedWith(compareBy { it.date })
    }
    private fun findAvailablePlayer(users: List<Users>):List<Users>{
        val filterUsers = mutableListOf<Users>()
        for(user in users){
            if(!user.isHost){
                filterUsers.add(user)
            }
        }
        return filterUsers
    }


    private fun sortByHost(users: List<Users>):List<Users>{
        val filterUsers = mutableListOf<Users>()
        for(user in users){
            if(user.isHost){
                filterUsers.add(user)
            }
        }
        return filterUsers
    }

   private fun sendUserToDatabase(user:Users) {
       Log.d("sendUserToDatabase", "Sending user: $user")
       val databaseRef = FirebaseDatabase.getInstance()
       val userRef = databaseRef.getReference("users").child(user.userId)

       userRef.setValue(user)
           .addOnSuccessListener {
               Log.d("sendUserToDatabase", "User added successfully: $user")
           }
           .addOnFailureListener { error ->
               Log.e("sendUserToDatabase", "Error adding user: $error")
           }
   }
}