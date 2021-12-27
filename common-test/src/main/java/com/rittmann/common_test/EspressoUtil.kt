package com.rittmann.common_test

import android.app.Activity
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.rittmann.common_test.CustomMatchers.hasChildren
import com.rittmann.common_test.CustomMatchers.withHtml
import com.rittmann.common_test.CustomViewActions.setTextInTextView
import com.rittmann.common_test.WithIdMatcherConcat.withIdConcatened
import com.rittmann.common_test.drawable.DrawableMatcher
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher

object EspressoUtil {

    fun exists(id: Int, wait: Long = 1_000){
        viewExists(withId(id), wait)
    }

    fun checkValue(id: Int, value: String, scroll: Boolean = false) {
        onView(withId(id)).apply {
            if (scroll)
                perform(scrollTo())
            check(matches(isValueEqualTo(value)))
        }
    }

    fun checkValueSpinner(id: Int, value: String, scroll: Boolean = false) {
        onView(withId(id)).apply {
            if (scroll)
                perform(scrollTo())
            check(matches(withSpinnerText(containsString(value))))
        }
    }

    fun checkValueSpinner(id: Int, idConcat: Int, value: String, scroll: Boolean = false) {
        onView(withIdConcatened(id, idConcat)).apply {
            if (scroll)
                perform(scrollTo())
            check(matches(withSpinnerText(containsString(value))))
        }
    }

    fun checkValue(id: Int, idConcat: Int, value: String, scroll: Boolean = false) {
        onView(withIdConcatened(id, idConcat)).apply {
            if (scroll)
                perform(scrollTo())
            check(matches(isValueEqualTo(value)))
        }
    }

    fun checkValueWithScroll(id: Int, value: String) {
        onView(withId(id)).perform(scrollTo()).check(matches(isValueEqualTo(value)))
    }

    fun performBack(id: Int, withScroll: Boolean = false) {
        onView(withId(id)).apply {
            if (withScroll)
                perform(scrollTo())
            perform(ViewActions.pressBack())
        }
    }

    fun performClick(id: Int, withScroll: Boolean = false) {
        onView(withId(id)).apply {
            if (withScroll)
                perform(scrollTo())
            perform(ViewActions.click())
        }
    }

    fun performClick(id: Int, idConcat: Int, withScroll: Boolean = false) {
        onView(withIdConcatened(id, idConcat)).apply {
            if (withScroll)
                perform(scrollTo())
            perform(ViewActions.click())
        }
    }

    fun putValue(id: Int, value: String, withScroll: Boolean = false) {
        onView(withId(id)).apply {
            if (withScroll)
                perform(scrollTo())
            perform(ViewActions.replaceText(value), ViewActions.closeSoftKeyboard())
        }
    }

    fun performClick(value: String, withScroll: Boolean = false) {
        onView(withText(value)).apply {
            if (withScroll)
                perform(scrollTo())
            perform(ViewActions.click())
        }
    }

    fun putValue(id: Int, idConcat: Int, value: String, withScroll: Boolean = false) {
        onView(withIdConcatened(id, idConcat)).apply {
            if (withScroll)
                perform(scrollTo())
            perform(ViewActions.replaceText(value), ViewActions.closeSoftKeyboard())
        }
    }

    fun viewIsVisible(id: Int, withScroll: Boolean = false) {
        onView(withId(id)).apply {
            if (withScroll)
                perform(scrollTo())
        }.isVisible()
    }

    fun viewIsGone(id: Int, withScroll: Boolean = false) {
        onView(withId(id)).isGone()
    }

    fun viewIsChecked(id: Int, withScroll: Boolean = false) {
        onView(withId(id)).apply {
            if (withScroll)
                perform(scrollTo())

            check(matches(isChecked()))
        }
    }

    fun viewIsNotChecked(id: Int, withScroll: Boolean = false) {
        onView(withId(id)).apply {
            if (withScroll)
                perform(scrollTo())

            check(matches(not(isChecked())))
        }
    }

