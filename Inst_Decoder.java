package OSphase1_22795_22840;

import java.util.BitSet;

public class Inst_Decoder {


    Memory memory;
    Registers regs;
    byte IRvalue;
    String opcode;
    String regCodeA, regCodeB;
    public BitSet FlagRegister;

    public Inst_Decoder(Memory m, Registers r){
        this.memory = m;
        this.regs = r;
        this.FlagRegister = regs.FlagRegister;
        regCodeA = null;
        regCodeB = null;
        opcode = null;

        //initiate the fetch process
        Fetch();
    }

    public void Fetch(){
        //fetch instructions until it reaches end of instruction code
        while(memory.pc != memory.cc){
            this.IRvalue = memory.fetch();
            regs.spr[10] = this.IRvalue;
            Decode(this.IRvalue);
        }
        System.out.println(memory);
        System.exit(0);
    }

    public void Decode(byte Byte){
        //convert fetched opcode back to Hexadecimal and find instruction type
        opcode = Main.BytetoHex(Byte);
        String type = Instr_Type();

        //fetch registers and values from memory according to instr type
        if ("RRI".equals(type)){
            //fetch operands
            //get index of registers in gpr
            int A = Integer.parseInt(regCodeA, 16);
            int B = Integer.parseInt(regCodeB, 16);
            //call function
            this.RRI(A, B);
        }
        else if ("RII".equals(type)){
            //fetch operands
            byte a = memory.fetch();
            byte b = memory.fetch();
            //get index of registers in gpr
            int A = Integer.parseInt(regCodeA, 16);
            //load values a and b into registers and call function to execute
            regs.gpr[A] = a ;
            this.RII(A, b);
        }
        else if ("MI".equals(type)){
            //fetch operands
            byte a = memory.fetch();
            byte b = memory.fetch();
            //get index of registers in gpr
            int A = Integer.parseInt(regCodeA, 16);
            //load values a and b into registers and call function to execute
            regs.gpr[A] = a ;
            this.MI(A, b);
        }
        else if ("SOI".equals(type)){
            //get index of registers in gpr
            int A = Integer.parseInt(regCodeA, 16);
            this.SOI(A);
        }
        else if ("NOI".equals(type)){
            this.NOI();
        }
        Fetch();
    }

    public String Instr_Type(){
        /*FIND INSTRUCTION TYPE: REGISTER-REGISTER INSTR, REGISTER-IMMEDIATE INSTR,
          MEMORY INSTR, SINGLE OPERAND INSTR, NO OPERAND INSTR
        */
        switch(opcode.charAt(0)){
            case '1' :
                regCodeA = Main.BytetoHex(memory.fetch());
                regCodeB = Main.BytetoHex(memory.fetch());
                return "RRI";
            case '3' :
                regCodeA = Main.BytetoHex(memory.fetch());
                return "RII";
            case '5' :
                regCodeA = Main.BytetoHex(memory.fetch());
                return "MI";
            case '7' :
                regCodeA = Main.BytetoHex(memory.fetch());
                return "SOI";
            case 'F' :
                return "NOI";
        }
        System.out.println(opcode + " is an Invalid Opcode");
        return null;
    }

