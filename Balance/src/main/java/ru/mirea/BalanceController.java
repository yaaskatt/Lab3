package ru.mirea;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Controller
public class BalanceController {

    private BalanceService balanceService;

    @RequestMapping(value = "balance/{id}", method = GET)
    @ResponseBody
    public List balance(@PathVariable int id) {
        return balanceService.balance(id);
    }

    @RequestMapping(value = "balance/{id}={balance}", method = PUT)
    @ResponseBody
    public void putNewBal(@PathVariable int id, @PathVariable double balance) {
        balanceService.putNewBal(id, balance);
    }

    @Autowired
    public void setBalanceService (BalanceService balanceService) {
        this.balanceService = balanceService;
    }
}
