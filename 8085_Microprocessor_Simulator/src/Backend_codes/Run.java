/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Backend_codes;

import Frontend_swing_files.MainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author selva
 */
public class Run implements ActionListener {

    /**
     *
     */
    public static String op, opr, par, A, B, C, D, E, H, L, M, HL, BC, DE, PC, PSW, SP, S_flag, P_flag, Z_flag, AC_flag, CY_flag, line[];
    public static MainFrame MF = new MainFrame();
    public static char d[];
    public static int ctr, k, j_k = 0, next, adc1, ana1, ora1, xor1, rrc1, rlc1, ral1, rar1, l, jm_flag, jm_kval, inp_ct, lp = 0, runall = 0, forward = 0, click = 0, st_aadr = 65535, pre_k = 0;
    public boolean loop = false;
    public HashMap<String, Integer> lab_tab = new HashMap<>();
    public ArrayList<opcode_pc> opcodeList = new ArrayList<>();
    static Queue<String> que = new LinkedList<String>();
    static Stack<Integer> k_ret = new Stack();

    public static void main(String args[]) {
        MF.setVisible(true);
    }

    public void initialize() {

        A = "00";
        B = "00";
        C = "00";
        D = "00";
        E = "00";
        H = "00";
        L = "00";
        M = "00";
        HL = "0000";
        BC = "0000";
        DE = "0000";
        PC = "0000";
        PSW = "0000";
        SP = "0000";
        S_flag = "0";
        P_flag = "0";
        Z_flag = "0";
        AC_flag = "0";
        CY_flag = "0";
        ctr = 0;
        k = 0;
        next = 1;
        adc1 = 0;
        ana1 = 0;
        ora1 = 0;
        xor1 = 0;
        rrc1 = 0;
        rlc1 = 0;
        ral1 = 0;
        rar1 = 0;
        l = 0;
        jm_flag = 1;
        inp_ct = 0;
        que.clear();
        k_ret.clear();
        j_k = 0;

        opcodeList.clear();
        st_aadr = 65535;
        pre_k = 0;
        DefaultTableModel m = (DefaultTableModel) MF.jTable4.getModel();
        m.setRowCount(0);
        m.setRowCount(250);

    }

    public void process() {

        initialize();

        set_reg_values();
        reset_out();
        lab_tab.clear();
        int j = 0;
        ctr = 0;
        k = 0;
        String label;
        StringBuilder str = new StringBuilder();

        String text = MF.jTextPane1.getText();
        text = text.toUpperCase();
        text = text.replaceAll(",\\s*", ",");
        text = text.replaceAll("\\s*,", ",");
        text = text.replaceAll("\\s*:", ":");
        text = text.replaceAll("(?m)^[ \t]*\r?\n", "");

        MF.jTextPane1.setText(text);

        line = text.split("\n");

        for (int i = 0; i < line.length; i++) {
            line[i] = line[i].trim();
            line[i] = line[i].replaceAll(" +", " ");
            String l_text[] = line[i].split("\\s");
            if (l_text[0].endsWith(":")) {
                label = l_text[0].substring(0, l_text[0].length() - 1);
                lab_tab.put(label, i);
            } else {

            }
        }

        get_reg_values();
        out_mem_set();
        set_list_mem();
        l = opcodeList.size();
        copy();
        k = 0;

        if (runall == 1) {
            while (k < line.length) {
                Assemble();
                k++;
            }

        }

        //for (opcode_pc op : opcodeList) {
        //     System.out.println(op.address + " " + op.label + " " + op.opr + " " + op.par + " " + op.value + " " + op.bytee);
        //  }
    }//***PROCESS METHOD END***

    public void Assemble() {

        DefaultTableModel m = (DefaultTableModel) MF.jTable3.getModel();
 DefaultTableModel m1 = (DefaultTableModel) MF.jTable4.getModel();
        line[k] = line[k].replaceAll(" +", " ");
        String l_t[] = line[k].split("\\s");
        if (l_t[0].contains(":")) {
            opr = l_t[1].trim();
            par = l_t[2].trim();
        } else {
            switch (l_t.length) {
                case 2:
                    opr = l_t[0].trim();
                    par = l_t[1].trim();
                    break;
                case 1:
                    opr = l_t[0].trim();
                        par = " ";
                    break;
                default:
                    break;
            }
        }
        if (opr.equals("MOV")) {
            mov(par);
        } else if (opr.equals("MVI")) {
            mvi(par);
        } else if (opr.equals("INR")) {
            inr(par);
        } else if (opr.equals("INX")) {
            inx(par);
        } else if (opr.equals("DCR")) {
            dcr(par);
        } else if (opr.equals("DCX")) {
            dcx(par);
        } else if (opr.equals("ADD")) {
            add(par);
        } else if (opr.equals("SUB")) {
            sub(par);
        } else if (opr.equals("ADC")) {
            adc(par);
        } else if (opr.equals("ADI")) {
            adi(par);
        } else if (opr.equals("SBI")) {
            sbi(par);
        } else if (opr.equals("SUI")) {
            sbi(par);
        } else if (opr.equals("ANA")) {
            ana(par);
        } else if (opr.equals("ANI")) {
            ani(par);
        } else if (opr.equals("ORI")) {
            ori(par);
        } else if (opr.equals("ORA")) {
            ora(par);
        } else if (opr.equals("XRA")) {
            xra(par);
        } else if (opr.equals("XRI")) {
            xri(par);
        } else if (opr.equals("CMA")) {
            cma();
        } else if (opr.equals("CMC")) {
            cmc();
        } else if (opr.equals("STC")) {
            stc();
        } else if (opr.equals("CMP")) {
            cmp(par);
        } else if (opr.equals("CPI")) {
            cpi(par);
        } else if (opr.equals("RLC")) {
            rlc();
        } else if (opr.equals("RRC")) {
            rrc();
        } else if (opr.equals("RAR")) {
            rar();
        } else if (opr.equals("RAL")) {
            ral();
        } else if (opr.equals("LDA")) {
            lda(par);
        } else if (opr.equals("STA")) {
            sta(par);
        } else if (opr.equals("LXI")) {
            lxi(par);
        } else if (opr.equals("LHLD")) {
            lhld(par);
        } else if (opr.equals("SHLD")) {
            shld(par);
        } else if (opr.equals("STAX")) {
            stax(par);
        } else if (opr.equals("LDAX")) {
            ldax(par);
        } else if (opr.equals("XCHG")) {
            xchg();
        } else if (opr.equals("JMP")) {
            jmp(par);
        } else if (opr.equals("JP")) {
            jp(par);
        } else if (opr.equals("JM")) {
            jm(par);
        } else if (opr.equals("JC")) {
            jc(par);
        } else if (opr.equals("JNC")) {
            jnc(par);
        } else if (opr.equals("JZ")) {
            jz(par);
        } else if (opr.equals("JNZ")) {
            jnz(par);
        } else if (opr.equals("JPE")) {
            jpe(par);
        } else if (opr.equals("JPO")) {
            jpo(par);
        } else if (opr.equals("HLT")) {
            k = line.length;
        } else if (opr.equals("PUSH")) {
            push(par);
        } else if (opr.equals("POP")) {
            pop(par);
        } else if (opr.equals("CALL")) {
            call(par);
        } else if (opr.equals("NOP")) {

        }
        else if (opr.equals("RET")) {
            //   MF.jTextPane1.setText(Integer.toString(pre_k));
            k = k_ret.pop();
            pop_addr();
        } else {
            JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
            k = line.length;
        }

        PC = (String) m1.getValueAt(k+1,1);
        if (k == l - 1) {
            PC = String.format("%04X", (PC));
        }
        if (jm_flag != 1) {
            k = jm_kval;
            jm_flag = 1;
        }

        set_reg_values();
        MF.jLabel38.setText(opr +" "+ par);


    }//assemble

