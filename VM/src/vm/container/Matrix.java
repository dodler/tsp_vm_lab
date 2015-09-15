package vm.container;

/**
 * interface matrix
 * Created by artem on 04.09.15.
 */
public interface Matrix
{
    public Object get(int i, int j);
    public void set(int i, int j, Object value);
}
