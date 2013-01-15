package de.paluch.status;

import de.paluch.status.status.entity.MaintenanceEntity;
import org.junit.Test;

import java.sql.Time;
import java.util.Calendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 23.11.12 08:21
 */
public class MaintenanceCheckTest {


    @Test
    public void testTime1() {
        MaintenanceEntity me = new MaintenanceEntity();

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR, 8);
        cal.set(Calendar.MINUTE, 0);

        me.setStartTime(cal.getTime());
        me.setDurationMinutes(20);
        me.setRecurring(true);

        cal.set(Calendar.HOUR, 8);
        cal.set(Calendar.MINUTE, 8);

        assertTrue(me.isActive(cal.getTime()));
    }


    @Test
    public void testTime2() {
        MaintenanceEntity me = new MaintenanceEntity();
        me.setStartTime(new Time(8, 0, 0));
        me.setDurationMinutes(5);
        me.setRecurring(true);

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR, 8);
        cal.set(Calendar.MINUTE, 8);

        assertFalse(me.isActive(cal.getTime()));

    }

    @Test
    public void testTime3() {
        MaintenanceEntity me = new MaintenanceEntity();
        me.setStartTime(new Time(8, 0, 0));
        me.setDurationMinutes(5);
        me.setRecurring(true);

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR, 7);
        cal.set(Calendar.MINUTE, 8);

        assertFalse(me.isActive(cal.getTime()));
    }


    @Test
    public void testTimePeriod() {

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR, 8);
        cal.set(Calendar.MINUTE, 0);

        MaintenanceEntity me = new MaintenanceEntity();
        me.setStartTime(cal.getTime());
        me.setDurationMinutes(10);
        me.setRecurring(false);

        cal.set(Calendar.HOUR, 8);
        cal.set(Calendar.MINUTE, 1);

        assertTrue(me.isActive(cal.getTime()));
    }

    @Test
    public void testTimePeriod1() {

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR, 8);
        cal.set(Calendar.MINUTE, 0);

        MaintenanceEntity me = new MaintenanceEntity();
        me.setStartTime(cal.getTime());
        me.setDurationMinutes(10);
        me.setRecurring(false);

        cal.set(Calendar.HOUR, 8);
        cal.set(Calendar.MINUTE, 20);

        assertFalse(me.isActive(cal.getTime()));
    }

}
