import java.io.*;
import java.lang.*;
import java.util.*;

public class CalcDists{

   private static final String DEFAULT_DATASET = "pyroprints.csv";
   private static final String DEFAULT_UNKNOWN = "pyroprints.csv";
   private static final String ENCLOSING_FOLDER = "/Users/Jeff/School/2014SenProj/calculated/";
   private static final String FILE_EXT= ".data";
   private static final int DEFAULT_LINE_NUM = 1;
   private static final int LAST_LINE = 6588;
   private static final int MAX_PHEIGHT_VALS = 103;

   private static ArrayList<Pyroprint> pyroData;
   private static Pyroprint unknownPyro;
   private static ArrayList<Distance> pearsonCorrelation;

   public static void main(String[] args)
      throws FileNotFoundException, IOException, ClassNotFoundException{
      String datasetFilename = "";
      String unknownFilename = "";
      String dataFilename = "";
      int unknownLine = 0;
      int lastLine = 0;
      long beginTime = 0;
      long beginCalc = 0;
      long intermedTime = 0;
      ArrayList<Distance> fromFile;

      pyroData = new ArrayList<Pyroprint>();

      if(args.length == 2){
         datasetFilename = args[0];
         unknownFilename = args[1]; 
         lastLine = LAST_LINE; 
      }
      if(args.length == 4){
         datasetFilename = args[0];
         unknownFilename = args[1]; 
         unknownLine = Integer.parseInt(args[2]);
         lastLine = Integer.parseInt(args[3]);
      }
      else{
         System.out.println("usage: java CalcDists <datasetFilename.csv>" + 
               " <unknownPrintFileName.csv> <beginLineNumber> <lastLineNumber>");
      }


      beginTime = System.nanoTime();
      beginCalc = System.nanoTime();
      readDataSet(datasetFilename);
      System.out.println("PARSE Time: " + ((System.nanoTime() - beginCalc)*0.000000001) + "s");
      for( ; unknownLine <= lastLine; unknownLine++){
         System.out.println("-----");
         beginCalc = System.nanoTime();
         try{
            unknownPyro = readUnknown(unknownFilename, unknownLine);
            System.out.println(unknownLine + ". beginning unknown: " + unknownPyro.toString());

            intermedTime = System.nanoTime();
            calculateDistances();
            System.out.println("CALC Time: " + ((System.nanoTime() - intermedTime)*0.000000001) + "s");

            intermedTime = System.nanoTime();
            Collections.sort(pearsonCorrelation);
            System.out.println("SORT Time: " + ((System.nanoTime() - intermedTime)*0.000000001) + "s");

            dataFilename = ENCLOSING_FOLDER + unknownLine + FILE_EXT;
            intermedTime = System.nanoTime();
            writeToFile(dataFilename);
            System.out.println("WRITE Time: " + ((System.nanoTime() - intermedTime)*0.000000001) + "s");
            intermedTime = System.nanoTime();
            fromFile = readFromFile(dataFilename);
            System.out.println("READ Time: " + ((System.nanoTime() - intermedTime)*0.000000001) + "s");

            System.out.println(unknownLine + ". ending unknown: " + fromFile.get(0).toString());
         }
         catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Invalid data at line: " + unknownLine);
         }
         finally{
            System.out.println("TOTAL Time: " + ((System.nanoTime() - beginCalc)*0.000000001) + "s");
         }

      }
      System.out.println("~~~~~");
      System.out.println("Finished calculations.");
      System.out.println("FINAL Time: " + ((System.nanoTime() - beginTime)*0.000000001) + "s");
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
      int pyroLine = lineNum;
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
      return new Pyroprint(pyroLine,pyroId, isolateId, commonName, appliedRegion, pHeight);
   }//<==

   /**
    * Requests input from the console for the filename of a .csv file
    * containing pyroprint data in this format:
    *
    * PyroId,IsolateId,CommonName,AppliedRegion,pHeight(0),...,pHeight(103)
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

      dataHeader = inputFile.nextLine();
      lines++;
      while(inputFile.hasNext()){
         lines++;

         /* Reads the metadata for each pyroprint entry
         */
         pyroId = inputFile.nextInt();
         isolateId = inputFile.next();
         commonName = inputFile.next();
         appliedRegion = inputFile.next();

         /* Vestiges of the previous file. Not sure how I can get a
          * speciesTable to happen across serialized files. We'll see if it's
          * even necessary.
          */
         //         count(commonName, speciesTable);

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
            pyroData.add(new Pyroprint(lines, pyroId, isolateId, commonName, appliedRegion, pHeight));
         }
         catch(java.lang.ArrayIndexOutOfBoundsException e){
            System.out.println(String.format("Line %d: size %d read (needed %d): %d %s",
                    lines, numPHeights,Pyroprint.getRelevantVals(),pyroId,commonName));
            lines++;
         }
         finally{
            inputFile.nextLine();
         }
      }
      inputFile.close();
   }//<==

   public static void writeToFile(String filename) //==>
      throws FileNotFoundException, IOException{
      FileOutputStream fileOut = new FileOutputStream(filename);
      ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

      System.out.println("Trimming...");
      pearsonCorrelation.trimToSize();
      System.out.println("Writing to " + filename + "...");
      objectOut.writeObject(pearsonCorrelation);
      fileOut.close();
   }
   public static ArrayList<Distance> readFromFile(String filename)
      throws FileNotFoundException, IOException, ClassNotFoundException{
      FileInputStream fileIn = new FileInputStream(filename);
      ObjectInputStream objectIn = new ObjectInputStream(fileIn);
      System.out.println("Reading from " + filename + "...");
      Object obj = objectIn.readObject();
      fileIn.close();

      ArrayList<Distance> returnArray = null;
      if(obj instanceof ArrayList){
         returnArray = (ArrayList<Distance>) obj;
      }
      return returnArray;
   }//<==

   public static void printAll(ArrayList<Distance> list){//==>
      for(int corrLoc = 0; corrLoc < list.size(); corrLoc++){
         System.out.println((corrLoc) + ". " 
               + list.get(corrLoc).toString());
      }
   }
   public static void printAllReverse(ArrayList<Distance> list){
      for(int corrLoc = 0; corrLoc < list.size(); corrLoc++){
         System.out.println((list.size()-corrLoc - 1) + ". " 
               + list.get(list.size()-corrLoc-1).toString());
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

}
