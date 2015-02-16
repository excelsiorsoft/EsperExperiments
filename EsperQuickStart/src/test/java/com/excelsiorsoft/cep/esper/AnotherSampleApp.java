package com.excelsiorsoft.cep.esper;

import java.util.Date;
import java.util.Random;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.junit.Test;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class AnotherSampleApp {

	public static class Tick {
		String symbol;
		Double price;
		Date timeStamp;

		public Tick(String s, double p, long t) {
			symbol = s;
			price = p;
			timeStamp = new Date(t);
		}

		public double getPrice() {
			return price;
		}

		public String getSymbol() {
			return symbol;
		}

		public Date getTimeStamp() {
			return timeStamp;
		}

		@Override
		public String toString() {
			return "Symbol: " + symbol + "| Price: " + price.toString() + "| Time: "
					+ timeStamp.toString();
		}
	}

	private static Random generator = new Random();

	public static void GenerateRandomTick(EPRuntime cepRT) {

		double price = (double) generator.nextInt(10);
		long timeStamp = System.currentTimeMillis();
		String symbol = "AAPL";
		Tick tick = new Tick(symbol, price, timeStamp);
		
		System.out.println(">>>Sending tick:" + tick + "\n");
		
		cepRT.sendEvent(tick);

	}

	public static class CEPListener implements UpdateListener {

		public void update(EventBean[] newData, EventBean[] oldData) {
			System.out.println("----------------------------------------------------------------");
			System.out.println("Event received: " + newData[0].getUnderlying());
			System.out.println("----------------------------------------------------------------");
		}
	}

	@Test
	public void testApp() {

		SimpleLayout layout = new SimpleLayout();
		ConsoleAppender appender = new ConsoleAppender(new SimpleLayout());
		Logger.getRootLogger().addAppender(appender);
		Logger.getRootLogger().setLevel((Level) Level.WARN);

		// The Configuration is meant only as an initialization-time object.
		Configuration cepConfig = new Configuration();
		cepConfig.addEventType("StockTick", Tick.class.getName());
		EPServiceProvider cep = EPServiceProviderManager.getProvider(
				"myCEPEngine", cepConfig);
		EPRuntime cepRT = cep.getEPRuntime();

		EPAdministrator cepAdm = cep.getEPAdministrator();
		EPStatement cepStatement = cepAdm.createEPL("select sum(price), avg(price), * from "
				+ "StockTick(symbol='AAPL').win:length(3) "
				+ "having avg(price) > 5.0");

		cepStatement.addListener(new CEPListener());

		// We generate a few ticks...
		for (int i = 0; i < 20; i++) {
			GenerateRandomTick(cepRT);
		}
	}
}
