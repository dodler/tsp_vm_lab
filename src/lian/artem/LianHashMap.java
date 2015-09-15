package lian.artem;

import java.util.NoSuchElementException;

/**
 * implementation of hashmap
 * for now it will be Map<String, String> by default
 * Created by artem on 01.08.15.
 */
public class LianHashMap<K, V> {

    private int length =0;

    class Entry<K, V> {
        Entry next;
        int hash;
        K key;
        V value;
    };

    static int hash(int h){
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    static int indexFor(int h, int length){
        return h&(length-1);
    }

    private transient Entry[] entries;

    public LianHashMap(Class<K> k, Class<V> v, int lenght) {
        this.entries = (Entry[]) new Object[16];
        Entry<K, V> e = new Entry<>();
        e.value = null;
        e.key = null;
        e.hash = 0;
        entries[0] = e;
    }

}
