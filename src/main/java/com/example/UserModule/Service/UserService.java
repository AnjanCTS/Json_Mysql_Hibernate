package com.example.UserModule.Service;

import com.amazonaws.util.IOUtils;
import com.example.UserModule.Model.HpnsConfig;
import com.example.UserModule.Model.User;
import com.example.UserModule.Repository.HpnsRepository;
import com.example.UserModule.Repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    HpnsRepository hpnsRepository;

    public List<User> getAllUser(){
        return this.userRepository.findAll();
    }

    //Create new user
    public User addUser(User user) throws Exception {
        User local = this.userRepository.findByUserName(user.getUserName());
        if(local!=null){
            System.out.println("user is already there");
            throw new Exception("User already present");
        }else{
//           if(user.getUserRole()==null){
//               user.setUserRole("USER");
//           }
            local = this.userRepository.save(user);

        }
        return local;
    }


    public void insertJsonData() throws IOException {
        List<HpnsConfig> hpnsConfigs = new ArrayList<>();
        InputStream stream = getClass().getResourceAsStream("/config/HPNS_JSON.txt");
        List<String> lines = new ArrayList<>();
        if (stream != null) {
            String fileAsString = IOUtils.toString(stream);
            lines = new BufferedReader(new StringReader(fileAsString))
                    .lines().collect(Collectors.toList());
        }
        lines.stream().forEach(line -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                HpnsConfig hpnsConfig = objectMapper.readValue(line, HpnsConfig.class);
                hpnsConfigs.add(hpnsConfig);
            } catch (IOException e) {
                System.out.println("==> Error while mapping HpnsConfig | error={}"+ e.getMessage());
            }
        });

        hpnsRepository.insertHPNSDailyTable(hpnsConfigs);
    }

    public List<HpnsConfig>  readJsonData() throws JsonProcessingException {
        return hpnsRepository.readJsonData();

    }
}
