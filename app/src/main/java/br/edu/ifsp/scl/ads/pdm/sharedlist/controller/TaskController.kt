package br.edu.ifsp.scl.ads.pdm.sharedlist.controller

import br.edu.ifsp.scl.ads.pdm.sharedlist.model.Task
import br.edu.ifsp.scl.ads.pdm.sharedlist.model.TaskDao
import br.edu.ifsp.scl.ads.pdm.sharedlist.model.TaskFbDao
import br.edu.ifsp.scl.ads.pdm.sharedlist.view.MainActivity

class TaskController (private val mainActivity: MainActivity){
    private val taskDao: TaskDao = TaskFbDao()

    fun insertTask(task: Task) {
        Thread {
            taskDao.createTask(task)
        }.start()
    }

    fun getTask(id: Int) = taskDao.retrieveTask(id)

    fun getTasks() {
        Thread {
            val list = taskDao.retrieveTasks()
            mainActivity.runOnUiThread {
                mainActivity.updateTaskList(list)
            }
        }.start()
    }

    fun findTitleIfExists(titulo: String) = taskDao.findTitle(titulo)

    fun editTask(task: Task) {
        Thread {
            taskDao.updateTask(task)
        }.start()
    }

    fun removeTask(task: Task) {
        Thread {
            taskDao.deleteTask(task)
        }.start()
    }
}