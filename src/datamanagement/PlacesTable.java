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
public class PlacesTable {
    public static Connection connection;
    
    
    public static Place_dbo[] select(String whereclause, int min_id, int count) {
        ArrayList<Place_dbo> places = new ArrayList<Place_dbo>();
        String statement = "SELECT * ";
        statement = statement.concat(" from "+Place_dbo.tablename);
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
             Place_dbo place = new Place_dbo();
          for(int i=0;i<Place_dbo.nooffields;i++){
              String value = rs.getString(i+1);
                  if(value!=null) {
                  place.values[i].setValue(value);
                  }
          }
          places.add(place);
        }
        }
        catch(Exception e){
           e.printStackTrace();
        }
        Place_dbo placesarray[] = new Place_dbo[places.size()];
        for(int i=0; i<places.size();i++){
            placesarray[i] = places.get(i);
        }
        return placesarray;
    }
    
    public static Place_dbo[] select(boolean[] selected, String whereclause, int min_id, int count) {
         ArrayList<Place_dbo> places = new ArrayList<Place_dbo>();
        String statement = "SELECT ";
        boolean firstentry = true;
        for(int i=0; i<Place_dbo.nooffields;i++)
        {
         if(selected[i]){
             if(!firstentry){
                 statement = statement.concat(",");
             }
             statement = statement.concat(Place_dbo.fieldnames[i]);
         }
        }
        statement = statement.concat(" from "+Place_dbo.tablename);
       
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
              Place_dbo place = new Place_dbo();
          for(int i=0;i<Place_dbo.nooffields;i++){
              if(selected[i]){
                  String value = rs.getString(i+1);
                  if(value!=null) {
                  place.values[i].setValue(value);
                  }
              }
          }
          places.add(place);
        }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Place_dbo placesarray[] = new Place_dbo[places.size()];
        for(int i=0; i<places.size();i++){
            placesarray[i] = places.get(i);
        }
        return placesarray;
    }
    
    public static void insert(Place_dbo place) {
        String statement = "insert into "+Place_dbo.tablename+"(";
        String values = " values(";
        boolean firstentry = true;
        for(int i=0; i<Place_dbo.nooffields;i++) {
            if(place.values[i].used){
            if(!firstentry) {
                statement = statement.concat(",");
                values = values.concat(",");
            }
            else{
                firstentry = false;
            }
            statement = statement.concat(""+Place_dbo.fieldnames[i]+"");
           values =  values.concat(place.values[i].getSQLStringValue());
        }
          if(i==Place_dbo.nooffields-1) {
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
    
    public static void insert(Place_dbo place,boolean[] selection) {
        String statement = "insert into "+Place_dbo.tablename+"(";
        String values = " values(";
        boolean firstentry = true;
        for(int i=0; i<Place_dbo.nooffields;i++) {
            if(selection[i]){
            if(!firstentry) {
                statement = statement.concat(",");
                values = values.concat(",");
            }
            else{
                firstentry = false;
            }
            statement = statement.concat(""+Place_dbo.fieldnames[i]+"");
           values =  values.concat(place.values[i].getSQLStringValue());
        }
          if(i==Place_dbo.nooffields-1) {
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
    
    public static void update(Place_dbo place, boolean[] selected, String whereclause) {
        String statement = "UPDATE "+Place_dbo.tablename;
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
                statement = statement.concat(Place_dbo.fieldnames[i]+"="+place.values[i].getSQLStringValue());
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
