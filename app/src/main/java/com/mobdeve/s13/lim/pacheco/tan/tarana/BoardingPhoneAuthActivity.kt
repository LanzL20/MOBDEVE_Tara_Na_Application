package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityBoardingPhoneAuthBinding
import java.util.concurrent.TimeUnit

class BoardingPhoneAuthActivity:AppCompatActivity() {
    lateinit var phoneNumber: String
    lateinit var username: String
    lateinit var password: String
    lateinit var name: String
    val timeoutSeconds= 60L
    val mAuth: FirebaseAuth= FirebaseAuth.getInstance()

    lateinit var verificationCode: String
    lateinit var mResendToken: PhoneAuthProvider.ForceResendingToken

    lateinit var otpInput: EditText
    lateinit var signInButton: Button
    lateinit var resendTv: TextView
    lateinit var timerTv: TextView
    lateinit var progressbar: ProgressBar
    var timerFinished: Boolean= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding= ActivityBoardingPhoneAuthBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        name= intent.getStringExtra(User.NAME_KEY)!!
        phoneNumber= intent.getStringExtra(User.PHONE_NUMBER_KEY)!!
        username= intent.getStringExtra(User.USERNAME_KEY)!!
        password= intent.getStringExtra(User.PASSWORD_KEY)!!
        phoneNumber= "+63${phoneNumber.substring(1)}"
        viewBinding.phoneNumberTv.text= phoneNumber



        otpInput= viewBinding.activityPhoneAuthOtpEt
        signInButton= viewBinding.activityBoardingPhoneAuthBtnSignup
        resendTv= viewBinding.activityBoardingPhoneAuthResendTv
        timerTv= viewBinding.activityBoardingPhoneAuthTimerCountdown
        progressbar= viewBinding.progressBar2

        Log.d("BoardingPhoneAuthActivityDebugging", phoneNumber)
        sendVerificationCode(phoneNumber!!, false)

        signInButton.setOnClickListener {
            val otp= otpInput.text.toString()
            /*if(otp.isEmpty()){
                otpInput.error= "Please fill up this field"
                return@setOnClickListener
            }
            if(otp.length!=6){
                otpInput.error= "Invalid OTP"
                return@setOnClickListener
            }*/
            val credential:PhoneAuthCredential= PhoneAuthProvider.getCredential(verificationCode, otp)
            signIn(credential)
        }

        resendTv.setOnClickListener {
            sendVerificationCode(phoneNumber!!, true)
        }

    }


    fun sendVerificationCode(phoneNumber: String, isResend: Boolean) {
        startResendTimer()
        setInProgress(true)
        val callback:PhoneAuthProvider.OnVerificationStateChangedCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                //Log.d(TAG, "onVerificationCompleted:$credential")
                //signInWithPhoneAuthCredential(credential)
                signIn(credential)
                setInProgress(false)
                Log.d("BoardingPhoneAuthActivityDebugging", "Verification completed")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                //Log.w(TAG, "onVerificationFailed", e)
                //showError(e)
                Toast.makeText(this@BoardingPhoneAuthActivity, "Verification failed", Toast.LENGTH_SHORT).show()
                setInProgress(false)
                Log.d("BoardingPhoneAuthActivityDebugging", e.message.toString())
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                //Log.d(TAG, "onCodeSent:$verificationId")
                // Save verification ID and resending token so we can use them later
                //mVerificationId = verificationId
                //mResendToken = token
                // ...
                super.onCodeSent(verificationId, token)
                verificationCode= verificationId
                mResendToken= token
                Toast.makeText(this@BoardingPhoneAuthActivity, "Code sent", Toast.LENGTH_SHORT).show()
                setInProgress(false)
                Log.d("BoardingPhoneAuthActivityDebugging", "Code sent")
            }
        }

        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(timeoutSeconds, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callback)

        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(options.setForceResendingToken(mResendToken).build())
        }
        else{
            PhoneAuthProvider.verifyPhoneNumber(options.build())
        }
    }

    fun signIn(credential: PhoneAuthCredential){
        setInProgress(true)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                setInProgress(false)
                if(task.isSuccessful){
                    val intent= Intent(this, BoardingGreetingActivity::class.java)
                    intent.putExtra(User.USERNAME_KEY, username)
                    DBHelper.addUser(User(name, username, password, R.drawable.asset_profile1, phoneNumber))
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show()
                }
            }
        startActivity(intent)
    }

    fun setInProgress(inProgress: Boolean){
        if(inProgress){
            progressbar.visibility= ProgressBar.VISIBLE
            signInButton.visibility= Button.GONE
        }
        else{
            progressbar.visibility= ProgressBar.GONE
            signInButton.visibility= Button.VISIBLE
        }
    }

    fun startResendTimer(){
        resendTv.isEnabled = false
        timerTv.isEnabled = false

        object : CountDownTimer(timeoutSeconds * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerTv.text = "${millisUntilFinished / 1000} seconds"
            }

            override fun onFinish() {
                timerTv.text = ""
                resendTv.text = "Resend OTP"
                resendTv.isEnabled = true
                timerTv.isEnabled = true
            }
        }.start()
    }


}