    //                          *******ARITHMETIC OPERATIONS*********
    public void aci(String parameter) {
        if (parameter.contains("H")) {
            parameter = parameter.replace("H", "");
            parameter = String.format("%02x", Integer.parseInt(parameter, 16));
        } else {
            parameter = String.format("%02x", Integer.parseInt(parameter));
        }
        adc1 = 1;
        set_add(parameter);
        adc1 = 0;

    }

    public void adc(String parameter) {
        if (parameter.equals("A")) {
            adc1 = 1;
            set_add(A);
            adc1 = 0;
        } else if (parameter.equals("B")) {
            adc1 = 1;
            set_add(B);
            adc1 = 0;
        } else if (parameter.equals("C")) {
            adc1 = 1;
            set_add(C);
            adc1 = 0;
        } else if (parameter.equals("D")) {
            adc1 = 1;
            set_add(D);
            adc1 = 0;
        } else if (parameter.equals("E")) {
            adc1 = 1;
            set_add(E);
            adc1 = 0;
        } else if (parameter.equals("H")) {
            adc1 = 1;
            set_add(H);
            adc1 = 0;
        } else if (parameter.equals("L")) {
            adc1 = 1;
            set_add(L);
            adc1 = 0;
        } else if (parameter.equals("M")) {
            adc1 = 1;
            M = get_mem_val_at(H + L);
            set_add(M);
            adc1 = 0;
        } else {
            JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
            k = line.length;
        }

    }

    public void add(String parameter) {
        if (parameter.equals("A")) {
            set_add(A);
        } else if (parameter.equals("B")) {
            set_add(B);
        } else if (parameter.equals("C")) {
            set_add(C);
        } else if (parameter.equals("D")) {
            set_add(D);
        } else if (parameter.equals("E")) {
            set_add(E);
        } else if (parameter.equals("H")) {
            set_add(H);
        } else if (parameter.equals("L")) {
            set_add(L);
        } else if (parameter.equals("M")) {
            M = get_mem_val_at(H + L);
            set_add(M);
        } else {
            JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
            k = line.length;
        }

    }//add()

    public void adi(String parameter) {

        if (parameter.contains("H")) {
            parameter = parameter.replace("H", "");
            parameter = String.format("%02x", Integer.parseInt(parameter, 16));
        } else {
            parameter = String.format("%02x", Integer.parseInt(parameter));
        }
        set_add(parameter);

    }//adi()

    public void dcr(String parameter) {
        if (parameter.equals("A")) {
            A = set_dcr_flags(A);
        } else if (parameter.equals("B")) {
            B = set_dcr_flags(B);
        } else if (parameter.equals("C")) {
            C = set_dcr_flags(C);
        } else if (parameter.equals("D")) {
            D = set_dcr_flags(D);
        } else if (parameter.equals("E")) {
            E = set_dcr_flags(E);
        } else if (parameter.equals("H")) {
            H = set_dcr_flags(H);
        } else if (parameter.equals("L")) {
            L = set_dcr_flags(L);
        } else if (parameter.equals("M")) {
            M = get_mem_val_at(H + L);
            M = set_dcr_flags(M);
        } else {
            JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
            k = line.length;
        }

    }//dcr()

    public void dcx(String parameter) {

        if (parameter.equals("H")) {
            HL = H + L;
            if (HL.equals("0000")) {
                HL = "FFFF";
            } else {
                HL = (String.format("%04x", Integer.parseInt(H + L, 16) - 1));
                HL = HL.toUpperCase();
            }
            H = HL.substring(0, 2);
            L = HL.substring(2, 4);

        } else if (parameter.equals("B")) {
            BC = B + C;
            if (BC.equals("0000")) {
                BC = "FFFF";
            } else {
                BC = (String.format("%04x", Integer.parseInt(B + C, 16) - 1));
                BC = BC.toUpperCase();
            }
            //char hl[] = HL.toCharArray();
            B = BC.substring(0, 2);
            C = BC.substring(2, 4);
        } else if (parameter.equals("D")) {
            DE = D + E;
            if (DE.equals("0000")) {
                DE = "FFFF";
            } else {
                DE = (String.format("%04x", Integer.parseInt(D + E, 16) - 1));
                DE = DE.toUpperCase();
            }
            //char hl[] = HL.toCharArray();
            D = DE.substring(0, 2);
            E = DE.substring(2, 4);

        } else if (parameter.equals("SP")) {
            SP = String.format("%04x", Integer.parseInt(SP, 16) - 1);

        } else {
            JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
            k = line.length;
        }
    }//dcx()

    public void inr(String parameter) {
        if (parameter.equals("A")) {
            A = set_inr_flags(A);
        } else if (parameter.equals("B")) {
            B = set_inr_flags(B);
        } else if (parameter.equals("C")) {
            C = set_inr_flags(C);
        } else if (parameter.equals("D")) {
            D = set_inr_flags(D);
        } else if (parameter.equals("E")) {
            E = set_inr_flags(E);
        } else if (parameter.equals("H")) {
            H = set_inr_flags(H);
        } else if (parameter.equals("L")) {
            L = set_inr_flags(L);
        } else if (parameter.equals("M")) {
            M = get_mem_val_at(H + L);
            M = set_inr_flags(M);
        } else {
            JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
            k = line.length;
        }

    }//inr()

