/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterlocationsmining;

import DataCollections.*;
import datamanagement.UserRelation_dbo;
import datamanagement.User_dbo;
import datamanagement.UsersTable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Srikanth
 */
public class TwitterLocationsMining {

    /**
     * @param args the command line arguments
     */
    
    public UserRelationsHelper userrelationhelper;
    public UserHelper userhelper;
    public UserRelationsHelper relationhelper;
    
    public TwitterLocationsMining() {
        userrelationhelper = new UserRelationsHelper();
        userhelper = new UserHelper();
        relationhelper = new UserRelationsHelper();
    }
    public static void main(String[] args) {
        TwitterLocationsMining locationminer = new TwitterLocationsMining();  
    }
    
    void estimator() {
        
    }
    
    
    public UserRelation_dbo[] getEdges(User_dbo user){
        return relationhelper.getEdges(user);
    }
    
    public HashMap<Long,Long> getConncectedUsers(UserRelation_dbo[] relations,long user_id){
        HashMap<Long,Long> connectedusers = new HashMap<Long,Long>();
        for(int i=0; i<relations.length; i++){
            Long total_tweets = relations[i].values[UserRelation_dbo.map.get("fu_tweets")].lnumber + relations[i].values[UserRelation_dbo.map.get("su_tweets")].lnumber;
            if(relations[i].values[UserRelation_dbo.map.get("fu_id")].lnumber==user_id){
                connectedusers.put(relations[i].values[UserRelation_dbo.map.get("su_id")].lnumber,total_tweets);
            }
            else{
                connectedusers.put(relations[i].values[UserRelation_dbo.map.get("fu_id")].lnumber,total_tweets);
            }
        }
        return connectedusers;
    }
    
    public double[] getProfileLocationOfUser(long user_id){
        User_dbo[] users = UsersTable.select(" user_id = "+user_id, 0, 1);
        double geocoor[] = null;
        if(users.length>0){
            geocoor = new double[2];
            if(users[0].values[User_dbo.map.get("probased_geoinfo")].bool){
           geocoor[0] = users[0].values[User_dbo.map.get("probased_lon")].decimal;
           geocoor[1] = users[0].values[User_dbo.map.get("probased_lat")].decimal;
           }
        }
        return geocoor;
    }
    
    
    public double[] getEstimatedLocationOfUser(long user_id){
        User_dbo[] users = UsersTable.select(" user_id = "+user_id, 0, 1);
        double geocoor[] = null;
        if(users.length>0){
            geocoor = new double[2];
            if(users[0].values[User_dbo.map.get("estimated_geoinfo")].bool){
           geocoor[0] = users[0].values[User_dbo.map.get("estimated_lon")].decimal;
           geocoor[1] = users[0].values[User_dbo.map.get("estimated_lat")].decimal;
           }
        }
        return geocoor;
    }
    
      public void setEstimatedLocationOfUser(long user_id, double[] geocoor){
        User_dbo[] users = UsersTable.select(" user_id = "+user_id, 0, 1);
        boolean selected[] = new boolean[User_dbo.nooffields];
        selected[User_dbo.map.get("estimated_geoinfo")] = true;
        selected[User_dbo.map.get("estimated_lon")] = true;
        selected[User_dbo.map.get("estimated_lat")] = true;
        if(users.length>0){
            User_dbo user = users[0];
            users[0].values[User_dbo.map.get("estimated_geoinfo")].setValue("true");
            users[0].values[User_dbo.map.get("estimated_lon")].setValue(String.valueOf(geocoor[0]));
            users[0].values[User_dbo.map.get("estimated_lat")].setValue(String.valueOf(geocoor[1]));
            UsersTable.update(user, selected, " user_id = "+user.values[User_dbo.map.get("user_id")].lnumber);
        }
        
    }
      
    
    
}
