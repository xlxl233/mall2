package com.etc.controller;

import com.etc.model.entity.Cart;
import com.etc.model.entity.Goods;
import com.etc.model.entity.User;
import com.etc.model.service.CartService;
import com.etc.model.service.GoodsService;
import com.etc.model.vo.CartVO;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public String showGoods(Model model, HttpSession session){
        model.addAttribute("list",goodsService.findAll());
        model.addAttribute("recomendlist",goodsService.getRecomendList((Integer)session.getAttribute("uid")));
        return "shop";
    }

    @RequestMapping("getrecommend")
    public String Test(){
        goodsService.readRelationFile("F:\\test.txt");
        return "forward:/admin.html";
    }

    @RequestMapping("specificgood")
    public String specificGood(int gid,Model model){
        Goods good = goodsService.findByGid(gid);
        model.addAttribute("good",good);
        return "single";
    }

    @RequestMapping(value = "addcart",method = RequestMethod.POST)
    public String addCart(HttpServletRequest request,int gid, int ccount){
        int uid = (Integer) request.getSession().getAttribute("uid");
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        if(goodsService.selectCartByUidAndGid(uid,gid).isEmpty()){
            Cart cart = new Cart(uid,gid,ccount,time);
            goodsService.insertCart(cart);
        }else{
            goodsService.updateCcountByUidAndGid(uid,gid,ccount);
        }
        return "forward:/showgoods.html";
    }

    @RequestMapping(value = "showsearchgoods",method = RequestMethod.POST)
    public String showSearchGoods(String string, Model model){
        List<Goods> list = goodsService.findByLike(string);
        model.addAttribute("list",list);
        return "shop";
    }

    @RequestMapping("checkgcount")
    @ResponseBody
    public boolean  checkGcount(int num,int gid){
        return goodsService.checkGcount(num,gid);
    }
}
