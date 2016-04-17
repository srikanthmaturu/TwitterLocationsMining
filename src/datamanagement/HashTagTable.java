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
public class HashTagTable {
    
    public static Connection connection;
    
    
    
    public static Hashtag_dbo[] select(String whereclause, long min_id, int count) {
        Hashtag_dbo htags[] = null;
        String statement = "SELECT * ";
        statement =statement.concat(" from "+Hashtag_dbo.tablename);
        if(whereclause!=null){
        statement =statement.concat(" where "+whereclause+" AND "); }
        else{
        statement =statement.concat(" where ");
        }
        statement =statement.concat(" id >"+ min_id);
        statement =statement.concat(" ORDER BY id ASC ");
        statement =statement.concat(" LIMIT "+count);
        ResultSet rs;
        try{
        PreparedStatement ps = connection.prepareStatement(statement);
         rs = ps.executeQuery();
         htags = new Hashtag_dbo[rs.getFetchSize()];
         int index = 0;
         while(rs.next()){
          for(int i=0;i<Hashtag_dbo.nooffields;i++){
                  String value = rs.getString(i+1);
                  if(value!=null) {
                  htags[index].values[i].setValue(value);
                  }
          }
        }
        }
        catch(Exception e){
            
        }
        return htags;
    }
    
    public static Hashtag_dbo[] select(boolean[] selected, String whereclause, long min_id, int count) {
        Hashtag_dbo htags[] = null;
        String statement = "SELECT ";
        boolean firstentry = true;
        for(int i=0; i<Hashtag_dbo.nooffields;i++)
        {
         if(selected[i]){
             if(!firstentry){
                 statement =statement.concat(",");
             }
             statement =statement.concat(Hashtag_dbo.fieldnames[i]);
         }
        }
        statement = statement.concat(" from "+Hashtag_dbo.tablename);
       
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
         htags = new Hashtag_dbo[rs.getFetchSize()];
         int index = 0;
         
         while(rs.next()){
          for(int i=0;i<Hashtag_dbo.nooffields;i++){
              if(selected[i]){
                  String value = rs.getString(i+1);
                  if(value!=null)
                  htags[index].values[i].setValue(value);
              }
          }
        }
        }
        catch(Exception e){
            
        }
        return htags;
    }
    
    public static void insert(Hashtag_dbo htag) {
        
        String statement = "insert into "+Hashtag_dbo.tablename+"(";
        String values = " values(";
        boolean firstentry = true;
        for(int i=0; i<Hashtag_dbo.nooffields;i++) {
            if(htag.values[i].used){
            if(!firstentry) {
                statement = statement.concat(",");
                values = values.concat(",");
            }
            else{
                firstentry = false;
            }
            statement = statement.concat(""+Hashtag_dbo.fieldnames[i]+"");
            values = values.concat(htag.values[i].getSQLStringValue());
        }
          if(i==Hashtag_dbo.nooffields-1) {
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
            
        } 
    }
    
    public static void insert(Hashtag_dbo htag,boolean[] selection) {
        String statement = "insert into "+Hashtag_dbo.tablename+"(";
        String values = " values(";
        boolean firstentry = true;
        for(int i=0; i<Hashtag_dbo.nooffields;i++) {
            if(selection[i]){
            if(!firstentry) {
                statement = statement.concat(",");
                values = values.concat(",");
            }
            else{
                firstentry = false;
            }
            statement = statement.concat(""+Hashtag_dbo.fieldnames[i]+"");
            values = values.concat(htag.values[i].getSQLStringValue());
        }
          if(i==Hashtag_dbo.nooffields-1) {
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
            
        }   
    }
    
    public static void update(Hashtag_dbo htag, boolean[] selected, String whereclause) {
        String statement = "UPDATE "+Hashtag_dbo.tablename;
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
                statement = statement.concat(Hashtag_dbo.fieldnames[i]+"="+htag.values[i].getSQLStringValue());
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
