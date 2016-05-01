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
    public double xcoor;
    public double ycoor;
    public boolean locationavailable = false;
    public boolean hideloc = false;
    public boolean locationestimate = false;
    public double xecoor;
    public double yecoor;
    
    public ArrayList<Edge> neighbours;
    
    public Node(String label,double[] geocoor){
        this.label = label;
        neighbours = new ArrayList<Edge>();
        this.xcoor = geocoor[0];
        this.ycoor = geocoor[1];
        locationavailable = true;
    }
    
    public Node(String label) {
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
    return this.label.hashCode();
    
}






}