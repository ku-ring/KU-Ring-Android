package com.ku_stacks.ku_ring.push

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.ku_stacks.ku_ring.push.local.PushDatabase
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [24])
abstract class LocalDbAbstract {
    lateinit var db: PushDatabase

    @Before
    fun initDB() {
        db = Room.inMemoryDatabaseBuilder(getApplicationContext(), PushDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDB() {
        db.close()
    }
}