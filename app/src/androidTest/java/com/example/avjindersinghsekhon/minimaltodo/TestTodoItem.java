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

package com.example.avjindersinghsekhon.minimaltodo;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * JUnit tests to verify functionality of ToDoItem class.
 */
public class TestTodoItem extends TestCase {
    private final String TEXT_BODY = "This is some text";

     /**
      * Check we can construct a ToDoItem object using the three parameter constructor
      */
    public void testThreeParameterConstructor() {
        ToDoItem toDoItem = getToDoItem();
        assertEquals(TEXT_BODY, toDoItem.getToDoText());
    }

     /**
      * Ensure we can marshall ToDoItem objects to Json
      */
    public void testObjectMarshallingToJson() {
        ToDoItem toDoItem = getToDoItem();

        try {
            JSONObject json = toDoItem.toJSON();
            assertEquals(TEXT_BODY, json.getString("todotext"));
        } catch (JSONException e) {
            fail("Exception thrown during test execution: " + e.getMessage());
        }
    }

    /**
    * Ensure we can create ToDoItem objects from Json data by using the json constructor
    */
    public void testObjectUnmarshallingFromJson() {
        ToDoItem originalItem = getToDoItem();

        try {
            JSONObject json = originalItem.toJSON();
            ToDoItem itemFromJson = new ToDoItem(json);

            assertEquals(originalItem.getToDoText(), itemFromJson.getToDoText());
            assertEquals(originalItem.getIdentifier(), itemFromJson.getIdentifier());

        } catch (JSONException e) {
            fail("Exception thrown during test execution: " + e.getMessage());
        }
    }

    private ToDoItem getToDoItem() {
        return new ToDoItem(TEXT_BODY);
    }
}