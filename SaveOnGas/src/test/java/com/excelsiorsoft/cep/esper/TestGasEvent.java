package com.excelsiorsoft.cep.esper;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.excelsiorsoft.cep.esper.listener.GasEventListener;
import com.excelsiorsoft.cep.esper.model.GasEvent;
import com.excelsiorsoft.cep.esper.model.Store;


public class TestGasEvent {
	private Configuration cepConfig = null;
	private EPServiceProvider epService = null;
	private GasEvent[] gasEvents = new GasEvent[20];

	@Before
	public void initialize() throws Exception {
		cepConfig = new Configuration();
		cepConfig.addEventType("GasEvent", GasEvent.class.getName());
		epService = EPServiceProviderManager.getProvider("myCEPEngine",
				cepConfig);
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
		store4.setZipCode("11211");
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
			String expression = "select * from GasEvent(grade='Regular') having price < 2.80 and store.zipCode in ('11209')";
			EPStatement statement = epService.getEPAdministrator().createEPL(
					expression);
			GasEventListener listener = new GasEventListener();
			statement.addListener(listener);
			for (int i = 0; i < 6; i++) {
				Random random = new Random();
				int eventIndex = random.nextInt(5);
				epService.getEPRuntime().sendEvent(gasEvents[eventIndex]);
				Thread.sleep(3000);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@After
	public void cleanup() {
		cepConfig = null;
		epService = null;
	}
}
