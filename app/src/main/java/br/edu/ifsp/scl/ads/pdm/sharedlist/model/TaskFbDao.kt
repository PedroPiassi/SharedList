package br.edu.ifsp.scl.ads.pdm.sharedlist.model

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class TaskFbDao : TaskDao {
    private val TASK_LIST_ROOT_NODE = "tasks"
    private val taskFbDbReference = Firebase.database.getReference(TASK_LIST_ROOT_NODE)

    private val taskList: MutableList<Task> = mutableListOf()
    init{
        taskFbDbReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val task : Task ? = snapshot.getValue<Task>()

                task?.let { _task ->
                    if (!taskList.any { _task.titulo == it.titulo }) {
                        taskList.add(_task)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val task: Task? = snapshot.getValue<Task>()

                task?.let { _task ->
                    taskList[taskList.indexOfFirst { _task.titulo == it.titulo }] = _task
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val task: Task? = snapshot.getValue<Task>()

                task?.let { _task ->
                    taskList.remove(_task)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // NSA (Não se aplica)
            }

            override fun onCancelled(error: DatabaseError) {
                // NSA
            }
        })

        taskFbDbReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val taskHashMap = snapshot.getValue<HashMap<String, Task>>()
                taskList.clear()
                taskHashMap?.values?.forEach {
                    taskList.add(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // NSA
            }
        })
    }

    override fun createTask(task: Task) {
        createOrUpdateTask(task)
    }

    override fun retrieveTask(id: Int): Task? {
        // Função com código genérico, porque não usamos no app.
        return taskList[taskList.indexOfFirst { id == it.id }]
    }

    override fun retrieveTasks(): MutableList<Task> {
        return taskList
    }

    override fun updateTask(task: Task): Int {
        createOrUpdateTask(task)
        return 1
    }

    override fun deleteTask(task: Task): Int {
        taskFbDbReference.child(task.titulo).removeValue()
        return 1
    }

    override fun findTitle(titulo: String): Int {
        return taskList.count { it.titulo == titulo }
    }

    private fun createOrUpdateTask(task: Task) =
        taskFbDbReference.child(task.titulo).setValue(task)
}