//package pjv.dao;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.core.env.Environment;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//import java.util.Properties;
//
//@Configuration
//@EnableJpaRepositories(basePackages = {
//        "pjv.dao"
//})
//@EnableTransactionManagement
//public class PersistenceConfig {
//
//    private final Environment environment;
//
//    public PersistenceConfig(Environment environment) {
//        this.environment = environment;
//    }
//
//    @Bean
//    @Profile("test")
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.h2.Driver");
//        dataSource.setUrl("jdbc:h2:mem:db;DB__CLOSE__DELAY=-1");
//        dataSource.setUsername("sa");
//        dataSource.setPassword("sa");
//
//        return dataSource;
//    }
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource ds) {
//        final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//        emf.setDataSource(ds);
//        emf.setJpaVendorAdapter(new EclipseLinkJpaVendorAdapter());
//        emf.setPackagesToScan("cz.cvut.kbss.ear.brigade.model");
//
//        final Properties props = new Properties();
//        props.setProperty("databasePlatform", "org.eclipse.persistence.platform.database.PostgreSQLPlatform");
//        props.setProperty("generateDdl", "true");
//        props.setProperty("showSql", "true");
//        props.setProperty("eclipselink.weaving", "static");
//        props.setProperty("eclipselink.ddl-generation", "create-tables");
//        props.setProperty("eclipselink.logging.level", "INFO");
//        props.setProperty("eclipselink.logging.parameters", "true");
//        emf.setJpaProperties(props);
//        return emf;
//    }
//
//    @Bean(name = "txManager")
//    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory);
//        return transactionManager;
//    }
//
//    //configure entityManagerFactory
//    //configure transactionManager
//    //configure additional Hibernate properties
//}
