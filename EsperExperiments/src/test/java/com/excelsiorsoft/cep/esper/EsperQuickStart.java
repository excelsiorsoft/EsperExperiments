package com.excelsiorsoft.cep.esper;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.excelsiorsoft.cep.esper.event.OrderEvent;

/**
 * Espert Quick Start Class.
 * <p/>
 * See {@link http://esper.codehaus.org/tutorials/tutorial/quickstart.html}
 *
 */
public class EsperQuickStart {
	
	@Before
	public void init(){
		//BasicConfigurator.configure();
	}
	
	@Test
	public void run() {
		// Configuration
		Configuration config = new Configuration();
		config.addEventTypeAutoName("com.excelsiorsoft.cep.esper.event");
		EPServiceProvider epService = EPServiceProviderManager
				.getDefaultProvider(config);
		// Creating a Statement
		String expression = "select Math.max(2, 3) as mymax, avg(price) from OrderEvent.win:time(30 sec)";
		EPStatement statement = epService.getEPAdministrator().createEPL(
				expression);
		// Adding a Listener
		MyListener listener = new MyListener();
		statement.addListener(listener);
		// Sending events
		OrderEvent event = new OrderEvent("shirt", 74.50);
		epService.getEPRuntime().sendEvent(event);
	}

	
	public class MyListener implements UpdateListener {
		public void update(EventBean[] newEvents, EventBean[] oldEvents) {
			// Here are Biz codes
			EventBean event = newEvents[0];
			System.out.println("avg=" + event.get("avg(price)"));
			System.out.println("mymax=" + event.get("mymax"));
		}
	}

	/*public static void main(String[] args) {
		EsperQuickStart test = new EsperQuickStart();
		test.run();
	}*/
}
