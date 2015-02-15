

Blog write up available at:
http://corsoftlimited.blogspot.co.uk/2013/02/complex-event-processing-made-easy.html

Also featured on JavaLobby:
http://java.dzone.com/articles/complex-event-processing-made


Simple project that illustrates the use of the Esper Complex Event Processing Engine. Purposefully left Unit Tests out to reduce size of code.

When the demo runs it will just simulate sending random temperature events through the processing engine. It will print debug messages to the console when it detects a sequence of events matching any of the 3 criteria statements we have defined (Critical, Warning, Monitor). 


To run demo:

4. 'mvn exec:java' (this will start running the demo - sending random temperature events)
	
