package com.example.mock1

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase


class Login : AppCompatActivity() {
    lateinit var btn1 : Button
    lateinit var auth : FirebaseAuth
    lateinit var database : FirebaseDatabase
    lateinit var googleSignInClient : GoogleSignInClient
    lateinit var progressDialog : ProgressDialog
    lateinit var emailText : EditText
    lateinit var passwordText : EditText
    lateinit var btn : Button
    lateinit var btn2 : Button
    lateinit var forgot : TextView

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this@Login, SecondActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn1 = findViewById(R.id.btn1)
        btn = findViewById(R.id.btn)
        btn2 = findViewById(R.id.btn_sign_up)
        emailText = findViewById(R.id.email)
        passwordText = findViewById(R.id.password)
        forgot = findViewById(R.id.forgot)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        progressDialog = ProgressDialog(this@Login)
        progressDialog.setTitle("Creating account")
        progressDialog.setMessage("We are creating your account")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        btn.setOnClickListener {
            onclick()
        }

        btn1.setOnClickListener {
            signIn()
        }

        btn2.setOnClickListener {
            onclick2()
        }

        forgot.setOnClickListener {
            onclick3()
        }
    }

    private fun onclick3() {
        val intent = Intent(this@Login, Forgot_Password::class.java)
        startActivity(intent)
    }

    private fun onclick2() {
        val intent = Intent(this@Login, Register::class.java)
        startActivity(intent)
    }

    private fun onclick() {
        val email : String = emailText.text.toString()
        val password : String = passwordText.text.toString()

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this@Login, "Enter email", Toast.LENGTH_SHORT).show()
            return
        }

        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this@Login, "Enter password", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@Login, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@Login, SecondActivity::class.java)
                    startActivity(intent)
                } else {

                    Toast.makeText(
                        this@Login,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    var RC_SIGN_IN = 40

    private fun signIn() {
        val intent : Intent = googleSignInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try{
                val account : GoogleSignInAccount = task.getResult(ApiException::class.java)
                firebaseAuth(account.idToken)
            } catch (e : ApiException) {
                throw RuntimeException(e)
            }
        }
    }

    private fun firebaseAuth(idToken: String?) {
        val credential : AuthCredential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user : FirebaseUser? = auth.currentUser

                val users = Users()
                users.UserId = user?.uid
                users.name = user?.displayName
                users.profile = user?.photoUrl.toString()

                user?.uid?.let { database.reference.child("Users").child(it).setValue(users) }
                val intent = Intent(this@Login, SecondActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@Login, "error", Toast.LENGTH_SHORT).show()
            }

        }
    }
}