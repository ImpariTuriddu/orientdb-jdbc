package com.orientechnologies.orient.jdbc;

import java.sql.DriverManager;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.jdbc.common.OrientJdbcConnectionProperties;

import static java.lang.Class.forName;
import static com.orientechnologies.orient.jdbc.OrientDbCreationHelper.createSchemaDB;
import static com.orientechnologies.orient.jdbc.OrientDbCreationHelper.loadDB;

public abstract class OrientJdbcBaseTest {

    protected OrientJdbcConnection conn;

    @BeforeClass
    public static void loadDriver() throws ClassNotFoundException {
        forName(OrientJdbcDriver.class.getName());

    }

    @Before
    public void prepareDatabase() throws Exception {
        String dbUrl = "local:./working/db/test";
//        dbUrl = "memory:test";

        ODatabaseDocumentPool.global().close();

        ODatabaseDocumentTx db = new ODatabaseDocumentTx(dbUrl);

        String username = "admin";
        String password = "admin";

        if (db.exists()) {
            db.open(username, password);
            db.drop();
            db.close();
        }

        db.create();

        createSchemaDB(db);
        loadDB(db, 20);

        db.close();

        Properties info = new Properties();
        info.put(OrientJdbcConnectionProperties.Keys.USERNAME, username);
		info.put(OrientJdbcConnectionProperties.Keys.PASSWORD, password);
		
        conn = (OrientJdbcConnection) DriverManager.getConnection("jdbc:orient:" + dbUrl, info);

    }

    @After
    public void closeConnection() throws Exception {
        if (conn != null && !conn.isClosed()) conn.close();
    }

}
