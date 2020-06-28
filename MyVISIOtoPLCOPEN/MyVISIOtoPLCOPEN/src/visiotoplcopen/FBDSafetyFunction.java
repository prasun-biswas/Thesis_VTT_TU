/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visiotoplcopen;


import java.util.*;

/**
 *
 * @author pbpras
 */
public class FBDSafetyFunction {
    
    String safetyFunctionPageID;
    
//    LinkedList<FBDConnection> fBDConnectionsLL= new LinkedList<>();
//    LinkedList<FBDBooleanLogic> fBDBooleanLogicsLL = new LinkedList<>();
//    LinkedList<FBDInput> fBDInputsLL = new LinkedList<>();
//    LinkedList<FBDOutput> fBDOutputsLL = new LinkedList<>();
//    LinkedList<FBDLimitChecker> fBDLimitCheckersLL = new LinkedList<>();
//    LinkedList<FBDVotingLogic> fBDVotingLogicsLL = new LinkedList<>();
//    LinkedList<FBDLatch> fBDLatchsLL = new LinkedList<>();
        
    LinkedHashMap<String, FBDConnection> fBDConnectionsLHM = new LinkedHashMap<>();
    LinkedHashMap<String, FBDBooleanLogic> fBDBooleanLogicsLHM = new LinkedHashMap<>();
    LinkedHashMap<String, FBDInput> fBDInputsLHM = new LinkedHashMap<>();
    LinkedHashMap<String, FBDOutput> fBDOutputsLHM = new LinkedHashMap<>();
    LinkedHashMap<String, FBDVotingLogic> fBDVotingLogicsLHM = new LinkedHashMap<>();
    LinkedHashMap<String, FBDLatch> fBDLatchsLHM = new LinkedHashMap<>();
    LinkedHashMap<String, String> idFbdElementNotConnection = new LinkedHashMap<>();

    
    
    public FBDSafetyFunction(String safetyFunctionPageID){
    
        this.safetyFunctionPageID=safetyFunctionPageID;
    }
    
}
