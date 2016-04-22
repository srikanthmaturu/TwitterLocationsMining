/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCollections;

import DataAccess.ConnectionFactory;
import Logger.LogPrinter;
import datamanagement.PlacesTable;
import datamanagement.Tweet_dbo;
import datamanagement.TweetsTable;
import datamanagement.UsersTable;
import java.sql.Date;
import org.json.simple.JSONArray;
import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.UserMentionEntity;

/**
 *
 * @author Srikanth
 */
public class TweetHelper {
    
    public TweetHelper() {
         if(TweetsTable.connection==null){
        
        TweetsTable.connection = ConnectionFactory.defaultConnect();
    }
    }
    
    public Tweet_dbo[] selectTweets(boolean[] selected,String whereclause, int min_id, int count) {
        return TweetsTable.select(selected, whereclause, min_id, count);
    }
    
    public Tweet_dbo convertStatusToTweet_dbo(Status s) {
        Tweet_dbo tweet = new Tweet_dbo();
        tweet.values[Tweet_dbo.map.get("tweet_id")].setValue(String.valueOf(s.getId()));
        tweet.values[Tweet_dbo.map.get("user_id")].setValue(String.valueOf(s.getUser().getId()));
        tweet.values[Tweet_dbo.map.get("user_screenname")].setValue(removeEscapeCharacters(s.getUser().getScreenName()));
        if(s.getGeoLocation()!=null){
        tweet.values[Tweet_dbo.map.get("lon")].setValue(String.valueOf(s.getGeoLocation().getLongitude()));
        tweet.values[Tweet_dbo.map.get("lat")].setValue(String.valueOf(s.getGeoLocation().getLatitude()));
        }
        tweet.values[Tweet_dbo.map.get("f_search")].setValue("true");
        tweet.values[Tweet_dbo.map.get("text")].setValue(removeEscapeCharacters(s.getText()));
        tweet.values[Tweet_dbo.map.get("hashtags")].setValue(stringifyHashtags(s.getHashtagEntities()));
        tweet.values[Tweet_dbo.map.get("mentions")].setValue(stringiyMentions(s.getUserMentionEntities()));
        tweet.values[Tweet_dbo.map.get("favouritecount")].setValue(String.valueOf(s.getFavoriteCount()));
        tweet.values[Tweet_dbo.map.get("retweetcount")].setValue(String.valueOf(s.getRetweetCount()));
        return tweet;
    }
    
    
    
    public String stringifyHashtags(HashtagEntity[] hashtags) {
        JSONArray jsonarray = new JSONArray();
        for(HashtagEntity hashtag:hashtags){
            jsonarray.add(hashtag.getText());
        }
        return jsonarray.toJSONString();
    }
    
    public String stringiyMentions(UserMentionEntity[] mentions) {
        JSONArray jsonarray = new JSONArray();
        for(UserMentionEntity mention:mentions){
            jsonarray.add(mention.getId());
        }
        
        return jsonarray.toJSONString();
    }
     public String removeEscapeCharacters(String input){
         String escapedstring = input;
         escapedstring = escapedstring.replaceAll("'", "");
        escapedstring = escapedstring.replaceAll("\"", "");
        escapedstring = escapedstring.replaceAll(",", "");
        return escapedstring;
     }
    
}
