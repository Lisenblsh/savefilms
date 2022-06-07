package com.lis.safefilms

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ContentInfoCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.lis.safefilms.data.room.FilmDatabase
import com.lis.safefilms.di.Injection
import com.lis.safefilms.tools.DatabaseFun
import kotlinx.coroutines.launch
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        intent()

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
            val filmId = URL(it).path
                .substringBeforeLast('/') //get without last '/'
                .substringAfterLast('/') //get film id
                .toIntOrNull() //convert film id to int
            lifecycleScope.launch {
                getFIlm(filmId)
            }
        }
    }

    private suspend fun getFIlm(filmId: Int?) {
        if (filmId != null) {
            val response = Injection.provideRepository().getFilm(filmId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    DatabaseFun(FilmDatabase.getInstance(baseContext)).writeToDB(body)
                    onBackPressed()
                }
            }
        }
    }
}