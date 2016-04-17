/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanagement;



import java.util.HashMap;

/**
 *
 * @author Srikanth
 */
public class Hashtag_dbo {
    
    
      public final static String fieldnames[] = {"id","hashtag_popterm","nooftweets","popular","ftrendsres","ftweets","fusers"
        ,"fgeoloc","pop_location","lat","lon","max_id","since_id","processed"};
     public final static int types[] = {5,2,5,3,3,3,3,3,2,1,1,5,5,3};
     public final static int nooffields = fieldnames.length;
     public Datatype values[] = new Datatype[fieldnames.length];
     public static final String tablename = "`tweethitter`.`hashtags`";
     public  static HashMap<String,Integer> map;
     boolean selected[] = new boolean[fieldnames.length];
     
     
     public Hashtag_dbo() {
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