    // METHODS EXECUTION
    public void RRI(int ra, int rb) {
        byte r1 = regs.gpr[ra];
        byte r2 = regs.gpr[rb];
        switch (opcode.charAt(1)) {
            case '6'://MOV
                regs.gpr[ra] = r2;
                break;
            case '7'://ADD
                int sum = (int) (r1) + (int) (r2);
                r1= (byte) sum;
                regs.gpr[ra] = r1;

                if (r1 > 65536)  //setting the overflow bit to 0 or 1
                    FlagRegister.set(3, 4, true);
                else
                    FlagRegister.set(3, 4, false);

                if (r1< 0) //setting sign bit to 1 if sum<0
                    FlagRegister.set(2, 3, true);
                else
                    FlagRegister.set(2, 3, false);
                if (r1 == 0) //setting zero bit to 1 if sum=0
                    FlagRegister.set(1, 2, true);
                else
                    FlagRegister.set(1, 2, false);
                break;

            case '8'://SUB
                int diff = r1 - r2;
                r1 = (byte) diff;
                regs.gpr[ra] = r1;
                if (r1 > 65536)  //setting the overflow bit to 0 or 1
                    FlagRegister.set(3, 4, true);
                else
                    FlagRegister.set(3, 4, false);

                if (r1 < 0) //setting sign bit to 1 if difference<0
                    FlagRegister.set(2, 3, true);
                else
                    FlagRegister.set(2, 3, false);
                if (r1 == 0) //setting zero bit to 1 if difference=0
                    FlagRegister.set(1, 2, true);
                else
                    FlagRegister.set(1, 2, false);

                break;
            case '9'://MUL
                int prod = r1 * r2;
                r1 = (byte) prod;
                regs.gpr[ra] = r1;
                if (r1> 65536)  //setting the overflow bit to 0 or 1
                    FlagRegister.set(3, 4, true);
                else
                    FlagRegister.set(3, 4, false);

                if (r1< 0) //setting sign bit to 1 if product<0
                    FlagRegister.set(2, 3, true);
                else
                    FlagRegister.set(2, 3, false);

                if (r1 == 0) //setting zero bit to 1 if product=0
                    FlagRegister.set(1, 2, true);
                else
                    FlagRegister.set(1, 2, false);

                break;
            case 'A'://DIV
                int quotient = r1 / r2;
                r1 = (byte) quotient;
                regs.gpr[ra] = r1;
                if (r1> 65536)  //setting the overflow bit to 0 or 1
                    FlagRegister.set(3, 4, true);
                else
                    FlagRegister.set(3, 4, false);

                if (r1< 0) //setting sign bit to 1 if quotient<0
                    FlagRegister.set(2, 3, true);
                else
                    FlagRegister.set(2, 3, false);
                if (r1 == 0) //setting zero bit to 1 if quotient=0
                    FlagRegister.set(1, 2, true);
                else
                    FlagRegister.set(1, 2, false);

                break;
            case 'B'://AND
                int logicAND = r1& r2;
                r1 = (byte) logicAND;
                regs.gpr[ra] = r1;
                if (r1 > 65536)  //setting the overflow bit to 0 or 1
                    FlagRegister.set(3, 4, true);
                else
                    FlagRegister.set(3, 4, false);

                if (r1 < 0) //setting sign bit to 1/0
                    FlagRegister.set(2, 3, true);
                else
                    FlagRegister.set(2, 3, false);
                if (r1 == 0) //setting zero bit to 1/0
                    FlagRegister.set(1, 2, true);
                else
                    FlagRegister.set(1, 2, false);

                break;

            case 'C'://OR
                int logicOR = r1| r2;
                r1 = (byte) logicOR;
                regs.gpr[ra] = r1;
                if (r1 > 65536)  //setting the overflow bit to 0 or 1
                    FlagRegister.set(3, 4, true);
                else
                    FlagRegister.set(3, 4, false);

                if (r1< 0) //setting sign bit to 0/1
                    FlagRegister.set(2, 3, true);
                else
                    FlagRegister.set(2, 3, false);
                if (r1 == 0) //setting zero bit to 1/0
                    FlagRegister.set(1, 2, true);
                else
                    FlagRegister.set(1, 2, false);

                break;
        }

        System.out.println("General Purpose Register Contents: "+ regs.printReg(regs.gpr));
        System.out.println("Special Purpose Register Contents: "+ regs.printReg(regs.spr));
        System.out.println("Flag Bit Register Contents: "+ regs.FlagRegister);
    }

