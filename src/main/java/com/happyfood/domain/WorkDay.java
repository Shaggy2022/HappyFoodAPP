package com.happyfood.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.happyfood.domain.enumeration.State;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A WorkDay.
 */
@Entity
@Table(name = "work_day")
public class WorkDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 40)
    @Column(name = "day_name", length = 40, nullable = false, unique = true)
    private String dayName;

    @NotNull
    @Size(max = 40)
    @Column(name = "month", length = 40, nullable = false, unique = true)
    private String month;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    @OneToMany(mappedBy = "workDay")
    @JsonIgnoreProperties(value = { "user", "workDay", "manager" }, allowSetters = true)
    private Set<Employee> employees = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "workDays" }, allowSetters = true)
    private Horary horary;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkDay id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDayName() {
        return this.dayName;
    }

    public WorkDay dayName(String dayName) {
        this.setDayName(dayName);
        return this;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getMonth() {
        return this.month;
    }

    public WorkDay month(String month) {
        this.setMonth(month);
        return this;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public State getState() {
        return this.state;
    }

    public WorkDay state(State state) {
        this.setState(state);
        return this;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.setWorkDay(null));
        }
        if (employees != null) {
            employees.forEach(i -> i.setWorkDay(this));
        }
        this.employees = employees;
    }

    public WorkDay employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public WorkDay addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.setWorkDay(this);
        return this;
    }

    public WorkDay removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.setWorkDay(null);
        return this;
    }

    public Horary getHorary() {
        return this.horary;
    }

    public void setHorary(Horary horary) {
        this.horary = horary;
    }

    public WorkDay horary(Horary horary) {
        this.setHorary(horary);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkDay)) {
            return false;
        }
        return id != null && id.equals(((WorkDay) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkDay{" +
            "id=" + getId() +
            ", dayName='" + getDayName() + "'" +
            ", month='" + getMonth() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
