import java.io.*;
import java.lang.*;
import java.util.*;

public class Experiment{
   private static final String ENCLOSING_FOLDER = "/Users/Jeff/School/2014SenProj/calculated/";
   private static final String FILE_EXT= ".data";
   private static final int DEFAULT_K = 17;
   private static final double DEFAULT_ALPHA = 1;
   private static final String filter[] = {
      "SLO Creek Water","Pennington Creek Water",
      "Hu Cw and Dg","Hu and Cw","Hu and Dg","Cw and Dg",
      "Human"};
   private static  HashSet hashFilter; 

   private static Pyroprint unknownPyro;
   public static void main(String[] args)
      throws FileNotFoundException, IOException, ClassNotFoundException{
      int unknownLine = 0;
      int endLine = 0;
      int k = 0;
      int successes = 0;
      int failures = 0;
      double alpha = 1;
      String dataFileName;
      ArrayList<Distance> calcDists;
      hashFilter = new HashSet();
      for(int ndx = 0; ndx < filter.length; ndx++){
         hashFilter.add(filter[ndx]);
      }

      if(args.length == 1){
         unknownLine = Integer.parseInt(args[0]);
         endLine = unknownLine; 
         k = DEFAULT_K;
         alpha = DEFAULT_ALPHA;
      }
      else if(args.length == 2){
         unknownLine = Integer.parseInt(args[0]);
         endLine = Integer.parseInt(args[1]);
         k = DEFAULT_K;
         alpha = DEFAULT_ALPHA;
      }
      else if(args.length == 3){
         unknownLine = Integer.parseInt(args[0]);
         endLine = Integer.parseInt(args[1]);
         k = Integer.parseInt(args[2]);
         alpha = DEFAULT_ALPHA;
      }
      else if(args.length == 4){
         unknownLine = Integer.parseInt(args[0]);
         endLine = Integer.parseInt(args[1]);
         k = Integer.parseInt(args[2]);
         alpha = Double.parseDouble(args[3]);
      }
      else{
         System.out.println("usage: java Experiment <begin> <end> <k value> <alpha>");
      }
      System.out.println("begin: " + unknownLine +" end: "+ endLine +" k:" + k + " alpha:" + alpha);

      for(;unknownLine <= endLine; unknownLine++){
         dataFileName = ENCLOSING_FOLDER + unknownLine + FILE_EXT;
         calcDists = readFromFile(dataFileName);
         System.out.println("Top " + k + " (unfiltered): ");
         printTopK(k,calcDists);
         System.out.println(String.format("Beginning experiment for %s...",calcDists.get(0).getCommonName()));
         if(countTopK(k,alpha,calcDists)){
            successes++;
            System.out.println("Line " + unknownLine);
            System.out.println("--------------");
            System.out.println("Woo! SUCCESS!!");
         }
         else{
            failures++;
            System.out.println("Line " + unknownLine);
            System.out.println("--------------");
            System.out.println("FAILURE");
         }
         System.out.println("--------------");
      }
      System.out.println(String.format("%.2f percent right, as usual.",100*successes/(1.0*successes+failures)));
   }

   public static void count(String thing, HashMap<String,Integer> table){//==>
      Integer count = table.get(thing);
      table.put(thing,(count == null) ? 1 : count + 1);
   }//<==
   public static void addSpecies(String species, int position, HashMap<String,Species> table) {//==>
      if (table.containsKey(species)){
         table.get(species).addPosition(position);
      } else {
         table.put(species, new Species(species,position));
      }
   }//<==
   public static void printTopK(int k, ArrayList<Distance> pearsonCorrelation){//==>
      Distance current;
      for(int topKLoc = 0; topKLoc <= k; topKLoc++){
         current = pearsonCorrelation.get(topKLoc);
         System.out.println(String.format("%2d. %.6f: %s",topKLoc,current.getDistance(),current.getCommonName()));
      }
   }//<==
   public static boolean countTopK(int k, double alpha, ArrayList<Distance> pearsonCorrelation){//==>
      Distance current;
      Species topCurrent;
      HashMap<String,Species> topKTable = new HashMap<String,Species>();

      System.out.println("Top " + k + " (filtered):");

      current = pearsonCorrelation.get(1);
      for(int topKLoc = 1; topKLoc <= k && alpha > (1-current.getDistance()) ; topKLoc++){
         while(hashFilter.contains(pearsonCorrelation.get(topKLoc).getCommonName())){
//            System.out.println("Filtered: " + pearsonCorrelation.get(topKLoc).getCommonName());
            pearsonCorrelation.remove(topKLoc);
         }
         if(alpha > (1-current.getDistance())){
            current = pearsonCorrelation.get(topKLoc);
            System.out.println(String.format("%2d. %.6f: %s",topKLoc,current.getDistance(),current.getCommonName()));
            addSpecies(current.getCommonName(),topKLoc,topKTable);
         }
      }

      ArrayList<Species> topKList = new ArrayList<Species>();
      topKList.addAll(topKTable.values());
      Collections.sort(topKList);

      System.out.println("Rnk  # avPos: Common Name");
      System.out.println("--------------------------");
      for(int listLoc = 0; listLoc < topKList.size(); listLoc++){
         topCurrent = topKList.get(listLoc);
         System.out.println(String.format("%2d. %2d %2.3f: %s",listLoc+1,topCurrent.getCount(),
                  topCurrent.getAveragePosition(),topCurrent.getCommonName()));
      }
      if(topKList.size() > 0){
         System.out.println(
               pearsonCorrelation.get(0).getCommonName() 
               + " was classified as " 
               + topKList.get(0).getCommonName());
         return pearsonCorrelation.get(0).getCommonName().equals(topKList.get(0).getCommonName());
      }
      else{
         System.out.println("Nothing in top" + k + " matches.");
         return false;
      }
   }//<==

//   public static void printSpeciesCount(){//==>
//      System.out.println("Species Table:");
//      System.out.println("-------------");
//      for (Map.Entry<String, Integer> entry : speciesTable.entrySet()) {
//         System.out.println(String.format("%4d: %s",entry.getValue(),entry.getKey()));
//      }
//      System.out.println("-------------");
//   }//<==
   public static void writeToFile(String filename, ArrayList<Distance> list) //==>
      throws FileNotFoundException, IOException{
      FileOutputStream fileOut = new FileOutputStream(filename);
      ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

      System.out.println("Trimming...");
      list.trimToSize();
      System.out.println("Writing to " + filename + "...");
      objectOut.writeObject(list);
      fileOut.close();
   }//<==
   public static ArrayList<Distance> readFromFile(String filename)//==>
      throws FileNotFoundException, IOException, ClassNotFoundException{
      FileInputStream fileIn = new FileInputStream(filename);
      ObjectInputStream objectIn = new ObjectInputStream(fileIn);
      System.out.println("Reading from " + filename + "...");
      Object obj = objectIn.readObject();

//      System.out.println("Casting...");
      ArrayList<Distance> returnArray = null;
      if(obj instanceof ArrayList){
         returnArray = (ArrayList<Distance>) obj;
      }
      fileIn.close();
      return returnArray;
   }//<==

}
