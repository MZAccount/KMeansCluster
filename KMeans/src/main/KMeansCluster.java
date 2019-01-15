package main;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.jblas.DoubleMatrix;

public class KMeansCluster
{
    private static int NR_CLUSTERE;    		// Total clustere.
    private static int TOTAL_PUNCTE;    	// Total puncte.

    private static double PuncteNegrupate[][];
    
    private static ArrayList<Punct> setDate = new ArrayList<>();
    private static ArrayList<Centroid> centroizi = new ArrayList<>();
    
    
    private static void initializareDate(String fisier)
    {
		print("Centroizi initializati:");
		String date = "";
		File file = new File(fisier);
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(file);

			date = IOUtils.toString(fileInputStream, "UTF-8");

		} catch (IOException e) {
			e.printStackTrace();
		}
		
        parsareDate(date);
    }

	private static void parsareDate(String date) {
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
            print("     (" + centroizi.get(i).X() + ", " + centroizi.get(i).Y()+")");
        }
        System.out.print("\n");
            
        split = split0[1].split("\r\n");
        PuncteNegrupate= new double[split.length][2];
            for (int i = 0; i < split.length; i++) {
            	String[] split2 = split[i].split("[ \t]");
    			String s1 = split2[0];
            	PuncteNegrupate[i][0]=Double.parseDouble(s1);
    			String s2 = split2[1];
            	PuncteNegrupate[i][1]=Double.parseDouble(s2);
    		}
        TOTAL_PUNCTE=PuncteNegrupate.length;
	}
    
    private static void clusterKMeans()
    {
        int sampleNumber = 0;
        StareGrupare continuaOptimizarea = StareGrupare.NEOPTIMIZAT;
        Punct punct = null;
        
        // Adauga date noi si recalculeaza centroizii
        while(setDate.size() < TOTAL_PUNCTE) {

            punct = new Punct(PuncteNegrupate[sampleNumber][0], PuncteNegrupate[sampleNumber][1]);
            setDate.add(punct);

            int cluster = centroidMinim(punct);
            punct.cluster(cluster);
            
            // calculeaza centroizi noi
            recalculeazaPozitiaCentroizilor();
            sampleNumber++;
        }
        
        // Continua pasii de optimizarea a pozitiei centroizilor
        while(continuaOptimizarea==StareGrupare.NEOPTIMIZAT)
        {
            // calculeaza centroizi noi
        	 recalculeazaPozitiaCentroizilor();
            
            // atribuie datele la noii centroizi
            continuaOptimizarea = grupeazaPunctele();
        }
        return;
    }

	private static int centroidMinim(Punct punct) {
		int cluster = 0;
		double minim = Double.MAX_VALUE;
		for(int i = 0; i < NR_CLUSTERE; i++)
		{
			double distance = dist(punct, centroizi.get(i));
		    if(distance < minim){
		        minim = distance;
		        cluster = i;
		    }
		}
		return cluster;
	}
	
	enum StareGrupare{NEOPTIMIZAT, OPTIMIZAT}
	
	/**
	 * Atribuie pentru fiecare punct cel mai apropiat cluster
	 */
	private static StareGrupare grupeazaPunctele() {
		// Presupunem initial ca este optimizat
		StareGrupare continuaOptimizarea = StareGrupare.OPTIMIZAT;
        
		for (int i = 0; i < setDate.size(); i++) {
			Punct tempData = setDate.get(i);
			int cluster = centroidMinim(tempData);
		    if(tempData.cluster() != cluster){
		        tempData.cluster(cluster);
		        continuaOptimizarea = StareGrupare.NEOPTIMIZAT;
		    }
		}
		return continuaOptimizarea;
	}


	private static void recalculeazaPozitiaCentroizilor() {
		for(int i = 0; i < NR_CLUSTERE; i++)
		{
		    double totalX = 0;
		    double totalY = 0;
		    double totalZ = 0;
		    int totalInCluster = 0;
		    for(int j = 0; j < setDate.size(); j++)
		    {
		        if(setDate.get(j).cluster() == i){
		            totalX += setDate.get(j).X();
		            totalY += setDate.get(j).Y();
		            totalZ += setDate.get(j).Z();
		            totalInCluster++;
		        }
		    }
		    if(totalInCluster > 0){
		        centroizi.get(i).X(totalX / totalInCluster);
		        centroizi.get(i).Y(totalY / totalInCluster);
		        centroizi.get(i).Z(totalZ / totalInCluster);
		    }
		}
	}

    // Functia de distanta
    private static double dist(Punct v1, Centroid v2)
    {
        return distEuclid(v1,v2);
    }

    // Distanta Euclidiana
    @SuppressWarnings("unused")
    private static double distEuclid(Punct v1, Centroid v2)
    {
        return Math.sqrt(Math.pow((v2.Y() - v1.Y()), 2) + Math.pow((v2.X() - v1.X()), 2));
    }
    
    
    // Distanta Manhattan
    @SuppressWarnings("unused")
	private static double distMan(Punct v1, Centroid v2)
    {
        return Math.abs((v2.Y() - v1.Y())) + Math.abs((v2.X() - v1.X()));
    }
    
    private static class Punct
    {
        private int mIndexCluster = 0;
        
        DoubleMatrix s=new DoubleMatrix(1,3);
        
        public Punct(double x, double y)
        {
            this.X(x);
            this.Y(y);
            return;
        }

        public void X(double x)
        {
            this.s.put(0, 0, x);
            return;
        }
        
        public double X()
        {
            return this.s.get(0, 0);
        }
        
        public void Y(double y)
        {
            this.s.put(0,1,y);
            return;
        }
        
        public double Y()
        {
            return this.s.get(0,1);
        }

        @SuppressWarnings("unused")
		public void Z(double z)
        {
            this.s.put(0, 2, z);
            return;
        }
        
        public double Z()
        {
            return this.s.get(0, 2);
        }
        
        public void cluster(int cluster)
        {
            this.mIndexCluster = cluster;
            return;
        }
        
        public int cluster()
        {
            return this.mIndexCluster;
        }
    }
    
    private static class Centroid
    {

        DoubleMatrix s=new DoubleMatrix(1,3);
        
        public Centroid(double X, double Y)
        {
            this.s.put(0,0,X);
            this.s.put(0,1,Y);
            return;
        }
        

		public void X(double X)
        {
            this.s.put(0, 0,X);
            return;
        }
        
        public double X()
        {
            return this.s.get(0,0);
        }
        
        public void Y(double Y)
        {
            this.s.put(0, 1,Y);
            return;
        }
        
        public double Y()
        {
            return this.s.get(0,1);
        }
        
		public void Z(double Z)
        {
            this.s.put(0, 2,Z);
            return;
        }
        
        @SuppressWarnings("unused")
		public double Z()
        {
            return this.s.get(0,2);
        }
    }
    
    public static void main(String[] args)
    {
        new KMeansCluster().run(args);
    }

    
	public void run(String[] args) {
		initializareDate(args[0]);
        clusterKMeans();
        
        // Printeaza numarul de clustere
        for(int i = 0; i < NR_CLUSTERE; i++)
        {
            print("Cluster " + i + " final:");
            for(int j = 0; j < TOTAL_PUNCTE; j++)
            {
                if(setDate.get(j).cluster() == i){
                    print("     (" + setDate.get(j).X() + ", " + setDate.get(j).Y() + ")");
                }
            }

        }
        
        // Printeaza centroizii finali
        print("Centroizi finali:");
        for(int i = 0; i < NR_CLUSTERE; i++)
        {
            print("     (" + centroizi.get(i).X() + ", " + centroizi.get(i).Y()+")");
        }
        System.out.print("\n");
        return;
	}

	private static void print(String string) {
		System.out.println(string);
	}
    
    
}