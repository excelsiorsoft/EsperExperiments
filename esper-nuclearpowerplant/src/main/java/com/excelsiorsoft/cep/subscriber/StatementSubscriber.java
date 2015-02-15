package com.excelsiorsoft.cep.subscriber;

/**
 * Just a convenience interface to let us easily connect the Esper statement queries with the Subscribers -
 * just for clarity so it's easy to see the queries the subscribers are registered against.
 */
public interface StatementSubscriber {

    public abstract class AbstractStatementSubscriber implements StatementSubscriber {
    	
    	@Override
    	public String toString() {
    		return getClass().getName() + "@" + Integer.toHexString(hashCode())+" [getQuery()=" + getQuery() + "]";
    	}
	}

	/**
     * Get the EPL Query the Subscriber will listen to.
     * @return EPL Query
     */
    String getQuery();

}
