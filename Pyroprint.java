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
         throw new IncomparableVectorException("Vectors are not equal in length. this[ " 
               +this.pHeight.length + "] other[" + other.pHeight.length + "]");
      }
      double covariance = covariance(this.pHeight,other.pHeight);
      double thisVariance = variance(this.pHeight);
      double otherVariance = variance(other.pHeight);

      return covariance/(thisVariance*otherVariance);
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
      for(int dataLoc = 0; dataLoc < this.pHeight.length; dataLoc++){
         mean += values[dataLoc];
      }
      mean /= values.length;
      return mean;
   }//<==

   /**
    * Calculates the covariance betwen two double arrays. For now, it chooses
    * the length of the x array to calculate over. In the future, we may need
    * to implement something to consider the smaller length and calculate over
    * only that one.
    */
   private double covariance(double[] x, double[] y){//==>
      if(x.length != y.length){
         throw new IncomparableVectorException("Vectors are not equal in length. x[ " 
               +x.length + "] y[" + y.length + "]");
      }
      double covariance = 0;
      double xMean = mean(x);
      double yMean = mean(y);
      double dataPoints = x.length;     //TODO may need to change to the min value
      for(int dataLoc = 0; dataLoc < dataPoints; dataLoc++){
         covariance += (x[dataLoc] - xMean)*(y[dataLoc] - yMean);
      }
      covariance /= dataPoints;
      
      return covariance;
   }//<==

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
      for(int dataLoc = 0; dataLoc < values.length; dataLoc++){
         variance += (values[dataLoc]-mean)*(values[dataLoc]-mean);
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
