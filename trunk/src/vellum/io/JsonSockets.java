/*
 * Source https://code.google.com/p/vellum by @evanxsummers

       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements. See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.  
 */
package vellum.io;

import venigma.provider.ClientContext;
import com.google.gson.Gson;
import java.io.*;
import java.net.Socket;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan.summers
 */
public class JsonSockets {
    static Logr logger = LogrFactory.getThreadLogger(JsonSockets.class);
     
    public static void write(Socket socket, Object message) throws IOException {
        String json = new Gson().toJson(message);
        byte[] bytes = json.getBytes(ClientContext.CHARSET);
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        writer.println(json);
        writer.flush();
        logger.trace("write", message.getClass(), bytes.length, new String(bytes));
    }

    public static <T> T read(Socket socket, Class messageClass) throws IOException {        
        logger.trace("read message", messageClass, socket.getInputStream().available());
        InputStreamReader reader = new InputStreamReader(socket.getInputStream());
        BufferedReader br = new BufferedReader(reader);
        String json = br.readLine();
        logger.trace("read json", json); 
        Object response = new Gson().fromJson(json, messageClass);
        logger.trace("read response", response.getClass(), response);
        return (T) response;
    }
}