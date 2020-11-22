package softuni.exam.models.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

@XmlRootElement(name = "sellers")
@XmlAccessorType(XmlAccessType.FIELD)
public class SellerImportRootDto {

    @XmlElement(name = "seller")
    private Set<SellerImportDto> sellerSet;


    public SellerImportRootDto() {
    }

    public Set<SellerImportDto> getSellerSet() {
        return sellerSet;
    }

    public void setSellerSet(Set<SellerImportDto> sellerSet) {
        this.sellerSet = sellerSet;
    }
}
