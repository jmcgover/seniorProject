public class Distance
   implements Comparable<Distance>, java.io.Serializable{
   private double distance;
   private Pyroprint print;
   public Distance(double distance, Pyroprint print){
      this.distance = distance;
      this.print = print;
   }
   public int compareTo(Distance other){
      if(this.distance < other.distance) return 1;
      if(this.distance > other.distance) return -1;
      return 0;
   }
   public String getCommonName(){
      return this.print.getCommonName();
   }
   public double getDistance(){
      return this.distance;
   }
   public String toString(){
      return String.format("%.5f: %s(%s)",this.distance,this.print.getCommonName(),this.print.getPyroId());
   }

}
