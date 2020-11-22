package softuni.exam.service.impl;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.OfferImportDto;
import softuni.exam.models.dtos.OfferImportRootDto;
import softuni.exam.models.entities.Car;
import softuni.exam.models.entities.Offer;
import softuni.exam.models.entities.Picture;
import softuni.exam.models.entities.Seller;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class OfferServiceImpl implements OfferService {
    private final String PATH_OFFERS =
            "src/main/resources/files/xml/offers.xml";

    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final CarRepository carRepository;
    private final OfferRepository offerRepository;
    private final SellerRepository sellerRepository;

    public OfferServiceImpl(PictureRepository pictureRepository, ModelMapper modelMapper,
                            XmlParser xmlParser, ValidationUtil validationUtil, CarRepository carRepository, OfferRepository offerRepository, SellerRepository sellerRepository) {
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.carRepository = carRepository;
        this.offerRepository = offerRepository;
        this.sellerRepository = sellerRepository;
    }


    @Override
    public boolean areImported() {
        return this.offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return String.join("",Files.readAllLines(Path.of(PATH_OFFERS)));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {

        StringBuilder sb = new StringBuilder();
        OfferImportRootDto offerImportRootDto =
                this.xmlParser.parseXml(OfferImportRootDto.class, PATH_OFFERS);

        for (OfferImportDto offerDto : offerImportRootDto.getOffer()) {

//            Optional<Offer> byAddedOn =
//                    this.offerRepository.findByAddedOn(offerDto.getAddedOn());

            if(this.validationUtil.isValid(offerDto) ){

                Offer offer = this.modelMapper.map(offerDto, Offer.class);
                Optional<Car> car = this.carRepository
                        .findById(offerDto.getCar().getId());

                Set<Picture> pictures = new HashSet<>(car.get().getPictures());

                Optional<Seller> seller = this.sellerRepository
                        .findById(offerDto.getSeller().getId());

                System.out.println();

                offer.setCar(car.get());
                offer.setSeller(seller.get());
                offer.setPictures(pictures);

               System.out.println();

                this.offerRepository.saveAndFlush(offer);
                sb.append(String.format("Successfully import offer %s - %s",
                        offer.getAddedOn().toString(),offer.isHasGoldStatus()))
                .append(System.lineSeparator());

            }else {
                 sb.append("Invalid offer.").append(System.lineSeparator());

            }


        }



        return sb.toString();
    }
}
