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
public class Tweet_dbo {
    
    
       public final static String fieldnames[] = {"id","text","timestamp","entrytime","searchterm","favouritecount","retweetcount"
        ,"tweet_id","hastags","mentions","inreply_to_user_id","inreply_to_screenname","inreply_to_statusid"
        ,"isretweet","user_id","user_screenname","lat","lon"};
     public final static int types[] = {5,2,2,2,2,5,5,5,2,2,5,2,5,3,5,2,1,1};
     public final static int nooffields = fieldnames.length;
     public Datatype values[] = new Datatype[fieldnames.length];
     public static final String tablename = "`tweethitter`.`tweets`";
     public  static HashMap<String,Integer> map;
     boolean selected[] = new boolean[fieldnames.length];
     
     public Tweet_dbo() {
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
