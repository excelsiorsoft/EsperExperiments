package com.excelsiorsoft.cep.esper.listener;


import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.excelsiorsoft.cep.esper.model.GasEvent;


public class GasEventListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {

		GasEvent event = (GasEvent) newEvents[0].getUnderlying();
		
		System.out.println("Least expensive local gas seems to be at: ");
		System.out.println("Store Name: " + event.getStore().getStoreName());
		System.out.println("Store Zip: " + event.getStore().getZipCode());
		System.out.println("Gas Grade: " + event.getGrade());
		System.out.println("Gas Price: " + event.getPrice());
	}
}
