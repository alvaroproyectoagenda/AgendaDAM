package es.proyecto.agendadam.agenda

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.proyecto.agendadam.databinding.ItemAgendaBinding


class TareasAdapter(private val clickListener: TareaListener): ListAdapter<Tarea, TareasAdapter.ViewHolder>(TareaDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemAgendaBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Tarea, clickItem: TareaListener) {
            binding.tarea = item
            binding.clickItem = clickItem
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemAgendaBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class TareaDiffCallback : DiffUtil.ItemCallback<Tarea>() {

    override fun areItemsTheSame(oldItem: Tarea, newItem: Tarea): Boolean {
        return oldItem.id == newItem.id
    }


    override fun areContentsTheSame(oldItem: Tarea, newItem: Tarea): Boolean {
        return oldItem == newItem
    }


}

class TareaListener(val clickItem: (item: Tarea) -> Unit) {
    fun onClick(item: Tarea) = clickItem(item)
}