package de.paluch.status.status.model;

import de.paluch.status.status.Util;
import de.paluch.status.status.entity.ServiceEntity;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 23.11.12 09:03
 */
@XmlRootElement
public class UIOverviewModel {

    @JsonIgnore
    @XmlTransient
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
    @JsonIgnore
    @XmlTransient
    private SimpleDateFormat linkDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
    @JsonIgnore
    @XmlTransient
    private DecimalFormat df = new DecimalFormat("##0.00");

    private List<ServiceEntity> services = new ArrayList<ServiceEntity>();
    private Map<String, ServiceState> currentState = new HashMap<String, ServiceState>();
    private Map<Date, Map<String, ServiceState>> history = new TreeMap<Date, Map<String,
            ServiceState>>(new Comparator<Date>() {
        @Override
        public int compare(Date date, Date date2) {
            return date2.compareTo(date);
        }
    });
    private String environment;


    public String image(ServiceEntity service, Date date) {
        ServiceState state = getHistory().get(date).get(service.getServiceKey());
        return Util.getImage(state.getResult());
    }

    public String message(ServiceEntity service, Date date) {
        ServiceState state = getHistory().get(date).get(service.getServiceKey());
        return getMessage(state);
    }

    public String currentImage(ServiceEntity service) {
        ServiceState currentState = getCurrentState().get(service.getServiceKey());
        return Util.getImage(currentState.getResult());
    }


    public String currentMessage(ServiceEntity service) {
        ServiceState currentState = getCurrentState().get(service.getServiceKey());
        return getMessage(currentState);
    }

    private String getMessage(ServiceState currentState) {

        String message = "";
        boolean wrap = false;
        if (currentState != null && currentState.getMessage() != null) {
            message = currentState.getMessage();
            wrap = true;
        }

        if (currentState != null && currentState.getServiceLevel() != 0) {
            if (wrap) {
                message += ", ";
            }

            message += "Service-Level: " + df.format(currentState.getServiceLevel()) + "%";
        }
        return message;
    }

    public List<ServiceEntity> getServices() {
        return services;
    }

    public void setServices(List<ServiceEntity> services) {
        this.services = services;
    }

    public String getLinkDate(Date date) {
        return linkDateFormat.format(date);
    }

    public String getDate(Date date) {
        return dateFormat.format(date);
    }

    public Set<Date> getDates() {
        return history.keySet();
    }


    public Map<String, ServiceState> getCurrentState() {
        return currentState;
    }

    public void setCurrentState(Map<String, ServiceState> currentState) {
        this.currentState = currentState;
    }

    public Map<Date, Map<String, ServiceState>> getHistory() {
        return history;
    }

    public void setHistory(Map<Date, Map<String, ServiceState>> history) {
        this.history = history;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
