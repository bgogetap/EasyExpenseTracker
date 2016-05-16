package com.cultureoftech.easyexpensetracker.dao;

import java.util.List;
import com.cultureoftech.easyexpensetracker.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "PAYEE".
 */
public class Payee {

    private Long id;
    private String name;
    private String comments;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient PayeeDao myDao;

    private List<Expense> expenses;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Payee() {
    }

    public Payee(Long id) {
        this.id = id;
    }

    public Payee(Long id, String name, String comments) {
        this.id = id;
        this.name = name;
        this.comments = comments;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPayeeDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Expense> getExpenses() {
        if (expenses == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ExpenseDao targetDao = daoSession.getExpenseDao();
            List<Expense> expensesNew = targetDao._queryPayee_Expenses(id);
            synchronized (this) {
                if(expenses == null) {
                    expenses = expensesNew;
                }
            }
        }
        return expenses;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetExpenses() {
        expenses = null;
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
    public static Payee create(String name, String comments) {
        return new Payee(null, name, comments);
    }
    // KEEP METHODS END

}
