package com.excelsiorsoft.cep.subscriber;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.excelsiorsoft.cep.subscriber.StatementSubscriber.AbstractStatementSubscriber;

/**
 *  Wraps Esper Statement Query and Listener. No dependency on Esper libraries.
 */
@Component
public class MonitorEventSubscriber extends AbstractStatementSubscriber/*implements StatementSubscriber*/ {

    /** Logger */
    private static Logger LOG = LoggerFactory.getLogger(MonitorEventSubscriber.class);

    /**
     * {@inheritDoc}
     */
    public String getQuery() {

        // Example of simple EPL with a Time Window
        return "select avg(temperature) as avg_val from TemperatureEvent.win:time_batch(5 sec)";
    }

    /**
     * Listener method called when Esper has detected a pattern match.
     */
    public void update(Map<String, Double> eventMap) {

        // average temp over 10 secs
        Double avg = (Double) eventMap.get("avg_val");

        StringBuilder sb = new StringBuilder();
        sb.append("---------------------------------");
        sb.append("\n- [MONITOR] Average Temp = " + avg);
        sb.append("\n---------------------------------");

        LOG.debug(sb.toString());
    }
    
	@Override
	public String toString() {
		return this.getClass()+String.valueOf(this.hashCode())+" [getQuery()=" + getQuery() + "]";
	}
}
