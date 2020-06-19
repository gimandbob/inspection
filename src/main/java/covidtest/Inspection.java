package covidtest;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;


@Entity
@Table(name="Inspection_table")
public class Inspection {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String patientName;
    private Long kitId;
    private String status;
    private String result;

    @PostPersist
    public void onPostPersist(){
        Inspected inspected = new Inspected();
        BeanUtils.copyProperties(this, inspected);
        inspected.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        covidtest.external.Send send = new covidtest.external.Send();
        send.setInspectionId(getId());
        send.setKitId(getKitId());
        send.setStatus("SENT");
        Application.applicationContext.getBean(covidtest.external.SendService.class)
                .send(send);


//        if(this.getStatus().equals("CANCELLED")){
//            if(!this.getResult().equals("UNKNOWN")){
//                new Exception("진단이 완료된 검사입니다.");
//            }
//            InspectCancelled inspectCancelled = new InspectCancelled();
//            BeanUtils.copyProperties(this, inspectCancelled);
//            inspectCancelled.publishAfterCommit();
//        } else{
//            covidtest.external.Send send = new covidtest.external.Send();
//            send.setInspectionId(getId());
//            send.setKitId(getKitId());
//            send.setStatus("SENT");
//            Application.applicationContext.getBean(covidtest.external.SendService.class)
//                    .send(send);
//        }

    }

    @PostUpdate
    public void onPostUpdate(){
//        Inspected inspected = new Inspected();
//        BeanUtils.copyProperties(this, inspected);
//        inspected.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        if(this.getStatus().equals("CANCELLED")) {
            if (this.getResult().equals("UNKNOWN")) {
                this.setStatus("CANCELLED");
                InspectCancelled inspectCancelled = new InspectCancelled();
                BeanUtils.copyProperties(this, inspectCancelled);
                inspectCancelled.publishAfterCommit();
            } else{
                System.out.println("진단이 완료되어 검사를 취소할 수 없습니다.");
            }
        }

//        if(this.getStatus().equals("CANCELLED")){
//            if(!this.getResult().equals("UNKNOWN")){
//                new Exception("진단이 완료된 검사입니다.");
//            }
//            InspectCancelled inspectCancelled = new InspectCancelled();
//            BeanUtils.copyProperties(this, inspectCancelled);
//            inspectCancelled.publishAfterCommit();
//        } else{
//            covidtest.external.Send send = new covidtest.external.Send();
//            send.setInspectionId(getId());
//            send.setKitId(getKitId());
//            send.setStatus("SENT");
//            Application.applicationContext.getBean(covidtest.external.SendService.class)
//                    .send(send);
//        }

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
    public Long getKitId() {
        return kitId;
    }

    public void setKitId(Long kitId) {
        this.kitId = kitId;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }




}
