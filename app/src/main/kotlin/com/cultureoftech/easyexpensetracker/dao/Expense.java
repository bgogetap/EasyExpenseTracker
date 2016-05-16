package com.cultureoftech.easyexpensetracker.dao;

import android.text.TextUtils;

import java.util.Date;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "EXPENSE".
 */
public class Expense {

    private Long id;
    private String title;
    private java.util.Date date;
    private Double amount;
    private String comments;
    private String imagePath;
    private long payeeId;
    private long reportId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient ExpenseDao myDao;

    private Payee payee;
    private Long payee__resolvedKey;

    private ExpenseReport expenseReport;
    private Long expenseReport__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    private boolean selected;
    // KEEP FIELDS END

    public Expense() {
    }

    public Expense(Long id) {
        this.id = id;
    }

    public Expense(Long id, String title, java.util.Date date, Double amount, String comments, String imagePath, long payeeId, long reportId) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.amount = amount;
        this.comments = comments;
        this.imagePath = imagePath;
        this.payeeId = payeeId;
        this.reportId = reportId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getExpenseDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public long getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(long payeeId) {
        this.payeeId = payeeId;
    }

    public long getReportId() {
        return reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    /** To-one relationship, resolved on first access. */
    public Payee getPayee() {
        long __key = this.payeeId;
        if (payee__resolvedKey == null || !payee__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PayeeDao targetDao = daoSession.getPayeeDao();
            Payee payeeNew = targetDao.load(__key);
            synchronized (this) {
                payee = payeeNew;
            	payee__resolvedKey = __key;
            }
        }
        return payee;
    }

    public void setPayee(Payee payee) {
        if (payee == null) {
            throw new DaoException("To-one property 'payeeId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.payee = payee;
            payeeId = payee.getId();
            payee__resolvedKey = payeeId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public ExpenseReport getExpenseReport() {
        long __key = this.reportId;
        if (expenseReport__resolvedKey == null || !expenseReport__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ExpenseReportDao targetDao = daoSession.getExpenseReportDao();
            ExpenseReport expenseReportNew = targetDao.load(__key);
            synchronized (this) {
                expenseReport = expenseReportNew;
            	expenseReport__resolvedKey = __key;
            }
        }
        return expenseReport;
    }

    public void setExpenseReport(ExpenseReport expenseReport) {
        if (expenseReport == null) {
            throw new DaoException("To-one property 'reportId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.expenseReport = expenseReport;
            reportId = expenseReport.getId();
            expenseReport__resolvedKey = reportId;
        }
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
    public static final class Builder {

        private final double amount;

        private String title = "";
        private Date date = new Date();
        private String comments = "";
        private long payeeId = 1;
        private long reportId = 1;
        private String filePath = "";

        public Builder(double amount) {
            this.amount = amount;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder payee(long payeeId) {
            this.payeeId = payeeId;
            return this;
        }

        public Builder date(Date date) {
            this.date = date;
            return this;
        }

        public Builder comments(String comments) {
            this.comments = comments;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder expenseReport(long reportId) {
            this.reportId = reportId;
            return this;
        }

        public Expense build() {
            return new Expense(null, title, date, amount, comments, filePath, payeeId, reportId);
        }
    }

    public boolean hasImage() {
        return !TextUtils.isEmpty(imagePath);
    }

    public boolean isNewExpense() {
        return id == null || id < 1;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    // KEEP METHODS END

}