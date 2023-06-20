package br.edu.ifsp.scl.ads.pdm.sharedlist.adapter

import android.graphics.Paint
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.View.OnCreateContextMenuListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.scl.ads.pdm.sharedlist.databinding.TileTaskBinding
import br.edu.ifsp.scl.ads.pdm.sharedlist.model.Task

class TaskAdapter (
    private val taskList: MutableList<Task>,
    private val onTaskClickListener: OnTaskClickListener
): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(tileTaskBinding: TileTaskBinding):
        RecyclerView.ViewHolder(tileTaskBinding.root), OnCreateContextMenuListener{

        val tituloTv: TextView = tileTaskBinding.tituloTv
        val dataTv: TextView = tileTaskBinding.dataTv
        var taskPosition= -1
        init{
            tileTaskBinding.root.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            val task = taskList[taskPosition]

            if (!task.status) {
                menu?.add(Menu.NONE, 0,0, "Editar")?.setOnMenuItemClickListener {
                    if (taskPosition != -1){
                        onTaskClickListener.onEditMenuItemClick(taskPosition)
                    }
                    true
                }

                menu?.add(Menu.NONE, 1,1, "Remover")?.setOnMenuItemClickListener {
                    if (taskPosition != -1){
                        onTaskClickListener.onRemoveMenuItemClick(taskPosition)
                    }
                    true
                }

                menu?.add(Menu.NONE, 2,2, "Concluir")?.setOnMenuItemClickListener {
                    if (taskPosition != -1){
                        onTaskClickListener.onCompleteMenuItemClick(taskPosition)
                    }
                    true
                }
            }
        }
    }

    override fun getItemCount(): Int = taskList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val tileTaskBinding = TileTaskBinding.inflate(LayoutInflater.from(parent.context))
        return TaskViewHolder(tileTaskBinding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]

        if (task.status) {
            holder.tituloTv.text = "${task.titulo} \u2713"
        } else {
            holder.tituloTv.text = task.titulo
        }

        holder.dataTv.text = task.dataPrevistaCumprimento
        holder.taskPosition = position

        holder.itemView.setOnClickListener {
            onTaskClickListener.onTileTaskClick(position)
        }
    }
}