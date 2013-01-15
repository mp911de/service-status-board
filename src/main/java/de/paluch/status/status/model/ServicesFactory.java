package de.paluch.status.status.model;

import javax.xml.bind.JAXB;
import java.net.URL;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 22.11.12 21:14
 */
public class ServicesFactory {

    private URL configLocation;

    public Services createInstance()  {
        return JAXB.unmarshal(configLocation, Services.class);
    }

    public URL getConfigLocation() {
        return configLocation;
    }

    public void setConfigLocation(URL configLocation) {
        this.configLocation = configLocation;
    }
}
