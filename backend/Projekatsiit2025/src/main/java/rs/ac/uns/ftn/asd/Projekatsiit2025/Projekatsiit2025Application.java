package rs.ac.uns.ftn.asd.Projekatsiit2025;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = "rs.ac.uns.ftn.asd.Projekatsiit2025")
@EntityScan("rs.ac.uns.ftn.asd.Projekatsiit2025.model")
@EnableJpaRepositories("rs.ac.uns.ftn.asd.Projekatsiit2025.repository")
public class Projekatsiit2025Application {

	public static void main(String[] args) {
		SpringApplication.run(Projekatsiit2025Application.class, args);
	
		/*
		 * Ako u application.properties stoji:
		 * 1. spring.jpa.hibernate.ddl-auto=create --> svkai put kada se
		 * aplikacija pokrene, tabele se automatski brisu i ponovo prave
		 * 2. spring.jpa.hibernate.ddl-auto=update --> tabele su napravljenej pri
		 * prvom pokretanju, i samo se azuriraju pri svakom novom
		 * 
		 * TRENUTNO SE KORISTI UPDATE. MOZE TAKO DA OSTANE
		 * 
		 * U SRC/MAIN/RESOURCES SE NALAZI FAJL import.sql
		 * U njega se pisu sve insert naredbe, i pri pokretanju aplikacije
		 * tabele se na osnovu njega azuriraju/pune/prazne
		 * 
		 * */
		System.out.println("==========================");
		System.out.println("    STARTED SUCESSFULLY   ");
		System.out.println("==========================\n");
		
	}
}
