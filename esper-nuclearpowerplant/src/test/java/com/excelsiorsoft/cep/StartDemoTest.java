package com.excelsiorsoft.cep;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excelsiorsoft.cep.util.RandomTemperatureEventGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/application-context.xml"})
public class StartDemoTest {

	/** Logger */
    private static Logger LOG = LoggerFactory.getLogger(StartDemoTest.class);
	
	@Autowired
	@Qualifier("eventGenerator")
	RandomTemperatureEventGenerator generator;
	
	@Test
	public void test() {
		LOG.debug("Starting...");

        long noOfTemperatureEvents = 1000;

        /*if (args.length != 1) {*/
            LOG.debug("No override of number of events detected - defaulting to " + noOfTemperatureEvents + " events.");
        /*} else {
            noOfTemperatureEvents = Long.valueOf(args[0]);
        }*/

        // Load spring config
       /* ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] { "application-context.xml" });
        BeanFactory factory = (BeanFactory) appContext;*/

        // Start Demo
        //RandomTemperatureEventGenerator generator = (RandomTemperatureEventGenerator) factory.getBean("eventGenerator");
        generator.startSendingTemperatureReadings(noOfTemperatureEvents);
	}

}
