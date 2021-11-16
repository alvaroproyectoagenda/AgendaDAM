package es.proyecto.agendadam.utils

import android.os.Build
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.Converters
import com.google.firebase.Timestamp
import es.proyecto.agendadam.R

@BindingAdapter("setFecha")
fun TextView.setFecha(datetime: Timestamp){
    text = Utilities.convertTmtmpToString(datetime)
}

@RequiresApi(Build.VERSION_CODES.M)
@BindingAdapter("setColorTarea")
fun LinearLayout.setColorTarea(tipo: String){
    when(tipo){
        "Tarea" -> background = Converters.convertColorToDrawable( context.resources.getColor(R.color.colorTarea,context.theme))
        "Examen" -> background = Converters.convertColorToDrawable( context.resources.getColor(R.color.colorExamen,context.theme))
        "Proyecto" -> background = Converters.convertColorToDrawable( context.resources.getColor(R.color.colorProyecto,context.theme))
        "Consulta" -> background = Converters.convertColorToDrawable( context.resources.getColor(R.color.colorConsulta,context.theme))
        "Otros" -> background = Converters.convertColorToDrawable( context.resources.getColor(R.color.colorOtros,context.theme))
    }

}