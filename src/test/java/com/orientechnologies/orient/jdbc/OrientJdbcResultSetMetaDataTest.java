package com.orientechnologies.orient.jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class OrientJdbcResultSetMetaDataTest extends OrientJdbcBaseTest {

    @Test
    public void shouldMapReturnTypes() throws Exception {

        assertFalse(conn.isClosed());

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT stringKey, intKey, text, length, date FROM Item");
        assertEquals(20, rs.getFetchSize());

        assertTrue(rs.isBeforeFirst());

        assertTrue(rs.next());

        assertEquals(1, rs.getRow());

        assertEquals("1", rs.getString(2));
        assertEquals("1", rs.getString("stringKey"));
        assertEquals(2, rs.findColumn("stringKey"));

        assertEquals(1, rs.getInt(3));
        assertEquals(1, rs.getInt("intKey"));

        assertEquals(rs.getString("text").length(), rs.getLong("length"));

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.add(Calendar.HOUR_OF_DAY, -1);
        Date date = new Date(cal.getTimeInMillis());
        assertEquals(date.toString(), rs.getDate("date").toString());
        assertEquals(date.toString(), rs.getDate(6).toString());

        rs.last();

        assertEquals(20, rs.getRow());
        rs.close();

        assertTrue(rs.isClosed());
    }

    @Test
    public void shouldNavigateResultSet() throws Exception {
        assertFalse(conn.isClosed());

        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        ResultSet rs = stmt.executeQuery("SELECT * FROM Item");
        assertEquals(20, rs.getFetchSize());

        assertTrue(rs.isBeforeFirst());

        assertTrue(rs.next());

        assertEquals(1, rs.getRow());

        rs.last();

        assertEquals(20, rs.getRow());

        assertFalse(rs.next());

        rs.afterLast();

        assertFalse(rs.next());

        rs.close();

        assertTrue(rs.isClosed());

        stmt.close();

        assertTrue(stmt.isClosed());
    }

    @Test
    public void shouldReturnResultSetAfterExecute() throws Exception {

        assertFalse(conn.isClosed());

        Statement stmt = conn.createStatement();

        assertTrue(stmt.execute("SELECT stringKey, intKey, text, length, date FROM Item"));
        ResultSet rs = stmt.getResultSet();
        assertNotNull(rs);
        assertEquals(20, rs.getFetchSize());

    }

    @Test
    public void shouldNavigateResultSetByMetadata() throws Exception {
        assertFalse(conn.isClosed());

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT stringKey, intKey, text, length, date FROM Item");

        rs.next();
        ResultSetMetaData metaData = rs.getMetaData();
        assertEquals(6, metaData.getColumnCount());

        assertEquals("stringKey", metaData.getColumnName(2));
        assertTrue(rs.getObject(2) instanceof String);

        assertEquals("intKey", metaData.getColumnName(3));

        assertEquals("text", metaData.getColumnName(4));
        assertTrue(rs.getObject(4) instanceof String);

        assertEquals("length", metaData.getColumnName(5));

        assertEquals("date", metaData.getColumnName(6));

    }


    @Test
    public void shouldMapOrientTypesToJavaSQL() throws Exception {
        assertFalse(conn.isClosed());

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT stringKey, intKey, text, length, date FROM Item");

        rs.next();
        ResultSetMetaData metaData = rs.getMetaData();

        assertEquals(6, metaData.getColumnCount());

        assertEquals(Types.INTEGER, metaData.getColumnType(3));

        assertEquals(Types.VARCHAR, metaData.getColumnType(4));
        assertTrue(rs.getObject(4) instanceof String);

        assertEquals(Types.BIGINT, metaData.getColumnType(5));

        assertEquals(Types.TIMESTAMP, metaData.getColumnType(6));

        assertEquals(String.class.getName(), metaData.getColumnClassName(2));
        assertEquals(Types.VARCHAR, metaData.getColumnType(2));

    }


}
