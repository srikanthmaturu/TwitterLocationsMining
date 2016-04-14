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
public class Place_dbo {
     public final static String fieldnames[] = {"id","name","full_name","country","country_code","place_type","url",
            "boundingbox_coord","centroid_lat","centroid_lon","max_id","since_id"};
     public final static int types[] = {5,2,2,2,2,2,2,2,1,1,5,5};
     public final static int nooffields = fieldnames.length;
     public Datatype values[] = new Datatype[fieldnames.length];
     public  final static String tablename = "`tweethitter`.`places`";
     public  static HashMap<String,Integer> map;
     boolean selected[] = new boolean[fieldnames.length];
     
     public Place_dbo() {
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
