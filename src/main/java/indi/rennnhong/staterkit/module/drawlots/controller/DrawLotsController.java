package indi.rennnhong.staterkit.module.drawlots.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.text.MessageFormat;

@Controller
@RequestMapping("/drawlots")
public class DrawLotsController {

    @GetMapping
    public String showDrawLots() {
        return "page/drawlots";
    }
}
