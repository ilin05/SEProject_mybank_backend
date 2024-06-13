package com.mybank.module5_foreign.Controller;

import com.mybank.module5_foreign.back.WHCommonFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/operators")
public class WHOPController {
    @Autowired
    private WHCommonFunctions commonFunctions;

    @GetMapping
    public ResponseEntity<?> getAllOperators() {
        try {
            return ResponseEntity.ok(commonFunctions.getAllOperators());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error fetching operators: " + e.getMessage()));
        }
    }


    @PostMapping
    public ResponseEntity<?> addOperator(@RequestBody OperatorRequest request) {
        try {
            boolean success = commonFunctions.addOP(request.getOp_name(), request.getOp_password());
            if (!success) {
                throw new Exception("Failed to add operator due to database constraints.");
            }
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to add operator: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{opt_name}")
    public ResponseEntity<?> deleteOperator(@PathVariable String opt_name) {
        try {
            boolean success = commonFunctions.deleteOP(opt_name);
            if (!success) {
                throw new Exception("Failed to delete operator because it does not exist.");
            }
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to delete operator: " + e.getMessage()));
        }
    }

    @PostMapping("/changeAuthority")
    public ResponseEntity<?> changeAuthority(@RequestBody AuthorityChangeRequest request) {
        try {
            boolean success = commonFunctions.changeAuthority(request.getOp_name(), request.getControl_currency(), request.getControl_rate());
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

}

class OperatorRequest {
    private String op_name;
    private String op_password;

    public String getOp_name() { return op_name; }
    public void setOp_name(String op_name) { this.op_name = op_name; }
    public String getOp_password() { return op_password; }
    public void setOp_password(String op_password) { this.op_password = op_password; }
}

class AuthorityChangeRequest {
    private String op_name;
    private Boolean control_currency;
    private Boolean control_rate;

    public Boolean getControl_currency() {
        return control_currency;
    }

    public void setControl_currency(Boolean control_currency) {
        this.control_currency = control_currency;
    }

    public Boolean getControl_rate() {
        return control_rate;
    }

    public void setControl_rate(Boolean control_rate) {
        this.control_rate = control_rate;
    }

    public String getOp_name() {
        return op_name;
    }

    public void setOp_name(String op_name) {
        this.op_name = op_name;
    }

    // getters and setters
}

