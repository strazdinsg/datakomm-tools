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
import net.wimpi.modbus.msg.WriteCoilRequest;
import net.wimpi.modbus.msg.WriteCoilResponse;
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
    ReadInputDiscretesRequest readReq = null; //the request to read 1+ bits
    ReadInputDiscretesResponse readResp = null; //the response of retrieved bits
    WriteCoilRequest writeCoilReq = null; //the request to write one coil (one bit)
    WriteCoilResponse writeCoilResp = null; //the response of single coil write
    
    /**
     * Establish connection with Modbus slave (TCP server)
     * @param host
     * @param port
     * @return true when connection established, false otherwise
     */
    public boolean connect(String host, int port) {
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

    public BitVector readDiscreteInputs(int addr, int count) {
        // Invalid number of inputs to read
        if (count < 1) return null;
        
        //3. Prepare the request
        readReq = new ReadInputDiscretesRequest(addr, count);

        //4. Prepare the transaction
        trans = new ModbusTCPTransaction(conn);
        trans.setRequest(readReq);
        
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
        readResp = (ReadInputDiscretesResponse) trans.getResponse();
        BitVector bits = readResp.getDiscretes();
        System.out.println("Vector: " + bits.toString());
        return bits;
    }

    /**
     * Set a specific discrete output coil as true/false
     * @param addr address of the coil
     * @param value 
     * @return true on success, false on error
     */
    public boolean writeDiscreteOutput(int addr, boolean value) {
        //3. Prepare the request
        writeCoilReq = new WriteCoilRequest(addr, value);

        //4. Prepare the transaction
        trans = new ModbusTCPTransaction(conn);
        trans.setRequest(writeCoilReq);
        
        // Write the values
        try {
            trans.execute();
        } catch (ModbusSlaveException ex) {
            System.out.println("Modbus Slave exception: " + ex.getMessage());
            return false;
        } catch (ModbusException ex) {
            System.out.println("Modbus exception: " + ex.getMessage());
            return false;
        }
        writeCoilResp = (WriteCoilResponse) trans.getResponse();
        // TODO - should somehow check whether the write was successful?
        return true;
        
    }
     
    public void close() {
        if (conn != null) {
            conn.close();
        }
    }
}
