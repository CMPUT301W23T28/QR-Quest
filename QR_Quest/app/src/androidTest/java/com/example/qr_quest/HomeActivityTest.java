package com.example.qr_quest;

import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {
    private Solo solo;
    private HomeActivity activity;

    @Rule
    public ActivityTestRule<HomeActivity> rule =
            new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.assertCurrentActivity("Home Activity", HomeActivity.class);
    }

    @Test
    public void testLeaderBoardFragmentNavigation() {
        assertTrue("bottom navigation", solo.waitForView(R.id.bottonnav));
        assertTrue("leaderboard icon", solo.waitForView(R.id.leaderboard));
        solo.clickOnView(solo.getView(R.id.leaderboard));
        solo.waitForFragmentById(R.layout.fragment_leaderboard);
    }

    @Test
    public void testHomeFragmentNavigation() {
        assertTrue("bottom navigation", solo.waitForView(R.id.bottonnav));
        assertTrue("home icon", solo.waitForView(R.id.home));
        solo.clickOnView(solo.getView(R.id.home));
        solo.waitForFragmentById(R.layout.fragment_home);
    }

    @Test
    public void testProfileFragmentNavigation() {
        assertTrue("bottom navigation", solo.waitForView(R.id.bottonnav));
        assertTrue("profile icon", solo.waitForView(R.id.profile));
        solo.clickOnView(solo.getView(R.id.profile));
        solo.waitForFragmentById(R.layout.fragment_profile);
    }

    @Test
    public void testMapFragmentNavigation() {
        assertTrue("bottom navigation", solo.waitForView(R.id.bottonnav));
        assertTrue("map icon", solo.waitForView(R.id.search));
        solo.clickOnView(solo.getView(R.id.search));
        solo.waitForFragmentById(R.layout.fragment_maps);
    }

    @Test
    public void testCameraActivityNavigation() {
        assertTrue("bottom navigation", solo.waitForView(R.id.bottonnav));
        assertTrue("camera icon", solo.waitForView(R.id.camera));
        solo.clickOnView(solo.getView(R.id.camera));
        solo.waitForFragmentById(R.layout.activity_camera);
    }


}
