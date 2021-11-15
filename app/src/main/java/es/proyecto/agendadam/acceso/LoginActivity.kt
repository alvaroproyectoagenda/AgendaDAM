package es.proyecto.agendadam.acceso

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import es.proyecto.agendadam.R
import es.proyecto.agendadam.agenda.AgendaActivity
import es.proyecto.agendadam.databinding.ActivityLoginBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity()  {

    lateinit var binding: ActivityLoginBinding
    val viewModel: AccesoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.btnRegistro.setOnClickListener {
            val intent = Intent(applicationContext, RegistroActivity::class.java)
            startActivity(intent)
        }
        binding.botonAcceder.setOnClickListener {
            if(TextUtils.isEmpty(binding.email.text.toString()) ||
                    TextUtils.isEmpty(binding.contrasena.text.toString()))
            {
                Toast.makeText(applicationContext, "Rellena email y/o password", Toast.LENGTH_SHORT).show()
            }else{
                viewModel.login(
                    binding.email.text.toString(),
                    binding.contrasena.text.toString()
                )
            }
        }
        observer()
    }

    private fun observer(){
        lifecycleScope.launch {
            viewModel.login.collect {

                if(it){
                    val intent = Intent(applicationContext, AgendaActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(applicationContext, "Error al acceder, revisa tus credenciales", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}
