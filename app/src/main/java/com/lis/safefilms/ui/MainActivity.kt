package com.lis.safefilms.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.lis.safefilms.R
import com.lis.safefilms.data.room.FilmDatabase
import com.lis.safefilms.di.Injection
import com.lis.safefilms.tools.DatabaseFun
import kotlinx.coroutines.launch
import java.net.URL

class MainActivity : AppCompatActivity() {

    private var filmId: Int? = null
    private var apiKey: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        intent()
        apiKey = getApiKey()

        if (apiKey.isNullOrBlank()) {
            Log.e("apikey", "$filmId")
            val intent = Intent(this, AuthorizationActivity::class.java)
            startActivity(intent)
        } else {
            saveFilm()
        }
    }

    private fun saveFilm() {
        lifecycleScope.launch {
            getFIlm(filmId, apiKey!!)
        }
    }

    private fun intent() {
        if (intent?.action == Intent.ACTION_SEND) {
            if ("text/plain" == intent.type) {
                handleSendText(intent)
            }
        }
    }

    private fun handleSendText(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            filmId = URL(it).path
                .substringBeforeLast('/') //get without last '/'
                .substringAfterLast('/') //get film id
                .toIntOrNull() //convert film id to int
        }
    }

    private suspend fun getFIlm(filmId: Int?, apiKey: String) {
        if (filmId != null) {
            val response = Injection.provideRepository(apiKey).getFilm(filmId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    DatabaseFun(FilmDatabase.getInstance(baseContext)).writeToDB(body)
                    finish()
                }
            }
        }
    }

    private fun getApiKey(): String? {
        val pref = getSharedPreferences("app_preference", MODE_PRIVATE)
        return pref.getString("api_key", "")
    }
}