package dv360updater;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 * @version 1.0.00
 * @author Arturo Martínez <arturo.martinez@gdsmodellica.com>
 */
public class UpdaterEngine extends SwingWorker{
    private static EngineSettings _settings = null;
    private static EngineLogger _log;
    private List<String> _extensions;
    private JProgressBar _status;
    private JProgressBar _global;
    private File _dest;
    private String _version;
    
    public UpdaterEngine(){}
    public UpdaterEngine(List<String> aExt){
        this();
        _extensions = aExt;
    }
    public UpdaterEngine(List<String> aExt, JProgressBar globalBar){
        this(aExt);
        _global = globalBar;
    }
    public UpdaterEngine(List<String> aExt, JProgressBar globalBar, JProgressBar fileBar){
        this(aExt, globalBar);
        _status = fileBar;
    }
    public UpdaterEngine(List<String> aExt, JProgressBar globalBar, JProgressBar fileBar, EngineSettings settings, EngineLogger logger) throws Exception{
        this(aExt, globalBar, fileBar);
        _log = logger;
        _settings = settings;
    }
    
    public UpdaterEngine(List<String> aExt, JProgressBar globalBar, JProgressBar fileBar, EngineSettings settings, EngineLogger logger, DV360Versions version, File destination) throws Exception{
        this(aExt, globalBar, fileBar, settings, logger);
        _version = version.getSelected();
        _dest = destination;
    }
    
    private boolean copyAllFilesByExtensions() throws IOException{
       
        File srcFolder = new File(_settings.getDV360UpdatesFolder()+File.separator+"Dataview360-"+_version);
        File[] filesInDir = srcFolder.listFiles();
        _log.debug("Source folder: \""+srcFolder.getAbsolutePath()+"\"");

        Iterator<String> extensions = _extensions.iterator();
        _global.setMaximum(_extensions.size());
        _global.setValue(0);
        while (extensions.hasNext()){
            String extension = extensions.next();
            _log.debug("Start coping all \""+extension+"\" file/s");
            _global.setString("Step: "+_global.getValue()+1+"/"+_extensions.size()+" / Coping \""+extension+"\" file/s");
            _global.setValue(_global.getValue()+1);
            _global.repaint();
            copyFilesByExtension(filesInDir, extension);
        }//while
        
        return true;
    }
    
    private void copyFilesByExtension(File[] files, String ext) throws IOException{
        
        for (File pf : files){
            if (extensionMatch(pf, ext) || isMatchName(pf, ext)){
                _status.setMaximum(Integer.parseInt(String.valueOf(pf.length())));
                _status.setValue(0);
                copyFile(pf);
            }
        }//for:pf
    }
    
    private boolean extensionMatch(File file, String ext){
        return (file.getName().indexOf("."+ext) != -1);
    }
    
    private boolean isMatchName(File file, String name){
        return (file.getName().toLowerCase().equals(name.toLowerCase()));
    }
    
    private void copyFile(File file) throws IOException{
        OutputStream os = new FileOutputStream(_dest+File.separator+file.getName());        
        InputStream is = new FileInputStream(file);
        _log.debug("Coping "+file.getName());
        byte[] buffer = new byte[1024];
        int length;
        while((length = is.read(buffer)) > 0){
            os.write(buffer, 0, length);
            _status.setString("Coping: "+file.getName()+" - "+length+"/"+_status.getMaximum()+" bytes");
            _status.setValue(_status.getValue()+length);
            _status.repaint();
        }//while
        is.close();
        os.close();
    }

    @Override
    protected Object doInBackground() throws Exception {
        return copyAllFilesByExtensions();
    }
}
