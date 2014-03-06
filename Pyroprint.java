public class Pyroprint{
   int pyroId;
   String isolateId;
   String commonName;
   String appliedRegion;
   double[] pHeight;

   public Pyroprint(){
      pHeight = new double[103];
   }
   
   public int getPyroId(){
      return pyroId;
   }
   public String getIsolateId(){
      return isolateId;
   }
   public String getCommonName(){
      return commonName;
   }
   public String getAppliedRegion(){
      return appliedRegion;
   }
   public double[] getPHeight(){
      return pHeight;
   }
   public String toString(Object other){
      return pyroId + " " + commonName;
   }
   public boolean equals(Object other){
      if(other == null) return false;
      if(this.getClass() != other.getClass()) return false;
      if(this.pyroId != ((Pyroprint)other).pyroId) return false;
      if(!this.appliedRegion.equals(((Pyroprint)other).appliedRegion)) return false;
      return true;
   }
}
