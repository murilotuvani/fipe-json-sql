/**
 *  @see https://mit-license.org/
 *  The MIT License (MIT)
 * Copyright © 2019 <copyright holders>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions: The above copyright
 * notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.cartola.fipe.dao;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.serial.SerialBlob;

/**
 * 05/11/2019 17:59:57
 *
 * @author murilo
 */
public abstract class Dao<T> {
    
    private static java.sql.Connection connection = null;
    
    public int salvar(T t) throws SQLException, ClassNotFoundException {
        int id = 0;
        String comando = getInsert();
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(comando, Statement.RETURN_GENERATED_KEYS);
        prepareUpdate(stmt, t);
        int afetados = stmt.executeUpdate();

        try (ResultSet chavesGeradas = stmt.getGeneratedKeys()) {
            if (chavesGeradas.next()) {
                id = chavesGeradas.getInt(1);
            }
        }
        System.out.println("Registros salvos " + afetados);
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINER, "Insertiu {0} registro(s)", afetados);
        return id;
    }

    protected abstract String getInsert();

    protected abstract String getUpdate();

    protected abstract void prepareUpdate(PreparedStatement stmt, T t) throws SQLException;

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        if (this.connection == null) {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fipe?useSSL=false", "root", "root");
        }
        return this.connection;
    }

    protected void setNullSafe(PreparedStatement stmt, Integer value, int index) throws SQLException {
        if (value == null || value == 0) {
            stmt.setNull(index, java.sql.Types.INTEGER);
        } else {
            stmt.setInt(index, value);
        }
    }

    protected void setNullSafe(PreparedStatement stmt, Long value, int index) throws SQLException {
        if (value == null || value == 0) {
            stmt.setNull(index, java.sql.Types.BIGINT);
        } else {
            stmt.setLong(index, value);
        }
    }

    protected void setNullSafe(PreparedStatement stmt, String value, int index) throws SQLException {
        if (isNull(value)) {
            stmt.setNull(index, java.sql.Types.VARCHAR);
        } else {
            stmt.setString(index, value);
        }
    }

    protected void setNullSafe(PreparedStatement stmt, Double value, int index) throws SQLException {
        if (value == null || value == 0) {
            stmt.setNull(index, java.sql.Types.DOUBLE);
        } else {
            stmt.setDouble(index, value);
        }
    }

    protected void setNullSafe(PreparedStatement stmt, Float value, int index) throws SQLException {
        if (value == null || value == 0) {
            stmt.setNull(index, java.sql.Types.DOUBLE);
        } else {
            stmt.setDouble(index, value);
        }
    }

    protected void setNullSafe(PreparedStatement stmt, java.util.Date date, int idx) throws SQLException {
        if (date == null) {
            stmt.setNull(idx++, java.sql.Types.TIMESTAMP);
        } else {
            stmt.setTimestamp(idx++, new java.sql.Timestamp(date.getTime()));
        }
    }

    protected void setNullSafe(PreparedStatement stmt, Boolean value, int index) throws SQLException {
        if (value != null) {
            stmt.setBoolean(index, value);
        } else {
            stmt.setNull(index, java.sql.Types.BOOLEAN);
        }
    }

    protected void setNullSafe(PreparedStatement stmt, byte[] value, int index) throws SQLException {
        if (value != null) {
            stmt.setBlob(index, new SerialBlob(value));
        } else {
            stmt.setNull(index, java.sql.Types.BLOB);
        }
    }

    protected Integer getIntOrNull(ResultSet rs, String string) throws SQLException {
        int value = rs.getInt(string);
        if (rs.wasNull()) {
            return null;
        }
        return value;
    }

    protected Long getLongOrNull(ResultSet rs, String string) throws SQLException {
        long value = rs.getLong(string);
        if (rs.wasNull()) {
            return null;
        }
        return value;
    }

    protected Float getFloatOrNull(ResultSet rs, String string) throws SQLException {
        Float value = rs.getFloat(string);
        if (rs.wasNull()) {
            return null;
        }
        return value;
    }

    protected Double getDoubleOrNull(ResultSet rs, String string) throws SQLException {
        Double value = rs.getDouble(string);
        if (rs.wasNull()) {
            return null;
        }
        return value;
    }

    protected String getStringOrNull(ResultSet rs, String string) throws SQLException {
        String value = rs.getString(string);
        if (rs.wasNull()) {
            return null;
        }
        return value;
    }

    protected Date getDateOrNull(ResultSet rs, String string) throws SQLException {
        Date value = rs.getDate(string);
        if (rs.wasNull()) {
            return null;
        }
        return value;
    }

    protected Date getTimeOrNull(ResultSet rs, String string) throws SQLException {
        Date value = rs.getTime(string);
        if (rs.wasNull()) {
            return null;
        }
        return value;
    }

    protected Date getTimestampOrNull(ResultSet rs, String string) throws SQLException {
        Date value = rs.getTimestamp(string);
        if (rs.wasNull()) {
            return null;
        }
        return value;
    }

    protected Boolean getBooleanOrNull(ResultSet rs, String string) throws SQLException {
        Boolean value = rs.getBoolean(string);
        if (rs.wasNull()) {
            value = null;
        }
        return value;
    }

    protected byte[] getBlobOrNull(ResultSet rs, String string) throws SQLException {
        Blob blob = rs.getBlob(string);
        byte[] value = null;
        if (!rs.wasNull()) {
            long length = blob.length();
            value = (blob.getBytes(1, (int) length));
        }
        return value;
    }

    private boolean isNull(String value) {
        return (value == null || "".equals(value.trim()));
    }
}
