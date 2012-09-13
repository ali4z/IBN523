package net.ibn523.business;

import java.util.HashMap;
import java.util.Map;

import net.ibn523.blocks.data.TileEntityIBN523;
import net.ibn523.network.ServerPacketHandler;
import net.ibn523.network.packages.IBN523;
import net.minecraft.src.EntityPlayerMP;

public class Processor implements Runnable {

	private String[] code;
	private Map<String,Integer> register;
	private Map<String,ProcessorInstruction> instructions;
	
	private Map<Integer,Integer> labels;
	
	private int frequency;
	
	private TileEntityIBN523 entity;
	
	private boolean isRunning = false;
	
	public Processor(int frequency, TileEntityIBN523 entity) {
		this.frequency = frequency;
		initRegistry();
		initInstructions();
		this.entity = entity;
	}
	
	private void initRegistry() {
		register = new HashMap<String,Integer>();
		register.put("NPO",0); // North Power Out	10
		register.put("SPO",0); // South Power Out	100
		register.put("WPO",0); // West Power Out	1000
		register.put("EPO",0); // East Power Out	10000
		register.put("UPO",0); // Up Power Out		100000
		register.put("DPO",0); // Down Power Out	1000000
		
		register.put("NPI",0); // North Power In	11
		register.put("SPI",0); // South Power In	101
		register.put("WPI",0); // West Power In		1001
		register.put("EPI",0); // East Power In		10001
		register.put("UPI",0); // Up Power In		100001
		register.put("DPI",0); // Down Power In		1000001
		
		register.put("IP",0); // Instruction Pointer		10000000
		
		register.put("CEF",0); // Compare Equals Flag		10000001
		register.put("CGF",0); // Compare Greater than Flag	10000010
		register.put("CLF",0); // Compare Less than Flag	10000011
		
		register.put("AX",0); // Multiply/Divide			10000100
	}
	
	private void initInstructions() {
		instructions = new HashMap<String,ProcessorInstruction>();
		instructions.put("MOV", new MOVInstruction());	// 1
		
		instructions.put("AND", new ANDInstruction());	// 10
		instructions.put("OR", new ORInstruction());	// 11
		instructions.put("XOR", new XORInstruction());	// 100
		instructions.put("NOT", new NOTInstruction());	// 101
		instructions.put("SHL", new SHLInstruction());	// 110
		instructions.put("SHR", new SHRInstruction());	// 111
		
		instructions.put("ADD", new ADDInstruction());	// 1000
		instructions.put("SUB", new SUBInstruction());	// 1001
		instructions.put("MUL", new MULInstruction());	// 1010
		instructions.put("DIV", new DIVInstruction());	// 1011
		instructions.put("INC", new INCInstruction());	// 1100
		instructions.put("DEC", new DECInstruction());  // 1101
		
		instructions.put("CMP", new CMPInstruction());	// 1110
		instructions.put("JE", new JEInstruction());	// 1111
		instructions.put("JNE", new JNEInstruction());	// 10000
		instructions.put("JG", new JGInstruction());	// 10001
		instructions.put("JL", new JLInstruction());	// 10010
		instructions.put("JMP", new JMPInstruction());	// 10011
		
		instructions.put("LBL", new LBLInstruction());	// 10100
	}
	
	private void initLabels() {
		labels = new HashMap<Integer,Integer>();
		for ( int i = 0; i < code.length; i++ ) {
			if ( code[i].contains("LBL") ) {
				String lblName =  code[i].replaceAll("[^0-9]", "");
				labels.put(Integer.parseInt(lblName), i);
			}
		}
	}
	
	public int getRegistryValue(String reg) {
		return register.get(reg);
	}
	public void setRegistryValue(String reg, int value) {
		register.put(reg,value);
	}
	
	public void setCode(String code) {
		setCode(code.split("\n"));
	}
	public void setCode(String[] code) {
		this.code = code;
		if ( code != null )
			initLabels();
	}
	public String[] getCode() {
		return code;
	}
	
