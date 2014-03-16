public class Pyroprint{
   private int pyroId;
   private String isolateId;
   private String commonName;
   private String appliedRegion;
   double[] pHeight;

   public Pyroprint(int pyroId, 
         String isolateId, 
         String commonName, 
         String appliedRegion,
         double[] pHeight){
      this.pyroId = pyroId;
      this.isolateId = isolateId;
      this.commonName = commonName;
      this.appliedRegion = appliedRegion;
      this.pHeight = new double[pHeight.length];
      for(int arrLoc = 0; arrLoc < pHeight.length; arrLoc++){
         this.pHeight[arrLoc] = pHeight[arrLoc];
      }
   }
   //PYROPRINT STUFFS //==>
   public int getPyroId(){
      return pyroId;
   }
   public String getIsolateId(){
      return isolateId;
   }
   public String getCommonName(){
      return commonName;
   }
   public void setCommonName(String commonName){
      this.commonName = commonName;
   }
   public String getAppliedRegion(){
      return appliedRegion;
   }
   public double[] getPHeight(){
      return pHeight;
   }
   public int getNumPHeights(){
      return pHeight.length;
   }
   public String toString(){
      return this.pyroId + ":" + this.isolateId + ":" + this.commonName +":" + this.appliedRegion + ":[" + pHeight.length + "]";
   }
   public boolean equals(Object other){
      if(other == null) return false;
      if(this.getClass() != other.getClass()) return false;
      if(this.pyroId != ((Pyroprint)other).pyroId) return false;
      if(!this.appliedRegion.equals(((Pyroprint)other).appliedRegion)) 
         return false;
      return true;
   }//<==
   //MATHS
   public double pearsonDist(Pyroprint other){
      if(this.pHeight.length != other.pHeight.length){
         throw new IncomparableVectorException("Vectors are not equal in length. this: " 
               + this.pHeight.length + ". other: " + other.pHeight.length);
      }
      double thisVariance;
      double otherVariance;
      

      return 0.0;
   }
   //PRIVATE HELPERS
   /**
    * Calculates the MEAN of the doubles values in a given array.
    *
    * @param double array containing values to calculate upon
    * @return double type mean of the given data
    */
   private double mean(double[] values){//==>
      double mean = 0;
      for(int arrLoc = 0; arrLoc < this.pHeight.length; arrLoc++){
         mean += values[arrLoc];
      }
      mean /= values.length;
      return mean;
   }//<==
   private double covariance(double[] x, double[] y){
      if(x.length != y.length){
         throw new IncomparableVectorException("Vectors are not equal in length. x: " 
               +x.length + ". y: " + y.length);
      }
      double covariance = 0;
      double xMean;
      double yMean;
      
      return covariance;
   }
   /**
    * Calculates the variance of the double values in a given array. Variance
    * here is defined as the average square of the difference between each
    * value and the mean.
    *
    * @param double array containing values to calculate upon
    * @return double type variance of the given data
    */
   private double variance(double[] values){//==>
      double variance = 0;
      double mean = mean(values);
      for(int arrLoc = 0; arrLoc < values.length; arrLoc++){
         variance += (values[arrLoc]-mean)*(values[arrLoc]-mean);
      }
      variance /= values.length;

      return variance;
   }//<==
   /**
    * IncomparableVectorException is a RuntimeException class made for throwing
    * the program when two vectors cannot be compared, usually for the
    * difference in length.
    */
   public class IncomparableVectorException//==>
         extends RuntimeException{
         public IncomparableVectorException(){
         }
         public IncomparableVectorException(String msg){
            super(msg);
         }
   }//<==
}
