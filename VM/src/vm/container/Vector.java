package vm.container;

/**
 * class that incapsulates logic of work with
 * vectors
 * Created by artem on 18.09.15.
 */
public class Vector<T>
{
    private T[] vector;
    private int capacity;

    public Vector(int capacity)
    {
        if (capacity < 0) throw new IllegalArgumentException("Capacity should be >= 0");
        vector = (T[]) new Object[capacity];
        this.capacity = capacity;
    }

    public int getSize(){
        return vector.length;
    }

    public T get(int i)
    {
        if (i < 0 || i > vector.length)
        {
            throw new IndexOutOfBoundsException("Index [" + i + "] is out of bounds.");
        }
        return vector[i];
    }

    public void set(T value, int i)
    {
        if (i < 0 || i > vector.length)
        {
            throw new IndexOutOfBoundsException("Index [" + i + "] is out of bounds.");
        }
        vector[i] = value;
    }

    public void insertAtEnd(T value)
    {
        insert(value, vector.length);
    }

    private void insert(T value, int index)
    {
        if (vector.length == capacity)
        {

        }
    }
}