    private fun getViewAssertion(visibility: ViewMatchers.Visibility): ViewAssertion? {
        return matches(ViewMatchers.withEffectiveVisibility(visibility))
    }

    fun ViewInteraction.isGone() = getViewAssertion(ViewMatchers.Visibility.GONE)

    fun ViewInteraction.isVisible() = getViewAssertion(ViewMatchers.Visibility.VISIBLE)

    fun ViewInteraction.isInvisible() = getViewAssertion(ViewMatchers.Visibility.INVISIBLE)


    fun isValueEqualTo(content: String): Matcher<View> {

        return object : TypeSafeMatcher<View>() {

            override fun describeTo(description: Description) {
                description.appendText("Match Edit Text Value with View ID Value : :  $content")
            }

            override fun matchesSafely(view: View?): Boolean {
                if (view !is TextView && view !is EditText) {
                    return false
                }
                val text: String = if (view is TextView) {
                    view.text.toString()
                } else {
                    (view as EditText).text.toString()
                }

                return text.equals(content, ignoreCase = true)
            }
        }
    }

    fun checkValueFromHtml(id: Int, value: String, withScroll: Boolean = false) {
        onView(withId(id)).inRoot(RootMatchers.isDialog()).apply {
            if (withScroll)
                perform(scrollTo())
        }.check(matches(withHtml(value)))
    }

    fun checkValue(value: String, withScroll: Boolean = false) {
        onView(CoreMatchers.allOf(withText(value))).apply {
            if (withScroll)
                perform(scrollTo())
        }.check(matches(ViewMatchers.isDisplayed()))
    }

    fun checkValueDialog(value: String, withScroll: Boolean = false) {
        onView(CoreMatchers.allOf(withText(value)))
            .inRoot(isDialog())
            .apply {
                if (withScroll)
                    perform(scrollTo())
            }
            .check(matches(ViewMatchers.isDisplayed()))
    }

    fun checkValueError(id: Int, value: String, withScroll: Boolean = false) {
        onView(withId(id)).check(matches(ViewMatchers.hasErrorText(value)))
    }

//    fun checkValueRecycler(recyclerId: Int, targetId: Int, position: Int, value: String) {
//        onView(withRecyclerView(recyclerId).atPositionOnView(position, targetId)).check(
//                matches(withText(value))
//        )
//    }

    fun viewIsChecked(id: Int) {
        onView(withId(id)).check(matches(isChecked()))
    }

    fun viewIsNotChecked(id: Int) {
        onView(withId(id)).check(matches(CoreMatchers.not(isChecked())))
    }

//    fun performClickRecycler(recyclerId: Int, position: Int) {
//        onView(withRecyclerView(recyclerId).atPosition(position)).perform(ViewActions.click())
//    }

    fun performClickByTag(tag: String, withScroll: Boolean = false) {
        onView(ViewMatchers.withTagValue(Matchers.`is`(tag as Any))).apply {
            if (withScroll)
                perform(scrollTo())
            perform(ViewActions.click())
        }
    }

    fun putValueTextView(id: Int, value: String) {
        onView(withId(id)).perform(setTextInTextView(value))
    }

    fun hasBackground(id: Int, idConcat: Int, resId: Int) {
        onView(
            CoreMatchers.allOf(
                withIdConcatened(id, idConcat),
                ViewMatchers.hasBackground(resId),
                ViewMatchers.isDisplayed()
            )
        )
    }

    fun hasBackground(id: Int, resId: Int) {
        onView(
            CoreMatchers.allOf(
                withId(id),
                ViewMatchers.hasBackground(resId),
                ViewMatchers.isDisplayed()
            )
        )
    }

    fun hasBackgroundByTag(tag: String, resId: Int) {
        onView(
            CoreMatchers.allOf(
                ViewMatchers.withTagValue(Matchers.`is`(tag as Any)),
                ViewMatchers.hasBackground(resId),
                ViewMatchers.isDisplayed()
            )
        )
    }

