import java.io.BufferedReader;
import java.io.IOException;

/**
 * Finder
 * A puzzle written by Zach Blick
 * for Adventures in Algorithms
 * At Menlo School in Atherton, CA
 *
 * Completed by: Eisha Yadav
 */

public class Finder {
    private static final String INVALID = "INVALID KEY";
    // Large Prime Number -> Sourced from ChatGPT
    private static final int DEFAULT_TABLE_SIZE = 100003;
    // Base for Polynomial Hashing
    private static final int R = 31;

    private int tableSize;
    // Number of Keys currently in table
    private int n;
    private String[] keys;
    private String[] values;

    public Finder() {
        this.tableSize = DEFAULT_TABLE_SIZE;
        this.keys = new String[tableSize];
        this.values = new String[tableSize];
        this.n = 0;
    }

    // Build the table from a CSV file
    public void buildTable(BufferedReader br, int keyCol, int valCol) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            String[] elements = line.split(",");
            if (elements.length > Math.max(keyCol, valCol)) {
                String key = elements[keyCol];
                String value = elements[valCol];
                add(key, value);
            }
        }
        br.close();
    }

    // Query the table
    public String query(String key) {
        return get(key);
    }

    // Polynomial rolling hash
    // Using Horners Method from Slides
    private int hash(String key) {
        long h = 0;
        for (int i = 0; i < key.length(); i++) {
            h = (h * R + key.charAt(i)) % tableSize;
        }
        return (int) h;
    }

    // Add key-value pair (linear probing)
    // Resize if table is halfway full
    private void add(String key, String value) {
        if ((double) n / tableSize >= 0.5) {
            resize();
        }

        int i = hash(key);
        while (keys[i] != null) {
            if (keys[i].equals(key)) {
                // Update the existing key
                values[i] = value; // update existing key
                return;
            }
            // Linear probe
            i = (i + 1) % tableSize;
        }

        keys[i] = key;
        values[i] = value;
        n++;
    }

    // Retrieve value
    private String get(String key) {
        int i = hash(key);
        int start = i;

        while (keys[i] != null) {
            if (keys[i].equals(key)) {
                return values[i];
            }
            i = (i + 1) % tableSize;
            // Ensure a full loop
            if (i == start) break;
        }

        return INVALID;
    }

    // Resize table when α > 0.5
    private void resize() {
        int oldSize = tableSize;
        tableSize = nextPrime(tableSize * 2);

        String[] oldKeys = keys;
        String[] oldValues = values;

        keys = new String[tableSize];
        values = new String[tableSize];
        n = 0;

        // Reinsert old pairs
        for (int i = 0; i < oldSize; i++) {
            if (oldKeys[i] != null) {
                add(oldKeys[i], oldValues[i]);
            }
        }
    }

    // Find next prime number ≥ n (for better hashing distribution)
    // Provides a slight speedup (which is needed considering my sad runtime performance thusfar..)
    private int nextPrime(int n) {
        while (!isPrime(n)) n++;
        return n;
    }

    // Function to determine if a number is prime
    private boolean isPrime(int n) {
        if (n < 2) return false;
        if (n % 2 == 0 && n != 2) return false;
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
