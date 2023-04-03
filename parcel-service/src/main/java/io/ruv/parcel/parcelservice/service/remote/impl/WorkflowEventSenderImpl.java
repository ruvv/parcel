package io.ruv.parcel.parcelservice.service.remote.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ruv.parcel.messaging.ParcelMessagingProperties;
import io.ruv.parcel.messaging.api.WorkflowEventDto;
import io.ruv.parcel.parcel.api.ParcelStatus;
import io.ruv.parcel.parcelservice.entity.Parcel;
import io.ruv.parcel.parcelservice.service.remote.WorkflowEventSender;
import io.ruv.parcel.user.api.auth.UserInfo;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class WorkflowEventSenderImpl implements WorkflowEventSender {

    private final AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper;
    private final ParcelMessagingProperties properties;


    @Override
    public void sendWorkflowEvent(Parcel parcel, @Nullable UserInfo userInfo,
                                  @Nullable ParcelStatus previousStatus, @Nullable String previousAssignee) {

        try {

            var workflowEvent = new WorkflowEventDto()
                    .setParcelId(parcel.getId())
                    .setCreatedBy(parcel.getCreatedBy())
                    .setParcelCost(parcel.getCost())
                    .setFromStatus(previousStatus)
                    .setToStatus(parcel.getStatus())
                    .setFromAssignee(previousAssignee)
                    .setToAssignee(parcel.getAssignedTo())
                    .setActor(userInfo);

            var payload = objectMapper.writeValueAsBytes(workflowEvent);

            amqpTemplate.convertAndSend(properties.getExchangeName(),
                    properties.getRouteBase() + parcel.getStatus().name(),
                    payload);
        } catch (JsonProcessingException e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Failed to serialize workflow event for parcel '%s'.", parcel.getId()), e);
        }
    }
}
