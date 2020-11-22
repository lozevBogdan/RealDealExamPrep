package softuni.exam.models.dtos;

import java.util.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "offers")
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferImportRootDto {

    @XmlElement(name = "offer")
    private Set<OfferImportDto> offerSet;

    public OfferImportRootDto() {
    }

    public Set<OfferImportDto> getOffer() {
        return offerSet;
    }

    public void setOffer(Set<OfferImportDto> offer) {
        this.offerSet = offer;
    }
}
