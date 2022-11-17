package com.lis.safefilms.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lis.safefilms.databinding.ActivityAuthorizationBinding
import kotlinx.coroutines.launch

class AuthorizationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthorizationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        binding.bindElement()
        setContentView(binding.root)
    }

    private fun ActivityAuthorizationBinding.bindElement() {
        getTokenButton.setOnClickListener {
            lifecycleScope.launch {
                confirmListenerClick()
            }
        }
    }

    private var captchaSid: String? = null
    private fun ActivityAuthorizationBinding.confirmListenerClick() {
        val token = tokenEditText.text.toString()
        if(token.isBlank()){
            showToast("токен")
        } else {
            saveAuthInfo(token)
            finish()
        }

    }

    private fun showToast(field: String) {
        Toast.makeText(this, "Поле: $field не заполненно", Toast.LENGTH_LONG).show()
    }

    private fun saveAuthInfo(token: String) {
        val preferences =
            getSharedPreferences("app_preference", Context.MODE_PRIVATE)

        if (preferences != null) {
            with(preferences.edit()) {
                putString("api_key", token)
                apply()
            }
        }
    }
}
