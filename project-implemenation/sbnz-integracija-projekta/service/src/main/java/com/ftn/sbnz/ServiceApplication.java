package com.ftn.sbnz;

import com.ftn.sbnz.enums.LoyaltyType;
import com.ftn.sbnz.model.User;
import com.ftn.sbnz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.runtime.KieContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.ftn.sbnz")
@EnableMongoRepositories
@EnableAsync
public class ServiceApplication implements CommandLineRunner{

	@Autowired
	private UserRepository userRepository;
	
	private static Logger log = LoggerFactory.getLogger(ServiceApplication.class);

	public static void main(String[] args) {
//		ApplicationContext ctx = SpringApplication.run(ServiceApplication.class, args);
//
//		String[] beanNames = ctx.getBeanDefinitionNames();
//		Arrays.sort(beanNames);
//
//		StringBuilder sb = new StringBuilder("Application beans:\n");
//		for (String beanName : beanNames) {
//			sb.append(beanName + "\n");
//		}
//		log.info(sb.toString());
		SpringApplication.run(ServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		deleteAllEntities();
		createInitialUsers();
	}

	private void deleteAllEntities() {
		this.userRepository.deleteAll();
	}

	private void createInitialUsers() {
		String password = "12345678"; //svi ce imati istu sifru radi olaksanja
		userRepository.save(new User("misa@gmail.com", password, "Misa", "Jokic", "Stojanova 22", "0613005551", "Novi Sad", LoyaltyType.BRONZE));
		userRepository.save(new User("jelena@gmail.com", password, "Jelena", "Jelenic", "Stojanova 23", "0613005552", "Novi Sad", LoyaltyType.SILVER));
		userRepository.save(new User("pera@gmail.com", password, "Pera", "Peric", "Stojanova 24", "0613005553", "Novi Sad", LoyaltyType.GOLD));
		userRepository.save(new User("perafdsa@gmail.com", password, "Pera", "Peric", "Stojanova 24", "0613005553", "Novi Sad", LoyaltyType.GOLD));
	}

	@Bean
	public KieContainer kieContainer() {
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks
				.newKieContainer(ks.newReleaseId("com.ftn.sbnz", "kjar", "0.0.1-SNAPSHOT"));
		KieScanner kScanner = ks.newKieScanner(kContainer);
		kScanner.start(1000);
		return kContainer;
	}

//	 KieServices ks = KieServices.Factory.get(); KieContainer kContainer =
//	 ks.newKieContainer(ks.newReleaseId("drools-spring-v2",
//	 "drools-spring-v2-kjar", "0.0.1-SNAPSHOT")); KieScanner kScanner = ks.newKieScanner(kContainer); kScanner.start(10_000); KieSession kSession =
//	 kContainer.newKieSession();

}
