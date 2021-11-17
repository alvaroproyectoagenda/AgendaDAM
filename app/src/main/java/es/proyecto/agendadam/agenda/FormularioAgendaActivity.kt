package es.proyecto.agendadam.agenda

import android.content.DialogInterface
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.proyecto.agendadam.R
import es.proyecto.agendadam.databinding.ActivityFormularioAgendaBinding
import es.proyecto.agendadam.utils.Utilities.Companion.EXTRA_ID_TAREA
import es.proyecto.agendadam.utils.Utilities.Companion.convertStringDateToTmstmp
import es.proyecto.agendadam.utils.Utilities.Companion.convertTmtmpToString
import es.proyecto.agendadam.utils.Utilities.Companion.obtenerPosicionItemResource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class FormularioAgendaActivity : AppCompatActivity() {

    lateinit var binding: ActivityFormularioAgendaBinding
    lateinit var viewModel: AgendaViewModel
    private var isUpdate = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_formulario_agenda)
        binding.btnCrearTarea.setOnClickListener {
            crearTarea()
        }
        binding.btnEliminar.setOnClickListener {
            borrarTarea()
        }
        initViewModel()
        val bundle = intent.extras
        if (bundle != null) {
            isUpdate = true
            var idTarea = bundle.getString(EXTRA_ID_TAREA)
            idTarea?.let {

                viewModel.getTarea(it)
                cargarTareaModificar()
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(AgendaViewModel::class.java)
        viewModel.getAsignaturas()
        cargarAsignaturas()

    }

    private fun crearTarea() {
        if (TextUtils.isEmpty(binding.descripcion.text) ||
            TextUtils.isEmpty(binding.fechaEntrega.text) ||
            convertStringDateToTmstmp(binding.fechaEntrega.text.toString()) == null
        ) {
            Toast.makeText(
                this,
                "Debes de rellenar todos los datos y el formato de fecha debe de ser dd/MM/aaa",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            if (isUpdate) {
                modificarTarea()
            } else {
                insertarTarea()
            }

        }
    }

    private fun insertarTarea() {
        val tarea = Tarea(
            id = UUID.randomUUID().toString(),
            usuario = Firebase.auth.currentUser?.uid,
            fecha_entrega = convertStringDateToTmstmp(binding.fechaEntrega.text.toString()),
            descripcion = binding.descripcion.text.toString(),
            tipo = binding.tipo.selectedItem.toString(),
            asignatura = binding.asignaturas.selectedItem.toString()
        )
        viewModel.addUpateTareas(tarea)
        lifecycleScope.launch {
            viewModel.addTarea.collect {
                it?.let{
                    if (it) {
                        Toast.makeText(applicationContext, "Tarea creada con éxito", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Ocurrió un error al crear la tarea",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        }
    }

    private fun modificarTarea() {
        val tarea = Tarea(
            id = viewModel.tarea.value.id,
            usuario = Firebase.auth.currentUser?.uid,
            fecha_creacion = viewModel.tarea.value.fecha_creacion,
            fecha_entrega = convertStringDateToTmstmp(binding.fechaEntrega.text.toString()),
            descripcion = binding.descripcion.text.toString(),
            tipo = binding.tipo.selectedItem.toString(),
            asignatura = binding.asignaturas.selectedItem.toString()
        )
        viewModel.addUpateTareas(tarea)
        lifecycleScope.launch {
            viewModel.addTarea.collect {
                it?.let{
                    if (it) {
                        Toast.makeText(applicationContext, "Tarea modificada con éxito", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Ocurrió un error al crear la tarea",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        }
    }

    private fun cargarAsignaturas() {
        lifecycleScope.launchWhenCreated {
            viewModel.asignaturas.collect {
                var adapterAsignaturas =
                    ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, it)
                adapterAsignaturas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.asignaturas.adapter = adapterAsignaturas
            }
        }
    }

    private fun cargarTareaModificar() {
        lifecycleScope.launchWhenCreated {
            viewModel.tarea.collect {
                dibujarUI(it)
            }
        }
    }

    private fun dibujarUI(tarea: Tarea) {
        binding.descripcion.setText(tarea.descripcion)
        binding.fechaEntrega.setText(convertTmtmpToString(tarea.fecha_entrega ?: Timestamp.now()))
        binding.tipo.setSelection(
            obtenerPosicionItemResource(
                this,
                tarea.tipo,
                R.array.tipos
            )
        )
        val index = viewModel.asignaturas.value.indexOf(tarea.asignatura)
        binding.asignaturas.setSelection(index)

        binding.btnCrearTarea.text = "Modificar"
        binding.btnEliminar.visibility = View.VISIBLE
    }


    private fun borrarTarea() {
        var idTarea = viewModel.tarea.value.id
        idTarea?.let {
            val builder = AlertDialog.Builder(this)
            builder.apply {
                setPositiveButton("SI",
                    DialogInterface.OnClickListener { dialog, id ->
                        viewModel.deleteTarea(idTarea)
                        lifecycleScope.launchWhenCreated {
                            viewModel.removeTarea.collect {
                                it?.let {
                                    if (it) {
                                        Toast.makeText(applicationContext, "Tarea borrada", Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                                }

                            }
                        }

                    })
                setNegativeButton("NO",
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
            }
                .setCancelable(true)
                .setTitle("Estás seguro de borrar la tarea?")

            builder.create().show()
        }

    }
}