/*

   Derby - Class org.apache.derby.impl.io.CPFile

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to you under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.apache.derby.impl.io;

import org.apache.derby.io.StorageFile;

import java.io.InputStream;

import java.io.FileNotFoundException;
import java.net.URL;

/**
 * This class provides a class path based implementation of the StorageFile interface. It is used by the
 * database engine to access persistent data and transaction logs under the classpath subsubprotocol.
 */
class CPFile extends InputStreamFile<CPStorageFactory>
{

    CPFile( CPStorageFactory storageFactory, String path)
    {
        super( storageFactory, path);
    }

    CPFile( CPStorageFactory storageFactory, String parent, String name)
    {
        super( storageFactory, parent, name);
    }

    CPFile( CPFile dir, String name)
    {
        super( dir,name);
    }

    private CPFile( CPStorageFactory storageFactory, String child, int pathLen)
    {
        super( storageFactory, child, pathLen);
    }

    /**
     * Tests whether the named file exists.
     *
     * @return <b>true</b> if the named file exists, <b>false</b> if not.
     */
    public boolean exists()
    {
    	return getURL() != null;
    } // end of exists

    /**
     * Get the parent of this file.
     *
     * @param pathLen the length of the parent's path name.
     */
    StorageFile getParentDir( int pathLen)
    {
        return new CPFile( storageFactory, path, pathLen);
    }
    
    /**
     * Creates an input stream from a file name.
     *
     * @return an input stream suitable for reading from the file.
     *
     * @exception FileNotFoundException if the file is not found.
     */
    public InputStream getInputStream( ) throws FileNotFoundException
    {
        InputStream is = null;
        ClassLoader cl = getContextClassLoader(Thread.currentThread());
        if (cl != null) {
            is = getResourceAsStream(cl, path);
        }

        // don't assume the context class loader is tied
        // into the class loader that loaded this class.
        if (is == null) {
            cl = getClass().getClassLoader();
            // Javadoc indicates implementations can use
            // null as a return from Class.getClassLoader()
            // to indicate the system/bootstrap classloader.
            if (cl != null) {
                is = getResourceAsStream(cl, path);
            } else {
                is = getSystemResourceAsStream(path);
            }
        }

        if (is == null) {
            throw new FileNotFoundException(toString());
        }

        return is;

    } // end of getInputStream
    
	/**
     * Return a URL for this file (resource).
     */
    private URL getURL() {

        ClassLoader cl = getContextClassLoader(Thread.currentThread());
        if (cl != null) {
            URL myURL = getResource(cl, path);
            if (myURL != null)
                return myURL;
        }

        // don't assume the context class loader is tied
        // into the class loader that loaded this class.
        cl = getClass().getClassLoader();
        // Javadoc indicates implementations can use
        // null as a return from Class.getClassLoader()
        // to indicate the system/bootstrap classloader.
        if (cl != null) {
            return getResource(cl, path);
        } else {
            return getSystemResource(path);
        }
    }

    private static ClassLoader getContextClassLoader(final Thread thread) {
        return thread.getContextClassLoader();
    }

    /** This used to be a Privileged wrapper for {@code ClassLoader.getResource(String)}. */
    private static URL getResource(
            final ClassLoader cl, final String name) {
        return cl.getResource(name);
    }

    /** This used to be a Privileged wrapper for {@code ClassLoader.getSystemResource(String)}. */
    private static URL getSystemResource(final String name) {
        return ClassLoader.getSystemResource(name);
    }

    /**
     * This used to be a Privileged wrapper for {@code ClassLoader.getResourceAsStream(String)}.
     */
    private static InputStream getResourceAsStream(
            final ClassLoader cl, final String name) {
        return cl.getResourceAsStream(name);
    }

    /**
     * This used to be a Privileged wrapper for
     * {@code ClassLoader.getSystemResourceAsStream(String)}.
     */
    private static InputStream getSystemResourceAsStream(final String name) {
        return ClassLoader.getSystemResourceAsStream(name);
    }
}
