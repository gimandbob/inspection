package covidtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InspectionService {
    @Autowired
    InspectionRepository inspectionRepository;

    public void inspectionCancel(Long id) throws Exception {
        Inspection inspection = inspectionRepository.findById(id)
                .orElseThrow(() -> new Exception());
        inspection.setStatus("CANCELLED");
        inspectionRepository.save(inspection);
    }
}
