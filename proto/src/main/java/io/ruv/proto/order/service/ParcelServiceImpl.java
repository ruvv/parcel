package io.ruv.proto.order.service;

import io.ruv.proto.order.api.ParcelCreateDto;
import io.ruv.proto.order.api.ParcelDto;
import io.ruv.proto.order.api.ParcelStatus;
import io.ruv.proto.order.api.ParcelUpdateDto;
import io.ruv.proto.order.entity.Parcel;
import io.ruv.proto.order.repo.ParcelRepository;
import io.ruv.proto.order.service.converter.ParcelConverter;
import io.ruv.proto.user.service.CourierService;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParcelServiceImpl implements ParcelService {

    private final ParcelRepository parcelRepository;
    private final ParcelConverter parcelConverter;
    private final CourierService courierService;

    @Override
    public List<ParcelDto> assignedTo(String courierUsername) {

        return parcelRepository.findByAssignedTo(courierUsername).stream()
                .map(parcelConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParcelDto> createdBy(String customerUsername) {

        return parcelRepository.findByCreatedBy(customerUsername).stream()
                .map(parcelConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParcelDto createParcel(String customerUsername, ParcelCreateDto parcelCreateDto) {

        var toSave = parcelConverter.toEntity(parcelCreateDto);
        toSave.setCreatedBy(customerUsername);
        var saved = parcelRepository.save(toSave);

        return parcelConverter.toDto(saved);
    }

    @Override
    @Transactional
    public ParcelDto updateParcel(long id, ParcelUpdateDto parcelUpdateDto) {

        var parcel = parcelRepository.findById(id)
                .orElseThrow(() -> throwParcelNotFound(id));

        var toSave = parcelConverter.updateEntity(parcel, parcelUpdateDto);
        var saved = parcelRepository.save(toSave);

        return parcelConverter.toDto(saved);
    }

    @Override
    @Transactional
    public ParcelDto updateParcelStatus(long id, ParcelStatus status) {

        var parcel = parcelRepository.findById(id)
                .orElseThrow(() -> throwParcelNotFound(id));

        parcel.setStatus(status);
        var saved = parcelRepository.save(parcel);

        return parcelConverter.toDto(saved);
    }

    @Override
    public List<ParcelDto> find(int page, int size,
                                @Nullable ParcelStatus parcelStatus,
                                @Nullable String customerUsername,
                                @Nullable String courierUsername) {

        var pageable = PageRequest.of(page, size);

        var probe = new Parcel();
        if (parcelStatus != null) {
            probe.setStatus(parcelStatus);
        }
        if (customerUsername != null) {
            probe.setCreatedBy(customerUsername);
        }
        if (courierUsername != null) {
            probe.setAssignedTo(courierUsername);
        }

        return parcelRepository.findAll(Example.of(probe), pageable).get()
                .map(parcelConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParcelDto findById(long id) {

        var parcel = parcelRepository.findById(id)
                .orElseThrow(() -> throwParcelNotFound(id));

        return parcelConverter.toDto(parcel);
    }

    @Override
    @Transactional
    public ParcelDto assignToCourier(long id, String courierUsername) {

        var parcel = parcelRepository.findById(id)
                .orElseThrow(() -> throwParcelNotFound(id));

        var courier = courierService.findCourierById(courierUsername);

        var toSave = parcel
                .setAssignedTo(courier.getUsername())
                .setStatus(ParcelStatus.ASSIGNED);
        var saved = parcelRepository.save(toSave);

        return parcelConverter.toDto(saved);
    }

    @Override
    @Transactional
    public ParcelDto unAssign(long id) {

        var parcel = parcelRepository.findById(id)
                .orElseThrow(() -> throwParcelNotFound(id));

        var toSave = parcel.setAssignedTo(null);
        var saved = parcelRepository.save(toSave);

        return parcelConverter.toDto(saved);
    }

    private RuntimeException throwParcelNotFound(long id) {

        var message = String.format("Parcel '%d' was not found.", id);

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    }
}
