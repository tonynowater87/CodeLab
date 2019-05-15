package com.tonynowater.recyclerview_selection_test

import android.view.InputDevice
import android.view.MotionEvent
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.tonynowater.recyclerview_selection_test", appContext.packageName)
    }

    @Test
    fun delete_position_0() {
        enableSelectionMode()
        onView(withId(R.id.action_delete)).perform(click())
        onView(withId(R.id.reccyclerview)).check(matches(isDisplayed()))
    }

    @Test
    fun delete_all() {
        enableSelectionMode()
        onView(withId(R.id.action_select_all)).perform(click())
        onView(withId(R.id.action_delete)).perform(click())
        onView(withId(R.id.reccyclerview)).check(RecyclerViewItemCountAssertion(0))
    }

    @Test
    fun delete_multi_rows() {
        enableSelectionMode()
        onView(withId(R.id.action_select_all)).perform(click())
        onView(withId(R.id.action_delete)).perform(click())
        onView(withId(R.id.reccyclerview)).check(RecyclerViewItemCountAssertion(0))
        var total = 40
        for (i in 0 until total) {
            onView(withId(R.id.fab)).perform(click())
            if (i == 0) {
                enableSelectionMode()
                continue
            }
            if (i % 2 == 1) {
                onView(withId(R.id.reccyclerview)).perform(
                    RecyclerViewActions.actionOnItemAtPosition<MyListAdapter.MyViewHolder>(
                        i,
                        click()
                    )
                )
            }
        }
        onView(withId(R.id.reccyclerview)).check(RecyclerViewItemCountAssertion(total))
        onView(withId(R.id.reccyclerview)).perform(swipeUp())
        sleep()
        onView(withId(R.id.reccyclerview)).perform(swipeDown())
        sleep()
        onView(withId(R.id.action_delete)).perform(click())
        total /= 2
        println("total:$total")
        for (i in 0 until total) {
            println("index:$i")
            if (i == 0) {
                enableSelectionMode()
                continue
            }
            if (i <= 5) {
                onView(withId(R.id.reccyclerview)).perform(
                    RecyclerViewActions.actionOnItemAtPosition<MyListAdapter.MyViewHolder>(
                        i,
                        click()
                    )
                )
            }
        }
        sleep()
        onView(withId(R.id.reccyclerview)).perform(swipeUp())
        sleep()
        onView(withId(R.id.reccyclerview)).perform(swipeDown())
        sleep()
        onView(withId(R.id.action_delete)).perform(click())
        sleep()
        onView(withId(R.id.reccyclerview)).perform(swipeUp())
        sleep()
        onView(withId(R.id.reccyclerview)).perform(swipeDown())
        sleep()
    }

    @Test
    fun clickOutSideToCancel() {
        enableSelectionMode()
        onView(withId(R.id.action_select_all)).perform(click())
        onView(withId(R.id.action_delete)).perform(click())
        onView(withId(R.id.reccyclerview)).check(RecyclerViewItemCountAssertion(0))
        var total = 40
        for (i in 0 until total) {
            onView(withId(R.id.fab)).perform(click())
            if (i == 0) {
                enableSelectionMode()
                continue
            }
            if (i % 2 == 1) {
                onView(withId(R.id.reccyclerview)).perform(
                    RecyclerViewActions.actionOnItemAtPosition<MyListAdapter.MyViewHolder>(
                        i,
                        click()
                    )
                )
            }
        }
        onView(withId(R.id.reccyclerview)).check(RecyclerViewItemCountAssertion(total))
        sleep()
        onView(withId(R.id.reccyclerview)).perform(swipeUp())
        sleep()
        onView(withId(R.id.reccyclerview)).perform(swipeUp())
        sleep()
        onView(withId(R.id.cl_root)).perform(clickInPercent(pctX = 0.5F, pctY = 0.0015F))
        sleep()
        onView(withId(R.id.reccyclerview)).perform(swipeDown())
        sleep()
        onView(withId(R.id.reccyclerview)).perform(swipeDown())
        total /= 2
        println("total:$total")
        for (i in 0 until total) {
            println("index:$i")
            if (i == 0) {
                enableSelectionMode()
                continue
            }
            if (i <= 5) {
                onView(withId(R.id.reccyclerview)).perform(
                    RecyclerViewActions.actionOnItemAtPosition<MyListAdapter.MyViewHolder>(
                        i,
                        click()
                    )
                )
            }
        }
        sleep()
        onView(withId(R.id.reccyclerview)).perform(swipeUp())
        sleep()
        onView(withId(R.id.reccyclerview)).perform(swipeUp())
        sleep()
        onView(withId(R.id.cl_root)).perform(clickInPercent(pctX = 0.5F, pctY = 0.995F))
        sleep()
        onView(withId(R.id.reccyclerview)).perform(swipeDown())
        sleep()
        onView(withId(R.id.reccyclerview)).perform(swipeDown())
        sleep(duration = 1000 * 10)
    }

    private fun sleep(duration: Long = 1000) {
        Thread.sleep(duration)
    }

    private fun enableSelectionMode() {
        onView(withId(R.id.reccyclerview)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MyListAdapter.MyViewHolder>(0, longClick())
        )
    }

    companion object {
        fun clickInPercent(pctX: Float, pctY: Float): ViewAction {
            return GeneralClickAction(
                Tap.SINGLE,
                CoordinatesProvider { view ->
                    val screenPos = IntArray(2)
                    view.getLocationOnScreen(screenPos)
                    val w = view.width
                    val h = view.height

                    val x = w * pctX
                    val y = h * pctY

                    val screenX = screenPos[0] + x
                    val screenY = screenPos[1] + y
                    floatArrayOf(screenX, screenY)
                },
                Press.FINGER,
                InputDevice.SOURCE_MOUSE,
                MotionEvent.BUTTON_PRIMARY)
        }
    }
}