    public void inx(String parameter) {

        if (parameter.equals("H")) {
            HL = H + L;
            if (HL.equals("FFFF")) {
                HL = "0000";
            } else {
                HL = (String.format("%04x", Integer.parseInt(H + L, 16) + 1));
                HL = HL.toUpperCase();
            }
            H = HL.substring(0, 2);
            L = HL.substring(2, 4);

        } else if (parameter.equals("B")) {
            BC = B + C;
            if (BC.equals("FFFF")) {
                BC = "0000";
            } else {
                BC = (String.format("%04x", Integer.parseInt(B + C, 16) + 1));
                BC = BC.toUpperCase();
            }
            //char hl[] = HL.toCharArray();
            B = BC.substring(0, 2);
            C = BC.substring(2, 4);

        } else if (parameter.equals("D")) {
            DE = D + E;
            if (DE.equals("FFFF")) {
                DE = "0000";
            } else {
                DE = (String.format("%04x", Integer.parseInt(D + E, 16) + 1));
                DE = DE.toUpperCase();
            }
            //char hl[] = HL.toCharArray();
            D = DE.substring(0, 2);
            E = DE.substring(2, 4);

        } else if (parameter.equals("SP")) {
            SP = String.format("%04x", Integer.parseInt(SP, 16) + 1);

        } else {
            JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
            k = line.length;
        }
    }//inx()

    public void sbi(String parameter) {
        // MF.jTextPane1.setText(A);

        int c = 0;
        if ("1".equals(CY_flag)) {
            c = 1;
        }
        if (parameter.contains("H")) {
            parameter = parameter.replace("H", "");
            parameter = String.format("%02x", Integer.parseInt(parameter, 16));
        } else {
            parameter = String.format("%02x", Integer.parseInt(parameter));
        }
        parameter = String.format("%02x", Integer.parseInt(parameter, 16) + c);
        set_sub(parameter);

    }

    public void sbb(String parameter) {
        int c = 0;
        if ("1".equals(CY_flag)) {
            c = 1;
        }
        if (parameter.equals("A")) {
            A = String.format("%02x", Integer.parseInt(A, 16) + c);
            set_sub(A);
        } else if (parameter.equals("B")) {
            B = String.format("%02x", Integer.parseInt(B, 16) + c);

            set_sub(B);
        } else if (parameter.equals("C")) {
            C = String.format("%02x", Integer.parseInt(C, 16) + c);
            set_sub(C);
        } else if (parameter.equals("D")) {
            D = String.format("%02x", Integer.parseInt(D, 16) + c);
            set_sub(D);
        } else if (parameter.equals("E")) {
            E = String.format("%02x", Integer.parseInt(E, 16) + c);
            set_sub(E);
        } else if (parameter.equals("H")) {
            H = String.format("%02x", Integer.parseInt(H, 16) + c);
            set_sub(H);
        } else if (parameter.equals("L")) {
            L = String.format("%02x", Integer.parseInt(L, 16) + c);
            set_sub(L);
        } else if (parameter.equals("M")) {
            M = get_mem_val_at(H + L);
            M = String.format("%02x", Integer.parseInt(M, 16) + c);

            set_sub(M);
        } else {
            JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
            k = line.length;
        }

    }

    public void sub(String parameter) {
        if (parameter.equals("A")) {
            set_sub(A);
        } else if (parameter.equals("B")) {
            set_sub(B);
        } else if (parameter.equals("C")) {
            set_sub(C);
        } else if (parameter.equals("D")) {
            set_sub(D);
        } else if (parameter.equals("E")) {
            set_sub(E);
        } else if (parameter.equals("H")) {
            set_sub(H);
        } else if (parameter.equals("L")) {
            set_sub(L);
        } else if (parameter.equals("M")) {
            M = get_mem_val_at(H + L);
            set_sub(M);
        } else {
            JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
            k = line.length;
        }

    }

    public void sui(String parameter) {

        if (parameter.contains("H")) {
            parameter = parameter.replace("H", "");
            parameter = String.format("%02x", Integer.parseInt(parameter, 16));
        } else {
            parameter = String.format("%02x", Integer.parseInt(parameter));
        }
        set_sub(parameter);
    }

    //                                ******LOGICAL OPERATIONS******
    public void ana(String parameter) {
        ana1 = 1;
        if (parameter.equals("A")) {
            set_ana(A);
        } else if (parameter.equals("B")) {
            set_ana(B);
        } else if (parameter.equals("C")) {
            set_ana(C);
        } else if (parameter.equals("D")) {
            set_ana(D);
        } else if (parameter.equals("E")) {
            set_ana(E);
        } else if (parameter.equals("H")) {
            set_ana(H);
        } else if (parameter.equals("L")) {
            set_ana(L);
        } else if (parameter.equals("M")) {
            M = get_mem_val_at(H + L);
            set_ana(M);
        } else {
            JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
            k = line.length;
        }
        ana1 = 0;
    }

    public void ani(String parameter) {
        ana1 = 1;
        if (parameter.contains("H")) {
            parameter = parameter.replace("H", "");
            // parameter = String.format("%02x", Integer.parseInt(parameter, 16));
        } else {
            parameter = String.format("%02x", Integer.parseInt(parameter));
        }
        set_ana(parameter);
        ana1 = 0;
    }

    public void ora(String parameter) {
        ora1 = 1;
        ora1 = 1;
        if (parameter.equals("A")) {
            set_ana(A);
        } else if (parameter.equals("B")) {
            set_ana(B);
        } else if (parameter.equals("C")) {
            set_ana(C);
        } else if (parameter.equals("D")) {
            set_ana(D);
        } else if (parameter.equals("E")) {
            set_ana(E);
        } else if (parameter.equals("H")) {
            set_ana(H);
        } else if (parameter.equals("L")) {
            set_ana(L);
        } else if (parameter.equals("M")) {
            M = get_mem_val_at(H + L);
            set_ana(M);
        } else {
            JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
            k = line.length;
        }
        ora1 = 0;
    }

    public void ori(String parameter) {
        ora1 = 1;
        if (parameter.contains("H")) {
            parameter = parameter.replace("H", "");
            // parameter = String.format("%02x", Integer.parseInt(parameter,16));
        } else {
            parameter = String.format("%02x", Integer.parseInt(parameter));
        }
        set_ana(parameter);
        op = "F6";
        ora1 = 0;
    }

