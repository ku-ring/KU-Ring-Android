package com.ku_stacks.ku_ring.department

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.ku_stacks.ku_ring.department.local.DepartmentDatabase
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [24])
abstract class LocalDbAbstract {
    lateinit var db: DepartmentDatabase

    @Before
    fun initDB() {
        db = Room.inMemoryDatabaseBuilder(getApplicationContext(), DepartmentDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDB() {
        db.close()
    }
}