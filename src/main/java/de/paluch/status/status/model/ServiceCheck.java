package de.paluch.status.status.model;

import javax.xml.bind.annotation.*;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 22.11.12 21:05
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceCheck {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private Integer performanceWarnMs;

    @XmlAttribute
    private Integer performanceFailMs;

    @XmlValue
    private String location;

    @XmlAttribute
    private String expectedResult;

    @XmlAttribute
    private String warnResult;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPerformanceWarnMs() {
        return performanceWarnMs;
    }

    public void setPerformanceWarnMs(Integer performanceWarnMs) {
        this.performanceWarnMs = performanceWarnMs;
    }

    public Integer getPerformanceFailMs() {
        return performanceFailMs;
    }

    public void setPerformanceFailMs(Integer performanceFailMs) {
        this.performanceFailMs = performanceFailMs;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public String getWarnResult() {
        return warnResult;
    }

    public void setWarnResult(String warnResult) {
        this.warnResult = warnResult;
    }
}
