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
public class Location_dbo {
    
     public final static String fieldnames[] = {"id","country_code","country_name","name","place_code","place_name","url","woeid"};
     public final static int types[] = {5,2,2,2,5,2,2,5};
     public final static int nooffields = fieldnames.length;
     public final static String tablename = " `tweethitter`.`locations` ";
     public static HashMap<String,Integer> map = null;
     boolean selected[] = new boolean[fieldnames.length];
     
     public Datatype values[] = new Datatype[fieldnames.length];
     
     public Location_dbo() {
         
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
