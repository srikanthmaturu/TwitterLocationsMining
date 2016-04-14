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
public class UserRelation_dbo {
     public final static String fieldnames[] = {"id","fu_id","su_id","fu_tweets","lu_tweets"};
     public final static int types[] = {5,5,5,5,5};
     public final static int nooffields = fieldnames.length;
     public Datatype values[] = new Datatype[fieldnames.length];
     static final String tablename = "`tweethitter`.`userrelations`";
     public  static HashMap<String,Integer> map;
     boolean selected[] = new boolean[fieldnames.length];
     
     public UserRelation_dbo() {
         for(int i =0; i<nooffields;i++) {
             values[i] = new Datatype();
             values[i].type = types[i];
         }
         if(map==null){
             for(int i =0; i<nooffields; i++){
                 map.put(fieldnames[i], i);
             }
         }
     }
    
}
