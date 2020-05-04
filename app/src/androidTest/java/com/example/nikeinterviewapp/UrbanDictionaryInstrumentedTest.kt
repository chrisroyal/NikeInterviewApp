package com.example.nikeinterviewapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.runner.AndroidJUnit4
import com.example.nikeinterviewapp.ui.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)

class UrbanDictionaryInstrumentedTest {

    @get:Rule

    val activityRule = ActivityTestRule(MainActivity::class.java)

    class ActivityTestRule(java: Class<MainActivity>) {

    }


    @Test
    fun whenActivityIsLaunched_shouldDisplayInitialState() {

        // View that the query hint descripiton is present

        onView(withContentDescription(R.string.query_hint))

            .check(matches(isDisplayed()))

    }

}