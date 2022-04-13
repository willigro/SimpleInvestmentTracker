package com.rittmann.crypto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rittmann.common.lifecycle.DefaultDispatcherProvider
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
open class BaseActivityTest<A : AppCompatActivity> {

    lateinit var activity: A
    lateinit var activityController: ActivityController<A>

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val component: TestAppComponent = DaggerTestAppComponent.builder()
            .application(TestApplication())
            .dispatcherProvider(DefaultDispatcherProvider())
            .build()

        component.inject(DefaultDispatcherProvider())
    }

    inline fun <reified B : AppCompatActivity> configureActivity() {
        activityController = Robolectric.buildActivity(B::class.java) as ActivityController<A>
        activity = activityController.get()
    }

    inline fun <reified B : AppCompatActivity> configureActivity(intent: Intent) {
        activityController = Robolectric.buildActivity(B::class.java, intent) as ActivityController<A>
        activity = activityController.get()
    }

    fun forceResume() {
        activityController.create()
        activityController.start()
        activityController.resume()
    }

    fun forceResumeFromCreate() {
        activityController.start()
        activityController.resume()
    }

    fun getString(res: Int) = RuntimeEnvironment.application.getString(res)
    fun getContext() = RuntimeEnvironment.application
}