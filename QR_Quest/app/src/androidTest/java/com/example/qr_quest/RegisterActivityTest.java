package com.example.qr_quest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {
    private Solo solo;
    private RegisterActivity activity;
    EditText usernameEditText, emailEditText, fNameEditText, lNameEditText, phoneEditText;

    @Rule
    public ActivityTestRule<RegisterActivity> rule =
            new ActivityTestRule<>(RegisterActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.assertCurrentActivity("Register Activity", RegisterActivity.class);
        assertTrue("username edit text", solo.waitForView(R.id.username_edit_text));
        assertTrue("email edit text", solo.waitForView(R.id.email_edit_text));
        assertTrue("first name edit text", solo.waitForView(R.id.f_name_edit_text));
        assertTrue("last name edit text", solo.waitForView(R.id.l_name_edit_text));
        assertTrue("last name edit text", solo.waitForView(R.id.phones_edit_text));

        usernameEditText = (EditText) solo.getView(R.id.username_edit_text);
        emailEditText = (EditText) solo.getView(R.id.email_edit_text);
        fNameEditText = (EditText) solo.getView(R.id.f_name_edit_text);
        lNameEditText = (EditText) solo.getView(R.id.l_name_edit_text);
        phoneEditText = (EditText) solo.getView(R.id.phones_edit_text);

    }

    @Test
    public void testSubmitWithoutUsername(){
        solo.enterText(usernameEditText, "");
        solo.enterText(emailEditText, "JohnDoe@gmail.com");
        solo.enterText(fNameEditText, "John");
        solo.enterText(lNameEditText, "Doe");
        solo.enterText(phoneEditText, "123-456-7890");
        assertTrue("submit button", solo.waitForView(R.id.submit));
        solo.clickOnView(solo.getView(R.id.submit));
//        boolean toastFound = solo.waitForText("Username is required");
//        assertTrue("Username is required", toastFound);
        solo.assertCurrentActivity("Home Activity", RegisterActivity.class);
    }

    @Test
    public void testSubmitWithUsernameThatAlreadyExists(){
        solo.enterText(usernameEditText, "johnDoe1");
        solo.enterText(emailEditText, "JohnDoe@gmail.com");
        solo.enterText(fNameEditText, "John");
        solo.enterText(lNameEditText, "Doe");
        solo.enterText(phoneEditText, "123-456-7890");
        assertTrue("submit button", solo.waitForView(R.id.submit));
        solo.clickOnView(solo.getView(R.id.submit));
        solo.assertCurrentActivity("Register Activity", RegisterActivity.class);
    }

    @Test
    public void testSubmitWithUsername(){
        solo.enterText(usernameEditText, "johnDoe1");
        solo.enterText(emailEditText, "JohnDoe@gmail.com");
        solo.enterText(fNameEditText, "John");
        solo.enterText(lNameEditText, "Doe");
        solo.enterText(phoneEditText, "123-456-7890");
        assertTrue("submit button", solo.waitForView(R.id.submit));
        solo.clickOnView(solo.getView(R.id.submit));
        solo.assertCurrentActivity("Home Activity", HomeActivity.class);
    }


}