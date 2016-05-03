/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SyntheticData;

/**
 *
 * @author Srikanth
 */
import java.awt.Color; 
import java.awt.BasicStroke; 
import java.io.File;
import java.io.IOException;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.data.xy.XYDataset; 
import org.jfree.data.xy.XYSeries; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.ui.RefineryUtilities; 
import org.jfree.chart.plot.XYPlot; 
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.plot.PlotOrientation; 
import org.jfree.data.xy.XYSeriesCollection; 
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
public class XYLineChart extends ApplicationFrame{
    
    
    public XYLineChart(String applicationTitle, String chartTitle, String xaxislabel, String yaxislabel ,XYSeriesCollection dataset) throws IOException {
        
        super(applicationTitle);
      JFreeChart xylinechart = ChartFactory.createXYLineChart(
         chartTitle ,
         xaxislabel ,
         yaxislabel ,
         dataset ,
         PlotOrientation.VERTICAL ,
         true , true , false);      
    
    ChartPanel cpanel = new ChartPanel(xylinechart);
    cpanel.setPreferredSize(new java.awt.Dimension(500, 500));
    final XYPlot xyplot = xylinechart.getXYPlot();
    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    renderer.setSeriesPaint(0, Color.blue);
    renderer.setSeriesStroke(0,new BasicStroke(2.0f) );
    xyplot.setRenderer(renderer);
    setContentPane(cpanel);
    File saveImageFile = new File(""+applicationTitle+".jpg");
    ChartUtilities.saveChartAsJPEG(saveImageFile, xylinechart, 1920, 1080);
    }
    
   
}
