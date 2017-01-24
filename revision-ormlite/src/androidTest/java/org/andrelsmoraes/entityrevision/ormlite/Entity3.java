package org.andrelsmoraes.entityrevision.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.andrelsmoraes.entityrevision.api.Entity;
import org.andrelsmoraes.entityrevision.api.RevisionField;

@DatabaseTable
@RevisionField
public class Entity3 implements Entity{

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField
    private String value3;

    @Override
    public Long getId() {
        return id;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Entity3 entity3 = (Entity3) object;

        return value3 != null ? value3.equals(entity3.value3) : entity3.value3 == null;

    }

    @Override
    public int hashCode() {
        return value3 != null ? value3.hashCode() : 0;
    }
}
