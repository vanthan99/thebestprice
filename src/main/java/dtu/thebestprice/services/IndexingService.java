package dtu.thebestprice.services;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
//@Slf4j
public class IndexingService {

    private final EntityManager entityManager;

    @Autowired
    public IndexingService(final EntityManagerFactory entityManagerFactory){
        this.entityManager = entityManagerFactory.createEntityManager();
    }

//    @PostConstruct
    public void initiateIndexing() {
        try {
            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
            fullTextEntityManager.createIndexer().threadsToLoadObjects(2).startAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        log.info("Initiating indexing...");
//        FullTextEntityManager fullTextEntityManager =
//                Search.getFullTextEntityManager(entityManager);
//        fullTextEntityManager.createIndexer().startAndWait();
//        log.info("All entities indexed");
    }
}
