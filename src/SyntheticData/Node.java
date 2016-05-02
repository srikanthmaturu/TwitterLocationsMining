/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SyntheticData;

import java.util.ArrayList;

/**
 *
 * @author Srikanth
 */
public class Node {
    
    public String label;
    public int xcoor;
    public int ycoor;
    public boolean locationavailable = false;
    public boolean hideloc = false;
    public boolean locationestimate = false;
    public double xecoor;
    public double yecoor;
    public double deviation=0;
    
    public ArrayList<Edge> neighbours;
    
    public Node(String label,int[] geocoor){
        this.label = label;
        neighbours = new ArrayList<Edge>();
        this.xcoor = geocoor[0];
        this.ycoor = geocoor[1];
        locationavailable = true;
    }
    
    public Node(String label) {
        neighbours = new ArrayList<Edge>();
        this.label = label;
        
    }
    
    public boolean addNeighbour(Edge e){
        if(!neighbours.contains(e)){
            neighbours.add(e);
            return true;
        }
        else{
            return false;
        }
    }
        
    public boolean isNeighbour(Edge e){
        
        if(!neighbours.contains(e)) {
            return true;
        }
        
        return false;
    }


public boolean equals(Object o){
        if(!(o instanceof Node)){
        return false;
        }

        Node n = (Node)o;
        return this.label.equals(n.label);
}       
    

public int hashCode() {
    String location = String.valueOf(xcoor)+" "+String.valueOf(ycoor);
    return location.hashCode();
    
}

public boolean computeDeviation(){
    if(locationestimate&&hideloc){
    deviation = distance(xcoor,xecoor,ycoor,yecoor);
    return true;
    }
    return false;
    
}

public double distance(double x1, double x2, double y1, double y2) {
        return Math.sqrt((Math.pow((x1-x2), 2)+ Math.pow((y1-y2), 2)));
    }

public String toString() {
    return label;
}






}