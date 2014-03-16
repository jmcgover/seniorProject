import java.io.*;
import java.lang.*;
import java.util.*;

public class Data{

   /* private Instance variables
    */
   private static final String DEFAULT_FILENAME = "pyroprints.csv";
   private static final int MAX_PHEIGHT_VALS = 103;
   private static ArrayList<Pyroprint> pyroData = new ArrayList<Pyroprint>();

   public static void main(String[] args){
      readData();
   }

   /**
    * Requests input from the console for the filename of a .csv file
    * containing pyroprint data in this format:
    *
    * PyroId,IsolateId,CommonName,AppliedRegion,pHeight(0),...,pHeight(103)
    *
    * and adds a new PyroPrint object to the data structure.
    */
   public static void readData(){//==>
      String dataHeader;
      int pyroId;
      String isolateId;
      String commonName;
      String appliedRegion;
      double[] pHeightTemp = null;  //ArrayList implementation would 
      int numPHeights = 0;          //remove necessity for numPHeights
      double[] pHeight = null;

      Scanner input = new Scanner(System.in);
      Scanner inputFile = null;
      boolean getFileInput = true;

      while(getFileInput){
         try{
            String inFileName;
            System.out.print("filename (press RETURN for ' "+ DEFAULT_FILENAME  +"'): ");
            inFileName = input.nextLine();
            if(inFileName.isEmpty()){
               inFileName = DEFAULT_FILENAME;
            }
            inputFile = new Scanner(new File(inFileName)).useDelimiter(",");
            getFileInput = false;
         }
         catch(FileNotFoundException e){
            System.out.println("could not find file");
         }
      }
      dataHeader = inputFile.nextLine();

//      try{
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

            /* Builds the pyroPrint data structure.
             */
            pyroData.add(new Pyroprint(pyroId, isolateId, commonName, appliedRegion, pHeight));
            System.out.println((pyroData.get(pyroData.size()-1)).toString());
            inputFile.nextLine();
         }
   }//<==
}
