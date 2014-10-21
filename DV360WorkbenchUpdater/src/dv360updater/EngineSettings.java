package dv360updater;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @version 1.0.00
 * @author Arturo Martínez <arturo.martinez@gdsmodellica.com>
 */
public class EngineSettings {
    
    private static String _DV360UpdatesFolder = null;
    
    public String getDV360UpdatesFolder(){
        return _DV360UpdatesFolder;
    }
    
    private void setDV360UpdatesFolder(String path){
        _DV360UpdatesFolder = path;
    }
    
    public EngineSettings(){}
    public EngineSettings(String exePath) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException{
        this();
        
        File config = new File(exePath+File.separator+"config"+File.separator+"engine.xml");
        if (config.canRead()){
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom  = db.parse(config);
            
            NodeList nodes = dom.getElementsByTagName("dv360updates");
            for (int i = 0; ((i < nodes.getLength()) && (_DV360UpdatesFolder == null)); i++){
                Element node = (Element) nodes.item(i);
                NodeList childrens = node.getElementsByTagName("path");
                
                for (int j = 0; (((j < childrens.getLength()) && (_DV360UpdatesFolder == null))); j++){
                    Element child = (Element) childrens.item(j);
                    if (child.getNodeName().equals("path")){
                        setDV360UpdatesFolder(child.getTextContent());                        
                    }//fi
                }//for:j
            }//for:i
            
            if ((_DV360UpdatesFolder == null) || (_DV360UpdatesFolder.length() == 0)){
                throw new FileNotFoundException("DV360 Updates folder was not set in config file");
            }
        }else{
            throw new FileNotFoundException("Config file doesn't exists or couldn't be read");
        }
        
    }
}
