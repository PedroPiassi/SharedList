package br.edu.ifsp.scl.ads.pdm.sharedlist.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.scl.ads.pdm.sharedlist.R
import br.edu.ifsp.scl.ads.pdm.sharedlist.adapter.OnTaskClickListener
import br.edu.ifsp.scl.ads.pdm.sharedlist.adapter.TaskAdapter
import br.edu.ifsp.scl.ads.pdm.sharedlist.controller.TaskController
import br.edu.ifsp.scl.ads.pdm.sharedlist.databinding.ActivityMainBinding
import br.edu.ifsp.scl.ads.pdm.sharedlist.model.Task
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity(), OnTaskClickListener {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    // Data source
    private val taskList: MutableList<Task> = mutableListOf()
    // Adapter
    private val taskAdapter: TaskAdapter by lazy {
        TaskAdapter(taskList, this)
    }

    private lateinit var carl: ActivityResultLauncher<Intent>

    // Controller
    private val taskController: TaskController by lazy {
        TaskController(this)
    }

    lateinit var updateViewsHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        supportActionBar?.subtitle = "Shared List"

        taskController.getTasks()
        amb.tasksRv.layoutManager = LinearLayoutManager(this)
        amb.tasksRv.adapter = taskAdapter

        carl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if ( result.resultCode == RESULT_OK) {
                val task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(EXTRA_TASK, Task::class.java)
                } else {
                    result.data?.getParcelableExtra(EXTRA_TASK)
                }
                task?.let {_task ->
                    val position = taskList.indexOfFirst { it.id == _task.id}
                    Toast.makeText(this, position.toString(), Toast.LENGTH_LONG).show()
                    if (position != -1) {
                        taskList[position] = _task
                        taskController.editTask(_task)
                        taskAdapter.notifyItemChanged(position)
                        Toast.makeText(this, "Tarefa editada", Toast.LENGTH_LONG).show()
                    } else {
                        if(taskController.findTitleIfExists(_task.titulo) == 0){
                            taskController.insertTask(_task)
                            Toast.makeText(this, "Tarefa Cadastrada", Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(this, "Erro ao cadastrar. Tarefa ${_task.titulo} já existente", Toast.LENGTH_LONG).show()
                        }
                    }
                    taskController.getTasks()
                    taskAdapter.notifyDataSetChanged()
                }
            }
        }

        updateViewsHandler = Handler(Looper.myLooper()!!) { msg ->
            taskController.getTasks()
            true
        }
        updateViewsHandler.sendMessageDelayed(Message(),3000)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addTaskMi -> {
                carl.launch(Intent(this, TaskActivity::class.java))
                true
            }
            R.id.signOutMi -> {
                FirebaseAuth.getInstance().signOut()
                googleSignInClient.signOut()
                finish()
                true
            }
            else -> false
        }
    }

    fun updateTaskList(_taskList: MutableList<Task>) {
        taskList.clear()
        taskList.addAll(_taskList)
        taskAdapter.notifyDataSetChanged()
    }

    override fun onTileTaskClick(position: Int) {
        val task = taskList[position]
        val taskIntent = Intent(this, TaskActivity::class.java)
        taskIntent.putExtra(EXTRA_TASK, task)
        taskIntent.putExtra(EXTRA_VIEW_TASK, true)
        carl.launch(taskIntent)
    }

    override fun onEditMenuItemClick(position: Int) {
        val task = taskList[position]
        val taskIntent = Intent(this, TaskActivity::class.java)
        taskIntent.putExtra(EXTRA_TASK, task)
        carl.launch(taskIntent)
    }

    override fun onRemoveMenuItemClick(position: Int) {
        val task = taskList[position]
        taskList.removeAt(position)
        taskController.removeTask(task)
        taskAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Tarefa removida", Toast.LENGTH_SHORT).show()
    }

    override fun onCompleteMenuItemClick(position: Int) {
        val task = taskList[position]

        task.status = true
        task.usuarioConclusao = FirebaseAuth.getInstance().currentUser?.email.toString()
        taskController.editTask(task)
        taskAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Tarefa concluida", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser == null) {
            Toast.makeText(this, "Não há usuário autenticado!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}