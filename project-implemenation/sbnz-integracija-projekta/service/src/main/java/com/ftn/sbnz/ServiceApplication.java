package com.ftn.sbnz;

import com.ftn.sbnz.enums.LoyaltyType;
import com.ftn.sbnz.enums.TicketType;
import com.ftn.sbnz.model.Discount;
import com.ftn.sbnz.model.Flight;
import com.ftn.sbnz.model.Ticket;
import com.ftn.sbnz.model.User;
import com.ftn.sbnz.repository.DiscountRepository;
import com.ftn.sbnz.repository.FlightRepository;
import com.ftn.sbnz.repository.TicketRepository;
import com.ftn.sbnz.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.runtime.KieContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@SpringBootApplication(scanBasePackages = "com.ftn.sbnz")
@EnableMongoRepositories
@EnableAsync
@AllArgsConstructor
public class ServiceApplication implements CommandLineRunner{


	private final UserRepository userRepository;
	private final FlightRepository flightRepository;
	private final TicketRepository ticketRepository;
	private final DiscountRepository discountRepository;

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}

	@Override
	public void run(String... args) {
		deleteAllEntities();
		createInitialUsers();
		createInitialDiscounts();
		List<Ticket> tickets = createInitialTickets();
		createInitialFlights(tickets);
	}

	private void deleteAllEntities() {
		this.userRepository.deleteAll();
		this.flightRepository.deleteAll();
		this.ticketRepository.deleteAll();
	}

	private void createInitialUsers() {
		String password = "12345678"; //svi ce imati istu sifru radi olaksanja
		userRepository.save(new User("misa@gmail.com", password, "Misa", "Jokic", "Stojanova 22", "0613005551", "Novi Sad", LoyaltyType.BRONZE));
		userRepository.save(new User("jelena@gmail.com", password, "Jelena", "Jelenic", "Stojanova 23", "0613005552", "Novi Sad", LoyaltyType.SILVER));
		userRepository.save(new User("pera@gmail.com", password, "Pera", "Peric", "Stojanova 24", "0613005553", "Novi Sad", LoyaltyType.GOLD));
		userRepository.save(new User("milica@gmail.com", password, "Milica", "Milicic", "Stojanova 24", "0613005553", "Novi Sad", LoyaltyType.REGULAR));
	}

	private void createInitialDiscounts(){
		List<Discount> discounts = List.of(
			//template discounts
			new Discount("2 business tickets", 5),
			new Discount("3 business tickets", 8),
			new Discount("4 business tickets", 12),
			new Discount("5 or more business tickets", 15),
			new Discount("2 economic tickets", 3),
			new Discount("3 economic tickets", 7),
			new Discount("4 economic tickets", 10),
			new Discount("5 or more economic tickets", 12),
			// Forward chaining
			new Discount("Popular flight with over 8000km", 12),
			// CEP discounts
			new Discount("less than 4 seats", 10),
			new Discount("4-8 seats", 5),
			// Loyalty discounts
			new Discount("Bronze loyalty status", 15),
			new Discount("Silver loyalty status", 30),
			new Discount("Gold loyalty status", 50)
		);
		discountRepository.saveAll(discounts);
	}

	private List<Ticket> createInitialTickets(){
		List<User> passengerList = List.of(
			Objects.requireNonNull(userRepository.findByEmail("misa@gmail.com").orElse(null)),
			Objects.requireNonNull(userRepository.findByEmail("jelena@gmail.com").orElse(null)),
			Objects.requireNonNull(userRepository.findByEmail("pera@gmail.com").orElse(null)),
			Objects.requireNonNull(userRepository.findByEmail("milica@gmail.com").orElse(null))
		);

		List<Ticket> tickets = List.of(
			new Ticket(1L, passengerList.get(0), passengerList.get(0), null, 40000, TicketType.BUSINESS),
			new Ticket(2L, passengerList.get(1), passengerList.get(0), null, 40000, TicketType.BUSINESS),
			new Ticket(3L, passengerList.get(2), passengerList.get(0), null, 40000, TicketType.BUSINESS),
			new Ticket(4L, passengerList.get(1), passengerList.get(1), null, 40000, TicketType.BUSINESS),
			new Ticket(5L, passengerList.get(2), passengerList.get(3), null, 40000, TicketType.BUSINESS)
		);
		ticketRepository.saveAll(tickets);
		return tickets;
	}

	private void createInitialFlights(List<Ticket> tickets) {
		flightRepository.save(new Flight(1L, "London", 1694, 40000, LocalDateTime.of(2023, 6, 10, 14, 0), tickets, 120, false));
		flightRepository.save(new Flight(2L, "Istanbul", 642, 40000, LocalDateTime.of(2023, 6, 10, 12, 0), tickets, 120, false));
		flightRepository.save(new Flight(3L, "Vienna", 536, 35000, LocalDateTime.of(2023, 6, 11, 9, 0), tickets, 125, true));
		flightRepository.save(new Flight(4L, "Munich", 871, 45000, LocalDateTime.of(2023, 6, 7, 8, 30), tickets, 125, false));
		flightRepository.save(new Flight(5L, "Zurich", 1082, 55000, LocalDateTime.of(2023, 6, 11, 10, 0), tickets, 120, false));
		flightRepository.save(new Flight(6L, "Rome", 1107, 48000, LocalDateTime.of(2023, 6, 10, 15, 0), tickets, 150, true));
		flightRepository.save(new Flight(7L, "Paris", 1376, 60000, LocalDateTime.of(2023, 6, 9, 11, 30), tickets, 180, false));
		flightRepository.save(new Flight(8L, "Amsterdam", 1314, 55000, LocalDateTime.of(2023, 6, 9, 5, 0), tickets, 150, false));
		flightRepository.save(new Flight(9L, "Moscow", 1548, 70000, LocalDateTime.of(2023, 6, 8, 19, 30), tickets, 180, false));
		flightRepository.save(new Flight(10L, "New York City", 7978, 100000, LocalDateTime.of(2023, 6, 8, 22, 0), tickets, 220, false));
		flightRepository.save(new Flight(11L, "Chicago", 8200, 110000, LocalDateTime.of(2023, 6, 12, 12, 30), tickets, 220, false));
		flightRepository.save(new Flight(12L, "Los Angeles", 10307, 110000, LocalDateTime.of(2023, 6, 9, 20, 0), tickets, 220, true));
		flightRepository.save(new Flight(13L, "Sydney", 16146, 120000, LocalDateTime.of(2023, 6, 7, 7, 30), tickets, 220, false));
		flightRepository.save(new Flight(14L, "Tokyo", 9178, 110000, LocalDateTime.of(2023, 6, 11, 5, 30), tickets, 6, true));
		//flightRepository.save(new Flight(15L, "London", 9178, 110000, LocalDateTime.of(2023, 7, 11, 5, 30), tickets, 220, true)); //radi testa
	}

//	@Bean
//	public KieContainer kieContainer() {
//		KieServices ks = KieServices.Factory.get();
//		KieContainer kContainer = ks
//				.newKieContainer(ks.newReleaseId("com.ftn.sbnz", "kjar", "0.0.1-SNAPSHOT"));
//		KieScanner kScanner = ks.newKieScanner(kContainer);
//		kScanner.start(1000);
//		return kContainer;
//	}

}
