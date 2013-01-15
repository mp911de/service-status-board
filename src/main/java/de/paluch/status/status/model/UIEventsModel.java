package de.paluch.status.status.model;

import de.paluch.status.status.Util;
import de.paluch.status.status.entity.ServiceEntity;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 26.11.12 08:00
 */
@XmlRootElement
public class UIEventsModel {

    @JsonIgnore
    @XmlTransient
    private DecimalFormat df = new DecimalFormat("##0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
    @JsonIgnore
    @XmlTransient
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, HH:mm", Locale.ENGLISH);

    private ServiceEntity service;
    private List<ServiceState> serviceStates;
    private boolean allOK;
    private double serviceLevel;
    private String environment;


    public String getDate(ServiceState state) {
        return dateFormat.format(state.getCheckDate());
    }

    public String image(ServiceState state) {
        return Util.getImage(state.getResult());
    }


    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public ServiceEntity getService() {
        return service;
    }

    public void setService(ServiceEntity service) {
        this.service = service;
    }

    public List<ServiceState> getServiceStates() {
        return serviceStates;
    }

    public void setServiceStates(List<ServiceState> serviceStates) {
        this.serviceStates = serviceStates;
    }

    public boolean isAllOK() {
        return allOK;
    }

    public void setAllOK(boolean allOK) {
        this.allOK = allOK;
    }

    public String getServiceLevelText() {
        return "Service-Level: " + df.format(getServiceLevel()) + "%";
    }

    public double getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(double serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
