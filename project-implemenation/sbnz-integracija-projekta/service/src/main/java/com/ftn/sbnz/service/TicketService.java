package com.ftn.sbnz.service;

import com.ftn.sbnz.dto.ticket.PassengerDataDTO;
import com.ftn.sbnz.dto.ticket.TicketDataDTO;
import com.ftn.sbnz.enums.TicketType;
import com.ftn.sbnz.exception.UserNotFoundException;
import com.ftn.sbnz.model.Flight;
import com.ftn.sbnz.model.PriceTemplate;
import com.ftn.sbnz.model.Ticket;
import com.ftn.sbnz.model.User;
import com.ftn.sbnz.dto.ticket.TicketToShowDTO;
import com.ftn.sbnz.exception.FlightNotFoundException;
import com.ftn.sbnz.repository.DiscountRepository;
import com.ftn.sbnz.repository.FlightRepository;
import com.ftn.sbnz.repository.TicketRepository;
import com.ftn.sbnz.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieServices;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;
    private final DiscountRepository discountRepository;


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

        Ticket ticket = new Ticket(newId, passenger, payer, null, 0,
                TicketType.valueOf(ticketDataDTO.getCardType().toUpperCase()));

        long suggestedFlightId = checkTicketFlight(ticketDataDTO.getFlightId(), ticket);
        if(suggestedFlightId != ticketDataDTO.getFlightId())
            return new TicketToShowDTO(suggestedFlightId);
        ticketRepository.save(ticket);

        setPrice(suggestedFlightId, ticket);
        return new TicketToShowDTO(ticket, suggestedFlightId);
    }

    private long checkTicketFlight(Long flightId, Ticket ticket) {
        KieSession ksession = getKieContainer().newKieSession("forwardKsession");

        ksession.setGlobal("flightId", flightId);
        TicketToShowDTO ticketToShowDTO = new TicketToShowDTO(flightId);
        ksession.insert(ticketToShowDTO);
        List<Flight> allFlights = this.flightRepository.findAll();
        allFlights.forEach(ksession::insert);
        ksession.insert(ticket);
        this.discountRepository.findAll().forEach(ksession::insert);
        ksession.fireAllRules();
        this.flightRepository.saveAll(allFlights);
        return ticketToShowDTO.getAlternativeFlightId();
    }

    private void setPrice(Long flightId, Ticket ticket) {
        Flight flight = this.flightRepository.findById(flightId).
                orElseThrow(() -> new FlightNotFoundException("Flight with this id not found!"));
        InputStream template = TicketService.class.getResourceAsStream("/rules/template/priceTemplate.drt");

        List<PriceTemplate> priceTemplates = List.of(
                new PriceTemplate(TicketType.BUSINESS, 0, 1000, 50000),
                new PriceTemplate(TicketType.BUSINESS, 1000, 2000, 70000),
                new PriceTemplate(TicketType.BUSINESS, 2000, 3000, 90000),
                new PriceTemplate(TicketType.BUSINESS, 3000, 4000, 110000),
                new PriceTemplate(TicketType.BUSINESS, 4000, 5000, 130000),
                new PriceTemplate(TicketType.BUSINESS, 5000, 6000, 150000),
                new PriceTemplate(TicketType.BUSINESS, 6000, 7000, 170000),
                new PriceTemplate(TicketType.BUSINESS, 7000, 8000, 190000),
                new PriceTemplate(TicketType.BUSINESS, 8000, 9000, 210000),
                new PriceTemplate(TicketType.BUSINESS, 9000, 10000, 230000),
                new PriceTemplate(TicketType.BUSINESS, 10000, Integer.MAX_VALUE, 250000),

                new PriceTemplate(TicketType.ECONOMIC, 0, 1000, 25000),
                new PriceTemplate(TicketType.ECONOMIC, 1000, 2000, 35000),
                new PriceTemplate(TicketType.ECONOMIC, 2000, 3000, 45000),
                new PriceTemplate(TicketType.ECONOMIC, 3000, 4000, 550000),
                new PriceTemplate(TicketType.ECONOMIC, 4000, 5000, 650000),
                new PriceTemplate(TicketType.ECONOMIC, 5000, 6000, 750000),
                new PriceTemplate(TicketType.ECONOMIC, 6000, 7000, 850000),
                new PriceTemplate(TicketType.ECONOMIC, 7000, 8000, 950000),
                new PriceTemplate(TicketType.ECONOMIC, 8000, 9000, 105000),
                new PriceTemplate(TicketType.ECONOMIC, 9000, 10000, 115000),
                new PriceTemplate(TicketType.ECONOMIC, 10000, Integer.MAX_VALUE, 125000)
        );

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

        Ticket ticket = new Ticket(newId, passenger, payer, null, 0,
                TicketType.valueOf(ticketDataDTO.getCardType().toUpperCase()));

        ticketRepository.save(ticket);
        long suggestedFlightId = checkTicketFlight(ticketDataDTO.getFlightId(), ticket);
        setPrice(ticketDataDTO.getFlightId(), ticket);
        return new TicketToShowDTO(ticket, ticketDataDTO.getFlightId());
    }
}
