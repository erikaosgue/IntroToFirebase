package com.erikaosgue.introtokotlinfirebase.SignInAndSignOutWithGoogle

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.erikaosgue.introtokotlinfirebase.R
import com.erikaosgue.introtokotlinfirebase.databinding.ActivitySinginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

//Each activity contains a different exercise
//In order to run each onw just uncomment in the manifest
//the activity you want to run


class SignInActivity : AppCompatActivity() {


    val RC_SIGN_IN: Int = 1

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    lateinit var firebaseAuth: FirebaseAuth


    lateinit var actSignInBinding: ActivitySinginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actSignInBinding = ActivitySinginBinding.inflate(layoutInflater)
        setContentView(actSignInBinding.root)

        configureGoogleSignIn()
        setupUI()
        firebaseAuth = FirebaseAuth.getInstance()

    }

    // Return an Intent to open the SingInActivity via Intent
    // This companion object will be call from the HomeActivity
    companion object {
        fun getLaunchIntent(from: Context): Intent {
            return Intent(from, SignInActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        }
    }


    // To avoid the need for the user to sign in every time the app is launched
    // we can check if the user is signed in already.
    // This can be achieved by checking if the current user is already signed in
    // from within the onStart() method:
    override fun onStart() {

        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            Toast.makeText(this, "Successfully User sing-in: $currentUser", Toast.LENGTH_LONG)
                .show()
            // getLaunchIntent is the companion Object that returns an Intent
            startActivity(HomeActivity.newStartIntent(this))
            finish()
        } else {
            Toast.makeText(this, "user is not sing-in", Toast.LENGTH_LONG).show()

        }
    }


    // Make the configuration to SignIn with GOOGLE
    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)

    }

    // SetUp the click listener for the sign in button
    private fun setupUI() {
        actSignInBinding.googleButton.setOnClickListener {
            signIn()
        }
    }

    // signIn creates an Intent that will redirect to google Services to SignIn
    // the googleClient will signIn using the getSignInIntent public method
    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent

        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    //This method will receive the data From Google Services Authentication when doing the SingIn
    // then the firebase Authentication will begin if the account from google is not null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                println("account $account")
                if (account != null) {
                    firebaseAuthWithGoogle(account)
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }


    //Initialize Firebase Authentication with google
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {

        // GoogleAuthProvider will use The idToken from GoogleSignInAccount() and
        // exchange it for a firebase Credentials,
        // the credential will be use to authenticate with firebase
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        // If it is successful the authentication with firebase
        // the user will be sent to HomeActivity
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->

            if (task.isSuccessful) {

                // getLaunchIntent is the companion Object
                startActivity(HomeActivity.newStartIntent(this))
            } else {
                Toast.makeText(this, "Google sign in failed :(", Toast.LENGTH_LONG).show()
            }
        }
    }
}