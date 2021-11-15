package es.proyecto.agendadam.acceso

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import es.proyecto.agendadam.R
import es.proyecto.agendadam.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity()  {

    lateinit var binding: ActivityLoginBinding
    val viewModel: AccesoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
    }

}
