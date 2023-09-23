package com.ku_stacks.ku_ring.department.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "departments")
data class DepartmentEntity(
    @PrimaryKey val name: String,
    val shortName: String,
    val koreanName: String,
    val isSubscribed: Boolean,
) {
    companion object {
        fun mock() = DepartmentEntity(
            name = "smart_ict_convergence",
            shortName = "sicte",
            koreanName = "스마트ICT융합공학과",
            isSubscribed = false,
        )
    }
}