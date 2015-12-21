import sun.applet.Main;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;

/**
 * Created by artem on 20.12.15.
 */
@ManagedBean(name = "xmlBean")
@SessionScoped
public class XmlBean
{

    @ManagedProperty("#{mainBean}")
    private MainBean bean;

    private String xml;

    public void execute()
    {
        StringWriter writer = new StringWriter();
        Marshaller marshaller = null;
        try
        {
            JAXBContext context = JAXBContext.newInstance(MainBean.class);
            marshaller = context.createMarshaller();
            marshaller.marshal(bean, writer);
            writer.flush();
        } catch (JAXBException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        setXml(writer.toString());

    }

    public MainBean getBean()
    {
        return bean;
    }

    public void setBean(MainBean bean)
    {
        this.bean = bean;
    }

    public String getXml()
    {
        return xml;
    }

    public void setXml(String xml)
    {
        this.xml = xml;
    }
}
