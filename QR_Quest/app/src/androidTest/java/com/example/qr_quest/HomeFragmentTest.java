package com.example.qr_quest;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;



import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static java.util.regex.Pattern.matches;


@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest {

    @Test
    public void testEventFragment() {
        FragmentScenario<HomeFragment> scenario = FragmentScenario.launchInContainer(HomeFragment.class);
        scenario.moveToState(Lifecycle.State.RESUMED);
        assertNotNull(scenario);
    }
}
