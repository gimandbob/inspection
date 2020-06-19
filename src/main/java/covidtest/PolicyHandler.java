package covidtest;

import covidtest.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{

    @Autowired
    InspectionRepository inspectionRepository;
    
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDiagnosed_ResultUpdate(@Payload Diagnosed diagnosed) throws Exception {

        if(diagnosed.isMe()){
            Inspection inspection = inspectionRepository.findById(diagnosed.getInspectionId())
                    .orElseThrow(() -> new Exception("inspection not found"));
            inspection.setStatus("DIAGNOSED");
            inspection.setResult(diagnosed.getResult());
            inspectionRepository.save(inspection);
            System.out.println("##### listener ResultUpdate : " + diagnosed.toJson());
        }
    }

}
