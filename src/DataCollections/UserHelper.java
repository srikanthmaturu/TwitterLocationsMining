/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCollections;

import DataAccess.ConnectionFactory;
import datamanagement.PlacesTable;
import datamanagement.User_dbo;
import datamanagement.UsersTable;
import twitter4j.User;
import twitter4j.UserMentionEntity;

/**
 *
 * @author Srikanth
 */
public class UserHelper {
    
    public boolean geoinfoavailable = false;
    public GeoNamesHelper geohelper;
    public UserHelper() {
        geohelper = new GeoNamesHelper();
        if(UsersTable.connection==null){
        UsersTable.connection = ConnectionFactory.defaultConnect();
        }
    }
    public User_dbo[] selectUsers(boolean[] selected, String whereclause, int min_id, int count){
            return UsersTable.select(selected, whereclause, min_id, count);
    }
    
    public User_dbo convertUserToUser_dbo(User user){
        
        User_dbo u = new User_dbo();
        u.values[User_dbo.map.get("user_id")].setValue(String.valueOf(user.getId()));
        u.values[User_dbo.map.get("name")].setValue(removeEscapeCharacters(user.getName()));
        u.values[User_dbo.map.get("screename")].setValue(removeEscapeCharacters(user.getScreenName()));
        u.values[User_dbo.map.get("lang")].setValue(user.getLang());
        u.values[User_dbo.map.get("location")].setValue(removeEscapeCharacters(user.getLocation()));
        u.values[User_dbo.map.get("geoenabled")].setValue(String.valueOf(user.isGeoEnabled()));
        u.values[User_dbo.map.get("timezone")].setValue(user.getTimeZone());
        u.values[User_dbo.map.get("profileurl")].setValue(user.getURL());
        u.values[User_dbo.map.get("protected")].setValue(String.valueOf(user.isProtected()));
        u.values[User_dbo.map.get("verified")].setValue(String.valueOf(user.isVerified()));
        
        
       
        u.values[User_dbo.map.get("description")].setValue(removeEscapeCharacters(user.getDescription()));
        
        if(geoinfoavailable){
            double[] geocoor = geohelper.searchGeoLocCoor(user.getLocation());
            u.values[User_dbo.map.get("probased_geoinfo")].setValue(String.valueOf("true"));
            //u.values[User_dbo.map.get("descbased_geoinfo")].setValue(String.valueOf("false"));
            u.values[User_dbo.map.get("probased_lat")].setValue(String.valueOf(geocoor[0]));
            u.values[User_dbo.map.get("probased_lon")].setValue(String.valueOf(geocoor[1]));
        }
        u.values[User_dbo.map.get("udetails_processed")].setValue(String.valueOf(true));
        u.values[User_dbo.map.get("totaltweets")].setValue(String.valueOf(user.getStatusesCount()));
        return u;
    }
    
     public User_dbo convertMentionedUserToUser_dbo(UserMentionEntity user){
        
        User_dbo u = new User_dbo();
        u.values[User_dbo.map.get("user_id")].setValue(String.valueOf(user.getId()));
        u.values[User_dbo.map.get("name")].setValue(removeEscapeCharacters(user.getName()));
        u.values[User_dbo.map.get("screename")].setValue(removeEscapeCharacters(user.getScreenName()));
        return u;
    }
     
     public String removeEscapeCharacters(String input){
         String escapedstring = input;
         escapedstring = escapedstring.replaceAll("'", "");
        escapedstring = escapedstring.replaceAll("\"", "");
        escapedstring = escapedstring.replaceAll(",", "");
        return escapedstring;
     }
    
    
    
    
}
