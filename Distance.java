public class Distance
   implements Comparable<Distance>{
   private double distance;
   private Pyroprint print;
   public Distance(double distance, Pyroprint print){
      this.distance = distance;
      this.print = print;
   }
   public int compareTo(Distance other){
      if(this.distance < other.distance) return -1;
      if(this.distance > other.distance) return 1;
      return 0;
   }
   public String toString(){
      return this.distance + ": " + this.print.getCommonName() + "(" + this.print.getPyroId() + ")";
   }

}
