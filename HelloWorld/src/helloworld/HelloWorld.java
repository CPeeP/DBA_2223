/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package helloworld;

import appboot.JADEBoot;
import appboot.LARVABoot;

/**
 *
 * @author hicham
 */
public class HelloWorld {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LARVABoot boot = new LARVABoot();
        boot.Boot("isg2.ugr.es",1099);
        boot.launchAgent("Hicham", AgentLARVAFull.class);
        boot.WaitToShutDown();
    }
    
}
