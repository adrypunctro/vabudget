/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vabudget;

/**
 * Abstract class DAO Factory
 * More details about Core J2EE Patterns - Data Access Object on:
 *   http://www.oracle.com/technetwork/java/dataaccessobject-138824.html
 * 
 * @author Adrian Simionescu
 */

public abstract class DAOFactory {
    // There will be a method for each DAO that can be 
    // created. The concrete factories will have to 
    // implement these methods.
    public abstract boolean connected();
    public abstract CardDAO getCardDAO();
    public abstract TransactionDAO getTransactionDAO();
    public abstract DistributionDAO getDistributionDAO();
    public abstract ShareDAO getShareDAO();
    

    public static DAOFactory getDAOFactory(WalletConnection whichFactory, Config config) {
        switch (whichFactory) {
        case MYSQL:
            return new MySQLDAOFactory(config);
        case WEB:
            return new MySQLDAOFactory(config);
        default:
            return null;
        }
    }
}
