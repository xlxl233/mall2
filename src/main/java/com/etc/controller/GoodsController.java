package com.etc.controller;

import com.etc.model.entity.Cart;
import com.etc.model.entity.Goods;
import com.etc.model.entity.User;
import com.etc.model.service.CartService;
import com.etc.model.service.GoodsService;
import com.etc.model.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RequestMapping("/")
@Controller
public class GoodsController {
    private GoodsService goodsService;

    private CartService cartService;

    public CartService getCartService() {
        return cartService;
    }

    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    public GoodsService getGoodsService() {
        return goodsService;
    }

    @Autowired
    public void setGoodsService(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @RequestMapping("showgoods")
    public String showGoods(Model model){
        model.addAttribute("list",goodsService.findAll());
        return "shop";
    }

    @RequestMapping("specificgood")
    public String specificGood(int gid,Model model){
        Goods good = goodsService.findByGid(gid);
        model.addAttribute("good",good);
        return "single";
    }

    @RequestMapping(value = "addcart",method = RequestMethod.POST)
    public String addCart(int gid, int ccount){
//        User user = (User)request.getSession().getAttribute("user");
//        int uid = user.getUid();
        int temp = 1;
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        if(goodsService.selectCartByUidAndGid(temp,gid)==0){
            Cart cart = new Cart(temp,gid,ccount,time);
            goodsService.insertCart(cart);
        }else{
            goodsService.updateCcountByUidAndGid(temp,gid,ccount);
        }
        return "forward:/showgoods.html";
    }
}
