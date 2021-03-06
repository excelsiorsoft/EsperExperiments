package com.excelsiorsoft.cep.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.excelsiorsoft.cep.event.TemperatureEvent;
import com.excelsiorsoft.cep.subscriber.StatementSubscriber;

/**
 * This class handles incoming Temperature Events. It processes them through the EPService, to which
 * it has attached the 3 queries.
 */
@Component
//@Scope(value = "singleton")
public class TemperatureEventHandler implements InitializingBean{

    /** Logger */
    private static Logger LOG = LoggerFactory.getLogger(TemperatureEventHandler.class);

    /** Esper service */
    private EPServiceProvider epService;
    private EPStatement criticalEventStatement;
    private EPStatement warningEventStatement;
    private EPStatement monitorEventStatement;

    @Autowired
    @Qualifier("criticalEventSubscriber")
    private StatementSubscriber criticalEventSubscriber;

    @Autowired
    @Qualifier("warningEventSubscriber")
    private StatementSubscriber warningEventSubscriber;

    @Autowired
    @Qualifier("monitorEventSubscriber")
    private StatementSubscriber monitorEventSubscriber;

    /**
     * Configure Esper Statement(s).
     */
    public void initService() {

        LOG.debug("Initializing TemperatureEventHandler ...\n");
        
        Configuration config = new Configuration();
        config.addEventTypeAutoName("com.excelsiorsoft.cep.event");
        epService = EPServiceProviderManager.getDefaultProvider(config);

        createCriticalTemperatureCheckExpression();
        createWarningTemperatureCheckExpression();
        createTemperatureMonitorExpression();
        
        LOG.debug(this +" has successfully initialized .\n");

    }

    /**
     * EPL to check for a sudden critical rise across 4 events, where the last event is 1.5x greater
     * than the first event. This is checking for a sudden, sustained escalating rise in the
     * temperature
     */
    private void createCriticalTemperatureCheckExpression() {
        
    	LOG.debug("creating 'Critical Temperature Check Expression'");
    	criticalEventStatement = epService.getEPAdministrator().createEPL(criticalEventSubscriber.getQuery());
    	
    	LOG.debug("associating subscriber "+criticalEventSubscriber+" with statement "+criticalEventStatement+"\n");
        criticalEventStatement.setSubscriber(criticalEventSubscriber);
        
    }

    /**
     * EPL to check for 2 consecutive Temperature events over the threshold - if matched, will alert
     * listener.
     */
    private void createWarningTemperatureCheckExpression() {

        LOG.debug("creating 'Warning Temperature Check Expression'");
        warningEventStatement = epService.getEPAdministrator().createEPL(warningEventSubscriber.getQuery());
        
        LOG.debug("associating subscriber "+warningEventSubscriber+" with statement "+warningEventStatement+"\n");
        warningEventStatement.setSubscriber(warningEventSubscriber);
    }

    /**
     * EPL to monitor the average temperature every 10 seconds. Will call listener on every event.
     */
    private void createTemperatureMonitorExpression() {

        LOG.debug("creating 'Timed Average Monitor'");
        monitorEventStatement = epService.getEPAdministrator().createEPL(monitorEventSubscriber.getQuery());
        
        LOG.debug("associating subscriber "+monitorEventSubscriber+" with statement "+monitorEventStatement+"\n");
        monitorEventStatement.setSubscriber(monitorEventSubscriber);
    }

    /**
     * Handle the incoming TemperatureEvent.
     */
    public void handle(TemperatureEvent event) {

        LOG.debug(event.toString());
        epService.getEPRuntime().sendEvent(event);

    }

    @Override
    public void afterPropertiesSet() {
        
        LOG.debug("Subscribers are initialized. Configuring TemperatureEventHandler...\n");
        initService();
    }
}
