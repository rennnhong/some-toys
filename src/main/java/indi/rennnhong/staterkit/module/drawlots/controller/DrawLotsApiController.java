package indi.rennnhong.staterkit.module.drawlots.controller;

import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

@RestController
@RequestMapping("api/drawlots")
public class DrawLotsApiController {

    @GetMapping("numbers")
    public String[] getBase64Numbers() {
        String[] numbers = new String[10];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = getBase64NumberImage(i);
        }
        return numbers;
    }

    @GetMapping("numbers/{number}")
    public String getBase64Numbers(@PathVariable("number") Integer number) {
        String base64NumberImage = getBase64NumberImage(number);
        return base64NumberImage;
    }

    private String getBase64NumberImage(Integer number) {

//        String imgFile = MessageFormat.format("static/img/number_{0}.png", Integer.toString(number));//待處理的圖片
        InputStream in = null;
        byte[] data = null;
        //讀取圖片位元組陣列
        try {
//                in = new FileInputStream(file);
            in = ResourceUtils.getURL(MessageFormat.format("classpath:static/img/number_{0}.png", Integer.toString(number))).openStream();
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //對位元組陣列Base64編碼
        BASE64Encoder encoder = new BASE64Encoder();
        String encode = encoder.encode(data);
//        System.out.println(encode);
        return encode;//返回Base64編碼過的位元組陣列字串
    }
}

