/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OSphase1_22795_22840;

import java.io.File;
import java.util.Scanner;

/**
 *
 * @author dell
 */
import java.math.BigInteger;
public class Main {
    public static String[] hexinst;
    public static byte[] byteinst;
    
    public static byte HexToByte(String hex){  //converts hex instructions into byte

        int median = Integer.parseInt(hex, 16);
        return (byte) median;
      /*BigInteger b = BigInteger.valueOf(median);
      byte[] Byte = (b.toByteArray());
      if(hex.equals("ff") ){
          return Byte[-1];
    }
      return Byte[0];*/
      
    }
    
    public static String BytetoHex(byte b){
        String hex = Integer.toHexString(b & 0xFF);
        return hex;
     }
    
    public static void main(String[] args) throws Exception{

        hexinst=new String[100];
        byteinst=new byte[100];
        Memory memory = new Memory();
        Registers reg = new Registers();
        
        //READ FILE AND STORE HEX INSTRUCTIONS AS BYTE IN MAIN MEMORY
        File file = new File("C:\\Users\\dell\\Documents\\NetBeansProjects\\OS_Project\\src\\phase1_1\\p0.txt");
        Scanner sc = new Scanner(file);
        int i=0;
        while (sc.hasNext()) {  //reads 8 bits(2 hex digits) at a time and calls the function to convert them into byte

            //hexinst[i]=sc.next();
            //Main.byteinst[i] = HexToByte(Main.hexinst[i]);
            memory.store(HexToByte(sc.next()));
            i++;
           // System.out.println(hexinst[i]+" " +byteinst[i]);
        }
        //System.out.println(memory);
        
        //INITIATE FETCH, DECODE, EXECUTE CYCLE AND PRINT OUTPUT VALUES 
        System.out.println(memory);
        Inst_Decoder VM = new Inst_Decoder(memory, reg);
        
    }
    
}
