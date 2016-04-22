/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanagement;

import static datamanagement.TweetsTable.connection;
import static datamanagement.UsersTable.connection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Srikanth
 */
public class UsersRelationsTable {
    public static Connection connection;
    
    public static UserRelation_dbo[] select(String whereclause, long min_id, int count) {
        ArrayList<UserRelation_dbo> urelations = new ArrayList<UserRelation_dbo>();
        String statement = "SELECT * ";
        statement = statement.concat(" from "+UserRelation_dbo.tablename);
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
           UserRelation_dbo relation = new UserRelation_dbo();
          for(int i=0;i<UserRelation_dbo.nooffields;i++){
                  String value = rs.getString(i+1);
                  if(value!=null) {
                  relation.values[i].setValue(value);
                  }
          }
          urelations.add(relation);
        }
        }
        catch(Exception e){
            e.printStackTrace();
        }
         UserRelation_dbo[] relationsarray = new UserRelation_dbo[urelations.size()];
        for(int i=0; i<urelations.size();i++){
            relationsarray[i] = urelations.get(i);
        }
        return relationsarray;
    }
    
    public static UserRelation_dbo[] select(boolean[] selected, String whereclause, long min_id, int count) {
        ArrayList<UserRelation_dbo> urelations = new ArrayList<UserRelation_dbo>();
        String statement = "SELECT ";
        boolean firstentry = true;
        for(int i=0; i<UserRelation_dbo.nooffields;i++)
        {
         if(selected[i]){
             if(!firstentry){
                statement =  statement.concat(",");
             }
             statement = statement.concat(UserRelation_dbo.fieldnames[i]);
         }
        }
        statement = statement.concat(" from "+UserRelation_dbo.tablename);
       
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
             UserRelation_dbo relation = new UserRelation_dbo();
          for(int i=0;i<UserRelation_dbo.nooffields;i++){
              if(selected[i]){
                  String value = rs.getString(i+1);
                  if(value!=null) {
                  relation.values[i].setValue(value);
                  }
              }
          }
          urelations.add(relation);
        }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        UserRelation_dbo[] relationsarray = new UserRelation_dbo[urelations.size()];
        for(int i=0; i<urelations.size();i++){
            relationsarray[i] = urelations.get(i);
        }
        return relationsarray;
    }
    
    public static void insert(UserRelation_dbo r) {
         String statement = "insert into "+UserRelation_dbo.tablename+"(";
        String values = " values(";
        boolean firstentry = true;
        for(int i=0; i<UserRelation_dbo.nooffields;i++) {
            if(r.values[i].used){
            if(!firstentry) {
                statement = statement.concat(",");
               values =  values.concat(",");
            }
            else{
                firstentry = false;
            }
            statement = statement.concat(""+UserRelation_dbo.fieldnames[i]+"");
            values = values.concat(r.values[i].getSQLStringValue());
        }
          if(i==UserRelation_dbo.nooffields-1) {
              statement = statement.concat(") ");
              values = values.concat(");");
        }
        }
        try {
        PreparedStatement ps = connection.prepareStatement(statement+values);
        //Logger.LogPrinter.printLog(ps.toString());
        ps.executeUpdate();
        ps.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }  
        
    }
    
    public static void insert(UserRelation_dbo r,boolean[] selection) {
        String statement = "insert into "+UserRelation_dbo.tablename+"(";
        String values = " values(";
        boolean firstentry = true;
        for(int i=0; i<UserRelation_dbo.nooffields;i++) {
            if(selection[i]){
            if(!firstentry) {
                statement = statement.concat(",");
               values =  values.concat(",");
            }
            else{
                firstentry = false;
            }
            statement = statement.concat(""+UserRelation_dbo.fieldnames[i]+"");
            values = values.concat(r.values[i].getSQLStringValue());
        }
          if(i==UserRelation_dbo.nooffields-1) {
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
    
    public static void update(UserRelation_dbo r, boolean[] selected, String whereclause) {
        String statement = "UPDATE "+UserRelation_dbo.tablename;
        boolean firstentry = true;
        for(int i=0; i<UserRelation_dbo.nooffields;i++){
            if(selected[i]){
                if(!firstentry){
                    statement = statement.concat(",");
                }
                else{
                    statement = statement.concat(" set ");
                    firstentry = false;
                }
                statement = statement.concat(UserRelation_dbo.fieldnames[i]+"="+r.values[i].getSQLStringValue());
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
