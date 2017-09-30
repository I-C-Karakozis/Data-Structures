/******************************************************************************
  *  Author:  Ioannis Christos Karakozis, Princeton '19
  *  Date: 9/29/2017 - 9/30/2017
  *  Description: A fixed size hash map hat associates string keys with arbitrary 
  *  data object references
  * 
  *  Compilation command: javac FixedCapacityHashMap.java
  *  Test Suite Execution command: java FixedCapacityHashMap
  * 
  *  Coded in:
  *  java version "1.7.0_67"
  *  Java(TM) SE Runtime Environment (build 1.7.0_67-b01)
  *  Java HotSpot(TM) 64-Bit Server VM (build 24.65-b04, mixed mode)
  * 
  ******************************************************************************/
import java.lang.Math; // methods are built-in in python

public class FixedCapacityHashMap<Value> {
  private int n; // number of key-value pairs stored
  private String[][] st;  // array of arrays storing keys
  private Value[][] vt;  // array of arrays storing values
  
  public FixedCapacityHashMap(int capacity) {
    // probabilistic guarantee of max number of solutions being Math.sqrt(n); n = number of elements inserted
    // probability of 1 collision for n buckets is 0.5 if number of elements inserted is sqrt(2ln2 * n)
    // from binomial distribution, current configuration guarantees the probability that the number of collisions
    // in a bucket will exceed log base 2 of n is less than 1 * 10e-6 for large n
    // follows from birthday paradox
    
    // set minCapacity for small inputs arbitrarily to avoid collisions due to high variance; 
    // ensure n is large enough such that variance is low (follows from Central Limit Theorem)
    int minCapacity = 65; // (2 ^ 6 + 1)
    if (minCapacity > capacity) capacity = minCapacity;
    
     // Performance: O(Nlogn) (n = max capacity of hashmap)
    st = new String [2 * capacity][logTwo(capacity)]; 
    vt = (Value[][]) new Object [st.length][st[0].length];
    this.n = 0;
  } 
  
  // compute log base 2 of x
  private int logTwo(int x) { return ((int) (Math.log(x) / Math.log(2)) + 1); }
  
  // hash value between 0 and capacity-1
  private int hash(String key) { return (key.hashCode() & 0x7fffffff) % st.length;} 
  
  // Returns the load factor (items in hash map)/(size of hash map) of the data structure
  // Performance: O(1)
  public double load() { 
    System.out.println(st.length);
    System.out.println(st[0].length);
    System.out.println(this.n);
    return this.n / ((double) (st.length * st[0].length)); 
  }   

  // Returns the value associated with the specified key in this hash map, or null if no value is set.
  // Performance: O(logn) (n = max capacity of hashmap)
  public Value get(String key) {
    if (key == null) throw new IllegalArgumentException("argument to get() is null");
    if (this.n == 0) return null;
    int hashedKey = hash(key);
    
    for (int i = 0; i < st[hashedKey].length; i++)
      if (key.equals(st[hashedKey][i])) return vt[hashedKey][i];     
    
    return null;
  } 
  
  // Stores the given key/value pair in the hash map. 
  // Returns a boolean value indicating success / failure of the operation.
  // Performance: O(logn) (n = max capacity of hashmap)
  public boolean set(String key, Value val) {
    if (key == null) throw new IllegalArgumentException("first argument to set() is null");
    if (this.n >= st.length / 2) return false; // max capacity reached
    
    int hashedKey = hash(key);
    
    // update existing element
    for (int i = 0; i < st[hashedKey].length; i++) {
      if (key.equals(st[hashedKey][i])) {
        vt[hashedKey][i] = val; 
        this.n++;
        return true;
      }
    }
    
    // add new element
    for (int i = 0; i < st[hashedKey].length; i++) {
      if (st[hashedKey][i] == null) {
        st[hashedKey][i] = key;
        vt[hashedKey][i] = val; 
        this.n++;
        return true;
      }
    }
    
    return false; // this line of code should never be reached, under probabilistic guarantees
  } 
  
  // Delete the value associated with the given key, returning the value on success or null if the key has no value.
  // Performance: O(logn) (n = max capacity of hashmap)
  public Value delete(String key) {
    if (key == null) throw new IllegalArgumentException("first argument to delete() is null");
    int hashedKey = hash(key);
    
    // delete existing element
    for (int i = 0; i < st[hashedKey].length; i++) {
      if (key.equals(st[hashedKey][i])) {
        Value value = vt[hashedKey][i];
        vt[hashedKey][i] = null; 
        st[hashedKey][i] = null;
        this.n--;
        return value;
      }
    }
    
    return null;
  } 
  
  // minimal testing suite
  public static void main(String[] args) {
    FixedCapacityHashMap<Integer> m = new FixedCapacityHashMap<Integer>(10);
    
    // expect null null 0.0
    System.out.println(m.get("hi"));
    System.out.println(m.delete("hi"));
    System.out.println(m.load());
    
    // expect true true true 0.0032967032967032967
    System.out.println(m.set("my", 609));
    System.out.println(m.set("phone", 712));
    System.out.println(m.set("number", 480));
    System.out.println(m.load());
    
    // expect null 609 712 480
    System.out.println(m.get("hi"));
    System.out.println(m.get("my"));
    System.out.println(m.get("phone"));
    System.out.println(m.get("number"));
    
    // expect 609 null null
    System.out.println(m.delete("my"));
    System.out.println(m.get("my"));
    System.out.println(m.delete("my"));
    
    // expect 0.002197802197802198 
    System.out.println(m.load());
  }  
}