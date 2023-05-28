package com.ftn.sbnz.service.tests;

import com.ftn.sbnz.enums.LoyaltyType;
import com.ftn.sbnz.enums.TicketType;
import com.ftn.sbnz.enums.UserType;
import com.ftn.sbnz.model.*;
import com.ftn.sbnz.repository.RedundantPaymentEventRepository;
import org.junit.Assert;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class CEPSuspiciousTransactionTest {

    private SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private KieContainer getKieContainer(){
        KieServices ks = KieServices.Factory.get();
        return ks.newKieClasspathContainer();
    }

    @Test
    public void shouldTriggerOnlyRedundantPaymentEvent() throws ParseException {
        KieSession ksession = getKieContainer().newKieSession("cepKsession");

        User user = new User("misa@gmail.com", "", "Misa", "Jokic", "Stojanova 22", "0613005551", "Novi Sad", LoyaltyType.BRONZE, UserType.PASSENGER, false);
        Ticket ticket1 = new Ticket(1L, user, user, null,0, 0, TicketType.ECONOMIC, ft.parse("26.05.2023 20:00"));
        Ticket ticket2 = new Ticket(2L, user, user, null,0, 0, TicketType.ECONOMIC, ft.parse("26.05.2023 20:30"));
        Flight flight1 = new Flight(1L, "New York City", 7978, 100000, ft.parse("27.05.2023 20:00"), new ArrayList<>(Arrays.asList(ticket1, ticket2)), 2, false, "NYC1");

        ksession.insert(ticket1);
        ksession.insert(ticket2);
        ksession.insert(flight1);
        ksession.insert(user);
        ksession.setGlobal("flightId", 1L);
        int rulesFired = ksession.fireAllRules();
        Collection<?> redundantPaymentEvents = ksession.getObjects(new ClassObjectFilter(RedundantPaymentEvent.class));
        Collection<?> suspiciousTransactionEvents = ksession.getObjects(new ClassObjectFilter(SuspiciousTransactionEvent.class));
        Assert.assertEquals(1, rulesFired);
        Assert.assertEquals(1, redundantPaymentEvents.size());
        Assert.assertEquals(0, suspiciousTransactionEvents.size());
        Assert.assertEquals(false, user.isBlocked());
        ksession.destroy();
    }

    @Test
    public void shouldTriggerOnlySuspiciousTransactionEvent() throws ParseException {
        KieSession ksession = getKieContainer().newKieSession("cepKsession");

        User user = new User("misa@gmail.com", "", "Misa", "Jokic", "Stojanova 22", "0613005551", "Novi Sad", LoyaltyType.BRONZE, UserType.PASSENGER, false);
        //first redundant payment event
        Ticket ticket1 = new Ticket(1L, user, user, null,0, 0, TicketType.ECONOMIC, ft.parse("26.05.2023 20:00"));
        Ticket ticket2 = new Ticket(2L, user, user, null,0, 0, TicketType.ECONOMIC, ft.parse("26.05.2023 20:30"));

        //second redundant payment event
        Ticket ticket3 = new Ticket(3L, user, user, null,0, 0, TicketType.ECONOMIC, ft.parse("26.05.2023 20:40"));
        Ticket ticket4 = new Ticket(4L, user, user, null,0, 0, TicketType.ECONOMIC, ft.parse("26.05.2023 20:50"));

        Flight flight1 = new Flight(1L, "New York City", 7978, 100000, ft.parse("27.05.2023 20:00"), new ArrayList<>(Arrays.asList(ticket1, ticket2, ticket3, ticket4)), 2, false, "NYC1");

        ksession.insert(ticket1);
        ksession.insert(ticket2);
        ksession.insert(ticket3);
        ksession.insert(ticket4);
        ksession.insert(flight1);
        ksession.insert(user);
        ksession.setGlobal("flightId", 1L);
        int rulesFired = ksession.fireAllRules();
        Collection<?> redundantPaymentEvents = ksession.getObjects(new ClassObjectFilter(RedundantPaymentEvent.class));
        Collection<?> suspiciousTransactionEvents = ksession.getObjects(new ClassObjectFilter(SuspiciousTransactionEvent.class));
        Assert.assertTrue(suspiciousTransactionEvents.size() >= 1);
        Assert.assertEquals(false, user.isBlocked());
        ksession.destroy();
    }

    @Test
    public void shouldBlockUser() throws ParseException {
        KieSession ksession = getKieContainer().newKieSession("cepKsession");

        User user = new User("misa@gmail.com", "", "Misa", "Jokic", "Stojanova 22", "0613005551", "Novi Sad", LoyaltyType.BRONZE, UserType.PASSENGER, false);
        //first redundant payment event
        Ticket ticket1 = new Ticket(1L, user, user, null,0, 0, TicketType.ECONOMIC, ft.parse("26.05.2023 20:00"));
        Ticket ticket2 = new Ticket(2L, user, user, null,0, 0, TicketType.ECONOMIC, ft.parse("26.05.2023 20:30"));

        //second redundant payment event
        Ticket ticket3 = new Ticket(3L, user, user, null,0, 0, TicketType.ECONOMIC, ft.parse("26.05.2023 20:40"));
        Ticket ticket4 = new Ticket(4L, user, user, null,0, 0, TicketType.ECONOMIC, ft.parse("26.05.2023 20:50"));

        //third redundant payment event
        Ticket ticket5 = new Ticket(5L, user, user, null,0, 0, TicketType.ECONOMIC, ft.parse("26.05.2023 21:05"));
        Ticket ticket6 = new Ticket(6L, user, user, null,0, 0, TicketType.ECONOMIC, ft.parse("26.05.2023 21:10"));

        //fourth redundant payment event
        Ticket ticket7 = new Ticket(7L, user, user, null,0, 0, TicketType.ECONOMIC, ft.parse("26.05.2023 21:20"));
        Ticket ticket8 = new Ticket(8L, user, user, null,0, 0, TicketType.ECONOMIC, ft.parse("26.05.2023 21:30"));

        Flight flight1 = new Flight(1L, "New York City", 7978, 100000,
                ft.parse("27.05.2023 20:00"),
                new ArrayList<>(Arrays.asList(ticket1, ticket2, ticket3, ticket4, ticket5, ticket6, ticket7, ticket8)),
                2, false, "NYC1");

        ksession.insert(ticket1);
        ksession.insert(ticket2);
        ksession.insert(ticket3);
        ksession.insert(ticket4);
        ksession.insert(ticket5);
        ksession.insert(ticket6);
        ksession.insert(ticket7);
        ksession.insert(ticket8);
        ksession.insert(flight1);
        ksession.insert(user);
        ksession.setGlobal("flightId", 1L);
        int rulesFired = ksession.fireAllRules();
        Collection<?> redundantPaymentEvents = ksession.getObjects(new ClassObjectFilter(RedundantPaymentEvent.class));
        Collection<?> suspiciousTransactionEvents = ksession.getObjects(new ClassObjectFilter(SuspiciousTransactionEvent.class));
        Assert.assertEquals(true, user.isBlocked());
        ksession.destroy();
    }
}
