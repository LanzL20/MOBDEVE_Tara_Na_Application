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
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityBoardingPhoneAuthBinding
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class BoardingPhoneAuthActivity:AppCompatActivity() {
    lateinit var phoneNumber: String
    lateinit var username: String
    lateinit var password: String
    lateinit var name: String
    lateinit var salt: String
    val timeoutSeconds= 60L
    val mAuth: FirebaseAuth= FirebaseAuth.getInstance()
    lateinit var from:String

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
        Log.d("BoardingPhoneAuthActivityDebugging", "Phone Auth Activity Created")
        from= intent.getStringExtra("from")!!
        if(from=="login"){
            phoneNumber= intent.getStringExtra(User.PHONE_NUMBER_KEY)!!
        }
        else if(from=="signup"){
            phoneNumber= intent.getStringExtra(User.PHONE_NUMBER_KEY)!!
            name= intent.getStringExtra(User.NAME_KEY)!!
            username= intent.getStringExtra(User.USERNAME_KEY)!!
            password= intent.getStringExtra(User.PASSWORD_KEY)!!
            salt= intent.getStringExtra(User.SALT_KEY)!!
        }

        viewBinding.phoneNumberTv.text= phoneNumber

        otpInput= viewBinding.activityPhoneAuthOtpEt
        signInButton= viewBinding.activityBoardingPhoneAuthBtnSignup
        resendTv= viewBinding.activityBoardingPhoneAuthResendTv
        timerTv= viewBinding.activityBoardingPhoneAuthTimerCountdown
        progressbar= viewBinding.progressBar2

        sendVerificationCode(phoneNumber!!, false)

        signInButton.setOnClickListener {
            val otp= otpInput.text.toString()
            if(otp.isEmpty()){
                Toast.makeText(this, "Please enter the OTP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
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
                    val intent= Intent(this, BoardingGreetingActivity::class.java).apply {
                        flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    Log.d("BoardingPhoneAuthActivityDebugging", "Sign in successful")

                    lifecycleScope.launch {
                        if(from=="signup"){
                            //TODO:ADD ENCRYPTION FOR PASSWORD
                            DBHelper.addUser(User(name, username, password, 1, phoneNumber, salt))
                            Log.d("BoardingPhoneAuthActivityDebugging", User(name, username, password, 1, phoneNumber, salt).toString())
                        }
                        val user= DBHelper.getUserFromNumber(phoneNumber)
                        UserSession.setUser(user!!)
                        startActivity(intent)
                        finish()
                    }
                }
                else{
                    Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show()
                }
            }
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