    public void xra(String parameter) {
        xor1 = 1;
        if (parameter.equals("A")) {
            set_ana(A);
        } else if (parameter.equals("B")) {
            set_ana(B);
        } else if (parameter.equals("C")) {
            set_ana(C);
        } else if (parameter.equals("D")) {
            set_ana(D);
        } else if (parameter.equals("E")) {
            set_ana(E);
        } else if (parameter.equals("H")) {
            set_ana(H);
        } else if (parameter.equals("L")) {
            set_ana(L);
        } else if (parameter.equals("M")) {
            M = get_mem_val_at(H + L);
            set_ana(M);
        } else {
            JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
            k = line.length;
        }
        xor1 = 0;

    }

    public void xri(String parameter) {
        xor1 = 1;
        if (parameter.contains("H")) {
            parameter = parameter.replace("H", "");
            // parameter = String.format("%02x", Integer.parseInt(parameter));
        } else {
            parameter = String.format("%02x", Integer.parseInt(parameter));
        }
        set_ana(parameter);
        xor1 = 0;
    }

    public void cma() {
        A = String.format("%02x", ~Integer.parseInt(A, 16)).toUpperCase();
        A = A.substring(6, 8);
    }

    public void cmc() {
        if (CY_flag.equals("1")) {
            CY_flag = "0";
        } else {
            CY_flag = "1";
        }
    }

    public void stc() {
        CY_flag = "1";

    }

    public void cmp(String parameter) {

        if (parameter.equals("A")) {
            set_cmp(A);
        } else if (parameter.equals("B")) {
            set_cmp(B);
        } else if (parameter.equals("C")) {
            set_cmp(C);
        } else if (parameter.equals("D")) {
            set_cmp(D);
        } else if (parameter.equals("E")) {
            set_cmp(E);
        } else if (parameter.equals("H")) {
            set_cmp(H);
        } else if (parameter.equals("L")) {
            set_cmp(L);
        } else if (parameter.equals("M")) {
            M = get_mem_val_at(H + L);
            set_cmp(M);
        } else {
            JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
            k = line.length;
        }

    }

    public void cpi(String parameter) {

        if (parameter.contains("H")) {
            parameter = parameter.replace("H", "");
            // parameter = String.format("%02x", Integer.parseInt(parameter));
        } else {
            parameter = String.format("%02x", Integer.parseInt(parameter));
        }
        set_cmp(parameter);
    }

    public void rlc() {
        rlc1 = 1;
        set_rotation();
        rlc1 = 0;
    }

    public void rrc() {
        rrc1 = 1;
        set_rotation();
        rrc1 = 0;
    }

    public void ral() {
        ral1 = 1;
        set_rotation();
        ral1 = 0;
    }

    public void rar() {
        rar1 = 1;
        set_rotation();
        rar1 = 0;
    }

    //                                ******DATA TRANSFER OPERATIONS*********
    public void mov(String parameter) {

        switch (parameter) {
            case "A,B":
                A = B;
                break;
            case "A,C":
                A = C;
                break;
            case "A,D":
                A = D;
                break;
            case "A,E":
                A = E;
                break;
            case "A,H":
                A = H;
                break;
            case "A,L":
                A = L;
                break;
            case "B,A":
                B = A;
                break;
            case "B,C":
                B = C;
                break;
            case "B,D":
                B = D;
                break;
            case "B,E":
                B = E;
                break;
            case "B,H":
                B = H;
                break;
            case "B,L":
                B = L;
                break;
            case "C,A":
                C = A;
                break;
            case "C,B":
                C = B;
                break;
            case "C,D":
                C = D;
                break;
            case "C,E":
                C = E;
                break;
            case "C,H":
                C = H;
                break;
            case "C,L":
                C = L;
                break;
            case "D,A":
                D = A;
                break;
            case "D,B":
                D = B;
                break;
            case "D,C":
                D = C;
                break;
            case "D,E":
                D = E;
                break;
            case "D,H":
                D = H;
                break;
            case "D,L":
                D = L;
                break;
            case "E,A":
                E = A;
                break;
            case "E,B":
                E = B;
                break;
            case "E,C":
                E = C;
                break;
            case "E,D":
                E = D;
                break;
            case "E,H":
                E = H;
                break;
            case "E,L":
                E = L;
                break;
            case "H,A":
                H = A;
                break;
            case "H,B":
                H = B;
                break;
            case "H,C":
                H = C;
                break;
            case "H,D":
                H = D;
                break;
            case "H,E":
                H = E;
                break;
            case "H,L":
                H = L;
                break;
            case "L,A":
                L = A;
                break;
            case "L,B":
                L = B;
                break;
            case "L,C":
                L = C;
                break;
            case "L,D":
                L = D;
                break;
            case "L,E":
                L = E;
                break;
            case "L,H":
                L = H;
                break;
            case "M,A":
                M = A;
                set_mem_val_at(H + L, A);
                break;
            case "M,B":
                M = B;
                set_mem_val_at(H + L, B);
                break;
            case "M,C":
                M = C;
                set_mem_val_at(H + L, C);
                break;
            case "M,D":
                M = D;
                set_mem_val_at(H + L, D);
                break;
            case "M,E":
                M = E;
                set_mem_val_at(H + L, E);
                break;
            case "M,H":
                M = H;
                set_mem_val_at(H + L, H);
                break;
            case "M,L":
                M = L;
                set_mem_val_at(H + L, L);
                break;
            case "A,M":
                M = get_mem_val_at(H + L);
                A = M;
                break;
            case "B,M":
                M = get_mem_val_at(H + L);
                B = M;
                break;
            case "C,M":
                M = get_mem_val_at(H + L);
                C = M;
                break;
            case "D,M":
                M = get_mem_val_at(H + L);
                D = M;
                break;
            case "E,M":
                M = get_mem_val_at(H + L);
                E = M;
                break;
            case "H,M":
                M = get_mem_val_at(H + L);
                H = M;
                break;
            case "L,M":
                M = get_mem_val_at(H + L);
                L = M;
                break;
            case "A,A":
                break;
            case "B,B":
                break;
            case "C,C":
                break;
            case "D,D":
                break;
            case "E,E":
                break;
            case "H,H":
                break;
            case "L,L":
                break;
            default:
                JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
                k = line.length;
                break;
        }

    }//mov()

