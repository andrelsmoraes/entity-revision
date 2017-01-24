package org.andrelsmoraes.entityrevision.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.andrelsmoraes.entityrevision.api.Entity;
import org.andrelsmoraes.entityrevision.api.RevisionField;

@DatabaseTable
@RevisionField
public class Entity2 implements Entity{

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField
    private String value2;

    @Override
    public Long getId() {
        return id;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Entity2 entity2 = (Entity2) object;

        return value2 != null ? value2.equals(entity2.value2) : entity2.value2 == null;

    }

    @Override
    public int hashCode() {
        return value2 != null ? value2.hashCode() : 0;
    }
}
