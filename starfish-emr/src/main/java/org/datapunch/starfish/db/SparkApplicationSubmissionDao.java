package org.datapunch.starfish.db;

import org.datapunch.starfish.db.framework.BaseJdbcDao;
import org.datapunch.starfish.db.framework.ConnectionInfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SparkApplicationSubmissionDao extends BaseJdbcDao {

    public static final String PARTITION_KEY = "DAYOFMONTH(createTime)";

    public static final String[] PRIMARY_KEYS = new String[] {"createTime", "environment", "cluster", "submissionId"};

    public static final String[] INDEX_COLUMNS = new String[] {"createTime", "environment", "cluster", "submissionId"};

    public static final String[] DATETIME_COLUMNS = new String[] {"createTime"};

    public static final String[] TEXT_COLUMNS = new String[] {"submissionRequest"};

    public SparkApplicationSubmissionDao(String jdbcDriverClass, String connectionString, String tableName) {
        super(
                jdbcDriverClass,
                connectionString,
                SparkApplicationSubmissionEntity.class,
                tableName,
                PARTITION_KEY,
                Arrays.asList(PRIMARY_KEYS),
                Arrays.asList(INDEX_COLUMNS),
                Arrays.asList(DATETIME_COLUMNS),
                Arrays.asList(TEXT_COLUMNS));
    }

    public SparkApplicationSubmissionDao(String jdbcDriverClass, ConnectionInfo connectionInfo, String tableName) {
        super(
                jdbcDriverClass,
                connectionInfo,
                SparkApplicationSubmissionEntity.class,
                tableName,
                PARTITION_KEY,
                Arrays.asList(PRIMARY_KEYS),
                Arrays.asList(INDEX_COLUMNS),
                Arrays.asList(DATETIME_COLUMNS),
                Arrays.asList(TEXT_COLUMNS));
    }

    public void insertOrUpdate(SparkApplicationSubmissionEntity entity) {
        Map map = new HashMap();
        map.put("createTime", entity.getCreateTime());
        map.put("environment", entity.getEnvironment());
        map.put("cluster", entity.getCluster());
        map.put("submissionId", entity.getSubmissionId());
        map.put("submissionRequest", entity.getSubmissionRequest());
        insertOrUpdate(map);
    }
}
