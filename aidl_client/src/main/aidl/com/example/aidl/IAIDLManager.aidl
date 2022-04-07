// IAIDLManager.aidl
package com.example.aidl;
import com.example.aidl.Dto;
import com.example.aidl.IAIDLListener;

// Declare any non-default types here with import statements

interface IAIDLManager {
   void register ();
   void unregister ();
   Dto getDataFromIPC ();
   void setDataToIPC(in Dto data1, in IAIDLListener listener);
   void startIPCActivity(in int code);
}