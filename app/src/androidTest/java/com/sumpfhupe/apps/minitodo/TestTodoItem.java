/**
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Miikka Andersson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.sumpfhupe.apps.minitodo;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * JUnit tests to verify functionality of TodoItem class.
 */
public class TestTodoItem extends TestCase {
    private final String TEXT_BODY = "This is some text and special chars äöü ~²!";

     /**
      * Check we can construct a TodoItem object using the constructor
      */
    public void testConstructor() {
        // empty constructor
        TodoItem item = new TodoItem();
        assertEquals(TodoItem.DEFAULT_TEXT, item.getTodoText());
        // constructor with text
        item = getToDoItem();
        assertEquals(TEXT_BODY, item.getTodoText());
    }

     /**
      * Ensure we can marshall TodoItem objects to Json
      */
    public void testObjectMarshallingToJson() throws JSONException {
        TodoItem todoItem = getToDoItem();
        JSONObject json = todoItem.toJSON();
        assertEquals(TEXT_BODY, json.getString("todotext"));
    }

    /**
    * Ensure we can create TodoItem objects from Json data by using the json constructor
    */
    public void testObjectUnmarshallingFromJson() throws JSONException {
        TodoItem originalItem = getToDoItem();
        JSONObject json = originalItem.toJSON();
        TodoItem itemFromJson = new TodoItem(json);
        assertEquals(originalItem, itemFromJson);
    }

    private TodoItem getToDoItem() {
        return new TodoItem(TEXT_BODY);
    }
}
