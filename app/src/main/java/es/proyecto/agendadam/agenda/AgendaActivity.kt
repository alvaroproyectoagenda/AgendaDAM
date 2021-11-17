package es.proyecto.agendadam.agenda

import android.content.DialogInterface
import android.content.Intent
import android.database.DatabaseUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.proyecto.agendadam.MainActivity
import es.proyecto.agendadam.R
import es.proyecto.agendadam.databinding.ActivityAgendaBinding
import es.proyecto.agendadam.utils.Utilities.Companion.EXTRA_ID_TAREA
import kotlinx.coroutines.flow.collect

class AgendaActivity : AppCompatActivity() {

    lateinit var binding: ActivityAgendaBinding
    lateinit var adapter: TareasAdapter
    lateinit var viewModel: AgendaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_agenda)
        binding.fabCrear.setOnClickListener {
            val intent = Intent(applicationContext, FormularioAgendaActivity::class.java)
            startActivity(intent)

        }
        adapter = TareasAdapter(TareaListener { tarea ->
            val intent = Intent(applicationContext, FormularioAgendaActivity::class.java)
            intent.putExtra(EXTRA_ID_TAREA,tarea.id)
            startActivity(intent)
        })
        binding.rvTareas.adapter = adapter
        initViewModel()

    }

    override fun onStart() {
        super.onStart()
        initViewModel()
    }
    fun initViewModel(){
        viewModel = ViewModelProvider(this).get(AgendaViewModel::class.java)
        viewModel.getTareas()
        lifecycleScope.launchWhenCreated {
            viewModel.tareas.collect {
                adapter.submitList(it)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_agenda, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.menu_cerrar_sesion -> {
                cerrarSesion()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cerrarSesion(){
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setPositiveButton("SI",
                DialogInterface.OnClickListener { dialog, id ->
                    Firebase.auth.signOut()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)

                })
            setNegativeButton("NO",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })
        }
            .setCancelable(true)
            .setTitle("Estás seguro de cerrar la sesión?")

        builder.create().show()
    }
}