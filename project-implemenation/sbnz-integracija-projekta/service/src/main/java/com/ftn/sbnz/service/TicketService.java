package com.ftn.sbnz.service;

import com.ftn.sbnz.dto.ReportDTO;
import com.ftn.sbnz.dto.TicketDTO;
import com.ftn.sbnz.dto.ticket.PassengerDataDTO;
import com.ftn.sbnz.dto.ticket.TicketDataDTO;
import com.ftn.sbnz.enums.LoyaltyType;
import com.ftn.sbnz.enums.TicketType;
import com.ftn.sbnz.exception.UserIsBlockedException;
import com.ftn.sbnz.exception.UserNotFoundException;
import com.ftn.sbnz.model.*;
import com.ftn.sbnz.dto.ticket.TicketToShowDTO;
import com.ftn.sbnz.exception.FlightNotFoundException;
import com.ftn.sbnz.repository.*;
import lombok.AllArgsConstructor;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieServices;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;
    private final DiscountRepository discountRepository;
    private final PriceTemplateRepository priceTemplateRepository;
    private final LastMinuteEventRepository lastMinuteEventRepository;
    private final RedundantPaymentEventRepository redundantPaymentEventRepository;
    private final SuspiciousTransactionEventRepository suspiciousTransactionEventRepository;


    private KieContainer getKieContainer(){
        KieServices ks = KieServices.Factory.get();
        return ks.newKieClasspathContainer();
    }


    public TicketToShowDTO createTicket(TicketDataDTO ticketDataDTO) {
        User passenger = this.userRepository.findByEmail(ticketDataDTO.getPassengerData().getEmailPassenger()).
                orElse(createNewUser(ticketDataDTO.getPassengerData()));
        User payer = this.userRepository.findByEmail(ticketDataDTO.getPayerEmail()).
                orElseThrow(() -> new UserNotFoundException("User with this email not found!"));

        long newId = this.ticketRepository.count() + 1;

        Ticket ticket = new Ticket(newId, passenger, payer, null,0, 0,
                TicketType.valueOf(ticketDataDTO.getCardType().toUpperCase()), new Date());

        setSuspiciousTransactions(ticket, ticketDataDTO.getFlightId());
        payer = this.userRepository.findByEmail(ticketDataDTO.getPayerEmail()).
                orElseThrow(() -> new UserNotFoundException("User with this email not found!"));
        if(payer.isBlocked())
            throw new UserIsBlockedException("User is blocked exception");

        TicketToShowDTO suggestedTicketDTO = checkTicketFlight(ticketDataDTO.getFlightId(), ticket);
        long suggestedFlightId = suggestedTicketDTO.getAlternativeFlightId();
        if(!suggestedTicketDTO.isFlightFound())
            return new TicketToShowDTO(suggestedFlightId, suggestedTicketDTO.isFlightFound());
        if(suggestedTicketDTO.getAlternativeFlightId() != ticketDataDTO.getFlightId())
            return new TicketToShowDTO(suggestedFlightId, suggestedTicketDTO.isFlightFound());
        ticketRepository.save(ticket);

        setPrice(suggestedFlightId, ticket);
        setPopularFlightWithOver8000kmDiscount(ticket);
        setTicketNumberDiscount(suggestedFlightId, ticket);
        setUserLoyaltyStatus(payer, ticket);
        setUserLoyaltyDiscounts(payer, ticket);
        setLastMinuteDiscounts(ticket, ticketDataDTO.getFlightId());
        return new TicketToShowDTO(ticket, suggestedFlightId, suggestedTicketDTO.isFlightFound());
    }

    private void setLastMinuteDiscounts(Ticket ticket, Long flightId) {
        KieSession ksession = getKieContainer().newKieSession("cepDiscountKsession");

        this.flightRepository.findAll().forEach(ksession::insert);
        this.discountRepository.findAll().forEach(ksession::insert);
        this.lastMinuteEventRepository.findAll().forEach(ksession::insert);

        ksession.insert(ticket);
        ksession.setGlobal("flightId", flightId);
        int firedRules = ksession.fireAllRules();
        List<LastMinuteEvent> lmes = Arrays.stream(ksession.getObjects().toArray()).filter(obj->obj instanceof LastMinuteEvent).map(obj->(LastMinuteEvent)obj).collect(Collectors.toList());
        for (LastMinuteEvent lme : lmes) {
            if(lme.getId() == null){
                lme.setId(this.lastMinuteEventRepository.count()+1);
                this.lastMinuteEventRepository.save(lme);
            }
        }

        List<Flight> flights = Arrays.stream(ksession.getObjects().toArray()).filter(obj->obj instanceof Flight).map(obj->(Flight)obj).collect(Collectors.toList());
        this.flightRepository.saveAll(flights);
    }

    private void setSuspiciousTransactions(Ticket ticket, long flightId) {
        KieSession ksession = getKieContainer().newKieSession("cepKsession");

        this.flightRepository.findAll().forEach(ksession::insert);
        this.redundantPaymentEventRepository.findAll().forEach(ksession::insert);
        this.suspiciousTransactionEventRepository.findAll().forEach(ksession::insert);
        this.userRepository.findAll().forEach(ksession::insert);
        ksession.insert(ticket);
        ksession.setGlobal("ticketId", ticket.getId());
        ksession.setGlobal("flightId", flightId);
        int firedRules = ksession.fireAllRules();

        saveRedundantPaymentEvents(ksession);
        saveSuspiciousTransactionEvents(ksession);
        saveUsers(ksession);
    }

    private void saveUsers(KieSession ksession) {
        List<User> users = Arrays.stream(ksession.getObjects().toArray()).filter(obj->obj instanceof User).map(obj->(User)obj).collect(Collectors.toList());
        this.userRepository.saveAll(users);
    }

    private void saveRedundantPaymentEvents(KieSession ksession) {
        List<RedundantPaymentEvent> redundantPaymentEvents = Arrays.stream(ksession.getObjects().toArray()).filter(obj->obj instanceof RedundantPaymentEvent).map(obj->(RedundantPaymentEvent)obj).collect(Collectors.toList());
        for (RedundantPaymentEvent redundantPaymentEvent : redundantPaymentEvents) {
            if(redundantPaymentEvent.getId() == null){
                redundantPaymentEvent.setId(this.redundantPaymentEventRepository.count()+1);
                this.redundantPaymentEventRepository.save(redundantPaymentEvent);
            }
        }
    }

    private void saveSuspiciousTransactionEvents(KieSession ksession) {
        List<SuspiciousTransactionEvent> suspiciousTransactionEvents = Arrays.stream(ksession.getObjects().toArray()).filter(obj->obj instanceof SuspiciousTransactionEvent).map(obj->(SuspiciousTransactionEvent)obj).collect(Collectors.toList());
        for (SuspiciousTransactionEvent suspiciousTransactionEvent : suspiciousTransactionEvents) {
            if(suspiciousTransactionEvent.getId() == null){
                suspiciousTransactionEvent.setId(this.suspiciousTransactionEventRepository.count()+1);
                this.suspiciousTransactionEventRepository.save(suspiciousTransactionEvent);
            }
        }
    }

    private void setUserLoyaltyDiscounts(User payer, Ticket ticket) {
        if(!(payer.getLoyaltyStatus() == LoyaltyType.REGULAR)){
            String loyaltyType = payer.getLoyaltyStatus().name().toLowerCase();
            String cap = loyaltyType.substring(0, 1).toUpperCase() + loyaltyType.substring(1);
            Discount discount = this.discountRepository.findByName(cap+ " loyalty status");
            if(ticket.getDiscounts() == null)
                ticket.setDiscounts(new ArrayList<>());
            ticket.getDiscounts().add(discount);
            double finalPrice = ticket.getFinalPrice();
            double percentage = discount.getPercentage()/100;
            ticket.setFinalPrice(finalPrice - finalPrice*percentage);
            ticketRepository.save(ticket);
        }
    }

    private void setPopularFlightWithOver8000kmDiscount(Ticket ticket) {
        if(ticket.getDiscounts() != null){
            double finalPrice = ticket.getFinalPrice();
            double percentage = ticket.getDiscounts().get(0).getPercentage()/100;
            ticket.setFinalPrice(finalPrice - finalPrice*percentage);
            ticketRepository.save(ticket);
        }
    }

    private void setUserLoyaltyStatus(User payer, Ticket ticket) {
        InputStream template = TicketService.class.getResourceAsStream("/rules/template/loyaltyStatusTemplate.drt");

        List<LoyaltyStatusTemplate> loyaltyStatusTemplates = List.of(
                new LoyaltyStatusTemplate(250000, 400000, LoyaltyType.BRONZE, "Bronze loyalty status"),
                new LoyaltyStatusTemplate(400000, 600000, LoyaltyType.SILVER, "Silver loyalty status"),
                new LoyaltyStatusTemplate(600000, Double.MAX_VALUE, LoyaltyType.GOLD, "Gold loyalty status")
        );
        ObjectDataCompiler compiler = new ObjectDataCompiler();
        String drl = compiler.compile(loyaltyStatusTemplates, template);

        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(drl, ResourceType.DRL);
        KieSession ksession = kieHelper.build().newKieSession();

        ksession.insert(payer);
        this.ticketRepository.findAll().forEach(ksession::insert);
        this.discountRepository.findAll().forEach(ksession::insert);


        int rulesFired = ksession.fireAllRules();
        System.out.println("Rules fired for loyalty status template: "+rulesFired);

        userRepository.save(payer);
    }

    private TicketToShowDTO checkTicketFlight(Long flightId, Ticket ticket) {
        KieSession ksession = getKieContainer().newKieSession("forwardKsession");

        ksession.setGlobal("flightId", flightId);
        TicketToShowDTO ticketToShowDTO = new TicketToShowDTO(flightId, false);
        ksession.insert(ticketToShowDTO);
        List<Flight> allFlights = this.flightRepository.findAll();
        allFlights.forEach(ksession::insert);
        ksession.insert(ticket);
        this.discountRepository.findAll().forEach(ksession::insert);
        ksession.fireAllRules();
        this.flightRepository.saveAll(allFlights);
        return ticketToShowDTO;
    }

    private void setPrice(Long flightId, Ticket ticket) {
        Flight flight = this.flightRepository.findById(flightId).
                orElseThrow(() -> new FlightNotFoundException("Flight with this id not found!"));
        InputStream template = TicketService.class.getResourceAsStream("/rules/template/priceTemplate.drt");

        List<PriceTemplate> priceTemplates =priceTemplateRepository.findAll();

        ObjectDataCompiler compiler = new ObjectDataCompiler();
        String drl = compiler.compile(priceTemplates, template);

        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(drl, ResourceType.DRL);
        KieSession ksession = kieHelper.build().newKieSession();

        ksession.insert(flight);
        ksession.insert(ticket);

        int rulesFired = ksession.fireAllRules();
        System.out.println("Rules fired for price template: "+rulesFired);
        ticketRepository.save(ticket);
    }

    private void setTicketNumberDiscount(Long flightId, Ticket ticket) {
        Flight flight = this.flightRepository.findById(flightId).
                orElseThrow(() -> new FlightNotFoundException("Flight with this id not found!"));
        InputStream template = TicketService.class.getResourceAsStream("/rules/template/ticketNumberDiscountTemplate.drt");

        List<TicketNumberDiscountTemplate> ticketNumberDiscountTemplates = List.of(
                new TicketNumberDiscountTemplate(2, 3, TicketType.BUSINESS, "2 business tickets"),
                new TicketNumberDiscountTemplate(3, 4, TicketType.BUSINESS, "3 business tickets"),
                new TicketNumberDiscountTemplate(4, 5, TicketType.BUSINESS, "4 business tickets"),
                new TicketNumberDiscountTemplate(5, Integer.MAX_VALUE, TicketType.BUSINESS, "5 or more business tickets"),

                new TicketNumberDiscountTemplate(2, 3, TicketType.ECONOMIC, "2 economic tickets"),
                new TicketNumberDiscountTemplate(3, 4, TicketType.ECONOMIC, "3 economic tickets"),
                new TicketNumberDiscountTemplate(4, 5, TicketType.ECONOMIC, "4 economic tickets"),
                new TicketNumberDiscountTemplate(5, Integer.MAX_VALUE, TicketType.ECONOMIC, "5 or more economic tickets")
        );

        ObjectDataCompiler compiler = new ObjectDataCompiler();
        String drl = compiler.compile(ticketNumberDiscountTemplates, template);

        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(drl, ResourceType.DRL);
        KieSession ksession = kieHelper.build().newKieSession();

        ksession.insert(flight);
        ksession.insert(ticket);

        discountRepository.findAll().forEach(ksession::insert);

        int rulesFired = ksession.fireAllRules();
        System.out.println("Rules fired for ticket number discount template: " + rulesFired);
        ticketRepository.save(ticket);
    }

    private User createNewUser(PassengerDataDTO passengerData) {
        User user = new User(passengerData);
        this.userRepository.save(user);
        return user;
    }

    public TicketToShowDTO acceptSuggestedFlight(TicketDataDTO ticketDataDTO) {
        User passenger = this.userRepository.findByEmail(ticketDataDTO.getPassengerData().getEmailPassenger()).
                orElse(createNewUser(ticketDataDTO.getPassengerData()));
        User payer = this.userRepository.findByEmail(ticketDataDTO.getPayerEmail()).
                orElseThrow(() -> new UserNotFoundException("User with this email not found!"));

        long newId = this.ticketRepository.count() + 1;

        Ticket ticket = new Ticket(newId, passenger, payer, null,0, 0,
                TicketType.valueOf(ticketDataDTO.getCardType().toUpperCase()), new Date());

        ticketRepository.save(ticket);
        TicketToShowDTO suggestedTicketDTO = checkTicketFlight(ticketDataDTO.getFlightId(), ticket);
        if (!suggestedTicketDTO.isFlightFound())
            return new TicketToShowDTO(ticket, ticketDataDTO.getFlightId(), suggestedTicketDTO.isFlightFound());
        setPrice(ticketDataDTO.getFlightId(), ticket);
        return new TicketToShowDTO(ticket, ticketDataDTO.getFlightId(), suggestedTicketDTO.isFlightFound());
    }

    public List<PriceTemplate> getPriceTemplate() {
        return this.priceTemplateRepository.findAll();
    }

    public void setPriceTemplate(List<PriceTemplate> priceTemplates) {
        List<PriceTemplate> databasePriceTemplates = (List<PriceTemplate>) this.priceTemplateRepository.findAllById(priceTemplates.stream().map(PriceTemplate::getId).collect(Collectors.toList()));
        for (int i = 0; i< priceTemplates.size(); i++){
            PriceTemplate oldPrice = databasePriceTemplates.get(i);
            PriceTemplate newPrice = priceTemplates.get(i);
            oldPrice.setMaxDistance(newPrice.getMaxDistance());
            oldPrice.setMinDistance(newPrice.getMinDistance());
            oldPrice.setPrice(newPrice.getPrice());
            this.priceTemplateRepository.save(oldPrice);
        }
    }

    public ReportDTO getAllTickets() {
//        List<Flight> flights = flightRepository.findAll();
//        List<TicketDTO> tickets = new ArrayList<>();
//        for (Flight flight :flights)
//            for (Ticket soldTicket: flight.getSoldTickets())
//                tickets.add(new TicketDTO(soldTicket, flight));
//        return tickets;
        return getTicketsReport(new TicketsReportTemplate());
    }

    public List<TicketDTO> getTicketsForUser(String email) {
        List<Flight> flights = flightRepository.findAll();
        List<TicketDTO> tickets = new ArrayList<>();
        for (Flight flight :flights) {
            List<Ticket> filteredTickets = flight.getSoldTickets().stream().filter(ticket -> ticket.getPassenger().getEmail().equals(email) || ticket.getPayer().getEmail().equals(email)).collect(Collectors.toList());
            for (Ticket soldTicket : filteredTickets) {
                tickets.add(new TicketDTO(soldTicket, flight));
            }
        }
        return tickets;
    }

    public ReportDTO getTicketsReport(TicketsReportTemplate filterTicketsTemplate) {
        filterTicketsTemplate.setFilterSalience(3);
        filterTicketsTemplate.setCountSalience(2);
        filterTicketsTemplate.setAverageSalience(1);

        InputStream template = TicketService.class.getResourceAsStream("/rules/template/ticketsReportTemplate.drt");

        List<TicketsReportTemplate> ticketsReportTemplates = List.of(filterTicketsTemplate);
        ObjectDataCompiler compiler = new ObjectDataCompiler();
        String drl = compiler.compile(ticketsReportTemplates, template);

        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(drl, ResourceType.DRL);
        KieSession ksession = kieHelper.build().newKieSession();

        Report newReport = new Report();

        flightRepository.findAll().forEach(ksession::insert);
        ksession.insert(newReport);

        int rulesFired = ksession.fireAllRules();
        System.out.println("Rules fired for ticket number discount template: " + rulesFired);

        ReportDTO reportDTO = new ReportDTO(newReport);
        reportDTO.setTickets(convertTicketsToDTO(newReport.getTickets()));
        return reportDTO;
    }

    private List<TicketDTO> convertTicketsToDTO(List<Ticket> existingTickets) {
        List<Flight> flights = flightRepository.findAll();
        List<TicketDTO> tickets = new ArrayList<>();
        for (Flight flight :flights)
            for (Ticket soldTicket: flight.getSoldTickets())
                for (Ticket ticket : existingTickets)
                    if(Objects.equals(soldTicket.getId(), ticket.getId()))
                        tickets.add(new TicketDTO(soldTicket, flight));

        return tickets;
    }
}
