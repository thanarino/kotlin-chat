package com.thanarino.kotlinchatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.thanarino.kotlinchatapp.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {

  private val TAG = Login::class.java.simpleName

  private lateinit var edtEmail: EditText
  private lateinit var edtPassword: EditText
  private lateinit var btnLogin: Button
  private lateinit var btnSignUp: Button

  private lateinit var binding: ActivityLoginBinding

  private lateinit var mAuth: FirebaseAuth

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLoginBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)
    supportActionBar?.hide()

    mAuth = FirebaseAuth.getInstance()

    edtEmail = binding.edtEmail
    edtPassword = binding.edtPassword
    btnLogin = binding.btnLogin
    btnSignUp = binding.btnSignup

    btnSignUp.setOnClickListener {
      startActivity(Intent(this, SignUp::class.java))
    }

    btnLogin.setOnClickListener {
      val username = edtEmail.text.toString()
      val password = edtPassword.text.toString()

      login(username, password)
    }
  }

  private fun login(email: String, password: String) {
    mAuth.signInWithEmailAndPassword(email, password)
      .addOnCompleteListener(this) { task ->
        if (task.isSuccessful) {
          // Sign in success, update UI with the signed-in user's information
          Log.d(TAG, "signInWithEmail:success $email")
          Toast.makeText(this@Login, "Authentication success!",
            Toast.LENGTH_SHORT).show()
          val user = mAuth.currentUser
//          updateUi(user)
          val intent = Intent(this@Login, MainActivity::class.java)
          finish()
          startActivity(intent)
        } else {
          // If sign in fails, display a message to the user.
          Log.w(TAG, "signInWithEmail:failure $email", task.exception);
          Toast.makeText(this@Login, "Authentication failed. User does not exist.",
            Toast.LENGTH_SHORT).show()
//          updateUi(null)
        }
      }
  }

  private fun updateUi(user: FirebaseUser?) {

  }
}