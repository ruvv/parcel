package io.ruv.parcel.parcelservice.service.workflow;

import feign.FeignException;
import io.ruv.parcel.parcel.api.ParcelStatus;
import io.ruv.parcel.parcelservice.entity.Parcel;
import io.ruv.parcel.parcelservice.repo.ParcelRepository;
import io.ruv.parcel.parcelservice.service.exception.ForbiddenParcelStatusChangeException;
import io.ruv.parcel.parcelservice.service.exception.IllegalParcelStatusChangeException;
import io.ruv.parcel.parcelservice.service.remote.RemoteUserService;
import io.ruv.parcel.parcelservice.service.remote.WorkflowEventSender;
import io.ruv.parcel.user.api.UserRole;
import io.ruv.parcel.user.api.auth.UserInfo;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
@RequiredArgsConstructor
public class ParcelWorkflowServiceImpl implements ParcelWorkflowService {

    private final ParcelRepository parcelRepository;
    private final RemoteUserService userService;
    private final WorkflowEventSender workflowEventSender;

    @Override
    public Parcel processCreatedParcel(Parcel parcel, UserInfo user) {

        return saveWithEvent(parcel, user, null, null);
    }

    @Override
    public Parcel processUpdatedParcel(Parcel parcel, UserInfo user) {

        return saveWithEvent(parcel, user, null, null);
    }

    @Override
    public Parcel changeParcelStatus(Parcel parcel, ParcelStatus newStatus,
                                     @Nullable UserInfo user, @Nullable String assignee) {

        var allowedStatuses = allowedStatusChanges(parcel, user);

        if (allowedStatuses.contains(newStatus)) {

            return executeStatusChange(parcel, newStatus, user, assignee);

        } else {

            throw new ForbiddenParcelStatusChangeException();
        }
    }

    private Set<ParcelStatus> allowedStatusChanges(Parcel parcel, @Nullable UserInfo userInfo) {


        var currentStatus = parcel.getStatus();

        if (userInfo == null) {
            // app parcel processing

            return currentStatus == ParcelStatus.CREATED ?
                    Set.of(ParcelStatus.ACCEPTED, ParcelStatus.REJECTED)
                    : Set.of();
        } else {
            // user parcel processing

            var allowedStatuses = new TreeSet<ParcelStatus>();

            if (userInfo.roles().contains(UserRole.ADMIN)) {

                switch (currentStatus) {

                    // admin can assign or cancel
                    case ACCEPTED -> allowedStatuses.addAll(List.of(ParcelStatus.ASSIGNED, ParcelStatus.CANCELLED));
                    // admin can reassign or cancel
                    case ASSIGNED -> allowedStatuses.addAll(List.of(ParcelStatus.CANCELLED, ParcelStatus.ASSIGNED));
                    // admin can cancel
                    case DELIVERY_PROBLEM -> allowedStatuses.add(ParcelStatus.CANCELLED);
                }
            }

            if (userInfo.username().equals(parcel.getAssignedTo()) && userInfo.roles().contains(UserRole.COURIER)) {

                switch (currentStatus) {

                    //courier can start or continue delivery
                    case ASSIGNED, DELIVERY_PROBLEM -> allowedStatuses.add(ParcelStatus.DELIVERING);
                    // courier can complete delivery or report a problem
                    case DELIVERING ->
                            allowedStatuses.addAll(List.of(ParcelStatus.DELIVERED, ParcelStatus.DELIVERY_PROBLEM));
                }
            }

            if (userInfo.username().equals(parcel.getCreatedBy()) && userInfo.roles().contains(UserRole.CUSTOMER)) {

                switch (currentStatus) {

                    // customer can cancel delivery until it starts
                    case CREATED, ACCEPTED, ASSIGNED -> allowedStatuses.add(ParcelStatus.CANCELLED);
                }
            }

            return allowedStatuses;
        }
    }

    private Parcel executeStatusChange(Parcel parcel, ParcelStatus newStatus,
                                       @Nullable UserInfo userInfo, @Nullable String newAssignee) {

        var currentStatus = parcel.getStatus();

        return switch (currentStatus) {

            case CREATED -> switch (newStatus) {

                // delivery can be accepted/rejected by system after checking /w user balance
                // customer can cancel delivery
                case ACCEPTED, REJECTED, CANCELLED -> saveWithEvent(parcel.setStatus(newStatus), userInfo, null, null);
                default -> throw new IllegalParcelStatusChangeException(parcel.getId(), currentStatus, newStatus);
            };

            case ACCEPTED -> switch (newStatus) {

                // admin can assign delivery
                case ASSIGNED -> {
                    ensureAssigneeExists(newAssignee);
                    yield saveWithEvent(parcel.setStatus(newStatus), userInfo, currentStatus, newAssignee);
                }
                // customer or admin can cancel delivery
                case CANCELLED -> saveWithEvent(parcel.setStatus(newStatus), userInfo, currentStatus, null);
                default -> throw new IllegalParcelStatusChangeException(parcel.getId(), currentStatus, newStatus);
            };

            case ASSIGNED -> switch (newStatus) {

                // admin can reassign delivery
                case ASSIGNED -> {
                    ensureAssigneeExists(newAssignee);
                    var prevAssignee = parcel.getAssignedTo();
                    yield saveWithEvent(parcel.setAssignedTo(newAssignee), userInfo, currentStatus, prevAssignee);
                }
                // customer or admin can cancel delivery
                case CANCELLED -> {
                    var prevAssignee = parcel.getAssignedTo();
                    yield saveWithEvent(parcel.setStatus(newStatus).setAssignedTo(null), userInfo, currentStatus, prevAssignee);
                }
                // courier can start delivery
                case DELIVERING -> saveWithEvent(parcel.setStatus(newStatus), userInfo, currentStatus, null);
                default -> throw new IllegalParcelStatusChangeException(parcel.getId(), currentStatus, newStatus);
            };

            case DELIVERING -> switch (newStatus) {

                // courier can complete delivery
                case DELIVERED -> saveWithEvent(parcel.setStatus(newStatus), userInfo, currentStatus, null);
                // courier can report a problem
                case DELIVERY_PROBLEM -> saveWithEvent(parcel.setStatus(newStatus), userInfo, currentStatus, null);
                default -> throw new IllegalParcelStatusChangeException(parcel.getId(), currentStatus, newStatus);
            };

            case DELIVERY_PROBLEM -> switch (newStatus) {

                // courier can continue delivery
                case DELIVERING -> saveWithEvent(parcel.setStatus(newStatus), userInfo, currentStatus, null);
                // admin can cancel delivery
                case CANCELLED -> {
                    var prevAssignee = parcel.getAssignedTo();
                    yield saveWithEvent(parcel.setStatus(newStatus).setAssignedTo(null), userInfo, currentStatus, prevAssignee);
                }
                default -> throw new IllegalParcelStatusChangeException(parcel.getId(), currentStatus, newStatus);
            };

            // end states
            case DELIVERED, CANCELLED, REJECTED ->
                    throw new IllegalParcelStatusChangeException(parcel.getId(), currentStatus, newStatus);
        };
    }

    private Parcel saveWithEvent(Parcel parcel, @Nullable UserInfo userInfo,
                                 @Nullable ParcelStatus prevStatus, @Nullable String prevAssignee) {

        var saved = parcelRepository.save(parcel);
        workflowEventSender.sendWorkflowEvent(parcel, userInfo, prevStatus, prevAssignee);
        return saved;
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
}
