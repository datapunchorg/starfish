package org.datapunch.starfish.db;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class QueueDaoTest {
    @Test
    public void test() throws IOException {
        File file = File.createTempFile("h2test", ".db");
        file.deleteOnExit();

        String connectionString =
                String.format("jdbc:h2:%s;DB_CLOSE_DELAY=-1;MODE=PostgreSQL", file.getAbsolutePath());

        QueueDao dao = new QueueDao("org.h2.Driver", connectionString, "test_QueueDao_table");
        dao.createTable(false);
        dao.queryColumns(1000, "*");

        QueueEntity entity1 = new QueueEntity();
        entity1.setName("name01");
        entity1.setWeight(100);
        entity1.setCluster("cluster01");

        dao.insertOrUpdate(entity1);

        QueueEntity entity2 = new QueueEntity();
        entity2.setName("name02");
        entity2.setWeight(50);
        entity2.setCluster("cluster02");

        dao.insertOrUpdate(entity2);

        Assert.assertEquals(2, dao.getTotalCount());

        QueueEntity readback1 = dao.getByPrimaryKeys(
                Arrays.asList(
                        entity1.getEnvironment(),
                        entity1.getName()),
                QueueEntity.class);

        Assert.assertEquals(entity1.getEnvironment(), readback1.getEnvironment());
        Assert.assertEquals(entity1.getName(), readback1.getName());
        Assert.assertEquals(entity1.getWeight(), readback1.getWeight());
        Assert.assertEquals(entity1.getCluster(), readback1.getCluster());

        QueueEntity readback2 = dao.getByPrimaryKeys(
                Arrays.asList(
                        entity2.getEnvironment(),
                        entity2.getName()),
                QueueEntity.class);

        Assert.assertEquals(entity2.getEnvironment(), readback2.getEnvironment());
        Assert.assertEquals(entity2.getName(), readback2.getName());
        Assert.assertEquals(entity2.getWeight(), readback2.getWeight());
        Assert.assertEquals(entity2.getCluster(), readback2.getCluster());

        entity2 = new QueueEntity();
        entity2.setName("name02");
        entity2.setWeight(100);
        entity2.setCluster("cluster03");

        dao.insertOrUpdate(entity2);

        Assert.assertEquals(2, dao.getTotalCount());

        readback2 = dao.getByPrimaryKeys(
                Arrays.asList(
                        entity2.getEnvironment(),
                        entity2.getName()),
                QueueEntity.class);

        Assert.assertEquals(entity2.getEnvironment(), readback2.getEnvironment());
        Assert.assertEquals(entity2.getName(), readback2.getName());
        Assert.assertEquals(entity2.getWeight(), readback2.getWeight());
        Assert.assertEquals(entity2.getCluster(), readback2.getCluster());

        dao.close();
    }
}
