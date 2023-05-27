package com.ftn.sbnz;

import com.ftn.sbnz.enums.LoyaltyType;
import com.ftn.sbnz.enums.TicketType;
import com.ftn.sbnz.enums.UserType;
import com.ftn.sbnz.model.*;
import com.ftn.sbnz.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	private final PriceTemplateRepository priceTemplateRepository;
	private final LastMinuteEventRepository lastMinuteEventRepository;

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws ParseException {
		deleteAllEntities();
		createInitialUsers();
		createInitialDiscounts();
		List<Ticket> tickets = createInitialTickets();
		createInitialFlights(tickets);
		createInitialPriceTemplates();
	}

	private void deleteAllEntities() {
		this.userRepository.deleteAll();
		this.flightRepository.deleteAll();
		this.ticketRepository.deleteAll();
		this.discountRepository.deleteAll();
		this.priceTemplateRepository.deleteAll();
		this.lastMinuteEventRepository.deleteAll();
	}

	private void createInitialUsers() {
		String password = "12345678"; //svi ce imati istu sifru radi olaksanja
		userRepository.save(new User("misa@gmail.com", password, "Misa", "Jokic", "Stojanova 22", "0613005551", "Novi Sad", LoyaltyType.REGULAR, UserType.PASSENGER, false));
		userRepository.save(new User("jelena@gmail.com", password, "Jelena", "Jelenic", "Stojanova 23", "0613005552", "Novi Sad", LoyaltyType.REGULAR, UserType.PASSENGER, false));
		userRepository.save(new User("pera@gmail.com", password, "Pera", "Peric", "Stojanova 24", "0613005553", "Novi Sad", LoyaltyType.REGULAR, UserType.PASSENGER, false));
		userRepository.save(new User("milica@gmail.com", password, "Milica", "Milicic", "Stojanova 24", "0613005553", "Novi Sad", LoyaltyType.REGULAR, UserType.PASSENGER, false));
		userRepository.save(new User("admin@gmail.com", password, "Admin", "Admin", "Stojanova 24", "0613005553", "Novi Sad", LoyaltyType.REGULAR, UserType.ADMIN, false));
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
			new Ticket(1L, passengerList.get(0), passengerList.get(0), null, 40000, 40000, TicketType.BUSINESS, new Date()),
			new Ticket(2L, passengerList.get(1), passengerList.get(0), null, 40000, 40000, TicketType.BUSINESS, new Date()),
			new Ticket(3L, passengerList.get(2), passengerList.get(0), null, 40000, 40000, TicketType.BUSINESS, new Date()),
			new Ticket(4L, passengerList.get(1), passengerList.get(1), null, 40000, 40000, TicketType.BUSINESS, new Date()),
			new Ticket(5L, passengerList.get(2), passengerList.get(3), null, 40000, 40000, TicketType.BUSINESS, new Date())
		);
		ticketRepository.saveAll(tickets);
		return tickets;
	}

	private void createInitialFlights(List<Ticket> tickets) throws ParseException {
		SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		flightRepository.save(new Flight(1L, "London", 1694, 40000, ft.parse("10.06.2023 12:00"), tickets, 5, false, "LON1"));
		flightRepository.save(new Flight(2L, "Istanbul", 642, 40000, ft.parse("10.06.2023 12:00"), tickets, 120, false, "IST2"));
		flightRepository.save(new Flight(3L, "Vienna", 536, 35000, ft.parse("11.06.2023 09:00"), tickets, 125, true, "VIE3"));
		flightRepository.save(new Flight(4L, "Munich", 871, 45000, ft.parse("07.06.2023 07:30"), tickets, 125, false, "MUN4"));
		flightRepository.save(new Flight(5L, "Zurich", 1082, 55000, ft.parse("11.06.2023 10:00"), tickets, 120, false, "ZUR5"));
		flightRepository.save(new Flight(6L, "Rome", 1107, 48000, ft.parse("10.06.2023 15:0"), tickets, 150, true, "ROM6"));
		flightRepository.save(new Flight(7L, "Paris", 1376, 60000, ft.parse("09.06.2023 09:30"), tickets, 5, false, "PAR7"));
		flightRepository.save(new Flight(8L, "Amsterdam", 1314, 55000, ft.parse("09.06.2023 05:00"), tickets, 6, false, "AMS8"));
		flightRepository.save(new Flight(9L, "Moscow", 1548, 70000, ft.parse("08.06.2023 17:30"), tickets, 5, false, "MOS9"));
		flightRepository.save(new Flight(10L, "New York City", 7978, 100000, ft.parse("08.06.2023 10:00"), tickets, 220, false, "NYC10"));
		flightRepository.save(new Flight(11L, "Chicago", 8200, 110000, ft.parse("12.06.2023 12:30"), tickets, 5, false, "CHI11"));
		flightRepository.save(new Flight(12L, "Los Angeles", 10307, 110000, ft.parse("09.06.2023 20:00"), tickets, 220, true, "LA12"));
		flightRepository.save(new Flight(13L, "Sydney", 16146, 120000, ft.parse("07.06.2023 07:30"), tickets, 220, false, "SYD13"));
		flightRepository.save(new Flight(14L, "Tokyo", 9178, 110000, ft.parse("11.06.2023 05:30"), tickets, 6, false, "TKY14"));
		flightRepository.save(new Flight(15L, "London", 1694, 110000, ft.parse("11.06.2023 05:30"), tickets, 5, true, "LON15")); //radi testa
		flightRepository.save(new Flight(16L, "Tokyo", 9178, 110000, ft.parse("11.06.2023 08:30"), tickets, 6, false, "TKY16"));
		flightRepository.save(new Flight(17L, "New York City", 7978, 100000, ft.parse("27.05.2023 20:00"), tickets, 8, false, "NYC17"));
		flightRepository.save(new Flight(18L, "New York City", 7978, 100000, ft.parse("27.05.2023 22:00"), tickets, 120, false, "NYC18"));
	}

	private void createInitialPriceTemplates() {
		List<PriceTemplate> priceTemplates = List.of(
				new PriceTemplate(1L, TicketType.BUSINESS, 0, 1000, 50000),
				new PriceTemplate(2L, TicketType.BUSINESS, 1000, 2000, 70000),
				new PriceTemplate(3L, TicketType.BUSINESS, 2000, 3000, 90000),
				new PriceTemplate(4L, TicketType.BUSINESS, 3000, 4000, 110000),
				new PriceTemplate(5L, TicketType.BUSINESS, 4000, 5000, 130000),
				new PriceTemplate(6L, TicketType.BUSINESS, 5000, 6000, 150000),
				new PriceTemplate(7L, TicketType.BUSINESS, 6000, 7000, 170000),
				new PriceTemplate(8L, TicketType.BUSINESS, 7000, 8000, 190000),
				new PriceTemplate(9L, TicketType.BUSINESS, 8000, 9000, 210000),
				new PriceTemplate(10L, TicketType.BUSINESS, 9000, 10000, 230000),
				new PriceTemplate(11L, TicketType.BUSINESS, 10000, Integer.MAX_VALUE, 250000),

				new PriceTemplate(12L, TicketType.ECONOMIC, 0, 1000, 25000),
				new PriceTemplate(13L, TicketType.ECONOMIC, 1000, 2000, 35000),
				new PriceTemplate(14L, TicketType.ECONOMIC, 2000, 3000, 45000),
				new PriceTemplate(15L, TicketType.ECONOMIC, 3000, 4000, 55000),
				new PriceTemplate(16L, TicketType.ECONOMIC, 4000, 5000, 65000),
				new PriceTemplate(17L, TicketType.ECONOMIC, 5000, 6000, 75000),
				new PriceTemplate(18L, TicketType.ECONOMIC, 6000, 7000, 85000),
				new PriceTemplate(19L, TicketType.ECONOMIC, 7000, 8000, 95000),
				new PriceTemplate(20L, TicketType.ECONOMIC, 8000, 9000, 105000),
				new PriceTemplate(21L, TicketType.ECONOMIC, 9000, 10000, 115000),
				new PriceTemplate(22L, TicketType.ECONOMIC, 10000, Integer.MAX_VALUE, 125000)
		);
		this.priceTemplateRepository.saveAll(priceTemplates);

	}

}
