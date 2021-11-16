 package es.proyecto.agendadam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.proyecto.agendadam.acceso.LoginActivity
import es.proyecto.agendadam.agenda.AgendaActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

 class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            delay(3000)
            if(Firebase.auth.currentUser!=null){
                val intent = Intent(applicationContext, AgendaActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


    }
}