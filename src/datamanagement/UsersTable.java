/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanagement;

import Logger.LogPrinter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Srikanth
 */
public class UsersTable {
    
    public static Connection connection;
    
    
    
    
    public static void insert(User_dbo us) {
        String statement = "insert into "+User_dbo.tablename+"(";
        String values = " values(";
        boolean firstentry = true;
        for(int i=0; i<User_dbo.nooffields;i++) {
            if(us.values[i].used){
            if(!firstentry) {
                statement = statement.concat(",");
                values = values.concat(",");
            }
            else{
                firstentry = false;
            }
            statement = statement.concat(""+User_dbo.fieldnames[i]+"");
            values = values.concat(us.values[i].getSQLStringValue());
        }
          if(i==User_dbo.nooffields-1) {
              statement = statement.concat(") ");
              values = values.concat(");");
        }
        }
        
        try {
         PreparedStatement ps = connection.prepareStatement(statement+values);
        //LogPrinter.printLog(ps.toString());
        ps.executeUpdate();
        ps.close();
        }
        catch(Exception e){
            e.printStackTrace();
            LogPrinter.printLog(statement+values);
        }
        
    }
    
   public  static void insert(User_dbo us,boolean[] selection) {
        String statement = "insert into "+User_dbo.tablename+"(";
        String values = " values(";
        boolean firstentry = true;
        for(int i=0; i<User_dbo.nooffields;i++) {
            if(selection[i]){
            if(!firstentry) {
                statement = statement.concat(",");
                values = values.concat(",");
            }
            else{
                firstentry = false;
            }
            statement = statement.concat(""+User_dbo.fieldnames[i]+"");
            values = values.concat(us.values[i].getSQLStringValue());
        }
          if(i==User_dbo.nooffields-1) {
              statement = statement.concat(") ");
              values = values.concat(");");
        }
        }
        try {
        PreparedStatement ps = connection.prepareStatement(statement+values);
        ps.executeUpdate();
        ps.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }   
    }
    
    
    
    
    
    public static User_dbo[] select(String whereclause, long min_id, int count) {
        ArrayList<User_dbo> users = new ArrayList<User_dbo>();
        String statement = "SELECT * ";
       statement =  statement.concat(" from "+User_dbo.tablename);
        if(whereclause!=null){
       statement =  statement.concat(" where "+whereclause+" AND "); }
        else{
       statement =  statement.concat(" where ");
        }
       statement =  statement.concat(" id >"+ min_id);
       statement =  statement.concat(" ORDER BY id ASC ");
       statement =  statement.concat(" LIMIT "+count);
        ResultSet rs;
        try{
        PreparedStatement ps = connection.prepareStatement(statement);
         rs = ps.executeQuery();
        
         int index = 0;
         while(rs.next()){
             User_dbo user = new User_dbo();
          for(int i=0;i<User_dbo.nooffields;i++){
              String value = rs.getString(i+1);
                  if(value!=null) {
                  user.values[i].setValue(value);
                  
                  }
            }
         
          users.add(user);
          }
        }
        catch(Exception e){
            e.printStackTrace();
        }
       User_dbo usersarray[] = new User_dbo[users.size()];
       for(int i=0; i<users.size();i++){
           usersarray[i] = users.get(i);
       }
       
       return usersarray;
        
    }
    
    public static User_dbo[] select(boolean[] selected, String whereclause, long min_id, int count) {
        ArrayList<User_dbo> users = new ArrayList<User_dbo>();
        String statement = "SELECT ";
        boolean firstentry = true;
        for(int i=0; i<User_dbo.nooffields;i++)
        {
         if(selected[i]){
             if(!firstentry){
                statement =  statement.concat(",");
             }
             statement = statement.concat(User_dbo.fieldnames[i]);
         }
        }
        statement = statement.concat(" from "+User_dbo.tablename);
       
        if(whereclause!=null){
        statement = statement.concat(" where "+whereclause+" AND "); }
        else{
        statement = statement.concat(" where ");
        }
        statement = statement.concat(" id >"+ min_id);
        statement = statement.concat(" ORDER BY id ASC ");
        statement = statement.concat(" LIMIT "+count);
        ResultSet rs;
        try{
        PreparedStatement ps = connection.prepareStatement(statement);
         rs = ps.executeQuery();
        
         int index = 0;
         
         while(rs.next()){
             User_dbo user = new User_dbo();
          for(int i=0;i<User_dbo.nooffields;i++){
              if(selected[i]){
                  String value = rs.getString(i+1);
                  if(value!=null) {
                  user.values[i].setValue(value);
                  }
              }
          }
          users.add(user);
        }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        User_dbo usersarray[] = new User_dbo[users.size()];
       for(int i=0; i<users.size();i++){
           usersarray[i] = users.get(i);
       }
       return usersarray;
    }
    
    public static void delete(long user_id){
        String statement = "DELETE FROM "+User_dbo.tablename+" where user_id = "+user_id+";";
        
        try {
            
            PreparedStatement ps = connection.prepareStatement(statement);
            ps.executeUpdate();
            ps.close();
        }
        catch(Exception e){
          e.printStackTrace();
        }
        
    }
    
    
    public static void update(User_dbo user, boolean[] selected, String whereclause) {
        String statement = "UPDATE "+User_dbo.tablename;
        boolean firstentry = true;
        for(int i=0; i<User_dbo.nooffields;i++){
            if(selected[i]){
                if(!firstentry){
                    statement = statement.concat(",");
                }
                else{
                    statement = statement.concat(" set ");
                    firstentry = false;
                }
                statement = statement.concat(User_dbo.fieldnames[i]+"="+user.values[i].getSQLStringValue());
            }
        }
        if(whereclause!=null){
        statement = statement.concat(" where ");
        statement = statement.concat(whereclause);
        }
        statement = statement.concat(";");
        try {
            PreparedStatement ps = connection.prepareStatement(statement);
            ps.executeUpdate();
            ps.close();
        }
        catch(Exception e){
           
        }
    }
    
    public static void update(User_dbo user, String whereclause) {
        String statement = "UPDATE "+User_dbo.tablename;
        boolean firstentry = true;
        for(int i=0; i<User_dbo.nooffields;i++){
            if(user.values[i].used){
                if(!firstentry){
                    statement = statement.concat(",");
                }
                else{
                    statement = statement.concat(" set ");
                    firstentry = false;
                }
                statement = statement.concat(User_dbo.fieldnames[i]+"="+user.values[i].getSQLStringValue());
            }
        }
        if(whereclause!=null){
        statement = statement.concat(" where ");
        statement = statement.concat(whereclause);
        }
        statement = statement.concat(";");
        try {
            PreparedStatement ps = connection.prepareStatement(statement);
            ps.executeUpdate();
            ps.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void printTable(){
        
    }
    
}
