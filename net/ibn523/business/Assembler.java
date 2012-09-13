package net.ibn523.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ibn523.items.data.DataPunchedCard;

public class Assembler {
	private List<DataPunchedCard> cards;
	private Map<Integer,String> instructions;
	private Map<Integer,Integer> instructionParams;
	private Map<Integer,Boolean> instructionParam1IsReg;
	private Map<Integer,String> registries;
	
	public Assembler() {
		cards = new ArrayList<DataPunchedCard>();
		initInstructions();
		initRegistries();
	}
	
	/*
	 * MOV 	-------o
	 * 
	 * AND	------o-
	 * OR	------oo
	 * XOR	-----o--
	 * NOT	-----o-o
	 * SHL	-----oo-
	 * SHR	-----ooo
	 * 
	 * ADD	----o---
	 * SUB	----o--o
	 * MUL	----o-o-
	 * DIV	----o-oo
	 * INC	----oo--
	 * DEC	----oo-o
	 * 
	 * CMP	----ooo-
	 * 
	 * JE	----oooo
	 * JNE	---o----
	 * JG	---o---o
	 * JL	---o--o-
	 * JMP	---o--oo
	 * 
	 * LBL	---o-o--
	 */
	private void initInstructions() {
		instructions = new HashMap<Integer,String>();
		instructionParams = new HashMap<Integer,Integer>();
		instructionParam1IsReg = new HashMap<Integer,Boolean>();
		
		instructions.put(1, "MOV");
		instructionParams.put(1,2);
		instructionParam1IsReg.put(1, true);
		
		instructions.put(2, "AND");
		instructionParams.put(2,2);
		instructionParam1IsReg.put(2, true);
		
		instructions.put(3, "OR");
		instructionParams.put(3,2);
		instructionParam1IsReg.put(3, true);
		
		instructions.put(4, "XOR");
		instructionParams.put(4,2);
		instructionParam1IsReg.put(4, true);
		
		instructions.put(5, "NOT");
		instructionParams.put(5,1);
		instructionParam1IsReg.put(5, true);
		
		instructions.put(6, "SHL");
		instructionParams.put(6,1);
		instructionParam1IsReg.put(6, true);

		instructions.put(7, "SHR");
		instructionParams.put(7,1);
		instructionParam1IsReg.put(7, true);
		
		
		instructions.put(8, "ADD");
		instructionParams.put(8,2);
		instructionParam1IsReg.put(8, true);
		
		instructions.put(9, "SUB");
		instructionParams.put(9,2);
		instructionParam1IsReg.put(9, true);
		
		instructions.put(10, "MUL");
		instructionParams.put(10,1);
		instructionParam1IsReg.put(10, true);
		
		instructions.put(11, "DIV");
		instructionParams.put(11,1);
		instructionParam1IsReg.put(11, true);

		instructions.put(12, "INC");
		instructionParams.put(12,1);
		instructionParam1IsReg.put(12, true);

		instructions.put(13, "DEC");
		instructionParams.put(13,1);
		instructionParam1IsReg.put(13, true);
		

		instructions.put(14, "CMP");
		instructionParams.put(14,2);
		instructionParam1IsReg.put(14, true);
		
		instructions.put(15, "JE");
		instructionParams.put(15,1);
		instructionParam1IsReg.put(15, false);
		
		instructions.put(16, "JNE");
		instructionParams.put(16,1);
		instructionParam1IsReg.put(16, false);
		
		instructions.put(17, "JG");
		instructionParams.put(17,1);
		instructionParam1IsReg.put(17, false);
		
		instructions.put(18, "JL");
		instructionParams.put(18,1);
		instructionParam1IsReg.put(18, false);
		
		instructions.put(19, "JMP");
		instructionParams.put(19,1);
		instructionParam1IsReg.put(19, false);

		
		instructions.put(20, "LBL");
		instructionParams.put(20,1);
		instructionParam1IsReg.put(20, false);
	}
	/*
	 * NPO	------o-
	 * SPO	-----o--
	 * WPO	----o---
	 * EPO	---o----
	 * UPO	--o-----
	 * DPO	-o------
	 * 
	 * NPI	------oo
	 * SPI	-----o-o
	 * WPI	----o--o
	 * EPI	---o---o
	 * UPI	--o----o
	 * DPI	-o-----o
	 * 
	 * IP	o-------
	 * 
	 * CEF	o------o
	 * CGF	o-----o-
	 * CLF	o-----oo
	 * 
	 * AX	o----o--
	 */
	private void initRegistries() {
		registries = new HashMap<Integer,String>();
		
		registries.put(2, "NPO");
		registries.put(4, "SPO");
		registries.put(8, "WPO");
		registries.put(16, "EPO");
		registries.put(32, "UPO");
		registries.put(64, "DPO");

		registries.put(3, "NPI");
		registries.put(5, "SPI");
		registries.put(9, "WPI");
		registries.put(17, "EPI");
		registries.put(33, "UPI");
		registries.put(65, "DPI");

		registries.put(128,"IP");
		
		registries.put(129,"CEF");
		registries.put(130,"CGF");
		registries.put(131,"CLF");
		
		registries.put(132,"AX");
		
	}
	
	public void setPunchCards(List<DataPunchedCard> cards) {
		this.cards = cards;
	}
	public List<DataPunchedCard> getPunchCards() {
		return this.cards;
	}
	
	public void addPunchCard(int index, DataPunchedCard card) {
		cards.add(index,card);
	}
	public void remPunchCard(int index) {
		cards.remove(index);
	}
	
	public int getNumberOfCards() {
		return cards.size();
	}
	
	public String assemble() {
		StringBuilder asm = new StringBuilder("");
		for ( DataPunchedCard card : cards ) {
			asm.append(assembleCard(card));
		}
		return asm.toString();
	}
	
	private String assembleCard(DataPunchedCard card) {
		StringBuilder asm = new StringBuilder("");
		int instruction = 0;
		for ( int lineNum = 0; lineNum < card.getNumLines(); lineNum++ ) {
			int value = card.getLineValue(lineNum);
			switch ( lineNum % 3 ) {
				case 0: // Instruction
					if ( value > 0 ) {
						if ( instructions.containsKey(value) ) {
							asm.append(instructions.get(value));
							instruction = value;
						} else
							asm.append(Integer.toString(value));
						
						if ( instructionParams.get(instruction) == 0 )
							asm.append(";\n");
					} else 
						instruction = 0;
					break;
				case 1: // Param 1
					if ( instruction > 0 && instructionParams.get(instruction) > 0 ) {
						asm.append(" ");
						if ( registries.containsKey(value) && instructionParam1IsReg.get(instruction) )
							asm.append(registries.get(value));
						else
							asm.append(Integer.toString(value));
						
						if ( instructionParams.get(instruction) == 1 )
							asm.append(";\n");
					}
					break;
				case 2: // Param 2
					if ( instruction > 0 && instructionParams.get(instruction) > 1 ) {
						asm.append(", ");
						asm.append(Integer.toString(value));
						if ( instructionParams.get(instruction) == 2 )
							asm.append(";\n");
					}
					break;
			}
		}
		return asm.toString();
	}
}
