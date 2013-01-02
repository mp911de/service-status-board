package de.paluch.status.status.model;

import de.paluch.status.status.entity.ServiceCheckResultEnum;
import de.paluch.status.status.entity.ServiceStateEntity;
import org.joda.time.*;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.Date;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 26.11.12 14:41
 */
public class ServiceState {

    private ServiceStateEntity serviceStateEntity;
    private ServiceCheckResultEnum result;
    private double serviceLevel;

    public ServiceState() {
    }

    public ServiceState(ServiceStateEntity serviceStateEntity) {
        this.serviceStateEntity = serviceStateEntity;
        result = serviceStateEntity.getResult();
    }

    public Long getId() {
        return serviceStateEntity.getId();
    }

    public void setId(Long id) {
        serviceStateEntity.setId(id);
    }

    public long getServiceId() {
        return serviceStateEntity.getServiceId();
    }

    public void setMessage(String message) {
        serviceStateEntity.setMessage(message);
    }

    public ServiceCheckResultEnum getResult() {
        return result;
    }

    public String getMessage() {
        return serviceStateEntity.getMessage();
    }

    public void setResult(ServiceCheckResultEnum result) {
        this.result = result;
    }

    public String getCheckKey() {
        return serviceStateEntity.getCheckKey();
    }

    public void setServiceId(long serviceId) {
        serviceStateEntity.setServiceId(serviceId);
    }

    public Date getCheckDate() {
        return serviceStateEntity.getCheckDate();
    }

    public void setCheckDate(Date checkDate) {
        serviceStateEntity.setCheckDate(checkDate);
    }

    public void setCheckKey(String checkKey) {
        serviceStateEntity.setCheckKey(checkKey);
    }

    public ServiceStateEntity getServiceStateEntity() {
        return serviceStateEntity;
    }

    public void setServiceStateEntity(ServiceStateEntity serviceStateEntity) {
        this.serviceStateEntity = serviceStateEntity;
    }

    public double getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(double serviceLevel) {
        this.serviceLevel = serviceLevel;
    }


    public boolean isSimilar(ServiceState other) {

        if (other == null) {
            return false;
        }

        if (other.getResult() == getResult()) {

            if ((other.getMessage() != null && getMessage() != null && other.getMessage
                    ().equals(getMessage())) || (other.getMessage() == null && getMessage() == null)) {
                return true;
            }

        }

        return false;
    }

    public boolean isDisplayOffsetToNow() {
        return true;
    }

    public String getOffsetToNow() {
        DateTime now = new DateTime();
        DateTime date = new DateTime(getCheckDate());
        int minutes = Minutes.minutesBetween(date, now).getMinutes();


        StringBuffer result = new StringBuffer();


        if (minutes != 0) {
            PeriodType type = PeriodType.forFields(new DurationFieldType[] {
                    DurationFieldType.days(), DurationFieldType.hours(), DurationFieldType.minutes(),
            });


            PeriodFormatter dateFormat = new PeriodFormatterBuilder()
                    .appendDays()
                    .appendSuffix(" day", " days")
                    .appendSeparator(" ")
                    .appendHours()
                    .appendSuffix(" hour", " hours")
                    .appendSeparator(" ")
                    .appendMinutes()
                    .appendSuffix(" minute", " minutes")
                    .appendSuffix(" ago")
                    .toFormatter();

            Period p = new Period(date, now, type);
            result.append(p.toString(dateFormat));


        } else {
            result.append("right now");
        }

        return result.toString();
    }
}
