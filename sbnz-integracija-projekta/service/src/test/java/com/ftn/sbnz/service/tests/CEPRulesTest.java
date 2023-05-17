package com.ftn.sbnz.service.tests;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.ftn.sbnz.model.TransactionEvent;

public class CEPRulesTest {

    @Test
    public void testCEPRules() {
    	KieServices ks = KieServices.Factory.get();
    	KieContainer kc = ks.newKieClasspathContainer();
        KieSession ksession = kc.newKieSession("cepKsession");
        Long customerId = 1L;
        ksession.insert(new TransactionEvent(customerId, 10.00));
        ksession.insert(new TransactionEvent(customerId, 12.00));
        ksession.insert(new TransactionEvent(customerId, 14.00));
        ksession.insert(new TransactionEvent(customerId, 10.50));
        ksession.insert(new TransactionEvent(customerId, 10.99));
        ksession.insert(new TransactionEvent(customerId, 9.00));
        ksession.insert(new TransactionEvent(customerId, 11.00));
        ksession.insert(new TransactionEvent(customerId, 15.00));
        ksession.insert(new TransactionEvent(customerId, 18.00));
        ksession.insert(new TransactionEvent(customerId, 201.00));
        long ruleFireCount = ksession.fireAllRules();
        System.out.println(ruleFireCount);
        ksession.insert(new TransactionEvent(customerId, 202.00));
        ruleFireCount = ksession.fireAllRules();
        System.out.println(ruleFireCount);
    }
}
