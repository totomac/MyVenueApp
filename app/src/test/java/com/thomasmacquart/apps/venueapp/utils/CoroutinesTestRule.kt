package com.thomasmacquart.apps.venueapp.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.coroutines.ContinuationInterceptor

/**
 * Test rule that can make coroutine test easier (going from one thread to another), in this class the test rule is triggered when
 * the test start and when it finish in which case it overrides the Dispatcher.Main and all the coroutine that use that test dispatcher's
 * @see kotlinx.coroutines.test.runBlockingTest would run in synchronously in the test
 *
 * to use this class, inside the test class add the following
 * @ExperimentalCoroutinesApi
 * @get:Rule
 * var coroutinesTestRule = CoroutinesTestRule()
 *
 * then in the method that calls a coroutine use
 * coroutinesTestRule.testDispatcher.runBlockingTest {} instead of runBloacking
 */
@ExperimentalCoroutinesApi
class CoroutinesTestRule(
) : TestWatcher(), TestCoroutineScope by TestCoroutineScope() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(this.coroutineContext[ContinuationInterceptor] as CoroutineDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}