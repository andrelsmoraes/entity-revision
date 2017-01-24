package org.andrelsmoraes.entityrevision.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.andrelsmoraes.entityrevision.api.Entity;
import org.andrelsmoraes.entityrevision.api.EntityRevision;

import java.util.Date;

@DatabaseTable
public class EntityRevisionOrmLite implements EntityRevision {

    public static final String REVISION_DATE = "revisionDate";
    public static final String ENTITY_ID = "entityId";
    public static final String ENTITY_NAME = "entityName";
    public static final String ACTIVE = "active";

    @DatabaseField(generatedId = true) private Long id;
    @DatabaseField(index = true) private Long entityId;
    @DatabaseField(index = true) private String entityName;
    @DatabaseField private String entityAsJson;
    @DatabaseField private Date revisionDate;
    @DatabaseField private boolean active;

    @Override
    public Long getEntityId() {
        return entityId;
    }

    @Override
    public String getEntityName() {
        return entityName;
    }

    @Override
    public Date getRevisionDate() {
        return revisionDate;
    }

    @Override
    public String getEntityAsJson() {
        return entityAsJson;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public static EntityRevisionOrmLite createRevisionActive(Entity entity) {
        //TODO error

        EntityRevisionOrmLite revision = new EntityRevisionOrmLite();
        revision.id = null;
        revision.entityId = (Long) entity.getId();
        revision.entityName = entity.getClass().getName();
        revision.entityAsJson = Entity.Util.toJson(entity);
        revision.revisionDate = new Date();
        revision.active = true;

        return revision;
    }

}
