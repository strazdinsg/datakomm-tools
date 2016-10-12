package no.hials.modbustest;

import net.wimpi.modbus.util.BitVector;

/**
 * A demo application reading several discrete inputs from a WAGO PLC
 * with ModBus support.
 * 
 * As a demo a Wago at NTNU Aalesund was used. 
 * 
 * It is safest to test it on a local network. The Wage 750-881 which
 * Girts has, has IP address 158.38.140.34/22 , with gateway 158.38.140.1
 * 
 * In general, you should make sure the Wago device is on a network accessible
 * to the master device at a specific address.
 * 
 * @author Girts Strazdins, girts.strazdins@gmail.com, v1.0 2015-09-11 
 * @version 1.1, 2016-10-12
 */
public class Runner {
    private static final String MODBUS_SLAVE_IP = "158.38.140.34";
    private static final int MODBUS_SLAVE_PORT = 502;
    
    public static void main(String args[]) {
        ModBusClient client = new ModBusClient();
        if (client.connect(MODBUS_SLAVE_IP, MODBUS_SLAVE_PORT)) {
            System.out.println("Connected to ModBus slave");
            // Read 4 bits. The result will always be a vector with 
            // multiples of 8-bits, i.e, 8, 16, 24, etc. Even if you
            // Read just 3 bits, you will get 8 bits as response
            BitVector vals = client.readDiscreteInputs(0, 4);     
            if (vals != null) {
                System.out.println(vals.size() + " bits received");
                for (int i = 0; i < vals.size(); ++i) {
                    System.out.println("Value of bit " + i + ": " + vals.getBit(i));
                }
            } else {
                System.out.println("Could not read value");
            }
            
            client.close();            
        } else {
            System.out.println("Could not connect to ModBus slave");
        }
    }
}