    public void mvi(String parameter) {
        String p[];
        DefaultTableModel m = (DefaultTableModel) MF.jTable3.getModel();
        p = parameter.split(",");
        if (p[1].contains("H")) {
            p[1] = p[1].replace("H", "");
        } else {
            p[1] = String.format("%02X", Integer.parseInt(p[1]));
        }
        if (p[0].equals("A")) {
            A = p[1];
        } else if (p[0].equals("B")) {
            B = p[1];
        } else if (p[0].equals("C")) {
            C = p[1];
        } else if (p[0].equals("D")) {
            D = p[1];
        } else if (p[0].equals("E")) {
            E = p[1];
        } else if (p[0].equals("H")) {
            H = p[1];
        } else if (p[0].equals("L")) {
            L = p[1];
        } else if (p[0].equals("M")) {
            M = p[1];
            set_mem_val_at(H + L, M);

        } else {
            JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
            k = line.length;
        }
    }//mvi()

    public void lda(String parameter) {
        parameter = rem_4h(parameter);
        A = get_mem_val_at(parameter);

    }

    public void lxi(String parameter) {
        String para[] = parameter.split(",");
        para[1] = rem_4h(para[1]);

        switch (para[0]) {
            case "H":
                H = para[1].substring(0, 2);
                L = para[1].substring(2, 4);
                break;
            case "B":
                B = para[1].substring(0, 2);
                C = para[1].substring(2, 4);
                break;
            case "D":
                D = para[1].substring(0, 2);
                E = para[1].substring(2, 4);
                break;
            //  D = para[0].substring(0, 2);
            //E = para[0].substring(2, 4);
            case "SP":
                break;
            default:

                JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
                k = line.length;

                break;
        }

    }

    public void sta(String parameter) {
        parameter = rem_4h(parameter);
        set_mem_val_at(parameter, A);
    }

    public void lhld(String parameter) {

        parameter = rem_4h(parameter);
        L = get_mem_val_at(parameter);

        H = get_mem_val_at(String.format("%04x", Integer.parseInt(parameter, 16) + 1));

    }

    public void shld(String parameter) {
        parameter = rem_4h(parameter);
        set_mem_val_at(parameter, L);
        set_mem_val_at(String.format("%04x", Integer.parseInt(parameter, 16) + 1), H);
    }

    public void ldax(String parameter) {
        if (parameter.equals("B")) {
            BC = B + C;
            A = get_mem_val_at(BC);
        } else if (parameter.equals("D")) {
            DE = D + E;
            A = get_mem_val_at(DE);
        } else {
            JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
            k = line.length;
        }
    }

    public void stax(String parameter) {

        if (parameter.equals("B")) {
            BC = B + C;
            set_mem_val_at(BC, A);
        } else if (parameter.equals("D")) {
            DE = D + E;
            set_mem_val_at(DE, A);
        } else {
            JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
            k = line.length;
        }
    }

    public void xchg() {
        String temp;
        temp = H;
        H = D;
        D = temp;
        temp = L;
        L = E;
        E = temp;
    }

    //                             *****BRANCH INSTRUCTIONS******
    public void jmp(String parameter) {
        jm_kval = get_k_val(parameter);

    }

    public void jm(String parameter) {
        if (S_flag.equals("1")) {
            jmp(parameter);
        }
    }

    public void jp(String parameter) {
        if (S_flag.equals("0")) {
            jmp(parameter);
        }

    }

    public void jz(String parameter) {
        if (Z_flag.equals("1")) {
            jmp(parameter);
        }
    }

    public void jnz(String parameter) {
        if (Z_flag.equals("0")) {
            jmp(parameter);
        }
    }

    public void jc(String parameter) {
        if (CY_flag.equals("1")) {
            jmp(parameter);
        }
    }

    public void jnc(String parameter) {
        if (CY_flag.equals("0")) {
            jmp(parameter);
        }
    }

    public void jpe(String parameter) {
        if (P_flag.equals("1")) {
            jmp(parameter);
        }
    }

    public void jpo(String parameter) {
        if (P_flag.equals("0")) {
            jmp(parameter);
        }
    }

    public void call(String parameter) {

        k_ret.push(k);

        jm_kval = get_k_val(parameter);
        int i = 0;

        for (opcode_pc tm : opcodeList) {
            if (parameter.equals(tm.label)) {
                i = 1;
            }
            if (i == 1) {
                parameter = tm.address;
                push_addr(parameter);
                break;
            }

        }
        if (i == 0) {

            JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
            k = line.length;

        }

    }

    public void push(String parameter) {
        String psw = "";
        if (parameter.equals("B")) {
            set_mem_val_at(String.format("%02x", st_aadr--), B);
            set_mem_val_at(String.format("%02x", st_aadr--), C);
        } else if (parameter.equals("D")) {
            set_mem_val_at(String.format("%02x", st_aadr--), D);
            set_mem_val_at(String.format("%02x", st_aadr--), E);
        } else if (parameter.equals("H")) {
            set_mem_val_at(String.format("%02x", st_aadr--), H);
            set_mem_val_at(String.format("%02x", st_aadr--), L);
        } else if (parameter.equals("PSW")) {
            set_mem_val_at(String.format("%02x", st_aadr--), psw.substring(0, 2));
            set_mem_val_at(String.format("%02x", st_aadr--), psw.substring(2));
        } else {
            JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
            k = line.length;
        }

    }

    public void pop(String parameter) {
        if (parameter.equals("B")) {
            C = get_mem_val_at(String.format("%02x", ++st_aadr));

            set_mem_val_at(String.format("%02x", st_aadr), " ");
            B = get_mem_val_at(String.format("%02x", ++st_aadr));
            set_mem_val_at(String.format("%02x", st_aadr), " ");
        } else if (parameter.equals("D")) {
            E = get_mem_val_at(String.format("%02x", ++st_aadr));
            set_mem_val_at(String.format("%02x", st_aadr), " ");
            D = get_mem_val_at(String.format("%02x", ++st_aadr));
            set_mem_val_at(String.format("%02x", st_aadr), " ");

        } else if (parameter.equals("H")) {
            L = get_mem_val_at(String.format("%02x", ++st_aadr));
            set_mem_val_at(String.format("%02x", st_aadr), " ");
            H = get_mem_val_at(String.format("%02x", ++st_aadr));
            set_mem_val_at(String.format("%02x", st_aadr), " ");
        } else if (parameter.equals("PSW")) {
            String temp = get_mem_val_at(String.format("%02x", ++st_aadr));
            set_mem_val_at(String.format("%02x", st_aadr), " ");
            A = get_mem_val_at(String.format("%02x", ++st_aadr));
            set_mem_val_at(String.format("%02x", st_aadr), " ");

        } else {
            JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
            k = line.length;
        }

    }

