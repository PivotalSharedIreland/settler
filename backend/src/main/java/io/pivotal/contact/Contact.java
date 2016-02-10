package io.pivotal.contact;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.pivotal.property.Property;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // TODO: Add anotation for NotEmpty name, look into conditional validation. API is expecting id only for existing contacts
    private String name;
    private String phone;
    private String email;
    //    @JsonIdentityReference(alwaysAsId = true)
    //    @ManyToOne(fetch = FetchType.EAGER)
    //    private Property property;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }

        Contact obj = (Contact) o;

        return Objects.equals(this.id, obj.id) && Objects.equals(this.name, obj.name) && Objects.equals(this.phone, obj.phone) && Objects.equals(this.email, obj.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.phone, this.email);
    }
}
