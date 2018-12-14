package ru.mirea;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class PetController {

    private PetService petService;

    @RequestMapping(value = "pets", method = GET)
    @ResponseBody
    public List<HashMap<String, String>> pets() {
        return petService.pets();
    }

    @Autowired
    public void setPetService (PetService petService) {
        this.petService = petService;
    }
}
