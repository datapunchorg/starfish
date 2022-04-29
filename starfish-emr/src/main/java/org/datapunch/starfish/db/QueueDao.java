package org.datapunch.starfish.db;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class QueueDao extends BaseJdbcDao {

    public static final String PARTITION_KEY = null;

    public static final String[] PRIMARY_KEYS = new String[] {"environment", "name"};

    public static final String[] INDEX_COLUMNS = new String[] {"environment", "name"};

    public static final String[] DATETIME_COLUMNS = new String[] {};

    public static final String[] TEXT_COLUMNS = new String[] {};

    public QueueDao(String jdbcDriverClass, String connectionString, String tableName) {
        super(
                jdbcDriverClass,
                connectionString,
                QueueDbEntity.class,
                tableName,
                PARTITION_KEY,
                Arrays.asList(PRIMARY_KEYS),
                Arrays.asList(INDEX_COLUMNS),
                Arrays.asList(DATETIME_COLUMNS),
                Arrays.asList(TEXT_COLUMNS));
    }

    public void insertOrUpdate(QueueDbEntity entity) {
        Map map = new HashMap();
        map.put("environment", entity.getEnvironment());
        map.put("name", entity.getName());
        map.put("weight", entity.getWeight());
        map.put("cluster", entity.getCluster());
        insertOrUpdate(map);
    }
}
