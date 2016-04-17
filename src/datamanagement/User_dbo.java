/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanagement;


import static datamanagement.Hashtag_dbo.fieldnames;
import java.util.HashMap;

/**
 *
 * @author Srikanth
 */
public class User_dbo {
    public static final String fieldnames[] = {"id","user_id","name","screename","lang","location","geoenabled","timezone","profileurl",
                            "protected","verified","description","createddate","probased_geoinfo","descbased_geoinfo","probased_lat","probased_lon",
                            "tweetbased_geoinfo","tweetbased_lat","tweetbased_lon","estimated_geoinfo","estimated_lat","estimated_lon","totaltweets"};
     public static final int types[] = {0,5,2,2,2,2,3,2,2,3,3,2,4,3,3,1,1,3,1,1,3,1,1,5};
     public static final int nooffields = fieldnames.length;
     public Datatype values[] = new Datatype[fieldnames.length];
     public static final String tablename = "`tweethitter`.`users`";
     public  static HashMap<String,Integer> map;
     boolean selected[] = new boolean[fieldnames.length];
     
     public User_dbo() {
         for(int i =0; i<nooffields;i++) {
             values[i] = new Datatype();
             values[i].type = types[i];
         }
         if(map==null){
             map = new HashMap<>();
             for(int i =0; i<nooffields; i++){
                 map.put(fieldnames[i], i);
             }
         }
         
     }
}
