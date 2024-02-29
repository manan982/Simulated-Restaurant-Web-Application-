package uk.ac.warwick.cs126.structures;
import uk.ac.warwick.cs126.structures.KeyValuePairLinkedList;

// Source for this - Based off my existing work in CS126 Lab 4. Taken from that
public class HashMap<K extends Comparable<K>,V> {

        protected KeyValuePairLinkedList[] table;
        
        public HashMap() {
            /* for very simple hashing, primes reduce collisions */
            this(11);
        }
        
        public HashMap(int magnitude) {
            table = new KeyValuePairLinkedList[magnitude];
            initTable();
        }
    
        public int find(K key) {
            //Number of comparisons are recorded and returned. Count no of times needed to compare to find element using Linear Search
            int location = hash(key) % table.length;
            int countOfComparisons = 1;
    
            ListElement<KeyValuePair<K,V>> pointer = table[location].getHead();
    
            while (pointer != null) {
                if (pointer.getValue().getKey().equals(key)) {
                    return countOfComparisons;
                }
    
                pointer = pointer.getNext();
                countOfComparisons++;
            }
     
            return -1;
    
        }
        
        protected void initTable() {
            for(int i = 0; i < table.length; i++) {
                table[i] = new KeyValuePairLinkedList<>();
            }
        }
        
        protected int hash(K key) {
            int code = key.hashCode();
            return code;    
        }
    
        public void add(K key, V value) {
            int hash_code = hash(key);
            int location = hash_code % table.length;
            
            System.out.println("Adding " + value + " under key " + key + " at location " + location);
            
            table[location].add(key,value);
        }
    
        public V get(K key) {
            int hash_code = hash(key);
            int location = hash_code % table.length;
            
            return (V)table[location].get(key).getValue();
        }

        
    }


