import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

/**
 * it's actually task bean, refatoring will be done later
 * Created by artem on 25.11.15.
 */
@XmlRootElement
@XmlType(propOrder = {"arg1", "arg2", "result"})
@ManagedBean(name = "mainBean", eager = true)
@SessionScoped
public class MainBean
{
    private final String output = String.valueOf(new Date(System.currentTimeMillis()));

    public String getOutput()
    {
        return output;
    }

    private double arg1 = 0, arg2 = 0, result = 0;

    @XmlElement(name = "arg1")
    public double getArg1()
    {
        return arg1;
    }

    @XmlElement(name = "arg2")
    public double getArg2()
    {
        return arg2;
    }

    public void setArg1(double arg1)
    {
        this.arg1 = arg1;
    }

    public void setArg2(double arg2)
    {
        this.arg2 = arg2;
    }

    @XmlElement(name = "result")
    public double getResult()
    {
        return result;
    }

    public void setResult(double result)
    {
        this.result = result;
    }

    public void execute()
    {
        this.result = arg2 - arg1;
    }
}
