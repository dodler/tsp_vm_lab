package lian;

import javax.xml.bind.annotation.*;

/**
 * Created by artem on 28.11.15.
 */
//@XmlRootElement(name="task", namespace = "com.xmlservice.jaxb.model")
@XmlRootElement
public class Task
{
    public String num1,num2, res;

//    @XmlElement
//    public void setArgument1(String num1){
//        this.num1 = num1;
//    }
//
//    @XmlElement
//    public void setArgument2(String num2){
//        this.num2 = num2;
//    }
//
//    @XmlElement
//    public void setResult(String res){
//        this.res = res;
//    }
}
