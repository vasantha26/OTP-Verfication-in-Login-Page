package com.example.otpverification

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.otpverification.databinding.ActivityVerifyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider

class  VerifyActivity : AppCompatActivity() {

    private lateinit var verifyOtpBinding: ActivityVerifyBinding

    private lateinit var mAuth: FirebaseAuth

    companion object {
        private const val TAG = "VerifyActivity"
        private var verificationId: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verifyOtpBinding = DataBindingUtil.setContentView(this, R.layout.activity_verify)

        mAuth = FirebaseAuth.getInstance()

        val mobile = intent.getStringExtra("mobileNumber")
        verificationId = intent.getStringExtra("verificationId")


        verifyOtpBinding.tvMobileNumberPass.text = String.format("+ 91 -%s", mobile)

        setupOtpInputs()

    }

    private fun setupOtpInputs() {

        verifyOtpBinding.inputCodeOne.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    verifyOtpBinding.inputCodeTwo.requestFocus()
                }
            }
        })

        verifyOtpBinding.inputCodeTwo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    verifyOtpBinding.inputCodeThree.requestFocus()
                }
            }
        })


        verifyOtpBinding.inputCodeThree.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    verifyOtpBinding.inputCodeFour.requestFocus()
                }
            }
        })

        verifyOtpBinding.inputCodeFour.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    verifyOtpBinding.inputCodeFive.requestFocus()
                }
            }
        })

        verifyOtpBinding.inputCodeFive.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    verifyOtpBinding.inputCodeSix.requestFocus()
                }
            }
        })

        verifyOtpBinding.tvResendButton.setOnClickListener {
            Toast.makeText(this@VerifyActivity, "OTP Send Successfully", Toast.LENGTH_SHORT).show()
        }

        verificationClick()


    }

    private fun verificationClick() {




        verifyOtpBinding.bvVerify.setOnClickListener {
            verifyOtpBinding.pbVerifyProgressBar.visibility = View.VISIBLE
            verifyOtpBinding.bvVerify.visibility = View.INVISIBLE

            if (verifyOtpBinding.inputCodeOne.text.toString().trim().isEmpty() ||
                verifyOtpBinding.inputCodeTwo.text.toString().trim().isEmpty() ||
                verifyOtpBinding.inputCodeThree.text.toString().trim().isEmpty() ||
                verifyOtpBinding.inputCodeFour.text.toString().trim().isEmpty() ||
                verifyOtpBinding.inputCodeFive.text.toString().trim().isEmpty() ||
                verifyOtpBinding.inputCodeSix.text.toString().trim().isEmpty()
            ) {
                Toast.makeText(this@VerifyActivity, "OTP is not Valid ", Toast.LENGTH_SHORT)
                    .show()

            } else {

                if (verificationId != null) {
                    val code = verifyOtpBinding.inputCodeOne.text.toString().trim() +
                            verifyOtpBinding.inputCodeTwo.text.toString().trim() +
                            verifyOtpBinding.inputCodeThree.text.toString().trim() +
                            verifyOtpBinding.inputCodeFour.text.toString().trim() +
                            verifyOtpBinding.inputCodeFive.text.toString().trim() +
                            verifyOtpBinding.inputCodeSix.text.toString().trim()

                    val credential =
                        verificationId?.let { PhoneAuthProvider.getCredential(it, code) }

                    credential?.let { it1 ->
                        FirebaseAuth.getInstance().signInWithCredential(it1)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {

                                    verifyOtpBinding.pbVerifyProgressBar.visibility = View.VISIBLE
                                    verifyOtpBinding.bvVerify.visibility = View.INVISIBLE

                                    Toast.makeText(
                                        this@VerifyActivity,
                                        " Welcome ...",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent =
                                        Intent(this@VerifyActivity, MainPageActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    Log.d(TAG, "signInWithCredential:success")

                                } else {

                                    verifyOtpBinding.pbVerifyProgressBar.visibility = View.GONE
                                    verifyOtpBinding.bvVerify.visibility = View.VISIBLE

                                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                                    Toast.makeText(
                                        this@VerifyActivity,
                                        "OTP is not Valid ",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                }
            }
        }
    }
}