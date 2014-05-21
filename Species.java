public class Species
   implements Comparable<Species>{
   private String commonName;
   private int count;
   private int highestPosition;
   private int positionSum;

   public Species(String commonName, int position){
      this.commonName = commonName;
      this.count = 1;
      this.highestPosition = position;
      this.positionSum  = position;
   }
   public int getCount(){
      return count;
   }
   public String getCommonName(){
      return commonName;
   }
   public double getAveragePosition(){
      return 1.0*this.positionSum/this.count;
   }
   public boolean addPosition(int position){
      ++this.count;
      this.positionSum += position;
      if(position > highestPosition){
         this.highestPosition = position;
         return true;
      }
      return false;
   }
   public int compareTo(Species other){
      if(this.count < other.count) return 1;
      if(this.count > other.count) return -1;
      if((this.getAveragePosition()) < (other.getAveragePosition()))
         return 1;
      if((this.getAveragePosition()) > (other.getAveragePosition()))
         return -1;
//      if(this.highestPosition < other.highestPosition) return 1;
//      if(this.highestPosition > other.highestPosition) return -1;
      return 0;
   }
   public String toString(){
      return commonName;
   }
   public boolean equals(Object other){
      if (other == null){
         return false;
      }
      if (this.getClass() != other.getClass()) {
         return false;
      }
      return this.commonName.equals(((Species)other).getCommonName());
   }
}
