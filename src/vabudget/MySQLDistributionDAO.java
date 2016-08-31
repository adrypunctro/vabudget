/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vabudget;

import config.MySQLConfig;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.RowSet;

/**
 *
 * @author Adrian Simionescu
 */
public class MySQLDistributionDAO implements DistributionDAO {

    @Override
    public int insertDistribution(int ownerId, String label, Map<Integer, Integer> ratio) {
        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;
        int distribId;
        try {
            conn = MySQLDAOFactory.createConnection();
            conn.setAutoCommit(false);
            
            stmt1 = conn.prepareStatement("INSERT INTO " + MySQLConfig.PREFIX + MySQLConfig.DISTRIBUTIONS_TABLE + " ("
                        + MySQLConfig.DISTRIBUTIONS_OWNER_ID + ", "
                        + MySQLConfig.DISTRIBUTIONS_LABEL + " "
                    + ") "
                    + "VALUES ("
                        + "'" + ownerId + "', "
                        + "'" + label + "' "
                    + ")", Statement.RETURN_GENERATED_KEYS);
            stmt1.executeUpdate();
            rs = stmt1.getGeneratedKeys();
            if (rs.next()) {
                distribId =  rs.getInt(1);
            }
            else {
                throw new SQLException("Creating distribution failed, no ID obtained.");
            }
            
            for (int cardId : ratio.keySet()) {
                int percente = ratio.get(cardId);
                stmt2 = conn.prepareStatement("INSERT INTO " + MySQLConfig.PREFIX + MySQLConfig.DISTRIBUTIONS_RATIO_TABLE + " ("
                        + MySQLConfig.DISTRIBUTIONS_RATIO_DISTRIB_ID + ", "
                        + MySQLConfig.DISTRIBUTIONS_RATIO_CARD_ID + ", "
                        + MySQLConfig.DISTRIBUTIONS_RATIO_PERCENT + " "
                    + ") "
                    + "VALUES ("
                        + "'" + distribId + "', "
                        + "'" + cardId + "', "
                        + "'" + percente + "' "
                    + ")");
                stmt2.executeUpdate();
            }
            conn.commit();
            return distribId;
        } catch (SQLException | ClassNotFoundException ex) {
            try {
                if(conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex1) {
                // TODO: It's bad. Rollback failed.
            }
        } finally {
            try {
                if(rs != null) rs.close();
                if(stmt1 != null) stmt1.close();
                if(stmt2 != null) stmt2.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                
            }
        }
        return 0;
    }

    @Override
    public boolean deleteDistribution(int distribId) {
        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        try {
            conn = MySQLDAOFactory.createConnection();
            conn.setAutoCommit(false);
            
            stmt1 = conn.prepareStatement("DELETE FROM " + MySQLConfig.PREFIX + MySQLConfig.DISTRIBUTIONS_TABLE + " "
                        + " WHERE " + MySQLConfig.DISTRIBUTIONS_DISTRIB_ID + " = '" + distribId + "'");
            stmt1.executeUpdate();
            
            stmt2 = conn.prepareStatement("DELETE FROM " + MySQLConfig.PREFIX + MySQLConfig.DISTRIBUTIONS_RATIO_TABLE + " "
                        + " WHERE " + MySQLConfig.DISTRIBUTIONS_RATIO_DISTRIB_ID + " = '" + distribId + "'");
            stmt2.executeUpdate();
            
            conn.commit();
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            try {
                if(conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex1) {
                // TODO: It's bad. Rollback failed.
            }
        } finally {
            try {
                if(stmt1 != null) stmt1.close();
                if(stmt2 != null) stmt2.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                
            }
        }
        return false;
    }

    @Override
    public Distribution findDistribution(int distribId) {
        Connection conn = null;
        Statement stmt1 = null;
        Statement stmt2 = null;
        ResultSet rs = null;
        try {
            conn = MySQLDAOFactory.createConnection();

            stmt2 = conn.createStatement();
            rs = stmt2.executeQuery("SELECT * FROM " + MySQLConfig.PREFIX + MySQLConfig.DISTRIBUTIONS_RATIO_TABLE
                    + " WHERE " + MySQLConfig.DISTRIBUTIONS_RATIO_DISTRIB_ID + " = '" + distribId + "'");
            Map<Integer, Integer> ratio = new HashMap<>();
            while(rs.next()){
                ratio.put(
                    rs.getInt(MySQLConfig.DISTRIBUTIONS_RATIO_CARD_ID),
                    rs.getInt(MySQLConfig.DISTRIBUTIONS_RATIO_PERCENT)
                );
            }
            
            stmt2 = conn.createStatement();
            rs = stmt2.executeQuery("SELECT * FROM " + MySQLConfig.PREFIX + MySQLConfig.DISTRIBUTIONS_TABLE
                    + " WHERE " + MySQLConfig.DISTRIBUTIONS_DISTRIB_ID + " = '" + distribId + "'"
                    + " LIMIT 1");
            if(rs.next()) {
                return new Distribution(
                    rs.getInt(MySQLConfig.DISTRIBUTIONS_DISTRIB_ID),
                    rs.getInt(MySQLConfig.DISTRIBUTIONS_OWNER_ID),
                    rs.getString(MySQLConfig.DISTRIBUTIONS_LABEL),
                    ratio
                );
            }
        } catch (SQLException | ClassNotFoundException ex) {
            
        } finally {
            try {
                if(rs != null) rs.close();
                if(stmt1 != null) stmt1.close();
                if(stmt2 != null) stmt2.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                
            }
        }
        return null;
    }

    @Override
    public boolean updateDistribution(int distribId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Distribution> selectDistributions(int personId) {
        Connection conn = null;
        Statement stmt1 = null;
        Statement stmt2 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        List<Distribution> results = new ArrayList<>();
        try {
            conn = MySQLDAOFactory.createConnection();
            stmt1 = conn.createStatement();
            rs1 = stmt1.executeQuery("SELECT * FROM " + MySQLConfig.PREFIX + MySQLConfig.DISTRIBUTIONS_TABLE);
            while(rs1.next()){
                stmt2 = conn.createStatement();
                rs2 = stmt2.executeQuery("SELECT * FROM " + MySQLConfig.PREFIX + MySQLConfig.DISTRIBUTIONS_RATIO_TABLE
                        + " WHERE " + MySQLConfig.DISTRIBUTIONS_RATIO_DISTRIB_ID + " = '" + rs1.getInt(MySQLConfig.DISTRIBUTIONS_RATIO_DISTRIB_ID) + "'");
                Map<Integer, Integer> ratio = new HashMap<>();
                while(rs2.next()){
                    ratio.put(
                        rs2.getInt(MySQLConfig.DISTRIBUTIONS_RATIO_CARD_ID),
                        rs2.getInt(MySQLConfig.DISTRIBUTIONS_RATIO_PERCENT)
                    );
                }
                results.add(
                    new Distribution(
                        rs1.getInt(MySQLConfig.DISTRIBUTIONS_DISTRIB_ID),
                        rs1.getInt(MySQLConfig.DISTRIBUTIONS_OWNER_ID),
                        rs1.getString(MySQLConfig.DISTRIBUTIONS_LABEL),
                        ratio
                    )
                );
            }
            if(!results.isEmpty()) {
                return results;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            
        } finally {
            try {
                if(rs1 != null) rs1.close();
                if(rs2 != null) rs2.close();
                if(stmt1 != null) stmt1.close();
                if(stmt2 != null) stmt2.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                
            }
        }
        return Collections.emptyList();
    }
    
}
