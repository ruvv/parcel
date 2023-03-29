package io.ruv.parcelservice.service.workflow;

import io.ruv.parcelservice.api.ParcelStatus;
import io.ruv.parcelservice.api.UserInfo;
import io.ruv.parcelservice.entity.Parcel;
import io.ruv.parcelservice.repo.ParcelRepository;
import io.ruv.parcelservice.service.exception.ForbiddenParcelStatusChangeException;
import io.ruv.parcelservice.service.exception.IllegalParcelStatusChangeException;
import io.ruv.userservice.api.UserRole;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ParcelWorkflowServiceImpl implements ParcelWorkflowService {

    private final ParcelRepository parcelRepository;

    @Override
    public Parcel processCreatedParcel(Parcel parcel, UserInfo user) {

        var saved = new BalanceAdjustingOrchestration(parcel).execute(parcelRepository::save);
        notifyCreator(saved);
        notifyAdministrators(saved, user);
        return saved;
    }

    @Override
    public Parcel processUpdatedParcel(Parcel parcel, UserInfo user) {

        var saved = parcelRepository.save(parcel);
        notifyCreator(saved);
        notifyAdministrators(saved, user);
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
                case ASSIGNED -> allowedStatuses.addAll(List.of(ParcelStatus.CREATED, ParcelStatus.CANCELLED));
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

    private Parcel executeStatusChange(Parcel parcel, ParcelStatus newStatus, UserInfo userInfo, @Nullable String assignee) {

        var currentStatus = parcel.getStatus();

        return switch (currentStatus) {

            case CREATED -> switch (newStatus) {

                case ASSIGNED -> {

                    ensureAssigneeExists(assignee);
                    var toSave = parcel.setStatus(newStatus).setAssignedTo(assignee);
                    var saved = parcelRepository.save(toSave);
                    notifyAssignee(saved, assignee);
                    notifyCreator(saved);
                    yield saved;
                }
                case CANCELLED -> {

                    var toSave = parcel.setStatus(ParcelStatus.CANCELLED);
                    var saved = new BalanceAdjustingOrchestration(toSave).execute(parcelRepository::save);
                    notifyCreator(saved);
                    notifyAdministrators(saved, userInfo);
                    yield saved;
                }
                default -> throw new IllegalParcelStatusChangeException(parcel.getId(), currentStatus, newStatus);
            };

            case ASSIGNED -> switch (newStatus) {

                case CREATED -> {

                    var prevAssignee = parcel.getAssignedTo();
                    var toSave = parcel.setStatus(ParcelStatus.CREATED).setAssignedTo(null);
                    var saved = parcelRepository.save(toSave);
                    notifyCreator(saved);
                    notifyAssignee(saved, prevAssignee);
                    yield saved;
                }
                case CANCELLED -> {

                    var prevAssignee = parcel.getAssignedTo();
                    var toSave = parcel.setStatus(ParcelStatus.CANCELLED).setAssignedTo(null);
                    var saved = new BalanceAdjustingOrchestration(toSave).execute(parcelRepository::save);
                    notifyCreator(saved);
                    notifyAssignee(saved, prevAssignee);
                    notifyAdministrators(saved, userInfo);
                    yield saved;
                }
                case DELIVERING -> {

                    var toSave = parcel.setStatus(ParcelStatus.DELIVERING);
                    var saved = parcelRepository.save(toSave);
                    notifyCreator(saved);
                    yield saved;
                }
                default -> throw new IllegalParcelStatusChangeException(parcel.getId(), currentStatus, newStatus);
            };

            case DELIVERING -> switch (newStatus) {

                case DELIVERED -> {

                    var toSave = parcel.setStatus(ParcelStatus.DELIVERED);
                    var saved = new BalanceAdjustingOrchestration(toSave).execute(parcelRepository::save);
                    adjustCreatorBalance(saved);
                    notifyCreator(saved);
                    yield saved;
                }
                case DELIVERY_PROBLEM -> {

                    var toSave = parcel.setStatus(ParcelStatus.DELIVERY_PROBLEM);
                    var saved = parcelRepository.save(toSave);
                    notifyCreator(saved);
                    notifyAdministrators(saved, userInfo);
                    yield saved;
                }
                default -> throw new IllegalParcelStatusChangeException(parcel.getId(), currentStatus, newStatus);
            };

            case DELIVERY_PROBLEM -> switch (newStatus) {

                case DELIVERING -> {

                    var toSave = parcel.setStatus(ParcelStatus.DELIVERING);
                    var saved = parcelRepository.save(toSave);
                    notifyCreator(saved);
                    notifyAdministrators(saved, userInfo);
                    yield saved;
                }
                case CANCELLED -> {

                    var toSave = parcel.setStatus(ParcelStatus.CANCELLED);
                    var saved = new BalanceAdjustingOrchestration(toSave).execute(parcelRepository::save);
                    notifyCreator(saved);
                    notifyAssignee(saved, parcel.getAssignedTo());
                    notifyAdministrators(saved, userInfo);
                    yield saved;
                }
                default -> throw new IllegalParcelStatusChangeException(parcel.getId(), currentStatus, newStatus);
            };

            case DELIVERED, CANCELLED ->
                    throw new IllegalParcelStatusChangeException(parcel.getId(), currentStatus, newStatus);
        };
    }

    private void adjustCreatorBalance(Parcel parcel) {
        //todo
    }

    private void cancelBalanceAdjustment(Parcel parcel) {
        //todo
    }

    private void ensureAssigneeExists(@Nullable String assignee) {
        //todo
    }

    private void notifyAssignee(Parcel parcel, String assignee) {
        //todo
    }

    private void notifyCreator(Parcel parcel) {
        //todo
    }

    private void notifyAdministrators(Parcel parcel, UserInfo userInfo) {
        //todo
    }

    @RequiredArgsConstructor
    private class BalanceAdjustingOrchestration {
        private final Parcel parcel;

        public Parcel execute(Function<Parcel, Parcel> controlledAction) {

            adjustCreatorBalance(parcel);

            try {

                return controlledAction.apply(parcel);
            } catch (RuntimeException e) {

                cancelBalanceAdjustment(parcel);
                throw e;
            }
        }
    }
}
