package com.rittmann.crypto

import android.app.Activity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.rittmann.common.lifecycle.DefaultDispatcherProvider
import com.rittmann.common.utils.DataBindingIdlingResource
import com.rittmann.common.utils.EspressoIdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import androidx.test.espresso.NoMatchingViewException

import androidx.test.espresso.ViewAssertion

import androidx.test.espresso.matcher.ViewMatchers.isRoot

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@LargeTest
open class BaseTestActivity {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

//    // Set the main coroutines dispatcher for unit testing.
//    @ExperimentalCoroutinesApi
//    @get:Rule
//    var mainCoroutineRule = MainCoroutineRule()

    // An idling resource that waits for Data Binding to have no pending bindings.
    protected val dataBindingIdlingResource = DataBindingIdlingResource()

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    @Before
    fun registerIdlingResource() {
        val component: TestAppComponent = DaggerTestAppComponent.builder()
            .application(TestApplication())
            .dispatcherProvider(DefaultDispatcherProvider())
            .build()

        component.inject(DefaultDispatcherProvider())

        // If i won't use it, I'll keep here just for check
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)

        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)

        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    inline fun <reified A : Activity> getActivity(): ActivityScenario<A> {
        return ActivityScenario.launch(A::class.java)
    }

//    @Throws(Throwable::class)
//    open fun getCurrentActivity(): Activity? {
//        val activity = arrayOfNulls<Activity>(1)
//        onView(isRoot()).check { view, _ ->
//            if (view.context is Activity)
//                activity[0] = view.context as Activity
//        }
//        return activity[0]
//    }
}