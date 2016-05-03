/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SyntheticData;

import Logger.LogPrinter;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableUndirectedGraph;

/**
 *
 * @author Srikanth
 */
public class GraphVisualizer extends JApplet {
    
    public Graph graph;
    public static Dimension DEFAULT_SIZE = new Dimension(1920, 1080);
    public static Color DEFAULT_BG_COLOR = Color.decode("#000000");
    public int nxpoints;
    public int nypoints;
    JScrollPane scrollPane;
     JGraph jgraph;
    String filename = "F:\\Google Drive(srikanthmaturu1)\\Final Project\\test.png";;

    public JGraphModelAdapter<Node, DefaultEdge> jgAdapter;
    
    public GraphVisualizer(Graph graph, int nxpoints, int nypoints){
        this.graph = graph;
        this.nxpoints = nxpoints;
        this.nypoints = nypoints;
                
        
    }
    
    public void run() {
        this.init();
        JFrame frame = new JFrame();
        frame.getContentPane().add(this);
        frame.setTitle(" Visualization of Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void init() {
        
        ListenableUndirectedGraph<Node,DefaultEdge> g = new ListenableUndirectedGraph<Node,DefaultEdge>(DefaultEdge.class);
        jgAdapter = new JGraphModelAdapter<Node,DefaultEdge>(g);
        jgraph = new JGraph(jgAdapter);
        
        adjustDisplaySettings(jgraph);
        JPanel panel = new JPanel();
        panel.add(jgraph);
        scrollPane = new JScrollPane(panel);
        getContentPane().add(scrollPane,BorderLayout.CENTER);
        resize(DEFAULT_SIZE);
        
        
        for(Node n: graph.vertices.values()){
            g.addVertex(n);
        }
        for(Edge e: graph.edges.values()){
            g.addEdge(e.one, e.two);
        }
        
       for(Node n: graph.vertices.values()) {
           int loc[] = new int[2];
           loc[0] = (int)Math.floor(((double)n.xcoor/(double)nxpoints)*7000);
           loc[1] = (int)Math.floor(((double)n.ycoor/(double)nypoints)*7000);
           positionVertexAt(n, loc[0], loc[1]);
       }
       
        for(Edge e: graph.edges.values()){
           modifyEdge(g.getEdge(e.one, e.two));
        }
        
    }
     
    public void adjustDisplaySettings(JGraph jg)
    {
        jg.setPreferredSize(new Dimension(nxpoints*7,nypoints*7));

        Color c = DEFAULT_BG_COLOR;
        String colorStr = null;

        try {
            colorStr = getParameter("bgcolor");
        } catch (Exception e) {
        }

        if (colorStr != null) {
            c = Color.decode(colorStr);
        }
        jg.setBackground(c);
       
    }

    @SuppressWarnings("unchecked") 
    public void positionVertexAt(Object vertex, int x, int y)
    {
        DefaultGraphCell cell = jgAdapter.getVertexCell(vertex);
        AttributeMap attr = cell.getAttributes();
        Rectangle2D bounds = GraphConstants.getBounds(attr);
        
        Rectangle2D newBounds =
            new Rectangle2D.Double(
                x,
                y,
                5,
                5);

        GraphConstants.setBounds(attr, newBounds);

       
        AttributeMap cellAttr = new AttributeMap();
        cellAttr.put(cell, attr);
        jgAdapter.edit(cellAttr, null, null, null);
    }
    
    public void modifyEdge(DefaultEdge e){
        org.jgraph.graph.DefaultEdge cell = jgAdapter.getEdgeCell(e);
        AttributeMap attr = cell.getAttributes();
        float width = GraphConstants.getLineWidth(attr);
        GraphConstants.setLineColor(attr, DEFAULT_BG_COLOR);
        //LogPrinter.printLog("Changing Edge");
        GraphConstants.setLabelEnabled(attr, false);
    }
    
    public void saveImage(){
        
        BufferedImage image = new BufferedImage(jgraph.getWidth(), jgraph.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        jgraph.paint(g);
        try {
        ImageIO.write(image, "png", new File(filename));
        } catch (IOException ex) {
            LogPrinter.printLog(null);
        }
    }
    
    

 
    
    
}