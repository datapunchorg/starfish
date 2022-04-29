package org.datapunch.starfish.db;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class SparkApplicationSubmissionDaoTest {
    @Test
    public void test() throws IOException {
        File file = File.createTempFile("h2test", ".db");
        file.deleteOnExit();

        String connectionString =
                String.format("jdbc:h2:%s;DB_CLOSE_DELAY=-1;MODE=MySQL", file.getAbsolutePath());

        SparkApplicationSubmissionDao dao = new SparkApplicationSubmissionDao("org.h2.Driver", connectionString, "test_SparkApplicationSubmissionDao_table");
        dao.createTable();
        dao.queryColumns(1000, "*");

        SparkApplicationSubmissionEntity entity1 = new SparkApplicationSubmissionEntity();
        entity1.setCreateTime(System.currentTimeMillis());
        entity1.setCluster("cluster01");
        entity1.setSubmissionId("submission01");
        entity1.setSubmissionRequest("request body 01");

        dao.insertOrUpdate(entity1);

        SparkApplicationSubmissionEntity entity2 = new SparkApplicationSubmissionEntity();
        entity2.setCreateTime(System.currentTimeMillis());
        entity2.setCluster("cluster02");
        entity2.setSubmissionId("submission02");
        entity2.setSubmissionRequest("request body 02");

        dao.insertOrUpdate(entity2);

        Assert.assertEquals(2, dao.getTotalCount());

        SparkApplicationSubmissionEntity readback1 = dao.getByPrimaryKeys(
                Arrays.asList(
                        entity1.getCreateTime(),
                        entity1.getEnvironment(),
                        entity1.getCluster(),
                        entity1.getSubmissionId()),
                SparkApplicationSubmissionEntity.class);

        Assert.assertEquals(entity1.getCreateTime(), readback1.getCreateTime());
        Assert.assertEquals(entity1.getEnvironment(), readback1.getEnvironment());
        Assert.assertEquals(entity1.getCluster(), readback1.getCluster());
        Assert.assertEquals(entity1.getSubmissionId(), readback1.getSubmissionId());
        Assert.assertEquals(entity1.getSubmissionRequest(), readback1.getSubmissionRequest());

        SparkApplicationSubmissionEntity readback2 = dao.getByPrimaryKeys(
                Arrays.asList(
                        entity2.getCreateTime(),
                        entity2.getEnvironment(),
                        entity2.getCluster(),
                        entity2.getSubmissionId()),
                SparkApplicationSubmissionEntity.class);

        Assert.assertEquals(entity2.getCreateTime(), readback2.getCreateTime());
        Assert.assertEquals(entity2.getEnvironment(), readback2.getEnvironment());
        Assert.assertEquals(entity2.getCluster(), readback2.getCluster());
        Assert.assertEquals(entity2.getSubmissionId(), readback2.getSubmissionId());
        Assert.assertEquals(entity2.getSubmissionRequest(), readback2.getSubmissionRequest());

        entity2.setSubmissionRequest("request body 02 new value");

        dao.insertOrUpdate(entity2);

        Assert.assertEquals(2, dao.getTotalCount());

        readback2 = dao.getByPrimaryKeys(
                Arrays.asList(
                        entity2.getCreateTime(),
                        entity2.getEnvironment(),
                        entity2.getCluster(),
                        entity2.getSubmissionId()),
                SparkApplicationSubmissionEntity.class);

        Assert.assertEquals(entity2.getCreateTime(), readback2.getCreateTime());
        Assert.assertEquals(entity2.getEnvironment(), readback2.getEnvironment());
        Assert.assertEquals(entity2.getCluster(), readback2.getCluster());
        Assert.assertEquals(entity2.getSubmissionId(), readback2.getSubmissionId());
        Assert.assertEquals(entity2.getSubmissionRequest(), readback2.getSubmissionRequest());

        dao.close();
    }
}
