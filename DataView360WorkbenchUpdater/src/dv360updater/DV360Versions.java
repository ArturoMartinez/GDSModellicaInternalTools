package dv360updater;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @version 1.0.00
 * @author Arturo Martinez <arturo.martinez@gdsmodellica.com>
 */
public class DV360Versions {
    
    private static String _selected = null;
    private static List<String> _availables = null;
    
    public DV360Versions(){}
    public DV360Versions(String dv360updates) throws FileNotFoundException, IOException{
        
        this();
        
        File[] fUpdates = new File(dv360updates).listFiles();       
        String fPatternName = "Dataview360";        
        
        Pattern patt = Pattern.compile(fPatternName+"\\-{1}[\\.\\d]+");
        for (File pf : fUpdates){
            if (pf.isDirectory() && patt.matcher(pf.getName()).matches()){
                String version = pf.getName().substring(pf.getName().lastIndexOf("-")+1, pf.getName().length());
                if (_availables == null)
                    _availables = new ArrayList();
                _availables.add(version);
            }
        }
    }
    
    public void setSelected(String version){
        _selected = version;
    }
    
    public String getSelected(){
        return _selected;
    }
    
    public Object[] getAvailables(){
        return _availables.toArray();
    }    
}
