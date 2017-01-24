package org.andrelsmoraes.entityrevision.api;

import java.util.Date;
import java.util.Set;

public interface RevisionManager {

    EntityRevision save(Entity entity) throws RevisionOperationException;

    EntityRevision saveAndClearNotActive(Entity entity) throws RevisionOperationException;

    EntityRevision retrieveActive(Entity entity) throws RevisionOperationException;

    Set<EntityRevision> retrieveByDate(Entity entity, Date start, Date end) throws RevisionOperationException;

    Set<EntityRevision> retrieveAll(Entity entity) throws RevisionOperationException;

    void clearNotActive(Entity entity) throws RevisionOperationException;

}
