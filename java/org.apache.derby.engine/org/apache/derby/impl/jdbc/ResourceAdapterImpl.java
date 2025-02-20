/*

   Derby - Class org.apache.derby.impl.jdbc.ResourceAdapterImpl

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.apache.derby.impl.jdbc;

import org.apache.derby.shared.common.info.JVMInfo;

import org.apache.derby.iapi.services.context.ContextService;
import org.apache.derby.iapi.services.monitor.ModuleControl;
import org.apache.derby.iapi.services.monitor.Monitor;
import org.apache.derby.shared.common.sanity.SanityManager;

import org.apache.derby.iapi.jdbc.ResourceAdapter;

import org.apache.derby.shared.common.error.StandardException;
import org.apache.derby.iapi.store.access.AccessFactory;
import org.apache.derby.iapi.store.access.xa.XAResourceManager;
import org.apache.derby.iapi.store.access.xa.XAXactId;

import java.util.Properties;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.transaction.xa.XAException;


public class ResourceAdapterImpl
		implements ResourceAdapter, ModuleControl
{
	private boolean active;

	// the real resource manager 
	private XAResourceManager rm;	

	// maps Xid to XATransationResource for run time transactions
    private Hashtable<XAXactId, XATransactionState> connectionTable;

	/*
	 * Module control
	 */

	public void boot(boolean create, Properties properties)
		throws StandardException
	{
		// we can only run on jdk1.2 or beyond with JTA and JAVA 20 extension
		// loaded.

        connectionTable = new Hashtable<XAXactId, XATransactionState>();

		AccessFactory af = 
			(AccessFactory)findServiceModule(this, AccessFactory.MODULE);

		rm = (XAResourceManager) af.getXAResourceManager();

		active = true;
	}

	public void stop()
	{
		active = false;

        for (Enumeration<XATransactionState> e = connectionTable.elements();
                e.hasMoreElements(); ) {

            XATransactionState tranState = e.nextElement();

			try {
				tranState.conn.close();
			} catch (java.sql.SQLException sqle) {
			}
		}

		active = false;
	}

	public boolean isActive()
	{
		return active;
	}

	/*
	 * Resource Adapter methods 
	 */

	public synchronized Object findConnection(XAXactId xid) {

		return connectionTable.get(xid);
	}

	public synchronized boolean addConnection(XAXactId xid, Object conn) {
		if (connectionTable.get(xid) != null)
			return false;

		// put this into the transaction table, if the xid is already
		// present as an in-doubt transaction, we need to remove it from
		// the run time list
        connectionTable.put(xid, (XATransactionState) conn);
		return true;
	}

	public synchronized Object removeConnection(XAXactId xid) {

		return connectionTable.remove(xid);

	}

	/** @see org.apache.derby.iapi.jdbc.ResourceAdapter#cancelXATransaction(XAXactId, String)
	 */
	public void cancelXATransaction(XAXactId xid, String messageId)
	throws XAException
	{
		XATransactionState xaState = (XATransactionState) findConnection(xid);

		if (xaState != null) {
			xaState.cancel(messageId);
		}
	}


	/**
		Return the XA Resource manager to the XA Connection
	 */
	public XAResourceManager getXAResourceManager()
	{
		return rm;
	}
    /**
     * Must be private so that user code
     * can't call this entry point.
     */
    private  static  Object findServiceModule( final Object serviceModule, final String factoryInterface)
        throws StandardException
    {
        return Monitor.findServiceModule( serviceModule, factoryInterface );
    }

}
