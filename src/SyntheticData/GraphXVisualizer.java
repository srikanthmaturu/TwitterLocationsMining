/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SyntheticData;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import java.awt.Dimension;
import javax.swing.JApplet;
import javax.swing.JFrame;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableUndirectedGraph;

/**
 *
 * @author Srikanth
 */
public class GraphXVisualizer extends JApplet {
    
    public Graph graph;
    public static Dimension DEFAULT_SIZE = new Dimension(530, 320);

    public JGraphXAdapter<Node, DefaultEdge> jgxAdapter;
    
    public GraphXVisualizer(Graph graph, int nxpoints, int nypoints){
        this.graph = graph;
        DEFAULT_SIZE = new Dimension(nxpoints,nypoints);
        
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
        jgxAdapter = new JGraphXAdapter<Node,DefaultEdge>(g);
        getContentPane().add(new mxGraphComponent(jgxAdapter));
        resize(DEFAULT_SIZE);
        
        for(Node n: graph.vertices.values()){
            g.addVertex(n);
        }
        for(Edge e: graph.edges.values()){
            g.addEdge(e.one, e.two);
        }
        
        mxCircleLayout layout = new mxCircleLayout(jgxAdapter,0.10);
        layout.execute(jgxAdapter.getDefaultParent());
        
        LayOut lt = new LayOut(jgxAdapter);
        
    }
    
    public class LayOut extends mxGraphLayout  {

        public LayOut(mxGraph graph) {
            super(graph);
        }
        
    }
     
    public void positionVertex(){
        
    }
}
