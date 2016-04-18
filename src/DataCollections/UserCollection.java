/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCollections;

import datamanagement.User_dbo;
import datamanagement.UsersTable;
import java.util.ListIterator;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.api.UsersResources;

/**
 *
 * @author Srikanth
 */
public class UserCollection {
    public UserHelper userhelper;
    Twitter twitter;
    UsersResources userres;
    
    public UserCollection(){
        userhelper = new UserHelper();
        twitter = TwitterFactory.getSingleton();
        userres = twitter.users();
    }
    
    void updateUsersWithIncompleteDetails(){
        
        boolean available = true;
        long min_id = 0;
        int count = 100;
        User_dbo[] users;
        ResponseList<User> userslist  = null;
        while(true){
            users = UsersTable.select( " udetails_processed = false", min_id, count);
            if(users.length>0){
                min_id = users[users.length-1].values[User_dbo.map.get("id")].lnumber;
                
            }
            else{
                available = false;
                continue;
            }
            long[] userids = new long[users.length];
            int index =0;
            for(User_dbo u: users){
                userids[index] = u.values[User_dbo.map.get("user_id")].lnumber;
                index++;
            }
            try{
            userslist = userres.lookupUsers(userids);
            }
            catch(Exception e){
                
            }
            if(userslist!=null) {
            if(userslist.size()>0){
                ListIterator li = userslist.listIterator();
                while(li.hasNext()){
                 User u = (User)li.next();
                 User_dbo usdbo = userhelper.convertUserToUser_dbo(u);
                 UsersTable.delete(usdbo.values[User_dbo.map.get("user_id")].lnumber);
                 UsersTable.insert(usdbo);
                }
                
            }
            }
        }
    }
    
    
    
    
    
    
    
}
