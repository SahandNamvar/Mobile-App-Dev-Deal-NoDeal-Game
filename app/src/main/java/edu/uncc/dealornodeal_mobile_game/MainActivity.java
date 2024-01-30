// Assignment #2
// File name: DealOrNoDeal_Mobile_Game
// Author: Sahand Namvar

// Package declaration and imports
package edu.uncc.dealornodeal_mobile_game;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

// MainActivity class definition that extends AppCompatActivity and implements View.OnClickListener interface
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "Debugging --> ";

    TextView textViewGameStatus;
    Button buttonDeal, buttonNoDeal;
    CaseInfo caseInfoTemp = null;
    int matchCount = 0, roundCount = 0;
    String rewardFormatted;
    ArrayList<Integer> selectedSuitcaseIndexPosition = new ArrayList<>(); // Keep index position of the opened suitcases to match dollarAmounts index.

    // Dollar Amounts
    private final ArrayList<Integer> dollarAmounts = new ArrayList<>(Arrays.asList(1, 10, 50, 100, 300, 1000, 10000, 50000, 100000, 500000));

    // Arrays storing the IDs of unopened suitcases, opened suitcases, uncrossed dollar amounts, and crossed-out dollar amounts
    // ID - UnOpened suitcases
    private final int[] imageViewUnOpenedCase_IDs = {
            R.id.imageViewUnOpened1, R.id.imageViewUnOpened2, R.id.imageViewUnOpened3,
            R.id.imageViewUnOpened4, R.id.imageViewUnOpened5, R.id.imageViewUnOpened6,
            R.id.imageViewUnOpened7, R.id.imageViewUnOpened8, R.id.imageViewUnOpened9,
            R.id.imageViewUnOpened10};

    // ID @ Drawable - Opened suitcases
    private final int[] drawableOpenedCase_IDs = {
            R.drawable.suitcase_open_1, R.drawable.suitcase_open_10, R.drawable.suitcase_open_50,
            R.drawable.suitcase_open_100, R.drawable.suitcase_open_300, R.drawable.suitcase_open_1000,
            R.drawable.suitcase_open_10000, R.drawable.suitcase_open_50000, R.drawable.suitcase_open_100000,
            R.drawable.suitcase_open_500000};

    // ID - UnCrossed dollar amounts
    private final int[] imageViewNotCrossedAmount_IDs = {
            R.id.imageViewNotCrossed1, R.id.imageViewNotCrossed10, R.id.imageViewNotCrossed50,
            R.id.imageViewNotCrossed100, R.id.imageViewNotCrossed300, R.id.imageViewNotCrossed1000,
            R.id.imageViewNotCrossed10000, R.id.imageViewNotCrossed50000, R.id.imageViewNotCrossed100000,
            R.id.imageViewNotCrossed500000};

    // ID @ Drawable - CrossedOut dollar amounts
    private final int[] drawableCrossedOutAmount_IDs = {
            R.drawable.reward_open_1, R.drawable.reward_open_10, R.drawable.reward_open_50,
            R.drawable.reward_open_100, R.drawable.reward_open_300, R.drawable.reward_open_1000,
            R.drawable.reward_open_10000, R.drawable.reward_open_50000, R.drawable.reward_open_100000,
            R.drawable.reward_open_500000};

    // ID @ Drawable - Closed suitcase (reset button)
    private final int[] drawableClosedSuitcase_Reset = {
            R.drawable.suitcase_position_1, R.drawable.suitcase_position_2, R.drawable.suitcase_position_3,
            R.drawable.suitcase_position_4, R.drawable.suitcase_position_5, R.drawable.suitcase_position_6,
            R.drawable.suitcase_position_7, R.drawable.suitcase_position_8, R.drawable.suitcase_position_9,
            R.drawable.suitcase_position_10};

    // ID @ Drawable - Clear reward amount (reset button)
    private final int[] drawableClearAmounts_Reset = {
            R.drawable.reward_1, R.drawable.reward_10, R.drawable.reward_50,
            R.drawable.reward_100, R.drawable.reward_300, R.drawable.reward_1000,
            R.drawable.reward_10000, R.drawable.reward_50000, R.drawable.reward_100000,
            R.drawable.reward_500000};

    // onCreate method called when the activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize fields by finding its ID in the layout
        textViewGameStatus = findViewById(R.id.textViewGameStatus);
        buttonNoDeal = findViewById(R.id.buttonNoDeal);
        buttonDeal = findViewById(R.id.buttonDeal);

        // Hide buttons
        buttonNoDeal.setVisibility(View.GONE);
        buttonDeal.setVisibility(View.GONE);

        // Set onClickListener for the "Reset" button
        findViewById(R.id.buttonReset).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // Reset all variables and UI elements
                matchCount = 0;
                roundCount = 0;
                textViewGameStatus.setText("Choose " + (4 - matchCount) + " Cases");
                buttonDeal.setVisibility(View.GONE);
                buttonNoDeal.setVisibility(View.GONE);
                caseInfoTemp = null;
                selectedSuitcaseIndexPosition.clear();
                setupNewGame(); // Call setupNewGame method when the "Reset" button is clicked
                resetSuitcases();
                resetCrossedAmounts();
                Toast.makeText(MainActivity.this, "Game Reset - Good Luck!", Toast.LENGTH_LONG).show();
            }
        });

        setupNewGame(); // Initial setup of the new game

        // Button Deal onClick
        buttonDeal.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // Handle the deal option
                double reward = calculateRewardAmount();

                // Display "You won <formattedReward>"
                textViewGameStatus.setText("You WON! $" + rewardFormatted);
                Toast.makeText(MainActivity.this, "You won $" + rewardFormatted, Toast.LENGTH_LONG).show();

                // Display the "Reset" button and hide "Deal" and "No Deal" buttons
                buttonDeal.setVisibility(View.GONE);
                buttonNoDeal.setVisibility(View.GONE);
                findViewById(R.id.buttonReset).setVisibility(View.VISIBLE);
            }
        });

        buttonNoDeal.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                buttonDeal.setVisibility(View.GONE);
                buttonNoDeal.setVisibility(View.GONE);
                roundCount++;
                matchCount = 0;
                textViewGameStatus.setText("Choose " + (4 - matchCount) + " Cases");
                if (roundCount == 2) {
                    matchCount = 3; // Reason: Number of cases to be opened is 1 --> (4 - 3 = 1)
                    textViewGameStatus.setText("Choose " + (4 - matchCount) + " Case!");
                }
                // Handle the no deal option
                // Continue the game or proceed to the next round
                // You may add logic here based on your game rules
            }
        });
    }

    // Method to set up a new game
    private void setupNewGame(){

        textViewGameStatus.setText(R.string.choose_case_number);

        // Create an ArrayList to store shuffled drawable IDs of opened suitcases
        ArrayList<Integer> shuffledSuitCaseDrawables = new ArrayList<>();

        // Populate the ArrayList with drawable IDs from drawableOpenedCase_IDs array
        for (int drawableID: drawableOpenedCase_IDs) {
            shuffledSuitCaseDrawables.add(drawableID);
        }

        // Shuffle the drawable IDs to randomize the order
        Collections.shuffle(shuffledSuitCaseDrawables);

        // Loop through the shuffled drawable IDs to set up the initial state of unopened suitcases
        for (int i = 0; i < shuffledSuitCaseDrawables.size(); i++) {
            int imageViewID = imageViewUnOpenedCase_IDs[i];
            int drawableID = shuffledSuitCaseDrawables.get(i);
            CaseInfo caseInfo = new CaseInfo(imageViewID, drawableID); // Create a CaseInfo object for the current state
            ImageView imageView = findViewById(imageViewID);
            imageView.setTag(caseInfo); // Set the CaseInfo object as a tag to the ImageView
            imageView.setOnClickListener(this); // Set onClickListener for the ImageView
        }
        caseInfoTemp = null;
        matchCount = 0;
    }

    @Override
    @SuppressLint("SetTextI18n")
    public void onClick(View v) {
        // Check if the user has not opened the maximum allowed cases (4)
        if (matchCount < 4) {
            ImageView imageView = (ImageView) v;
            CaseInfo caseInfo = (CaseInfo) imageView.getTag();

            // Check if the suitcase is not already opened and not already matched
            if (!caseInfo.isOpened() && !caseInfo.isMatched()) {
                imageView.setImageResource(caseInfo.getDrawableID());
                caseInfo.setOpened(true);
                updateCrossedAmountImage(caseInfo);

                double reward = calculateRewardAmount();

                // Display comma after three places
                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                String formattedReward = decimalFormat.format(reward);
                rewardFormatted = formattedReward; // For Global use

                // Check if the user has reached the maximum allowed cases
                if (matchCount == 4) {
                    if (roundCount == 2) { // Check for final round
                        textViewGameStatus.setText("You WON! $" + rewardFormatted);
                        buttonDeal.setVisibility(View.GONE);
                        buttonNoDeal.setVisibility(View.GONE);
                    } else {
                        buttonDeal.setVisibility(View.VISIBLE);
                        buttonNoDeal.setVisibility(View.VISIBLE);
                        textViewGameStatus.setText("Bank Deal is: $" + formattedReward);
                    }

                } else {
                    // Update the status text to indicate the remaining number of cases to be opened
                    textViewGameStatus.setText("Choose " + (4 - matchCount) + " Cases");
                }
            } else {
                // Display a toast message if the user tries to open an already opened or matched suitcase
                Toast.makeText(MainActivity.this, "Select another suitcase!", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Display a toast message if the user tries to open more cases than the allowed limit
            Toast.makeText(MainActivity.this, "You've reached the limit!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Updates the crossed amount image corresponding to the opened suitcase.
     * 1. Extract the drawable ID of the opened suitcase.
     * 2. Find the index of the suitcase drawable ID in the array of opened suitcases.
     * 3. If the index is found:
     *      a. Get the corresponding ID of the not-crossed amount ImageView.
     *      b. Update the image resource of the not-crossed amount ImageView to the crossed-out version.
     * Note: caseInfo The CaseInfo object representing the state of the opened suitcase.
     */
    @SuppressLint("SetTextI18n")
    private void updateCrossedAmountImage(CaseInfo caseInfo) {
        int suitcaseDrawableID = caseInfo.getDrawableID();

        // Find the index of suitcaseDrawableID in drawableOpenedCase_IDs
        int index = -1;
        for (int i = 0; i < drawableOpenedCase_IDs.length; i++) {
            if (drawableOpenedCase_IDs[i] == suitcaseDrawableID) {
                index = i;
                selectedSuitcaseIndexPosition.add(index); // Keep track of that index
                Log.d(TAG, "Index found: " + index);
                break;
            }
        }

        // If index found, update the crossed amount image
        if (index != -1) {
            matchCount++;
            caseInfo.setMatched(true);
            textViewGameStatus.setText("Choose " + (4-matchCount) + " Cases");
            int crossedAmountImageViewID = imageViewNotCrossedAmount_IDs[index];
            ImageView crossedAmountImageView = findViewById(crossedAmountImageViewID);
            Log.d(TAG, "Updating crossed amount image for index: " + index);
            crossedAmountImageView.setImageResource(drawableCrossedOutAmount_IDs[index]);
        }
    }

    // Method to reset unopened suitcases
    private void resetSuitcases() {
        for (int i = 0; i < imageViewUnOpenedCase_IDs.length; i++) {
            int imageViewID = imageViewUnOpenedCase_IDs[i];
            ImageView imageView = findViewById(imageViewID);
            imageView.setImageResource(drawableClosedSuitcase_Reset[i]);
        }
    }

    // Method to reset crossed amounts
    private void resetCrossedAmounts() {
        for (int i = 0; i < imageViewNotCrossedAmount_IDs.length; i++) {
            int imageViewID = imageViewNotCrossedAmount_IDs[i];
            ImageView imageView = findViewById(imageViewID);
            imageView.setImageResource(drawableClearAmounts_Reset[i]);
        }
    }

    // Method to calculate reward based on 60% of the average value of non-open cases
    private double calculateRewardAmount(){
        ArrayList<Integer> unOpenedCaseAmounts = new ArrayList<>(); // Filtered amounts (not crossed out)
        for (int i = 0; i < dollarAmounts.size(); i++) {
            if (!selectedSuitcaseIndexPosition.contains(i)){
                unOpenedCaseAmounts.add(dollarAmounts.get(i));
                Log.d(TAG, "Unopened Case Dollar Amount: " + unOpenedCaseAmounts); // Array of dollar amounts left unOpened.
            }
        }
        double totalAmountLeftUnopened = 0;
        for (double num: unOpenedCaseAmounts) {
            totalAmountLeftUnopened+=num;
            Log.d(TAG, "Sum of total amount unopened: " + totalAmountLeftUnopened); // Sum of dollar amount left unOpened.
        }
        double average = totalAmountLeftUnopened / unOpenedCaseAmounts.size(); // Average of summation
        Log.d(TAG, "Average (before 60%): " + average);
        double finalAmount = average * 0.6;
        if (roundCount == 2) {
            return (Math.ceil((average)));
        } else {
            return (Math.ceil(finalAmount));
        }
    }
}


