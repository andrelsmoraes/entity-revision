package org.andrelsmoraes.entityrevision.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.andrelsmoraes.entityrevision.api.Entity;
import org.andrelsmoraes.entityrevision.api.RevisionField;
import org.andrelsmoraes.entityrevision.api.RevisionIgnore;

@DatabaseTable
@RevisionField
public class Entity1 implements Entity {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private String value1;

    @DatabaseField(foreign = true, canBeNull = true)
    private Entity2 entity2;

    @DatabaseField(foreign = true, canBeNull = true)
    @RevisionIgnore
    private Entity3 entity3;

    @Override
    public Long getId() {
        return id;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public Entity2 getEntity2() {
        return entity2;
    }

    public void setEntity2(Entity2 entity2) {
        this.entity2 = entity2;
    }

    public Entity3 getEntity3() {
        return entity3;
    }

    public void setEntity3(Entity3 entity3) {
        this.entity3 = entity3;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Entity1 entity1 = (Entity1) object;

        if (value1 != null ? !value1.equals(entity1.value1) : entity1.value1 != null) return false;
        if (entity2 != null ? !entity2.equals(entity1.entity2) : entity1.entity2 != null)
            return false;
        return entity3 != null ? entity3.equals(entity1.entity3) : entity1.entity3 == null;

    }

    @Override
    public int hashCode() {
        int result = value1 != null ? value1.hashCode() : 0;
        result = 31 * result + (entity2 != null ? entity2.hashCode() : 0);
        result = 31 * result + (entity3 != null ? entity3.hashCode() : 0);
        return result;
    }
}
