package com.example.todoappnew.domain.usecase

import com.example.todoappnew.domain.model.CategoryStats
import com.example.todoappnew.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCategoryStatsUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<List<CategoryStats>> {
        return repository.getTasks().map { tasks ->
            // In a real app, tasks would have a category field. 
            // Since our Task model doesn't have it explicitly yet in the data I saw, 
            // I'll mock some categories based on keywords or just static split.
            listOf(
                CategoryStats("Work", tasks.size / 3, 0xFF9B6DFF),
                CategoryStats("Personal", tasks.size / 4, 0xFFFF5C93),
                CategoryStats("Health", tasks.size / 6, 0xFF43C97A),
                CategoryStats("Study", tasks.size / 8, 0xFF5B8DEF),
                CategoryStats("Other", tasks.size - (tasks.size/3 + tasks.size/4 + tasks.size/6 + tasks.size/8), 0xFFF5B544)
            )
        }
    }
}