    public void RII(int ra, byte num) {//performs operations of Register-Register instructions
        byte r1 = regs.gpr[ra];
        switch (opcode.charAt(1)) {
            case '0':  //MOVI
                regs.gpr[ra] = num;
                break;
            case '1':  //ADDI
                int sum = r1 + num;
                r1 = (byte) sum;
                regs.gpr[ra] = r1;
                if (r1 > 65536)  //setting the overflow bit to 0 or 1
                    FlagRegister.set(3, 4, true);
                else
                    FlagRegister.set(3, 4, false);

                if (r1 < 0) //setting sign bit to 1 if sum<0
                    FlagRegister.set(2, 3, true);
                else
                    FlagRegister.set(2, 3, false);
                if (r1 == 0) //setting zero bit to 1 if sum=0
                    FlagRegister.set(1, 2, true);
                else
                    FlagRegister.set(1, 2, false);
                break;
            case '2':  //SUBI
                int diff = r1- num;
                r1 = (byte) diff;
                regs.gpr[ra] = r1;
                if (r1 > 65536)  //setting the overflow bit to 0 or 1
                    FlagRegister.set(3, 4, true);
                else
                    FlagRegister.set(3, 4, false);

                if (r1< 0) //setting sign bit to 1 if difference<0
                    FlagRegister.set(2, 3, true);
                else
                    FlagRegister.set(2, 3, false);
                if (r1== 0) //setting zero bit to 1 if difference=0
                    FlagRegister.set(1, 2, true);
                else
                    FlagRegister.set(1, 2, false);

                break;
            case '3':  //MULI
                int prod = r1 * num;
                r1= (byte) prod;
                regs.gpr[ra] = r1;
                if (r1> 65536)  //setting the overflow bit to 0 or 1
                    FlagRegister.set(3, 4, true);
                else
                    FlagRegister.set(3, 4, false);

                if (r1< 0) //setting sign bit to 1 if product<0
                    FlagRegister.set(2, 3, true);
                else
                    FlagRegister.set(2, 3, false);

                if (r1== 0) //setting zero bit to 1 if product=0
                    FlagRegister.set(1, 2, true);
                else
                    FlagRegister.set(1, 2, false);
                break;
            case '4':  //DIVI
                int quotient = r1/ num;
                r1= (byte) quotient;
                regs.gpr[ra] = r1;
                if (r1 > 65536)  //setting the overflow bit to 0 or 1
                    FlagRegister.set(3, 4, true);
                else
                    FlagRegister.set(3, 4, false);

                if (r1< 0) //setting sign bit to 1 if quotient<0
                    FlagRegister.set(2, 3, true);
                else
                    FlagRegister.set(2, 3, false);
                if (r1 == 0) //setting zero bit to 1 if quotient=0
                    FlagRegister.set(1, 2, true);
                else
                    FlagRegister.set(1, 2, false);
                break;
            case '5':   //ANDI
                int logicAND = r1 & num;
                r1 = (byte) logicAND;
                regs.gpr[ra] = r1;
                if (r1> 65536)  //setting the overflow bit to 0 or 1
                    FlagRegister.set(3, 4, true);
                else
                    FlagRegister.set(3, 4, false);

                if (r1 < 0) //setting sign bit to 1/0
                    FlagRegister.set(2, 3, true);
                else
                    FlagRegister.set(2, 3, false);
                if (r1 == 0) //setting zero bit to 1/0
                    FlagRegister.set(1, 2, true);
                else
                    FlagRegister.set(1, 2, false);
                break;
            case '6':  //ORI
                int logicOR = r1 | num;
                r1 = (byte) logicOR;
                regs.gpr[ra] = r1;
                if (r1> 65536)  //setting the overflow bit to 0 or 1
                    FlagRegister.set(3, 4, true);
                else
                    FlagRegister.set(3, 4, false);

                if (r1< 0) //setting sign bit to 1/0
                    FlagRegister.set(2, 3, true);
                else
                    FlagRegister.set(2, 3, false);
                if (r1 == 0) //setting zero bit to 1/0
                    FlagRegister.set(1, 2, true);
                else
                    FlagRegister.set(1, 2, false);
                break;
            //register r1 is PC for all the branch and jump operations
            case '7':  //BZ
                if (FlagRegister.get(1) == true) // if the zero bit is true increments the PC by the offset
                    regs.gpr[ra] = (byte) (r1+ (num * 4));
                break;
            case '8':   //BNZ
                if (FlagRegister.get(1) == false) // if the zero bit is false increments the PC by the offset
                    regs.gpr[ra] = (byte) (r1 + (num * 4));
                break;
            case '9':  //BC
                if (FlagRegister.get(0) == true) //if the carry bit is true increments the PC by the offset
                    regs.gpr[ra] = (byte) (r1+ (num * 4));
                break;
            case 'A':  //BS
                if (FlagRegister.get(2) == true) //if the sign bit is true increments the PC by the offset
                    regs.gpr[ra] = (byte) (r1+ (num * 4));
                break;
            case 'B':  //JUMP
                regs.gpr[ra] = (byte) (r1 + (num * 4));
                break;
            case 'C':   //CALL
                break;
            case 'D':  //ACT
                break;
        }
        System.out.println("General Purpose Register Contents: "+ regs.printReg(regs.gpr));
        System.out.println("Special Purpose Register Contents: "+ regs.printReg(regs.spr));
        System.out.println("Flag Bit Register Contents: "+ regs.FlagRegister);
    }

