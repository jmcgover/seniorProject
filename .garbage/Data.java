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
   private static final int CURRENT_K = 17;
   private static ArrayList<Pyroprint> pyroData;
   private static Pyroprint unknownPyro;
   private static ArrayList<Distance> pearsonCorrelation;
   private static HashMap<String, Integer> speciesTable;

   public static void main(String[] args)
   throws FileNotFoundException{
      String datasetFilename;
      String unknownFilename;
      int unknownLine = 0;
      boolean isCorrect;
      pyroData = new ArrayList<Pyroprint>();
      if(args.length == 1){
         datasetFilename = args[0];
         unknownFilename = DEFAULT_UNKNOWN;
         unknownLine = DEFAULT_LINE_NUM;
         System.out.println("Using default '" + unknownFilename 
               + "' and line " + unknownLine + " for unknown datum.");
      }
      else if(args.length == 2){
         datasetFilename = args[0];
         unknownFilename = args[1]; 
         unknownLine = DEFAULT_LINE_NUM;
         System.out.println("Using line " + unknownLine + " from '" 
               + unknownFilename + "' for unknown datum.");

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
         System.out.println("usage: java Data <datasetFilename.csv>" + 
               " <unknownPrintFileName.csv> <lineNumber>");
         System.out.println("using default: 'java Data " + DEFAULT_DATASET + " " 
               + DEFAULT_UNKNOWN + " " + DEFAULT_LINE_NUM + "'");
      }

      speciesTable = new HashMap<String, Integer>();
      unknownPyro = readUnknown(unknownFilename, unknownLine);
      System.out.println("-----");
      System.out.println("unknown: " + unknownPyro.toString());
      System.out.println("-----");
      readDataSet(datasetFilename);
      calculateDistances();
      System.out.println("Sorting...");
      Collections.sort(pearsonCorrelation);
//      printSpecies();
//      printAll();
      printSpeciesCount();
      isCorrect = countTopK(CURRENT_K);
      System.out.println("Is the top one right? ");
      System.out.println(isCorrect ? "yes":"no");
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
      System.out.println("Going to line " + lineNum + "...");
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
      int lines = 0;
      Scanner inputFile = getFileScanner("dataset",filename);
      System.out.println("Reading " + filename + "...");

      lines++;
      dataHeader = inputFile.nextLine();
      while(inputFile.hasNext()){
         lines++;

         /* Reads the metadata for each pyroprint entry
          */
         pyroId = inputFile.nextInt();
         isolateId = inputFile.next();
         commonName = inputFile.next();
         appliedRegion = inputFile.next();

         count(commonName, speciesTable);

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
         /* Builds the pyroPrint data structure.
          */
         try{
            pyroData.add(new Pyroprint(pyroId, isolateId, commonName, appliedRegion, pHeight));
         }
         catch(java.lang.ArrayIndexOutOfBoundsException e){
            System.out.println("Size " + numPHeights + " read instead of " + Pyroprint.getRelevantVals() 
                  + ": " + pyroId + " " + commonName);
         }
         inputFile.nextLine();
      }
      inputFile.close();
   }//<==
   
   public static void printAll(){//==>
      for(int corrLoc = 0; corrLoc < pearsonCorrelation.size(); corrLoc++){
         System.out.println((pearsonCorrelation.size()-corrLoc - 1) + ". " 
               + pearsonCorrelation.get(pearsonCorrelation.size()-corrLoc-1).toString());
      }
   }
   public static void printSpecies(){
      for(int corrLoc = 0; corrLoc < pearsonCorrelation.size(); corrLoc++){
         if(unknownPyro.getCommonName().equals(pearsonCorrelation.get(corrLoc).getCommonName())){
            System.out.println((pearsonCorrelation.size()-corrLoc - 1) + ". " 
                  + pearsonCorrelation.get(corrLoc).toString());
         }
      }
   }//<==

   public static void printTopK(int k){
      Distance current;
      for(int topKLoc = 0; topKLoc <= k; topKLoc++){
         current = pearsonCorrelation.get(topKLoc);
         System.out.println(String.format("%2d. %.6f: %s",topKLoc,current.getDistance(),current.getCommonName()));
      }
   }
   public static void count(String thing, HashMap<String,Integer> table){
      Integer count = table.get(thing);
      table.put(thing,(count == null) ? 1 : count + 1);
   }
   public static void addSpecies(String species, HashMap<String,Species> table) {
       if (table.containsKey(species)){
           table.get(species).increment();
       } else {
           table.put(species, new Species(species));
       }
   }
   public static boolean countTopK(int k){
      Distance current;
      Species topCurrent;
      HashMap<String,Species> topKTable = new HashMap<String,Species>();
      System.out.println("Top " + k + ":");
      for(int topKLoc = 1; topKLoc <= k; topKLoc++){
         current = pearsonCorrelation.get(topKLoc);
         System.out.println(String.format("%2d. %.6f: %s",topKLoc,current.getDistance(),current.getCommonName()));
         addSpecies(current.getCommonName(),topKTable);
      }

      ArrayList<Species> topKList = new ArrayList<Species>();
      topKList.addAll(topKTable.values());
      Collections.sort(topKList);
      for(int listLoc = 0; listLoc < topKList.size(); listLoc++){
         topCurrent = topKList.get(listLoc);
         System.out.println(String.format("%2d. %2d: %s",listLoc+1,topCurrent.getCount(),topCurrent.getSpecies()));
      }
      return unknownPyro.getCommonName().equals(topKList.get(0).getSpecies());

   }
   public static void printSpeciesCount(){
      System.out.println("Species Table:");
      System.out.println("-------------");
      for (Map.Entry<String, Integer> entry : speciesTable.entrySet()) {
         System.out.println(String.format("%4d: %s",entry.getValue(),entry.getKey()));
      }
      System.out.println("-------------");
   }
}
