
package helloworld;

import agents.LARVAFirstAgent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class AgentLARVAFull extends LARVAFirstAgent {

   
    enum Status {
        START, 
        CHECKIN, 
        OPENPROBLEM, 
        SOLVEPROBLEM, 
        CLOSEPROBLEM, 
        CHECKOUT, 
        EXIT
    }
    
    Status myStatus;
    String service = "PMANAGER", 
            problem = "AnswerToSongoanda", 
            problemManager = "", 
            sessionManager, 
            content, 
            sessionKey; 
    
    ACLMessage open, session;
    String[] contentTokens;
    
    @Override
    public void setup() {
        this.enableDeepLARVAMonitoring();
        
        super.setup();

        this.activateSequenceDiagrams();
        
        //logger.onEcho();
        
        logger.onTabular();
                
        myStatus = Status.START;
    }
    
    @Override
    public void Execute() {
        Info("Status: " + myStatus.name());
        switch (myStatus) {
            case START:
                myStatus = Status.CHECKIN;
                break;
            case CHECKIN:
                
                myStatus = MyCheckin();
                break;
            case OPENPROBLEM:
                myStatus = MyOpenProblem();
                break;
            case SOLVEPROBLEM:
                myStatus = MySolveProblem();
                break;
            case CLOSEPROBLEM:
                myStatus = MyCloseProblem();
                break;
            case CHECKOUT:
                myStatus = MyCheckout();
                break;
            case EXIT:
            default:
                doExit();
                break;
        }
    }
    
    @Override
    public void takeDown() {
        Info("Taking down...");
        this.saveSequenceDiagram("./" + getLocalName() + ".seqd");
        super.takeDown();
    }

    public Status MyCheckin() {
        Info("Loading passport and checking-in to LARVA");
        
        if (!doLARVACheckin()) {
            Error("Unable to checkin");
            return Status.EXIT;
        }
        return Status.OPENPROBLEM;
    }

    public Status MyCheckout() {
        this.doLARVACheckout();
        return Status.EXIT;
    }

    public Status MyOpenProblem() {
        if (this.DFGetAllProvidersOf(service).isEmpty()) {
            Error("Service PMANAGER is down");
            return Status.CHECKOUT;
        }
        problemManager = this.DFGetAllProvidersOf(service).get(0);
        Info("Found problem manager " + problemManager);
        
        this.outbox = new ACLMessage();
        outbox.setSender(getAID());
        outbox.addReceiver(new AID(problemManager, AID.ISLOCALNAME));
        outbox.setContent("Request open " + problem);
        this.LARVAsend(outbox);
        Info("Request opening problem " + problem + " to " + problemManager);
        
        open = LARVAblockingReceive();
        Info(problemManager + " says: " + open.getContent());
        content = open.getContent();
        contentTokens = content.split(" ");
        if (contentTokens[0].toUpperCase().equals("AGREE")) {
            sessionKey = contentTokens[4];
            session = LARVAblockingReceive();
            sessionManager = session.getSender().getLocalName();
            Info(sessionManager + " says: " + session.getContent());
            return Status.SOLVEPROBLEM;
        } else {
            Error(content);
            return Status.CHECKOUT;
        }
    }

    public Status MySolveProblem() {
        
        String clave;
        
        outbox = session.createReply();
        outbox.setContent("Hello");
        LARVAsend(outbox);
        
        inbox = this.LARVAblockingReceive();
        
        clave = inbox.getContent();
        outbox = inbox.createReply();
        String answer = new StringBuilder(clave).reverse().toString();
        outbox.setContent(answer);
        LARVAsend(outbox); 
        
        return Status.CLOSEPROBLEM;
    }

    public Status MyCloseProblem() {
        outbox = open.createReply();
        outbox.setContent("Cancel session " + sessionKey);
        Info("Closing problem Helloworld, session " + sessionKey);
        this.LARVAsend(outbox);
        inbox = LARVAblockingReceive();
        Info(problemManager + " says: " + inbox.getContent());
        return Status.CHECKOUT;
    }

}

