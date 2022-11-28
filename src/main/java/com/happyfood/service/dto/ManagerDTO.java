package com.happyfood.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.happyfood.domain.Manager} entity.
 */
public class ManagerDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String documentNumber;

    @NotNull
    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String secondName;

    @NotNull
    @Size(max = 50)
    private String firstLastname;

    @Size(max = 50)
    private String secondLastname;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getFirstLastname() {
        return firstLastname;
    }

    public void setFirstLastname(String firstLastname) {
        this.firstLastname = firstLastname;
    }

    public String getSecondLastname() {
        return secondLastname;
    }

    public void setSecondLastname(String secondLastname) {
        this.secondLastname = secondLastname;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ManagerDTO)) {
            return false;
        }

        ManagerDTO managerDTO = (ManagerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, managerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManagerDTO{" +
            "id=" + getId() +
            ", documentNumber='" + getDocumentNumber() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", secondName='" + getSecondName() + "'" +
            ", firstLastname='" + getFirstLastname() + "'" +
            ", secondLastname='" + getSecondLastname() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
