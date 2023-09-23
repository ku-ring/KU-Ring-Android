package com.ku_stacks.ku_ring.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.ku_stacks.ku_ring.SchedulersTestRule
import com.ku_stacks.ku_ring.data.api.DepartmentClient
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [24])
class SubscribeRepositoryTest {

    private lateinit var repository: SubscribeRepository
    private val departmentClient: DepartmentClient = Mockito.mock(DepartmentClient::class.java)
    private lateinit var pref: PreferenceUtil

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val testSchedulersRule = SchedulersTestRule()

    @Before
    fun setup() {
        pref = PreferenceUtil(getApplicationContext())
        repository = SubscribeRepositoryImpl(departmentClient, pref)
    }

    @Test
    fun `save Subscription To Local Test`() {
        // given
        val mockData = arrayListOf("학사", "취창업")

        // when
        pref.saveSubscriptionFromKorean(mockData)

        // then
        val expected = arrayListOf("bch", "emp").toSet()
        assertEquals(expected, pref.subscription)
    }
}
