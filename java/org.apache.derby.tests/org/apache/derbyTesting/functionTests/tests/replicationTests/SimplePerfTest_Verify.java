/*
 
Derby - Class org.apache.derbyTesting.functionTests.tests.replicationTests.SimplePerfTest
 
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

package org.apache.derbyTesting.functionTests.tests.replicationTests;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import junit.framework.Test;
import org.apache.derby.jdbc.BasicClientDataSource40;
import org.apache.derby.client.ClientDataSourceInterface;
import org.apache.derbyTesting.junit.BaseJDBCTestCase;
import org.apache.derbyTesting.junit.BaseTestSuite;
import org.apache.derbyTesting.junit.JDBC;
import org.apache.derbyTesting.junit.TestConfiguration;

public class SimplePerfTest_Verify extends BaseJDBCTestCase
{
    
    public SimplePerfTest_Verify(String testcaseName)
    {
        super(testcaseName);
    }
    
    private static String dbPath = "UNDEFINED!";
    private static String masterHostName = "UNDEFINED!";
    private static int masterPortNo = -1;
    private static int tuplesToInsert = -1;
    
    public static Test suite()
        throws Exception
    {
        masterHostName = System.getProperty("test.serverHost", "localhost");
        masterPortNo = Integer.parseInt(System.getProperty("test.serverPort", "1527"));
        dbPath = System.getProperty("test.dbPath", "wombat");
        tuplesToInsert= Integer.parseInt(System.getProperty("test.inserts", "0"));
        
        BaseTestSuite suite = new BaseTestSuite("SimplePerfTest_Verify");
        
        suite.addTest(SimplePerfTest_Verify.suite(masterHostName, masterPortNo, dbPath));
        return (Test)suite;
    }

    /**
     * Adds this class to the *existing server* suite.
     */
    public static Test suite(String serverHost, int serverPort, String dbPath)
    {
        Test t = TestConfiguration.existingServerSuite(SimplePerfTest_Verify.class,false,serverHost,serverPort,dbPath);
        return t;
   }
    
    /**
     *
     *
     * @throws SQLException, IOException, InterruptedException
     */
    public void testVerify()
        throws SQLException, IOException, InterruptedException,
               ClassNotFoundException, IllegalAccessException, InstantiationException,
               NoSuchMethodException, InvocationTargetException
    {
        verifyTestInserts();
    }
    private void verifyTestInserts()
        throws SQLException, IOException, InterruptedException,
               ClassNotFoundException, IllegalAccessException, InstantiationException,
               NoSuchMethodException, InvocationTargetException
    {
        Connection conn = clientConnection(masterHostName, masterPortNo, dbPath); // getConnection();
        
        Statement s = conn.createStatement();
        
        ResultSet rs = s.executeQuery("select count(*) from t");
        rs.next();
        int count = rs.getInt(1);
        this.assertEquals(count, tuplesToInsert);
    }
    private Connection clientConnection(String hostName, int portNo, String dbPath)
            throws SQLException, ClassNotFoundException, IllegalAccessException,
                   InstantiationException, NoSuchMethodException,
                   InvocationTargetException
    {
        ClientDataSourceInterface ds;

        if (JDBC.vmSupportsJNDI()) {
            Class<?> clazz = Class.forName("org.apache.derby.jdbc.ClientDataSource");
            ds = (ClientDataSourceInterface) clazz.getConstructor().newInstance();
        } else {
            ds = new BasicClientDataSource40();
        }

        ds.setDatabaseName(dbPath);
        ds.setServerName(hostName);
        ds.setPortNumber(portNo);
        // ds.setConnectionAttributes(useEncryption(false));
        return ds.getConnection();
    }
}
