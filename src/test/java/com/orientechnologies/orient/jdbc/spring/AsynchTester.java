/***************************************************************************
 * Copyright 2012 TXT e-solutions SpA
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This work was performed within the IoT_at_Work Project
 * and partially funded by the European Commission's
 * 7th Framework Programme under the research area ICT-2009.1.3
 * Internet of Things and enterprise environments.
 *
 * Authors:
 *      Salvatore Piccione (TXT e-solutions SpA)
 *
 * Contributors:
 *        Domenico Rotondi (TXT e-solutions SpA)
 **************************************************************************/
package com.orientechnologies.orient.jdbc.spring;

/**
 * @author Salvatore Piccione (TXT e-solutions SpA - salvatore.piccione AT network.txtgroup.com)
 *
 */
public class AsynchTester {
    private Thread thread;
    private volatile Error error;
    private volatile RuntimeException runtimeExc;

    public AsynchTester(final Runnable runnable) {
        thread = new Thread(new Runnable() {
            public void run() {
                try {
                    runnable.run();
                } catch (Error e) {
                    error = e;
                } catch (RuntimeException e) {
                    runtimeExc = e;
                }
            }
        });
    }

    public void start() {
        thread.start();
    }

    public void test() throws InterruptedException {
        thread.join();
        if (error != null)
            throw error;
        if (runtimeExc != null)
            throw runtimeExc;
    }
}