    public void MI(int ra, byte imm){
        int num = (int)imm;
        switch (opcode.charAt(1)) {
            case '1': //MOVL -- loads data from memory at the index num into register r1
                regs.gpr[ra] = (byte) memory.main_memory[num];
                break;

            case '2':  //MOVS -- stores data in the register r1 into memory at the index num
                memory.main_memory[num] = regs.gpr[ra];
                break;

        }
        System.out.println("General Purpose Register Contents: "+ regs.printReg(regs.gpr));
        System.out.println("Special Purpose Register Contents: "+ regs.printReg(regs.spr));
        System.out.println("Flag Bit Register Contents: "+ regs.FlagRegister);
    }

    public void SOI(int ra){
        byte r1 = regs.gpr[ra];
        switch (opcode.charAt(1)) {
            case 71: //SHL (Shift Left Logical)
                int temp = r1 << 1;
                r1= (byte) temp;
                regs.gpr[ra] = r1;
                //code for carry bit flag has to be written
                if (r1 < 0) //setting sign bit to 0/1
                    FlagRegister.set(2, 3, true);
                else
                    FlagRegister.set(2, 3, false);
                if (r1 == 0) // setting zero bit to 0/1
                    FlagRegister.set(1, 2, true);
                else
                    FlagRegister.set(1, 2, false);
                break;

            case 72: // SHR (Shift Right Logical)
                int temp1 = r1 >> 1;
                r1 = (byte) temp1;
                regs.gpr[ra] = r1;
                //code for carry bit flag has to be written
                if (r1 < 0) //setting sign bit to 0/1
                    FlagRegister.set(2, 3, true);
                else
                    FlagRegister.set(2, 3, false);
                if (r1 == 0) // setting zero bit to 0/1
                    FlagRegister.set(1, 2, true);
                else
                    FlagRegister.set(1, 2, false);
                break;

            case 73:  // RTL (Rotate Left)
                r1 = (byte) Integer.rotateLeft(r1, 1);
                regs.gpr[ra] = r1;
                if (r1 < 0) //setting sign bit to 0/1
                    FlagRegister.set(2, 3, true);
                else
                    FlagRegister.set(2, 3, false);
                if (r1== 0) // setting zero bit to 0/1
                    FlagRegister.set(1, 2, true);
                else
                    FlagRegister.set(1, 2, false);
                break;

            case 74:  //RTR  (Rotate Right)
                r1 = (byte) Integer.rotateRight(r1, 1);
                regs.gpr[ra] = r1;
                if (r1 < 0) //setting sign bit to 0/1
                    FlagRegister.set(2, 3, true);
                else
                    FlagRegister.set(2, 3, false);
                if (r1== 0) // setting zero bit to 0/1
                    FlagRegister.set(1, 2, true);
                else
                    FlagRegister.set(1, 2, false);
                break;
            case 75:  // INC (Increment)
                regs.gpr[ra] = (byte) ((int)r1+ 1);
                break;

            case 76:  // DEC (Decrement)
                r1= (byte) (r1- 1);
                regs.gpr[ra] = r1;
                if (r1 < 0) //setting sign bit to 0/1
                    FlagRegister.set(2, 3, true);
                else
                    FlagRegister.set(2, 3, false);
                if (r1 == 0) // setting zero bit to 0/1
                    FlagRegister.set(1, 2, true);
                else
                    FlagRegister.set(1, 2, false);

                break;
            /*case 77: //PUSH
                break;
            case 78:  //POP
                break;*/
        }

        System.out.println("General Purpose Register Contents: "+ regs.printReg(regs.gpr));
        System.out.println("Special Purpose Register Contents: "+ regs.printReg(regs.spr));
        System.out.println("Flag Bit Register Contents: "+ regs.FlagRegister);
    }

    public void NOI(){
        switch (opcode.charAt(1)) {
            /*case '1': // involves stack so not for phase 1
                break;*/
            case '2': // no operation
                break;
            case '3':
                System.out.println(memory);
                System.exit(0);
                break;

        }

        System.out.println("General Purpose Register Contents: "+ regs.printReg(regs.gpr));
        System.out.println("Special Purpose Register Contents: "+ regs.printReg(regs.spr));
        System.out.println("Flag Bit Register Contents: "+ regs.FlagRegister);
    }

}
