package org.andrelsmoraes.entityrevision.api;

import java.io.Serializable;
import java.util.Date;

public interface EntityRevision {

    Serializable getEntityId();

    String getEntityName();

    String getEntityAsJson();

    Date getRevisionDate();

    boolean isActive();

}
