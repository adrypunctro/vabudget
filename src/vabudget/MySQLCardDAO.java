/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vabudget;

import config.MySQLConfig;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Adrian Simionescu
 */
public class MySQLCardDAO implements CardDAO {
    final private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public MySQLCardDAO() {
        // initialization 
    }
    
    @Override
    public int insertCard(int ownerId, String label, BigDecimal initAmount) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = MySQLDAOFactory.createConnection();
            stmt = conn.createStatement();
            
            String sql = "INSERT INTO " + MySQLConfig.PREFIX + MySQLConfig.CARDS_TABLE + " ("
                        + MySQLConfig.CARDS_OWNER_ID + ", "
                        + MySQLConfig.CARDS_LABEL + ", "
                        + MySQLConfig.CARDS_AMOUNT + " "
                    + ") "
                    + "VALUES ("
                        + "'" + String.valueOf(ownerId) + "', "
                        + "'" + label + "', "
                        + "'" + initAmount.toPlainString() + "'"
                    + ")";
            
            int affectedRows = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            if (affectedRows == 0) {
                throw new SQLException("Creating card failed, no rows affected.");
            }
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            else {
                throw new SQLException("Creating card failed, no ID obtained.");
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
        return 0;
    }

    @Override
    public boolean deleteCard(int cardId) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = MySQLDAOFactory.createConnection();
            stmt = conn.createStatement();

            String sql = "DELETE FROM " + MySQLConfig.PREFIX + MySQLConfig.CARDS_TABLE
                    + " WHERE " + MySQLConfig.CARDS_CARD_ID + " = '" + cardId + "'"
                    + " LIMIT 1";
            int affectedRows = stmt.executeUpdate(sql);
            if (affectedRows == 0) {
                throw new SQLException("Deleting card failed, no rows affected.");
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
    public Card findCard(int cardId) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = MySQLDAOFactory.createConnection();
            stmt = conn.createStatement();

            String sql = "SELECT * FROM " + MySQLConfig.PREFIX + MySQLConfig.CARDS_TABLE
                    + " WHERE " + MySQLConfig.CARDS_CARD_ID + " = '" + cardId + "'"
                    + " LIMIT 1";
            rs = stmt.executeQuery(sql);
            if(rs.next()) {
                return new DebitCard(
                    rs.getInt(MySQLConfig.CARDS_CARD_ID),
                    rs.getInt(MySQLConfig.CARDS_OWNER_ID),
                    rs.getString(MySQLConfig.CARDS_LABEL),
                    new BigDecimal(rs.getString(MySQLConfig.CARDS_AMOUNT))
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
    public boolean updateCardLabel(int cardId, String label) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = MySQLDAOFactory.createConnection();
            stmt = conn.createStatement();

            String sql = "UPDATE " + MySQLConfig.PREFIX + MySQLConfig.CARDS_TABLE
                    + " SET " + MySQLConfig.CARDS_LABEL + " = '" + label + "'"
                    + " WHERE " + MySQLConfig.CARDS_CARD_ID + " = '" + cardId + "'"
                    + " LIMIT 1";
            int affectedRows = stmt.executeUpdate(sql);
            if (affectedRows == 0) {
                throw new SQLException("Updating card failed, no rows affected.");
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
    public List<Card> selectCards(int ownerId) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Card> results = new ArrayList<>();
        try {
            conn = MySQLDAOFactory.createConnection();
            stmt = conn.createStatement();

            String sql = "SELECT * FROM " + MySQLConfig.PREFIX + MySQLConfig.CARDS_TABLE
                    + " WHERE " + MySQLConfig.CARDS_OWNER_ID + " = '" + ownerId + "'";
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                results.add(
                    new DebitCard(
                        rs.getInt(MySQLConfig.CARDS_CARD_ID),
                        rs.getInt(MySQLConfig.CARDS_OWNER_ID),
                        rs.getString(MySQLConfig.CARDS_LABEL),
                        new BigDecimal(rs.getString(MySQLConfig.CARDS_AMOUNT))
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