    public void push_addr(String parameter) {
        set_mem_val_at(String.format("%02x", st_aadr--), parameter.substring(0, 2));
        //que.add(String.format("%02x", st_aadr));

        set_mem_val_at(String.format("%02x", st_aadr--), parameter.substring(2, 4));
        // que.add(String.format("%02x", st_aadr));

    }

    public void pop_addr() {
        String suf = get_mem_val_at(String.format("%02x", ++st_aadr));
        set_mem_val_at(String.format("%02x", st_aadr), " ");
        suf = get_mem_val_at(String.format("%02x", ++st_aadr));
        set_mem_val_at(String.format("%02x", st_aadr), " ");

    }

    //                                 ****FUNCTIONS***
    public void set_reg_values() {
        A= A.toUpperCase();
        MF.jLabel7.setText(A);
        MF.jLabel8.setText(B);
        MF.jLabel9.setText(C);
        MF.jLabel10.setText(D);
        MF.jLabel11.setText(E);
        MF.jLabel12.setText(H);
        MF.jLabel14.setText(L);
        M = get_mem_val_at(H+L);
        SP = String.format("%04x", st_aadr);
        SP = SP.toUpperCase();
         MF.jLabel16.setText(SP);
        MF.jLabel33.setText(M);
        MF.jLabel15.setText(PC);
        MF.jLabel23.setText(S_flag);
        MF.jLabel24.setText(Z_flag);
        MF.jLabel25.setText(AC_flag);
        MF.jLabel26.setText(P_flag);
        MF.jLabel27.setText(CY_flag);

    }

    public void get_reg_values() {
        A = MF.jLabel7.getText();
        B = MF.jLabel8.getText();
        C = MF.jLabel9.getText();
        D = MF.jLabel10.getText();
        E = MF.jLabel11.getText();
        H = MF.jLabel12.getText();
        L = MF.jLabel14.getText();
        M = MF.jLabel33.getText();
        S_flag = MF.jLabel23.getText();
        Z_flag = MF.jLabel24.getText();
        AC_flag = MF.jLabel25.getText();
        P_flag = MF.jLabel26.getText();
        CY_flag = MF.jLabel27.getText();

    }

    public void set_add(String F) {
        String t;
        int ct = 0;
        String A1, F1;

        if (adc1 == 1) {
            if (F.contains("FF") && CY_flag.contains("1")) {
                F = "00";
            } else {
                F = String.format("%02x", Integer.parseInt(F, 16) + Integer.parseInt(CY_flag));
            }
        }

        A1 = A.substring(A.length() - 1);
        F1 = F.substring(F.length() - 1);

        if (Integer.parseInt(A1, 16) + Integer.parseInt(F1, 16) > 15) {
            AC_flag = "1";
        } else {
            AC_flag = "0";
        }
        A = String.format("%04x", Integer.parseInt(A, 16) + Integer.parseInt(F, 16));

        if (Integer.parseInt(A, 16) > 255) {
            CY_flag = "1";
            A = A.substring(2, 4);
        } else {
            CY_flag = "0";
            A = String.format("%02x", Integer.parseInt(A, 16));
        }
        t = A;
        t = String.format("%8s", Integer.toBinaryString(Integer.parseInt(t, 16))).replaceAll(" ", "0");
        S_flag = t.substring(0, 1);
        for (int i = 0; i < 8; i++) {
            if (t.charAt(i) == '1') {
                ct++;
            }
        }
        if (ct % 2 == 0) {
            P_flag = "1";
        } else {
            P_flag = "0";
        }

        if (A.equals("00")) {
            Z_flag = "1";
            P_flag = "1";
        } else {
            Z_flag = "0";
        }

        A = A.toUpperCase();

    }

    public String set_dcr_flags(String F) {
        String t;
        int ct = 0;
        AC_flag = "1";
        if (F.contains("00")) {
            AC_flag = "0";
        }

        F = String.format("%02X", Integer.parseInt(F, 16) - 1);
        if (F.equals("FFFFFFFF")) {
            F = "FF";
        }
        t = F;
        t = String.format("%8s", Integer.toBinaryString(Integer.parseInt(t, 16))).replaceAll(" ", "0");
        S_flag = t.substring(0, 1);
        for (int i = 0; i < 8; i++) {
            if (t.charAt(i) == '1') {
                ct++;
            }
        }
        if (ct % 2 == 0) {
            P_flag = "1";
        } else {
            P_flag = "0";
        }
        if (F.contains("00")) {
            Z_flag = "1";
            P_flag = "1";
        } else {
            Z_flag = "0";
        }
        return F;
    }

    public String set_inr_flags(String F) {
        String t;
        int ct = 0;

        if (F.charAt(F.length() - 1) == 'F') {
            AC_flag = "1";
        } else {
            AC_flag = "0";
        }

        F = String.format("%02X", Integer.parseInt(F, 16) + 1);
        //op = "34";
        if (F.equals("100")) {
            F = "00";
        }
        t = F;
        t = String.format("%8s", Integer.toBinaryString(Integer.parseInt(t, 16))).replaceAll(" ", "0");
        S_flag = t.substring(0, 1);
        for (int i = 0; i < 8; i++) {
            if (t.charAt(i) == '1') {
                ct++;
            }
        }
        if (ct % 2 == 0) {
            P_flag = "1";
        } else {
            P_flag = "0";
        }
        if (F.contains("00")) {
            Z_flag = "1";
            P_flag = "1";
        } else {
            Z_flag = "0";
        }
        return F;

    }

    public void set_sub(String F) {
        String t;
        int ct = 0;

        String A1 = A.substring(A.length() - 1);
        String F1 = F.substring(F.length() - 1);
        if (Integer.parseInt(A1, 16) < Integer.parseInt(F1, 16)) {
            AC_flag = "0";
        } else {
            AC_flag = "1";
        }
        if (A.equals("00")) {
            AC_flag = "0";
        }

        if (Integer.parseInt(A, 16) < Integer.parseInt(F, 16)) {
            A = String.format("%02x", Integer.parseInt(A, 16) - Integer.parseInt(F, 16));
            A = A.substring(6, 8);
            CY_flag = "1";

        } else {
            CY_flag = "0";

            A = String.format("%02x", Integer.parseInt(A, 16) - Integer.parseInt(F, 16));
        }
        t = A;
        t = String.format("%8s", Integer.toBinaryString(Integer.parseInt(t, 16))).replaceAll(" ", "0");
        S_flag = t.substring(0, 1);
        for (int i = 0; i < 8; i++) {
            if (t.charAt(i) == '1') {
                ct++;
            }
        }
        if (ct % 2 == 0) {
            P_flag = "1";
        } else {
            P_flag = "0";
        }

        if (A.equals("00")) {
            Z_flag = "1";
            P_flag = "1";
        } else {
            Z_flag = "0";
        }

        A = A.toUpperCase();

    }

