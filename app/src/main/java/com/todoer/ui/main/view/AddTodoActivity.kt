package com.todoer.ui.main.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.todoer.R
import com.todoer.data.api.RetrofitService
import com.todoer.data.local.TodoListDatabase
import com.todoer.data.model.Todo
import com.todoer.data.repository.MainRepository
import com.todoer.databinding.ActivityAddTodoBinding
import com.todoer.ui.main.viewmodel.MainViewModel
import com.todoer.ui.main.viewmodel.MyViewModelFactory
import com.todoer.utils.Extensions.getCheckedRadioButtonPosition
import com.todoer.utils.MainActivityWorkManager
import com.todoer.utils.Repeat
import java.util.*
import java.util.concurrent.TimeUnit

class AddTodoActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var binding: ActivityAddTodoBinding
    private var todoDatabase: TodoListDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_todo)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        todoDatabase = TodoListDatabase.getInstance(this)
        val retrofitService = RetrofitService.getInstance()
        val mainRepository = MainRepository(todoDatabase, retrofitService)

        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(mainRepository)
        ).get(MainViewModel::class.java)

        binding.viewModel = viewModel

        val title = intent.getStringExtra("title")
        if (title == null || title == "") {
            supportActionBar?.title = getString(R.string.add)
            binding.addTodo.setOnClickListener {
                val todo = Todo(
                    binding.titleEd.text.toString(),
                    binding.detailEd.text.toString(),
                    binding.radioGroup.getCheckedRadioButtonPosition(),
                    Date()
                )
                todo.detail = binding.detailEd.text.toString()
                viewModel.addTodo(false, todo)
            }
        } else {
            supportActionBar?.title = getString(R.string.update)
            binding.addTodo.text = getString(R.string.update)
            val repeat = intent.getIntExtra("repeat", -1)
            val detail = intent.getStringExtra("detail")
            val tId = intent.getIntExtra("tId", 0)
            binding.titleEd.setText(title)
            binding.detailEd.setText(detail)
            if (-1 != repeat) {
                binding.radioGroup.check((binding.radioGroup.getChildAt(repeat) as RadioButton).id)
            }
            binding.addTodo.setOnClickListener {
                val todo = Todo(
                    binding.titleEd.text.toString(),
                    binding.detailEd.text.toString(),
                    binding.radioGroup.getCheckedRadioButtonPosition(),
                    Date(),
                    tId
                )
                todo.detail = binding.detailEd.text.toString()
                viewModel.addTodo(true, todo)
            }
        }

        viewModel.addTodoSuccess.observe(this, {
            val repeatInterval: Long = when (binding.radioGroup.getCheckedRadioButtonPosition()) {
                Repeat.DAILY.ordinal -> 24
                Repeat.WEEKLY.ordinal -> 168
                else -> 0
            }
            if (repeatInterval != 0L) {
                createWorkManager(repeatInterval)
                Toast.makeText(applicationContext, "Notification created", Toast.LENGTH_SHORT)
                    .show()
            }

            finish()
        })

        viewModel.successMessage.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.errorMessage.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.loading.observe(this, {
            if (it) {
                binding.progressDialog.visibility = View.VISIBLE
            } else {
                binding.progressDialog.visibility = View.GONE
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createWorkManager(repeatInterval: Long) {
        val constraints = Constraints.Builder()
            .build()
        val workRequest =
            PeriodicWorkRequestBuilder<MainActivityWorkManager>(repeatInterval, TimeUnit.HOURS)
//                    .setInitialDelay(repeatInterval, TimeUnit.HOURS)
                .setInputData(
                    workDataOf(
                        "title" to binding.titleEd.text.toString(),
                        "detail" to binding.detailEd.text.toString()
                    )
                )
                .setConstraints(constraints)
                .build()

        WorkManager
            .getInstance(applicationContext)
            .enqueue(workRequest)

    }
}