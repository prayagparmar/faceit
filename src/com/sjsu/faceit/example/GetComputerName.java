package com.sjsu.faceit.example;

import java.net.*;

public class GetComputerName
{
  public static void main(String args[]) {
  try{
  String computername=InetAddress.getLocalHost().getHostName();
  System.out.println(computername);
  }catch (Exception e){
  System.out.println("Exception caught ="+e.getMessage());
  }
  }
}