    public void set_ana(String F) {
        String t;
        int ct = 0;

        if (ana1 == 1) {
            int x = Integer.parseInt(A, 16) & Integer.parseInt(F, 16);
            A = String.format("%02x", x);
            AC_flag = "1";
            CY_flag = "0";
        } else if (ora1 == 1) {
            int x = Integer.parseInt(A, 16) | Integer.parseInt(F, 16);
            A = String.format("%02x", x);
            AC_flag = "0";
            CY_flag = "0";
        } else if (xor1 == 1) {
            int x = Integer.parseInt(A, 16) ^ Integer.parseInt(F, 16);
            A = String.format("%02x", x);
            AC_flag = "0";
            CY_flag = "0";
        }
        t = A;
        t = String.format("%8s", Integer.toBinaryString(Integer.parseInt(t, 16))).replaceAll(" ", "0");
        S_flag = t.substring(0, 1);
        for (int i = 0; i < 8; i++) {
            if (t.charAt(i) == '1') {
                ct++;
            }
        }
        if (ct % 2 == 0) {
            P_flag = "1";
        } else {
            P_flag = "0";
        }

        if (A.equals("00")) {
            Z_flag = "1";
            P_flag = "1";
        } else {
            Z_flag = "0";
        }
        A = A.toUpperCase();
    }

    public void set_cmp(String F) {

        if (Integer.parseInt(A, 16) > Integer.parseInt(F, 16)) {
            CY_flag = "0";
            Z_flag = "0";
        } else if (Integer.parseInt(A, 16) < Integer.parseInt(F, 16)) {
            CY_flag = "1";
            Z_flag = "0";
        } else if (Integer.parseInt(A, 16) == Integer.parseInt(F, 16)) {
            Z_flag = "1";
            CY_flag = "0";
        }

    }

    public void set_rotation() {
        char dr[] = {'0', '0', '0', '0', '0', '0', '0', '0'};

        String t = String.format("%8s", Integer.toBinaryString(Integer.parseInt(A, 16))).replace(" ", "0");
        char tr[] = t.toCharArray();
        if (rlc1 == 1) {
            CY_flag = t.substring(0, 1);

            for (int i = 0; i < t.length(); i++) {
                if (i != t.length() - 1) {
                    dr[i] = tr[i + 1];
                } else {
                    dr[i] = tr[0];
                }
            }
        } else if (rrc1 == 1) {
            CY_flag = t.substring(7, 8);

            for (int i = 0; i < t.length(); i++) {
                if (i != t.length() - 1) {
                    dr[i + 1] = tr[i];
                } else {
                    dr[0] = tr[i];
                }
            }
        } else if (ral1 == 1) {

            for (int i = 0; i < t.length(); i++) {
                if (i != t.length() - 1) {
                    dr[i] = tr[i + 1];
                } else {
                    dr[i] = CY_flag.charAt(0);
                    CY_flag = t.substring(0, 1);
                }
            }

        } else if (rar1 == 1) {

            for (int i = 0; i < t.length(); i++) {
                if (i != t.length() - 1) {
                    dr[i + 1] = tr[i];
                } else {
                    dr[0] = CY_flag.charAt(0);
                    CY_flag = t.substring(7, 8);

                }
            }
        }

        String k = String.copyValueOf(dr);
        A = String.format("%02x", Integer.parseInt(k, 2)).toUpperCase();

    }

    public String get_mem_val_at(String addr) {
        DefaultTableModel m = (DefaultTableModel) MF.jTable3.getModel();
        String s = "";
        int row, col, ct = 1;
        row = m.getRowCount();
        col = m.getRowCount();
        addr = addr.toUpperCase();
        for (int i = 0; i < l + inp_ct + 10; i++) {
            if (m.getValueAt(i, 0) != null) {
                if (m.getValueAt(i, 0).equals(addr)) {
                    s = (String) m.getValueAt(i, 1);
                    ct = 0;
                    // JOptionPane.showMessageDialog(MF.jTextPane1, s);
                }
            }
        }
        if (ct != 0) {
            // JOptionPane.showMessageDialog(MF.jTextPane1, "Memory value not found!!");
            s = "00";
        }
        s = s.toUpperCase();

        return s;
    }

    public void set_mem_val_at(String addr, String val) {
        DefaultTableModel m = (DefaultTableModel) MF.jTable3.getModel();
        String s = "";
        addr = addr.toUpperCase();
        val = val.toUpperCase();

        int c = 1;

        for (int i = 0; i < l + inp_ct + 10; i++) {
            if (m.getValueAt(i, 0) != null) {
                if (m.getValueAt(i, 0).equals(addr)) {
                    m.setValueAt(val, i, 1);
                    c = 0;
                    inp_ct++;
                    break;
                }
            }
        }

        if (c != 0) {
            m.insertRow(l + 2, new Object[]{addr, val});
        }
    }

    public void copy() {
        DefaultTableModel m = (DefaultTableModel) MF.jTable3.getModel();
        DefaultTableModel n = (DefaultTableModel) MF.jTable1.getModel();
        for (int i = 0; i < 100; i++) {
            if (n.getValueAt(i, 0) != null) {
                m.insertRow(l + 2, new Object[]{n.getValueAt(i, 0), rem_2h((String)n.getValueAt(i, 1))});
                inp_ct++;
            }
        }
        // MF.jTextPane1.setText(Integer.toString(l));
    }

    public String rem_4h(String parameter) {
        parameter = parameter.toUpperCase();
        if (parameter.contains("H")) {
            parameter = parameter.replace("H", "");
            parameter = String.format("%04x", Integer.parseInt(parameter, 16));
        } else {
            parameter = String.format("%04x", Integer.parseInt(parameter));
        }
        return parameter;
    }

    public String rem_2h(String parameter) {
        parameter = parameter.toUpperCase();
        if (parameter.contains("H")) {
            parameter = parameter.replace("H", "");
            parameter = String.format("%02x", Integer.parseInt(parameter, 16));
        } else {
            parameter = String.format("%02x", Integer.parseInt(parameter));
        }
        return parameter;
    }

    public void reset_inp() {
        ///  DefaultTableModel m = (DefaultTableModel) MF.jTable3.getModel();
        DefaultTableModel n = (DefaultTableModel) MF.jTable1.getModel();
        n.setRowCount(0);
        n.setRowCount(100);
    }

