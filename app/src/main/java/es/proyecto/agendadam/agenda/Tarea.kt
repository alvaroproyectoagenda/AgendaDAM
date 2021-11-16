package es.proyecto.agendadam.agenda

import com.google.firebase.Timestamp

data class Tarea(
    var id: String? = null,
    var usuario: String? = null,
    var tipo: String? = null,
    var descripcion: String? = null,
    var asignatura: String? = null,
    var fecha_creacion: Timestamp = Timestamp.now(),
    var fecha_entrega: Timestamp? = null

)
