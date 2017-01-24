package org.andrelsmoraes.entityrevision.api;

import android.util.Log;

import java.lang.reflect.Field;

public abstract class RevisionManagerBase implements RevisionManager {

    private static final String TAG = RevisionManagerBase.class.getSimpleName();

    //TODO resolve circular dependencies
    @Override
    public EntityRevision save(Entity entity) throws RevisionOperationException {
        EntityRevision result = null;

        try {
            if (entity.getClass().getAnnotation(RevisionField.class) != null) {
                markAllAsInactive(entity);
                result = createRevision(entity);

                Field[] fields = entity.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.getAnnotation(RevisionIgnore.class) == null) {
                        field.setAccessible(true);
                        Object obj = field.get(entity);

                        if (obj != null && obj instanceof Entity) {
                            save((Entity) obj);
                        }
                    }
                }
            }

        } catch (IllegalAccessException e) {
            throwAndLogError(TAG, "Unable to access a field in entity " + entity.getClass().getSimpleName());
        }

        return result;
    }

    //TODO resolve circular dependencies
    @Override
    public void clearNotActive(Entity entity) throws RevisionOperationException {
        try {
            if (entity.getClass().getAnnotation(RevisionField.class) != null) {
                deleteInactive(entity);

                Field[] fields = entity.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.getAnnotation(RevisionIgnore.class) == null) {
                        field.setAccessible(true);
                        Object obj = field.get(entity);

                        if (obj != null && obj instanceof Entity) {
                            clearNotActive((Entity) obj);
                        }
                    }
                }
            }

        } catch (IllegalAccessException e) {
            throwAndLogError(TAG, "Unable to access a field in entity " + entity.getClass().getSimpleName());
        }
    }

    @Override
    public EntityRevision saveAndClearNotActive(Entity entity) throws RevisionOperationException {
        EntityRevision revision = save(entity);
        clearNotActive(entity);
        return revision;
    }

    protected abstract void deleteInactive(Entity entity) throws RevisionOperationException;

    protected abstract void markAllAsInactive(Entity entity) throws RevisionOperationException;

    protected abstract EntityRevision createRevision(Entity entity) throws RevisionOperationException;

    protected RevisionOperationException throwAndLogError(String tag, String message) {
        Log.e(tag, message);
        return new RevisionOperationException(message);
    }

}
