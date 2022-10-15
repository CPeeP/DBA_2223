/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helloworld;

import jade.core.Agent;

public class AgentJADE extends Agent {
    
    @Override
    public void setup(){
        System.out.println("Hello my name is "+this.getLocalName());
        doDelete();
    }
    
}
