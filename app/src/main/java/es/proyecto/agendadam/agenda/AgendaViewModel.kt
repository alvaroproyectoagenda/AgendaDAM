package es.proyecto.agendadam.agenda

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class AgendaViewModel: ViewModel() {
    private val _error: MutableStateFlow<String> =
        MutableStateFlow("")
    val error: StateFlow<String>
        get() = _error

    private val _tarea: MutableStateFlow<Tarea> =
        MutableStateFlow(Tarea())
    val tarea: StateFlow<Tarea>
        get() = _tarea


    private val _tareas: MutableStateFlow<List<Tarea>> =
        MutableStateFlow(listOf())
    val tareas: StateFlow<List<Tarea>>
        get() = _tareas

    private val _asignaturas: MutableStateFlow<List<String>> =
        MutableStateFlow(listOf())
    val asignaturas: StateFlow<List<String>>
        get() = _asignaturas

    private val _addTarea: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val addTarea: StateFlow<Boolean>
        get() = _addTarea

    private val _removeTarea: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val removeTarea: StateFlow<Boolean>
        get() = _removeTarea

    fun getTareas(){

        Firebase.auth.currentUser?.let{
            Firebase.firestore.collection("tareas")
                .whereEqualTo("usuario",it.uid)
                .get()
                .addOnSuccessListener { documents ->
                    var tareasListTemp = arrayListOf<Tarea>()
                    for (document in documents) {
                        var tarea = document.toObject(Tarea::class.java)
                        tareasListTemp.add(tarea)
                    }
                    _tareas.value = tareasListTemp
                }
                .addOnFailureListener {  e ->
                    _tareas.value = emptyList()
                    _error.value = e.message.toString()
                }
        }

    }

    fun getAsignaturas(){
        Firebase.auth.currentUser?.let{
            Firebase.firestore.collection("asignaturas")
                .get()
                .addOnSuccessListener { documents ->
                    var asginaturaTemp = arrayListOf<String>()
                    for (document in documents) {
                        var asignatura = document.toObject(Asignatura::class.java)
                        asignatura.nombre?.let{
                            asginaturaTemp.add(it)
                        }
                    }
                    _asignaturas.value = asginaturaTemp
                }
                .addOnFailureListener {  e ->
                    _asignaturas.value = emptyList()
                    _error.value = e.message.toString()
                }
        }
    }

    fun addUpateTareas(tarea: Tarea) {
        tarea.id?.let {
            Firebase.firestore.collection("tareas")
                .document(it)
                .set(tarea)
                .addOnCompleteListener { res ->
                    _addTarea.value = res.isSuccessful
                }

        }
    }

    fun getTarea(idTarea: String){

        Firebase.auth.currentUser?.let{
            Firebase.firestore.collection("tareas")
                .document(idTarea)
                .get()
                .addOnSuccessListener { document ->
                    _tarea.value = document.toObject(Tarea::class.java) ?: Tarea()
                }
                .addOnFailureListener {  e ->
                    _tareas.value = emptyList()
                    _error.value = e.message.toString()
                }
        }

    }
    fun deleteTarea(idTarea: String){

        Firebase.auth.currentUser?.let{
            Firebase.firestore.collection("tareas")
                .document(idTarea)
                .delete()
                .addOnCompleteListener { res ->
                    _removeTarea.value = res.isSuccessful
                }
        }

    }
}