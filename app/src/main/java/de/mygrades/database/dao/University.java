package de.mygrades.database.dao;

import java.util.List;
import de.mygrades.database.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "UNIVERSITY".
 */
public class University {

    private Long universityId;
    /** Not-null value. */
    private String name;
    private Boolean published;
    private String updatedAtServer;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient UniversityDao myDao;

    private List<Rule> rules;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public University() {
    }

    public University(Long universityId) {
        this.universityId = universityId;
    }

    public University(Long universityId, String name, Boolean published, String updatedAtServer) {
        this.universityId = universityId;
        this.name = name;
        this.published = published;
        this.updatedAtServer = updatedAtServer;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUniversityDao() : null;
    }

    public Long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Long universityId) {
        this.universityId = universityId;
    }

    /** Not-null value. */
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public String getUpdatedAtServer() {
        return updatedAtServer;
    }

    public void setUpdatedAtServer(String updatedAtServer) {
        this.updatedAtServer = updatedAtServer;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Rule> getRules() {
        if (rules == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RuleDao targetDao = daoSession.getRuleDao();
            List<Rule> rulesNew = targetDao._queryUniversity_Rules(universityId);
            synchronized (this) {
                if(rules == null) {
                    rules = rulesNew;
                }
            }
        }
        return rules;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetRules() {
        rules = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public List<Rule> getRulesRaw() {
        return rules == null ? new java.util.ArrayList<Rule>() : rules;
    }

    @Override
    public String toString() {
        return "University{" +
                "universityId=" + universityId +
                ", name='" + name + '\'' +
                ", published=" + published +
                ", updatedAtServer='" + updatedAtServer + '\'' +
                ", daoSession=" + daoSession +
                ", myDao=" + myDao +
                ", rules=" + rules +
                '}';
    }
    // KEEP METHODS END

}
