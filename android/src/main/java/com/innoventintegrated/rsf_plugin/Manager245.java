package com.innoventintegrated.rsf_plugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.pda.serialport.SerialPort;
import cn.pda.serialport.Tools;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;


public class Manager245 {
	public static int Port = 12;
	public static int Power = SerialPort.Power_5v;
	SerialPort serialPort;
	InputStream inputStream;
	OutputStream outputStream;
	String stopcmd =  "FEF1A300000000000000A3FF";//
	String startcmd = "FEF1A100000000000000A1FF";//
	String setcmd =   "FEF1000000000030000030FF";//
	public Manager245() {
		serialPort = new SerialPort();
		switch (Power) {
			case SerialPort.Power_Scaner:
				serialPort.scaner_poweron();
				break;
			case SerialPort.Power_3v3:
				serialPort.power3v3on();
				break;
			case SerialPort.Power_5v:
				serialPort.power_5Von();
				break;
			case SerialPort.Power_Psam:
				serialPort.psam_poweron();
				break;
			case SerialPort.Power_Rfid:
				serialPort.rfid_poweron();
				break;
		}
		if (Port==0) serialPort.scaner_poweron();
		try {
			serialPort = new SerialPort(Port, 115200, 0);
			inputStream = serialPort.getInputStream();
			outputStream = serialPort.getOutputStream();
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
public void stopRead(){
		
		try {
			outputStream.write(Tools.HexString2Bytes(stopcmd));
			inputStream.read(new byte[4096]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean setPower(int set){
		try {
			inputStream.read(new byte[4096]);
//			Thread.sleep(10);
			byte[] setcmdbytes = Tools.HexString2Bytes(setcmd);
			setcmdbytes[7] = (byte) (set +32);
			setcmdbytes[10] = (byte) (set +32);
			inputStream.read(new byte[4096]);
			outputStream.write(setcmdbytes);
			
			Thread.sleep(50);
			byte[] bs = new byte[1024];
			int len = inputStream.read(bs);
			Log.e("set : return data", Tools.Bytes2HexString(setcmdbytes, setcmdbytes.length)+":"+Tools.Bytes2HexString(bs, len));
			if (bs[0]==(byte)0xfe&&bs[2]==(byte)0x00) {
				return true;
			}else {
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	public void startRead(){
		try {
			outputStream.write(Tools.HexString2Bytes(startcmd));
//			Log.e("start", startcmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getTag(){
		try {
			int count = inputStream.available();
			if (count>=15) {
				byte[] head = new byte[1];
				inputStream.read(head);
				if (head[0] ==(byte)0x02) {
					byte[] bs = new byte[16];
					int len = inputStream.read(bs);
//					Log.e("id:", Tools.Bytes2HexString(bs, bs.length));
					if (bs[14]==(byte)0x0D&&bs[15]==(byte)0x0A) {
						byte[] idbytes = new byte[10];
						System.arraycopy(bs, 2, idbytes, 0, 10);
//						Log.e("id:", Tools.Bytes2HexString(idbytes, 10));
						String idstring = new String(idbytes,"ASC-II");
//						Log.e("id5:", idstring);

						byte[] idb = Tools.HexString2Bytes(idstring);
						byte[] id = new byte[5];
						for (int i = 0; i < id.length; i++) {
							id[i] = idb[4-i];
						}
//						int idint = Tools.bytesToInt(id);
//						byte stateb = idb[0];
						return idstring;
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void close() {
		switch (Power) {
			case SerialPort.Power_Scaner:
				serialPort.scaner_poweroff();
				break;
			case SerialPort.Power_3v3:
				serialPort.power_3v3off();
				break;
			case SerialPort.Power_5v:
				serialPort.power_5Voff();
				break;
			case SerialPort.Power_Psam:
				serialPort.psam_poweroff();
				break;
			case SerialPort.Power_Rfid:
				serialPort.rfid_poweroff();
				break;
		}
		if (Port==0) serialPort.scaner_poweroff();
		serialPort.close(Port);

	}
	public void clear() {
		try {
			inputStream.read(new byte[4096]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
