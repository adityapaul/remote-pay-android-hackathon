package com.clover.remote.client;

import com.clover.remote.client.clovergo.CloverGoConnector;
import com.clover.remote.client.clovergo.CloverGoDeviceConfiguration;
import com.firstdata.clovergo.domain.model.ReaderInfo;

import java.util.HashMap;

public class ConnectorFactory {

    // To Support RP350 and RP450 reader connected at the same time
    private static HashMap<ReaderInfo.ReaderType, CloverGoConnector> cloverGoConnectorMap = new HashMap<>();

    public static IPaymentConnector createPaymentConnector(CloverDeviceConfiguration configuration) {
        return (IPaymentConnector) createCloverConnector(configuration);
    }

    public static ICloverConnector createCloverConnector(CloverDeviceConfiguration configuration) {
        if (configuration instanceof CloverGoDeviceConfiguration) {
            if (cloverGoConnectorMap.get(((CloverGoDeviceConfiguration) configuration).getReaderType()) == null) {
                CloverGoConnector cloverGoConnector = new CloverGoConnector((CloverGoDeviceConfiguration) configuration);
                cloverGoConnectorMap.put(((CloverGoDeviceConfiguration) configuration).getReaderType(), cloverGoConnector);

                return cloverGoConnector;

            } else {
                return cloverGoConnectorMap.get(((CloverGoDeviceConfiguration) configuration).getReaderType());
            }

        } else {
            return new CloverConnector(configuration);
        }
    }
//  public static ICloverGoConnector createCloverGoConnector(CloverGoDeviceConfiguration configuration) {
//
//    if (cloverGoConnectorMap.get(((CloverGoDeviceConfiguration) configuration).getReaderType()) == null) {
//      CloverGoConnector cloverGoConnector = new CloverGoConnector((CloverGoDeviceConfiguration) configuration);
//      cloverGoConnectorMap.put(((CloverGoDeviceConfiguration) configuration).getReaderType(), cloverGoConnector);
//      return cloverGoConnector;
//    } else
//      return cloverGoConnectorMap.get(((CloverGoDeviceConfiguration) configuration).getReaderType());
//  }
}