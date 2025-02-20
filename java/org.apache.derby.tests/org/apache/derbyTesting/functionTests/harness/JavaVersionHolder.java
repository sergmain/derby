/*

   Derby - Class org.apache.derbyTesting.functionTests.harness.JavaVersionHolder

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

package org.apache.derbyTesting.functionTests.harness;

import java.util.StringTokenizer;

/**
  To break down the java version into major and minor
  Used by the test harness for special cases
  */
public class JavaVersionHolder
{
    private static final String EARLY_ACCESS_SUFFIX = "-ea";
 
    private String majorVersion;
    private String minorVersion;
    private int major;
    private int minor;
    
    public JavaVersionHolder(String javaVersion)
        throws java.lang.NumberFormatException
    {
        //System.out.println("JavaVersionHolder() javaVersion = " + javaVersion);

        // remove Open JDK early access indicator
        javaVersion = stripEarlyAccessSuffix(javaVersion);
        
        // handle early access versions of JDK 9
        if (javaVersion.startsWith( "9" ))
        {
            javaVersion = "1.9.0";
        }

        // handle JDK 11
        if (javaVersion.startsWith( "11" ))
        {
            javaVersion = "1.11.0";
        }

        // handle future java versions.
        // rewrite version into the form 1.$javaVersion.0
        switch(javaVersion)
        {
        case "10":
        case "13":
        case "14":
        case "15":
        case "16":
        case "17":
        case "18":
        case "19":
        case "20":
        case "21":
            javaVersion = "1." + javaVersion + ".0";
            break;

        default:
            break;
        }

        // check for jdk12 or higher
        int i = javaVersion.indexOf('.');
        int j = javaVersion.indexOf('.', i+1);
        majorVersion = javaVersion.substring(0, i);
        try
	    {
          Integer imajor = Integer.valueOf(majorVersion);
		    major = imajor.intValue();
		    if (j != -1)
		    {
		        minorVersion = javaVersion.substring(i+1, j);
		        Integer iminor = Integer.valueOf(minorVersion);
		        minor = iminor.intValue();
		    }
		    else
		    {
		        minorVersion = javaVersion.substring(i+1);
		        Integer iminor = Integer.valueOf(minorVersion);
		        minor = iminor.intValue();
		    }
		}
		catch (NumberFormatException nfe)
		{
		    // Cannot parse the version as an Integer
		    // such as on HP: hack for this special case
		    if (javaVersion.startsWith("HP"))
		    {
		        // attempt to get the version
		        StringTokenizer st = new StringTokenizer(javaVersion,".");
		        String tmp = st.nextToken();
		        majorVersion = st.nextToken();
		        if (majorVersion.equals("01"))
		            majorVersion = "1";
		        else if (majorVersion.equals("02"))
		            majorVersion = "2";
		        minorVersion = st.nextToken();
		        if (minorVersion.startsWith("1"))
		            minorVersion = "1";
		        else if (minorVersion.startsWith("2"))
		            minorVersion = "2";
		        //System.out.println("majorVersion: " + majorVersion);
		        //System.out.println("minorVersion: " + minorVersion);
		        try
	            {
                    Integer imajor = Integer.valueOf(majorVersion);
		            major = imajor.intValue();
		            Integer iminor = Integer.valueOf(minorVersion);
		            minor = iminor.intValue();
		        }
		        catch (NumberFormatException nfe2)
		        {
		            System.out.println("Could not parse version: " + nfe2);
		            // Still couldn't parse the vesion
		            // have to give up
		        }
            }
            else
            {
                System.out.println("NumberFormatException thrown trying to parse the version. " + javaVersion);
                System.out.println("The test harness only handles the HP special case.");
            }
                
        }
    }

    /**
     * Remove the Open JDK early access suffix from a version string so
     * that we can run tests against early access versions of the JDK
     * and identify JDK regressions early on.
     *
     * @param javaVersion The original version string
     *
     * @return the version string after stripping off the suffix
     */
    private String stripEarlyAccessSuffix(String javaVersion)
    {
        int suffixIndex = javaVersion.indexOf(EARLY_ACCESS_SUFFIX);

        // nothing to do if this isn't an early access version
        if (suffixIndex < 0) { return javaVersion; }

        return javaVersion.substring(0, suffixIndex);
    }

    public String getMajorVersion()
    {
        return majorVersion;
    }
    
    public String getMinorVersion()
    {
        return minorVersion;
    }
    
    public int getMajorNumber()
    {
        return major;
    }
    
    public int getMinorNumber()
    {
        return minor;
    }

	/**
	 * <p>
	 * Return true if we are at least at the passed in version.
	 * </p>
	 */
	public	boolean	atLeast( int baseMajor, int baseMinor )
	{
		if ( major < baseMajor ) { return false; }
		if ( major > baseMajor ) { return true; }

		// same major number

		return ( minor >= baseMinor );
	}

}
