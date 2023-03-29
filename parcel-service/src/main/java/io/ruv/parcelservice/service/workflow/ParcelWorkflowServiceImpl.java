package io.ruv.parcelservice.service.workflow;

import feign.FeignException;
import io.ruv.parcelservice.api.ParcelStatus;
import io.ruv.parcelservice.api.UserInfo;
import io.ruv.parcelservice.entity.Parcel;
import io.ruv.parcelservice.repo.ParcelRepository;
import io.ruv.parcelservice.service.exception.ForbiddenParcelStatusChangeException;
import io.ruv.parcelservice.service.exception.IllegalParcelStatusChangeException;
import io.ruv.parcelservice.service.remote.RemoteUserService;
import io.ruv.userservice.api.UserRole;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ParcelWorkflowServiceImpl implements ParcelWorkflowService {

    private final ParcelRepository parcelRepository;
    private final RemoteUserService userService;

    @Override
    public Parcel processCreatedParcel(Parcel parcel, UserInfo user) {

        var saved = new BalanceAdjustingOrchestration(parcel).execute(parcelRepository::save);
        launchWorkflowEvent(saved, user);
        return saved;
    }

    @Override
    public Parcel processUpdatedParcel(Parcel parcel, UserInfo user) {

        var saved = parcelRepository.save(parcel);
        launchWorkflowEvent(saved, user);
        return saved;
    }

    @Override
    public Parcel changeParcelStatus(Parcel parcel, ParcelStatus newStatus, UserInfo user, @Nullable String assignee) {

        var allowedStatuses = allowedStatusChanges(parcel, user);

        if (allowedStatuses.contains(newStatus)) {

            return executeStatusChange(parcel, newStatus, user, assignee);

        } else {

            throw new ForbiddenParcelStatusChangeException();
        }
    }

    private Set<ParcelStatus> allowedStatusChanges(Parcel parcel, UserInfo userInfo) {

        var allowedStatuses = new TreeSet<ParcelStatus>();

        var currentStatus = parcel.getStatus();

        if (userInfo.roles().contains(UserRole.ADMIN)) {

            switch (currentStatus) {

                case CREATED -> allowedStatuses.addAll(List.of(ParcelStatus.ASSIGNED, ParcelStatus.CANCELLED));
                case ASSIGNED ->
                        allowedStatuses.addAll(List.of(ParcelStatus.CREATED, ParcelStatus.CANCELLED, ParcelStatus.ASSIGNED));
                case DELIVERY_PROBLEM -> allowedStatuses.add(ParcelStatus.CANCELLED);
            }
        }

        if (userInfo.username().equals(parcel.getAssignedTo()) && userInfo.roles().contains(UserRole.COURIER)) {

            switch (currentStatus) {

                case ASSIGNED, DELIVERY_PROBLEM -> allowedStatuses.add(ParcelStatus.DELIVERING);
                case DELIVERING ->
                        allowedStatuses.addAll(List.of(ParcelStatus.DELIVERED, ParcelStatus.DELIVERY_PROBLEM));
            }
        }

        if (userInfo.username().equals(parcel.getCreatedBy()) && userInfo.roles().contains(UserRole.CUSTOMER)) {

            switch (currentStatus) {

                case CREATED, ASSIGNED -> allowedStatuses.add(ParcelStatus.CANCELLED);
            }
        }

        return allowedStatuses;
    }

    private Parcel executeStatusChange(Parcel parcel, ParcelStatus newStatus, UserInfo userInfo, @Nullable String newAssignee) {

        var currentStatus = parcel.getStatus();

        return switch (currentStatus) {

            case CREATED -> switch (newStatus) {

                case ASSIGNED -> {

                    ensureAssigneeExists(newAssignee);
                    var toSave = parcel.setStatus(newStatus).setAssignedTo(newAssignee);
                    var saved = parcelRepository.save(toSave);
                    launchWorkflowEvent(saved, userInfo, currentStatus);
                    yield saved;
                }
                case CANCELLED -> {

                    var toSave = parcel.setStatus(ParcelStatus.CANCELLED);
                    var saved = new BalanceAdjustingOrchestration(toSave).execute(parcelRepository::save);
                    launchWorkflowEvent(saved, userInfo, currentStatus);
                    yield saved;
                }
                default -> throw new IllegalParcelStatusChangeException(parcel.getId(), currentStatus, newStatus);
            };

            case ASSIGNED -> switch (newStatus) {

                case CREATED -> {

                    var prevAssignee = parcel.getAssignedTo();
                    var toSave = parcel.setStatus(ParcelStatus.CREATED).setAssignedTo(null);
                    var saved = parcelRepository.save(toSave);
                    launchWorkflowEvent(saved, userInfo, currentStatus, prevAssignee);
                    yield saved;
                }
                case ASSIGNED -> {

                    ensureAssigneeExists(newAssignee);
                    var prevAssignee = parcel.getAssignedTo();
                    var toSave = parcel.setAssignedTo(newAssignee);
                    var saved = parcelRepository.save(toSave);
                    launchWorkflowEvent(saved, userInfo, currentStatus, prevAssignee);
                    yield saved;
                }
                case CANCELLED -> {

                    var prevAssignee = parcel.getAssignedTo();
                    var toSave = parcel.setStatus(ParcelStatus.CANCELLED).setAssignedTo(null);
                    var saved = new BalanceAdjustingOrchestration(toSave).execute(parcelRepository::save);
                    launchWorkflowEvent(saved, userInfo, currentStatus, prevAssignee);
                    yield saved;
                }
                case DELIVERING -> {

                    var toSave = parcel.setStatus(ParcelStatus.DELIVERING);
                    var saved = parcelRepository.save(toSave);
                    launchWorkflowEvent(saved, userInfo, currentStatus);
                    yield saved;
                }
                default -> throw new IllegalParcelStatusChangeException(parcel.getId(), currentStatus, newStatus);
            };

            case DELIVERING -> switch (newStatus) {

                case DELIVERED -> {

                    var toSave = parcel.setStatus(ParcelStatus.DELIVERED);
                    var saved = new BalanceAdjustingOrchestration(toSave).execute(parcelRepository::save);
                    adjustCreatorBalance(saved);
                    launchWorkflowEvent(saved, userInfo, currentStatus);
                    yield saved;
                }
                case DELIVERY_PROBLEM -> {

                    var toSave = parcel.setStatus(ParcelStatus.DELIVERY_PROBLEM);
                    var saved = parcelRepository.save(toSave);
                    launchWorkflowEvent(saved, userInfo, currentStatus);
                    yield saved;
                }
                default -> throw new IllegalParcelStatusChangeException(parcel.getId(), currentStatus, newStatus);
            };

            case DELIVERY_PROBLEM -> switch (newStatus) {

                case DELIVERING -> {

                    var toSave = parcel.setStatus(ParcelStatus.DELIVERING);
                    var saved = parcelRepository.save(toSave);
                    launchWorkflowEvent(saved, userInfo, currentStatus);
                    yield saved;
                }
                case CANCELLED -> {

                    var prevAssignee = parcel.getAssignedTo();
                    var toSave = parcel.setStatus(ParcelStatus.CANCELLED);
                    var saved = new BalanceAdjustingOrchestration(toSave).execute(parcelRepository::save);
                    launchWorkflowEvent(saved, userInfo, currentStatus, prevAssignee);
                    yield saved;
                }
                default -> throw new IllegalParcelStatusChangeException(parcel.getId(), currentStatus, newStatus);
            };

            case DELIVERED, CANCELLED ->
                    throw new IllegalParcelStatusChangeException(parcel.getId(), currentStatus, newStatus);
        };
    }


    private void ensureAssigneeExists(String assignee) {

        try {

            userService.getCourier(assignee);
        } catch (FeignException.NotFound notFound) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("User '%s' was not found.", assignee));
        } catch (FeignException e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to query remote user-service.", e);
        }
    }

    private void launchWorkflowEvent(Parcel parcel, UserInfo userInfo) {

        launchWorkflowEvent(parcel, userInfo, null, null);
    }

    private void launchWorkflowEvent(Parcel parcel, UserInfo userInfo, ParcelStatus prevStatus) {

        launchWorkflowEvent(parcel, userInfo, prevStatus, null);
    }

    @SuppressWarnings("unused")
    private void launchWorkflowEvent(Parcel parcel, UserInfo userInfo,
                                     @Nullable ParcelStatus prevStatus, @Nullable String prevAssignee) {

        // todo feed it to streaming platform
    }

    @RequiredArgsConstructor
    private class BalanceAdjustingOrchestration {
        private final Parcel parcel;

        public Parcel execute(Function<Parcel, Parcel> controlledAction) {

            adjustCreatorBalance(parcel);

            try {

                return controlledAction.apply(parcel);
            } catch (RuntimeException e) {

                cancelCreatorBalanceAdjustment(parcel);
                throw e;
            }
        }
    }

    @SuppressWarnings("unused")
    private void adjustCreatorBalance(Parcel parcel) {
        //todo
    }

    @SuppressWarnings("unused")
    private void cancelCreatorBalanceAdjustment(Parcel parcel) {
        //todo
    }
}
