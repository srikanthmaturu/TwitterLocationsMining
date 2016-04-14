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

/**
 *
 * @author Srikanth
 */
public class UsersRelationsTable {
    public static Connection connection;
    
    public static UserRelation_dbo[] select(String whereclause, int min_id, int count) {
        UserRelation_dbo urelations[] = null;
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
         urelations = new UserRelation_dbo[rs.getFetchSize()];
         int index = 0;
         while(rs.next()){
          for(int i=0;i<UserRelation_dbo.nooffields;i++){
                  urelations[index].values[i].setValue(rs.getString(i+1));
          }
        }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return urelations;
    }
    
    public static UserRelation_dbo[] select(boolean[] selected, String whereclause, int min_id, int count) {
        UserRelation_dbo urelations[] = null;
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
         urelations = new UserRelation_dbo[rs.getFetchSize()];
         int index = 0;
         
         while(rs.next()){
          for(int i=0;i<UserRelation_dbo.nooffields;i++){
              if(selected[i]){
                  urelations[index].values[i].setValue(rs.getString(i+1));
              }
          }
        }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return urelations;
    }
    
    public static void insert(UserRelation_dbo r) {
        String statement = "Insert into "+UserRelation_dbo.tablename;
        String values = "values(";
        boolean firstentry = true;
        for(int i=0;i<UserRelation_dbo.nooffields;i++){
            if(!firstentry){
               statement =  statement.concat(",");
            }
            else{
                firstentry = false;
            }
            statement = statement.concat(r.values[i].getSQLStringValue());
        
        }
        statement = statement.concat(");");
        
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
           
        }
    }
    
    
    
}
