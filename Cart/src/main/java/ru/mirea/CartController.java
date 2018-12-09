package ru.mirea;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
public class CartController {

    private CartService cartService;

    @RequestMapping(value = "cart/{id}", method = GET)
    @ResponseBody
    public List get(@PathVariable int id) {
        return cartService.cart(id);
    }

    @RequestMapping(value = "cart/pets/{userId}", method = GET)
    @ResponseBody
    public List getPets(@PathVariable int userId) {
        return cartService.getPets(userId);
    }

    @RequestMapping(value = "cart/stuff/{userId}", method = GET)
    @ResponseBody
    public List getStuff(@PathVariable int userId) {
        return cartService.getStuff(userId);
    }

    @RequestMapping(value = "cart/{userId}/{itemId}", method = PUT)
    @ResponseBody
    public void put(@PathVariable int userId, @PathVariable int itemId) {
        cartService.put(userId, itemId);
    }

    @RequestMapping(value = "cart/{userId}/{itemId}", method = DELETE)
    @ResponseBody
    public void delete(@PathVariable int userId, @PathVariable int itemId) {
        cartService.delete(userId, itemId);
    }

    @RequestMapping(value = "cart/{userId}", method = POST)
    @ResponseBody
    public void post(@PathVariable int userId) {
        cartService.post(userId);
    }

    @Autowired
    public void setCartService (CartService cartService) {
        this.cartService = cartService;
    }

}