    public void reset_out() {
        DefaultTableModel m = (DefaultTableModel) MF.jTable3.getModel();
        //DefaultTableModel n = (DefaultTableModel) MF.jTable1.getModel();
        m.setRowCount(0);
        m.setRowCount(250);
    }

    public void out_mem_set() {

        DefaultTableModel m = (DefaultTableModel) MF.jTable3.getModel();
        String temp, mnemonics, temp_pc, temp_par[], temp_label = "";
        int bytee, b = 0;
        StringBuilder str1 = new StringBuilder();
        for (k = 0; k < line.length; k++) {
            int cont = 1;
            line[k] = line[k].replaceAll(" +", " ");
            String l_t[] = line[k].split("\\s");
            if (l_t[0].contains(":")) {
                temp_label = l_t[0].substring(0, l_t[0].length() - 1);
                opr = l_t[1].trim();
                par = l_t[2].trim();
            } else {
                switch (l_t.length) {
                    case 2:
                        temp_label = "";
                        opr = l_t[0].trim();
                        par = l_t[1].trim();

                        break;
                    case 1:
                        temp_label = "";
                        opr = line[k].trim();
                        par = " ";
                        break;
                    default:

                        break;

                }
            }

            try {
                FileReader fileReader = new FileReader("./opcode.txt");
                try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                    try {
                        while ((temp = bufferedReader.readLine()) != null) {
                            temp = temp.replaceAll(" +", " ");
                            String file_str[] = temp.split("\\s");

                            file_str[0] = file_str[0].trim();
                            file_str[1] = file_str[1].trim();
                            file_str[2] = file_str[2].trim();

                            mnemonics = opr + par;

                            if ((mnemonics).contains(file_str[0])) {
                                cont = 0;
                                String arg = par;
                                op = file_str[1].trim();
                                bytee = Integer.parseInt(file_str[2].trim());
                                ctr = ctr + bytee;
                                opcodeList.add(new opcode_pc(k, PC, temp_label, op, opr, arg, Integer.toString(bytee)));
                                if (bytee != 1) {
                                    mnemonics = "";
                                    temp_label = "";
                                    temp_pc = String.format("%04X", (1 + Integer.parseInt(PC, 16)));
                                    if (opr.equals("MVI") || opr.equals("ACI") || opr.equals("ADI") || opr.equals("ANI") || opr.equals("CPI") || opr.equals("ORI") | opr.equals("SUI") || opr.equals("XRI")) {
                                        b++;
                                        if (opr.equals("MVI") == false) {
                                            par = rem_2h(par);

                                        } else {
                                            temp_par = par.split(",");
                                            par = rem_2h(temp_par[1]);
                                        }
                                        par = par.toUpperCase();
                                        opcodeList.add(new opcode_pc(-1, temp_pc, temp_label, par, " ", " ", " "));
                                    } else {
                                        if (opr.equals("LXI")) {
                                            temp_par = par.split(",");
                                            par = rem_2h(temp_par[1]);
                                            switch (par) {
                                                case "B":
                                                    par = B + C;
                                                    break;
                                                case "D":
                                                    par = D + E;
                                                    break;
                                                case "H":
                                                    par = H + L;
                                                    break;
                                                case "SP":
                                                    par = SP;
                                                    break;
                                                default:
                                                    break;
                                            }
                                        } else if (opr.equals("LDA") || opr.equals("LHLD") || opr.equals("SHLD") || opr.equals("STA")) {
                                            par = rem_4h(par);
                                        } else {
                                            par = "****";
                                        }
                                        opcodeList.add(new opcode_pc(-1, temp_pc, temp_label, par.substring(2, 4), " ", " ", " "));
                                        temp_pc = String.format("%04X", (2 + Integer.parseInt(PC, 16)));
                                        opcodeList.add(new opcode_pc(-1, temp_pc, temp_label, par.substring(0, 2), " ", " ", " "));

                                    }

                                }
                                PC = String.format("%04X", (ctr));
                                break;
                            }
                        }

                        //   else
                        //  cont++;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
                        initialize();
                        break;
                    }

                }
            } catch (FileNotFoundException ex) {

            } catch (IOException ex) {

                System.out.println();
            }

        }
        // MF.jTextPane1.setText(str1.toString());
    }

    public int get_k_val(String parameter) {

        int ctt = 0;
        for (Map.Entry<String, Integer> entry : lab_tab.entrySet()) {
            String label = entry.getKey();
            if (label.equals(parameter)) {
                jm_kval = entry.getValue() - 1;
                jm_flag = 0;
                ctt = 1;
            }
        }
        if (ctt != 1) {
            JOptionPane.showMessageDialog(MF.jTextPane1, "Error at line " + Integer.toString(k + 1) + " : " + line[k]);
            k = line.length;
        }
        return jm_kval;

    }

    public void set_list_mem() {

        int i =0;
        for (opcode_pc tm : opcodeList) {
            i = 0;
            if (tm.label.isEmpty() == false) {
                String parr = tm.address;
                for (opcode_pc rm : opcodeList) {

                    if (rm.par.equals(tm.label)) {
                        tm = opcodeList.get(i + 1);
                        tm.value = parr.substring(2, 4);
                        opcodeList.set(i + 1, tm);
                        tm = opcodeList.get(i + 2);
                        tm.value = parr.substring(0, 2);
                        opcodeList.set(i + 2, tm);
                    }
                    i++;
                }

            }

        }
        i = 0;
        for (opcode_pc tm : opcodeList) {

            DefaultTableModel dm = (DefaultTableModel) MF.jTable3.getModel();

            DefaultTableModel dm2 = (DefaultTableModel) MF.jTable4.getModel();

            if (tm.kl >= 0) {
                dm2.setValueAt(tm.kl, i, 0);
            } else {
                dm2.setValueAt(" ", i, 0);
            }

            dm.setValueAt(tm.address, i, 0);
            dm.setValueAt(tm.value, i, 1);

            dm2.setValueAt(tm.address, i, 1);
            dm2.setValueAt(tm.label, i, 2);
            dm2.setValueAt(tm.opr + " " + tm.par, i, 3);
            dm2.setValueAt(tm.value, i, 4);
            dm2.setValueAt(tm.bytee, i, 5);

            i++;

        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}//***RUN CLASS END***

/*
    check the functions
        add & sub finaly
        and  flag functions
        M memory value is not yet completely set
        mov & mvi instructions for memory
         check for stack pointer instructions
         jmp if condition not yet set;

ret set




three functions to be set
check all functions....esp memory
memory H values
table colour change


 */
