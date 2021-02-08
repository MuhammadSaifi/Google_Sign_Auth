package com.muhammad.google_sign_auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult

import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient

//Dependency
class MainActivity : AppCompatActivity(),GoogleApiClient.OnConnectionFailedListener{
    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    internal lateinit var signInButton: SignInButton
    private var RC_SIGN_IN = 1
    private var googleApiClient: GoogleApiClient?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signInButton = findViewById(R.id.sign_in_button)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this,this)
            .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
            .build()

        signInButton.setOnClickListener {
            val intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
            startActivityForResult(intent,RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== RC_SIGN_IN){
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleResult(result!!)
        }
    }
    private fun handleResult(result: GoogleSignInResult){
        if(result.isSuccess){
            startActivity(Intent(this,ProfileActivity::class.java))
        }
        else{
            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
        }
    }
}
