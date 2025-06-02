package combooking.support;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesFile {
    private static FileInputStream input;
    private static Properties property = null;
    private static final Logger LOG = LogManager.getLogger(PropertiesFile.class);

    public static String getProperty(final String key) {

        try {

            input = new FileInputStream("src/test/java/combooking/constants/application.properties");
            property = new Properties();
            property.load(input);
        } catch (FileNotFoundException fnfe) {
            LOG.error("Properties File Not Found", fnfe);
        } catch (IOException ioe) {
            LOG.error("IO Exception while loading Properties File", ioe);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                LOG.error("IO Exception while closing file input stream", e);
            }
        }
        return property.getProperty(key).trim();
    }
}
