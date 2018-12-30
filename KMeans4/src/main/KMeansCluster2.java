package main;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;

public class KMeansCluster2
{
    private static int NR_CLUSTERE;    		// Total clustere.
    private static int TOTAL_PUNCTE;    	// Total puncte.
    
    private static double PUNCTE[][];
    
    private static ArrayList<Date> setDate = new ArrayList<>();
    private static ArrayList<Centroid> centroizi = new ArrayList<>();
    
    private static void initializareDate(String fisier)
    {
        System.out.println("Centroizi initializati:");
        String date="";
     	File file = new File(fisier);
    	FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(file);
		
    	
    	String theString = IOUtils.toString(fileInputStream, "UTF-8"); 
		
        date=theString;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        String[] split0 = date.split("\r\n\r\n");
        String[] split = split0[0].split("\r\n");
        for (int i = 0; i < split.length; i++) {
        	String[] split2 = split[i].split("[ \t]");
			String s1 = split2[0];
			double x = Double.parseDouble(s1);
			String s2 = split2[1];
			double y = Double.parseDouble(s2);
			centroizi.add(new Centroid(x, y));
		}
        NR_CLUSTERE=centroizi.size();

        for(int i = 0; i < NR_CLUSTERE; i++)
        {
            System.out.println("     (" + centroizi.get(i).X() + ", " + centroizi.get(i).Y()+")");
        }
        System.out.print("\n");
            
        split = split0[1].split("\r\n");
        PUNCTE= new double[split.length][2];
            for (int i = 0; i < split.length; i++) {
            	String[] split2 = split[i].split("[ \t]");
    			String s1 = split2[0];
            	PUNCTE[i][0]=Double.parseDouble(s1);
    			String s2 = split2[1];
            	PUNCTE[i][1]=Double.parseDouble(s2);
    		}
        TOTAL_PUNCTE=PUNCTE.length;
    }
    
    private static void kMeansCluster()
    {
        int sampleNumber = 0;
        int cluster = 0;
        boolean continuaOptimizarea = true;
        Date date = null;
        
        // Add in new data, one at a time, recalculating centroids with each new one. 
        while(setDate.size() < TOTAL_PUNCTE)
        {
            date = new Date(PUNCTE[sampleNumber][0], PUNCTE[sampleNumber][1]);
            setDate.add(date);
            double minim = Double.MAX_VALUE;
            for(int i = 0; i < NR_CLUSTERE; i++)
            {
            	double distance = dist(date, centroizi.get(i));
                if(distance < minim){
                    minim = distance;
                    cluster = i;
                }
            }
            date.cluster(cluster);
            
            // calculeaza centroizi noi
            for(int i = 0; i < NR_CLUSTERE; i++)
            {
                double totalX = 0;
                double totalY = 0;
                int totalInCluster = 0;
                for(int j = 0; j < setDate.size(); j++)
                {
                    if(setDate.get(j).cluster() == i){
                        totalX += setDate.get(j).X();
                        totalY += setDate.get(j).Y();
                        totalInCluster++;
                    }
                }
                if(totalInCluster > 0){
                    centroizi.get(i).X(totalX / totalInCluster);
                    centroizi.get(i).Y(totalY / totalInCluster);
                }
            }
            sampleNumber++;
        }
        
        // Continua pasii de optimizarea a pozitiei centroizilor
        while(continuaOptimizarea)
        {
            // calculeaza centroizi noi
            for(int i = 0; i < NR_CLUSTERE; i++)
            {
                double totalX = 0;
                double totalY = 0;
                int totalInCluster = 0;
                for(int j = 0; j < setDate.size(); j++)
                {
                    if(setDate.get(j).cluster() == i){
                        totalX += setDate.get(j).X();
                        totalY += setDate.get(j).Y();
                        totalInCluster++;
                    }
                }
                if(totalInCluster > 0){
                    centroizi.get(i).X(totalX / totalInCluster);
                    centroizi.get(i).Y(totalY / totalInCluster);
                }
            }
            
            // Assign all data to the new centroids
            continuaOptimizarea = false;
            
            for(int i = 0; i < setDate.size(); i++)
            {
                Date tempData = setDate.get(i);
                double minim = Double.MAX_VALUE;
                for(int j = 0; j < NR_CLUSTERE; j++)
                {
                	double distance = dist(tempData, centroizi.get(j));
                    if(distance < minim){
                        minim = distance;
                        cluster = j;
                    }
                }
                if(tempData.cluster() != cluster){
                    tempData.cluster(cluster);
                    continuaOptimizarea = true;
                }
            }
        }
        return;
    }

    // Distanta Euclidiana
    private static double dist(Date v1, Centroid v2)
    {
        return distEuclid(v1,v2);
    }

    // Distanta Euclidiana
    @SuppressWarnings("unused")
    private static double distEuclid(Date v1, Centroid v2)
    {
        return Math.sqrt(Math.pow((v2.Y() - v1.Y()), 2) + Math.pow((v2.X() - v1.X()), 2));
    }
    
    
    // Distanta Manhattan
    @SuppressWarnings("unused")
	private static double distMan(Date v1, Centroid v2)
    {
        return Math.abs((v2.Y() - v1.Y())) + Math.abs((v2.X() - v1.X()));
    }
    
    private static class Date
    {
        private double mX = 0;
        private double mY = 0;
        private int mCluster = 0;
        
        public Date(double x, double y)
        {
            this.X(x);
            this.Y(y);
            return;
        }
        
        public void X(double x)
        {
            this.mX = x;
            return;
        }
        
        public double X()
        {
            return this.mX;
        }
        
        public void Y(double y)
        {
            this.mY = y;
            return;
        }
        
        public double Y()
        {
            return this.mY;
        }
        
        public void cluster(int clusterNumber)
        {
            this.mCluster = clusterNumber;
            return;
        }
        
        public int cluster()
        {
            return this.mCluster;
        }
    }
    
    private static class Centroid
    {
        private double mX = 0.0;
        private double mY = 0.0;
        
        public Centroid(double X, double Y)
        {
            this.mX = X;
            this.mY = Y;
            return;
        }
        
        public void X(double X)
        {
            this.mX = X;
            return;
        }
        
        public double X()
        {
            return this.mX;
        }
        
        public void Y(double Y)
        {
            this.mY = Y;
            return;
        }
        
        public double Y()
        {
            return this.mY;
        }
    }
    
    public static void main(String[] args)
    {
        initializareDate(args[0]);
        kMeansCluster();
        
        // Printeaza numarul de clustere
        for(int i = 0; i < NR_CLUSTERE; i++)
        {
            System.out.println("Cluster " + i + " final:");
            for(int j = 0; j < TOTAL_PUNCTE; j++)
            {
                if(setDate.get(j).cluster() == i){
                    System.out.println("     (" + setDate.get(j).X() + ", " + setDate.get(j).Y() + ")");
                }
            } // j
            System.out.println();
        } // i
        
        // Printeaza centroizii finali
        System.out.println("Centroizi finali:");
        for(int i = 0; i < NR_CLUSTERE; i++)
        {
            System.out.println("     (" + centroizi.get(i).X() + ", " + centroizi.get(i).Y()+")");
        }
        System.out.print("\n");
        return;
    }
}
