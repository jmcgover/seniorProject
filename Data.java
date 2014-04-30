import java.io.*;
import java.lang.*;
import java.util.*;

public class Data{

   /* private Instance variables
    */
   private static final String DEFAULT_DATASET = "pyroprints.csv";
   private static final String DEFAULT_UNKNOWN = "pyroprints.csv";
   private static final int DEFAULT_LINE_NUM = 1;
   private static final int MAX_PHEIGHT_VALS = 103;
   private static ArrayList<Pyroprint> pyroData;
   private static Pyroprint unknownPyro;
   private static ArrayList<Distance> pearsonCorrelation;

   public static void main(String[] args)
   throws FileNotFoundException{
      String datasetFilename;
      String unknownFilename;
      int unknownLine = 0;
      pyroData = new ArrayList<Pyroprint>();
      if(args.length == 1){
         datasetFilename = args[0];
         unknownFilename = DEFAULT_UNKNOWN;
         unknownLine = DEFAULT_LINE_NUM;
      }
      else if(args.length == 2){
         datasetFilename = args[0];
         unknownFilename = args[1]; 
         unknownLine = DEFAULT_LINE_NUM;
      }
      else if(args.length == 3){
         datasetFilename = args[0];
         unknownFilename = args[1]; 
         unknownLine = Integer.parseInt(args[2]);
      }
      else{
         datasetFilename = DEFAULT_DATASET;
         unknownFilename = DEFAULT_UNKNOWN;
         unknownLine = DEFAULT_LINE_NUM;
         System.out.println("using default");
      }

      unknownPyro = readUnknown(unknownFilename, unknownLine);
      System.out.println("-----");
      System.out.println("unknown: " + unknownPyro.toString());
      System.out.println("-----");
      readDataSet(datasetFilename);
      calculateDistances();
      System.out.println("Sorting...");
      Collections.sort(pearsonCorrelation);
      for(int corrLoc = 0; corrLoc < pearsonCorrelation.size(); corrLoc++){
         System.out.println((pearsonCorrelation.size()-corrLoc - 1) + ". " + pearsonCorrelation.get(corrLoc).toString());
      }
      System.out.println("-----");
      System.out.println("unknown: " + unknownPyro.toString());
      System.out.println("-----");
   }
   public static void calculateDistances(){//==>
      int dataSetLoc = -1;
      pearsonCorrelation = new ArrayList<Distance>();
      System.out.println("Calculating distances...");
      while(++dataSetLoc < pyroData.size()){
        pearsonCorrelation.add(new Distance(unknownPyro.pearsonDist(pyroData.get(dataSetLoc)),pyroData.get(dataSetLoc)));
      }
   }//<==
   /**
    * Retrieves input from the user via System.in for an existing file and
    * returns a Scanner object of a successfully opened file.
    *
    * @param String goal, the type of file requested
    * @param String defaultName, the default filename, for ease of testing
    *
    * @return Scanner of the File requested by the user
    */
   public static Scanner getFileScanner(String goal, String fileName)//==>
   throws FileNotFoundException{
      while(true){
         System.out.println("Opening " + goal + " " + fileName + "...");
         return new Scanner(new File(fileName)).useDelimiter(",");
      }
   }//<==

