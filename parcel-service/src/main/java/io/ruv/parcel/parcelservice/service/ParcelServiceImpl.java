package io.ruv.parcel.parcelservice.service;

import io.ruv.parcel.parcel.api.ParcelCreateDto;
import io.ruv.parcel.parcel.api.ParcelDto;
import io.ruv.parcel.parcel.api.ParcelStatus;
import io.ruv.parcel.parcel.api.ParcelUpdateDto;
import io.ruv.parcel.parcelservice.repo.ParcelRepository;
import io.ruv.parcel.parcelservice.service.converter.ParcelConverter;
import io.ruv.parcel.parcelservice.service.workflow.ParcelWorkflowService;
import io.ruv.parcel.parcelservice.entity.Parcel;
import io.ruv.parcel.parcelservice.service.exception.ParcelNotFoundException;
import io.ruv.parcel.user.api.UserRole;
import io.ruv.parcel.user.api.auth.UserInfo;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParcelServiceImpl implements ParcelService {

    private final ParcelConverter converter;
    private final ParcelRepository repository;
    private final ParcelWorkflowService workflowService;

    @Override
    public ParcelDto getParcel(long id, UserInfo userInfo) {

        if (userInfo.roles().contains(UserRole.ADMIN)) {

            var parcel = repository.findById(id)
                    .orElseThrow(() -> new ParcelNotFoundException(id));

            return converter.dto(parcel);
        } else if(userInfo.roles().contains(UserRole.COURIER)) {

            var parcel = repository.findByIdAndAssignedTo(id, userInfo.username())
                    .orElseThrow(() -> new ParcelNotFoundException(id));

            return converter.dto(parcel);
        } else {

            var parcel = repository.findByIdAndCreatedByOrAssignedTo(id, userInfo.username())
                    .orElseThrow(() -> new ParcelNotFoundException(id));

            return converter.dto(parcel);
        }
    }

    @Override
    public List<ParcelDto> findParcels(int page, int size, @Nullable String createdBy, @Nullable String assignedTo) {

        var pageable = PageRequest.of(page, size);

        var probe = new Parcel()
                .setCreatedBy(createdBy)
                .setAssignedTo(assignedTo);

        return repository.findAll(Example.of(probe), pageable).stream()
                .map(converter::dto)
                .collect(Collectors.toList());
    }

    @Override
    public ParcelDto createParcel(ParcelCreateDto createDto, UserInfo userInfo) {

        var toSave = converter.entity(createDto)
                .setCreatedBy(userInfo.username());

        var saved = workflowService.processCreatedParcel(toSave, userInfo);

        return converter.dto(saved);
    }

    @Override
    @Transactional
    public ParcelDto updateParcel(long id, ParcelUpdateDto updateDto, UserInfo userInfo) {

        var parcel = repository.findByIdAndCreatedBy(id, userInfo.username())
                .orElseThrow(() -> new ParcelNotFoundException(id));

        var toSave = converter.update(parcel, updateDto);

        var saved = workflowService.processUpdatedParcel(toSave, userInfo);

        return converter.dto(saved);
    }

    @Override
    @Transactional
    public ParcelDto updateParcelStatus(long id, ParcelStatus status, @Nullable UserInfo userInfo) {

        var parcel = repository.findById(id)
                .orElseThrow(() -> new ParcelNotFoundException(id));

        return changeParcelStatus(parcel, status, userInfo, null);
    }

    @Override
    public ParcelDto updateAssignee(long id, String assigneeUsername, UserInfo userInfo) {

        var parcel = repository.findById(id)
                .orElseThrow(() -> new ParcelNotFoundException(id));

        return changeParcelStatus(parcel, ParcelStatus.ASSIGNED, userInfo, assigneeUsername);
    }

    private ParcelDto changeParcelStatus(Parcel parcel, ParcelStatus newStatus,
                                         @Nullable UserInfo userInfo, @Nullable String assignee) {

        var saved = workflowService.changeParcelStatus(parcel, newStatus, userInfo, assignee);

        return converter.dto(saved);
    }
}
