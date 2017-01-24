package org.andrelsmoraes.entityrevision.api;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;

public interface Entity {

    Serializable getId();

    class Util {

        private static final String TAG = Util.class.getSimpleName();

        public static String toJson(Entity entity) {
            String json = null;

            if (entity != null) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.ANY);
                    objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

                    json = objectMapper.writeValueAsString(entity);

                } catch (IOException e) {
                    Log.e(TAG, "Error converting Entity of type " + entity.getClass() + " to JSON.");
                }
            }

            return json;
        }

        public static <T extends Entity> T fromJson(Class<T> entityClass, String entityJson) {
            T entity = null;

            if (entityClass != null && entityJson != null) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    entity = objectMapper.readValue(entityJson, entityClass);

                } catch (IOException e) {
                    Log.e(TAG, "Error constructing Entity of type " + entityClass + " from JSON.");
                }
            }

            return entity;
        }

    }

}
