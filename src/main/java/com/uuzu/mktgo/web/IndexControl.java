package com.uuzu.mktgo.web;

import com.uuzu.mktgo.mapper.PhoneMapper;
import com.uuzu.mktgo.pojo.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author zj_pc
 */
@Controller
public class IndexControl {
    @Autowired
    PhoneMapper phoneMapper;

    @GetMapping("/index")
    public ResponseEntity<List<Phone>> index() {
        try {
            return ResponseEntity.ok(phoneMapper.queryPhoneLimit10());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
