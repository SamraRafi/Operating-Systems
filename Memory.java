/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OSphase1_22795_22840;

import java.util.Arrays;

/**
 *
 * @author dell
 */
public class Memory {
    byte[] main_memory;
    byte[] stack;
    final int cb = 0; //code base
    int cc = 0;
    int pc;
    int output_num;

    public Memory() {
        //initialize stack and main memory
        this.main_memory = new byte[65536];
        this.pc = this.cb;
        this.output_num = 0;
    }
    
    public byte fetch(){
        byte value = this.main_memory[pc];
        pc++;
        return value;
    } 
    
    public byte load(int pc){
        byte inst = 0;
        System.out.println("Memory contents successfully loaded to register");
        return inst;
    }
    
    public void store(byte b){
        this.main_memory[cc] = b;
        cc++;
        System.out.println(b + " stored in memory");
    }
    
    /*public void store(byte b, String s){
        this.output_num ++;
        this.main_memory[cc+output_num] = b;
    }*/

    @Override
    public String toString() {
        return "Memory{" +
                "main_memory=" + Arrays.toString(main_memory) +
                '}';
    }
}
