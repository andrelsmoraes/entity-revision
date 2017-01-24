package org.andrelsmoraes.entityrevision.ormlite;

import android.support.test.runner.AndroidJUnit4;

import com.j256.ormlite.table.TableUtils;

import org.andrelsmoraes.entityrevision.api.Entity;
import org.andrelsmoraes.entityrevision.api.EntityRevision;
import org.andrelsmoraes.entityrevision.api.RevisionManager;
import org.andrelsmoraes.entityrevision.api.RevisionOperationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.SQLException;
import java.util.Set;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class RevisionTest {

    private DatabaseHelper databaseHelper;
    private RevisionManager revisionManager;

    private Entity1 entity1;
    private Entity2 entity2;
    private Entity3 entity3;

    @Before
    public void config() throws Exception {
        databaseHelper = new DatabaseHelper(getTargetContext());

        databaseHelper.getDao(EntityRevisionOrmLite.class).executeRaw("DROP TABLE IF EXISTS " + EntityRevisionOrmLite.class.getSimpleName());
        databaseHelper.getDao(EntityRevisionOrmLite.class).executeRaw("DROP TABLE IF EXISTS " + Entity1.class.getSimpleName());
        databaseHelper.getDao(EntityRevisionOrmLite.class).executeRaw("DROP TABLE IF EXISTS " + Entity2.class.getSimpleName());
        databaseHelper.getDao(EntityRevisionOrmLite.class).executeRaw("DROP TABLE IF EXISTS " + Entity3.class.getSimpleName());

        TableUtils.createTableIfNotExists(databaseHelper.getConnectionSource(), Entity1.class);
        TableUtils.createTableIfNotExists(databaseHelper.getConnectionSource(), Entity2.class);
        TableUtils.createTableIfNotExists(databaseHelper.getConnectionSource(), Entity3.class);

        revisionManager = new RevisionManagerOrmLite(databaseHelper);
    }

    /**
     * Entity1 is saved 3 times, there must be three revisions at database
     * Entity2 is a field of Entity1, there must be four revisions at database (saved three time with Entity1 and one time alone)
     * Entity3 is a field of Entity1, but marked as RevisionIgnore, there must be only one revision at database (saved alone)
     *
     * @throws Exception
     */
    private void initEntityRevision() throws Exception {
        entity3 = new Entity3();
        entity3.setValue3("Entity3");
        save(Entity3.class, entity3);

        entity2 = new Entity2();
        entity2.setValue2("Entity2");
        save(Entity2.class, entity2);

        entity1 = new Entity1();
        entity1.setValue1("Entity1");
        entity1.setEntity2(entity2);
        entity1.setEntity3(entity3);
        save(Entity1.class, entity1);

        entity1.setValue1("Entity1.1");
        save(Entity1.class, entity1);

        entity1.setValue1("Entity1.2");
        save(Entity1.class, entity1);
    }

    @Test
    public void retrieveAll() throws Exception {
        initEntityRevision();

        assertEquals(3, revisionManager.retrieveAll(entity1).size());
        assertEquals(4, revisionManager.retrieveAll(entity2).size());
        assertEquals(1, revisionManager.retrieveAll(entity3).size());
    }

    @Test
    public void retrieveActive() throws Exception {
        initEntityRevision();

        assertEquals(entity1, Entity.Util.fromJson(Entity1.class, revisionManager.retrieveActive(entity1).getEntityAsJson()));
        assertEquals(entity2, Entity.Util.fromJson(Entity2.class, revisionManager.retrieveActive(entity2).getEntityAsJson()));
        assertEquals(entity3, Entity.Util.fromJson(Entity3.class, revisionManager.retrieveActive(entity3).getEntityAsJson()));
    }

    @Test
    public void saveAndClearNotActive() throws Exception {
        initEntityRevision();

        entity1.setValue1("Entity1.3");
        revisionManager.saveAndClearNotActive(entity1);
        assertEquals(entity1, Entity.Util.fromJson(Entity1.class, revisionManager.retrieveActive(entity1).getEntityAsJson()));
        assertEquals(1, revisionManager.retrieveAll(entity1).size());

        entity2.setValue2("Entity2.1");
        revisionManager.saveAndClearNotActive(entity2);
        assertEquals(entity2, Entity.Util.fromJson(Entity2.class, revisionManager.retrieveActive(entity2).getEntityAsJson()));
        assertEquals(1, revisionManager.retrieveAll(entity2).size());

        entity3.setValue3("Entity3.1");
        revisionManager.saveAndClearNotActive(entity3);
        assertEquals(entity3, Entity.Util.fromJson(Entity3.class, revisionManager.retrieveActive(entity3).getEntityAsJson()));
        assertEquals(1, revisionManager.retrieveAll(entity3).size());
    }

    @Test
    public void clearNotActive() throws Exception {
        initEntityRevision();

        revisionManager.clearNotActive(entity1);
        assertEquals(1, revisionManager.retrieveAll(entity1).size());

        revisionManager.clearNotActive(entity2);
        assertEquals(1, revisionManager.retrieveAll(entity2).size());

        revisionManager.clearNotActive(entity3);
        assertEquals(1, revisionManager.retrieveAll(entity3).size());
    }

    private <T extends Entity> EntityRevision save(Class<T> entityClass, T entity) throws SQLException, RevisionOperationException {
        databaseHelper.getDao(entityClass).createOrUpdate(entity);
        return revisionManager.save(entity);
    }

    //TODO retrieve by date

}