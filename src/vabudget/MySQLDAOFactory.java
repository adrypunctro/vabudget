/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vabudget;

import config.MySQLConfig;
import java.sql.*;

/**
 *
 * @author Adrian Simionescu
 */
public class MySQLDAOFactory extends DAOFactory {
    
    // method to create Cloudscape connections
    public static Connection createConnection() throws ClassNotFoundException, SQLException {
        Class.forName(MySQLConfig.DRIVER);
        return DriverManager.getConnection(MySQLConfig.DBURL, MySQLConfig.USER, MySQLConfig.PASS);
    }
    
    public static void createSchema() throws ClassNotFoundException, SQLException {
        Class.forName(MySQLConfig.DRIVER);
        Connection conn = DriverManager.getConnection(MySQLConfig.DBURL, MySQLConfig.USER, MySQLConfig.PASS);
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
