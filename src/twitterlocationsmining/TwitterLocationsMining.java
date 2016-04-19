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
        locationminer.estimator();
        locationminer.printResults();
    }
    public void printResults(){
        long noofuserswithgeoinfo;
        long noofuserswithestimatedlocation;
        
    }
    public void estimator() {
        
        int iterationno;
        
    }
    
    public void removeEstimatedLocations() {
        boolean available = true;
        int count = 100;
        long min_id = 0;
        while(available) {
            User_dbo[] users = UsersTable.select(" ", min_id, count);
            if(users.length==0){
                available = false;
                continue;
            }
            for(User_dbo user: users){
                boolean[] selected = new boolean[User_dbo.nooffields];
                selected[User_dbo.map.get("estimated_geoinfo")] = true;
                selected[User_dbo.map.get("estimated_lon")] = true;
                selected[User_dbo.map.get("estimated_lat")] = true;
                user.values[User_dbo.map.get("estimated_geoinfo")].bool = false;
                user.values[User_dbo.map.get("estimated_lon")].decimal = -1.0;
                user.values[User_dbo.map.get("estimated_lat")].decimal = -1.0;
                UsersTable.update(user, selected, " user_id = "+user.values[User_dbo.map.get("user_id")].lnumber);
            }
        }     
    }
    
    
    
    
    
    public double computeDispersionOfUser(long user_id){
           HashMap<Long,Long> edges = getConnectedUsers(getEdgesForUserId(user_id),user_id);
           double dispersion=0;
           double[] locationofcurrentuser = getPrevf(user_id);
           if(locationofcurrentuser!=null){
           for(Long connecteduser: edges.keySet()){
               Long weight = edges.get(connecteduser);
               double location[] = getPrevf(connecteduser);
               if(location!=null){
                   dispersion = dispersion + weight*calculateDistance(locationofcurrentuser,location);
               }
           }
           }
           return dispersion;
    }
    
    public  double calculateDistance(double[] location1, double[] location2) {
           
            double lat1,  lng1,  lat2,  lng2;
            lng1 = location1[0];
            lng2 = location2[0];
            lat1 = location1[1];
            lat2 = location2[1];
            double earthRadius = 6371000; //meters
            double dLat = Math.toRadians(lat2-lat1);
            double dLng = Math.toRadians(lng2-lng1);
            double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
               Math.sin(dLng/2) * Math.sin(dLng/2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            double dist = (double) (earthRadius * c);

            return dist;
    }
    
    
    public double[] getPrevf(long user_id) {
        double[] prevf;
        prevf = getProfileLocationOfUser(user_id);
        if(prevf==null){
            prevf = getEstimatedLocationOfUser(user_id);
        }
        return prevf;
    }
    
    
    public UserRelation_dbo[] getEdges(User_dbo user){
        return relationhelper.getEdges(user);
    }
    
    public UserRelation_dbo[] getEdgesForUserId(long user_id){
         User_dbo[] userdbos = UsersTable.select(" user_id = "+user_id, 0, 1);
         User_dbo user = userdbos[0];
         return relationhelper.getEdges(user);
    }
    
    public boolean isProfileLocationAvailable(long user_id){
        double[] prevf;
        prevf = getProfileLocationOfUser(user_id);
        if(prevf==null){
            prevf = getEstimatedLocationOfUser(user_id);
        }
        if(prevf==null){
            return false;
        }
        else{
            return true;
        }
    }
    
    
    
    public HashMap<Long,Long> getConnectedUsers(UserRelation_dbo[] relations,long user_id){
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
