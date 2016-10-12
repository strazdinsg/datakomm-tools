package no.hials.modbustest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadInputDiscretesRequest;
import net.wimpi.modbus.msg.ReadInputDiscretesResponse;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.util.BitVector;

/**
 * A simple JAMod library wrapper with easier interface to ModBus
 * Not all the functionality is implemented, only a limited set for demo
 * @author Girts Strazdins, girts.strazdins@gmail.com, 2015-09-11 
 */
public class ModBusClient {

    TCPMasterConnection conn = null; //the connection
    ModbusTCPTransaction trans = null; //the transaction
    ReadInputDiscretesRequest req = null; //the request
    ReadInputDiscretesResponse res = null; //the response
    
    /**
     * Establish connection with Modbus slave (TCP server)
     * @param host
     * @param port
     * @return true when connection established, false otherwise
     */
    boolean connect(String host, int port) {
        try {
            InetAddress addr = InetAddress.getByName(host);
            conn = new TCPMasterConnection(addr);
            conn.setPort(port);
            conn.connect();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ModBusClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(ModBusClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }

    BitVector readDiscreteInputs(int addr, int count) {
        // Invalid number of inputs to read
        if (count < 1) return null;
        
        //3. Prepare the request
        req = new ReadInputDiscretesRequest(addr, count);

        //4. Prepare the transaction
        trans = new ModbusTCPTransaction(conn);
        trans.setRequest(req);
        
        // Read the values
        try {
            trans.execute();
        } catch (ModbusSlaveException ex) {
            System.out.println("Modbus Slave exception: " + ex.getMessage());
            return null;
        } catch (ModbusException ex) {
            System.out.println("Modbus exception: " + ex.getMessage());
            return null;
        }
        res = (ReadInputDiscretesResponse) trans.getResponse();
        BitVector bits = res.getDiscretes();
        System.out.println("Vector: " + bits.toString());
        return bits;
    }

    public void close() {
        if (conn != null) {
            conn.close();
        }
    }
}
