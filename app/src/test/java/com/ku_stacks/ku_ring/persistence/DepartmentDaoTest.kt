package com.ku_stacks.ku_ring.persistence

import com.ku_stacks.ku_ring.LocalDbAbstract
import com.ku_stacks.ku_ring.MockUtil
import com.ku_stacks.ku_ring.data.db.DepartmentDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class DepartmentDaoTest : LocalDbAbstract() {
    private lateinit var departmentDao: DepartmentDao

    @Before
    fun setup() {
        departmentDao = db.departmentDao()
    }

    @Test
    fun `insertDepartment and updateDepartment test`() = runTest {
        // given
        assert(departmentDao.isEmpty())
        val entity = MockUtil.mockDepartmentEntity()
        departmentDao.insertDepartment(entity)
        assertFalse(departmentDao.isEmpty())

        // when
        val newShortName = "ict"
        val newKoreanName = "스융공"
        departmentDao.updateDepartment(
            name = entity.name,
            shortName = newShortName,
            koreanName = newKoreanName
        )

        // then
        val result = departmentDao.getDepartmentsByName(entity.name)
        assert(result.isNotEmpty())
        assertEquals(1, result.size)
        assertEquals(result[0], entity.copy(shortName = newShortName, koreanName = newKoreanName))
    }
}