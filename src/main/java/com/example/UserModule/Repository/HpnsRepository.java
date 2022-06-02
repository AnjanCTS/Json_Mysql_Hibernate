package com.example.UserModule.Repository;

import com.example.UserModule.Model.Container;
import com.example.UserModule.Model.HpnsConfig;
import com.example.UserModule.Model.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class HpnsRepository {

    public final static String HPNS_JSON_INSERT_QUERY = "INSERT INTO HPNS_CONTAINER_JSON (ID, SERVICES) VALUES (:id,:services)";
    public final static String HPNS_JSON_SELECT_QUERY = "SELECT * FROM  HPNS_CONTAINER_JSON";
    @PersistenceContext
    private EntityManager entityManager;

    //Insert HPNS start
    public void insertHPNSDailyTable(List<HpnsConfig> hpnsConfig) {
        System.out.println("Entered insertHPNSDailyTable() method of HpnsRepository");
        System.out.println(HPNS_JSON_INSERT_QUERY);
        Query query = entityManager.createNativeQuery(HPNS_JSON_INSERT_QUERY);

        hpnsConfig.forEach(hpns -> {
            String ServiceJSON = null;
            try {
                ObjectMapper mapper = new ObjectMapper();
                ServiceJSON = mapper.writeValueAsString(hpns.getContainer().getServices());
            } catch (JsonProcessingException e) {
                System.out.println("Error converting service object into json string {}"+ e.getMessage());
            }
            System.out.println("Service in JSON :"+ServiceJSON);
            query.setParameter("id", hpns.getContainer().getId());
            query.setParameter("services", ServiceJSON);
            int rows = query.executeUpdate();
            System.out.println("ROW NUM : "+ rows);
        });
    }
//INSERT HPNS END

    //SELECT STARTS
    public List<HpnsConfig> readJsonData() throws JsonProcessingException {
        System.out.println("Entered readJsonData() method of HpnsRepository");
        System.out.println(HPNS_JSON_SELECT_QUERY);
        Query query = entityManager.createNativeQuery(HPNS_JSON_SELECT_QUERY);
        List<Object[]> resultList = query.getResultList();
        List<HpnsConfig> hpnsConfigs = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (Object[] obj : resultList){
            HpnsConfig hpns = new HpnsConfig();
            Container container = new Container();
            List<Service> service = mapper.readValue(obj[1].toString(), new TypeReference<List<Service>>(){});
            container.setId(obj[0].toString());
            container.setServices(service);
            hpns.setContainer(container);
            hpnsConfigs.add(hpns);
     }
        return hpnsConfigs;
    }
}
