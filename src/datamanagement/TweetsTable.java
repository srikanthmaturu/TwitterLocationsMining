/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanagement;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Srikanth
 */
public class TweetsTable {
    
    public static Connection connection;
    
    public static Tweet_dbo[] select(String whereclause, long min_id, int count) {
        ArrayList<Tweet_dbo> tweets = new ArrayList<Tweet_dbo>();
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
         
         int index = 0;
         while(rs.next()){
             Tweet_dbo tweet = new Tweet_dbo();
          for(int i=0;i<Tweet_dbo.nooffields;i++){
              String value = rs.getString(i+1);
                  if(value!=null) {
                  tweet.values[i].setValue(value);
                  }
          }
          tweets.add(tweet);
        }
        }
        catch(Exception e){
            e.printStackTrace();
        }
       Tweet_dbo[] tweetsarray = new Tweet_dbo[tweets.size()];
        for(int i=0; i<tweets.size();i++){
            tweetsarray[i] = tweets.get(i);
        }
        return tweetsarray;
    }
    
    public static Tweet_dbo[] select(boolean[] selected, String whereclause, long min_id, int count) {
        ArrayList<Tweet_dbo> tweets = new ArrayList<Tweet_dbo>();
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
        
         int index = 0;
         
         while(rs.next()){
             Tweet_dbo tweet = new Tweet_dbo();
          for(int i=0;i<Tweet_dbo.nooffields;i++){
              if(selected[i]){
                  String value = rs.getString(i+1);
                  if(value!=null) {
                  tweet.values[i].setValue(value);
                  }
              }
          }
          tweets.add(tweet);
        }
        }
        catch(Exception e){
             e.printStackTrace();
        }
        Tweet_dbo[] tweetsarray = new Tweet_dbo[tweets.size()];
        for(int i=0; i<tweets.size();i++){
            tweetsarray[i] = tweets.get(i);
        }
        return tweetsarray;
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
             e.printStackTrace();
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
             e.printStackTrace();
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
             e.printStackTrace();
        }
    }
    
    
}
