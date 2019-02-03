package collection;

import java.util.Objects;

/**
 * This class implements the {@link Map} interface.
 * Maps int keys to long values.
 */
public class OpenAddressingHashMap implements Map {

    /**
     * Basic hash bin node
     */
    private class Node{
        int hash;
        int key;
        long value;

        Node(int hash, int key, long value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }

    /**
     * The default initial capacity.
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    /**
     * The load factor.
     */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node[] array = new Node[DEFAULT_INITIAL_CAPACITY];

    private int size = 0;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long put(int key, long value) {
        return  put(key, value, array);
    }

    /**
     * Same as {@link OpenAddressingHashMap#put(int, long)} but for specific array
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @param arr array to put key-value
     * @return the previous value associated with {@code key}, or
      *        {@code null} if there was no mapping for {@code key}.
     */
    private Long put(int key, long value, Node[] arr){
        Long returnValue;

        int hash = calculateIndex(key, arr);

        //Checks if there is a node in arrays' bin
        returnValue = returnValueIfExists(hash, arr);

        //If arrays' bin is clear
        if (Objects.isNull(returnValue)) {
            //Checks if resize is needed before putting new node
            if (isResizeNeeded()) {
                array = rehash(arr, ((int) (arr.length * 1.5)));
            }

            arr[hash] = new Node(hash, key, value);
            size++;
        } else {
            //Rewriting new value to contained key
            arr[hash].value = value;
        }

        return returnValue;
    }

    /**
     * Finds arrays' bin to store new {@link Node}. Uses linear probing with step = 1.
     *
     * @param key key with which the specified value is to be associated
     * @param arr array to find proper bin for
     * @return index of array to store a {@link Node}
     */
    private int calculateIndex(int key, Node[] arr){
        int hash = hash(key, arr.length);

        //Checks calculated bins until one is empty or has the same key
        while (!Objects.isNull(arr[hash]) && arr[hash].key != key){
            hash = ++hash % arr.length;
        }

        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long get(int key) {
        return returnValueIfExists(calculateIndex(key, array), array);
    }

    /**
     * Checks if there is a {@link Node} in the array and returns its value or null if {@link Node} is absent
     *
     * @param hash index of array to check for a {@link Node}
     * @param arr array to check
     * @return value of a {@link Node} if present or {@code null} if absent
     */
    private Long returnValueIfExists(int hash, Node[] arr){
        return !Objects.isNull(arr[hash]) ? arr[hash].value : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer size() {
        return size;
    }


    /**
     * Calculates a hash for key for specific array
     *
     * @param key a key calculate the hash for
     * @param arrLength arrays' length to limit the hash
     * @return calculated hash for specific array
     */
    private int hash(int key, int arrLength){
        return key % arrLength;
    }

    /**
     * Checks if arrays' load factor is reached and resize is needed
     *
     * @return result of checking the load factor
     */
    private boolean isResizeNeeded(){
        return size >= array.length * DEFAULT_LOAD_FACTOR;
    }

    /**
     * Creates new array with larger length, rehashes old array to a new created one.
     *
     * @param arr old array rehash to
     * @param newArrayCapacity new length of the rehashed array
     * @return new array with rehashed {@link Node}s
     */
    private Node[] rehash(Node[] arr, int newArrayCapacity){
        System.out.println(newArrayCapacity);
        Node[] newArray = new Node[newArrayCapacity];
        size = 0;

        for (int i = 0; i < arr.length; i++) {
            Node node = arr[i];
            if (!Objects.isNull(node)) {
                put(node.key, node.value, newArray);
            }
        }

        return newArray;
    }
}
