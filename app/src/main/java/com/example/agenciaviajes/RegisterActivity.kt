package com.example.agenciaviajes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtPassword: TextInputEditText
    private lateinit var edtConfirmarPassword: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.edtEmailRegister)
        edtPassword = findViewById(R.id.edtPasswordRegister)
        edtConfirmarPassword = findViewById(R.id.edtConfirmarPasswordRegister)

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            registrar()
        }

        findViewById<TextView>(R.id.tvGoLogin).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun registrar() {
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()
        val confirmar = edtConfirmarPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty() || confirmar.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_campos_vacios), Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, getString(R.string.error_password_corta), Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmar) {
            Toast.makeText(this, getString(R.string.error_passwords_no_coinciden), Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        task.exception?.localizedMessage ?: getString(R.string.error_autenticacion),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}