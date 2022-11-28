package com.happyfood.service.dto;

import com.happyfood.domain.enumeration.State;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.happyfood.domain.WorkDay} entity.
 */
public class WorkDayDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 40)
    private String dayName;

    @NotNull
    @Size(max = 40)
    private String month;

    @NotNull
    private State state;

    private HoraryDTO horary;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public HoraryDTO getHorary() {
        return horary;
    }

    public void setHorary(HoraryDTO horary) {
        this.horary = horary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkDayDTO)) {
            return false;
        }

        WorkDayDTO workDayDTO = (WorkDayDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workDayDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkDayDTO{" +
            "id=" + getId() +
            ", dayName='" + getDayName() + "'" +
            ", month='" + getMonth() + "'" +
            ", state='" + getState() + "'" +
            ", horary=" + getHorary() +
            "}";
    }
}
