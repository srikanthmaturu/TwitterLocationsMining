/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanagement;

import Logger.LogPrinter;
import java.sql.Date;

/**
 *
 * @author Srikanth
 */
public class Datatype {
    public Integer number;
    public Double decimal;
    public String string;
    public Boolean bool ;
    public Date date;
    public Long lnumber;
    
    public boolean used = false;
    
    public int type;
    
    public Datatype() {
        
    }
    
    public String getType() {
        switch(type)
        {
            case 0:
                return "integer";
            case 1:
                return "decimal";
               
            case 2:
                return "string";
                
            case 3: 
                return "bool";
            case 4:
                return "date";
            case 5:
                return "lnumber";
            default:
                return null;
        }
    }
    
    public void setValue(String value) {
        used = false;
        switch(type)
        {
            case 0:
                number = Integer.parseInt(value);
                break;
            case 1:
                decimal = Double.parseDouble(value);
                break;
            case 2:
                string = value;
                break;
            case 3: 
                bool = Boolean.parseBoolean(value);
                //LogPrinter.printLog("received value "+value);
               // LogPrinter.printLog("stored value "+bool);
                if("0".equals(value)||"1".equals(value)){
                   // LogPrinter.printLog("Stored Appropriately");
                 if("0".equals(value)){
                     bool = false;
                 }
                 else {
                     bool = true;
                 }
                }
                break;
            case 4:
                date = Date.valueOf(value);
                break;
            case 5:
                lnumber = Long.parseLong(value);
                break;
            default:
                used=true;
                System.out.println("Not Initialized");
        }
        used=!used;
        
    }
    
    public Object getValue(){
        switch(type)
        {
            case 0:
                return number;
            case 1:
                return decimal;
               
            case 2:
                return string;
                
            case 3: 
                return bool;
                
            case 4:
                return date;
            
            case 5:
                return lnumber;
                
            default:
                System.out.println("Not Initialized");
                return null;
        }
    }
    
    public String getStringValue() {
        switch(type)
        {
            case 0:
                return String.valueOf(number);
            case 1:
                return String.valueOf(decimal);
               
            case 2:
                return string;
                
            case 3: 
                return String.valueOf(bool);
                
            case 4:
                return String.valueOf(date);
            
            case 5:
                return String.valueOf(lnumber);
                
            default:
                System.out.println("Not Initialized");
                return null;
        }
        
    }
    
     public String getSQLStringValue() {
        switch(type)
        {
            case 0:
                return String.valueOf(number);
            case 1:
                return String.valueOf(decimal);
               
            case 2:
                return "'"+string+"'";
                
            case 3: 
                return String.valueOf(bool);
                
            case 4:
                return "'"+String.valueOf(date)+"'";
                
            case 5: 
                return String.valueOf(lnumber);
                
            default:
                System.out.println("Not Initialized");
                return null;
        }
        
    }
    
    
    

}
