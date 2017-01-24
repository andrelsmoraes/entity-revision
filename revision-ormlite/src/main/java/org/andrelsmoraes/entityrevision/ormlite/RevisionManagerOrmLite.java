package org.andrelsmoraes.entityrevision.ormlite;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.table.TableUtils;

import org.andrelsmoraes.entityrevision.api.Entity;
import org.andrelsmoraes.entityrevision.api.EntityRevision;
import org.andrelsmoraes.entityrevision.api.RevisionManagerBase;
import org.andrelsmoraes.entityrevision.api.RevisionOperationException;
import org.andrelsmoraes.entityrevision.api.RevisionUnsupportedException;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class RevisionManagerOrmLite extends RevisionManagerBase {

    private static final String TAG = RevisionManagerOrmLite.class.getSimpleName();

    private OrmLiteSqliteOpenHelper openHelper;

    public RevisionManagerOrmLite(OrmLiteSqliteOpenHelper openHelper) throws RevisionUnsupportedException {
        this.openHelper = openHelper;

        try {
            TableUtils.createTableIfNotExists(openHelper.getConnectionSource(), EntityRevisionOrmLite.class);
        } catch (SQLException e) {
            throwAndLogError(TAG, "Error creating EntityRevisionOrmLite table. Details: " + e.getMessage());
        }
    }

    @Override
    public EntityRevision retrieveActive(Entity entity) throws RevisionOperationException {
        EntityRevision result = null;
        try {
            QueryBuilder<EntityRevisionOrmLite, ?> queryBuilder = openHelper.getDao(EntityRevisionOrmLite.class).queryBuilder();
            queryBuilder
                    .where()
                    .eq(EntityRevisionOrmLite.ACTIVE, true)
                    .and()
                    .eq(EntityRevisionOrmLite.ENTITY_ID, entity.getId())
                    .and()
                    .eq(EntityRevisionOrmLite.ENTITY_NAME, entity.getClass().getName());
            result = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            throwAndLogError(TAG, "Error retrieving EntityRevisionOrmLite by active [" + entity.getClass() + ", " + entity.getId() + "]. Details: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Set<EntityRevision> retrieveByDate(Entity entity, Date start, Date end) throws RevisionOperationException {
        Set<EntityRevision> result = new HashSet<>();
        try {
            QueryBuilder<EntityRevisionOrmLite, ?> queryBuilder = openHelper.getDao(EntityRevisionOrmLite.class).queryBuilder();
            queryBuilder
                    .orderBy(EntityRevisionOrmLite.REVISION_DATE, false)
                    .where()
                    .between(EntityRevisionOrmLite.REVISION_DATE, start, end)
                    .and()
                    .eq(EntityRevisionOrmLite.ENTITY_ID, entity.getId())
                    .and()
                    .eq(EntityRevisionOrmLite.ENTITY_NAME, entity.getClass().getName());
            result = new LinkedHashSet<EntityRevision>(queryBuilder.query());
        } catch (SQLException e) {
            throwAndLogError(TAG, "Error retrieving EntityRevisionOrmLite by date [" + entity.getClass() + ", " + entity.getId() + "]. Details: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Set<EntityRevision> retrieveAll(Entity entity) throws RevisionOperationException {
        Set<EntityRevision> result = new HashSet<>();
        try {
            QueryBuilder<EntityRevisionOrmLite, ?> queryBuilder = openHelper.getDao(EntityRevisionOrmLite.class).queryBuilder();
            queryBuilder
                    .orderBy(EntityRevisionOrmLite.REVISION_DATE, false)
                    .where()
                    .eq(EntityRevisionOrmLite.ENTITY_ID, entity.getId())
                    .and()
                    .eq(EntityRevisionOrmLite.ENTITY_NAME, entity.getClass().getName());
            result = new LinkedHashSet<EntityRevision>(queryBuilder.query());
        } catch (SQLException e) {
            throwAndLogError(TAG, "Error retrieving all EntityRevisionOrmLite [" + entity.getClass() + ", " + entity.getId() + "]. Details: " + e.getMessage());
        }
        return result;
    }


    @Override
    protected void deleteInactive(Entity entity) throws RevisionOperationException {
        try {
            DeleteBuilder<EntityRevisionOrmLite, ?> deleteBuilder = openHelper.getDao(EntityRevisionOrmLite.class).deleteBuilder();
            deleteBuilder
                    .where()
                    .eq(EntityRevisionOrmLite.ENTITY_ID, entity.getId())
                    .and()
                    .eq(EntityRevisionOrmLite.ENTITY_NAME, entity.getClass().getName())
                    .and()
                    .eq(EntityRevisionOrmLite.ACTIVE, false);
            deleteBuilder.delete();
        } catch (SQLException e) {
            throwAndLogError(TAG, "Error clearing EntityRevisionOrmLite [" + entity.getClass() + ", " + entity.getId() + "]. Details: " + e.getMessage());
        }
    }

    @Override
    protected void markAllAsInactive(Entity entity) throws RevisionOperationException {
        try {
            UpdateBuilder<EntityRevisionOrmLite, ?> updateBuilder = openHelper.getDao(EntityRevisionOrmLite.class).updateBuilder();
            updateBuilder
                    .updateColumnValue(EntityRevisionOrmLite.ACTIVE, false)
                    .where()
                    .eq(EntityRevisionOrmLite.ENTITY_ID, entity.getId())
                    .and()
                    .eq(EntityRevisionOrmLite.ENTITY_NAME, entity.getClass().getName());
            updateBuilder.update();
        } catch (SQLException e) {
            throw throwAndLogError(TAG, "Error marking EntityRevisionOrmLite as inactive [" + entity.getClass() + ", " + entity.getId() + "]. Details: " + e.getMessage());
        }
    }

    @Override
    protected EntityRevision createRevision(Entity entity) throws RevisionOperationException {
        EntityRevisionOrmLite revision = EntityRevisionOrmLite.createRevisionActive(entity);

        try {
            openHelper.getDao(EntityRevisionOrmLite.class).create(revision);

        } catch (SQLException e) {
            throw throwAndLogError(TAG, "Error saving RevisionEntity [" + entity.getClass() + "]. Details: " + e.getMessage());
        }

        return revision;
    }

}
