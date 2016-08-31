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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Adrian Simionescu
 */
public class MySQLTransactionDAO implements TransactionDAO {
    final private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean deleteTransaction(int transId) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = MySQLDAOFactory.createConnection();
            stmt = conn.createStatement();

            String sql = "DELETE FROM " + MySQLConfig.PREFIX + MySQLConfig.TRANSACTIONS_TABLE
                    + " WHERE " + MySQLConfig.TRANSACTIONS_TRANSACTION_ID + " = '" + transId + "'"
                    + " LIMIT 1";
            int affectedRows = stmt.executeUpdate(sql);
            if (affectedRows == 0) {
                throw new SQLException("Deleting transaction failed, no rows affected.");
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
    public Transaction findTransaction(int transId) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = MySQLDAOFactory.createConnection();
            stmt = conn.createStatement();

            String sql = "SELECT * FROM " + MySQLConfig.PREFIX + MySQLConfig.TRANSACTIONS_TABLE
                    + " WHERE " + MySQLConfig.TRANSACTIONS_TRANSACTION_ID + " = '" + transId + "'"
                    + " LIMIT 1";
            rs = stmt.executeQuery(sql);
            if(rs.next()) {
                return new Transaction(
                    rs.getInt(MySQLConfig.TRANSACTIONS_TRANSACTION_ID),
                    rs.getInt(MySQLConfig.TRANSACTIONS_CARD_ID),
                    rs.getInt(MySQLConfig.TRANSACTIONS_USER_ID),
                    rs.getString(MySQLConfig.TRANSACTIONS_DESCRIPTION),  
                    new BigDecimal(rs.getString(MySQLConfig.CARDS_AMOUNT)),
                    dateFormat.parse(rs.getString(MySQLConfig.TRANSACTIONS_DATE)),
                    TransactionType.valueOf(rs.getString(MySQLConfig.TRANSACTIONS_TYPE))
                );
            }
        } catch (SQLException | ClassNotFoundException | ParseException ex) {
            
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
    public boolean updateTransaction(int transId, String description) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = MySQLDAOFactory.createConnection();
            stmt = conn.createStatement();

            String sql = "UPDATE " + MySQLConfig.PREFIX + MySQLConfig.TRANSACTIONS_TABLE
                    + " SET " + MySQLConfig.TRANSACTIONS_DESCRIPTION + " = '" + description + "'"
                    + " WHERE " + MySQLConfig.TRANSACTIONS_TRANSACTION_ID + " = '" + transId + "'"
                    + " LIMIT 1";
            int affectedRows = stmt.executeUpdate(sql);
            if (affectedRows == 0) {
                throw new SQLException("Updating transaction failed, no rows affected.");
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
    public List<Transaction> selectTransaction(int cardId) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Transaction> results = new ArrayList<>();
        try {
            conn = MySQLDAOFactory.createConnection();
            stmt = conn.createStatement();

            String sql = "SELECT * FROM " + MySQLConfig.PREFIX + MySQLConfig.TRANSACTIONS_TABLE
                    + " WHERE " + MySQLConfig.TRANSACTIONS_CARD_ID + " = '" + cardId + "'";
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                results.add(
                    new Transaction(
                        rs.getInt(MySQLConfig.TRANSACTIONS_TRANSACTION_ID),
                        rs.getInt(MySQLConfig.TRANSACTIONS_CARD_ID),
                        rs.getInt(MySQLConfig.TRANSACTIONS_USER_ID),
                        rs.getString(MySQLConfig.TRANSACTIONS_DESCRIPTION),  
                        new BigDecimal(rs.getString(MySQLConfig.CARDS_AMOUNT)),
                        dateFormat.parse(rs.getString(MySQLConfig.TRANSACTIONS_DATE)),
                        TransactionType.valueOf(rs.getString(MySQLConfig.TRANSACTIONS_TYPE))
                    )
                );
            }
            if(!results.isEmpty()) {
                return results;
            }
        } catch (SQLException | ClassNotFoundException | ParseException ex) {
            
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



    @Override
    public boolean insertIncome(int userId, int cardId, String description, BigDecimal amount, java.util.Date datetime, String type) {
        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        try {
            conn = MySQLDAOFactory.createConnection();
            conn.setAutoCommit(false);
            
            stmt1 = conn.prepareStatement("INSERT INTO " + MySQLConfig.PREFIX + MySQLConfig.TRANSACTIONS_TABLE + " ("
                        + MySQLConfig.TRANSACTIONS_CARD_ID + ", "
                        + MySQLConfig.TRANSACTIONS_USER_ID + ", "
                        + MySQLConfig.TRANSACTIONS_DESCRIPTION + ", "
                        + MySQLConfig.TRANSACTIONS_AMOUNT + ", "
                        + MySQLConfig.TRANSACTIONS_DATE + ", "
                        + MySQLConfig.TRANSACTIONS_TYPE + " "
                    + ") "
                    + "VALUES ("
                        + "'" + String.valueOf(cardId) + "', "
                        + "'" + String.valueOf(userId) + "', "
                        + "'" + description + "', "
                        + "'" + amount.toPlainString() + "', "
                        + "'" + dateFormat.format(datetime) + "', "
                        + "'" + type + "'"
                    + ")");
            stmt1.executeUpdate();
            
            stmt2 = conn.prepareStatement("UPDATE " + MySQLConfig.PREFIX + MySQLConfig.CARDS_TABLE
                    + " SET " + MySQLConfig.CARDS_AMOUNT + " = " + MySQLConfig.CARDS_AMOUNT + " + " + amount.toPlainString() + ""
                    + " WHERE " + MySQLConfig.CARDS_CARD_ID + " = '" + cardId + "'"
                    + " LIMIT 1");
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
    public boolean insertExpense(int userId, int cardId, String description, BigDecimal amount, java.util.Date datetime, String type) {
        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        try {
            conn = MySQLDAOFactory.createConnection();
            conn.setAutoCommit(false);
            
            stmt1 = conn.prepareStatement("INSERT INTO " + MySQLConfig.PREFIX + MySQLConfig.TRANSACTIONS_TABLE + " ("
                        + MySQLConfig.TRANSACTIONS_CARD_ID + ", "
                        + MySQLConfig.TRANSACTIONS_USER_ID + ", "
                        + MySQLConfig.TRANSACTIONS_DESCRIPTION + ", "
                        + MySQLConfig.TRANSACTIONS_AMOUNT + ", "
                        + MySQLConfig.TRANSACTIONS_DATE + ", "
                        + MySQLConfig.TRANSACTIONS_TYPE + " "
                    + ") "
                    + "VALUES ("
                        + "'" + String.valueOf(cardId) + "', "
                        + "'" + String.valueOf(userId) + "', "
                        + "'" + description + "', "
                        + "'" + amount.toPlainString() + "', "
                        + "'" + dateFormat.format(datetime) + "', "
                        + "'" + type + "'"
                    + ")");
            stmt1.executeUpdate();
            
            stmt2 = conn.prepareStatement("UPDATE " + MySQLConfig.PREFIX + MySQLConfig.CARDS_TABLE
                    + " SET " + MySQLConfig.CARDS_AMOUNT + " = " + MySQLConfig.CARDS_AMOUNT + " - " + amount.toPlainString() + ""
                    + " WHERE " + MySQLConfig.CARDS_CARD_ID + " = '" + cardId + "'"
                    + " LIMIT 1");
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
    public boolean insertMultipleIncome(List<Transaction> transactions) {
        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        try {
            conn = MySQLDAOFactory.createConnection();
            conn.setAutoCommit(false);
            
            for(Transaction t : transactions) {
                stmt1 = conn.prepareStatement("INSERT INTO " + MySQLConfig.PREFIX + MySQLConfig.TRANSACTIONS_TABLE + " ("
                            + MySQLConfig.TRANSACTIONS_CARD_ID + ", "
                            + MySQLConfig.TRANSACTIONS_USER_ID + ", "
                            + MySQLConfig.TRANSACTIONS_DESCRIPTION + ", "
                            + MySQLConfig.TRANSACTIONS_AMOUNT + ", "
                            + MySQLConfig.TRANSACTIONS_DATE + ", "
                            + MySQLConfig.TRANSACTIONS_TYPE + " "
                        + ") "
                        + "VALUES ("
                            + "'" + String.valueOf(t.getCardId()) + "', "
                            + "'" + String.valueOf(t.getId()) + "', "
                            + "'" + t.getDescription() + "', "
                            + "'" + t.getAmount().toPlainString() + "', "
                            + "'" + dateFormat.format(t.getDate()) + "', "
                            + "'" + t.getType() + "'"
                        + ")");
                stmt1.executeUpdate();

                stmt2 = conn.prepareStatement("UPDATE " + MySQLConfig.PREFIX + MySQLConfig.CARDS_TABLE
                        + " SET " + MySQLConfig.CARDS_AMOUNT + " = " + MySQLConfig.CARDS_AMOUNT + " + " + t.getAmount().toPlainString() + ""
                        + " WHERE " + MySQLConfig.CARDS_CARD_ID + " = '" + t.getCardId() + "'"
                        + " LIMIT 1");
                stmt2.executeUpdate();
            }
            
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


    
    
}
