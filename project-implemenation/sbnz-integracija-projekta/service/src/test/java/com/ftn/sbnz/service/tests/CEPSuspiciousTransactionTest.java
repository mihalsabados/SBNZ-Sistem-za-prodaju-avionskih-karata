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
import java.util.*;

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
        Flight flight1 = new Flight(1L, "New York City", 7978, 100000, ft.parse("27.05.2023 20:00"), new ArrayList<>(List.of(ticket2)), 2, false, "NYC1");

        ksession.insert(ticket1);
        ksession.insert(flight1);
        ksession.insert(user);
        ksession.setGlobal("flightId", 1L);
        int rulesFired = ksession.fireAllRules();
        Collection<?> redundantPaymentEvents = ksession.getObjects(new ClassObjectFilter(RedundantPaymentEvent.class));
        Collection<?> suspiciousTransactionEvents = ksession.getObjects(new ClassObjectFilter(SuspiciousTransactionEvent.class));
        Assert.assertEquals(1, rulesFired);
        Assert.assertEquals(1, redundantPaymentEvents.size());
        Assert.assertEquals(0, suspiciousTransactionEvents.size());
        Assert.assertFalse(user.isBlocked());
        ksession.destroy();
    }

    @Test
    public void shouldTriggerOnlySuspiciousTransactionEvent() throws ParseException {
        KieSession ksession = getKieContainer().newKieSession("cepKsession");

        User user = new User("misa@gmail.com", "", "Misa", "Jokic", "Stojanova 22", "0613005551", "Novi Sad", LoyaltyType.BRONZE, UserType.PASSENGER, false);
        //first redundant payment event
        Date date1 = new Date();
        Date date2, redudantPaymentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.add(Calendar.HOUR_OF_DAY, -5);
        date1 = calendar.getTime();
        calendar.add(Calendar.MINUTE, -20);
        date2 = calendar.getTime();
        calendar.setTime(redudantPaymentDate);
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        redudantPaymentDate = calendar.getTime();

        Ticket ticket1 = new Ticket(1L, user, user, null,0, 0, TicketType.ECONOMIC, date1);
        Ticket ticket2 = new Ticket(2L, user, user, null,0, 0, TicketType.ECONOMIC, date2);

        Flight flight1 = new Flight(1L, "New York City", 7978, 100000, ft.parse("01.10.2023 20:00"), new ArrayList<>(List.of(ticket2)), 2, false, "NYC1");
        RedundantPaymentEvent rpe = new RedundantPaymentEvent(1L, 1L, redudantPaymentDate, user.getEmail(), 2L, 4L);

        ksession.insert(ticket1);
        ksession.insert(flight1);
        ksession.insert(user);
        ksession.insert(rpe);
        ksession.setGlobal("flightId", 1L);
        int rulesFired = ksession.fireAllRules();
        Collection<?> redundantPaymentEvents = ksession.getObjects(new ClassObjectFilter(RedundantPaymentEvent.class));
        Collection<?> suspiciousTransactionEvents = ksession.getObjects(new ClassObjectFilter(SuspiciousTransactionEvent.class));
        Assert.assertEquals(2, redundantPaymentEvents.size());
        Assert.assertEquals(1, suspiciousTransactionEvents.size());
        Assert.assertFalse(user.isBlocked());
        ksession.destroy();
    }

    @Test
    public void shouldBlockUser() throws ParseException {
        KieSession ksession = getKieContainer().newKieSession("cepKsession");

        User user = new User("misa@gmail.com", "", "Misa", "Jokic", "Stojanova 22", "0613005551", "Novi Sad", LoyaltyType.BRONZE, UserType.PASSENGER, false);
        //first redundant payment event
        Date date1 = new Date();
        Date date2, redudantPaymentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.add(Calendar.HOUR_OF_DAY, -5);
        date1 = calendar.getTime();
        calendar.add(Calendar.MINUTE, -20);
        date2 = calendar.getTime();
        calendar.setTime(redudantPaymentDate);
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        redudantPaymentDate = calendar.getTime();

        Ticket ticket1 = new Ticket(1L, user, user, null,0, 0, TicketType.ECONOMIC, date1);
        Ticket ticket2 = new Ticket(2L, user, user, null,0, 0, TicketType.ECONOMIC, date2);

        Flight flight1 = new Flight(1L, "New York City", 7978, 100000, ft.parse("02.10.2023 20:00"), new ArrayList<>(List.of(ticket2)), 2, false, "NYC1");
        RedundantPaymentEvent rpe = new RedundantPaymentEvent(1L, 1L, redudantPaymentDate, user.getEmail(), 2L, 4L);
        SuspiciousTransactionEvent ste = new SuspiciousTransactionEvent(1L, 1L,user.getEmail(), redudantPaymentDate, 3L, 4L);

        ksession.insert(ticket1);
        ksession.insert(flight1);
        ksession.insert(user);
        ksession.insert(rpe);
        ksession.insert(ste);
        ksession.setGlobal("flightId", 1L);
        int rulesFired = ksession.fireAllRules();
        Collection<?> redundantPaymentEvents = ksession.getObjects(new ClassObjectFilter(RedundantPaymentEvent.class));
        Collection<?> suspiciousTransactionEvents = ksession.getObjects(new ClassObjectFilter(SuspiciousTransactionEvent.class));
        Assert.assertEquals(2, suspiciousTransactionEvents.size());
        Assert.assertEquals(2, redundantPaymentEvents.size());
        Assert.assertTrue(user.isBlocked());
        ksession.destroy();
    }
}
