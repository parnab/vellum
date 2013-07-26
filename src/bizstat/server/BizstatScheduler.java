/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package bizstat.server;

import vellum.datatype.Milli;
import vellum.util.Calendars;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import bizstat.entity.*;
import java.util.*;
import vellum.util.DefaultDateFormats;

/**
 *
 * @author evan
 */
public class BizstatScheduler implements Runnable {

    Logr logger = LogrFactory.getLogger(BizstatScheduler.class);
    
    BizstatServer server;

    public BizstatScheduler(BizstatServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        for (Network network : server.networkList) {
            if (network.isEnabled()) {
                schedule(network);
            }
        }
    }  
    
    private void schedule(Network network) {
        for (Host host : network.getHostList()) {
            if (host.isEnabled()) {
                schedule(host);
            }
        }
    }
    
    private void schedule(Host host) {
        for (BizstatService service : host.getServiceList()) {
            if (service.isEnabled() && service.getScheduleTime() != null) {
                schedule(host, service);
            }
        }
    }
    
    private void schedule(Host host, BizstatService service) {
        Calendar calendar = Calendar.getInstance();
        Calendars.setTime(calendar, service.getScheduleTime());
        long currentMillis = System.currentTimeMillis();
        long scheduleMillis = calendar.getTimeInMillis();
        if (scheduleMillis < currentMillis) scheduleMillis += Milli.fromDays(1);
        long initialDelay =  scheduleMillis - currentMillis;
        if (initialDelay < 0) initialDelay += Milli.fromDays(1);
        long period = service.getIntervalMillis();
        logger.info("schedule", host, service, 
                DefaultDateFormats.timeSecondsFormat.format(service.getScheduleTime()), 
                calendar.getTime(), initialDelay, period);
        server.getStatus(new HostServiceKey(host, service)).schedule(initialDelay, period);
    }
    
      
    
}