   /**
    * Reads a .csv file with a header as follows:
    *
    * PyroId,IsolateId,CommonName,AppliedRegion,pHyyeight(0),...,pHeight(103)
    *
    * and reads the first entry, assuming that it is the sole entry.
    *
    * @return Pyroprint object of the read pyroprint
    */
   public static Pyroprint readUnknown(String filename, int lineNum)//==>
   throws FileNotFoundException{
      String dataHeader;            //May not ever use this, but want it anyway
      int pyroId;
      String isolateId;
      String commonName;
      String appliedRegion;
      double[] pHeightTemp = null;  //ArrayList implementation would 
      int numPHeights = 0;          //remove necessity for numPHeights
      double[] pHeight = null;
      Scanner inputFile = getFileScanner("unknown", filename);
      System.out.println("Reading unknown " + filename + "...");

      dataHeader = inputFile.nextLine();

      /* Scrolls through the lines in the file to get to the desired unknown
       * data line.
       */
      while(--lineNum > 1){
         inputFile.nextLine();
      }
      /* Reads the metadata for each pyroprint entry
       */
      pyroId = inputFile.nextInt();
      isolateId = inputFile.next();
      commonName = inputFile.next();
      appliedRegion = inputFile.next();

      /* Temporarily reads in pHeight data and tracks how many values
       * the input file provides, then creates a new array of the
       * appropriate size and copies into the array that is used to build
       * the Pyroprint object.
       */
      numPHeights = 0;
      pHeightTemp = new double[MAX_PHEIGHT_VALS];
      while(inputFile.hasNextDouble()){
         pHeightTemp[numPHeights] = inputFile.nextDouble();
         numPHeights++;
      }
      pHeight = new double[numPHeights];
      for(int arrCopy = 0; arrCopy < pHeight.length; arrCopy++){
         pHeight[arrCopy] = pHeightTemp[arrCopy];
      }
      inputFile.close();
      /* Builds the pyroPrint data structure.
       */
      return new Pyroprint(pyroId, isolateId, commonName, appliedRegion, pHeight);
   }//<==

   /**
    * Requests input from the console for the filename of a .csv file
    * containing pyroprint data in this format:
    *
    * PyroId,IsolateId,CommonName,AppliedRegion,pHyyeight(0),...,pHeight(103)
    *
    * and adds a new PyroPrint object to the data structure.
    */
   public static void readDataSet(String filename)//==>
   throws FileNotFoundException{
      String dataHeader;            //May not ever use this, but want it anyway
      int pyroId;
      String isolateId;
      String commonName;
      String appliedRegion;
      double[] pHeightTemp = null;  //ArrayList implementation would 
      int numPHeights = 0;          //remove necessity for numPHeights
      double[] pHeight = null;
      Scanner inputFile = getFileScanner("dataset",filename);
      System.out.println("Reading " + filename + "...");

      dataHeader = inputFile.nextLine();
      while(inputFile.hasNext()){

         /* Reads the metadata for each pyroprint entry
          */
         pyroId = inputFile.nextInt();
         isolateId = inputFile.next();
         commonName = inputFile.next();
         appliedRegion = inputFile.next();

         /* Temporarily reads in pHeight data and tracks how many values
          * the input file provides, then creates a new array of the
          * appropriate size and copies into the array that is used to build
          * the Pyroprint object.
          */
         numPHeights = 0;
         pHeightTemp = new double[MAX_PHEIGHT_VALS];
         while(inputFile.hasNextDouble()){
            pHeightTemp[numPHeights] = inputFile.nextDouble();
            numPHeights++;
         }
         pHeight = new double[numPHeights];
         for(int arrCopy = 0; arrCopy < pHeight.length; arrCopy++){
            pHeight[arrCopy] = pHeightTemp[arrCopy];
         }
         /*GARBAGE I DONT WANT TO DELETE*/ //==>
//            for(int arrLoc = 0; arrLoc < pHeight.length && inputFile.hasNextDouble(); arrLoc++){
//               System.out.print("[" + arrLoc  +"]=");
//               pHeight[arrLoc] = inputFile.nextDouble();
//               System.out.print(pHeight[arrLoc]);
//            }
//            //DEBUG
//            if(pHeight[pHeight.length-1] == 0.0){
//               System.out.println(pyroId + " has zeros at the end: ");
//               for(int zeroLoc = pHeight.length-1;pHeight[zeroLoc] != 0 ;zeroLoc--){
//                  System.out.print("[" + zeroLoc + "]=" + pHeight[zeroLoc] + " ");
//               }
//            }
//            System.out.print("# pHeights = " +numPHeights + " for: ");
//<==

         /* Builds the pyroPrint data structure.
          */
         try{
            pyroData.add(new Pyroprint(pyroId, isolateId, commonName, appliedRegion, pHeight));
         }
         catch(java.lang.ArrayIndexOutOfBoundsException e){
            System.out.println("Print of size " + numPHeights + " read instead of " + Pyroprint.getRelevantVals() 
                  + ": " + pyroId + " " + commonName);
         }
         inputFile.nextLine();
      }
      inputFile.close();
   }//<==
}
