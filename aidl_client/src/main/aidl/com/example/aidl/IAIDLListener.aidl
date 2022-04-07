// IAIDLListener.aidl
package com.example.aidl;

// Declare any non-default types here with import statements

interface IAIDLListener {
   void onSuccess(in String message);
   void onError();
}