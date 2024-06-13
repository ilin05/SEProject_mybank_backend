package com.mybank.module4_creditcard.restapi.administrator;


import com.mybank.module4_creditcard.dao.impl.AuditorDaoImpl;
import com.mybank.module4_creditcard.entity.role.Auditor;
import com.mybank.module4_creditcard.entity.role.Authority;
import com.mybank.module4_creditcard.restapi.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class AdministratorController {
    AuditorDaoImpl impl;

    @Autowired
    public AdministratorController(AuditorDaoImpl impl) { this.impl = impl; }

    @PostMapping("/api/administrator/add-auditor")
    @ResponseBody
    public GeneralResponse addAuditor(@RequestBody Auditor auditor) {
        try {
            impl.createAuditor(auditor);
        } catch (NullPointerException e) {
            return new GeneralResponse(false,0,"null input: "+e.getMessage());
        } catch (NumberFormatException e) {
            return new GeneralResponse(false,1,"invalid auditor format: "+e.getMessage());
        } catch (DataAccessException e) {
            return new GeneralResponse(false,2, "cannot access data. maybe wrong information provided: "+e.getMessage());
        }
        return new GeneralResponse(0); // true
    }

    @DeleteMapping("/api/administrator/delete-auditor/{auditor_id}")
    @ResponseBody
    public GeneralResponse deleteAuditor(@PathVariable String auditor_id) {
        try {
            impl.deleteAuditor(auditor_id);
        } catch (Exception e) {
            return new GeneralResponse(false,0,"unexplained error: "+e.getMessage());
        }
        return new GeneralResponse(0); // true
    }

    @PutMapping("/api/administrator/grant-permission/{auditor_id}")
    @ResponseBody
    public GeneralResponse grantPermission(@PathVariable String auditor_id, @RequestBody Authority authority) {
        impl.grantAuditorPermission(auditor_id, authority);
        return new GeneralResponse(-1);
    }

    @PutMapping("/api/administrator/revoke-permission/{auditor_id}")
    @ResponseBody
    public GeneralResponse revokePermission(@PathVariable String auditor_id, @RequestBody Authority authority) {
        impl.revokeAuditorPermission(auditor_id, authority);
        return new GeneralResponse(-1);
    }

    @GetMapping("/api/administrator/auditors")
    @ResponseBody
    public GeneralResponse getAuditors() {
        return new GeneralResponse(impl.queryAuditors());
    }
}
