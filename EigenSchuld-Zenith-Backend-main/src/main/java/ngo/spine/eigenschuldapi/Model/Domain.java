package ngo.spine.eigenschuldapi.Model;

import jakarta.persistence.*;
import ngo.spine.eigenschuldapi.Services.AesEncrypter;

import java.util.UUID;

@Entity
public class Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "domain_id")
    public UUID id;

    @Column(name = "name")
    @Convert(converter = AesEncrypter.class)
    public String name;

    @Column(name = "logo_location")
    public String logo_location;

    @Column(name = "primary_color_hex", columnDefinition = "VARCHAR(7)")
    public String primary_color_hex;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo_location() {
        return logo_location;
    }

    public void setLogo_location(String location) {
        this.logo_location = location;
    }

    public String getPrimary_color_hex() {
        return primary_color_hex;
    }

    public void setPrimary_color_hex(String street) {
        this.primary_color_hex = street;
    }
}
