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
import com.ftn.sbnz.exception.UserNotFoundException;
import com.ftn.sbnz.model.Discount;
import com.ftn.sbnz.model.Flight;
import com.ftn.sbnz.model.Ticket;
import com.ftn.sbnz.model.User;
import com.ftn.sbnz.repository.DiscountRepository;
import com.ftn.sbnz.repository.FlightRepository;
import com.ftn.sbnz.repository.TicketRepository;
import com.ftn.sbnz.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.kie.api.KieServices;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.stereotype.Service;

import org.drools.template.ObjectDataCompiler;

import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;
    private final DiscountRepository discountRepository;

    public TicketToShowDTO createTicket(TicketDataDTO ticketDataDTO) {
        Ticket ticket = new Ticket();
        User passenger = this.userRepository.findByEmail(ticketDataDTO.getPassengerData().getEmailPassenger()).
                orElse(createNewUser(ticketDataDTO.getPassengerData()));
        ticket.setPassenger(passenger);

        User payer = this.userRepository.findByEmail(ticketDataDTO.getPayerEmail()).
                orElseThrow(() -> new UserNotFoundException("User with this email not found!"));

        ticket.setPayer(payer);
        ticket.setId(this.ticketRepository.count() + 1);
        ticket.setTicketType(TicketType.valueOf(ticketDataDTO.getCardType().toUpperCase()));
        setPrice(ticket);
        ticketRepository.save(ticket);
        addTicketToFlight(ticketDataDTO.getFlightId(), ticket);
        return new TicketToShowDTO(
            Arrays.asList(
                    this.discountRepository.findByName("2 business tickets"),
                    this.discountRepository.findByName("3 business tickets"),
                    this.discountRepository.findByName("4 business tickets")
            ),
            50.000
        );
    }

    private void addTicketToFlight(Long flightId, Ticket ticket) {
        Flight flight = this.flightRepository.findById(flightId).
                orElseThrow(() -> new FlightNotFoundException("Flight with this id not found!"));
        flight.getSoldTickets().add(ticket);
        flightRepository.save(flight);
    }

    private void setPrice(Ticket ticket) {
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

        ksession.insert(ticket);

        int rulesFired = ksession.fireAllRules();
        System.out.println("Rules fired for price template: "+rulesFired);
    }

    private User createNewUser(PassengerDataDTO passengerData) {
        User user = new User(passengerData);
        this.userRepository.save(user);
        return user;
    }

}
