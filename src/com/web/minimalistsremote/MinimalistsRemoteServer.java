package com.web.minimalistsremote;

import java.awt.event.KeyEvent;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

public class MinimalistsRemoteServer {

	private java.awt.Robot _robot;
	private int _event = 0;
	private int _key = 0;
	private java.net.ServerSocket _server;
	
	private static int _eventPress = 1;
	private static int _eventRelease = 2;
	private static int _eventType = 3;
	private static int _eventExit = -1;
	private byte[] _buffer = new byte[5];
	
	private boolean _bRun = true;
	
	public MinimalistsRemoteServer(int iPortNumber) {
		try{
			this._robot = new java.awt.Robot();
			this._server = new java.net.ServerSocket(iPortNumber);
			System.console().writer().println();
			System.console().writer().println("Listening on port: " + iPortNumber);
			while(_bRun){
				java.net.Socket newSocket = this._server.accept();
				while(newSocket.isConnected() && this._bRun){
					newSocket.getInputStream().read(this._buffer);
					this._event = (int)this._buffer[0];
					this._key = ((this._buffer[1]<<24) | 
							(this._buffer[2]<<16) | 
							(this._buffer[3]<<8) | 
							(this._buffer[4])
							);
					
					this._key = Protocol(this._key);
					if(this._event == MinimalistsRemoteServer._eventType) TypeKey(this._key);
					if(this._event == MinimalistsRemoteServer._eventPress) this._robot.keyPress(this._key);
					if(this._event == MinimalistsRemoteServer._eventRelease) this._robot.keyRelease(this._key);
					if(this._event == MinimalistsRemoteServer._eventExit) this._bRun = false;
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void Quit(){
		System.console().writer().println("Good bye");
	}
	
	private void TypeKey(int e){
		this._robot.keyPress(e);
		this._robot.keyRelease(e);
	}

	public static void main(String[] args) {
		System.console().writer().println("Minimalsits Remote Control Server. Press [CNTL]+C to force close.");
		int iPort = 9876;
		if(args.length > 0)
			if(args[0].toString().matches("\\d+")) iPort = Integer.parseInt(args[0]); 		
		try{
			Enumeration<NetworkInterface> interfaces = java.net.NetworkInterface.getNetworkInterfaces();
			System.console().writer().println("Interfaces:");
			while(interfaces.hasMoreElements()){
				NetworkInterface i = interfaces.nextElement();
				if(i != null)
				{
					if(!i.isUp()) continue;
					if(i.getDisplayName() != null)System.console().writer().println("\t" + i.getDisplayName());
					List<InterfaceAddress> addresses = i.getInterfaceAddresses();
					System.console().writer().println("\tAddresses:");
					for(int j=0; j<addresses.size();j++){
						System.console().writer().println("\t\t" + addresses.get(j).getAddress().toString());
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}		
		MinimalistsRemoteServer s = new MinimalistsRemoteServer(iPort);
		s.Quit();
		s = null;
	}
	
	private int Protocol(int protocolCode){
		int keyCode = 0;
		
		switch(protocolCode){
		case(0): keyCode = KeyEvent.VK_0; break;
		case(1): keyCode = KeyEvent.VK_1; break;
		case(2): keyCode = KeyEvent.VK_2; break;
		case(3): keyCode = KeyEvent.VK_3; break;
		case(4): keyCode = KeyEvent.VK_4; break;
		case(5): keyCode = KeyEvent.VK_5; break;
		case(6): keyCode = KeyEvent.VK_6; break;
		case(7): keyCode = KeyEvent.VK_7; break;
		case(8): keyCode = KeyEvent.VK_8; break;
		case(9): keyCode = KeyEvent.VK_9; break;
		case(10): keyCode = KeyEvent.VK_ESCAPE; break;
		case(11): keyCode = KeyEvent.VK_F1; break;
		case(12): keyCode = KeyEvent.VK_F2; break;
		case(13): keyCode = KeyEvent.VK_F3; break;
		case(14): keyCode = KeyEvent.VK_F4; break;
		case(15): keyCode = KeyEvent.VK_F5; break;
		case(16): keyCode = KeyEvent.VK_F6; break;
		case(17): keyCode = KeyEvent.VK_F7; break;
		case(18): keyCode = KeyEvent.VK_F8; break;
		case(19): keyCode = KeyEvent.VK_F9; break;
		case(20): keyCode = KeyEvent.VK_F10; break;
		case(21): keyCode = KeyEvent.VK_F11; break;
		case(22): keyCode = KeyEvent.VK_F12; break;
		case(23): keyCode = KeyEvent.VK_F13; break;
		case(24): keyCode = KeyEvent.VK_F14; break;
		case(25): keyCode = KeyEvent.VK_F15; break;
		case(26): keyCode = KeyEvent.VK_F16; break;
		case(27): keyCode = KeyEvent.VK_F17; break;
		case(28): keyCode = KeyEvent.VK_F18; break;
		case(29): keyCode = KeyEvent.VK_F19; break;
		case(30): keyCode = KeyEvent.VK_F20; break;
		case(31): keyCode = KeyEvent.VK_F21; break;
		case(32): keyCode = KeyEvent.VK_F22; break;
		case(33): keyCode = KeyEvent.VK_F23; break;
		case(34): keyCode = KeyEvent.VK_F24; break;
		
		default: keyCode = protocolCode;
		}
		
		return keyCode;
	}

}
