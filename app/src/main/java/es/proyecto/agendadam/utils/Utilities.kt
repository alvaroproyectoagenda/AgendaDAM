package es.proyecto.agendadam.utils

import android.content.Context
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.regex.Pattern


class Utilities{
    companion object {

        const val EXTRA_ID_TAREA = "ID_TAREA"

        fun convertTmtmpToString(date: Timestamp): String{
            val sfd = SimpleDateFormat("dd/MM/yyyy")
            return sfd.format(date.toDate())
        }

        fun validarFecha(date: String): Boolean {
            val patron ="^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d\$"
            return Pattern.matches(patron, date)
        }
        fun convertStringDateToTmstmp(fechaStr: String): Timestamp? {
            if (validarFecha(fechaStr)) {
                try {
                    val formatter = SimpleDateFormat("dd/MM/yyyy");
                    return Timestamp(formatter.parse(fechaStr))
                } catch (e: Exception) {
                    return null

                }
            } else {
                return null
            }


        }

          fun obtenerPosicionItemResource(context: Context, data: String?, resource: Int): Int {
            val provinces =  context.resources.getStringArray(resource)
            return provinces.indexOf(data)
        }

    }
}
