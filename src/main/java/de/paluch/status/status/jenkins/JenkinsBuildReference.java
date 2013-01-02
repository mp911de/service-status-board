package de.paluch.status.status.jenkins;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 26.11.12 08:27
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class JenkinsBuildReference {

    @XmlElement(name = "url")
    private String url;

    @XmlElement(name = "number")
    private int number;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
