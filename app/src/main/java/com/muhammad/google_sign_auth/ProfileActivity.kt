package com.muhammad.google_sign_auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient

class ProfileActivity : AppCompatActivity(),GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    internal lateinit var logoutBtn: Button
    internal lateinit var userName: TextView
    internal lateinit var userEmail: TextView
    internal lateinit var userId: TextView
    internal lateinit var profileImage: ImageView
    private var googleApiClient: GoogleApiClient?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        logoutBtn = findViewById<View>(R.id.logoutBtn) as Button
        userName = findViewById<View>(R.id.name) as TextView
        userEmail = findViewById<View>(R.id.email) as TextView
        userId = findViewById<View>(R.id.userId) as TextView
        profileImage = findViewById<View>(R.id.profileImage) as ImageView

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this,this)
            .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
            .build()

        logoutBtn.setOnClickListener {
            Auth.GoogleSignInApi.signOut(googleApiClient)
                .setResultCallback { status ->
                    if(status.isSuccess){
                        startActivity(Intent(this,MainActivity::class.java))
                    }
                    else{
                        Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()


                    }
                }
        }


    }

    override fun onStart() {
        super.onStart()
        val opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient)
        if(opr.isDone){
            val result = opr.get()
            handleResult(result!!)
        }
        else{
            opr.setResultCallback { googleSignInResult ->
                handleResult(googleSignInResult)
            }
        }
    }
    private fun handleResult(result: GoogleSignInResult){
        if(result.isSuccess){
            val account = result.signInAccount
            userName.text = account!!.displayName
            userEmail.text=account!!.email
            userId.text =account!!.id
            Glide.with(this).load(account.photoUrl).into(profileImage)
        }
        else{
            Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()
        }
    }
}
