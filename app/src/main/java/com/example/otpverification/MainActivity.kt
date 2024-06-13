package com.example.otpverification


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.otpverification.databinding.ActivityMainBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider

import com.hbb20.CountryCodePicker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }
    private lateinit var activityMainBinding: ActivityMainBinding
    private var callbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks ?= null
    private lateinit var authToken : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        authToken = FirebaseAuth.getInstance()




        activityMainBinding.etVerifyMobileNumber.setOnClickListener {
            if ( activityMainBinding.etMobileNumber.text.toString().trim().isEmpty()){
                Toast.makeText(this@MainActivity,"Enter Your Mobile Number" ,Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else {

                selectedOtp()
            }

        }






    }

    private fun selectedOtp() {

        otpSend()


        activityMainBinding.pbProgressBar.visibility = View.VISIBLE
        activityMainBinding.etVerifyMobileNumber.visibility = View.INVISIBLE

        val options = callbacks?.let {
            PhoneAuthOptions.newBuilder(authToken)
                .setPhoneNumber("+91" + activityMainBinding.etMobileNumber.text.toString().trim())
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(it)
                .build()
        }
        options?.let { PhoneAuthProvider.verifyPhoneNumber(it) }





    }


    private fun otpSend() {


        activityMainBinding.pbProgressBar.visibility = View.GONE
        activityMainBinding.etVerifyMobileNumber.visibility = View.VISIBLE


        Log.d(TAG, " otpSend >>> 1$callbacks")

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:$credential")
                activityMainBinding.pbProgressBar.visibility = View.GONE
                activityMainBinding.etVerifyMobileNumber.visibility = View.VISIBLE

            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(TAG, "onVerificationFailed", e)
                activityMainBinding.pbProgressBar.visibility = View.GONE
                activityMainBinding.etVerifyMobileNumber.visibility = View.VISIBLE

                Toast.makeText(this@MainActivity,e.message ,Toast.LENGTH_SHORT).show()

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                activityMainBinding.pbProgressBar.visibility = View.VISIBLE
                activityMainBinding.etVerifyMobileNumber.visibility = View.GONE

                Log.d(TAG, "onCodeSent:$verificationId")
                Toast.makeText(this@MainActivity," OTP is Successfully Send." ,Toast.LENGTH_SHORT).show()

                val intent = Intent(this@MainActivity ,VerifyActivity::class.java)
                intent.putExtra("mobileNumber" ,activityMainBinding.etMobileNumber.text.toString())
                intent.putExtra("verificationId" ,verificationId)
                startActivity(intent)
            }
        }

    }

}