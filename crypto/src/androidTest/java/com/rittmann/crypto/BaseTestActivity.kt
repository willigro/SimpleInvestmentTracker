package com.rittmann.crypto

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.FragmentActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.rittmann.common.datasource.dao.config.AppDatabase
import com.rittmann.common.lifecycle.DefaultDispatcherProvider
import com.rittmann.common.utils.DataBindingIdlingResource
import com.rittmann.common.utils.EspressoIdlingResource
import com.rittmann.common.utils.monitorActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith


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
    val dataBindingIdlingResource = DataBindingIdlingResource()

    val dataBase = AppDatabase.getDatabase(ApplicationProvider.getApplicationContext())

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

    inline fun <reified A : FragmentActivity> getActivity(monitor: Boolean = false): ActivityScenario<A> {
        return ActivityScenario.launch(A::class.java).apply {
            if (monitor)
                dataBindingIdlingResource.monitorActivity(this as ActivityScenario<A>)
        }
    }

    inline fun <reified A : FragmentActivity> getActivity(
        intent: Intent,
        monitor: Boolean = false
    ): ActivityScenario<A> {
        return ActivityScenario.launch<A>(intent).apply {
            if (monitor)
                dataBindingIdlingResource.monitorActivity(this as ActivityScenario<A>)
        }
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