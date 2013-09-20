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
package dualcontrol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Properties;
import javax.net.ssl.SSLContext;

/**
 *
 * @author evan.summers
 */
public class DualControlConsole {
    private final static int PORT = 4444;
    private final static String HOST = "127.0.0.1";
    Properties properties;
    MockableConsole console;
    SSLContext sslContext;

    public static void main(String[] args) throws Exception {
        DualControlConsole instance = new DualControlConsole(System.getProperties(), 
                new ConsoleAdapter(System.console()));
        try {
            instance.init();
            instance.call();
        } finally {    
            instance.clear();
        }
    }

    public DualControlConsole(Properties properties, MockableConsole console) {
        this.properties = properties;
        this.console = console;
    }

    public void init(SSLContext sslContext) {
        this.sslContext = sslContext;
    }

    public void init() throws Exception {
        init(DualControlSSLContextFactory.createSSLContext(properties, console));
    }

    public void call() throws Exception {
        Socket socket = sslContext.getSocketFactory().createSocket(HOST, PORT);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        String purpose = dis.readUTF();
        char[] password = console.readPassword(
                "Enter passphrase for " + purpose + ": ");
        String invalidMessage = new DualControlPassphraseVerifier(properties).
                getInvalidMessage(password);
        if (invalidMessage != null) {
            console.println(invalidMessage);
        } else {
            char[] pass = console.readPassword(
                    "Re-enter passphrase for " + purpose + ": ");
            if (!Arrays.equals(password, pass)) {
                console.println("Passwords don't match.");
            } else {
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                writeChars(dos, password);
                String message = dis.readUTF();
                console.println(message);
            }
            Arrays.fill(pass, (char) 0);
        }
        Arrays.fill(password, (char) 0);
        socket.close();
    }
    
    private void clear() {
    }
    
    public static char[] writeChars(DataOutputStream dos, char[] chars) throws IOException {
        dos.writeShort(chars.length);
        for (int i = 0; i < chars.length; i++) {
            dos.writeChar(chars[i]);
        }
        return chars;
    }
    
}
