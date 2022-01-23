package com.todoer.ui.main.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.todoer.R
import com.todoer.data.api.RetrofitService
import com.todoer.data.local.TodoListDatabase
import com.todoer.data.model.Todo
import com.todoer.data.repository.MainRepository
import com.todoer.databinding.ActivityMainBinding
import com.todoer.ui.main.adapter.TodoAdapter
import com.todoer.ui.main.viewmodel.MainViewModel
import com.todoer.ui.main.viewmodel.MyViewModelFactory

class MainActivity : AppCompatActivity(), TodoAdapter.OnTodoItemClickedListener {

    lateinit var viewModel: MainViewModel
    private val adapter = TodoAdapter()
    lateinit var binding: ActivityMainBinding
    private var todoDatabase: TodoListDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val retrofitService = RetrofitService.getInstance()
        todoDatabase = TodoListDatabase.getInstance(this)
        adapter.setTodoItemClickedListener(this)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.hasFixedSize()
        val mainRepository = MainRepository(todoDatabase, retrofitService)

        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(mainRepository)
        ).get(MainViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.todoList.observe(this, {
            adapter.setTodos(it)
        })

        viewModel.deleteTodoSuccess.observe(this, {
            viewModel.getAllTodos()
        })

        viewModel.addButtonClicked.observe(this, {
            if (it) {
                startActivity(Intent(this, AddTodoActivity::class.java))
            }
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

    override fun onResume() {
        super.onResume()
        viewModel.getAllTodos()
    }

    override fun onTodoItemClicked(todo: Todo) {
        viewModel.removeTodo(todo)
    }

    override fun onTodoItemLongClicked(todo: Todo) {
        val intent = Intent(this, AddTodoActivity::class.java)
        intent.putExtra("tId", todo.tId)
        intent.putExtra("title", todo.title)
        intent.putExtra("detail", todo.detail)
        intent.putExtra("repeat", todo.repeat)
        startActivity(intent)
    }

}