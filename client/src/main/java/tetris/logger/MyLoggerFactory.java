package tetris.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.net.URL;

public class MyLoggerFactory {
    static {
        URL loggerConfigUrl = MyLoggerFactory.class.getClassLoader().getResource("log4j2.xml");
        assert loggerConfigUrl != null;
        Configurator.initialize(null, loggerConfigUrl.getPath());
    }

    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}
