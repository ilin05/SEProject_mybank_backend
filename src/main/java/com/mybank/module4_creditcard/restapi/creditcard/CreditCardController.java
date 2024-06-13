package com.mybank.module4_creditcard.restapi.creditcard;

import com.mybank.module4_creditcard.dao.impl.CreditCardDaoImpl;
import com.mybank.module4_creditcard.entity.CreditCard;
import com.mybank.module4_creditcard.restapi.GeneralResponse;
import com.mybank.module4_creditcard.restapi.PasswordPairs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
public class CreditCardController {

    CreditCardDaoImpl impl;

    @Autowired
    public CreditCardController(CreditCardDaoImpl impl) { this.impl = impl; }

    @GetMapping("/api/card/list/{user_id}")
    @ResponseBody
    public GeneralResponse getCardList(@PathVariable String user_id) {
        List<CreditCard> res = impl.queryCreditCards(user_id);
        if(!res.isEmpty())
            return new GeneralResponse(res);
        else
            return new GeneralResponse(false,-1,"undetermined");
    }

    @GetMapping("/api/card/detail/{card_id}")
    @ResponseBody
    public GeneralResponse getCardDetail(@PathVariable String card_id) {
        CreditCard res = impl.queryCreditCard(card_id);
        if(res == null)
            return new GeneralResponse(false,-1,"undetermined");
        else
            return new GeneralResponse(res);
    }

    @PutMapping("/api/card/update/{card_id}")
    @ResponseBody
    public GeneralResponse updateCard(@PathVariable String card_id, @RequestBody CreditCard card) {
        Objects.requireNonNull(card_id);
        Objects.requireNonNull(card);
        CreditCard old_card = impl.queryCreditCard(card_id);
        if(old_card == null)
            return new GeneralResponse(false,0,"no such card");
        if(!old_card.getStatus().equals(card.getStatus()) && card.getStatus()!=null)
            impl.updateCardStatus(card_id, card.getStatus());

        return new GeneralResponse(0);
    }

    @PutMapping("/api/card/password/{card_id}")
    @ResponseBody
    public GeneralResponse setPassword(@PathVariable String card_id, @RequestBody PasswordPairs pair) {
        String old_password = pair.getOldPassword();
        String new_password = pair.getNewPassword();

        boolean res;
        try{
            res = impl.updateCardPassword(card_id,old_password,new_password);
        } catch (Exception e){
            return new GeneralResponse(false,0,"cannot update password: " + e.getMessage());
        }
        return new GeneralResponse(res, 0, "");
    }

}
