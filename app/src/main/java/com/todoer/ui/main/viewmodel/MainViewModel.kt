package com.todoer.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.todoer.data.model.Todo
import com.todoer.data.repository.MainRepository
import kotlinx.coroutines.*

class MainViewModel constructor(private val mainRepository: MainRepository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val successMessage = MutableLiveData<String>()
    val addTodoSuccess = MutableLiveData<Boolean>()
    val deleteTodoSuccess = MutableLiveData<Boolean>()
    val todoList = MutableLiveData<List<Todo>>()
    val addButtonClicked = MutableLiveData<Boolean>()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    val loading = MutableLiveData<Boolean>()

    fun getAllTodos() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response = mainRepository.getloadAllTasks()
            withContext(Dispatchers.Main){
                if (response!=null) {
                    todoList.value = response
                    loading.value = false
                } else {
                    onError("Error: Database Error")
                }
            }
        }
    }

    fun addTodo(isUpdate: Boolean, todo: Todo){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response = (if (isUpdate) {
                mainRepository.updateTodo(todo)
            } else {
                mainRepository.addTodo(todo)
            })
            withContext(Dispatchers.Main){
                if (response!=null) {
                    onSuccess(if(isUpdate) "Updated" else "Added")
                    addTodoSuccess.postValue(true)
                } else {
                    onError("Error: Database Error")
                }
            }
        }
    }

    fun removeTodo(todo: Todo){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response = mainRepository.removeTodo(todo)
            withContext(Dispatchers.Main){
                if (response!=null) {
                    onSuccess("Deleted")
                    deleteTodoSuccess.postValue(true)
                } else {
                    onError("Error: Database Error")
                }
            }
        }
    }

    fun onAddButtonClicked(){
        addButtonClicked.value = true
    }

    private fun onSuccess(message: String) {
        successMessage.value = message
        loading.value = false
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    init {
        getAllTodos()
    }

}