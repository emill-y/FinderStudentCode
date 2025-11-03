import java.io.BufferedReader;
import java.io.IOException;
/**
 * Finder
 * A puzzle written by Zach Blick
 * for Adventures in Algorithms
 * At Menlo School in Atherton, CA
 *
 * Completed by: Eisha Yadav
 **/


public class Finder {

    private static final String INVALID = "INVALID KEY";
    private Node[] table;
    // Set size to large prime number (used AI to generate number)
    private static final int TABLE_SIZE = 100003;

    // Initialize Table
    public Finder() {
        table = new Node[TABLE_SIZE];
    }

    // Read csv input files
    public void buildTable(BufferedReader br, int keyCol, int valCol) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            String[] elements = line.split(",");
            if (elements.length > Math.max(keyCol, valCol)) {
                String key = elements[keyCol].trim();
                String value = elements[valCol].trim();
                // Insert Key Value Pair into Hash Data Structure
                put(key, value);
            }
        }
        br.close();
    }

    // Retrieve Value Associated With the Key
    public String query(String key){
        return get(key);
    }

    // Hash function
    private int hash(String key) {
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            // Very simple hash function -- not the polynomial rolling hash yet
            hash = (31 * hash + key.charAt(i)) % TABLE_SIZE;
        }
        return Math.abs(hash);
    }

    // Insert Key Value pairs into tab;e
    private void put(String key, String value) {
        int index = hash(key);
        Node head = table[index];
        // Puts the value of the key at the nodes of the tree with the key
        for (Node curr = head; curr != null; curr = curr.next) {
            if (curr.key.equals(key)) {
                curr.value = value;
                return;
            }
        }
        // Sets next node to the key value pair
        // Connects new node to current node
        Node newNode = new Node(key, value);
        newNode.next = head;
        table[index] = newNode;
    }

    // Retrieve Values from table
    private String get(String key) {
        int index = hash(key);
        Node curr = table[index];
        // Continue to search for key in the graph until found
        while (curr != null) {
            if (curr.key.equals(key)) {
                // Return value
                return curr.value;
            }
            // Move through the graph
            curr = curr.next;
        }
        return INVALID;
    }

}

