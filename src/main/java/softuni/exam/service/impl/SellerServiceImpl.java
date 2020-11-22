package softuni.exam.service.impl;


import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.SellerImportDto;
import softuni.exam.models.dtos.SellerImportRootDto;
import softuni.exam.models.entities.Rating;
import softuni.exam.models.entities.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class SellerServiceImpl implements SellerService {
    private final String PATH_SELLERS = "src/main/resources/files/xml/sellers.xml";


    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final SellerRepository sellerRepository;

    public SellerServiceImpl(ModelMapper modelMapper, XmlParser xmlParser, Gson gson,
                             ValidationUtil validationUtil, SellerRepository sellerRepository) {
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.sellerRepository = sellerRepository;
    }


    @Override
    public boolean areImported() {
        return this.sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return String.join("", Files.readAllLines(Path.of(PATH_SELLERS)));
    }

    @Override
    public String importSellers() throws IOException, JAXBException {

        StringBuilder sb = new StringBuilder();

        SellerImportRootDto sellerImportRootDto =
                this.xmlParser.parseXml(SellerImportRootDto.class, PATH_SELLERS);

        for (SellerImportDto sellerDto : sellerImportRootDto.getSellerSet()) {

            Rating rating;

            try {
                rating = Rating.valueOf(sellerDto.getRating());
            }catch (Exception e){
                sb.append("Invalid seller").append(System.lineSeparator());
                continue;
            }

            Optional<Seller> byEmail = this.sellerRepository.findByEmail(sellerDto.getEmail());

            if (this.validationUtil.isValid(sellerDto) && byEmail.isEmpty()){

                Seller seller = this.modelMapper.map(sellerDto, Seller.class);

                this.sellerRepository.saveAndFlush(seller);

                sb.append(String.format("Successfully import seller %s - %s",
                        seller.getLastName(),seller.getEmail()))
                        .append(System.lineSeparator());


            }else {
                sb.append("Invalid seller").append(System.lineSeparator());
            }


        }


        return sb.toString();
    }
}
