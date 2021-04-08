package com.erikaosgue.introtokotlinfirebase.ExampleSingInWithEmail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.erikaosgue.introtokotlinfirebase.databinding.ActivityMainSignInBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

//Each activity contains a different exercise
//In order to run each onw just uncomment in the manifest
//the activity you want to run

class MainSignInActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    lateinit var currentUser: FirebaseUser

    lateinit var  actMainSingInBinding: ActivityMainSignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actMainSingInBinding = ActivityMainSignInBinding.inflate(layoutInflater)
        setContentView(actMainSingInBinding.root)


        mAuth = FirebaseAuth.getInstance()

        setupUI()
        sigInUser()

    }

    private fun setupUI() {

        actMainSingInBinding.buttonCreateAccount.setOnClickListener {
            //Create a new user
            val email = actMainSingInBinding.email.text.toString().trim()
            val password = actMainSingInBinding.passwordId.text.toString().trim()

            createNewUser(email, password)


        }
    }
    private  fun createNewUser(email: String, password: String) {

        mAuth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this)
            { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    var user: FirebaseUser = mAuth!!.currentUser
                    Log.d("User: ", user.email.toString())
                    Toast.makeText(this, "Success account Created! ${user.email}", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Fail to create account: ${task.exception}", Toast.LENGTH_LONG).show()
                    Log.d("Error: ", task.exception.toString())
                }
            }
    }

    private fun sigInUser(){
        //Sing in Existing User
        val user = "userexample@example.com"
        val pass = "example"
        mAuth?.signInWithEmailAndPassword(user, pass)?.addOnCompleteListener {
                task: Task<AuthResult> ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Sign in Successful", Toast.LENGTH_LONG).show()

            } else {
                Toast.makeText(this, "Can't Sign in", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        currentUser = mAuth!!.currentUser

        if (currentUser != null) {
            Toast.makeText(this, "Singin", Toast.LENGTH_LONG).show()
        }
        else {
            Toast.makeText(this, "can't Singin", Toast.LENGTH_LONG).show()

        }
    }
}