package es.proyecto.agendadam.agenda

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.proyecto.agendadam.R

class AgendaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agenda)
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