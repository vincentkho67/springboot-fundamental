package bytebrewers.bitpod.utils.helper;

import java.lang.reflect.Field;

public class EntityUpdater {
    public static <T> T updateEntity(T existingEntity, T updatedEntity) {
        if (existingEntity == null || updatedEntity == null) {
            throw new IllegalArgumentException("Entities must not be null");
        }

        Class<?> entityClass = existingEntity.getClass();

        for (Field field : entityClass.getDeclaredFields()) {
            field.setAccessible(true);

            if (field.getName().equals("id")) {
                continue;
            }

            try {
                Object updatedValue = field.get(updatedEntity);
                if (updatedValue != null) {
                    field.set(existingEntity, updatedValue);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error updating entity", e);
            }
        }

        return existingEntity;
    }
}

