package com.ftn.sbnz.service.tests;

import com.ftn.sbnz.enums.LoyaltyType;
import com.ftn.sbnz.enums.TicketType;
import com.ftn.sbnz.enums.UserType;
import com.ftn.sbnz.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;


public class CEPLastMinuteDiscountTest {

    private SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private KieContainer getKieContainer(){
        KieServices ks = KieServices.Factory.get();
        return ks.newKieClasspathContainer();
    }

    @Test
    public void fireFirstRule() throws ParseException {
        KieSession ksession = getKieContainer().newKieSession("cepDiscountKsession");

        User user = new User("misa@gmail.com", "", "Misa", "Jokic", "Stojanova 22", "0613005551", "Novi Sad", LoyaltyType.BRONZE, UserType.PASSENGER, false);
        Flight flight1 = new Flight(1L, "New York City", 7978, 100000, ft.parse("27.05.2023 20:00"), new ArrayList<>(), 2, false, "NYC1");
        Discount discount = new Discount("less than 4 seats", 10);
        Ticket ticket = new Ticket(1L, user, user, null,0, 0, TicketType.ECONOMIC, ft.parse("26.05.2023 20:00"));

        ksession.insert(ticket);
        ksession.insert(flight1);
        ksession.insert(discount);
        ksession.setGlobal("flightId", 1L);
        int rulesFired = ksession.fireAllRules();
        Collection<?> events = ksession.getObjects(new ClassObjectFilter(LastMinuteEvent.class));
        Assert.assertEquals(1, rulesFired);
        Assert.assertEquals(1, events.size());
        Assert.assertEquals(ticket.getDiscounts().size(), 1);
        ksession.destroy();
    }

    @Test
    public void fireFirstTwoRules() throws ParseException {
        KieSession ksession = getKieContainer().newKieSession("cepDiscountKsession");

        User user = new User("misa@gmail.com", "", "Misa", "Jokic", "Stojanova 22", "0613005551", "Novi Sad", LoyaltyType.BRONZE, UserType.PASSENGER, false);

        Flight flight1 = new Flight(1L, "New York City", 7978, 100000, ft.parse("27.05.2023 20:00"), new ArrayList<>(), 2, false, "NYC1");
        Discount discount = new Discount("less than 4 seats", 10);

        Ticket ticket1 = new Ticket(1L, user, user, null,0, 0, TicketType.ECONOMIC, ft.parse("26.05.2023 20:00"));
        Ticket ticket2 = new Ticket(2L, user, user, null,0, 0, TicketType.ECONOMIC, ft.parse("26.05.2023 20:03"));
        ksession.insert(ticket1);
        ksession.insert(ticket2);
        ksession.insert(flight1);
        ksession.insert(discount);
        ksession.setGlobal("flightId", 1L);
        int rulesFired = ksession.fireAllRules();
        Collection<?> LastMinuteEvents = ksession.getObjects(new ClassObjectFilter(LastMinuteEvent.class));
        Assert.assertEquals(3, rulesFired);
        Assert.assertEquals(LastMinuteEvents.size(), 2);
        Assert.assertEquals(ticket1.getDiscounts().size(), 1);
        Assert.assertEquals(ticket2.getDiscounts().size(), 1);
        Assert.assertTrue(flight1.isPopular());
        ksession.destroy();
    }

    @Test
    public void fireAllRules() throws ParseException {
        KieSession ksession = getKieContainer().newKieSession("cepDiscountKsession");

        User user = new User("misa@gmail.com", "", "Misa", "Jokic", "Stojanova 22", "0613005551", "Novi Sad", LoyaltyType.BRONZE, UserType.PASSENGER, false);

        Flight flight1 = new Flight(1L, "New York City", 7978, 100000, ft.parse("27.05.2023 20:00"), new ArrayList<>(), 2, false, "NYC1");
        Flight flight2 = new Flight(2L, "New York City", 7978, 100000, ft.parse("27.05.2023 22:00"), new ArrayList<>(), 120, false, "NYC2");
        Discount discount = new Discount("less than 4 seats", 10);

        Ticket ticket1 = new Ticket(1L, user, user, null,0, 0, TicketType.ECONOMIC, ft.parse("26.05.2023 20:00"));
        Ticket ticket2 = new Ticket(2L, user, user, null,0, 0, TicketType.ECONOMIC, ft.parse("26.05.2023 20:03"));
        ksession.insert(ticket1);
        ksession.insert(ticket2);
        ksession.insert(flight1);
        ksession.insert(flight2);
        ksession.insert(discount);
        ksession.setGlobal("flightId", 1L);
        int rulesFired = ksession.fireAllRules();
        Collection<?> LastMinuteEvents = ksession.getObjects(new ClassObjectFilter(LastMinuteEvent.class));
        Assert.assertEquals(4, rulesFired);
        Assert.assertEquals(LastMinuteEvents.size(), 2);
        Assert.assertEquals(ticket1.getDiscounts().size(), 1);
        Assert.assertEquals(ticket2.getDiscounts().size(), 1);
        Assert.assertTrue(flight1.isPopular());
        Assert.assertTrue(flight2.isPopular());
        ksession.destroy();
    }
}
