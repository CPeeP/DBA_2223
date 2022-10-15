/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helloworld;

import agents.LARVAFirstAgent;

/**
 *
 * @author hicham
 */
public class AgentLARVA extends LARVAFirstAgent{
    
    @Override
    public void setup() {
        super.setup();
        //logger.onTabular();
        Info("Configuring...");
        
    }
    @Override
    public void Execute () {
        Info("Executing...");
        this.doLARVACheckin();
        this.doLARVACheckout();
        doExit();
    }
    
    @Override
    public void takeDown () {
        Info("Taking down...");
        this.saveSequenceDiagram("seq.seqd");
        super.takeDown();
    }
}
