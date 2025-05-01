/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OSphase1_22795_22840;
import java.util.BitSet;
/**
 *
 * @author dell
 */
public class Registers {
    
    public byte[] gpr;
    public byte[] spr;
    public int flag;
    public BitSet FlagRegister;
    
    public Registers(){
        gpr = new byte[16];
        spr = new byte[16];
        flag = 15; //index of flag register in spr
        FlagRegister = new BitSet(16);
    }
    
     /*public void setRegister(int i , byte value){
         spr[i] = value;
     }*/

     public String printReg(byte[] array){
        String str="";
        for(int i=0; i<array.length; i++){
            str= str+" "+ array[i];
        }
        return str;
     }
}
