/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataAccess;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;

/**
 *
 * @author Srikanth
 */
public class TwitterResources {
    
    public static Twitter twitter; 
    
    public static Twitter getTwitterSingleton(){
        if(twitter==null){
            twitter = TwitterFactory.getSingleton();
        }
        return twitter;
    }
    
}
