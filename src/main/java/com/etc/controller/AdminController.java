package com.etc.controller;

import com.etc.model.entity.Goods;
import com.etc.model.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @RequestMapping("admin")
    public String getGoods(Model model){
        model.addAttribute("list",adminService.findAll());
        return "admin";
    }

    @RequestMapping("removegood")
    public String removeGood(int gid){
        adminService.removeGood(gid);
        return "forward:/admin.html";
    }

    @RequestMapping("addgoods")
    public String addgoods(){
        return "addgoods";
    }

    @RequestMapping("doadd")
    public String doAdd(Goods goods,MultipartHttpServletRequest request) throws IOException {
        String pathRoot = request.getSession().getServletContext().getRealPath("");
        String path="";
        String realPath="";
        int index=1;
        for(MultipartFile mf : request.getFiles("file")) {
            if(!mf.isEmpty()) {
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                String contentType = mf.getContentType();
                String imageName = contentType.substring(contentType.indexOf("/")+1);
                path =uuid+"."+imageName;
                realPath = pathRoot+path;
                mf.transferTo(new File(realPath));
                goods.setGpicture(path);
            }
        }
        adminService.addGood(goods);
        return "forward:/admin.html";
    }

    @RequestMapping("editgoods")
    public String editGoods(int gid,Model model){
        Goods goods=adminService.selectGood(gid);
        model.addAttribute("good",goods);
        return "editgoods";
    }

    @RequestMapping("doedit")
    public String doEdit(Goods goods,MultipartHttpServletRequest request) throws IOException {
        String pathRoot = request.getSession().getServletContext().getRealPath("");
        String path="";
        String realPath="";
        int index=1;
        for(MultipartFile mf : request.getFiles("file")) {
            if(!mf.isEmpty()) {
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                String contentType = mf.getContentType();
                String imageName = contentType.substring(contentType.indexOf("/")+1);
                path =uuid+"."+imageName;
                realPath = pathRoot+path;
                mf.transferTo(new File(realPath));
                goods.setGpicture(path);
            }
        }
        adminService.editGood(goods);
        return "forward:/admin.html";
    }
}
