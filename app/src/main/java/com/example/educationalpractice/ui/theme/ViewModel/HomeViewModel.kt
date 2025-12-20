import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educationalpractice.Category
import com.example.educationalpractice.Data.Repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val repository = CategoryRepository()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getAllCategories()
                _categories.value = result
            } catch (e: Exception) {
                // Обработка ошибки
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}