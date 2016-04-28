/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCollections;

import Logger.LogPrinter;
import datamanagement.Tweet_dbo;
import datamanagement.UserRelation_dbo;
import datamanagement.User_dbo;
import datamanagement.UsersRelationsTable;
import datamanagement.UsersTable;
import twitter4j.Status;
import twitter4j.UserMentionEntity;

/**
 *
 * @author Srikanth
 */
public class Users_EdgesCollectionFromTweets {
    
    public UserRelationsHelper relationhelper ;
    public UserHelper userhelper;
   
    public Users_EdgesCollectionFromTweets () {
        relationhelper = new UserRelationsHelper();
        userhelper = new UserHelper();
    }
    
    public void extract_InsertUsers_EdgesFromTweet(Status s){
        User_dbo tweetuser = userhelper.convertUserToUser_dbo(s.getUser());
        
        UserMentionEntity[] mentions = s.getUserMentionEntities();
        Long[] mentionedusers = new Long[mentions.length];
        User_dbo[] mentionedusersdbos = new User_dbo[mentions.length];
        
        int count = 0;
        for(UserMentionEntity u: mentions) {
            mentionedusers[count]= u.getId();
            mentionedusersdbos[count] = userhelper.convertMentionedUserToUser_dbo(u);
            count++;
        }
        User_dbo[] existinguser =  UsersTable.select(" user_id = "+tweetuser.values[User_dbo.map.get("user_id")].lnumber+"", 0, 2);
        tweetuser.values[User_dbo.map.get("udetails_processed")].bool = true;
        if(existinguser.length==0){
        UsersTable.insert(tweetuser);
        }
        else{
            if(existinguser.length==1){
                if(existinguser[0].values[User_dbo.map.get("udetails_processed")].bool == false){
                UsersTable.delete(existinguser[0].values[User_dbo.map.get("user_id")].lnumber);
                tweetuser.values[User_dbo.map.get("fri_fol_processed")].setValue(String.valueOf(existinguser[0].values[User_dbo.map.get("fri_fol_processed")].bool));
                LogPrinter.printLog("User Deleted");
                UsersTable.insert(tweetuser);
                }
        }
           
        }
        long uid1 = tweetuser.values[User_dbo.map.get("user_id")].lnumber;
        long uid2;
        for(User_dbo mentioneduser: mentionedusersdbos) {
            
            existinguser =  UsersTable.select(" user_id = "+mentioneduser.values[User_dbo.map.get("user_id")].lnumber+"", 0, 2);
        
        if(existinguser.length==0){
        UsersTable.insert(mentioneduser);
        }
        relationhelper.insertEdge(tweetuser, mentioneduser);
        }
    }
        
    
}
