package br.edu.ifsp.scl.ads.pdm.sharedlist.view

import android.os.Build
import android.os.Bundle
import android.view.View
import br.edu.ifsp.scl.ads.pdm.sharedlist.databinding.ActivityTaskBinding
import br.edu.ifsp.scl.ads.pdm.sharedlist.model.Task
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.random.Random

class TaskActivity : BaseActivity() {
    private val acb : ActivityTaskBinding by lazy {
        ActivityTaskBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acb.root)
        supportActionBar?.subtitle = "Informações da Tarefa"

        val receivedTask = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_TASK, Task::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_TASK)
        }

        receivedTask?.let{_receivedTask ->
            with(acb){
                with(_receivedTask){
                    tituloEt.setText(titulo)
                    descricaoEt.setText(descricao)
                    dataCriacaoEt.setText(dataCriacao)
                    dataPrevistaCumprimentoEt.setText(dataPrevistaCumprimento)
                    usuarioEt.setText(usuario)
                    usuarioConclusaoEt.setText(usuarioConclusao)
                }
            }
            val viewTask = intent.getBooleanExtra(EXTRA_VIEW_TASK, false)
            with(acb) {
                tituloEt.isEnabled = !viewTask
                descricaoEt.isEnabled = !viewTask
                dataCriacaoEt.isEnabled = !viewTask
                dataPrevistaCumprimentoEt.isEnabled = !viewTask
                usuarioEt.isEnabled = !viewTask
                usuarioConclusaoEt.isEnabled = !viewTask

                tituloEt.visibility = if(viewTask) View.VISIBLE else View.GONE
                dataCriacaoEt.visibility = if(viewTask) View.VISIBLE else View.GONE
                usuarioEt.visibility = if(viewTask) View.VISIBLE else View.GONE
                saveBt.visibility = if(viewTask) View.GONE else View.VISIBLE

                if(_receivedTask.status) {
                    usuarioConclusaoEt.visibility = if(viewTask) View.VISIBLE else View.GONE
                }
            }
        }

        acb.saveBt.setOnClickListener{
            val task = Task(
                id = receivedTask?.id,
                titulo = acb.tituloEt.text.toString(),
                descricao = acb.descricaoEt.text.toString(),
                dataCriacao = SimpleDateFormat("dd/MM/yyyy").format(Date()),
                dataPrevistaCumprimento = acb.dataPrevistaCumprimentoEt.text.toString(),
                usuario = FirebaseAuth.getInstance().currentUser?.email.toString()
            )

            val resultIntent = intent
            resultIntent.putExtra(EXTRA_TASK, task)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
    private fun generateId(): Int {
        val random = Random(System.currentTimeMillis())
        return random.nextInt()
    }
}