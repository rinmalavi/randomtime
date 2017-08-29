package de.atron.randomtime.time.services;

import de.atron.randomtime.time.model.TimeQuest;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@ApplicationScoped
public class TimeQuestRepository {

    private Logger logger =  Logger.getLogger(this.getClass());

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public int saveTime(LocalDateTime at) {
        TimeQuest timeQuest = new TimeQuest();
        timeQuest.setAt(at.toString());
        entityManager.persist(timeQuest);
        int id = timeQuest.getId();
        logger.infof("Saved date %s with id %d", at.toString(),  id);

        return id;
    }

    public TimeQuest[] getTimeQuests() {
        return entityManager.createQuery("From TimeQuest", TimeQuest.class).getResultList().toArray(new TimeQuest[0]);
    }
}
