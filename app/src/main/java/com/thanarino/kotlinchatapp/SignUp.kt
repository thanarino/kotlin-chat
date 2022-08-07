package com.thanarino.kotlinchatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.thanarino.kotlinchatapp.databinding.ActivitySignUpBinding
import com.thanarino.kotlinchatapp.objects.User

class SignUp : AppCompatActivity() {

  private val TAG = SignUp::class.java.simpleName

  private lateinit var edtEmail: EditText
  private lateinit var edtName: EditText
  private lateinit var edtPassword: EditText
  private lateinit var btnSignUp: Button
  private lateinit var btnGoBack: Button

  private lateinit var binding: ActivitySignUpBinding

  private lateinit var mAuth: FirebaseAuth
  private lateinit var mDbRef: DatabaseReference

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySignUpBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)
    supportActionBar?.hide()

    mAuth = FirebaseAuth.getInstance()


    edtEmail = binding.edtEmail
    edtName = binding.edtName
    edtPassword = binding.edtPassword
    btnSignUp = binding.btnSignup
    btnGoBack = binding.btnGoBack

    btnSignUp.setOnClickListener {
      val name = edtName.text.toString()
      val email = edtEmail.text.toString()
      val password = edtPassword.text.toString()

      signUp(name, email, password)
    }
  }

  private fun signUp(name: String, email: String, password: String) {
    mAuth.createUserWithEmailAndPassword(email, password)
      .addOnCompleteListener(this) { task ->
        if (task.isSuccessful) {
          Log.d(TAG, "created user $email")
          Toast.makeText(this@SignUp, "Sign in success!", Toast.LENGTH_SHORT).show()
//          var user = mAuth.currentUser
//          updateUi(user)
          addUserToDb(name, email, mAuth.currentUser?.uid!!)
          val intent = Intent(this@SignUp, MainActivity::class.java)
          finish()
          startActivity(intent)
        } else {
          Log.d(TAG, "create user $email failed: ", task.exception)
          if (task.exception is FirebaseAuthInvalidCredentialsException) {
            Toast.makeText(this@SignUp, "Registration failed. Please check email address format.", Toast.LENGTH_SHORT).show()
          } else if (task.exception is FirebaseAuthWeakPasswordException) {
            Toast.makeText(this@SignUp, "Registration failed. Password should be at least 6 characters.", Toast.LENGTH_SHORT).show()
          } else {
            Toast.makeText(this@SignUp, "Registration failed. Please try again later.", Toast.LENGTH_SHORT).show()
          }
//          updateUi(null)
        }
      }
  }

  private fun addUserToDb(name: String, email: String, uid: String) {
    mDbRef = Firebase.database.getReference("user")
    mDbRef.child(uid).setValue(User(name, email, uid))
  }

  private fun updateUi(user: FirebaseUser?) {

  }
}