package com.erikaosgue.introtokotlinfirebase.IntroToFirebase

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.erikaosgue.introtokotlinfirebase.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

//Each activity contains a different exercise
//In order to run each onw just uncomment in the manifest
//the activity you want to run


class MainActivity : AppCompatActivity() {

//	private lateinit var firebaseAnalytics: FirebaseAnalytics
//	lateinit var mContext: Context


	lateinit var  activityMainBinding: ActivityMainBinding
 	override fun onCreate(savedInstanceState: Bundle?) {
 		super.onCreate(savedInstanceState)
 		activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
 		setContentView(activityMainBinding.root)

		// **** ADD google Analytics
		/*
		mContext = this

		firebaseAnalytics = Firebase.analytics

		val button = activityMainBinding.buttonId

		button.setOnClickListener {

			// First Option to Log events with firebaseAnalytics
			firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
				param(FirebaseAnalytics.Param.ITEM_ID, button.toString())
				println("Inside the firebase analytics")
				Toast.makeText(mContext, "Inside analytics", Toast.LENGTH_LONG).show()
			}
		}
		// Second Option to Log events with firebaseAnalytics
		val bundle = Bundle()
		bundle.putString(FirebaseAnalytics.Param.ITEM_ID, button.toString())
		firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
		*/


		// ***** Adding data to the the Database ****

		//Add a String to the Database
		//This setValue will assign the string to the hole database
		/*
		val firebaseDatabase = FirebaseDatabase.getInstance()
		val databaseRef = firebaseDatabase.reference
		databaseRef.setValue("Hello There Kotlin!")
		*/



		// **** Add another node to the Database that
		// will Reference to a Table

		val firebaseDatabase = FirebaseDatabase.getInstance()
		val databaseRef = firebaseDatabase.getReference("messages").push()
		// databaseRef.setValue("Hello There Kotlin!")

		val employee = Employee("Elanor", "Android Developer", "Cra 1 # 2-3", 31)
		databaseRef.setValue(employee)

 		//Read from the Database
		//addValueEventListener is a callBack
		databaseRef.addValueEventListener(object: ValueEventListener {

			@SuppressLint("SetTextI18n")
			override fun onDataChange(snapshot: DataSnapshot) {
				val hashMapValue = snapshot.value as HashMap<*, *>
//				println("*** age:  $age")



				activityMainBinding.objectId.text = hashMapValue.toString()
				activityMainBinding.nameId.text = "name: "  + hashMapValue["name"] as CharSequence?
				activityMainBinding.positionId.text = "position: " + hashMapValue["position"] as CharSequence?
				activityMainBinding.addressId.text = "address: " + hashMapValue["homeAddress"] as CharSequence?
				activityMainBinding.ageId.text = "age: " + hashMapValue["age"].toString()



//				Log.d("***VALUE:", value.toString())
			}

			override fun onCancelled(error: DatabaseError) {
//				Log.d("***ERROR:", error.toString())

			}

		})
 	}

	data class Employee(var name: String, var position: String, var homeAddress: String, var age: Int)

}