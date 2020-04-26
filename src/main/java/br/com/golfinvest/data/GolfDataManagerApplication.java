package br.com.golfinvest.data;

import br.com.golfinvest.data.view.MainFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import sun.applet.Main;


@SpringBootApplication
public class GolfDataManagerApplication {

	//Spring Boot will automagically wire this object using application.properties:
	@Autowired
	private JdbcTemplate jdbcTemplate;


	public static void main(String[] args) {
		ConfigurableApplicationContext context = new SpringApplicationBuilder(
				GolfDataManagerApplication.class).headless(false).run(args);

		MainFrame mainFrame = (MainFrame) context.getBean("mainFrame");
		mainFrame.initComponents();
	}



//	@Override
//	public void run(String... args) throws Exception {
//
//
//		//Create the database table:
////		jdbcTemplate().execute("CREATE TABLE IF NOT EXISTS beers(name VARCHAR(100))");
//		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS beers(name VARCHAR(100))");
//
//		//Insert a record:
////		jdbcTemplate().execute("INSERT INTO beers VALUES ('Stella')");
//		jdbcTemplate.execute("INSERT INTO beers VALUES ('Stella2')");
//
//		//Read records:
////		List<Beer> beers = jdbcTemplate().query("SELECT * FROM beers",
////				(resultSet, rowNum) -> new Beer(resultSet.getString("name")));
//		List<Beer> beers2 = jdbcTemplate.query("SELECT * FROM beers",
//				(resultSet, rowNum) -> new Beer(resultSet.getString("name")));
//		//List<Beer> beers = selectAll();
//
//		//Print read records:
////		beers.forEach(System.out::println);
//		beers2.forEach(System.out::println);
//
//
//
//
//
//
//		String dbAddress = "c:/Users/pedro/Documents/azzam2.db";
//		AppConfig appConfig = new AppConfig();
//		jdbcTemplate = appConfig.newJdbcTemplate(dbAddress);
//
//		//Create the database table:
////		jdbcTemplate().execute("CREATE TABLE IF NOT EXISTS beers(name VARCHAR(100))");
//		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS beers(name VARCHAR(100))");
//
//		//Insert a record:
////		jdbcTemplate().execute("INSERT INTO beers VALUES ('Stella')");
//		jdbcTemplate.execute("INSERT INTO beers VALUES ('Stella2')");
//
//		//Read records:
////		List<Beer> beers = jdbcTemplate().query("SELECT * FROM beers",
////				(resultSet, rowNum) -> new Beer(resultSet.getString("name")));
//		List<Beer> beers22 = jdbcTemplate.query("SELECT * FROM beers",
//				(resultSet, rowNum) -> new Beer(resultSet.getString("name")));
//		//List<Beer> beers = selectAll();
//
//		//Print read records:
////		beers.forEach(System.out::println);
//		beers22.forEach(System.out::println);
//
//
//
//	}

}