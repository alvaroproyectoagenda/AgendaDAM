package es.proyecto.agendadam.acceso

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.proyecto.agendadam.R
import es.proyecto.agendadam.agenda.AgendaActivity
import es.proyecto.agendadam.databinding.ActivityRegistroBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RegistroActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistroBinding
    val viewModel: AccesoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registro)
        binding.botonCrearCuenta.setOnClickListener {
            if (TextUtils.isEmpty(binding.email.text.toString()) ||
                TextUtils.isEmpty(binding.contrasena.text.toString()) ||
                TextUtils.isEmpty(binding.repiteContrasena.text.toString()) ||
                TextUtils.isEmpty(binding.nombre.text.toString())
            ) {
                Toast.makeText(applicationContext, "Rellena todo los campos", Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (binding.contrasena.text.toString() != binding.repiteContrasena.text.toString()) {
                    Toast.makeText(
                        applicationContext,
                        "Las contraseñas deben de coincidir",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.botonCrearCuenta.isEnabled = false
                    viewModel.register(
                        binding.email.text.toString(),
                        binding.contrasena.text.toString()
                    )
                    observer()
                }

            }
        }


    }


    private fun observer() {
        lifecycleScope.launchWhenStarted {
            viewModel.addUser.collect {
                it?.let{
                    if (it) {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(applicationContext, "Registro completado", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(applicationContext, AgendaActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Error al registrarse, revisa tus credenciales",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }

            }
        }
        lifecycleScope.launchWhenStarted {

            viewModel.register.collect {
                it?.let{
                    if (it) {
                        val user = User(
                            id = Firebase.auth.uid,
                            nombre = binding.nombre.text.toString(),
                            email = binding.email.text.toString()
                        )
                        viewModel.addUser(user)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Error al registrarse, revisa tus credenciales",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        }
    }
}