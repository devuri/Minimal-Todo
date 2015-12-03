package com.example.avjindersinghsekhon.minimaltodo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.UUID;

public class ToDoItem implements Serializable{
    private String mToDoText;
    private int mTodoColor;
    private UUID mTodoIdentifier;
    private static final String TODOTEXT = "todotext";
    private static final String TODOCOLOR = "todocolor";
    private static final String TODOIDENTIFIER = "todoidentifier";

    public static String DEFAULT_TEXT = "Clean my room";


    public ToDoItem(String todoBody){
        mToDoText = todoBody;
        mTodoColor = 1677725;
        mTodoIdentifier = UUID.randomUUID();
    }

    public ToDoItem(JSONObject jsonObject) throws JSONException{
        mToDoText = jsonObject.getString(TODOTEXT);
        mTodoColor = jsonObject.getInt(TODOCOLOR);
        mTodoIdentifier = UUID.fromString(jsonObject.getString(TODOIDENTIFIER));
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TODOTEXT, mToDoText);
        jsonObject.put(TODOCOLOR, mTodoColor);
        jsonObject.put(TODOIDENTIFIER, mTodoIdentifier.toString());
        return jsonObject;
    }

    public ToDoItem(){
        this(DEFAULT_TEXT);
    }

    public String getToDoText() {
        return mToDoText;
    }

    public void setToDoText(String mToDoText) {
        this.mToDoText = mToDoText;
    }

    public boolean hasToDoText() {
        return getToDoText().length() > 0;
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
        if (obj instanceof ToDoItem) {
            ToDoItem item = (ToDoItem) obj;
            return item.getIdentifier().equals(this.getIdentifier()) &&
                item.getToDoText().equals(this.getToDoText());
        }
        return false;
    }

    /** The normal string representation of an item is its text */
    public String toString() {
        return getToDoText();
    }
}

