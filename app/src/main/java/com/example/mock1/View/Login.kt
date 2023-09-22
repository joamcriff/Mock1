package com.example.mock1.view

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mock1.R
import com.example.mock1.SecondActivity
import com.example.mock1.UsersModel
import com.example.mock1.databinding.ActivityLoginBinding
import com.example.mock1.data.repository.UserRepository
import com.example.mock1.utils.Resource
import com.example.mock1.viewmodel.LoginViewModel
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
    private lateinit var loginViewModel: LoginViewModel
    lateinit var auth : FirebaseAuth
    lateinit var database : FirebaseDatabase
    lateinit var googleSignInClient : GoogleSignInClient
    lateinit var progressDialog : ProgressDialog

    private lateinit var binding: ActivityLoginBinding

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        progressDialog = ProgressDialog(this@Login)
        progressDialog.setTitle("Creating account")
        progressDialog.setMessage("We are creating your account")


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        loginViewModel = ViewModelProvider(
            this,
            LoginViewModel.ViewModelFactory(UserRepository())
        )[LoginViewModel::class.java]


        binding.btn1.setOnClickListener {
            signInWithGoogle()
        }

        binding.btn.setOnClickListener {
            signIn()
        }

    }

    private fun signIn() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        // Gọi phương thức đăng nhập bằng email và password từ ViewModel
        loginViewModel.signInWithEmailAndPassword(email, password)

        loginViewModel.userLiveData.observe(this) { resource ->
            when (resource) {
                is Resource.Success<*> -> {
                    Toast.makeText(this@Login, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@Login, SecondActivity::class.java)
                    startActivity(intent)
                }

                is Resource.Error -> {
                    // Xử lý lỗi, hiển thị thông báo cho người dùng
                    val error = resource.exception
                    Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    var RC_SIGN_IN = 40

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
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

                val users = UsersModel()
                users.userId = user?.uid
                users.name = user?.displayName

                user?.uid?.let { database.reference.child("Users").child(it).setValue(users) }
                val intent = Intent(this@Login, SecondActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@Login, "error", Toast.LENGTH_SHORT).show()
            }

        }
    }
}