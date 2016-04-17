/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCollections;

import DataAccess.TwitterResources;
import datamanagement.User_dbo;
import datamanagement.UsersTable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.User;
import twitter4j.api.FriendsFollowersResources;

/**
 *
 * @author Srikanth
 */
public class FriendsnFollowersCollection {
    Twitter twitter ;
    FriendsFollowersResources ffresources;
    UserHelper userhelper;
    
    public FriendsnFollowersCollection() {
        twitter =  TwitterResources.getTwitterSingleton();
        ffresources = twitter.friendsFollowers();
        userhelper = new UserHelper();
        
    }
    public ArrayList<User_dbo> collectfriendsfollowersofuser(User_dbo cuserdbo){
        long cursor = 0;
        PagableResponseList<User> list;
        User user;
        User_dbo userdbo;
        ArrayList<User_dbo> userdbolist = new ArrayList<>();
        boolean available = true;
        while(available){
        
        try{
        list = ffresources.getFriendsList(cuserdbo.values[User_dbo.map.get("user_id")].lnumber, cursor);
        ListIterator li = list.listIterator();
        while(li.hasNext()){
            user = (User)li.next();
            userdbo = userhelper.convertUserToUser_dbo(user);
            userdbolist.add(userdbo);
        }
        if(list.hasNext()){
        cursor = list.getNextCursor();
        }
        else{
            available = false;
        }
        }
        catch(Exception e){
            
        }
        }
        available = false;
        cursor = 0;
        while(available){
        
        try{
        list = ffresources.getFollowersList(cuserdbo.values[User_dbo.map.get("user_id")].lnumber, cursor);
        ListIterator li = list.listIterator();
        while(li.hasNext()){
            user = (User)li.next();
            userdbo = userhelper.convertUserToUser_dbo(user);
            userdbolist.add(userdbo);
        }
        if(list.hasNext()){
        cursor = list.getNextCursor();
        }
        else{
            available = false;
        }
        }
        catch(Exception e){
            
        }
        }
        
        return userdbolist;
    }
        
    
    
    public void insertFriendsFollowers(ArrayList<User_dbo> userdbolist){
        if(!userdbolist.isEmpty()){
            ListIterator li = userdbolist.listIterator();
            while(li.hasNext()){
                User_dbo userdbo = (User_dbo)li.next();
                User_dbo[] users = UsersTable.select(" user_id = "+userdbo.values[User_dbo.map.get("user_id")].lnumber, 0, 2);
                if(users.length==0){
                    UsersTable.insert(userdbo);
                }
                else{
                    if(!users[0].values[User_dbo.map.get("udetails_processed")].bool){
                        UsersTable.delete(userdbo.values[User_dbo.map.get("user_id")].lnumber);
                        UsersTable.insert(userdbo);
                    }
                }
            }
            
        }
    }
     
    
    public void updateDatabasewithFriends_followers(){
        User_dbo[] users;
        int count = 100;
        long min_id = 0;
        boolean available = true;
        while(available) {
        users = UsersTable.select("fri_fol_processed = false ", min_id, count);
        if(users.length == 0) {
            available = false;
            continue;
        }
        min_id = users[users.length-1].values[User_dbo.map.get("id")].lnumber;
        for(User_dbo user: users) {
            insertFriendsFollowers(collectfriendsfollowersofuser(user));   
            user.values[User_dbo.map.get("fri_fol_processed")].bool = true;
            long userid = user.values[User_dbo.map.get("user_id")].lnumber;
            UsersTable.update(user, " user_id =  "+userid);
        }
        
        }
    }
}
