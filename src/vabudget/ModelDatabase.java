/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vabudget;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Adrian Simionescu
 */

public class ModelDatabase implements Model {
    
    final private String JDBC_DRIVER;
    final private String DB_URL;
    final private String USER;
    final private String PASS;
    final private String PREFIX;
    
    Connection conn = null;
    Statement stmt = null;
    
    public ModelDatabase(String driver, String url, String username, String password, String prefix) {
        JDBC_DRIVER = driver;  
        DB_URL = url;
        USER = username;
        PASS = password;
        PREFIX = prefix;
    }
    
    @Override
    public boolean connect() {
        try {
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);
            
            //STEP 3: Open a connection
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ModelDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    @Override
    public boolean create() {
        try {
            stmt = conn.createStatement();
            stmt.addBatch("CREATE TABLE IF NOT EXISTS " + PREFIX + Model.CARDS_TABLE +
                   "(" + Model.CARDS_CARD_ID + " INTEGER not NULL AUTO_INCREMENT, " +
                   " " + Model.CARDS_OWNER_ID + " INTEGER not NULL, " + 
                   " " + Model.CARDS_LABEL + " VARCHAR(255), " + 
                   " " + Model.CARDS_AMOUNT + " DECIMAL(8,2), " + 
                   " PRIMARY KEY ( " + Model.CARDS_CARD_ID + " ))");
            stmt.addBatch("CREATE TABLE IF NOT EXISTS " + PREFIX + Model.TRANSACTIONS_TABLE +
                   "(" + Model.TRANSACTIONS_TRANSACTION_ID + " INTEGER not NULL AUTO_INCREMENT, " +
                   " " + Model.TRANSACTIONS_CARD_ID + " INTEGER not NULL, " + 
                   " " + Model.TRANSACTIONS_USER_ID + " INTEGER not NULL, " + 
                   " " + Model.TRANSACTIONS_DESCRIPTION + " VARCHAR(255), " + 
                   " " + Model.TRANSACTIONS_TYPE + " VARCHAR(50), " + 
                   " " + Model.TRANSACTIONS_DATE + " DATETIME, " + 
                   " " + Model.TRANSACTIONS_AMOUNT + " DECIMAL(8,2), " + 
                   " PRIMARY KEY ( " + Model.TRANSACTIONS_TRANSACTION_ID + " ))");
            stmt.addBatch("CREATE TABLE IF NOT EXISTS " + PREFIX + Model.SHARED_TABLE +
                   "(" + Model.SHARED_CARD_ID + " INTEGER not NULL, " +
                   " " + Model.SHARED_PERSON_ID + " INTEGER not NULL, " + 
                   " " + Model.SHARED_STATUS + " ENUM('-1','0','1'), " + 
                   " PRIMARY KEY ( " + Model.SHARED_CARD_ID + ", " + Model.SHARED_PERSON_ID + " ))");
            stmt.addBatch("CREATE TABLE IF NOT EXISTS " + PREFIX + Model.DISTRIBUTIONS_TABLE +
                   "(" + Model.DISTRIBUTIONS_DISTRIB_ID + " INTEGER not NULL AUTO_INCREMENT, " +
                   " " + Model.DISTRIBUTIONS_OWNER_ID + " INTEGER not NULL, " + 
                   " PRIMARY KEY ( " + Model.DISTRIBUTIONS_DISTRIB_ID + " ))");
            stmt.addBatch("CREATE TABLE IF NOT EXISTS " + PREFIX + Model.DISTRIBUTIONS_RATIO_TABLE +
                   "(" + Model.DISTRIBUTIONS_RATIO_DISTRIB_ID + " INTEGER not NULL, " +
                   " " + Model.DISTRIBUTIONS_RATIO_CARD_ID + " INTEGER not NULL, " + 
                   " " + Model.DISTRIBUTIONS_RATIO_PERCENT + " INTEGER not NULL " + 
                   ")");
            stmt.addBatch("CREATE TABLE IF NOT EXISTS " + PREFIX + Model.DISTRIBUTIONS_SHARED_TABLE +
                   "(" + Model.DISTRIBUTIONS_SHARED_DISTRIB_ID + " INTEGER not NULL, " +
                   " " + Model.DISTRIBUTIONS_SHARED_PERSON_ID + " INTEGER not NULL, " + 
                   " " + Model.DISTRIBUTIONS_SHARED_STATUS + " ENUM('-1','0','1'), " + 
                   " PRIMARY KEY ( " + Model.DISTRIBUTIONS_SHARED_DISTRIB_ID + ", " + Model.DISTRIBUTIONS_SHARED_PERSON_ID + " ))");
            stmt.executeBatch();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModelDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public void close() {
        try {
            if(stmt!=null) {
                conn.close();
            }
        }catch(SQLException se){
        }
    }

    @Override
    public boolean insert(String table, Map<String, String> sets) {
        try {
            stmt = conn.createStatement();
            String sql = "INSERT INTO " + PREFIX + table + " (" + String.join(", ", sets.keySet()) + ") "
                    + "VALUES (" + sets.values().stream()
                                    .map((s) -> "'" + s + "'")
                                    .collect(Collectors.joining(", ")) + ")";
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModelDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    @Override
    public int insertWithReturn(String table, Map<String, String> sets) {
        try {
            stmt = conn.createStatement();
            String sql = "INSERT INTO " + PREFIX + table + " (" + String.join(", ", sets.keySet()) + ") "
                       + "VALUES (" + sets.values().stream().map((s) -> "'" + s + "'").collect(Collectors.joining(", ")) + ")";
            int affectedRows = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModelDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public HashMap<String,Object> selectOne(String table, Map<String, String> where) throws SQLException {
        stmt = conn.createStatement();
        String condition = "";
        if(where.size()>0) {
            for(String key : where.keySet()) {
                if (!condition.isEmpty()) {
                    condition+=" AND ";
                }
                condition+=" " + key + " = '" + where.get(key) + "'";
            }
            condition = " WHERE " + condition;
        }
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + PREFIX + table + " " + condition + " LIMIT 1");
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        rs.next();
        HashMap<String,Object> row = new HashMap<String, Object>(columns);
        for(int i=1; i<=columns; ++i) {
            row.put(md.getColumnName(i),rs.getObject(i));
        }

        return row;
    }
    
    @Override
    public List<HashMap<String,Object>> select(String table, Map<String, String> where) throws SQLException {
        stmt = conn.createStatement();
        String condition = createWhereClause(where);
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + PREFIX + table + " " + condition);
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<HashMap<String,Object>> results = new ArrayList<HashMap<String,Object>>();
        while(rs.next()){
            HashMap<String,Object> row = new HashMap<String, Object>(columns);
            for(int i=1; i<=columns; ++i) {
                row.put(md.getColumnName(i),rs.getObject(i));
            }
            results.add(row);
        }

        return results;
    }

    @Override
    public boolean delete(String table, Map<String, String> where) {
        try {
            stmt = conn.createStatement();
            String condition = createWhereClause(where);
            String sql = "DELETE FROM " + PREFIX + table + condition;
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModelDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean update(String table, Map<String, String> where, Map<String, String> sets, Map<String, String> setsExp) throws SQLException {
        stmt = conn.createStatement();
        String whereClause = createWhereClause(where);
        String setClause = createSetClause(sets);
        String setExpClause = createSetExpClause(setsExp);
        
        String sql = "UPDATE " + PREFIX + table + " SET ";
        sql += setClause;
        if (!setClause.isEmpty() && !setExpClause.isEmpty()) {
            sql += ", ";
        }
        sql += setExpClause;
        sql += whereClause;
        stmt.executeUpdate(sql);
        
        return true;
    }

    private String createWhereClause(Map<String, String> conditions) {
        String where = "";
        if(conditions.size()>0) {
            for(String key : conditions.keySet()) {
                if (!where.isEmpty()) {
                    where+=" AND ";
                }
                where+=" " + key + " = '" + conditions.get(key) + "'";
            }
            where = " WHERE " + where;
        }
        return where;
    }
    
    private String createSetClause(Map<String, String> conditions) {
        String sets = "";
        if(conditions.size()>0) {
            for(String key : conditions.keySet()) {
                if (!sets.isEmpty()) {
                    sets+=", ";
                }
                sets+=" " + key + " = '" + conditions.get(key) + "'";
            }
        }
        return sets;
    }
    
    private String createSetExpClause(Map<String, String> conditions) {
        String sets = "";
        if(conditions.size()>0) {
            for(String key : conditions.keySet()) {
                if (!sets.isEmpty()) {
                    sets+=", ";
                }
                sets+=" " + key + " = " + conditions.get(key) + "";
            }
        }
        return sets;
    }
}
