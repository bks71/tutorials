package org.baeldung.gson.serialization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;

import org.baeldung.gson.serialization.DifferentNameSerializer;
import org.baeldung.gson.serialization.IgnoringFieldsNotMatchingCriteriaSerializer;
import org.baeldung.gson.serialization.IgnoringFieldsSerializer;
import org.baeldung.gson.serialization.SourceClass;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class GsonSerializationTest {

    @Test
    public void givenArrayOfObjects_whenSerializing_thenCorrect() {
        final SourceClass[] sourceArray = { new SourceClass(1, "one"), new SourceClass(2, "two") };
        final String jsonString = new Gson().toJson(sourceArray);

        // test
        final String expectedResult = "[{\"intValue\":1,\"stringValue\":\"one\"},{\"intValue\":2,\"stringValue\":\"two\"}]";
        assertEquals(expectedResult, jsonString);
    }

    @Test
    public void givenCollection_whenSerializing_thenCorrect() {
        final Collection<SourceClass> sourceCollection = Lists.newArrayList(new SourceClass(1, "one"), new SourceClass(2, "two"));
        final Type sourceCollectionType = new TypeToken<Collection<SourceClass>>() {
        }.getType();
        final String jsonCollection = new Gson().toJson(sourceCollection, sourceCollectionType);

        final String expectedResult = "[{\"intValue\":1,\"stringValue\":\"one\"},{\"intValue\":2,\"stringValue\":\"two\"}]";
        assertEquals(expectedResult, jsonCollection);
    }

    @Test
    public void givenUsingCustomSerializer_whenChangingNameOfFieldOnSerializing_thenCorrect() {
        final SourceClass sourceObject = new SourceClass(7, "seven");
        final GsonBuilder gsonBuildr = new GsonBuilder();
        gsonBuildr.registerTypeAdapter(SourceClass.class, new DifferentNameSerializer());
        final String jsonString = gsonBuildr.create().toJson(sourceObject);

        final String expectedResult = "{\"otherIntValue\":7,\"otherStringValue\":\"seven\"}";
        assertEquals(expectedResult, jsonString);
    }

    @Test
    public void givenIgnoringAField_whenSerializingWithCustomSerializer_thenFieldIgnored() {
        final SourceClass sourceObject = new SourceClass(7, "seven");
        final GsonBuilder gsonBuildr = new GsonBuilder();
        gsonBuildr.registerTypeAdapter(SourceClass.class, new IgnoringFieldsSerializer());
        final String jsonString = gsonBuildr.create().toJson(sourceObject);

        final String expectedResult = "{\"intValue\":7}";
        assertEquals(expectedResult, jsonString);
    }

    @Test
    public void givenDate_whenSerializing_thenCorrect() {
        Date sourceDate = new Date(1000000L);
        Gson gson = new Gson();
        Type sourceDateType = new TypeToken<Date>() {
        }.getType();
        String jsonDate = gson.toJson(sourceDate, sourceDateType);
        // test
        System.out.println("jsonDate:\n" + jsonDate);
        String expectedResult = "\"Jan 1, 1970 3:16:40 AM\"";
        assertTrue(jsonDate.equals(expectedResult));
    }

    @Test
    public void givenUsingCustomDeserializer_whenFieldNotMatchesCriteria_thenIgnored() {
        SourceClass sourceObject = new SourceClass(-1, "minus 1");
        GsonBuilder gsonBuildr = new GsonBuilder();
        gsonBuildr.registerTypeAdapter(SourceClass.class, new IgnoringFieldsNotMatchingCriteriaSerializer());
        Gson gson = gsonBuildr.create();
        Type sourceObjectType = new TypeToken<SourceClass>() {
        }.getType();
        String jsonString = gson.toJson(sourceObject, sourceObjectType);
        
        String expectedResult = "{\"stringValue\":\"minus 1\"}";
        assertEquals(expectedResult, jsonString);
    }
    
}