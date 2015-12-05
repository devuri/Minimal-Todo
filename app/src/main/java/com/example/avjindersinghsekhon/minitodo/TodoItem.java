package com.example.avjindersinghsekhon.minitodo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.UUID;

public class TodoItem implements Serializable{
    private String mTodoText;
    private int mTodoColor;
    private UUID mTodoIdentifier;
    private static final String TODOTEXT = "todotext";
    private static final String TODOCOLOR = "todocolor";
    private static final String TODOIDENTIFIER = "todoidentifier";

    public static String DEFAULT_TEXT = "Clean my room";


    public TodoItem(String todoBody){
        mTodoText = todoBody;
        mTodoColor = 1677725;
        mTodoIdentifier = UUID.randomUUID();
    }

    public TodoItem(JSONObject jsonObject) throws JSONException{
        mTodoText = jsonObject.getString(TODOTEXT);
        mTodoColor = jsonObject.getInt(TODOCOLOR);
        mTodoIdentifier = UUID.fromString(jsonObject.getString(TODOIDENTIFIER));
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TODOTEXT, mTodoText);
        jsonObject.put(TODOCOLOR, mTodoColor);
        jsonObject.put(TODOIDENTIFIER, mTodoIdentifier.toString());
        return jsonObject;
    }

    public TodoItem(){
        this(DEFAULT_TEXT);
    }

    public String getTodoText() {
        return mTodoText;
    }

    public void setTodoText(String mTodoText) {
        this.mTodoText = mTodoText;
    }

    public boolean hasTodoText() {
        return getTodoText().length() > 0;
    }

    public int getTodoColor() {
        return mTodoColor;
    }

    public void setTodoColor(int mTodoColor) {
        this.mTodoColor = mTodoColor;
    }

    public UUID getIdentifier(){
        return mTodoIdentifier;
    }

    /** Check equal ID and todo text. */
    public boolean equals(Object obj) {
        if (obj instanceof TodoItem) {
            TodoItem item = (TodoItem) obj;
            return item.getIdentifier().equals(this.getIdentifier()) &&
                item.getTodoText().equals(this.getTodoText());
        }
        return false;
    }

    /** The normal string representation of an item is its text */
    public String toString() {
        return getTodoText();
    }
}

