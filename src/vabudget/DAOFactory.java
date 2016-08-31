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
    // List of DAO types supported by the factory
    public static final int MYSQL = 1;

    // There will be a method for each DAO that can be 
    // created. The concrete factories will have to 
    // implement these methods.
    public abstract CardDAO getCardDAO();
    public abstract TransactionDAO getTransactionDAO();
    public abstract DistributionDAO getDistributionDAO();
    public abstract ShareDAO getShareDAO();
    

    public static DAOFactory getDAOFactory(int whichFactory) {
        switch (whichFactory) {
        case MYSQL:
            return new MySQLDAOFactory();
        default:
            return null;
        }
    }
}
