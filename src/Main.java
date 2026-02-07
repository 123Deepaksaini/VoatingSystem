import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        String portEnv = System.getenv("PORT");
        if (portEnv != null) {
            port = Integer.parseInt(portEnv);
        }
        
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector();
        
        // Extract WAR if WAR_PATH is set
        String warPath = System.getenv("WAR_PATH");
        File webappDir = new File("target/webapp");
        
        if (warPath != null && new File(warPath).exists()) {
            // Extract WAR file
            File warFile = new File(warPath);
            webappDir = new File("target/extracted");
            extractWar(warFile, webappDir);
        }
        
        Context ctx = tomcat.addWebapp("/", new File("WebContent").getAbsolutePath());
        
        tomcat.start();
        System.out.println("Server started on port " + port);
        tomcat.getServer().await();
    }
    
    private static void extractWar(File warFile, File destDir) throws IOException {
        destDir.mkdirs();
        // Simple WAR extraction (JAR/ZIP format)
        java.util.zip.ZipFile zip = new java.util.zip.ZipFile(warFile);
        java.util.Enumeration<? extends java.util.zip.ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            java.util.zip.ZipEntry entry = entries.nextElement();
            File file = new File(destDir, entry.getName());
            if (entry.isDirectory()) {
                file.mkdirs();
            } else {
                file.getParentFile().mkdirs();
                InputStream is = zip.getInputStream(entry);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[4096];
                int len;
                while ((len = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                is.close();
            }
        }
        zip.close();
    }
}
