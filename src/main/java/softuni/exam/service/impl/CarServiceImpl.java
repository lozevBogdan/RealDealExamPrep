package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import softuni.exam.models.dtos.CarImportDto;
import softuni.exam.models.entities.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.carRepository = carRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }


    @Override
    public boolean areImported() {

        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        return String.join("",Files
                .readAllLines(Path.of("src/main/resources/files/json/cars.json")));
    }

    @Override
    public String importCars() throws IOException {

        StringBuilder sb = new StringBuilder();

        CarImportDto[] carImportDtos =
                this.gson.fromJson(this.readCarsFileContent(), CarImportDto[].class);

        for (CarImportDto carImportDto : carImportDtos) {

            if(this.validationUtil.isValid(carImportDto)){

                Car car = this.modelMapper.map(carImportDto, Car.class);
                this.carRepository.save(car);

                sb.append(String.format("Successfully imported car - %s - %s",
                        carImportDto.getMake(),carImportDto.getModel()));
                sb.append(System.lineSeparator());

            }else {

                sb.append("Invalid car");
                sb.append(System.lineSeparator());

            }


        }



        return sb.toString();
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        return null;
    }
}
