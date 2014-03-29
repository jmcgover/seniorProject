import java.io.*;
import java.lang.*;
import java.util.*;

public class Data{

   /* private Instance variables
    */
   private static final String DEFAULT_DATASET = "pyroprints.csv";
   private static final String DEFAULT_UNKNOWN = "unknown.csv";
   private static final int MAX_PHEIGHT_VALS = 103;
   private static ArrayList<Pyroprint> pyroData;
   private static Pyroprint unknownPyro;
   private static ArrayList<Distance> pearsonCorrelation;

   public static void main(String[] args){
      pyroData = new ArrayList<Pyroprint>();
      readDataSet();
      unknownPyro = readSingular("unknown");
      System.out.println("unknown: " + unknownPyro.toString());
      calculateDistances();
      Collections.sort(pearsonCorrelation);
      for(int corrLoc = 0; corrLoc < pearsonCorrelation.size(); corrLoc++){
         System.out.println(corrLoc + ": " + pearsonCorrelation.get(corrLoc).toString());
      }
      System.out.println("unknown: " + unknownPyro.toString());
   }
   public static void calculateDistances(){//==>
      int dataSetLoc = -1;
      pearsonCorrelation = new ArrayList<Distance>();
      System.out.print("Caught unequal vectors: ");
      while(++dataSetLoc < pyroData.size()){
         try{
           pearsonCorrelation.add(new Distance(unknownPyro.pearsonDist(pyroData.get(dataSetLoc)),pyroData.get(dataSetLoc)));
         }
         catch(Pyroprint.IncomparableVectorException e){
//            System.out.println("size not equal to " + unknownPyro.getNumPHeights()  + ": " + pyroData.get(dataSetLoc).toString() );
            System.out.print(pyroData.get(dataSetLoc).getPyroId() + " " + pyroData.get(dataSetLoc).getNumPHeights()+ ", ");
         }
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
   public static Scanner getFileScanner(String goal, String defaultName){//==>
      Scanner input = new Scanner(System.in);
      while(true){
         try{
            String inFileName;
            System.out.print(goal + " filename (press RETURN for '" + defaultName  +"'): ");
//            inFileName = input.nextLine();
//            if(inFileName.isEmpty()){
//               return new Scanner(new File(defaultName)).useDelimiter(",");
//            }
//            return new Scanner(new File(inFileName)).useDelimiter(",");
            return new Scanner(new File(DEFAULT_DATASET)).useDelimiter(",");
         }
         catch(FileNotFoundException e){
            System.out.println("could not find file");
         }
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
   public static Pyroprint readSingular(String reason){//==>
      String dataHeader;            //May not ever use this, but want it anyway
      int pyroId;
      String isolateId;
      String commonName;
      String appliedRegion;
      double[] pHeightTemp = null;  //ArrayList implementation would 
      int numPHeights = 0;          //remove necessity for numPHeights
      double[] pHeight = null;
      Scanner inputFile = getFileScanner(reason, DEFAULT_DATASET);

      dataHeader = inputFile.nextLine();
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
   public static void readDataSet(){//==>
      String dataHeader;            //May not ever use this, but want it anyway
      int pyroId;
      String isolateId;
      String commonName;
      String appliedRegion;
      double[] pHeightTemp = null;  //ArrayList implementation would 
      int numPHeights = 0;          //remove necessity for numPHeights
      double[] pHeight = null;
      Scanner inputFile = getFileScanner("dataset",DEFAULT_DATASET);

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
         pyroData.add(new Pyroprint(pyroId, isolateId, commonName, appliedRegion, pHeight));
//         System.out.println((pyroData.get(pyroData.size()-1)).toString());
         inputFile.nextLine();
      }
      inputFile.close();
   }//<==
}
