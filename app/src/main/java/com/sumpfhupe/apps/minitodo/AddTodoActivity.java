package com.sumpfhupe.apps.minitodo;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;


public class AddTodoActivity extends AppCompatActivity {
    private EditText mTodoTextBodyEditText;

    private TodoItem mUserTodoItem;
    private FloatingActionButton mTodoSendFloatingActionButton;

    private String mUserEnteredText;
    private Toolbar mToolbar;
    private int mUserColor;
    private LinearLayout mContainerLayout;
    private String mTheme;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Need references to these to change them during light/dark mode
        mTheme = getSharedPreferences(MainActivity.THEME_PREFERENCES, MODE_PRIVATE).getString(MainActivity.THEME_SAVED, MainActivity.LIGHTTHEME);
        if(mTheme.equals(MainActivity.LIGHTTHEME)) {
            setTheme(R.style.CustomStyle_LightTheme);
        }
        else {
            setTheme(R.style.CustomStyle_DarkTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        // Show an X in place of <-
        final Drawable cross = getResources().getDrawable(R.drawable.ic_clear_white_24dp);
        if(cross !=null){
            cross.setColorFilter(getResources().getColor(R.color.icons), PorterDuff.Mode.SRC_ATOP);
        }

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(cross );

        }

        mUserTodoItem = (TodoItem)getIntent().getSerializableExtra(MainActivity.TODOITEM);
        mUserEnteredText = mUserTodoItem.getTodoText();
        mUserColor = mUserTodoItem.getTodoColor();
        mTodoTextBodyEditText = (EditText)findViewById(R.id.userTodoEditText);
        mTodoSendFloatingActionButton = (FloatingActionButton)findViewById(R.id.makeTodoFloatingActionButton);
        mTodoTextBodyEditText.requestFocus();
        mTodoTextBodyEditText.setText(mUserEnteredText);
        InputMethodManager imm = (InputMethodManager)this.getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        mTodoTextBodyEditText.setSelection(mTodoTextBodyEditText.length());

        mTodoTextBodyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUserEnteredText = s.toString();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTodoSendFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeResult(RESULT_OK);
                hideKeyboard(mTodoTextBodyEditText);
                finish();
            }
        });
    }

    private String getThemeSet() {
        return getSharedPreferences(MainActivity.THEME_PREFERENCES, MODE_PRIVATE).getString(MainActivity.THEME_SAVED, MainActivity.LIGHTTHEME);
    }

    public void hideKeyboard(EditText et) {
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    public void makeResult(int result) {
        Intent i = new Intent();
        mUserTodoItem.setTodoText(mUserEnteredText);
        mUserTodoItem.setTodoColor(mUserColor);
        i.putExtra(MainActivity.TODOITEM, mUserTodoItem);
        setResult(result, i);
    }

    @Override
    public void onBackPressed() {
        makeResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(NavUtils.getParentActivityName(this)!=null){
                    makeResult(RESULT_CANCELED);
                    NavUtils.navigateUpFromSameTask(this);
                }
                hideKeyboard(mTodoTextBodyEditText);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
