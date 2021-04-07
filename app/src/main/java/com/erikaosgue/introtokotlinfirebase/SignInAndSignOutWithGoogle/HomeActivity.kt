package com.erikaosgue.introtokotlinfirebase.SignInAndSignOutWithGoogle

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.erikaosgue.introtokotlinfirebase.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

//Each activity contains a different exercise
//In order to run each onw just uncomment in the manifest
//the activity you want to run


class HomeActivity : AppCompatActivity() {

    // This companion object will be call from the SigIn Activity
    companion object {
        fun newStartIntent(from: Context) = Intent(from, HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    lateinit var actiHomeBinding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actiHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(actiHomeBinding.root)

        setupUI()
    }

    // Set up the click listener for the sign Out button
    private fun setupUI() {
        actiHomeBinding.signOutButtonId.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        startActivity(SignInActivity.getLaunchIntent(this))
        // SignOut from the application
        FirebaseAuth.getInstance().signOut();
    }

}