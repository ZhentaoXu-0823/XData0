package com.custom.mdm.util;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.INetworkManagementService;
import android.os.ServiceManager;

import java.util.List;

public class NetworkRules {
    public static int setNetworkWhitelist(List<String> ipList,List<String> ssidList) throws RemoteException {
        IBinder ibinder = ServiceManager.getService("network_management");
        INetworkManagementService mService = INetworkManagementService.Stub.asInterface(ibinder);
        //mService.enableWhite(ipList, ssidList);
        return 1;
    }

    public static int setNetworkBlacklist(List<String> ipList, List<String> ssidList) throws RemoteException {
        IBinder ibinder = ServiceManager.getService("network_management");
        INetworkManagementService mService = INetworkManagementService.Stub.asInterface(ibinder);
        //mService.enableBlack(ipList, ssidList);
        return 1;
    }

    public static int clearNetworkRules() throws RemoteException {
        IBinder ibinder = ServiceManager.getService("network_management");
        INetworkManagementService mService = INetworkManagementService.Stub.asInterface(ibinder);
        //mService.disableAllRules();
        return 1;
    }
}
