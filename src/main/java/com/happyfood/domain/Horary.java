package com.happyfood.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Horary.
 */
@Entity
@Table(name = "horary")
public class Horary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private String startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private String endTime;

    @OneToMany(mappedBy = "horary")
    @JsonIgnoreProperties(value = { "employees", "horary" }, allowSetters = true)
    private Set<WorkDay> workDays = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Horary id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public Horary startTime(String startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public Horary endTime(String endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Set<WorkDay> getWorkDays() {
        return this.workDays;
    }

    public void setWorkDays(Set<WorkDay> workDays) {
        if (this.workDays != null) {
            this.workDays.forEach(i -> i.setHorary(null));
        }
        if (workDays != null) {
            workDays.forEach(i -> i.setHorary(this));
        }
        this.workDays = workDays;
    }

    public Horary workDays(Set<WorkDay> workDays) {
        this.setWorkDays(workDays);
        return this;
    }

    public Horary addWorkDay(WorkDay workDay) {
        this.workDays.add(workDay);
        workDay.setHorary(this);
        return this;
    }

    public Horary removeWorkDay(WorkDay workDay) {
        this.workDays.remove(workDay);
        workDay.setHorary(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Horary)) {
            return false;
        }
        return id != null && id.equals(((Horary) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Horary{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            "}";
    }
}
