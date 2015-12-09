package com.sumpfhupe.apps.minitodo;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.TextUtils;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private RecyclerViewEmptySupport mRecyclerView;
    private FloatingActionButton mAddTodoItemFAB;
    private ArrayList<TodoItem> mTodoItemsArrayList;
    private CoordinatorLayout mCoordLayout;
    public static final String TODOITEM = "com.sumpfhupe.apps.minitodo.MainActivity";
    private BasicListAdapter adapter;
    private static final int REQUEST_ID_TODO_ITEM = 100;
    private TodoItem mJustDeletedTodoItem;
    private int mIndexOfDeletedTodoItem;
    public static final String FILENAME = "todoitems.json";
    private StoreRetrieveData storeRetrieveData;
    public ItemTouchHelper itemTouchHelper;
    private CustomRecyclerScrollViewListener customRecyclerScrollViewListener;
    public static final String SHARED_PREF_DATA_SET_CHANGED = "com.sumpfhupe.datasetchanged";
    public static final String CHANGE_OCCURED = "com.sumpfhupe.changeoccured";
    public static final String THEME_PREFERENCES = "com.sumpfhupe.themepref";
    public static final String RECREATE_ACTIVITY = "com.sumpfhupe.recreateactivity";
    public static final String THEME_SAVED = "com.sumpfhupe.savedtheme";
    public static final String DARKTHEME = "com.sumpfhupe.darktheme";
    public static final String LIGHTTHEME = "com.sumpfhupe.lighttheme";

    public static ArrayList<TodoItem> getLocallyStoredData(StoreRetrieveData storeRetrieveData){
        ArrayList<TodoItem> items = null;

        try {
            items  = storeRetrieveData.loadFromFile();

        } catch (IOException | JSONException e) {
            Log.e("Mini", "Could not load data", e);
        }

        if(items == null){
            items = new ArrayList<>();
        }
        return items;
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
        We need to do this, as this activity's onCreate won't be called when coming back from SettingsActivity,
        thus our changes to dark/light mode won't take place, as the setContentView() is not called again.
        So, inside our SettingsFragment, whenever the checkbox's value is changed, in our shared preferences,
        we mark our recreate_activity key as true.

        Note: the recreate_key's value is changed to false before calling recreate(), or we woudl have ended up in an infinite loop,
        as onResume() will be called on recreation, which will again call recreate() and so on....
        and get an ANR

         */
        if(getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE).getBoolean(RECREATE_ACTIVITY, false)){
            SharedPreferences.Editor editor = getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE).edit();
            editor.putBoolean(RECREATE_ACTIVITY, false);
            editor.apply();
            recreate();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
        if(sharedPreferences.getBoolean(CHANGE_OCCURED, false)){

            mTodoItemsArrayList = getLocallyStoredData(storeRetrieveData);
            adapter = new BasicListAdapter(mTodoItemsArrayList);
            mRecyclerView.setAdapter(adapter);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(CHANGE_OCCURED, false);
            editor.apply();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        //We recover the theme we've set and setTheme accordingly
        String theme = getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE).getString(THEME_SAVED, LIGHTTHEME);
        if(theme.equals(LIGHTTHEME)){
            setTheme(R.style.CustomStyle_LightTheme);
        }
        else{
            setTheme(R.style.CustomStyle_DarkTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CHANGE_OCCURED, false);
        editor.apply();

        storeRetrieveData = new StoreRetrieveData(this, FILENAME);
        mTodoItemsArrayList =  getLocallyStoredData(storeRetrieveData);
        adapter = new BasicListAdapter(mTodoItemsArrayList);

        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCoordLayout = (CoordinatorLayout)findViewById(R.id.myCoordinatorLayout);
        mAddTodoItemFAB = (FloatingActionButton)findViewById(R.id.addTodoItemFAB);
        mAddTodoItemFAB.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                Intent newTodo = new Intent(MainActivity.this, AddTodoActivity.class);
                TodoItem item = new TodoItem("");
                int color = ColorGenerator.MATERIAL.getRandomColor();
                item.setTodoColor(color);
                newTodo.putExtra(TODOITEM, item);
                startActivityForResult(newTodo, REQUEST_ID_TODO_ITEM);
            }
        });

        mRecyclerView = (RecyclerViewEmptySupport)findViewById(R.id.toDoRecyclerView);
        if(theme.equals(LIGHTTHEME)){
            mRecyclerView.setBackgroundColor(getResources().getColor(R.color.primary_lightest));
        }
        mRecyclerView.setEmptyView(findViewById(R.id.toDoEmptyView));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        customRecyclerScrollViewListener = new CustomRecyclerScrollViewListener() {
            @Override
            public void show() {

                mAddTodoItemFAB.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide() {

                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mAddTodoItemFAB.getLayoutParams();
                int fabMargin = lp.bottomMargin;
                mAddTodoItemFAB.animate().translationY(mAddTodoItemFAB.getHeight()+fabMargin).setInterpolator(new AccelerateInterpolator(2.0f)).start();
            }
        };
        mRecyclerView.addOnScrollListener(customRecyclerScrollViewListener);

        ItemTouchHelper.Callback callback = new ItemTouchHelperClass(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.setAdapter(adapter);
    }

    public void addThemeToSharedPreferences(String theme){
        SharedPreferences sharedPreferences = getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(THEME_SAVED, theme);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.menu_preferences:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.menu_sendto:
                return sendTodoList();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Send a string representation of the current todo list */
    private boolean sendTodoList() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getText(R.string.send_to_subject));
        sendIntent.putExtra(Intent.EXTRA_TEXT, getTodoListAsString());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
        return true;
    }

    /** Get the current todo list as a string - one line for each item. */
    private String getTodoListAsString() {
        return TextUtils.join("\n", mTodoItemsArrayList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= RESULT_CANCELED && requestCode == REQUEST_ID_TODO_ITEM){
            TodoItem item =(TodoItem) data.getSerializableExtra(TODOITEM);
            if(!item.hasTodoText()){
                return;
            }
            boolean existed = false;

            for(int i = 0; i< mTodoItemsArrayList.size(); i++){
                if(item.getIdentifier().equals(mTodoItemsArrayList.get(i).getIdentifier())){
                    mTodoItemsArrayList.set(i, item);
                    existed = true;
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
            if(!existed) {
                addToDataStore(item);
            }
        }
    }

    private boolean doesPendingIntentExist(Intent i, int requestCode){
        PendingIntent pi = PendingIntent.getService(this,requestCode, i, PendingIntent.FLAG_NO_CREATE);
        return pi!=null;
    }

    private void addToDataStore(TodoItem item){
        mTodoItemsArrayList.add(item);
        adapter.notifyItemInserted(mTodoItemsArrayList.size() - 1);
    }

    public class BasicListAdapter extends RecyclerView.Adapter<BasicListAdapter.ViewHolder> implements ItemTouchHelperClass.ItemTouchHelperAdapter{
        private ArrayList<TodoItem> mItems;

        @Override
        public void onItemMoved(int fromPosition, int toPosition) {
           if(fromPosition<toPosition){
               for(int i=fromPosition; i<toPosition; i++){
                   Collections.swap(mItems, i, i+1);
               }
           }
            else{
               for(int i=fromPosition; i > toPosition; i--){
                   Collections.swap(mItems, i, i-1);
               }
           }
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onItemRemoved(final int position) {
            mJustDeletedTodoItem =  mItems.remove(position);
            mIndexOfDeletedTodoItem = position;
            notifyItemRemoved(position);
            // Display the just deleted todo item text in the snackbar
            // Note: the limit to 20 characters is somewhat arbitrary but simple and sufficient.
            String text = String.format(getResources().getString(R.string.deleted),
                    Utils.LimitString(mJustDeletedTodoItem.toString(), 20));
            // Show the snackbar undo link long enough for the user to undo the deletion
            Snackbar.make(mCoordLayout, text, Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mItems.add(mIndexOfDeletedTodoItem, mJustDeletedTodoItem);
                            notifyItemInserted(mIndexOfDeletedTodoItem);
                        }
                    }).show();
        }

        @Override
        public BasicListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_circle_try, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final BasicListAdapter.ViewHolder holder, final int position) {
            TodoItem item = mItems.get(position);
            SharedPreferences sharedPreferences = getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE);
            //Background color for each to-do item. Necessary for night/day mode
            int bgColor;
            //color of title text in our to-do item. White for night mode, dark gray for day mode
            int todoTextColor;
            if(sharedPreferences.getString(THEME_SAVED, LIGHTTHEME).equals(LIGHTTHEME)){
                bgColor = Color.WHITE;
                todoTextColor = getResources().getColor(R.color.secondary_text);
            }
            else{
                bgColor = Color.DKGRAY;
                todoTextColor = Color.WHITE;
            }
            holder.linearLayout.setBackgroundColor(bgColor);

            holder.mTodoTextview.setMaxLines(2);
            holder.mTodoTextview.setText(item.toString());
            holder.mTodoTextview.setTextColor(todoTextColor);
            TextDrawable myDrawable = TextDrawable.builder().beginConfig()
                    .textColor(Color.WHITE)
                    .useFont(Typeface.DEFAULT)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(item.getTodoText().substring(0,1),item.getTodoColor());

            holder.mColorImageView.setImageDrawable(myDrawable);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        BasicListAdapter(ArrayList<TodoItem> items){
            mItems = items;
        }


        @SuppressWarnings("deprecation")
        public class ViewHolder extends RecyclerView.ViewHolder{

            View mView;
            LinearLayout linearLayout;
            TextView mTodoTextview;
            ImageView mColorImageView;

            public ViewHolder(View v){
                super(v);
                mView = v;
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TodoItem item = mItems.get(ViewHolder.this.getAdapterPosition());
                        Intent i = new Intent(MainActivity.this, AddTodoActivity.class);
                        i.putExtra(TODOITEM, item);
                        startActivityForResult(i, REQUEST_ID_TODO_ITEM);
                    }
                });
                mTodoTextview = (TextView)v.findViewById(R.id.toDoListItemTextview);
                mColorImageView = (ImageView)v.findViewById(R.id.toDoListItemColorImageView);
                linearLayout = (LinearLayout)v.findViewById(R.id.listItemLinearLayout);
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            storeRetrieveData.saveToFile(mTodoItemsArrayList);
        } catch (JSONException | IOException e) {
            Log.e("Mini", "Could not save data", e);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecyclerView.removeOnScrollListener(customRecyclerScrollViewListener);
    }
}
