package br.ufsm.topicos.jcolibri_ex;

import br.ufsm.topicos.cbr.TrucoDescription;
import br.ufsm.topicos.config.HibernateConfig;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 06/12/2018.
 */


public class DataBaseConnectorEx{

    TrucoDescription trucoDescription;

    public void buildSessionFactory() {
        HibernateConfig.buildSessionFactory();
    }

    public Collection<CBRCase> retrieveAllCases() {
        ArrayList<CBRCase> cases = new ArrayList<>();
        try {
            CaseComponent caseComponent;

            Session session = HibernateConfig.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<TrucoDescription> query = builder.createQuery(TrucoDescription.class);
            Root<TrucoDescription> root = query.from(TrucoDescription.class);
            query.select(root);
            Query<TrucoDescription> q = session.createQuery(query);

            List<TrucoDescription> maosList = q.getResultList();
            //List<TrucoDescription> maosList = session.createCriteria(TrucoDescription.class).list();
            transaction.commit();
            session.close();

            CBRCase _case;
            for(Iterator iter = maosList.iterator(); iter.hasNext(); cases.add(_case)) {
                _case = new CBRCase();
                caseComponent = (CaseComponent)iter.next();
                _case.setDescription(caseComponent);
            }

        }
        catch (Exception var12) {
            LogFactory.getLog(this.getClass()).error(var12);
        }

        LogFactory.getLog(this.getClass()).info(cases.size() + " cases read from the database.");
        return cases;

    }

    public void close() {

    }

    public void storeCases(Collection<CBRCase> cases) {

    }

    public void deleteCases(Collection<CBRCase> cases) {

    }



}
