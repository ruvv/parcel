package io.ruv.parcel.userservice.service.remote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ruv.parcel.messaging.ParcelMessagingProperties;
import io.ruv.parcel.messaging.api.ParcelApprovalDto;
import io.ruv.parcel.messaging.api.WorkflowEventDto;
import io.ruv.parcel.parcel.api.ParcelStatus;
import io.ruv.parcel.user.api.customer.BalanceChangeDto;
import io.ruv.parcel.userservice.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserBalanceChangingListener {

    private final ObjectMapper objectMapper;
    private final AmqpTemplate amqpTemplate;
    private final CustomerService customerService;
    private final ParcelMessagingProperties properties;

    @RabbitListener(queues = "${parcel.messaging.workflow.created.queue}")
    public void onParcelCreated(byte[] payload) {

        try {

            var workflowEvent = objectMapper.readValue(payload, WorkflowEventDto.class);

            if (workflowEvent.getToStatus() == ParcelStatus.CREATED) {

                try {

                    customerService.updateBalance(workflowEvent.getCreatedBy(), new BalanceChangeDto()
                            .setBalanceDelta(-workflowEvent.getParcelCost())
                            .setLockedDelta(workflowEvent.getParcelCost()));

                    sendApproval(workflowEvent.getParcelId(), true, "Balance changed.");
                } catch (RuntimeException e) {

                    sendApproval(workflowEvent.getParcelId(), false, e.getMessage());
                }
            }
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    @RabbitListener(queues = "${parcel.messaging.workflow.cancelled.queue}")
    public void onParcelCancelled(byte[] payload) {

        try {

            var workflowEvent = objectMapper.readValue(payload, WorkflowEventDto.class);

            if (workflowEvent.getToStatus() == ParcelStatus.CANCELLED) {

                customerService.updateBalance(workflowEvent.getCreatedBy(), new BalanceChangeDto()
                        .setBalanceDelta(workflowEvent.getParcelCost())
                        .setLockedDelta(-workflowEvent.getParcelCost()));
            }
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    private void sendApproval(long parcelId, boolean success, String comment) {

        try {

            var approvalDto = new ParcelApprovalDto()
                    .setParcelId(parcelId)
                    .setSuccess(success)
                    .setComment(comment);

            var payload = objectMapper.writeValueAsBytes(approvalDto);


            amqpTemplate.convertAndSend(properties.getExchangeName(),
                    properties.getRouteBase() + ParcelStatus.BALANCE_PROCESSED,
                    payload);
        } catch (JsonProcessingException e) {

            throw new RuntimeException(e);
        }
    }
}
