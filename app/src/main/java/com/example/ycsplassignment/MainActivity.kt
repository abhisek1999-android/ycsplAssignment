package com.example.ycsplassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import com.example.ycsplassignment.databinding.ActivityMainBinding
import com.example.ycsplassignment.sp.EMAIL
import com.example.ycsplassignment.sp.PASSWORD
import com.example.ycsplassignment.sp.writeStringPref
import com.google.android.gms.maps.MapView

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val passWordRegx="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{5,}\$".toRegex()
    private val emailRegx = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+".toRegex()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gotoNextButton.setOnClickListener {
            writeStringPref(this,binding.emailId.text.toString(), EMAIL)
            writeStringPref(this,binding.passwordText.text.toString(), PASSWORD)
            val intent = Intent(this,MapActivity::class.java)
            intent.putExtra(EMAIL,binding.emailId.text.toString())
            startActivity(intent)
            finish()
        }

        binding.emailId.doAfterTextChanged {
            val email = it.toString()
            checkValidInputs()
            if (!email.matches(emailRegx)) {
                binding.emailIdLayout.error = "Not a valid email"
            } else {
                binding.emailIdLayout.error = null
            }
        }

       binding.passwordText.doAfterTextChanged {
           checkValidInputs()

           val password = it.toString()
           if (!password.matches(passWordRegx)) {
               binding.passwordLayout.error = "Password must contain at least one uppercase, one lowercase, one number and at least 5 characters"
           } else {
               binding.passwordLayout.error = null
           }
       }



    }

    private fun checkValidInputs() {
        val email = binding.emailId.text.toString()
        val password = binding.passwordText.text.toString()
        binding.gotoNextButton.isEnabled = email.matches(emailRegx) && password.matches(passWordRegx)
    }
}