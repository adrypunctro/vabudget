/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vabudget;

import config.MySQLConfig;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian Simionescu
 */
public class MySQLDAOFactory extends DAOFactory {
    
    private static LocalConfig localConfig;
    
    public MySQLDAOFactory(Config config)
    {
        localConfig = (LocalConfig)config;
    }
    
    @Override
    public boolean connected()
    {
        Connection conn = null;
        try {
            conn = createConnection();
            return conn.isValid(100);
        } catch (SQLException | ClassNotFoundException ex) {
            
        } finally {
            try {
                if(conn != null) conn.close();
            } catch (SQLException e) {
                
            }
        }
        return false;
    }
    
    // method to create Cloudscape connections
    public static Connection createConnection() throws ClassNotFoundException, SQLException {
        Class.forName(localConfig.DRIVER);
        return DriverManager.getConnection(localConfig.DBURL, localConfig.USER, localConfig.PASS);
    }
    
    public static void createSchema() throws ClassNotFoundException, SQLException {
        Class.forName(localConfig.DRIVER);
        Connection conn = DriverManager.getConnection(localConfig.DBURL, localConfig.USER, localConfig.PASS);
        Statement stmt = conn.createStatement();
        for (String sql : MySQLConfig.createStmt()) {
            stmt.addBatch(sql);
        }
        stmt.executeBatch();
        stmt.close();
        conn.close();
    }
    
    @Override
    public CardDAO getCardDAO() {
        // MySQLCardDAO implements CardDAO
        return new MySQLCardDAO();
    }

    @Override
    public TransactionDAO getTransactionDAO() {
        // MySQLTransactionDAO implements TransactionDAO
        return new MySQLTransactionDAO();
    }

    @Override
    public DistributionDAO getDistributionDAO() {
        // MySQLDistributionDAO implements DistributionDAO
        return new MySQLDistributionDAO();
    }

    @Override
    public ShareDAO getShareDAO() {
        // MySQLShareDAO implements ShareDAO
        return new MySQLShareDAO();
    }
    
}
