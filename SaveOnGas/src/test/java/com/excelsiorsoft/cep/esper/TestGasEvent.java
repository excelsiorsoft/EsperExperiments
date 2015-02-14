package com.excelsiorsoft.cep.esper;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.excelsiorsoft.cep.esper.listener.GasEventListener;
import com.excelsiorsoft.cep.esper.model.GasEvent;
import com.excelsiorsoft.cep.esper.model.Store;


public class TestGasEvent {
	
	private static final int EVENT_BATCH_SIZE=5;
	private static  Configuration cepConfig = null;
	private static  EPServiceProvider epService = null;
	private static GasEvent[] gasEvents = new GasEvent[EVENT_BATCH_SIZE];

	@BeforeClass
	public static void initialize() throws Exception {
		
		cepConfig = new Configuration();
		cepConfig.addEventType("GasEvent", GasEvent.class.getName());
		epService = EPServiceProviderManager.getProvider("myCEPEngine",	cepConfig);
		
		buildEventsBatch();
	}

	private static void buildEventsBatch() {
		
		Store store1 = new Store();
		store1.setStoreName("Exxon");
		store1.setZipCode("11208");
		
		Store store2 = new Store();
		store2.setStoreName("Shell");
		store2.setZipCode("11209");
		
		Store store3 = new Store();
		store3.setStoreName("Chevron");
		store3.setZipCode("11210");
		
		Store store4 = new Store();
		store4.setStoreName("Citgo");
		store4.setZipCode("11209");
		GasEvent gasEvent1 = new GasEvent();
		;
		gasEvent1.setPrice(2.80);
		gasEvent1.setGrade("Premium");
		gasEvent1.setStore(store1);
		gasEvents[0] = gasEvent1;
		GasEvent gasEvent2 = new GasEvent();
		;
		gasEvent2.setPrice(2.50);
		gasEvent2.setGrade("Regular");
		gasEvent2.setStore(store2);
		gasEvents[1] = gasEvent2;
		GasEvent gasEvent3 = new GasEvent();
		;
		gasEvent3.setPrice(2.79);
		gasEvent3.setGrade("Mid");
		gasEvent3.setStore(store3);
		gasEvents[2] = gasEvent3;
		GasEvent gasEvent4 = new GasEvent();
		;
		gasEvent4.setPrice(2.55);
		gasEvent4.setGrade("Regular");
		gasEvent4.setStore(store4);
		gasEvents[3] = gasEvent4;
		GasEvent gasEvent5 = new GasEvent();
		;
		gasEvent5.setPrice(3.50);
		gasEvent5.setGrade("Premium");
		gasEvent5.setStore(store4);
		gasEvents[4] = gasEvent5;
	}

	@Test
	public void findCheapGasLocally() {
		try {
			
			final String expression = "select * from GasEvent(grade='Regular') having price < 2.60 and store.zipCode in ('11209')";
			final EPStatement statement = epService.getEPAdministrator().createEPL(expression);
			
			statement.addListener(new GasEventListener());
			
			for (int i = 0; i < gasEvents.length; i++) {

				System.out.println("EventSender --> About to send event #: "+i);
				epService.getEPRuntime().sendEvent(gasEvents[i]);
				
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@AfterClass
	public static void cleanup() {
		cepConfig = null;
		epService = null;
	}
}
