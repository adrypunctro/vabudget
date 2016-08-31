/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vabudget;

import config.MySQLConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Adrian Simionescu
 */
public class MySQLShareDAO implements ShareDAO {

    @Override
    public boolean insertShare(int cardId, int personId) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = MySQLDAOFactory.createConnection();
            stmt = conn.createStatement();
            
            String sql = "INSERT INTO " + MySQLConfig.PREFIX + MySQLConfig.SHARED_TABLE + " ("
                        + MySQLConfig.SHARED_CARD_ID + ", "
                        + MySQLConfig.SHARED_PERSON_ID + ", "
                        + MySQLConfig.SHARED_STATUS + " "
                    + ") "
                    + "VALUES ("
                        + "'" + cardId + "', "
                        + "'" + personId + "', "
                        + "'0' "
                    + ")";
            
            int affectedRows = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            if (affectedRows == 0) {
                throw new SQLException("Creating share failed, no rows affected.");
            }
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            
        } finally {
            try {
                if(rs != null) rs.close();
                if(stmt != null) stmt.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                
            }
        }
        return false;
    }

    @Override
    public boolean deleteShare(int cardId, int personId) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = MySQLDAOFactory.createConnection();
            stmt = conn.createStatement();

            String sql = "DELETE FROM " + MySQLConfig.PREFIX + MySQLConfig.SHARED_TABLE
                    + " WHERE " + MySQLConfig.SHARED_CARD_ID + " = '" + cardId + "'"
                    + "AND " + MySQLConfig.SHARED_PERSON_ID + " = '" + personId + "'"
                    + " LIMIT 1";
            int affectedRows = stmt.executeUpdate(sql);
            if (affectedRows == 0) {
                throw new SQLException("Deleting share failed, no rows affected.");
            }
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                
            }
        }
        return false;
    }

    @Override
    public Share findShare(int cardId, int personId) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = MySQLDAOFactory.createConnection();
            stmt = conn.createStatement();

            String sql = "SELECT * FROM " + MySQLConfig.PREFIX + MySQLConfig.SHARED_TABLE
                    + " WHERE " + MySQLConfig.SHARED_CARD_ID + " = '" + cardId + "'"
                    + "AND " + MySQLConfig.SHARED_PERSON_ID + " = '" + personId + "'"
                    + " LIMIT 1";
            rs = stmt.executeQuery(sql);
            if(rs.next()) {
                return new Share(
                    rs.getInt(MySQLConfig.SHARED_CARD_ID),
                    rs.getInt(MySQLConfig.SHARED_PERSON_ID),
                    rs.getString(MySQLConfig.SHARED_STATUS)
                );
            }
        } catch (SQLException | ClassNotFoundException ex) {
            
        } finally {
            try {
                if(rs != null) rs.close();
                if(stmt != null) stmt.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                
            }
        }
        return null;
    }

    @Override
    public boolean acceptShare(int cardId, int personId) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = MySQLDAOFactory.createConnection();
            stmt = conn.createStatement();

            String sql = "UPDATE " + MySQLConfig.PREFIX + MySQLConfig.SHARED_TABLE
                    + " SET " + MySQLConfig.SHARED_STATUS + " = '1'"
                    + " WHERE " + MySQLConfig.SHARED_CARD_ID + " = '" + cardId + "'"
                    + "AND " + MySQLConfig.SHARED_PERSON_ID + " = '" + personId + "'"
                    + " LIMIT 1";
            int affectedRows = stmt.executeUpdate(sql);
            if (affectedRows == 0) {
                throw new SQLException("Updating share failed, no rows affected.");
            }
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                
            }
        }
        return false;
    }

    @Override
    public boolean rejectShare(int cardId, int personId) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = MySQLDAOFactory.createConnection();
            stmt = conn.createStatement();

            String sql = "UPDATE " + MySQLConfig.PREFIX + MySQLConfig.SHARED_TABLE
                    + " SET " + MySQLConfig.SHARED_STATUS + " = '-1'"
                    + " WHERE " + MySQLConfig.SHARED_CARD_ID + " = '" + cardId + "'"
                    + "AND " + MySQLConfig.SHARED_PERSON_ID + " = '" + personId + "'"
                    + " LIMIT 1";
            int affectedRows = stmt.executeUpdate(sql);
            if (affectedRows == 0) {
                throw new SQLException("Updating share failed, no rows affected.");
            }
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                
            }
        }
        return false;
    }

    @Override
    public List<Share> selectShared(int cardId) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Share> results = new ArrayList<>();
        try {
            conn = MySQLDAOFactory.createConnection();
            stmt = conn.createStatement();

            String sql = "SELECT * FROM " + MySQLConfig.PREFIX + MySQLConfig.SHARED_TABLE
                    + " WHERE " + MySQLConfig.SHARED_CARD_ID + " = '" + cardId + "'";
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                results.add(
                    new Share(
                        rs.getInt(MySQLConfig.SHARED_CARD_ID),
                        rs.getInt(MySQLConfig.SHARED_PERSON_ID),
                        rs.getString(MySQLConfig.SHARED_STATUS)
                    )
                );
            }
            if(!results.isEmpty()) {
                return results;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            
        } finally {
            try {
                if(rs != null) rs.close();
                if(stmt != null) stmt.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                
            }
        }
        return Collections.emptyList();
    }
    
}