    fun childrenOnViewByTag(tag: String, count: Int, withScroll: Boolean = false) {
        onView(ViewMatchers.withTagValue(Matchers.`is`(tag as Any))).apply {
            if (withScroll)
                perform(scrollTo())
        }.check(
            matches(
                CoreMatchers.allOf(
                    ViewMatchers.isDisplayed(),
                    hasChildren(Matchers.`is`(count))
                )
            )
        )
    }

    fun scrollToById(id: Int) {
        onView(CoreMatchers.allOf(withId(id), ViewMatchers.isDisplayed())).perform(scrollTo())
    }

    fun viewIsDisplayed(id: Int, withScroll: Boolean = false) {
        onView(withId(id)).apply {
            if (withScroll)
                perform(scrollTo())
        }.check(matches(ViewMatchers.isDisplayed()))
    }

    fun viewIsNotDisplayed(id: Int, withScroll: Boolean = false) {
        onView(withId(id)).apply {
            if (withScroll)
                perform(scrollTo())
        }.check(matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
    }

    fun viewDoesNotExists(value: String) {
        onView(withText(value)).check(ViewAssertions.doesNotExist())
    }

    fun isEnabled(id: Int, withScroll: Boolean = false) {
        onView(withId(id)).apply {
            if (withScroll)
                perform(scrollTo())

            check(matches(ViewMatchers.isEnabled()))
        }
    }

    fun isDisabled(id: Int, withScroll: Boolean = false) {
        onView(withId(id)).apply {
            if (withScroll)
                perform(scrollTo())

            check(matches(CoreMatchers.not(ViewMatchers.isEnabled())))
        }
    }

    fun checkInputType(id: Int, inputType: Int) {
        onView(withId(id)).check(matches(CoreMatchers.allOf(ViewMatchers.withInputType(inputType))))
    }

    fun checkInputTypeIsNot(id: Int, inputType: Int) {
        onView(withId(id)).check(
            matches(
                CoreMatchers.not(
                    CoreMatchers.allOf(
                        ViewMatchers.withInputType(
                            inputType
                        )
                    )
                )
            )
        )
    }

    fun checkToast(value: String) {
        onView(withText(value)).inRoot(
            RootMatchers.withDecorView(
                not(
                    getCurrentActivity()!!.window.decorView
                )
            )
        ).check(
            matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    fun scrollToBottom(resId: Int) {
        onView(withId(resId)).perform(CustomViewActions.ScrollToBottomAction())
    }

    fun withColor(id: Int, resId: Int, color: ColorFilter) {
        onView(withId(id)).check(
            matches(
                DrawableMatcher(
                    resId,
                    color,
                    object : DrawableMatcher.Extractor {
                        override fun extract(v: View?): Drawable? {
                            return v?.background
                        }
                    })
            )
        )
    }

    fun buttonWithColor(id: Int, resId: Int, color: ColorFilter) {
        onView(withId(id)).check(
            matches(
                DrawableMatcher(
                    resId,
                    color,
                    object : DrawableMatcher.Extractor {
                        override fun extract(v: View?): Drawable? {
                            val targetImageView: Button = v as Button
                            return targetImageView.background
                        }
                    })
            )
        )
    }

    class ExecuteOn(private val callTime: Int) {
        var current = 1

        fun next(callback: () -> Unit) {
            if (current == callTime)
                callback()
            current++
        }
    }

    @Throws(Throwable::class)
    fun getCurrentActivity(): Activity? {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        val activity = arrayOfNulls<Activity>(1)
        onView(ViewMatchers.isRoot()).check { _, _ ->
            val activities =
                ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
            activity[0] = Iterables.getOnlyElement(activities)
        }
        return activity[0]
    }

    fun pressBack() {
        onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack())
    }

    fun waitToast() {
        Thread.sleep(3_000)
    }
}