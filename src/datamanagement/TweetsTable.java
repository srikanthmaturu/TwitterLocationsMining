/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanagement;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Srikanth
 */
public class TweetsTable {
    
    public static Connection connection;
    
    public static Tweet_dbo[] select(String whereclause, int min_id, int count) {
        Tweet_dbo tweets[] = null;
        String statement = "SELECT * ";
        statement = statement.concat(" from "+Tweet_dbo.tablename);
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
         tweets = new Tweet_dbo[rs.getFetchSize()];
         int index = 0;
         while(rs.next()){
          for(int i=0;i<Tweet_dbo.nooffields;i++){
                  tweets[index].values[i].setValue(rs.getString(i+1));
          }
        }
        }
        catch(Exception e){
           
        }
        return tweets;
    }
    
    public static Tweet_dbo[] select(boolean[] selected, String whereclause, int min_id, int count) {
        Tweet_dbo tweets[] = null;
        String statement = "SELECT ";
        boolean firstentry = true;
        for(int i=0; i<Tweet_dbo.nooffields;i++)
        {
         if(selected[i]){
             if(!firstentry){
                 statement = statement.concat(",");
             }
             statement = statement.concat(Tweet_dbo.fieldnames[i]);
         }
        }
        statement = statement.concat(" from "+Tweet_dbo.tablename);
       
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
         tweets = new Tweet_dbo[rs.getFetchSize()];
         int index = 0;
         
         while(rs.next()){
          for(int i=0;i<Tweet_dbo.nooffields;i++){
              if(selected[i]){
                  tweets[index].values[i].setValue(rs.getString(i+1));
              }
          }
        }
        }
        catch(Exception e){
            
        }
        return tweets;
    }
    
    public static void insert(Tweet_dbo t) {
        String statement = "insert into "+Tweet_dbo.tablename+"(";
        String values = " values(";
        boolean firstentry = true;
        for(int i=0; i<Tweet_dbo.nooffields;i++) {
            if(t.values[i].used){
            if(!firstentry) {
                statement = statement.concat(",");
                values = values.concat(",");
            }
            else{
                firstentry = false;
            }
            statement = statement.concat(""+Tweet_dbo.fieldnames[i]+"");
            values = values.concat(t.values[i].getSQLStringValue());
        }
          if(i==Tweet_dbo.nooffields-1) {
              statement = statement.concat(") ");
             values =  values.concat(");");
        }
        }
        try {
        PreparedStatement ps = connection.prepareStatement(statement+values);
        ps.executeUpdate();
        ps.close();
        }
        catch(Exception e){
            
        }
        
    }
    
    public static void insert(Tweet_dbo t,boolean[] selection) {
        String statement = "insert into "+Tweet_dbo.tablename+"(";
        String values = " values(";
        boolean firstentry = true;
        for(int i=0; i<Tweet_dbo.nooffields;i++) {
            if(selection[i]){
            if(!firstentry) {
                statement = statement.concat(",");
                values = values.concat(",");
            }
            else{
                firstentry = false;
            }
            statement = statement.concat(""+Tweet_dbo.fieldnames[i]+"");
            values = values.concat(t.values[i].getSQLStringValue());
        }
          if(i==Tweet_dbo.nooffields-1) {
              statement = statement.concat(") ");
             values =  values.concat(");");
        }
        }
        try {
        PreparedStatement ps = connection.prepareStatement(statement+values);
        ps.executeUpdate();
        ps.close();
        }
        catch(Exception e){
            
        }   
    }
    
    public static void update(Tweet_dbo t, boolean[] selected, String whereclause) {
        String statement = "UPDATE "+Tweet_dbo.tablename;
        boolean firstentry = true;
        for(int i=0; i<Tweet_dbo.nooffields;i++){
            if(selected[i]){
                if(!firstentry){
                    statement = statement.concat(",");
                }
                else{
                    statement = statement.concat(" set ");
                    firstentry = false;
                }
                statement = statement.concat(Tweet_dbo.fieldnames[i]+"="+t.values[i].getSQLStringValue());
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
    
    
}
