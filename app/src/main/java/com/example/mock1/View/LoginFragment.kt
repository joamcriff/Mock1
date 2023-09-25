package com.example.mock1.View

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.mock1.R
import com.example.mock1.Model.UsersModel
import com.example.mock1.data.repository.UserRepository
import com.example.mock1.databinding.FragmentLoginBinding
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var loginViewModel: LoginViewModel
    lateinit var auth : FirebaseAuth
    lateinit var database : FirebaseDatabase
    lateinit var googleSignInClient : GoogleSignInClient
    lateinit var progressDialog : ProgressDialog

    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Creating account")
        progressDialog.setMessage("We are creating your account")


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        loginViewModel = ViewModelProvider(
            requireActivity(),
            LoginViewModel.ViewModelFactory(UserRepository())
        )[LoginViewModel::class.java]


        binding.btn1.setOnClickListener {
            signInWithGoogle()
        }

        binding.btn.setOnClickListener {
            signIn()
        }

        binding.btn3.setOnClickListener {
            signUp()
        }

        binding.forgot.setOnClickListener {
            reset()
        }

        return view
    }

    private fun reset() {
        val fragment = ForgotPasswordFragment()
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.loginFragment, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun signUp() {
        val fragment = RegisterFragment()
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.loginFragment, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun signIn() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        // Gọi phương thức đăng nhập bằng email và password từ ViewModel
        loginViewModel.signInWithEmailAndPassword(email, password)

        loginViewModel.userLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success<*> -> {
                    Toast.makeText(requireActivity(), "Login Successful", Toast.LENGTH_SHORT).show()
                    val fragment = StartFragment()
                    val fragmentManager = fragmentManager
                    val fragmentTransaction = fragmentManager?.beginTransaction()
                    fragmentTransaction?.replace(R.id.loginFragment, fragment)
                    fragmentTransaction?.addToBackStack(null)
                    fragmentTransaction?.commit()
                }

                is Resource.Error -> {
                    // Xử lý lỗi, hiển thị thông báo cho người dùng
                    val error = resource.exception
                    Toast.makeText(requireActivity(), "Error: $error", Toast.LENGTH_SHORT).show()
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
                val fragment = StartFragment()
                val fragmentManager = fragmentManager
                val fragmentTransaction = fragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.loginFragment, fragment)
                fragmentTransaction?.addToBackStack(null)
                fragmentTransaction?.commit()
            } else {
                Toast.makeText(requireActivity(), "error", Toast.LENGTH_SHORT).show()
            }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}