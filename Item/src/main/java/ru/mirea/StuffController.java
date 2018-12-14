package ru.mirea;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class StuffController {

    @Autowired
    private StuffService stuffService;

    @RequestMapping(value = "stuff", method = GET)
    @ResponseBody
    public List<HashMap<String, String>> stuff() {
        return stuffService.stuff();
    }

    @Autowired
    public void setStuffService (StuffService stuffService) {
        this.stuffService = stuffService;
    }
}