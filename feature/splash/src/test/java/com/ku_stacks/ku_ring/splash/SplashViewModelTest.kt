package com.ku_stacks.ku_ring.splash

import com.ku_stacks.ku_ring.department.repository.DepartmentRepository
import com.ku_stacks.ku_ring.space.repository.KuringSpaceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class SplashViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val kuringSpaceRepository: KuringSpaceRepository =
        Mockito.mock(KuringSpaceRepository::class.java)
    private val departmentRepository: DepartmentRepository =
        Mockito.mock(DepartmentRepository::class.java)
    private lateinit var viewModel: SplashViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun init() {
        Dispatchers.setMain(dispatcher)
        viewModel = SplashViewModel(kuringSpaceRepository, departmentRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `minimum version is larger than current app version`() = runTest {
        executeTest("2.0.1", "2.0.0", SplashScreenState.UPDATE_REQUIRED)
    }

    @Test
    fun `minimum version is equal to current app version`() = runTest {
        executeTest("2.0.0", "2.0.0", SplashScreenState.UPDATE_NOT_REQUIRED)
    }

    @Test
    fun `minimum version is less than current app version`() = runTest {
        executeTest("1.10.0", "2.0.0", SplashScreenState.UPDATE_NOT_REQUIRED)
    }

    @Test
    fun `minimum version is less than current app version 2`() = runTest {
        executeTest("9.9.9", "10.0.0", SplashScreenState.UPDATE_NOT_REQUIRED)
    }

    @Test
    fun `update is not needed when exception occurs`() = runTest {
        executeTest("2.0.0", RuntimeException::class.java)
    }

    private suspend fun executeTest(
        minimumVersion: String,
        currentVersion: String,
        expected: SplashScreenState,
    ) {
        Mockito.`when`(kuringSpaceRepository.getMinimumAppVersion()).thenReturn(minimumVersion)
        viewModel.checkUpdateRequired(currentVersion).join()

        assertEquals(expected, viewModel.splashScreenState.value)
    }

    private suspend fun executeTest(
        currentVersion: String,
        exceptionClass: Class<out Exception>,
    ) {
        Mockito.`when`(kuringSpaceRepository.getMinimumAppVersion()).thenThrow(exceptionClass)
        viewModel.checkUpdateRequired(currentVersion).join()

        assertEquals(SplashScreenState.UPDATE_NOT_REQUIRED, viewModel.splashScreenState.value)
    }

}