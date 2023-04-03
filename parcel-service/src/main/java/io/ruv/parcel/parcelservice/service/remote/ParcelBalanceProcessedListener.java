package io.ruv.parcel.parcelservice.service.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ruv.parcel.messaging.api.ParcelApprovalDto;
import io.ruv.parcel.parcel.api.ParcelStatus;
import io.ruv.parcel.parcelservice.service.ParcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ParcelBalanceProcessedListener {

    private final ObjectMapper objectMapper;
    private final ParcelService parcelService;

    @RabbitListener(queues = "${parcel.messaging.workflow.balance-processed.queue}")
    public void onParcelApprovalEvent(byte[] payload) {

        try {

            var approvalEvent = objectMapper.readValue(payload, ParcelApprovalDto.class);

            if (approvalEvent.isSuccess()) {

                parcelService.updateParcelStatus(approvalEvent.getParcelId(), ParcelStatus.ACCEPTED, null);
            } else {

                parcelService.updateParcelStatus(approvalEvent.getParcelId(), ParcelStatus.REJECTED, null);
            }
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }
}
