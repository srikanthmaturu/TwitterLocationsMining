/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterlocationsmining;

import Logger.LogPrinter;
import SyntheticData.GraphXVisualizer;
import SyntheticData.XYLineChart;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author Srikanth
 */
public class syntheticdataexperiments {
    
        double density = 0.001;
        int nxpoints = 500;
        int nypoints = 500;
        int radius = 25;
        double percent = 0.4;
        int seed = 5;
        String file ="F:\\Google Drive(srikanthmaturu1)\\Final Project";
        
        
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        syntheticdataexperiments experiments = new syntheticdataexperiments();
        experiments.generateChartFordensity_V_AvgDeviation();
    }

    public syntheticdataexperiments() throws FileNotFoundException {
       
        
    }
    
    long getExecutionTime() {
        
        return bean.getCurrentThreadCpuTime();
        
    }
    
    
    void generateChartFordensity_V_AvgDeviation() throws IOException, InterruptedException {
        String applicationtitle = "Plot of Synthetic Data (Density versus Deviation) ";
        String chartitle = "Density versus Deviation (Percent hidden: 40%) ";
        String xaxislabel = "Density (No of Users per unit area)";
        String yaxislabel = "Deviation of Location Estimator";
        XYSeries avgdevseries = new XYSeries("Average Deviation");
        XYSeries mindevseries = new XYSeries("Min Deviation");
        XYSeries maxdevseries = new XYSeries("Max Deviation");
        XYSeries perestseries = new XYSeries("Percent of Users Locations Estimated");
        
        TwitterLocationsMiningUsingSyntheticData sminer;
        for(int i=1;i<100; i++){
            LogPrinter.printLog("Iteration "+i);
            sminer = new TwitterLocationsMiningUsingSyntheticData();
            LogPrinter.printLog("Building Network");
            
            sminer.buildNetwork(nxpoints, nypoints, density*i, radius, seed);
            LogPrinter.printLog("Adding Edges");
            sminer.buildEdges(radius);
            LogPrinter.printLog("No of Edges: "+sminer.graph.edges.size());
            
            LogPrinter.printLog("Hiding Percent of Users Locations "+percent);
            sminer.hideSomePercent(percent, seed);
            sminer.estimateUnknownLocations();
            sminer.computeDeviation();
            
            double avgdev = sminer.avgDeviation();
            LogPrinter.printLog("Density: "+density*i+" Radius: "+radius+" Deviation: "+avgdev+ " Max Deviation: "+sminer.maxDeviation()
                    +" Min Deviation: "+sminer.minDeviation()+" No of Users Hidden: "+sminer.noofusershidden+" No of Users Estimated "
                    + sminer.noofestimatedlocs+" Percent Estimated: "+((double)sminer.noofestimatedlocs/(double)sminer.noofusershidden)+" No of Users Not Estimated: "+(sminer.noofusershidden
                            -sminer.noofestimatedlocs));
            //sminer.gvis = new GraphXVisualizer(sminer.graph, nxpoints, nypoints);
            //sminer.gvis.run();
            avgdevseries.add(density*i,avgdev );
            mindevseries.add(density*i, sminer.mindev);
            maxdevseries.add(density*i, sminer.maxdev);
            perestseries.add(density*i, ((double)sminer.noofestimatedlocs/(double)sminer.noofusershidden));
            
        }
            
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(avgdevseries);
        dataset.addSeries(mindevseries);
        dataset.addSeries(maxdevseries);
        dataset.addSeries(perestseries);
        XYLineChart linechart = new XYLineChart(applicationtitle,chartitle, xaxislabel, yaxislabel, dataset);
        linechart.pack();
        RefineryUtilities.centerFrameOnScreen(linechart);
        linechart.setVisible(true);
    }
    
    
    
    
}
