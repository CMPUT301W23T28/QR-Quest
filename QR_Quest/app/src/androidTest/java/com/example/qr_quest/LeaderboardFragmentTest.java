package com.example.qr_quest;

import static org.junit.Assert.assertNotNull;


import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LeaderboardFragmentTest {
    private FragmentScenario<LeaderboardFragment> scenario;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp(){
        scenario = FragmentScenario.launchInContainer(LeaderboardFragment.class);
        scenario.moveToState(Lifecycle.State.RESUMED);

    }

    @Test
    public void testEventFragment() {
        Espresso.onView(ViewMatchers.withId(R.id.txtview_leaderboard_points_option))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.txtview_leaderboard_qr_collected_option))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.txtview_leaderboard_top_qr_option))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view_leaderboard))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}

