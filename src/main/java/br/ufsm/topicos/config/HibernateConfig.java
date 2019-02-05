package br.ufsm.topicos.config;

import br.ufsm.topicos.cbr.TrucoDescription;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 06/12/2018.
 */


public class HibernateConfig {

    private static SessionFactory sessionFactory;

    public static synchronized void buildSessionFactory() {
        if (sessionFactory == null) {
            try {
                sessionFactory = getConfiguration().buildSessionFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration cfg = getConfiguration();
                sessionFactory = cfg.buildSessionFactory();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }


    public static Configuration getConfiguration() {

        Configuration cfg = new Configuration();
        cfg.addPackage("br.ufsm.topicos.cbr");
        //cfg.addPackage("br.ufsm.topicos.log");
        cfg.addAnnotatedClass(TrucoDescription.class);
        //cfg.addAnnotatedClass(Log.class);

        cfg.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        //cfg.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/dbtrucocbr");
        cfg.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/dbclassifier");
        cfg.setProperty("hibernate.connection.username", "root");
        cfg.setProperty("hibernate.connection.password", "desenvolvimento");
        cfg.setProperty("dialect", "org.hibernate.dialect.MySQLDialect");

        return cfg;
    }
}
