/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterlocationsmining;

import DataCollections.*;

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
    
    public TwitterLocationsMining() {
        userrelationhelper = new UserRelationsHelper();
        userhelper = new UserHelper();
    }
    public static void main(String[] args) {
        TwitterLocationsMining locationminer = new TwitterLocationsMining();  
    }
    
    void estimator() {
        
    }
    
}