	private boolean processNextLine() {
		int ip = register.get("IP");
		String[] line = code[ip].split(" ");
		
		if ( line.length == 0 ) return false;
		
		String instruction = line[0];
		
		if ( instructions.containsKey(instruction) ) {
			register.put("IP",register.get("IP")+1);
			return instructions.get(instruction).exec(line);
		}
		return false;
	}
	
	public void start() {
		Thread thr = new Thread(this);
		thr.start();
	}
	
	public void stop() {
		isRunning = false;
	}

	@Override
	public void run() {
		if ( isRunning ) return;
		
		isRunning = true;
		while ( isRunning ) {
			try {
				Thread.sleep(1000/frequency);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			int ip = register.get("IP");
			
			// Exit.
			if ( ip >= code.length || code[ip] == null || code[ip].equals("") )
				break;
			
			if ( !processNextLine() )
				break;
			
			net.ibn523.IBN523.ibn523.updateTick(entity.worldObj, entity.xCoord, entity.yCoord, entity.zCoord, null);
			
			// Assemble and send reply packet.
			IBN523 newpacket = new IBN523(entity);
			ServerPacketHandler.broadcast(newpacket);
		}
		
		register.put("IP", 0);
		IBN523 newpacket = new IBN523(entity);
		ServerPacketHandler.broadcast(newpacket);
		isRunning = false;
	}
	
	public String toString() {
		return register.toString();
	}
	
 	private interface ProcessorInstruction {
		public boolean exec(String[] line);
	}
	private class MOVInstruction implements ProcessorInstruction {
		@Override
		public boolean exec(String[] line) {
			if ( line.length != 3 ) return false;
			
			String dest = line[1].replaceAll(",", "");
			String src = line[2].replaceAll(";", "");
			
			if ( !register.containsKey(dest) ) { 
				if ( Integer.parseInt(dest) > 255 )
					return false;
				register.put(dest, 0);
			}
			
			int value;
			if ( register.containsKey(src) ) {
				value = register.get(src);
			} else {
				value = Integer.parseInt(src);
			}
			
			//System.out.println("MOV "+dest+", "+value);
			
			register.put(dest, value);
				
			return true;
		}
	}

	private class ANDInstruction implements ProcessorInstruction {

		@Override
		public boolean exec(String[] line) {
			if ( line.length != 3 ) return false;
			
			String dest = line[1].replaceAll(",", "");
			String src = line[2].replaceAll(";", "");

			if ( !register.containsKey(dest) ) 
				return false;
			

			int destValue = register.get(dest);
			int srcValue;
			if ( register.containsKey(src) ) {
				srcValue = register.get(src);
			} else {
				srcValue = Integer.parseInt(src);
			}
			
			//System.out.println("AND "+destValue+", "+srcValue);

			register.put(dest,(destValue & srcValue));
			
			return true;
		}
	}
	private class ORInstruction implements ProcessorInstruction {

		@Override
		public boolean exec(String[] line) {
			if ( line.length != 3 ) return false;
			
			String dest = line[1].replaceAll(",", "");
			String src = line[2].replaceAll(";", "");

			if ( !register.containsKey(dest) ) 
				return false;
			

			int destValue = register.get(dest);
			int srcValue;
			if ( register.containsKey(src) ) {
				srcValue = register.get(src);
			} else {
				srcValue = Integer.parseInt(src);
			}
			
			//System.out.println("OR "+destValue+", "+srcValue);

			register.put(dest,(destValue | srcValue));
			
			return true;
		}
	}
	private class XORInstruction implements ProcessorInstruction {

		@Override
		public boolean exec(String[] line) {
			if ( line.length != 3 ) return false;
			
			String dest = line[1].replaceAll(",", "");
			String src = line[2].replaceAll(";", "");

			if ( !register.containsKey(dest) ) 
				return false;
			

			int destValue = register.get(dest);
			int srcValue;
			if ( register.containsKey(src) ) {
				srcValue = register.get(src);
			} else {
				srcValue = Integer.parseInt(src);
			}
			
			//System.out.println("XOR "+destValue+", "+srcValue);

			register.put(dest,(destValue ^ srcValue));
			
			return true;
		}
	}
	private class NOTInstruction implements ProcessorInstruction {

		@Override
		public boolean exec(String[] line) {
			if ( line.length != 2 ) return false;
			
			String dest = line[1].replaceAll(";", "");

			if ( !register.containsKey(dest) ) 
				return false;
			

			int destValue = register.get(dest);
			
			//System.out.println("NOT "+destValue);

			register.put(dest,(~destValue));
			
			return true;
		}
	}
	private class SHLInstruction implements ProcessorInstruction {

		@Override
		public boolean exec(String[] line) {
			if ( line.length != 3 ) return false;
			
			String dest = line[1].replaceAll(",", "");
			String src = line[2].replaceAll(";", "");

			if ( !register.containsKey(dest) ) 
				return false;
			

			int destValue = register.get(dest);
			int srcValue;
			if ( register.containsKey(src) ) {
				srcValue = register.get(src);
			} else {
				srcValue = Integer.parseInt(src);
			}
			
			//System.out.println("SHL "+destValue+", "+srcValue);

			register.put(dest,(destValue << srcValue));
			
			return true;
		}
	}
	private class SHRInstruction implements ProcessorInstruction {


		@Override
		public boolean exec(String[] line) {
			if ( line.length != 3 ) return false;
			
			String dest = line[1].replaceAll(",", "");
			String src = line[2].replaceAll(";", "");

			if ( !register.containsKey(dest) ) 
				return false;
			

			int destValue = register.get(dest);
			int srcValue;
			if ( register.containsKey(src) ) {
				srcValue = register.get(src);
			} else {
				srcValue = Integer.parseInt(src);
			}
			
			//System.out.println("SHR "+destValue+", "+srcValue);

			register.put(dest,(destValue >> srcValue));
			
			return true;
		}
	}
	
	private class ADDInstruction implements ProcessorInstruction {

		@Override
		public boolean exec(String[] line) {
			if ( line.length != 3 ) return false;
			
			String dest = line[1].replaceAll(",", "");
			String src = line[2].replaceAll(";", "");

			if ( !register.containsKey(dest) ) 
				return false;
			

			int destValue = register.get(dest);
			int srcValue;
			if ( register.containsKey(src) ) {
				srcValue = register.get(src);
			} else {
				srcValue = Integer.parseInt(src);
			}
			
			//System.out.println("ADD "+destValue+", "+srcValue);

			register.put(dest,(destValue + srcValue));
			
			return true;
		}
	}
	private class SUBInstruction implements ProcessorInstruction {

		@Override
		public boolean exec(String[] line) {
			if ( line.length != 3 ) return false;
			
			String dest = line[1].replaceAll(",", "");
			String src = line[2].replaceAll(";", "");

			if ( !register.containsKey(dest) ) 
				return false;
			

			int destValue = register.get(dest);
			int srcValue;
			if ( register.containsKey(src) ) {
				srcValue = register.get(src);
			} else {
				srcValue = Integer.parseInt(src);
			}
			
			//System.out.println("SUB "+destValue+", "+srcValue);

			register.put(dest,(destValue - srcValue));
			
			return true;
		}
	}
	private class MULInstruction implements ProcessorInstruction {

		@Override
		public boolean exec(String[] line) {
			if ( line.length != 2 ) return false;
			
			String dest = line[1].replaceAll(";", "");

			if ( !register.containsKey(dest) ) 
				return false;
			

			int destValue = register.get(dest);
			int ax = register.get("AX");
			
			//System.out.println("MUL "+destValue);

			register.put(dest,(ax * destValue));
			
			return true;
		}
	}
	private class DIVInstruction implements ProcessorInstruction {

		@Override
		public boolean exec(String[] line) {
			if ( line.length != 2 ) return false;
			
			String dest = line[1].replaceAll(";", "");

			if ( !register.containsKey(dest) ) 
				return false;
			

			int destValue = register.get(dest);
			int ax = register.get("AX");
			
			if ( destValue == 0 ) return false;
			
			//System.out.println("DIV "+destValue);
			register.put(dest,(ax / destValue));
			
			return true;
		}
	}
	private class INCInstruction implements ProcessorInstruction {

		@Override
		public boolean exec(String[] line) {
			if ( line.length != 2 ) return false;
			
			String dest = line[1].replaceAll(";", "");

			if ( !register.containsKey(dest) ) 
				return false;
			

			int destValue = register.get(dest);
			
			//System.out.println("INC "+destValue);

			register.put(dest,destValue+1);
			
			return true;
		}
	}
	private class DECInstruction implements ProcessorInstruction {

		@Override
		public boolean exec(String[] line) {
			if ( line.length != 2 ) return false;
			
			String dest = line[1].replaceAll(";", "");

			if ( !register.containsKey(dest) ) 
				return false;
			

			int destValue = register.get(dest);
			
			//System.out.println("DEC "+destValue);

			register.put(dest,destValue-1);
			
			return true;
		}
	}
	
	private class CMPInstruction implements ProcessorInstruction {

		@Override
		public boolean exec(String[] line) {
			if ( line.length != 3 ) return false;
			
			String dest = line[1].replaceAll(",", "");
			String src = line[2].replaceAll(";", "");

			if ( !register.containsKey(dest) ) 
				return false;
			

			int destValue = register.get(dest);
			int srcValue;
			if ( register.containsKey(src) ) {
				srcValue = register.get(src);
			} else {
				srcValue = Integer.parseInt(src);
			}
			
			//System.out.println("CMP "+destValue+", "+srcValue);
			
			if ( destValue == srcValue ) register.put("CEF",1);
			else register.put("CEF",0);

			if ( destValue > srcValue ) register.put("CGF",1);
			else register.put("CGF",0);
			
			if ( destValue < srcValue ) register.put("CLF",1);
			else register.put("CLF",0);
			
			return true;
		}
	}
	private class JEInstruction implements ProcessorInstruction {

		@Override
		public boolean exec(String[] line) {
			if ( line.length != 2 ) return false;

			int destValue = Integer.parseInt(line[1].replaceAll(";", ""));
			
			if ( !labels.containsKey(destValue) ) 
				return false;
			
			int dest = labels.get(destValue);
			
			//System.out.println("JE "+dest);
			
			if ( register.get("CEF") == 1 )
				register.put("IP",dest);
			
			return true;
		}
	}
	private class JNEInstruction implements ProcessorInstruction {

		@Override
		public boolean exec(String[] line) {
			if ( line.length != 2 ) return false;
			
			int destValue = Integer.parseInt(line[1].replaceAll(";", ""));
			
			if ( !labels.containsKey(destValue) ) 
				return false;
			
			int dest = labels.get(destValue);
			
			//System.out.println("JNE "+dest);
			
			if ( register.get("CEF") == 0 )
				register.put("IP",dest);
			
			return true;
		}
	}
	private class JGInstruction implements ProcessorInstruction {

		@Override
		public boolean exec(String[] line) {
			if ( line.length != 2 ) return false;
			
			int destValue = Integer.parseInt(line[1].replaceAll(";", ""));
			
			if ( !labels.containsKey(destValue) ) 
				return false;
			
			int dest = labels.get(destValue);
			
			//System.out.println("JG "+dest);
			
			if ( register.get("CGF") == 1 )
				register.put("IP",dest);
			
			return true;
		}
	}
	private class JLInstruction implements ProcessorInstruction {

		@Override
		public boolean exec(String[] line) {
			if ( line.length != 2 ) return false;
			
			int destValue = Integer.parseInt(line[1].replaceAll(";", ""));
			
			if ( !labels.containsKey(destValue) ) 
				return false;
			
			int dest = labels.get(destValue);
			
			//System.out.println("JL "+dest);
			
			if ( register.get("CLF") == 1 )
				register.put("IP",dest);
			
			return true;
		}
	}
	private class JMPInstruction implements ProcessorInstruction {

		@Override
		public boolean exec(String[] line) {
			if ( line.length != 2 ) return false;

			int destValue = Integer.parseInt(line[1].replaceAll(";", ""));
			
			if ( !labels.containsKey(destValue) ) 
				return false;
			
			int dest = labels.get(destValue);
			
			//System.out.println("JMP "+dest);
			
			register.put("IP",dest);
			
			return true;
		}
		
	}
	
	private class LBLInstruction implements ProcessorInstruction {
		@Override
		public boolean exec(String[] line) {
			return true;
		}
	}
}
