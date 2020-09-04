package com.upgrade.reservation.util;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
public class DbUtil {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    public void resetTables() {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        entityManager.createNativeQuery("DELETE FROM reservation").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM users").executeUpdate();
        entityManager.createNativeQuery("UPDATE availability set reservation_id=null, version=0").executeUpdate();
        transactionManager.commit(status);
    }
}
