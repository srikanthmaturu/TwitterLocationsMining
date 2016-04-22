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
public class LocationsTable {
    
    public int noofrows;
    
    public static Connection connection;
    
    
    
    
    
    public static Location_dbo[] select(String whereclause, int min_id, int count) {
        ArrayList<Location_dbo> locs = new ArrayList<Location_dbo>();
        String statement = "SELECT * ";
        statement = statement.concat(" from "+Location_dbo.tablename);
        if(whereclause!=null){
        statement =  statement.concat(" where "+whereclause+" AND "); }
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
              Location_dbo loc = new Location_dbo();
          for(int i=0;i<Location_dbo.nooffields;i++){
                  String value = rs.getString(i+1);
                  if(value!=null) {
                  loc.values[i].setValue(value);
                  }
          }
          locs.add(loc);
        }
        }
        catch(Exception e){
            
        }
       Location_dbo locations[] = new Location_dbo[locs.size()];
        for(int i=0; i<locs.size();i++){
            locations[i] = locs.get(i);
        }
        return locations;
    }
    
    
    
    public static Location_dbo[] select(boolean[] selected, String whereclause, int min_id, int count) {
        ArrayList<Location_dbo> locs = new ArrayList<Location_dbo>();
        String statement = "SELECT ";
        boolean firstentry = true;
        for(int i=0; i<Location_dbo.nooffields;i++)
        {
         if(selected[i]){
             if(!firstentry){
                 statement = statement.concat(",");
             }
             statement = statement.concat(Location_dbo.fieldnames[i]);
         }
        }
        statement = statement.concat(" from "+Location_dbo.tablename);
       
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
             Location_dbo loc = new Location_dbo();
          for(int i=0;i<Location_dbo.nooffields;i++){
              if(selected[i]){
                  String value = rs.getString(i+1);
                  if(value!=null) {
                  loc.values[i].setValue(value);
                  }
              }
          }
          locs.add(loc);
        }
        }
        catch(Exception e){
           e.printStackTrace();  
        }
        Location_dbo locations[] = new Location_dbo[locs.size()];
        for(int i=0; i<locs.size();i++){
            locations[i] = locs.get(i);
        }
        return locations;
    }
    
    
    public static void insert(Location_dbo loc) {
        String statement = "insert into "+Location_dbo.tablename+"(";
        String values = " values(";
        boolean firstentry = true;
        for(int i=0; i<Location_dbo.nooffields;i++) {
            if(loc.values[i].used){
            if(!firstentry) {
                statement = statement.concat(",");
                values =values.concat(",");
            }
            else{
                firstentry = false;
            }
            statement = statement.concat(""+Location_dbo.fieldnames[i]+"");
            values =values.concat(loc.values[i].getSQLStringValue());
        }
          if(i==Location_dbo.nooffields-1) {
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
    
    public static void insert(Location_dbo loc,boolean[] selection) {
        String statement = "insert into "+Location_dbo.tablename+"(";
        String values = " values(";
        boolean firstentry = true;
        for(int i=0; i<Location_dbo.nooffields;i++) {
            if(selection[i]){
            if(!firstentry) {
                statement = statement.concat(",");
                values =values.concat(",");
            }
            else{
                firstentry = false;
            }
            statement = statement.concat(""+Location_dbo.fieldnames[i]+"");
            values =values.concat(loc.values[i].getSQLStringValue());
        }
          if(i==Location_dbo.nooffields-1) {
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
    
    public static void update(Location_dbo loc, boolean[] selected, String whereclause) {
        String statement = "UPDATE "+Location_dbo.tablename;
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
                statement = statement.concat(Location_dbo.fieldnames[i]+"="+loc.values[i].getSQLStringValue());
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
