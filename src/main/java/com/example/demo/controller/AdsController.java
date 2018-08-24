package com.example.demo.controller;

import com.example.demo.model.Ads;
import com.example.demo.repository.AdsRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/ads")
public class AdsController {
    @Autowired
    private AdsRepository adsRepository;

    @Value("${AdsApp.ads.pic_url}")
    private String adsPicDir;

    @PostMapping("/add")
    public String add(@ModelAttribute Ads ads, @RequestParam("image") MultipartFile multipartFile) {
        File dir = new File(adsPicDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
        try {
            multipartFile.transferTo(new File(dir, picName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ads.setPicUrl(picName);
        ads.setCreatedDate(new Date());
        adsRepository.save(ads);
        return "redirect:/admin";
    }

    @GetMapping("/getAdByCategory/{category_id}")
    public String getAdByCategory(@PathVariable("category_id") String id, ModelMap modelMap) {
        Iterable<Long> longIds = convertToLong(id);
        List<Ads> adsList = adsRepository.findAllById(longIds);
        modelMap.addAttribute("ads", adsList);
        System.out.println("id: " + longIds);
        System.out.println("adslist: " + adsList.size());
        return "redirect:/";
    }

    public Iterable<Long> convertToLong(String ids) {
        String[] idArray = ids.split(",");
        List<Long> idsAsLong = new ArrayList<>();
        for (int i = 0; i < idArray.length; i++) {
            idsAsLong.add(Long.parseLong(idArray[i]));
        }
        return idsAsLong;
    }

    @GetMapping(value = "/adImage")
    public @ResponseBody
    byte[] getAdImage(@RequestParam("picUrl") String picUrl) throws IOException {
        InputStream inputStream = null;
        if (picUrl.equals("default")) {
            inputStream = new FileInputStream("/home/artur/Desktop/AdsApp/src/main/resources/static/img/default.jpg");
        } else {
            inputStream = new FileInputStream(adsPicDir + picUrl);
        }

        return IOUtils.toByteArray(inputStream);
    }
}
